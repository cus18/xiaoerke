package com.cxqm.xiaoerke.modules.nonRealTimeConsult.service.impl;

import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.utils.OSSObjectTool;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultDoctorDepartmentDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorDepartmentVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorInfoVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultDoctorInfoService;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.dao.NonRealTimeConsultRecordDao;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.dao.NonRealTimeConsultSessionDao;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.entity.ConsultSessionStatus;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.entity.NonRealTimeConsultRecordVo;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.entity.NonRealTimeConsultSessionVo;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.service.NonRealTimeConsultService;
import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.service.BabyBaseInfoService;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.WechatMessageUtil;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
@Service
@Transactional(readOnly = false)
public class NonRealTimeConsultServiceImpl implements NonRealTimeConsultService {

    @Autowired
    private NonRealTimeConsultSessionDao nonRealTimeConsultSessionDao;

    @Autowired
    private NonRealTimeConsultRecordDao nonRealTimeConsultRecordDao;

    @Autowired
    private ConsultDoctorDepartmentDao consultDoctorDepartmentDao;

    @Autowired
    private ConsultDoctorInfoService consultDoctorInfoService;

    @Autowired
    private BabyBaseInfoService babyBaseInfoService;

    @Autowired
    private WechatAttentionService wechatAttentionService;

    @Autowired
    private PatientRegisterPraiseService patientRegisterPraiseService;

    @Autowired
    private SystemService systemService;


    @Autowired
    private SysPropertyServiceImpl sysPropertyService;


    @Override
    public List<NonRealTimeConsultSessionVo> selectByNonRealTimeConsultSessionVo(NonRealTimeConsultSessionVo realTimeConsultSessionVo){
        return nonRealTimeConsultSessionDao.selectByNonRealTimeConsultSessionVo(realTimeConsultSessionVo);
    }
    @Override
    public List<NonRealTimeConsultRecordVo> selectSessionRecordByVo(NonRealTimeConsultRecordVo nonRealTimeConsultRecordVo){
        return nonRealTimeConsultRecordDao.selectSessionRecordByVo(nonRealTimeConsultRecordVo);
    }

    @Override
    public List<ConsultDoctorDepartmentVo> departmentList() {
        return consultDoctorDepartmentDao.getShowDepartmentList();
    }

    @Override
    public List<ConsultDoctorInfoVo> starDoctorList() {
        Map<String,Object> searchMap = new HashMap<String, Object>();
        searchMap.put("startDoctor","1");
        return consultDoctorInfoService.getConsultDoctorByInfo(searchMap);
    }

    @Override
    public HashMap<String, Object> starDoctorInfo(String userId) {
        return consultDoctorInfoService.getConsultDoctorHomepageInfo(userId);
    }

    @Override
    public BabyBaseInfoVo babyBaseInfo(String openid) {
        return babyBaseInfoService.selectLastBabyInfo(openid);
    }

    @Override
    public void saveBabyBaseInfo(BabyBaseInfoVo babyBaseInfoVo) {
        babyBaseInfoService.insertSelective(babyBaseInfoVo);
    }

    @Override
    public HashMap<String, Object> createSession(String csUserId,String openid,String content) {
        HashMap<String,Object> resultMap = new HashMap<String, Object>();
//        查询医生基本信息
        ConsultDoctorInfoVo doctorvo = consultDoctorInfoService.getConsultDoctorInfoByUserId(csUserId);
        WechatAttention attentionInfo  = wechatAttentionService.getAttentionByOpenId(openid);
        if(null == attentionInfo){
            attentionInfo = new WechatAttention();
        }
        //创建新会话
        NonRealTimeConsultSessionVo sessionVo = new NonRealTimeConsultSessionVo();
        Date nowTime = new Date();
        sessionVo.setCreateTime(nowTime);
        sessionVo.setLastMessageTime(nowTime);
        sessionVo.setUpdateTime(nowTime);
        sessionVo.setSource("weixin");
        sessionVo.setLastMessageContent(content);
        sessionVo.setLastMessageType(ConsultSessionStatus.CREATE_SESSION.getVariable());
        sessionVo.setDoctorDepartmentName(doctorvo.getDepartment());
        sessionVo.setDoctorProfessor(doctorvo.getSkill());
        sessionVo.setCsUserId(csUserId);
        sessionVo.setCsUserName(doctorvo.getName());
        sessionVo.setUserId(openid);
        sessionVo.setUserName(attentionInfo.getNickname());
        nonRealTimeConsultSessionDao.insertSelective(sessionVo);

        //创建新聊天内容
        NonRealTimeConsultRecordVo recordVo = new NonRealTimeConsultRecordVo();
        recordVo.setSessionId(sessionVo.getId());
        recordVo.setCreateTime(nowTime);
        recordVo.setCsUserId(csUserId);
        recordVo.setDoctorName(doctorvo.getName());
        recordVo.setMessage(content);
        recordVo.setMessageType(ConsultSessionStatus.CREATE_SESSION.getVariable());
        recordVo.setSenderId(openid);
        recordVo.setSysUserId(openid);
        recordVo.setUserName(attentionInfo.getNickname());
        nonRealTimeConsultRecordDao.insertSelective(recordVo);
        resultMap.put("sessionId",sessionVo.getId());

        //通知相关医生来回答--模板消息
//        sendRemindDoctor(csUserId,sessionVo.getUserName(),String.valueOf(sessionVo.getId()));
        return resultMap;
    }

    @Override
    public void updateConsultDoctorInfo() {
        nonRealTimeConsultSessionDao.updateEvaluateEarchDay();
        nonRealTimeConsultSessionDao.updateConsultNumEarchDay();
    }

    @Override
    public void updateConsultSessionInfo(NonRealTimeConsultSessionVo consultSessionVo) {
        nonRealTimeConsultSessionDao.updateByPrimaryKeySelective(consultSessionVo);
    }

    @Override
    public void savenConsultRecord(Integer sessionid, String userId, String fromType, String content, String msgtype) {
        //创建新聊天内容
        NonRealTimeConsultSessionVo sessionVo = nonRealTimeConsultSessionDao.selectByPrimaryKey(sessionid);
        Date nowTime = new Date();
        NonRealTimeConsultRecordVo recordVo = new NonRealTimeConsultRecordVo();
        recordVo.setSessionId(sessionid);
        recordVo.setCreateTime(nowTime);
        recordVo.setCsUserId(sessionVo.getCsUserId());
        recordVo.setDoctorName(sessionVo.getCsUserName());
        recordVo.setMessage(content);
        recordVo.setMessageType(msgtype);
        recordVo.setSenderId(userId);
        recordVo.setSysUserId(sessionVo.getUserId());
        recordVo.setUserName(sessionVo.getUserName());
        nonRealTimeConsultRecordDao.insertSelective(recordVo);

//        更新session的last字段
        sessionVo.setLastMessageType(fromType);
        sessionVo.setLastMessageContent(content);
        sessionVo.setLastMessageTime(nowTime);
        if("doctor".equals(fromType)){}
        sessionVo.setBak1("isAnswer");
        nonRealTimeConsultSessionDao.updateByPrimaryKeySelective(sessionVo);


    }

    @Override
    public HashMap<String, Object> uploadMediaFile(MultipartFile file) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        if(file !=null && ! file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            Map<String, Object> msgMap;
            try {
                InputStream inputStream = file.getInputStream();
                OSSObjectTool.uploadFileInputStream(fileName, file.getSize(), inputStream, OSSObjectTool.BUCKET_CONSULT_PIC);
                resultMap.put("status","success");
                resultMap.put("imgPath","http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/"+fileName);
                return resultMap;
            } catch (Exception e) {
                e.printStackTrace();
                resultMap.put("status","failure");
            }
        }
        return resultMap;
    }

    @Override
    public List<ConsultDoctorInfoVo> getDoctorListByDepartment(String departmentName) {
        ConsultDoctorInfoVo doctorInfoVo = new ConsultDoctorInfoVo();
        doctorInfoVo.setDepartment(departmentName);
        doctorInfoVo.setNonrealtimeStatus("1");
        return consultDoctorInfoService.findManagerDoctorInfoBySelective(doctorInfoVo);
    }

    @Override
    public List<ConsultDoctorInfoVo> getRecentTimeList(String openid) {
        ConsultDoctorInfoVo doctorInfoVo = new ConsultDoctorInfoVo();
        doctorInfoVo.setOpenId(openid);
        doctorInfoVo.setNonrealtimeStatus("1");
        return consultDoctorInfoService.getRecentTimeList(doctorInfoVo);
    }

    @Override
    public void sessinTimeOut() {
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        Map parameter = systemService.getWechatParameter();
        String token = (String) parameter.get("token");
        List<Map<String,Object>> sessionOutList = nonRealTimeConsultSessionDao.getTimeOutSessionInfo();
        for(Map<String,Object> sessionInfo:sessionOutList){
            String openid = (String) sessionInfo.get("openid");
            String infoPath = sysPropertyVoWithBLOBsVo.getTitanWebUrl()+"/titan/nonRealTimeConsult#/NonTimeUserConversation/"+sessionInfo.get("sessonId");
            String evalPath = sysPropertyVoWithBLOBsVo.getKeeperWebUrl() +"keeper/wxPay/patientPay.do?serviceType=customerPay&customerId="+sessionInfo.get("customerId")+"&sessionId="+sessionInfo.get("sessonId");
            String doctorInfo =(String)sessionInfo.get("department")+sessionInfo.get("name");

            String content = "您好，您和"+doctorInfo+"医生的会话已达到最大会话时长（36小时）\n系统将自动关闭。您对医生的回复还满意吗？\n<a href='"+evalPath+"'>评价送心意</a> |  <a href='"+infoPath+"'>查看详情</a> \n";
            WechatUtil.sendMsgToWechat(token,openid,content);
        }
        nonRealTimeConsultSessionDao.sessinTimeOut();
    }



        //记录评价信息
    @Override
    public void saveCustomerEvaluation(String openid, String doctorid, String sessionid) {
        Map<String, Object> evaluationMap = new HashMap<String, Object>();
        evaluationMap.put("openid", openid);
        evaluationMap.put("uuid", IdGen.uuid());
        evaluationMap.put("starNum1", 0);
        evaluationMap.put("starNum2", 0);
        evaluationMap.put("starNum3", 0);
        evaluationMap.put("doctorId", doctorid);
        evaluationMap.put("content", "");
        evaluationMap.put("dissatisfied", null);
        evaluationMap.put("redPacket", null);
        evaluationMap.put("consultSessionId", sessionid);
        evaluationMap.put("evaluateSource", "nonRealtimeConsult");
        patientRegisterPraiseService.saveCustomerEvaluation(evaluationMap);
    }

    @Override
    public String getNonRealtimeCustomerId(Integer sessionid){
        return patientRegisterPraiseService.getNonRealtimeCustomerId(sessionid);
    }

    @Override
    public void sendRemindDoctor(String doctorId, String userName, String sessionId) {
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        Map parameter = systemService.getDoctorWechatParameter();
        String token = (String) parameter.get("token");
        ConsultDoctorInfoVo doctorInfoVo = consultDoctorInfoService.getConsultDoctorInfoByUserId(doctorId);
       if(doctorInfoVo!=null){
           String title = null==doctorInfoVo.getName()?"":doctorInfoVo.getName()+"医生您好， 您有新消息";
           String url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "titan/nonRealTimeConsult#/NonTimeDoctorConversation/"+sessionId;
           WechatMessageUtil.templateModel(title, userName+"向您咨询，请尽快回复。", "", "", "", "   很高哦^_^", token, url, doctorInfoVo.getOpenId(), sysPropertyVoWithBLOBsVo.getTemplateIdForDoc());

       }

    }

    @Override
    public void sendRemindUser(NonRealTimeConsultSessionVo nonRealTimeConsultSessionVo) {
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        Map parameter = systemService.getWechatParameter();
        String token = (String) parameter.get("token");
        String url = sysPropertyVoWithBLOBsVo.getTitanWebUrl() + "titan/nonRealTimeConsult#/NonTimeUserConversation/"+nonRealTimeConsultSessionVo.getId();
        Map param = new HashMap();
        param.put("userId",nonRealTimeConsultSessionVo.getCsUserId());
        List<ConsultDoctorInfoVo> consultDoctorInfoVos = consultDoctorInfoService.getConsultDoctorByInfo(param);
        String title = "您好，("+consultDoctorInfoVos.get(0).getDepartment()+")("+nonRealTimeConsultSessionVo.getCsUserName()+" )回复了你的提问。\n"+ "<a href='"+url+"'>点击查看详细回复</a>";
        WechatUtil.sendMsgToWechat(token,nonRealTimeConsultSessionVo.getUserId(),title);

    }

    @Override
    public NonRealTimeConsultSessionVo getSessionInfoById(Integer id){
        return  nonRealTimeConsultSessionDao.selectByPrimaryKey(id);
    }

}
