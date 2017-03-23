package com.cxqm.xiaoerke.modules.wechat.web;

import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.member.service.MemberService;
import com.cxqm.xiaoerke.modules.sys.entity.Article;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.entity.WechatBean;
import com.cxqm.xiaoerke.modules.sys.interceptor.SystemControllerLog;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 微信页面参数获取相关的控制类
 * Created by baoweiw on 2015/7/27.
 */

@Controller
@RequestMapping(value = "wechatInfo")
public class FieldworkWechatController {

    @Autowired
    private SystemService systemService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private WechatAttentionService wechatAttentionService;

    @Autowired
    private SysPropertyServiceImpl sysPropertyService;


    /**
     * 医生公众号菜单引导页
     */
    @RequestMapping(value = "/getDoctorWechatMenId", method = {RequestMethod.POST, RequestMethod.GET})
    public String getDoctorWechatMenu(HttpServletRequest request,HttpServletResponse response, HttpSession session) throws Exception {
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();

        String code = request.getParameter("code");
        String url = java.net.URLDecoder.decode(request.getParameter("url"), "utf-8");
        String get_access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=APPID" +
                "&secret=SECRET&" +
                "code=CODE&grant_type=authorization_code";
        get_access_token_url = get_access_token_url.replace("APPID", sysPropertyVoWithBLOBsVo.getDoctorCorpid());
        get_access_token_url = get_access_token_url.replace("SECRET", sysPropertyVoWithBLOBsVo.getDoctorSectet());
        get_access_token_url = get_access_token_url.replace("CODE", code);
        String access_token = "";
        String openid = "";
        if (access_token.isEmpty() && openid.isEmpty()) {
            String json = HttpRequestUtil.getConnectionResult(get_access_token_url, "GET", "");
            WechatBean wechat = JsonUtil.getObjFromJsonStr(json, WechatBean.class);
            openid = wechat.getOpenid();
            session.setAttribute("openId", openid);
            CookieUtils.setCookie(response, "openId", openid, 60 * 60 * 24 * 30, sysPropertyVoWithBLOBsVo.getBaodfDomainValue());
        }
        if ("1".equals(url)) {
            //每日清单
            url = "#/myAccount";
        } else if ("2".equals(url)) {
            //出诊安排
            url = "#/doctorFirst/,";
        } else if ("3".equals(url)) {
            //接诊提醒
            url = "#/acceptRemind";
        } else if ("4".equals(url)) {
            //个人中心
            url = "#/myselfFirst";
        } else if("5".equals(url)){
            //电话咨询
            url = "#/phoneConsultFirst/";

            return "redirect:" + sysPropertyVoWithBLOBsVo.getDoctorWebUrl() + "/doctor/phoneConsultDoctor" + url;
        }else if ("6".equals(url)){
            return "redirect:" + sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "titan/nonRealTimeConsult#/NonTimeDoctorMessageList";
        }

        return "redirect:" + sysPropertyVoWithBLOBsVo.getDoctorWebUrl() + "/doctor/doctor" + url;
    }

    /**
     * 用户公众号菜单引导页
     */
    @RequestMapping(value = "/getUserWechatMenId", method = {RequestMethod.POST, RequestMethod.GET})
    public String getUserWechatMenu(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();

        String code = request.getParameter("code");
        String url = java.net.URLDecoder.decode(request.getParameter("url"), "utf-8");
        System.out.println("yuanxing"+url);
        if ("1".equals(url)) {
            //引导页
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/appoint#/guide";
            LogUtils.saveLog("引导页");
        } else if ("2".equals(url)) {
            //预约首页
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/firstPage/appoint";
            LogUtils.saveLog("预约首页");
        } else if ("3".equals(url)) {
            String state = request.getParameter("state");
            //接诊提醒
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/appoint#/userEvaluate/" + state;
            LogUtils.saveLog("接诊提醒");

        } else if ("4".equals(url)) {
            //郑玉巧育儿经
            url = sysPropertyVoWithBLOBsVo.getWisdomWebUrl() + "/wisdom/firstPage/knowledge";
            LogUtils.saveLog("郑玉巧育儿经");
        } else if ("5".equals(url)) {
            //郑玉巧在线
            url = sysPropertyVoWithBLOBsVo.getWisdomWebUrl() + "/wisdom/knowledge#/sheOnlineIndex";
            LogUtils.saveLog("郑玉巧在线");

        } else if ("6".equals(url)) {
            //我的预约
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/appoint#/myAppointment";
            LogUtils.saveLog("我的预约");

        } else if ("7".equals(url)) {
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/appoint#/operateIndex";
        } else if ("8".equals(url)) {
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/appoint#/operateIndex";
        } else if ("9".equals(url)) {
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/phoneConsult#/selfCenter";
        } else if ("20".equals(url)) {
            //扫码送周会员
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/appoint#/memberService/week,extend,";
        } else if ("23".equals(url)) {
            //赠送周会员
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/appoint#/memberService/week,extend,";
        } else if ("21".equals(url)) {
            //赠送月会员
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/appoint#/memberService/month,extend,";
        } else if ("22".equals(url)) {
            //赠送季会员
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/appoint#/memberService/quarter,extend,";
        }else if("10".equals(url)){
            //健康管理
            url = sysPropertyVoWithBLOBsVo.getWisdomWebUrl() +"/wisdom/firstPage/healthPlan";
            LogUtils.saveLog("BMGL_36");
        }else if("23".equals(url)){
            url = sysPropertyVoWithBLOBsVo.getWisdomWebUrl() + "/titan/appoint#/healthRecordIndex/0";
        }else if("24".equals(url)){
            url = sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "/keeper/health#/consultBabyList";
        }else if("25".equals(url)){
            url = sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "/market/market#/consultBabyList";
        }else if("11".equals(url)){
            //运营活动
            String state = request.getParameter("state");
            if("HDSY_YDYW".equals(state)){
                LogUtils.saveLog("HDSY_YDYW");//从阅读原文打开活动首页
            }else if("FXSY_PYQ".equals(state)){
                LogUtils.saveLog("FXSY_PYQ");//从首页朋友圈打开活动首页
            }else if("FXSY_PYXX".equals(state)){
                LogUtils.saveLog("FXSY_PYXX");//从首页朋友消息打开活动首页
            }else if("HDSY_CD".equals(state)){
                LogUtils.saveLog("HDSY_CD");//从菜单打开活动首页
            }else if("FXJG_PYQ".equals(state)){
                LogUtils.saveLog("FXJG_PYQ");//从结果页朋友圈打开活动首页
            }else if("FXJG_PYXX".equals(state)){
                LogUtils.saveLog("FXJG_PYXX");//从结果页朋友圈打开活动首页
            }
            url = sysPropertyVoWithBLOBsVo.getKeeperWebUrl() +"market/firstPage/momNutritionTest";
        }else if("26".equals(url)){
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "titan/firstPage/antiDogFirst";
        }else if ("28".equals(url)){
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "/titan/firstPage/phoneConsult";
        }else if (url.indexOf("consultPhone")>-1){
            System.out.println("begin"+url);
            String departmentName  = URLEncoder.encode(url.replace("consultPhone",""), "UTF-8");
            url =sysPropertyVoWithBLOBsVo.getTitanWebUrl() +"titan/phoneConsult#/phoneConDoctorList/"+departmentName+",searchDoctorByDepartment,";
            System.out.println("end"+url);
        }else if("29".equals(url)){
            //保险
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "titan/firstPage/insurance";
        }else if("30".equals(url)){
            //保险
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "titan/insurance#/handfootmouthIndex";
        }else if(url.indexOf("umbrella")>-1){
            String[] state = url.replace("umbrella","").split("_");
            if(state.length>1) {
                String id = state[1];
                String status = state[0];
                url = sysPropertyVoWithBLOBsVo.getWisdomWebUrl() + "wisdom/firstPage/umbrella?status="+status+"&id="+id+"&time="+new Date().getTime();
            }else{
                String status = state[0];
                url = sysPropertyVoWithBLOBsVo.getWisdomWebUrl() + "wisdom/firstPage/umbrella?time="+new Date().getTime()+"&status="+status;
            }
        }else if("31".equals(url)){
            url = sysPropertyVoWithBLOBsVo.getWisdomWebUrl() + "wisdom/umbrella#/umbrellaJoin/"+new Date().getTime()+"/120000000";
        }else if("32".equals(url)){
            url = sysPropertyVoWithBLOBsVo.getWisdomWebUrl() + "wisdom/firstPage/lovePlan";
        }else if("33".equals(url)){
            url = sysPropertyVoWithBLOBsVo.getWisdomWebUrl() + "wisdom/umbrella#/umbrellaInvite";
        }else if("34".equals(url)){
            url = sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "angel/patient/consult#/customerService";
        } else if("35".equals(url)){
            url = sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wxPay/patientPay.do?serviceType=doctorConsultPay";
        }else if("36".equals(url)){
            url = sysPropertyVoWithBLOBsVo.getWisdomWebUrl() + "wisdom/firstPage/heightForecast";
        }else if("37".equals(url)){
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "titan/olympicBaby#/olympicBabyFirst";
        }else if("38".equals(url)){
            //肺炎保
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "titan/insurance#/pneumoniaIndex";
        }else if("39".equals(url)){
            //非及时咨询
            url = sysPropertyVoWithBLOBsVo.getAngelWebUrl() + "angel/patient/consult#/patientConsultNoFee";
        }else if("40".equals(url)){
            //非及时咨询
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "titan/nonRealTimeConsult#/NonTimeUserConsultList";
        }else if("43".equals(url)){
            String doctorId  = URLEncoder.encode(url.replace("doctorId",""), "UTF-8");
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "titan/nonRealTimeConsult#/consultDoctorHome/"+doctorId;
        }else if("44".equals(url)){
            url = sysPropertyVoWithBLOBsVo.getWisdomWebUrl() + "wisdom/activity#/picSpreadIndex";
        }else if("47".equals(url)){
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "titan/baoFansCamp#/feedback";
        }else if("48".equals(url)){
            url = sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/playtour#/babyCoinTicketList";
        }else if("50".equals(url)){
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "titan/nonRealTimeConsult#/myDoctor";
        }else if(url.startsWith("51")){
            String[] parameters = request.getQueryString().split(",");
            url = sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/wxPay/patientPay.do?serviceType=nonRealTime&consultId="+parameters[1];
        }else if(url.startsWith("52")){  //集卡活动
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "titan/appWfdb";
        }else if(url.startsWith("54")){  //集卡活动
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "titan/appHusband#/husGuide";
        }else if(url.startsWith("57")){  //打卡活动
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "titan/sign#signHome/false";
        }
        String get_access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=APPID" +
                "&secret=SECRET&" +
                "code=CODE&grant_type=authorization_code";
        get_access_token_url = get_access_token_url.replace("APPID", sysPropertyVoWithBLOBsVo.getUserCorpid());
        get_access_token_url = get_access_token_url.replace("SECRET", sysPropertyVoWithBLOBsVo.getUserSectet());
        get_access_token_url = get_access_token_url.replace("CODE", code);
        String access_token = "";
        String openid = "";
        if (access_token.isEmpty() && openid.isEmpty()) {
            WechatBean wechat;
            int countNum = 0;
            do {
                System.out.print("wechatURL:"+get_access_token_url);
                String json = HttpRequestUtil.getConnectionResult(get_access_token_url, "GET", "");
                System.out.print("json:"+json);
                wechat = JsonUtil.getObjFromJsonStr(json, WechatBean.class);
                if (countNum++ > 3) {
                    break;
                }
            } while (wechat == null);

            openid = wechat.getOpenid();
            session.setAttribute("openId", openid);
            CookieUtils.setCookie(response, "openId", openid==null?"":openid,60*60*24*30,sysPropertyVoWithBLOBsVo.getBaodfDomainValue());
            memberService.sendExtendOldMemberWechatMessage(openid);
        }
        if(url.startsWith("41")){
            url = getBabyCoinURL(request, openid,sysPropertyVoWithBLOBsVo);
        }else if(url.startsWith("42")){
            if(url.equals("42")){
                url = sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/playtour#/babyCoinInvitePage/";
            }else{
                String logstr = url.split(",")[1];
                String showlayer = url.split(",").length>2?url.split(",")[2]:"";

                LogUtils.saveLog(logstr,openid);
                url = sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/playtour#/babyCoinInvitePage/"+showlayer;
            }
        }else if(url.startsWith("46")){
            if(StringUtils.isNull(openid)){
                openid = "testOpenId";
            }
            String QRCode = url.split(",")[1];
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "titan/vaccine/main.html#/"+openid+","+QRCode;
        }else if(url.equals("49")){
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "titan/baoFansCamp#/story2016Index";
        }else if(url.startsWith("53")){  //集卡活动
            url = getBabyCoinURL(request, openid,sysPropertyVoWithBLOBsVo);
        }else if(url.startsWith("55")){  //集卡活动
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "titan/share#/addGroup";
        }else if(url.startsWith("56")){
            url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "titan/nonRealTimeConsult#/NonTimeUserFindDoctor";
        }else if(url.startsWith("58")){  //打卡活动
            url = getBabyCoinURL(request, openid,sysPropertyVoWithBLOBsVo);
        }
        return "redirect:" + url;
    }

    private String getBabyCoinURL(HttpServletRequest request, String openid,SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo) throws UnsupportedEncodingException {
        String url = "";//判断新老用户
        WechatAttention attention = wechatAttentionService.getAttentionByOpenId(openid);
        String[] parameters = request.getQueryString().split(",");
        String oldOpenId = java.net.URLDecoder.decode(parameters[1], "utf-8");
        String marketer = java.net.URLDecoder.decode(parameters[2], "utf-8");
        String redPacketId = java.net.URLDecoder.decode(parameters.length>3?parameters[3]:"", "utf-8");

        if(marketer.contains("&")){
            marketer = marketer.split("&")[0];
        }

        if(attention == null || attention.getDate() == null){//新用户
            if(marketer.startsWith("177")){  //集卡活动
                url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "titan/appWfdb#/newUser/"+oldOpenId+","+marketer;
            }else if(marketer.startsWith("147")){  //打卡活动
                url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "titan/sign#/signNewUser/"+oldOpenId+","+marketer;
            }else{//宝宝币
                url = sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/playtour#/babyCoinInviteNew/"+oldOpenId+","+marketer;
                LogUtils.saveLog("ZXYQ_YQK_NEW","oldOpenId="+oldOpenId+"openid="+openid+"marketer"+marketer);
            }
        }else {//老用户
            if(marketer.startsWith("177")){  //集卡活动
                url = sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/fieldwork/wechat/author?url="+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/getUserWechatMenId?url=52";
            }else if(marketer.startsWith("147")){  //打卡活动
                url = sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/fieldwork/wechat/author?url="+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/getUserWechatMenId?url=57";
            }else{//宝宝币
                url = sysPropertyVoWithBLOBsVo.getKeeperWebUrl() + "keeper/playtour#/babyCoinInviteOld/"+oldOpenId+","+marketer+","+redPacketId;
                LogUtils.saveLog("ZXYQ_YQK_OLD","openid="+openid);
            }
        }
        return url;
    }


    /**
     * 用户端微信JS-SDK获得初始化参数
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getConfig", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, String> getConfig(HttpServletRequest request) throws Exception {
        String u = request.getParameter("url");
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        Map<String, Object> parameter = systemService.getWechatParameter();
        String ticket = (String) parameter.get("ticket");
        Map<String, String> config = JsApiTicketUtil.sign(ticket, u,sysPropertyVoWithBLOBsVo);
        config.put("payType1SumMoney", sysPropertyVoWithBLOBsVo.getPayType1SumMoney());
        config.put("payType1UseBabycoin", sysPropertyVoWithBLOBsVo.getPayType1UseBabycoin());
        config.put("payType2SumMoney", sysPropertyVoWithBLOBsVo.getPayType2SumMoney());
        config.put("payType2UseBabycoin",sysPropertyVoWithBLOBsVo.getPayType2UseBabycoin());
        config.put("payType3SumMoney", sysPropertyVoWithBLOBsVo.getPayType3SumMoney());
        config.put("payType3UseBabycoin",sysPropertyVoWithBLOBsVo.getPayType3UseBabycoin());
        config.put("angelWebUrl",sysPropertyVoWithBLOBsVo.getAngelWebUrl());
        return config;
    }

    /**
     * 验证主入口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/fieldwork/wechat/author", method = RequestMethod.GET)
    public String Oauth2API(HttpServletRequest request) {
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        String backUrl = request.getParameter("url");
        String oauth2Url = WechatUtil.getOauth2Url("user",backUrl,sysPropertyVoWithBLOBsVo);
        return "redirect:" + oauth2Url;
    }

    /**
     * 验证主入口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/fieldwork/wechat/doctorAuthor", method = RequestMethod.GET)
    public String doctorAuthor(HttpServletRequest request) {
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        String backUrl = request.getParameter("url");
        String oauth2Url = WechatUtil.getOauth2Url("doctor",backUrl,sysPropertyVoWithBLOBsVo);
        return "redirect:" + oauth2Url;
    }

    /**
     * 向微信客户端推送消息
     */
    @RequestMapping(value = "/postInfoToWechat", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    @SystemControllerLog(description = "00000084")//咨询医生消息推送
    String
    sendMsgToWechat(HttpServletRequest request, HttpSession session) {
        String openId = (String) session.getAttribute("openId");
        if (!StringUtils.isNotNull(openId)) {
            openId = CookieUtils.getCookie(request, "openId");
        }

        Map<String, Object> parameter = systemService.getWechatParameter();
        String token = (String) parameter.get("token");

        List<Article> articleList = new ArrayList<Article>();
        Article article = new Article();
        article.setTitle("三甲医院儿科专家    咨询秒回不等待");
        article.setDescription("小儿内科:       24小时全天 \n\n小儿皮肤科/保健科:   8:00 ~ 23:00\n\n妇产科:   12:00-14:00，19:00-22:00\n" +
                "\n小儿其他专科:   19:00 ~ 21:00\n\n" +
                "(外科、眼科、耳鼻喉科、口腔科、预防接种科、中医科、心理科)\n\n好消息！可在线咨询北京儿童医院皮肤科专家啦！");
        article.setPicUrl("http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/menu/%E6%8E%A8%E9%80%81%E6%B6%88%E6%81%AF2.png");
        article.setUrl("https://mp.weixin.qq.com/s?__biz=MzI2MDAxOTY3OQ==&mid=504236660&idx=1&sn=10d923526047a5276dd9452b7ed1e302&scene=1&srcid=0612OCo7d5ASBoGRr2TDgjfR&key=f5c31ae61525f82ed83c573369e70b8f9b853c238066190fb5eb7b8640946e0a090bbdb47e79b6d2e57b615c44bd82c5&ascene=0&uin=MzM2NjEyMzM1&devicetype=iMac+MacBookPro11%2C4+OSX+OSX+10.11.4+build(15E65)&version=11020201&pass_ticket=dG5W6eOP3JU1%2Fo3JXw19SFBAh1DgpSlQrAXTyirZuj970HMU7TYojM4D%2B2LdJI9n");
        articleList.add(article);
        WechatUtil.senImgMsgToWechat(token,openId,articleList);

        if (openId != null) {
            return "true";
        }
        return "false";
    }

    /**
     * 郑玉巧在线像微信客户端推送不同时间段消息
     */
    @RequestMapping(value = "/postInfoToWechatOnline", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    @SystemControllerLog(description = "00000085")//郑玉巧在线咨询医生消息推送
    String
    sendMsgToWechatOnline(HttpServletRequest request, HttpSession session) {
        String openId = (String) session.getAttribute("openId");
        if (!StringUtils.isNotNull(openId)) {
            openId = CookieUtils.getCookie(request, "openId");
        }

        Map<String, Object> parameter = systemService.getWechatParameter();
        String token = (String) parameter.get("token");
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + token;
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            String weektime = c.get(Calendar.DAY_OF_WEEK) + ":" + c.get(Calendar.HOUR_OF_DAY);
            String str = "";
            if (("4:14").equals(weektime) || ("4:15").equals(weektime)) {
                str = "您好，欢迎您咨询郑玉巧医生，可点击左下角“小键盘”直接输入文字或语音，在问题前加入“@郑玉巧”即可向郑大夫提问!";

            } else {
                str = "很遗憾，现在不是郑玉巧在线时间，若想咨询其他医生，可点击左下角“小键盘”直接输入文字或语音，即可咨询！";

            }
            String json = "{\"touser\":\"" + openId + "\",\"msgtype\":\"text\",\"text\":" +
                    "{\"content\":\"CONTENT\"}" + "}";
            json = json.replace("CONTENT", str);
            System.out.println(json);
            String result = HttpRequestUtil.httpPost(json, url);
            System.out.println("当前星期几和时间" + weektime);
            if (openId != null) {
                return "true";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "false";
    }


    /**
     * 用户评价
     */
    @RequestMapping(value = "/getCustomerEvaluation", method = {RequestMethod.POST, RequestMethod.GET})
    public String getCustomerEvaluation(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        String state = request.getParameter("state");
        return "redirect:" + "/appoint#/userEvaluate/" + state;
    }

    /**
     * 郑玉巧育儿经文章分享
     */
    @RequestMapping(value = "/getZhengArticle", method = {RequestMethod.POST, RequestMethod.GET})
    public String getZhengArticle(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();

        String code = request.getParameter("code");
        String get_access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=APPID" +
                "&secret=SECRET&" +
                "code=CODE&grant_type=authorization_code";
        get_access_token_url = get_access_token_url.replace("APPID", sysPropertyVoWithBLOBsVo.getUserCorpid());
        get_access_token_url = get_access_token_url.replace("SECRET", sysPropertyVoWithBLOBsVo.getUserSectet());
        get_access_token_url = get_access_token_url.replace("CODE", code);
        String access_token = "";
        String openid = "";
        if (access_token.isEmpty() && openid.isEmpty()) {
            String json = HttpRequestUtil.getConnectionResult(get_access_token_url, "GET", "");
            WechatBean wechat = JsonUtil.getObjFromJsonStr(json, WechatBean.class);
            openid = wechat.getOpenid();
            session.setAttribute("openId", openid);
            CookieUtils.setCookie(response, "openId", openid, 60 * 60 * 24 * 30,".baodf.com");
        }
        String id = request.getParameter("id");
        LogUtils.saveLog(Servlets.getRequest(),"00000082", "分享后查看某篇文章:" + id + ":openid:" + openid);
        return "redirect:" + "/ap#/knowledgeArticleContent/" + id + "," + "TG";
    }

    /**
     * 郑玉巧育儿经首页分享
     */
    @SystemControllerLog(description = "00000083")
    @RequestMapping(value = "/getZhengIndex", method = {RequestMethod.POST, RequestMethod.GET})
    public String getZhengIndex(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();

        String code = request.getParameter("code");
        String get_access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=APPID" +
                "&secret=SECRET&" +
                "code=CODE&grant_type=authorization_code";
        get_access_token_url = get_access_token_url.replace("APPID", sysPropertyVoWithBLOBsVo.getUserCorpid());
        get_access_token_url = get_access_token_url.replace("SECRET", sysPropertyVoWithBLOBsVo.getUserSectet());
        get_access_token_url = get_access_token_url.replace("CODE", code);
        String access_token = "";
        String openid = "";
        if (access_token.isEmpty() && openid.isEmpty()) {
            String json = HttpRequestUtil.getConnectionResult(get_access_token_url, "GET", "");
            WechatBean wechat = JsonUtil.getObjFromJsonStr(json, WechatBean.class);
            openid = wechat.getOpenid();
            session.setAttribute("openId", openid);
            CookieUtils.setCookie(response, "openId", openid, 60 * 60 * 24 * 30,".baodf.com");
        }
        return "redirect:" + "/ap#/knowledgeIndex";
    }

}
