/*
package com.cxqm.xiaoerke.modules.umbrella.web;


import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.modules.account.entity.PayRecord;
import com.cxqm.xiaoerke.modules.account.service.PayRecordService;
//PayRecordServiceimport com.cxqm.xiaoerke.modules.alipay.service.AlipayService;
//import com.cxqm.xiaoerke.modules.alipay.util.AlipayNotify;
//import com.cxqm.xiaoerke.modules.alipay.util.httpClient.HttpsUtil;
//import com.cxqm.xiaoerke.modules.alipay.util.httpClient.StringUtil;
import com.cxqm.xiaoerke.modules.healthRecords.service.HealthRecordsService;
import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.entity.ValidateBean;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.cxqm.xiaoerke.modules.alipay.service.AlipayService;

*/
/**@author guozengguang
 * @version 16/7/6
 * 非微信环境下的保护伞
 *//*

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

*/
/*
    @Autowired
    private AlipayService alipayService;
*//*


    @Autowired
    private PayRecordService payRecordService;

    */
/**
     *获取保护伞首页信息-已有多少人加入互助计划
    *//*

    @RequestMapping(value = "/firstPageMutualHelpCount", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody Map<String, Object> firstPageData() {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        Map<String, Object> map=new HashMap<String, Object>();
        Integer count = babyUmbrellaInfoSerivce.getBabyUmbrellaInfoTotal(map);
        map.put("mutualHelpCount",count+2000);
        return map;
    }

    */
/**
     *根据用户输入的手机号判断该用户是否已经购买宝护伞以及是否关注平台
     *//*

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

    */
/**
     * 第三方加入保护伞
     * 在信息页点击下一步按钮触发
     *//*

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
        babyUmbrellaInfo.setCreateTime(new Date());
        babyUmbrellaInfoSerivce.newSaveBabyUmbrellaInfo(babyUmbrellaInfo);

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

        result.put("umbrellaid",babyUmbrellaInfo.getId());
        result.put("userId",user.getId());
        return result;
    }

    */
/**
     * 获取用户专属临时二维码
     *//*

    @RequestMapping(value = "/getUserQRCode", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  getUserQRCode(@RequestBody Map<String, Object> params) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);
        StringBuffer sbf = new StringBuffer();
        //二维码规则:99(渠道标识)+宝护伞id的后八位.
        sbf.append("99").append(params.get("umbrellaid").toString().substring(2));
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

    */
/**
     * 非微信平台(第三方)支付宝支付
     * @author guozengguang
     *//*

    //@RequestMapping(value = "/alipayment", method = {RequestMethod.POST, RequestMethod.GET})
    //public
    //String  alipayment(@RequestBody Map<String, Object> params,HttpServletResponse response) {
    //    DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
    //    String totleFee = params.get("totleFee").toString();//支付金额
    //    String body = params.get("body").toString();//可以为空
    //    String describe = params.get("describe").toString();//商品描述
    //    String showUrl = params.get("showUrl").toString();//商品展示地址
    //    String out_trade_no = params.get("umbrellaid").toString();//交易订单号,这里用宝护伞id
    //    String userId = params.get("userId").toString();//保存宝护伞信息时带过去的userId
    //
    //    //先在数据库里生成一份订单数据(account_pay_record表)
    //    PayRecord payRecord = new PayRecord();
    //    payRecord.setId(IdGen.uuid());
    //    payRecord.setAmount(Float.parseFloat(totleFee)*100);
    //    payRecord.setCreatedBy(userId);
    //    payRecord.setUserId(userId);
    //    payRecord.setPayType("zfb");
    //    payRecord.setStatus("wait");
    //    payRecord.setPayDate(new Date());
    //    payRecord.setOrderId(out_trade_no);
    //    payRecord.setFeeType("umbrellaApp");
    //    payRecordService.insertPayInfo(payRecord);
    //
    //
    //    //调用支付
    //    String result = alipayService.alipayment(totleFee, body, describe, showUrl,out_trade_no);
    //    try {
    //        StringUtil.writeToWeb(result, "html", response);
    //    } catch (IOException e) {
    //        e.printStackTrace();
    //    }
    //    return null;
    //}

    @RequestMapping(value = "/alipayment", method = {RequestMethod.POST, RequestMethod.GET})
    public
    String  alipayment(HttpServletRequest request,HttpServletResponse response) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
        String out_trade_no = request.getParameter("WIDout_trade_no");
        String body = request.getParameter("WIDsubject");
        String totleFee = request.getParameter("WIDtotal_fee");
        String showUrl = request.getParameter("WIDshow_url");
        String describe = request.getParameter("WIDbody");

        //String totleFee = params.get("totleFee").toString();//支付金额
        //String body = params.get("body").toString();//可以为空
        //String describe = params.get("describe").toString();//商品描述
        //String showUrl = params.get("showUrl").toString();//商品展示地址
        //String out_trade_no = params.get("umbrellaid").toString();//交易订单号,这里用宝护伞id
        //String userId = params.get("userId").toString();//保存宝护伞信息时带过去的userId

        //先在数据库里生成一份订单数据(account_pay_record表)
        //PayRecord payRecord = new PayRecord();
        //payRecord.setId(IdGen.uuid());
        //payRecord.setAmount(Float.parseFloat(totleFee)*100);
        //payRecord.setCreatedBy(userId);
        //payRecord.setUserId(userId);
        //payRecord.setPayType("zfb");
        //payRecord.setStatus("wait");
        //payRecord.setPayDate(new Date());
        //payRecord.setOrderId(out_trade_no);
        //payRecord.setFeeType("umbrellaApp");
        //payRecordService.insertPayInfo(payRecord);


    */
/*    //调用支付
        String result = alipayService.alipayment(totleFee, body, describe, showUrl,out_trade_no);
        try {
            StringUtil.writeToWeb(result, "html", response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }*//*


    */
/**
     * 支付宝异步返回通知(支付结果信息)
     * @author guozengguang
     *//*

    @RequestMapping(value = "/notification",method = {RequestMethod.POST})
    public String notification(HttpServletRequest request, HttpServletResponse response) {
        // 获取请求数据
        String reqText = null;
        Map<String,String> map = new HashMap<String, String>();
        try {
            reqText = HttpsUtil.getInfoFromRequest(request);
            String[] paramArr = reqText.split("&");
            for (int i = 0; i <paramArr.length ; i++) {
                String[] arr = paramArr[i].split("=");
                map.put(arr[0],arr[1]);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String trade_status = map.get("trade_status").toString();
        if(AlipayNotify.verify(map)){//验证成功
            if("TRADE_FINISHED".equals(trade_status) || "TRADE_SUCCESS".equals(trade_status)) {
                //支付成功,更改宝护伞的支付状态pay_result,更新支付结果表(account_pay_record)
                PayRecord payRecord = new PayRecord();
                Date receiveDate = DateUtils.StrToDate(map.get("notify_time").toString(),"datetime");
                payRecord.setStatus("success");
                payRecord.setOrderId(map.get("out_trade_no").toString());
                payRecord.setReceiveDate(receiveDate);
                payRecordService.updatePayRecordByOrderId(payRecord);
            }
            return "success";
        }else{//验证失败
            return "fail";
        }
    }


    */
/**
     * 根据宝护伞id更新支付状态pay_result
     * 说明:只有支付成功时才调用该接口,保存信息的时候默认该状态为fail;
     * @author guozengguang
     *//*

    @RequestMapping(value = "/updateBabyUmbrellaInfoById", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  updateBabyUmbrellaInfoById(@RequestBody Map<String, Object> params) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
        String umbrellaid = params.get("umbrellaid").toString();
        BabyUmbrellaInfo babyUmbrellaInfo = new BabyUmbrellaInfo();
        babyUmbrellaInfo.setId(Integer.valueOf(umbrellaid));
        babyUmbrellaInfo.setPayResult("success");
        int result = babyUmbrellaInfoSerivce.updateBabyUmbrellaInfoById(babyUmbrellaInfo);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("result",result);
        return resultMap;
    }

}
*/
