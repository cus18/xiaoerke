package com.cxqm.xiaoerke.modules.consult.service;


import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface ConsultSessionService {

    List<Map<String,Object>> getConsultInfo(String openId);

    int saveConsultInfo(ConsultSession record);

    int updateSessionInfo(ConsultSession consultSession);

    List<ConsultSession> selectBySelective(ConsultSession consultSession);

    int insertConsultSessionBatch(List<ConsultSession> consultSession);

    List<ConsultSession> getCsUserByUserId(ConsultSession consultSession);

    List<HashMap<String, Object>> getOnlineCsListInfo(List<String> userList);

    String clearSession(String sessionId, String userId);

    //根据条件查询接待人数 sunxiao
    Integer getConsultSessionUserCount(Map map);

    Integer getConsultSessionByUserId(Map map);

    //根据条件查询会话列表 sunxiao
    List<ConsultSession> getConsultSessionListByInfo(Map map);

    ConsultSession selectByPrimaryKey(Integer id);

    //根据医生姓名查询咨询医生次数
    Map<String,Object> getConsultCountsByDoctorName(String marketer);
    //根据marketer查询医生信息
    Map<String,Object> getDoctorInfoByMarketer(String marketer);

    boolean cheakInstantConsultation(String openid);

    ConsultSession selectByOpenid(String openid);

    ConsultSession selectConsultDurationByOpenid(String openid);
}