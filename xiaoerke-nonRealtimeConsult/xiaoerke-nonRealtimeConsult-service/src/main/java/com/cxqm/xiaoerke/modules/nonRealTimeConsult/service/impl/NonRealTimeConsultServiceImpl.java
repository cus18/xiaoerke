package com.cxqm.xiaoerke.modules.nonRealTimeConsult.service.impl;

import com.alibaba.fastjson.JSON;
import com.cxqm.xiaoerke.common.utils.OSSObjectTool;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLDecoder;
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
    public HashMap<String, Object> createSession(String csUserId,String openid,String content) {
        HashMap<String,Object> resultMap = new HashMap<String, Object>();
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
        recordVo.setMessageType("create");
        recordVo.setSenderId(openid);
        recordVo.setSysUserId(openid);
        recordVo.setUserName(attentionInfo.getNickname());
        nonRealTimeConsultRecordDao.insertSelective(recordVo);
        resultMap.put("sessionId",sessionVo.getId());
        return resultMap;
    }

    @Override
    public void updateConsultDoctorInfo() {
        nonRealTimeConsultSessionDao.updateEvaluateEarchDay();
        nonRealTimeConsultSessionDao.updateConsultNumEarchDay();

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
        recordVo.setMessageType("send");
        recordVo.setSenderId(userId);
        recordVo.setSysUserId(sessionVo.getCsUserId());
        recordVo.setUserName(sessionVo.getUserName());
        nonRealTimeConsultRecordDao.insertSelective(recordVo);
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

}
