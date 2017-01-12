package com.cxqm.xiaoerke.modules.consult.service.impl;


import com.cxqm.xiaoerke.common.utils.ConstantUtil;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultSessionDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = false)
public class ConsultSessionServiceImpl implements ConsultSessionService {

    @Autowired
    private SessionRedisCache sessionRedisCache;

    @Autowired
    private ConsultRecordService consultRecordService;

    @Autowired
    private ConsultSessionDao consultSessionDao;


    @Autowired
    private SysPropertyServiceImpl sysPropertyService;

    private static String dataStr =  DateUtils.DateToStr(new Date(),"date");

//    private static Set<String> instantSet= Collections.synchronizedSet(new HashSet<String>());

    @Override
    public List<Map<String, Object>> getConsultInfo(String openId) {
        return consultSessionDao.selectByPrOpenid(openId);
    }

    @Override
    public int saveConsultInfo(ConsultSession record) {
        return consultSessionDao.insertSelective(record);
    }

    @Override
    public int updateSessionInfo(ConsultSession consultSession) {
        return consultSessionDao.updateByPrimaryKeySelective(consultSession);
    }

    @Override
    public List<ConsultSession> selectBySelective(ConsultSession consultSession) {
        return consultSessionDao.selectBySelective(consultSession);
    }


    @Override
    public int insertConsultSessionBatch(List<ConsultSession> consultSessions){
       return consultSessionDao.insertConsultSessionBatch(consultSessions);
    }



    @Override
    public List<ConsultSession> getCsUserByUserId(ConsultSession consultSession) {
        return consultSessionDao.getCsUserByUserId(consultSession);
    }

    @Override
    public List<HashMap<String, Object>> getOnlineCsListInfo(List<String> userList) {
        return consultSessionDao.getOnlineCsListInfo(userList);
    }

    @Override
    public String clearSession(String sessionId, String userId) {
        try {
            System.out.println("clear session ======" + sessionId + "=======" + userId + "======userId==========");
            //数据库中的consultSession，状态由ongoing变成completed
            ConsultSession consultSession = new ConsultSession();
            consultSession.setId(Integer.parseInt(sessionId));
            consultSession.setUserId(userId);
            consultSession.setStatus(ConsultSession.STATUS_ONGOING);
            List<ConsultSession> consultSessionList = this.selectBySelective(consultSession);
            if (consultSessionList.size() > 0) {
                consultSession = consultSessionList.get(0);
                consultSession.setStatus(ConsultSession.STATUS_COMPLETED);
                this.updateSessionInfo(consultSession);
                System.out.println("consultSessionList.size()=====" + consultSessionList.size() + "consultSessionList.get(0)====" + consultSessionList.get(0));
            }

            //清除redis内的数据
            System.out.println("clear redis=====" + Integer.parseInt(sessionId) + "userId====" + userId);
            sessionRedisCache.removeConsultSessionBySessionId(Integer.parseInt(sessionId));
            sessionRedisCache.removeUserIdSessionIdPair(userId);

            //清除内存内的数据，此处代码有问题，清除session，并不需要清除服务器内存中，用户的channel
            //ConsultSessionManager.getSessionManager().removeUserSession(userId);

            //更新最后一次会话
            consultRecordService.updateConsultSessionStatusVo(new Query().addCriteria(new Criteria().where("sessionId").is(sessionId)), "complete");

            //删除用户的临时聊天记录
            consultRecordService.deleteConsultTempRecordVo(new Query().addCriteria(new Criteria().where("userId").is(userId)));

            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
    }

    //根据条件查询接待人数 sunxiao
    @Override
    public Integer getConsultSessionUserCount(Map map) {
        return consultSessionDao.getConsultSessionUserCount(map);
    }

    @Override
    public Integer getConsultSessionByUserId(Map map) {
        return consultSessionDao.getConsultSessionByUserId(map);
    }

    //根据条件查询会话列表 sunxiao
    @Override
    public List<ConsultSession> getConsultSessionListByInfo(Map map) {
        return consultSessionDao.getConsultSessionListByInfo(map);
    }

    @Override
    public ConsultSession selectByPrimaryKey(Integer id) {
        return consultSessionDao.selectByPrimaryKey(id);
    }

    @Override
    public Map<String,Object> getConsultCountsByDoctorName(String marketer){
        return consultSessionDao.getConsultCountsByDoctorName(marketer);
    }

    @Override
    public Map<String,Object> getDoctorInfoByMarketer(String marketer){
        return consultSessionDao.getDoctorInfoByMarketer(marketer);
    }

    @Override
    public boolean cheakInstantConsultation(String openid) {

        String nowDate = DateUtils.DateToStr(new Date(),"date");
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();

        Date d = new Date();
        int hours = d.getHours();
        if(hours>=Integer.parseInt(sysPropertyVoWithBLOBsVo.getMaxInstantConsultEndTime())||hours<Integer.parseInt(sysPropertyVoWithBLOBsVo.getMaxInstantConsultStartTime())){
            return  false;
        }

        if(!nowDate.equals(dataStr)){
            sessionRedisCache.clearInstantConsultationList();
            this.dataStr = nowDate;
        }
        Long listNum = sessionRedisCache.num4InstantConsultationList();
        Integer maxNum =  Integer.parseInt(sysPropertyVoWithBLOBsVo.getMaxInstantConsultNum());
        if(listNum>=maxNum){
            return false;
        }
        sessionRedisCache.addInstantConsultationList(openid);
        return true;
    }

    @Override
    public ConsultSession selectByOpenid(String openid){
        return consultSessionDao.selectByOpenid(openid);
    }

    @Override
    public ConsultSession selectConsultDurationByOpenid(String openid){
        return consultSessionDao.selectConsultDurationByOpenid(openid);
    }

}