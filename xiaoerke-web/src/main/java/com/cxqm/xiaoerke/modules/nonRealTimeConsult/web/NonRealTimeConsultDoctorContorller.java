package com.cxqm.xiaoerke.modules.nonRealTimeConsult.web;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.activity.service.OlyGamesService;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorInfoVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultDoctorInfoService;
import com.cxqm.xiaoerke.modules.healthRecords.service.HealthRecordsService;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.entity.ConsultSessionStatus;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.entity.NonRealTimeConsultRecordVo;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.entity.NonRealTimeConsultSessionVo;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.service.NonRealTimeConsultService;
import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.entity.WechatBean;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.service.UserInfoService;
import com.cxqm.xiaoerke.modules.sys.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by Administrator on 2016/09/05 0024.
 */
@Controller
@RequestMapping(value = "nonRealTimeConsultDoctor")
public class NonRealTimeConsultDoctorContorller {

    @Autowired
    private ConsultDoctorInfoService consultDoctorInfoService;

    @Autowired
    private NonRealTimeConsultService nonRealTimeConsultService;

    @Autowired
    private HealthRecordsService healthRecordsService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private OlyGamesService olyGamesService;

    @Autowired
    private UtilService utilService;

    @Autowired
    private NonRealTimeConsultService nonRealTimeConsultUserService;

    @Autowired
    private SystemService systemService;

    @RequestMapping(value = "/test", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public void test(HttpSession session, HttpServletRequest request) {
//        babyCoinInit(session, request);
//        getBabyCoinInfo(session, request);
    }

    /**
     * 获取医生的登陆状态
     * response:
     * {
     * "status":"success"
     * }
     * //success 成功  failure 失败
     *
     * @param session
     * @param request
     * @return
     */
    @RequestMapping(value = "/GetDoctorLoginStatus", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map GetDoctorLoginStatus(HttpSession session, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<String, Object>();
        //根据openid查询当前医生
        Map param = new HashMap();
        String openId = WechatUtil.getOpenId(session, request);//"8ab94e95afe448dab66403fc5407d0ca"
        param.put("openId", openId);
        response.put("status", "failure");
        if (StringUtils.isNotNull(openId)) {
            List<ConsultDoctorInfoVo> consultDoctorInfoVos = consultDoctorInfoService.getConsultDoctorByInfo(param);
            if (consultDoctorInfoVos != null && consultDoctorInfoVos.size() > 0 && StringUtils.isNotBlank(consultDoctorInfoVos.get(0).getUserId())) {
                response.put("status", "success");
                if ("0".equals(consultDoctorInfoVos.get(0).getNonrealtimeStatus())) {
                    response.put("status", "backendClose");
                }
                response.put("csUserId",consultDoctorInfoVos.get(0).getUserId());
            }
        }
        return response;
    }

    /**
     * 医生绑定接口
     * response:
     * {
     * "status":"success"
     * }
     * //success 成功  failure 失败
     *
     * @param session
     * @param request
     * @return
     */
    @RequestMapping(value = "/doctorBinding", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map doctorBinding(@RequestBody Map<String, Object> params, HttpSession session, HttpServletRequest request) throws Exception {
        Map<String, Object> response = new HashMap<String, Object>();
        String username = String.valueOf(params.get("username"));
        String openid = WechatUtil.getOpenId(session, request);//"oogbDwD_2BTQpftPu9QClr-mCw7U"
        String passeord = String.valueOf(params.get("password"));
        String status = utilService.bindUser4ConsultDoctor(username, passeord, openid);
        response.put("status", "failure");
        if (status.equals("1") && StringUtils.isNotNull(passeord) && StringUtils.isNotNull(username)) {
            User user = new User();
            user.setPhone(username);
            user = userInfoService.doctorOper(user);
            ConsultDoctorInfoVo consultDoctorInfoVo = new ConsultDoctorInfoVo();
            consultDoctorInfoVo.setOpenId(openid);
            consultDoctorInfoVo.setUserId(user.getId());
            consultDoctorInfoVo.setNonrealtimeStatus("1");
            int updateFlag = consultDoctorInfoService.updateByphone(consultDoctorInfoVo);

            response.put("status", (updateFlag > 0) ? "success" : "notConsultDoctor");
        }
        return response;
    }

    /**
     * 获取医生的服务（当前服务和全部服务）
     * <p>
     * params:{"serviceType":"current","openId":"123sdfsf"}  //serviceType  currentService 当前服务 allService 全部服务
     * <p>
     * response:S
     * {
     * "status":"success"
     * }
     * //success 成功  failure 失败
     *
     * @param session
     * @param request
     * @return
     */
    @RequestMapping(value = "/GetDoctorService", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map GetDoctorService(@RequestBody Map<String, Object> params, HttpSession session, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<String, Object>();
        String serviceType = (String) params.get("serviceType");
        String csUserId = String.valueOf(params.get("csUserId"));
        String openId = WechatUtil.getOpenId(session, request);
        String babyInfo = "";
        NonRealTimeConsultSessionVo realTimeConsultSessionVo = new NonRealTimeConsultSessionVo();
        realTimeConsultSessionVo.setCsUserId(csUserId);
        if (serviceType.equals("currentService")) {//查询当前服务
            realTimeConsultSessionVo.setStatus("ongoing");
            realTimeConsultSessionVo.setOrder("lastMessageTimeAsc");
        } else {
            realTimeConsultSessionVo.setOrder("lastMessageTimeDesc");
        }
        Date date = new Date();
        date.getTime();
        //查询当前医生的会话信息
        List<NonRealTimeConsultSessionVo> nonRealTimeConsultSessionVos =
                nonRealTimeConsultService.selectByNonRealTimeConsultSessionVo(realTimeConsultSessionVo);
        if (nonRealTimeConsultSessionVos != null && nonRealTimeConsultSessionVos.size() > 0) {
            for (NonRealTimeConsultSessionVo nonRealTimeConsultSessionVo : nonRealTimeConsultSessionVos) {
                if (DateUtils.pastHour(nonRealTimeConsultSessionVo.getLastMessageTime()) > 24) {
                    nonRealTimeConsultSessionVo.setDispalyTimes(DateUtils.DateToStr(nonRealTimeConsultSessionVo.getLastMessageTime(), "monthDate"));
                } else {
                    nonRealTimeConsultSessionVo.setDispalyTimes(DateUtils.DateToStr(nonRealTimeConsultSessionVo.getLastMessageTime(), "time"));
                }
                if (nonRealTimeConsultSessionVo.getStatus().equals("sessionend")) {
                    nonRealTimeConsultSessionVo.setLastMessageType("close");
                }
                String headUrl = olyGamesService.getWechatMessage(nonRealTimeConsultSessionVo.getUserId());
                if (StringUtils.isNotNull(headUrl)) {
                    nonRealTimeConsultSessionVo.setHeadImgUrl(headUrl);
                }
                String lastMessageContent = nonRealTimeConsultSessionVo.getLastMessageContent();
                String sex = "";
                if (lastMessageContent.indexOf("#") != -1) {
                    sex = lastMessageContent.split("#")[0];
                    lastMessageContent = lastMessageContent.split("#")[2];
                    sex = sex.equals("0") ? "男" : "女";
                }
                nonRealTimeConsultSessionVo.setLastMessageContent(lastMessageContent);

                //查询宝宝信息
                List<BabyBaseInfoVo> babyInfoList = healthRecordsService.getUserBabyInfolistByOpenId(nonRealTimeConsultSessionVo.getCsUserId());
                if (babyInfoList != null && babyInfoList.size() > 0) {
                    BabyBaseInfoVo babyBaseInfoVo = babyInfoList.get(0);
                    Date babyBirthday = babyBaseInfoVo.getBirthday();
                    int babyBirthdayYear = babyBirthday.getYear();
                    int babyBirthdayMonth = babyBirthday.getMonth();
                    Date nowDate = new Date();
                    int nowDateYear = nowDate.getYear();
                    int nowDateMonth = nowDate.getMonth();
                    int chaDate = 0;
                    if (nowDateMonth > babyBirthdayMonth) {
                        chaDate = nowDateMonth - babyBirthdayMonth;
                    } else if (babyBirthdayMonth > nowDateMonth) {
                        chaDate = babyBirthdayMonth - nowDateMonth;
                    }
                    String babyName = babyBaseInfoVo.getName();
                    if (StringUtils.isNotNull(babyName)) {
                        babyName = babyBaseInfoVo.getName() + ",";
                    } else {
                        babyName = "";
                    }
                    sex = babyBaseInfoVo.getSex().equals("1") ? "男" : "女";

                    babyInfo = sex + babyName + (nowDateYear - babyBirthdayYear) + "岁" + chaDate + "个月";
                }
                babyInfo = StringUtils.isNull(babyInfo) ? "暂无数据" : babyInfo;

                nonRealTimeConsultSessionVo.setBabyInfo(babyInfo);
            }
        }
        response.put("ListInfo", nonRealTimeConsultSessionVos);
        return response;
    }


    @RequestMapping(value = "/conversationDoctorInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> conversationDoctorInfo(HttpSession session, HttpServletRequest request, @RequestBody Map<String, Object> params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String openid = WechatUtil.getOpenId(session, request);
        Integer sessionid = Integer.parseInt((String) params.get("sessionId"));
        if (!StringUtils.isNotNull(openid)) {
            resultMap.put("state", "error");
            resultMap.put("result_info", "请重新打开页面");
            return resultMap;
        }
        String csUserId = "";
        NonRealTimeConsultSessionVo sessionVo = new NonRealTimeConsultSessionVo();
        sessionVo.setId(sessionid);
        List<NonRealTimeConsultSessionVo> sessionInfo = nonRealTimeConsultUserService.selectByNonRealTimeConsultSessionVo(sessionVo);
        if (sessionInfo.size() > 0) {
            sessionVo = sessionInfo.get(0);
            resultMap.put("doctorName", sessionVo.getCsUserName());
            resultMap.put("doctorId", sessionVo.getCsUserId());
            resultMap.put("professor", sessionVo.getDoctorProfessor());
            resultMap.put("department", sessionVo.getDoctorDepartmentName());
            resultMap.put("sessionStatus", sessionVo.getStatus());
            csUserId = sessionVo.getCsUserId();
        } else {
            resultMap.put("state", "error");
            resultMap.put("result_info", "未找到相应的会话");
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
            if(sessionVo.getCsUserId().equals(vo.getSenderId())){
                recordMap.put("source","user");
            }else{
                recordMap.put("source","doctor");
            };
            String messageType = vo.getMessageType();
            recordMap.put("messageType",messageType);
            if(ConsultSessionStatus.CREATE_SESSION.getVariable().equals(messageType)){
                String[] messageInfo = vo.getMessage().split("\\#");
                recordMap.put("babyBaseInfo",messageInfo[0].equals("0")?"女,"+messageInfo[1]+" ":"男"+","+messageInfo[1]+" ");
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

    /**
     * 获取当前医生与用户的聊天记录
     * <p>
     * params:{"csUserId":"sdfsdfsdfsdfsdf","userId":"123sdfsf"}
     * <p>
     *
     * @param session
     * @param request
     * @return
     */
    @RequestMapping(value = "/GetSessionRecord", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map GetSessionRecord(@RequestBody Map<String, Object> params, HttpSession session, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<String, Object>();
        String csUserId = String.valueOf(params.get("csUserId"));
        String userId = String.valueOf(params.get("userId"));
        NonRealTimeConsultRecordVo nonRealTimeConsultRecordVo = new NonRealTimeConsultRecordVo();
        nonRealTimeConsultRecordVo.setCsUserId(csUserId);
        nonRealTimeConsultRecordVo.setSysUserId(userId);
        List<NonRealTimeConsultRecordVo> nonRealTimeConsultRecordVos = nonRealTimeConsultService.selectSessionRecordByVo(nonRealTimeConsultRecordVo);
        if (nonRealTimeConsultRecordVos != null && nonRealTimeConsultRecordVos.size() > 0) {
            for (NonRealTimeConsultRecordVo vo : nonRealTimeConsultRecordVos) {
                vo.setDisplayTimes(DateUtils.DateToStr(vo.getCreateTime(), "flag1"));
            }
        }
        response.put("nonRealTimeConsultRecordInfo", nonRealTimeConsultRecordVos);
        return response;
    }


}
