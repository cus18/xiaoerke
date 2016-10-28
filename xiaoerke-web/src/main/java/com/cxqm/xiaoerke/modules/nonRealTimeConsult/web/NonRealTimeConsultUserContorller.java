package com.cxqm.xiaoerke.modules.nonRealTimeConsult.web;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorInfoVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultDoctorInfoService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionPropertyService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.entity.ConsultSessionStatus;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.entity.NonRealTimeConsultRecordVo;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.entity.NonRealTimeConsultSessionVo;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.service.NonRealTimeConsultService;
import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.entity.WechatBean;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2016/09/05 0024.
 */
@Controller
@RequestMapping(value = "nonRealTimeConsultUser")
public class NonRealTimeConsultUserContorller {


    @Autowired
    private SessionRedisCache sessionRedisCache;

    @Autowired
    private ConsultSessionPropertyService consultSessionPropertyService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private NonRealTimeConsultService nonRealTimeConsultUserService;

    @Autowired
    private ConsultDoctorInfoService consultDoctorInfoService;

    @Autowired
    private SysPropertyServiceImpl sysPropertyService;


    @RequestMapping(value = "/getDepartmentList", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String,Object> departmentList(HttpSession session, HttpServletRequest request) {
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("departmentList",nonRealTimeConsultUserService.departmentList());
        return resultMap;
    }

    @RequestMapping(value = "/getStarDoctorlist", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String,Object> starDoctorlist(HttpSession session, HttpServletRequest request) {
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("startDoctorList",consultDoctorInfoService.getStarDoctorList());
        return resultMap;
    }

    @RequestMapping(value = "/getStarDoctorInfo", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String,Object> starDoctorInfo(HttpSession session, HttpServletRequest request,@RequestBody Map<String, Object> params) {
        Map<String,Object> resultMap = new HashMap<String, Object>();
        return consultDoctorInfoService.getConsultDoctorInfo(null);
    }

    @RequestMapping(value = "/savenConsultRecord", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public void savenConsultRecord(@RequestParam(required = true) Integer sessionid, String userId, String fromType, String content, String msgtype){
        nonRealTimeConsultUserService.savenConsultRecord(sessionid,userId,fromType,content,msgtype);
    }

    @RequestMapping(value = "/getBabyBaseInfo", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String,Object> getBabyBaseInfo(HttpSession session, HttpServletRequest request){
        Map<String,Object> reusltMap = new HashMap<String, Object>();
        String openid = WechatUtil.getOpenId(session,request);
        if(!StringUtils.isNotNull(openid)) {
            reusltMap.put("status", "error");
            reusltMap.put("msg", "未获取到用户的先关信息,请重新打开页面");
            return reusltMap;
        }
        BabyBaseInfoVo babyBaseInfoVo = nonRealTimeConsultUserService.babyBaseInfo(openid);
        reusltMap.put("babyId",babyBaseInfoVo.getId());
        reusltMap.put("babySex",babyBaseInfoVo.getSex());
        reusltMap.put("babyBirthDay", DateUtils.DateToStr(babyBaseInfoVo.getBirthday(),"date"));
        return reusltMap;
    }

    /**
     * 聊天咨询文件上传
     * @param file
     * @return {"status","success"}
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value="/uploadMediaFile",method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    HashMap<String,Object> UploadFile(@RequestParam("file") MultipartFile file) throws UnsupportedEncodingException {
        HashMap<String, Object> response = nonRealTimeConsultUserService.uploadMediaFile(file);
        return response;
    }

    @RequestMapping(value = "/createSession", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String,Object> createSession(HttpSession session, HttpServletRequest request,@RequestBody Map<String, Object> params) {
        String openid = WechatUtil.getOpenId(session,request);
        if(!StringUtils.isNotNull(openid)){
            Map<String,Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status","error");
            resultMap.put("msg","未获取到用户的先关信息,请重新打开页面");
            return resultMap;
        }
        String csUserId = (String )params.get("csUserId");
        String content =  (String) params.get("sex")+"#"+(String )params.get("birthday")+"#"+(String )params.get("describeIllness");
        List<String> imgList = (List)params.get("imgList");
        if(imgList.size()>0){
            for(String str:imgList){
                Map parameter = systemService.getWechatParameter();
                String token = (String) parameter.get("token");
                try {
                    String mediaURL = WechatUtil.downloadMediaFromWx(token,str, "image",null);
                    content +="#"+mediaURL;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(null ==params.get("babyId")){
            BabyBaseInfoVo vo = new BabyBaseInfoVo();
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date dfa =sdf.parse((String )params.get("birthday"));
                vo.setBirthday(dfa);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            vo.setSex((String) params.get("sex"));
            vo.setOpenid(openid);
            nonRealTimeConsultUserService.saveBabyBaseInfo(vo);
//            创建评价记录
        }
        HashMap<String, Object>  sessionMap = nonRealTimeConsultUserService.createSession(csUserId,openid,content);
        nonRealTimeConsultUserService.saveCustomerEvaluation(openid,csUserId,(Integer) sessionMap.get("sessionId")+"");
        return sessionMap;
    }


    @RequestMapping(value = "/doctorListByDepartment", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String,Object> getDoctorListByDepartment(HttpSession session, HttpServletRequest request,@RequestBody Map<String, Object> params) {
        Map<String,Object> resultMap = new HashMap<String, Object>();
        String  departmentName = (String) params.get("departmentName");
        List<ConsultDoctorInfoVo> departmentVoList = nonRealTimeConsultUserService.getDoctorListByDepartment(departmentName);
        resultMap.put("departmentVoList",departmentVoList);
        return resultMap;
    }

    @RequestMapping(value = "/sessionList", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String,Object> sessionList(HttpSession session, HttpServletRequest request) {
        Map<String,Object> resultMap = new HashMap<String, Object>();
        String openid = WechatUtil.getOpenId(session,request);
        NonRealTimeConsultSessionVo vo = new NonRealTimeConsultSessionVo();
        vo.setUserId(openid);
        List<NonRealTimeConsultSessionVo> sessionVoList = nonRealTimeConsultUserService.selectByNonRealTimeConsultSessionVo(vo);

        //组装页面数据
        List<Map<String,Object>> sessionlist = new ArrayList<Map<String, Object>>();
        Date nowTime = new Date();
        for(NonRealTimeConsultSessionVo sessionVo:sessionVoList){
            Map<String,Object> voMap = new HashMap<String, Object>();
            voMap.put("sessionId",sessionVo.getId());
            voMap.put("doctorid",sessionVo.getCsUserId());
            voMap.put("csUserName",sessionVo.getCsUserName());
            voMap.put("doctorDepartmentName",sessionVo.getDoctorDepartmentName());
            voMap.put("doctorProfessor",sessionVo.getDoctorProfessor());
            String message = sessionVo.getLastMessageContent();
            if("createSession".equals(sessionVo.getLastMessageType())){
                message = message.split("\\#")[2];
            }
            voMap.put("message",message);

            if("ongoing".equals(sessionVo.getStatus())){
                if("doctor".equals(sessionVo.getLastMessageType())){
                    voMap.put("state","2");
                }else{
                    voMap.put("state","1");
                }
            }else{
                voMap.put("state","0");
            }

            if(sessionVo.getLastMessageTime().getTime()+24*60*60*1000<nowTime.getTime()){
                voMap.put("lastMessageTime",DateUtils.formatDateToStr(sessionVo.getLastMessageTime(),"MM月dd日"));
            }else{
                voMap.put("lastMessageTime",DateUtils.formatDateToStr(sessionVo.getLastMessageTime(),"HH:ss"));
            }
            voMap.put("sortDate",sessionVo.getLastMessageTime());
            sessionlist.add(voMap);
        }
        resultMap.put("sessionVoList",sessionlist);
        return resultMap;
    }

    @RequestMapping(value = "/conversationInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String,Object> conversationInfo(HttpSession session, HttpServletRequest request,@RequestBody Map<String, Object> params){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        String openid = WechatUtil.getOpenId(session,request);
        Integer sessionid = Integer.parseInt((String)params.get("sessionId"));
        if(!StringUtils.isNotNull(openid)){
            resultMap.put("state","error");
            resultMap.put("result_info","请重新打开页面");
            return resultMap;
        }

        NonRealTimeConsultSessionVo sessionVo = new NonRealTimeConsultSessionVo();
        sessionVo.setId(sessionid);
        sessionVo.setUserId(openid);
        List<NonRealTimeConsultSessionVo> sessionInfo = nonRealTimeConsultUserService.selectByNonRealTimeConsultSessionVo(sessionVo);
        if(sessionInfo.size()>0){
            sessionVo = sessionInfo.get(0);
            resultMap.put("doctorName",sessionVo.getCsUserName());
            resultMap.put("doctorId",sessionVo.getCsUserId());
            resultMap.put("professor",sessionVo.getDoctorProfessor());
            resultMap.put("department",sessionVo.getDoctorDepartmentName());
            resultMap.put("sessionStatus",sessionVo.getStatus());

//            送心意地址
            if("sessionend".equals(sessionVo.getStatus())){
                String customerId = nonRealTimeConsultUserService.getNonRealtimeCustomerId(sessionid);
                SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(sysPropertyVoWithBLOBsVo.getKeeperWebUrl() +"keeper/wxPay/patientPay.do?serviceType=customerPay&customerId=");
                stringBuilder.append(customerId);
                stringBuilder.append("&sessionId=");
                stringBuilder.append(sessionid);
                stringBuilder.append("&evaluateSource=nonRealtimeConsult");
                resultMap.put("mindPath",stringBuilder.toString());
            }
        }else{
            resultMap.put("state","error");
            resultMap.put("result_info","未找到相应的会话");
            return resultMap;
        }

        NonRealTimeConsultRecordVo recordVo = new NonRealTimeConsultRecordVo();
        recordVo.setSessionId(sessionid);
        recordVo.setOrder("createTimeAsc");
        List<NonRealTimeConsultRecordVo> recodevoList = nonRealTimeConsultUserService.selectSessionRecordByVo(recordVo);
        //开始组装数据
        List<Map> messageList = new ArrayList<Map>();
        for(NonRealTimeConsultRecordVo vo:recodevoList){
            Map<String ,Object> recordMap = new HashMap<String, Object>();
            if(openid.equals(vo.getSenderId())){
                recordMap.put("source","user");
            }else{
                recordMap.put("source","doctor");
            };
            String messageType = vo.getMessageType();
            recordMap.put("messageType",messageType);
            if(ConsultSessionStatus.CREATE_SESSION.getVariable().equals(messageType)){
                String[] messageInfo = vo.getMessage().split("\\#");
                recordMap.put("babyBaseInfo",messageInfo[0] == "0"?"女":"男"+"  "+messageInfo[1]);
                recordMap.put("message",messageInfo[2]);

                if(messageInfo.length>3){
                    List<String > imgList = new ArrayList<String>();
                    for(int i=3;i<messageInfo.length;i++){
                        imgList.add(messageInfo[i]);
                    }
                    recordMap.put("imgPath",imgList);
                }
            }else{
                recordMap.put("message",vo.getMessage());
            }
            recordMap.put("messageTime",DateUtils.formatDateToStr(vo.getCreateTime(),"MM月dd日 HH:mm"));
            messageList.add(recordMap);
        }

        //用户微信头像的信息
            Map parameter = systemService.getWechatParameter();
        String token = (String) parameter.get("token");
        WechatBean wechatInfo = WechatUtil.getWechatName(token,openid);
        resultMap.put("wechatImg",wechatInfo.getHeadimgurl());
        resultMap.put("messageList",messageList);
        return resultMap;
    }

    @RequestMapping(value = "/upadateRecorde", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String,Object> upadateRecorde(HttpSession session, HttpServletRequest request,@RequestBody Map<String, Object> params){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        //根据sessionid和openid确认会话
//        然后插入。。。
        String openid = WechatUtil.getOpenId(session,request);
        Integer sessionid = Integer.parseInt((String)params.get("sessionId"));
        String content = (String)params.get("content");
        String msgType = (String)params.get("msgType");
        String source = (String)params.get("source");
        String doctorId = (String)params.get("doctorId");
        if("img".equals(msgType)){
            Map parameter = systemService.getWechatParameter();
            String token = (String) parameter.get("token");
            try {
                content = WechatUtil.downloadMediaFromWx(token,content, "image",null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(!StringUtils.isNotNull(openid)){
            resultMap.put("state","error");
            resultMap.put("result_info","请重新打开页面");
            return resultMap;
        }
        NonRealTimeConsultSessionVo sessionVo = new NonRealTimeConsultSessionVo();
        sessionVo.setId(sessionid);
        if("user".equals(source)){
            sessionVo.setUserId(openid);
        }else{
            sessionVo.setCsUserId(doctorId);
        }
        List<NonRealTimeConsultSessionVo> sessionInfo = nonRealTimeConsultUserService.selectByNonRealTimeConsultSessionVo(sessionVo);
        if(sessionInfo.size()>0){
            if(StringUtils.isNotNull(doctorId)){
                nonRealTimeConsultUserService.savenConsultRecord(sessionid,doctorId, source, content,msgType);
            }else{
                nonRealTimeConsultUserService.savenConsultRecord(sessionid,openid, source, content,msgType);
                doctorId = sessionInfo.get(0).getCsUserId();
            }
            resultMap.put("state","success");
        }else{
            resultMap.put("state","error");
            resultMap.put("result_info","未找到相应的会话");
        }

        //通知相关医生来回答--模板消息
        Map parameter = systemService.getDoctorWechatParameter();
        String token = (String) parameter.get("token");
        ConsultDoctorInfoVo doctorInfoVo = consultDoctorInfoService.getConsultDoctorInfoByUserId(doctorId);
//        WechatUtil.sendMsgToWechat(token,doctorInfoVo.getOpenId(),"你有问题了 ,赶快去回到吧");

        String data = "{ \"first\": {\"value\":代办事项\n\",\"color\":\"#173177\"},\"keynote1\":{\"value\":\"+doctorInfoVo.getName()+\"医生您好， 您有新消息\n" +
                "\",\"color\":\"#173177\"}, \"keynote2\": {\"value\":\"用户向您咨询，请尽快回复。\", \"color\":\"#173177\"}, \"remark\":{ \"value\":\"优先级很高哦！\", \"color\":\"#173177\" }";
        WechatUtil.sendTemplateMsgToUser(token,doctorInfoVo.getOpenId(),"APZhFvwnuhFL9TA-ufQo5xJxG4y1bM2J9anNsmnzvXM",data);


        //通知前台更新聊天记录
        Map<String,Object> conversationData = new HashMap<String, Object>();
        conversationData.put("message",content);
        conversationData.put("messageTime",DateUtils.formatDateToStr(new Date(),"MM月dd日 HH:mm"));
        conversationData.put("messageType",msgType);
        conversationData.put("source",source);
        resultMap.put("conversationData",conversationData);
        return resultMap;
    }
}
