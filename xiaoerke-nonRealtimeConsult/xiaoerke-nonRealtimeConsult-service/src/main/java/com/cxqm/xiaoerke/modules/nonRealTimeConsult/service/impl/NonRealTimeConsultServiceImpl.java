package com.cxqm.xiaoerke.modules.nonRealTimeConsult.service.impl;

import com.cxqm.xiaoerke.modules.consult.dao.ConsultDoctorDepartmentDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorDepartmentVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorInfoVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultDoctorInfoService;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.dao.NonRealTimeConsultRecordDao;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.dao.NonRealTimeConsultSessionDao;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.entity.NonRealTimeConsultRecordVo;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.entity.NonRealTimeConsultSessionVo;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.service.NonRealTimeConsultService;
import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;
import com.cxqm.xiaoerke.modules.sys.service.BabyBaseInfoService;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void createSession(String csUserId,String openid,String content) {

//        查询医生基本信息
        ConsultDoctorInfoVo doctorvo = consultDoctorInfoService.getConsultDoctorInfoByUserId(csUserId);
        WechatAttention attentionInfo  = wechatAttentionService.getAttentionByOpenId(openid);

        //创建新会话
        NonRealTimeConsultSessionVo sessionVo = new NonRealTimeConsultSessionVo();
        Date nowTime = new Date();
        sessionVo.setCreateTime(nowTime);
        sessionVo.setLastMessageTime(nowTime);
        sessionVo.setUpdateTime(nowTime);
        sessionVo.setSource("weixin");
        sessionVo.setLastMessageContent(content);
        sessionVo.setLastMessageType("user");
        sessionVo.setDoctorDepartmentName(doctorvo.getDepartment());
        sessionVo.setDoctorProfessor(doctorvo.getDescription());
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
        recordVo.setMessageType("create");
        recordVo.setSenderId(openid);
        recordVo.setSysUserId(openid);
        recordVo.setUserName(attentionInfo.getNickname());
        nonRealTimeConsultRecordDao.insertSelective(recordVo);
    }

    @Override
    public void updateConsultDoctorInfo() {



    }

    @Override
    public void savenConsultRecord(Integer sessionId,String toUser, String fromUser, String content, String type) {
        //创建新聊天内容
        NonRealTimeConsultSessionVo sessionVo = nonRealTimeConsultSessionDao.selectByPrimaryKey(sessionId);
        Date nowTime = new Date();
        NonRealTimeConsultRecordVo recordVo = new NonRealTimeConsultRecordVo();
        recordVo.setSessionId(sessionId);
        recordVo.setCreateTime(nowTime);
        recordVo.setCsUserId(sessionVo.getCsUserId());
        recordVo.setDoctorName(sessionVo.getCsUserName());
        recordVo.setMessage(content);
        recordVo.setMessageType("send");
        recordVo.setSenderId(fromUser);
        recordVo.setSysUserId(sessionVo.getCsUserId());
        recordVo.setUserName(sessionVo.getUserName());
        nonRealTimeConsultRecordDao.insertSelective(recordVo);
    }


}
