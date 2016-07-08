package com.cxqm.xiaoerke.modules.umbrella.web;


import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.modules.healthRecords.service.HealthRecordsService;
import com.cxqm.xiaoerke.modules.sys.entity.*;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.service.UtilService;
import com.cxqm.xiaoerke.modules.umbrella.entity.BabyUmbrellaInfo;
import com.cxqm.xiaoerke.modules.umbrella.entity.UmbrellaFamilyInfo;
import com.cxqm.xiaoerke.modules.umbrella.service.BabyUmbrellaInfoService;
import com.cxqm.xiaoerke.modules.umbrella.service.BabyUmbrellaInfoThirdPartyService;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**@author guozengguang
 * @version 16/7/6
 * 非微信环境下的保护伞
 */
@Controller
@RequestMapping(value = "umbrella/thirdParty")
public class UmbrellaThirdPartyController  {

    @Autowired
    private BabyUmbrellaInfoService babyUmbrellaInfoSerivce;

    @Autowired
    private BabyUmbrellaInfoThirdPartyService babyUmbrellaInfoThirdPartyService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private WechatAttentionService wechatAttentionService;

    @Autowired
    private UtilService utilService;

    @Autowired
    private HealthRecordsService healthRecordsService;

    /**
     *获取保护伞首页信息-已有多少人加入互助计划
     */
    @RequestMapping(value = "/firstPageMutualHelpCount", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody Map<String, Object> firstPageData() {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        Map<String, Object> map=new HashMap<String, Object>();
        Integer count = babyUmbrellaInfoSerivce.getBabyUmbrellaInfoTotal(map);
        map.put("mutualHelpCount",count+2000);
        return map;
    }

    /**
     *根据用户输入的手机号判断该用户是否已经购买宝护伞以及是否关注平台
     */
    @RequestMapping(value = "/checkIsBuyAndAttention", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> checkIsBuyAndAttention(@RequestBody Map<String, Object> params) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        String userPhone = (String) params.get("userPhone");
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("userPhone",userPhone);
        Map<String,Object> result = new HashMap<String, Object>();
        boolean flag = false;
        Map<String,Object> umbreMap = null;
        //根据手机号查询该用户是否购买
        List<Map<String, Object>> list = babyUmbrellaInfoThirdPartyService.getIfBuyUmbrellaByOpenidOrPhone(map);
        if (list != null && list.size() > 0) {
            flag = true;
            //根据手机号查询宝护伞id
            umbreMap = list.get(0);
        }
        //根据手机号查询该用户是否已经关注平台
        Map<String,Object> statusMap = babyUmbrellaInfoThirdPartyService.getStatusByPhone(map);
        if(flag){
            //表示已经购买
            if(statusMap!=null){
                String status = (String)statusMap.get("status");
                if("0".equals(status)){
                    result.put("result","2");
                    return result;
                }else{
                    result.put("result","1");
                    result.put("umbrellaid",umbreMap.get("id"));
                    return result;
                }
            }else{
                result.put("result","1");
                result.put("umbrellaid",umbreMap.get("id"));
                return result;
            }
        }
        //未购买
        Map<String, Object> codeMap = utilService.sendIdentifying(userPhone);
        result.putAll(codeMap);
        result.put("result", "0");
        return result;
    }

    /**
     * 第三方加入保护伞
     * 在信息页点击下一步按钮触发
     */
    @RequestMapping(value = "/joinUs", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  newJoinUs(@RequestBody Map<String, Object> params,HttpServletRequest request,HttpSession session) throws UnsupportedEncodingException {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        Map<String, Object> result=new HashMap<String, Object>();
        String phone = params.get("phone").toString();
        String userCode=params.get("code").toString();
        ValidateBean validateBean = babyUmbrellaInfoThirdPartyService.getIdentifying(phone);
        String code = validateBean.getCode();
        if (null == validateBean) {
            result.put("result","0");
            return result;
        }
        Date date = validateBean.getCreateTime();
        boolean flag = (date.getTime() + 1000 * 60) > new Date().getTime();
        if (validateBean != null && flag && userCode.equals(code)) {
            //code有效，根据用户的手机号（切记，目前手机号，都是用户user表中的login_name）
            System.out.println(userCode + "|||||||||||||" + code);
            result.put("result","1");
        } else {
            result.put("result","0");
            return result;
        }

        //創建用戶
        User user = babyUmbrellaInfoThirdPartyService.saveUserInfo(phone);

        BabyBaseInfoVo vo = new BabyBaseInfoVo();
        vo.setSex(params.get("sex").toString());
        vo.setBirthday(this.toDate(params.get("birthDay").toString()));
        vo.setName(URLDecoder.decode(params.get("name").toString(), "utf-8"));
        vo.setUserid(user.getId());
        int babyResult = healthRecordsService.insertBabyInfo(vo);

        //添加保护伞初始信息
        BabyUmbrellaInfo babyUmbrellaInfo=new BabyUmbrellaInfo();
        babyUmbrellaInfo.setBabyId(vo.getId().toString());
        babyUmbrellaInfo.setParentIdCard(params.get("idCard").toString());
        babyUmbrellaInfo.setParentPhone(params.get("phone").toString());
        babyUmbrellaInfo.setParentName(URLDecoder.decode(params.get("parentName").toString(), "UTF-8"));
        babyUmbrellaInfo.setParentType(Integer.parseInt(params.get("parentType").toString()));
        babyUmbrellaInfo.setMoney("5");
        babyUmbrellaInfo.setUmberllaMoney(200000);
        babyUmbrellaInfo.setPayResult("fail");
        babyUmbrellaInfo.setVersion("a");
        babyUmbrellaInfo.setTruePayMoneys("5");


        //插入家庭成员的信息
        //宝爸宝妈
        UmbrellaFamilyInfo familyInfo = new UmbrellaFamilyInfo();
        familyInfo.setName(URLDecoder.decode(params.get("parentName").toString(), "UTF-8"));
        familyInfo.setUmbrellaId(babyUmbrellaInfo.getId());
        familyInfo.setSex(Integer.parseInt(params.get("parentType").toString()));
        String idCard = params.get("idCard").toString();
        Date birthDay = DateUtils.StrToDate(idCard.substring(6, 14), "yyyyMMdd");
        familyInfo.setBirthday(birthDay);
        int res = babyUmbrellaInfoSerivce.saveFamilyUmbrellaInfo(familyInfo);

        result.put("status",res);
        return result;
    }

    /**
     * 获取用户专属临时二维码
     */
    @RequestMapping(value = "/getUserQRCode", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  getUserQRCode(@RequestBody Map<String, Object> params) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);
        StringBuffer sbf = new StringBuffer();
        //二维码规则:99(渠道标识)+宝护伞id的后八位.
        sbf.append("99").append(params.get("umbrellaid").toString().substring(1));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("qrcode",babyUmbrellaInfoSerivce.getUserQRCode(sbf.toString()));
        return result;
    }

    public Date toDate(String date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
