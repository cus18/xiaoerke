package com.cxqm.xiaoerke.modules.umbrella.web;


import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.HttpRequestUtil;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;
import com.cxqm.xiaoerke.modules.sys.entity.SwitchConfigure;
import com.cxqm.xiaoerke.modules.sys.entity.WechatBean;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.service.UtilService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import com.cxqm.xiaoerke.modules.sys.utils.WechatMessageUtil;
import com.cxqm.xiaoerke.modules.umbrella.entity.BabyUmbrellaInfo;
import com.cxqm.xiaoerke.modules.umbrella.entity.UmbrellaFamilyInfo;
import com.cxqm.xiaoerke.modules.umbrella.service.BabyUmbrellaInfoService;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Controller
@RequestMapping(value = "umbrella")
public class UmbrellaController  {

    @Autowired
    private BabyUmbrellaInfoService babyUmbrellaInfoSerivce;

    @Autowired
    private SystemService systemService;

    @Autowired
    private WechatAttentionService wechatAttentionService;

    @Autowired
    private UtilService utilService;

    private static ExecutorService threadExecutor = Executors.newSingleThreadExecutor();

    /**
     *获取保护伞首页信息
     */
    @RequestMapping(value = "/firstPageDataCount", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  firstPageData() {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        Map<String, Object> map=new HashMap<String, Object>();
        Integer count = babyUmbrellaInfoSerivce.getBabyUmbrellaInfoTotal(map);
        map.put("count",count+2000);
        return map;
    }

    /**
     *获取保护伞首页信息
     */
    @RequestMapping(value = "/firstPageDataTodayCount", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  firstPageDataTodayCount() {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        Map<String, Object> map=new HashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        map.put("today",sdf.format(new Date()));
        Integer todayCount = babyUmbrellaInfoSerivce.getBabyUmbrellaInfoTotal(map);
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> countmap = new HashMap<String, Object>();
        countmap.put("date",new Date());
        Map<String,Object> maps = babyUmbrellaInfoSerivce.getUmbrellaNum(countmap);
        Long familyNum = (Long)maps.get("familyNum");
        result.put("todayCount", todayCount*2+familyNum);
        return result;
    }

    /**
     *获取保护伞首页信息
     */
    @RequestMapping(value = "/firstPageDataTotalUmbrellaMoney", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  firstPageDataTotalUmbrellaMoney() {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        Map<String, Object> map=new HashMap<String, Object>();
        Integer totalUmbrellaMoney = babyUmbrellaInfoSerivce.getTotalBabyUmbrellaInfoMoney(map);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        map.put("today",sdf.format(new Date()));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("totalUmbrellaMoney", totalUmbrellaMoney);
        return result;
    }


    /**
     * 加入保护伞
     */
    @RequestMapping(value = "/joinUs", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  joinUs(@RequestBody Map<String, Object> params,HttpServletRequest request,HttpSession session) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        Map<String, Object> map=new HashMap<String, Object>();
        Map<String, Object> numm=new HashMap<String, Object>();
        String openid = WechatUtil.getOpenId(session, request);
        map.put("openid",openid);
        List<Map<String, Object>> list = babyUmbrellaInfoSerivce.getBabyUmbrellaInfo(map);
        if(list.size()>0){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Map<String, Object> m = list.get(0);
            if(m.get("baby_id")!=null&&!m.get("baby_id").equals("")){
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("result",3);
                result.put("umbrella",m);
                numm.put("userNums","1");
                numm.put("date",sdf.format(new Date()));
                return result;
            }
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("result", 2);
            result.put("umbrella", m);

            numm.put("date",sdf.format(new Date()));
            numm.put("userNums","1");
            return result;
        }
        BabyUmbrellaInfo babyUmbrellaInfo=new BabyUmbrellaInfo();
        babyUmbrellaInfo.setOpenid(openid);
        babyUmbrellaInfo.setUmberllaMoney(200000);
        babyUmbrellaInfo.setTruePayMoneys(5+"");
        Integer res = babyUmbrellaInfoSerivce.saveBabyUmbrellaInfo(babyUmbrellaInfo);
        String shareId=params.get("shareId").toString();
        sendUBWechatMessage(openid, shareId);
        Map<String, Object> result=new HashMap<String, Object>();
        result.put("result",res);
        result.put("id",babyUmbrellaInfo.getId());
        result.put("umbrella","");
        return result;
    }

    private void sendUBWechatMessage(String toOpenId, String fromId){
        Map<String, Object> param = new HashMap<String, Object>();
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        if(StringUtils.isNotNull(fromId)){
            param.put("id",fromId);
            list = babyUmbrellaInfoSerivce.getBabyUmbrellaInfo(param);
        }
        Map parameter = systemService.getWechatParameter();
        String token = (String)parameter.get("token");
        if(list.size()!=0){
            String fromOpenId = (String)list.get(0).get("openid");//分享者openid
            String babyId = (String)list.get(0).get("baby_id");
            int oldUmbrellaMoney = (Integer) list.get(0).get("umbrella_money");
            int newUmbrellaMoney = (Integer) list.get(0).get("umbrella_money")+20000;
            int friendJoinNum = (Integer) list.get(0).get("friendJoinNum");
            WechatAttention wa = wechatAttentionService.getAttentionByOpenId(toOpenId);
            String nickName = "";
            if(wa!=null){
                if(StringUtils.isNotNull(wa.getNickname())){
                    nickName = wa.getNickname();
                }else{
                    WechatBean userinfo = WechatUtil.getWechatName(token, toOpenId);
                    nickName = StringUtils.isNotNull(userinfo.getNickname())?userinfo.getNickname():"";
                }
            }
            String title = "恭喜您，您的好友"+nickName+"已成功加入。您既帮助了朋友，也提升了2万保障金！";
            String templateId = "b_ZMWHZ8sUa44JrAjrcjWR2yUt8yqtKtPU8NXaJEkzg";
            String keyword1 = "您已拥有"+newUmbrellaMoney/10000+"万的保障金，还需邀请"+(400000-newUmbrellaMoney)/20000+"位好友即可获得最高40万保障金。";
            String remark = "邀请一位好友，增加2万保额，最高可享受40万保障！";
            if(oldUmbrellaMoney<400000){
                BabyUmbrellaInfo babyUmbrellaInfo = new BabyUmbrellaInfo();
                babyUmbrellaInfo.setId(Integer.parseInt(fromId));
                babyUmbrellaInfo.setUmberllaMoney(newUmbrellaMoney);
                babyUmbrellaInfoSerivce.updateBabyUmbrellaInfoById(babyUmbrellaInfo);
            }
            if(newUmbrellaMoney>=400000){
                title = "感谢您的爱心，第"+(friendJoinNum+1)+"位好友"+nickName+"已成功加入，一次分享，一份关爱，汇聚微小力量，传递大爱精神！";
                templateId = "b_ZMWHZ8sUa44JrAjrcjWR2yUt8yqtKtPU8NXaJEkzg";
                keyword1 = "您已成功拥有40万的最高保障金。";
                remark = "您还可以继续邀请好友，传递关爱精神，让更多的家庭拥有爱的保障！";
            }
            BabyUmbrellaInfo babyUmbrellaInfo = new BabyUmbrellaInfo();
            babyUmbrellaInfo.setId(Integer.parseInt(fromId));
            babyUmbrellaInfo.setFriendJoinNum(friendJoinNum+1);
            babyUmbrellaInfoSerivce.updateBabyUmbrellaInfoById(babyUmbrellaInfo);

            String keyword2 = StringUtils.isNotNull(babyId)?"观察期":"待激活";
            String url = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=31";
            WechatMessageUtil.templateModel(title, keyword1, keyword2, "", "", remark, token, url, fromOpenId, templateId);
        }

        String title = "恭喜您";
        String description = "您已成功领到20万保障，分享1个好友，提升2万保障，最高可享受40万保障。\n\n点击进入，立即分享！";
        String url = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=31";
        String picUrl = "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/protectumbrella%2Fprotectumbrella";
        String message = "{\"touser\":\""+toOpenId+"\",\"msgtype\":\"news\",\"news\":{\"articles\": [{\"title\":\""+ title +"\",\"description\":\""+description+"\",\"url\":\""+ url +"\",\"picurl\":\""+picUrl+"\"}]}}";

        String jsonobj = HttpRequestUtil.httpsRequest("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" +
                token + "", "POST", message);
        System.out.println(jsonobj+"===============================");
        String result=addUserType(toOpenId);
        System.out.print(result+ "------------------------------------");
    }

    /**
     *更新保护伞用户信息
     */
    @RequestMapping(value = "/updateInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  updateInfo(@RequestBody Map<String, Object> params,HttpServletRequest request,HttpSession session) throws UnsupportedEncodingException {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        Map<String, Object> result=new HashMap<String, Object>();
        String phone=params.get("phone").toString();
        String code=params.get("code").toString();
        String openid= WechatUtil.getOpenId(session, request);
        String codeAuth=utilService.bindUser(phone,code,openid);
        if(codeAuth.equals("0")){
            codeAuth=utilService.bindUser4Doctor(phone,code,openid);
            if(codeAuth.equals("0")){
                codeAuth=utilService.bindUser4ConsultDoctor(phone,code,openid);
                if(codeAuth.equals("0")){
                    result.put("result","3");
                    return result;
                }
            }
        }
        BabyUmbrellaInfo babyUmbrellaInfo = new BabyUmbrellaInfo();
        babyUmbrellaInfo.setBabyId(params.get("babyId").toString());
        babyUmbrellaInfo.setParentIdCard(params.get("idCard").toString());
        babyUmbrellaInfo.setParentPhone(params.get("phone").toString());
        babyUmbrellaInfo.setParentName(URLDecoder.decode(params.get("parentName").toString(),"UTF-8"));
        babyUmbrellaInfo.setParentType(Integer.parseInt(params.get("parentType").toString()));
        babyUmbrellaInfo.setId(Integer.parseInt(params.get("umbrellaId").toString()));
        if(params.get("truePayMoney")!=null){
            babyUmbrellaInfo.setTruePayMoneys(params.get("truePayMoney").toString());
        }
        String res=babyUmbrellaInfoSerivce.updateBabyUmbrellaInfo(babyUmbrellaInfo)+"";

        //插入家庭成员的信息
        UmbrellaFamilyInfo familyInfo = new UmbrellaFamilyInfo();
        familyInfo.setName(URLDecoder.decode(params.get("parentName").toString(), "UTF-8"));
        familyInfo.setUmbrellaId(Integer.parseInt(params.get("umbrellaId").toString()));
        familyInfo.setSex(Integer.parseInt(params.get("parentType").toString()));
        String idCard = params.get("idCard").toString();
        Date birthDay = DateUtils.StrToDate(idCard.substring(6,14),"yyyyMMdd");
        familyInfo.setBirthday(birthDay);
        babyUmbrellaInfoSerivce.saveFamilyUmbrellaInfo(familyInfo);


        result.put("result",res);
        return result;
    }

    /**
     * 获取用户分享数
     */
    @RequestMapping(value = "/userShareNum", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  userShareNum(HttpServletRequest request,HttpSession session) {

        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);
        Map<String, Object> map=new HashMap<String, Object>();
        String openid= WechatUtil.getOpenId(session, request);
        map.put("openid",openid);
        String  id=babyUmbrellaInfoSerivce.getBabyUmbrellaInfo(map).get(0).get("id").toString();
        Map<String, Object> result=new HashMap<String, Object>();
        result.put("count",babyUmbrellaInfoSerivce.getUserShareNums(id));
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
        String id = params.get("id").toString();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("qrcode",babyUmbrellaInfoSerivce.getUserQRCode(id));
        return result;
    }

    /**
     * 根据openid 获取用户关注情况
     */
    @RequestMapping(value = "/getOpenidStatus", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  getOpenidStatus(HttpServletRequest request,HttpSession session) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        String openid= WechatUtil.getOpenId(session, request);
//        openid="o3_NPwrrWyKRi8O_Hk8WrkOvvNOk";
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String,Object> openIdStatus = babyUmbrellaInfoSerivce.getOpenidStatus(openid);
        if(openIdStatus != null){
            String status=openIdStatus.get("status").toString();
            if(status.equals("0")) {
                result.put("status", "0");
                result.put("openid", openid);
                return result;
            }else{
                String id = openIdStatus.get("status").toString();
                result.put("status",id);
                result.put("openid",openid);
                return result;
            }
        }else {
            result.put("status", "1");
            result.put("openid", openid);
            return result;
        }
    }


    /**
     * 判断用户是否存在过数据
     */
    @RequestMapping(value = "/ifExistOrder", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  ifExistOrder(HttpServletRequest request,HttpSession session) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        Map<String, Object> map=new HashMap<String, Object>();
        Map<String, Object> result=new HashMap<String, Object>();
        String openid= WechatUtil.getOpenId(session, request);
        map.put("openid",openid);
        List<Map<String, Object>> list=babyUmbrellaInfoSerivce.getBabyUmbrellaInfo(map);
        if(list.size()>0){
            Map<String, Object> m = list.get(0);
            if(m.get("pay_result")!=null&&m.get("pay_result").equals("fail")
                    &&(m.get("baby_id")==null||m.get("baby_id").equals(""))) {
                result.put("result",1);
                result.put("type","pay");
                return result;
            }
            if (m.get("baby_id") != null && !m.get("baby_id").equals("") &&
                    ( m.get("pay_result")==null || m.get("pay_result").equals("success"))) {
                if (m.get("activation_time") != null && !m.get("activation_time").equals("")) {
                    map.put("createTime",m.get("create_time"));
                    map.put("openid",openid);
                    result.put("rank", babyUmbrellaInfoSerivce.getUmbrellaRank(map));
                }
                result.put("result", 3);
                result.put("umbrella", m);
                return result;
            }
            result.put("result", 2);
            result.put("umbrella", m);
            map.put("createTime",m.get("create_time"));
            result.put("rank", babyUmbrellaInfoSerivce.getUmbrellaRank(map));
            return result;
        }
        result.put("result",1);
        return result;
    }

    /**
     * 判断用户是否存在过数据
     */
    @RequestMapping(value = "/updateActivationTime", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  updateActivationTime(@RequestBody Map<String, Object> params,HttpServletRequest request,HttpSession session) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
        Map<String, Object> map=new HashMap<String, Object>();
        Map<String, Object> result=new HashMap<String, Object>();
        BabyUmbrellaInfo babyUmbrellaInfo = new BabyUmbrellaInfo();
        babyUmbrellaInfo.setId(Integer.parseInt(params.get("id").toString()));
        babyUmbrellaInfo.setActivationTime(new Date());
        String res=babyUmbrellaInfoSerivce.updateBabyUmbrellaInfoById(babyUmbrellaInfo)+"";
        result.put("result",res);
        return result;
    }

    /**
     *
     */
    @RequestMapping(value = "/updateTruePayMoney", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  updateTruePayMoney(@RequestBody Map<String, Object> params) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
        Map<String, Object> map=new HashMap<String, Object>();
        Map<String, Object> result=new HashMap<String, Object>();
        BabyUmbrellaInfo babyUmbrellaInfo = new BabyUmbrellaInfo();
        babyUmbrellaInfo.setId(Integer.parseInt(params.get("id").toString()));
        babyUmbrellaInfo.setTruePayMoneys(params.get("truePayMoneys").toString());
        String res=babyUmbrellaInfoSerivce.updateBabyUmbrellaInfoById(babyUmbrellaInfo)+"";
        result.put("result",res);
        return result;
    }


    /**
     * 随机立减
     */
    @RequestMapping(value = "/randomMoney", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  randomMoney(HttpServletRequest request,HttpSession session) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
        Map<String, Object> map=new HashMap<String, Object>();
        String openid = WechatUtil.getOpenId(session, request);
        map.put("openid",openid);
        List<Map<String, Object>> list = babyUmbrellaInfoSerivce.getBabyUmbrellaInfo(map);
        if(list.size()>0){
            Map<String, Object> m = list.get(0);
            if(m.get("true_pay_moneys")!=null){
                Map<String, Object> result=new HashMap<String, Object>();
                String res=m.get("true_pay_moneys").toString();
                result.put("type","pay");
                result.put("result",res);
                result.put("id",m.get("id").toString());
                return result;
            }else{
                Map<String, Object> result=new HashMap<String, Object>();
                String type=list.get(0).get("true_pay_moneys").toString();
                result.put("type","free");
                return result;
            }
        }
        Map<String, Object> result=new HashMap<String, Object>();
        Map maps = new HashMap();
        maps.put("type","umbrella");
        SwitchConfigure switchConfigure = systemService.getUmbrellaSwitch(maps);
        String flag = switchConfigure.getFlag();
        System.out.println(flag+"flag=======================switchConfigure========================");

        //flag为1是打开，0是关闭
        double ram=0;
        if(flag.equals("1")) {
            do {
                ram = Math.random() * 5;
            } while (ram < 1);
        }
        String res = String.format("%.0f", ram);

        BabyUmbrellaInfo babyUmbrellaInfo=new BabyUmbrellaInfo();
        babyUmbrellaInfo.setOpenid(openid);
        babyUmbrellaInfo.setUmberllaMoney(200000);
        babyUmbrellaInfo.setTruePayMoneys(res);
        babyUmbrellaInfo.setVersion("a");
        if(res.equals("0")){
            babyUmbrellaInfo.setPayResult("success");
        }else {
            babyUmbrellaInfo.setPayResult("fail");
        }
        babyUmbrellaInfoSerivce.saveBabyUmbrellaInfo(babyUmbrellaInfo);
        result.put("type","pay");
        result.put("result",res);
        result.put("id",babyUmbrellaInfo.getId());
        return result;
    }

/**
 * 家庭版保护伞增加成员
 */
    @RequestMapping(value = "/addFamily", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> addFamilyUmbrella(@RequestBody Map<String, Object> params){
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        UmbrellaFamilyInfo familyInfo = new UmbrellaFamilyInfo();
        Date birthDay = DateUtils.StrToDate(params.get("birthDay").toString(), "yyyy-MM-dd");
        familyInfo.setBirthday(birthDay);
        familyInfo.setUmbrellaId(Integer.parseInt((String)params.get("id")));
        familyInfo.setName(params.get("name").toString());
        familyInfo.setSex(Integer.parseInt((String) params.get("sex")));
        int reusltStatus = babyUmbrellaInfoSerivce.saveFamilyUmbrellaInfo(familyInfo);
        resultMap.put("reusltStatus",reusltStatus);
      return resultMap;
    }

    /**
     * 家庭版保护成员列表
     */
    @RequestMapping(value = "/familyList", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> familyUmbrellaList(@RequestBody Map<String, Object> params){
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        boolean activation = false;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Integer id = Integer.parseInt((String) params.get("id"));
        List<UmbrellaFamilyInfo> list = new ArrayList<UmbrellaFamilyInfo>();
        list = babyUmbrellaInfoSerivce.getFamilyUmbrellaList(id);
        BabyBaseInfoVo babyInfo = babyUmbrellaInfoSerivce.getBabyBaseInfo(id);
        if(babyInfo!=null){
            UmbrellaFamilyInfo familyInfo = new UmbrellaFamilyInfo();
            familyInfo.setSex(Integer.parseInt(babyInfo.getSex()));
            familyInfo.setName(babyInfo.getName());
            familyInfo.setBirthday(babyInfo.getBirthday());
            list.add(familyInfo);
            activation = true;
        }
        resultMap.put("familyList",list);
        resultMap.put("activation",activation);
        return resultMap;
    }

//    判定绑定用户
    /**
     * 家庭版保护成员列表
     */
    @RequestMapping(value = "/checkFamilyMembers", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> checkFamilyMembers(@RequestBody Map<String, Object> params){
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        Boolean showFather = true;
        Boolean showMother = true;
        Integer id = Integer.parseInt((String) params.get("id"));
        List<UmbrellaFamilyInfo> list = babyUmbrellaInfoSerivce.getFamilyUmbrellaList(id);
        for(UmbrellaFamilyInfo info:list){
              //0 男 1 女
            if(info.getSex()==2){
              showFather = false;
            }else if(info.getSex()==3){
              showMother = false;
            }
        }
        resultMap.put("showFather",showFather);
        resultMap.put("showMother",showMother);
        return resultMap;
    }

    /**
     *
     */
    @RequestMapping(value = "/getUmbrellaNum", method = {RequestMethod.POST, RequestMethod.GET} )
    public
    @ResponseBody
    Map<String, Object> getUmbrellaNum(){
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String,Object> map = babyUmbrellaInfoSerivce.getUmbrellaNum(resultMap);
        Long familyNum = (Long)map.get("familyNum");
        resultMap.put("umbrellaCount",familyNum);
        return resultMap;
    }


    /**
     * 支付页面openid
     */
    @RequestMapping(value = "/getOpenid", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getOpenid(HttpServletRequest request,HttpSession session){
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        String openid= WechatUtil.getOpenId(session, request);
        if(openid==null||openid.equals("")){
            resultMap.put("openid","none");
            return resultMap;
        }
        resultMap.put("openid",openid);
        return resultMap;
    }

    /**
     * 支付页面openid
     */
    @RequestMapping(value = "/updateBabyUmbrellaInfoIfShare", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> updateBabyUmbrellaInfoIfShare(@RequestBody Map<String, Object> params){
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        BabyUmbrellaInfo babyUmbrellaInfo = new BabyUmbrellaInfo();
        babyUmbrellaInfo.setId(Integer.parseInt(params.get("id").toString()));
        Integer res=babyUmbrellaInfoSerivce.updateBabyUmbrellaInfoIfShare(babyUmbrellaInfo);
        resultMap.put("result",res);
        return resultMap;
    }

    /**
     *  新版加入保护伞
     */
    @RequestMapping(value = "/newJoinUs", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  newJoinUs(@RequestBody Map<String, Object> params,HttpServletRequest request,HttpSession session) throws UnsupportedEncodingException {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        Map<String, Object> result=new HashMap<String, Object>();
        //验证手机号是否正确
        String phone=params.get("phone").toString();
        String code=params.get("code").toString();
        String openid= WechatUtil.getOpenId(session, request);
        String codeAuth=utilService.bindUser(phone,code,openid);
        if(codeAuth.equals("0")){
             codeAuth=utilService.bindUser4Doctor(phone,code,openid);
            if(codeAuth.equals("0")){
                codeAuth=utilService.bindUser4ConsultDoctor(phone,code,openid);
                if(codeAuth.equals("0")){
                    result.put("result","3");
                    return result;
                }
            }
        }
        //添加保护伞初始信息
        BabyUmbrellaInfo babyUmbrellaInfo=new BabyUmbrellaInfo();
        babyUmbrellaInfo.setOpenid(openid);
        babyUmbrellaInfo.setBabyId(params.get("babyId").toString());
        babyUmbrellaInfo.setParentIdCard(params.get("idCard").toString());
        babyUmbrellaInfo.setParentPhone(params.get("phone").toString());
        babyUmbrellaInfo.setParentName(URLDecoder.decode(params.get("parentName").toString(),"UTF-8"));
        babyUmbrellaInfo.setParentType(Integer.parseInt(params.get("parentType").toString()));
        babyUmbrellaInfo.setMoney("5");
        babyUmbrellaInfo.setUmberllaMoney(200000);
        //随机立减
        Map maps = new HashMap();

        //先删除以前可能存在的旧数据
        maps.put("openid",openid);
        List<Map<String, Object>> list = babyUmbrellaInfoSerivce.getBabyUmbrellaInfo(maps);
        if(list.size()>0){
            babyUmbrellaInfoSerivce.deleteUmbrellaByOpenid(babyUmbrellaInfo.getOpenid());
            babyUmbrellaInfoSerivce.deleteByUmbrellaId(Integer.parseInt(list.get(0).get("id").toString()));
            if (list.get(0).get("true_pay_moneys") != null && !list.get(0).get("true_pay_moneys").equals("")) {
                babyUmbrellaInfo.setTruePayMoneys(list.get(0).get("true_pay_moneys").toString());
            }
        }

        maps.put("type","umbrella");
        SwitchConfigure switchConfigure = systemService.getUmbrellaSwitch(maps);
        String flag = switchConfigure.getFlag();
        System.out.println(flag+"flag=======================switchConfigure========================");
        //flag为1是打开，0是关闭
        double ram=0;
        if(flag.equals("1")){
            if(babyUmbrellaInfo.getTruePayMoneys()!=null&&!babyUmbrellaInfo.getTruePayMoneys().equals("")) {
                ram = Integer.parseInt(babyUmbrellaInfo.getTruePayMoneys().toString());
            }else{
                ram = Math.random() * 5;
            }
        }else if(flag.equals("2")){
            ram = 5;
        }
        String ress = String.format("%.0f", ram);

        babyUmbrellaInfo.setTruePayMoneys(ress);

        if(ress.equals("0")){
            babyUmbrellaInfo.setPayResult("success");
        }else {
            babyUmbrellaInfo.setPayResult("fail");
        }
        String shareId=params.get("shareId").toString();
        if(StringUtils.isNotNull(shareId)){
            babyUmbrellaInfo.setSource(shareId);
        }else{
            babyUmbrellaInfo.setSource("no");
        }
        //完成添加动作
        Integer res = babyUmbrellaInfoSerivce.newSaveBabyUmbrellaInfo(babyUmbrellaInfo);
        if(res==1&&"success".equals(babyUmbrellaInfo.getPayResult())){
            Runnable thread = new sendUBWechatMessage(openid,shareId);
            threadExecutor.execute(thread);
        }

        //插入家庭成员的信息
        //宝爸宝妈
        UmbrellaFamilyInfo familyInfo = new UmbrellaFamilyInfo();
        familyInfo.setName(URLDecoder.decode(params.get("parentName").toString(), "UTF-8"));
        familyInfo.setUmbrellaId(babyUmbrellaInfo.getId());
        familyInfo.setSex(Integer.parseInt(params.get("parentType").toString()));
        String idCard = params.get("idCard").toString();
        Date birthDay = DateUtils.StrToDate(idCard.substring(6,14),"yyyyMMdd");
        familyInfo.setBirthday(birthDay);
        babyUmbrellaInfoSerivce.saveFamilyUmbrellaInfo(familyInfo);
        addUserType(openid);
        result.put("result",res);
        result.put("id",babyUmbrellaInfo.getId());
        return result;
    }

    public class sendUBWechatMessage extends Thread {
        private String toOpenId;
        private String fromId;

        public sendUBWechatMessage(String toOpenId,String fromId) {
            this.toOpenId = toOpenId;
            this.fromId = fromId;
        }

        @Override
        public void run() {
            sendUBWechatMessage(toOpenId, fromId);
        }
    }


    public String addUserType(String id) {
        Map<String,Object> parameter = systemService.getWechatParameter();
        String token = (String)parameter.get("token");
        String url= "https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token="+token;
        String jsonData="{\"openid_list\":[\""+id+"\"],\"tagid\" : 105}";
        String reJson=this.post(url, jsonData,"POST");
        System.out.println(reJson);
        JSONObject jb=JSONObject.fromObject(reJson);
        String errmsg=jb.getString("errmsg");
        if(errmsg.equals("ok")){
            return "ok";
        }else {
            return errmsg;
        }
    }


    /**
     * 发送HttpPost请求
     *
     * @param strURL
     *            服务地址
     * @param params
     *            json字符串,例如: "{ \"id\":\"12345\" }" ;其中属性名必须带双引号<br/>
     *            type (请求方式：POST,GET)
     * @return 成功:返回json字符串<br/>
     */
    public String post(String strURL, String params,String type) {
        System.out.println(strURL);
        System.out.println(params);
        try {
            URL url = new URL(strURL);// 创建连接
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod(type); // 设置请求方式
            connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter(
                    connection.getOutputStream(), "UTF-8"); // utf-8编码
            out.append(params);
            out.flush();
            out.close();
            // 读取响应
            int length = (int) connection.getContentLength();// 获取长度
            InputStream is = connection.getInputStream();
            if (length != -1) {
                byte[] data = new byte[length];
                byte[] temp = new byte[512];
                int readLen = 0;
                int destPos = 0;
                while ((readLen = is.read(temp)) > 0) {
                    System.arraycopy(temp, 0, data, destPos, readLen);
                    destPos += readLen;
                }
                String result = new String(data, "UTF-8"); // utf-8编码
                System.out.println(result);
                return result;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null; // 自定义错误信息
    }

    /**
     *  获取用户昵称和排名
     */
    @RequestMapping(value = "/getNickNameAndRanking", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  getNickNameAndRanking(@RequestBody Map<String, Object> params,HttpServletRequest request,HttpSession session) throws UnsupportedEncodingException {
        Map<String, Object> response=new HashMap<String, Object>();
        String openid = (String)params.get("openid");
        String result = "fail";
        String nickName = "";
        int rank = 0;
        openid = StringUtils.isNotNull(openid)?openid:WechatUtil.getOpenId(session,request);
        if(StringUtils.isNotNull(openid)){
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("openid",openid);
            List<Map<String, Object>> list=babyUmbrellaInfoSerivce.getBabyUmbrellaInfo(map);
            if(list.size()>0){
                String payResult = (String)list.get(0).get("pay_result");
                if(StringUtils.isNull(payResult)||"success".equals(payResult)){
                    map.put("createTime",list.get(0).get("create_time"));
                    rank = babyUmbrellaInfoSerivce.getUmbrellaRank(map);
                    Map parameter = systemService.getWechatParameter();
                    String token = (String)parameter.get("token");
                    WechatAttention wa = wechatAttentionService.getAttentionByOpenId(openid);
                    if(wa!=null){
                        if(StringUtils.isNotNull(wa.getNickname())){
                            nickName = wa.getNickname();
                        }else{
                            WechatBean userinfo = WechatUtil.getWechatName(token, openid);
                            nickName = StringUtils.isNotNull(userinfo.getNickname())?userinfo.getNickname():"";
                        }
                    }
                    result = "suc";
                }
            }
        }

        response.put("result",result);
        response.put("nickName",nickName);
        response.put("rank",rank);
        return response;
    }
}
