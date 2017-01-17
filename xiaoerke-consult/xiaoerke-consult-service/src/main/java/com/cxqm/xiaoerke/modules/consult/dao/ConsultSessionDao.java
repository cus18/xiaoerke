package com.cxqm.xiaoerke.modules.consult.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MyBatisDao
public interface ConsultSessionDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ConsultSession consultSession);

    int insertSelective(ConsultSession consultSession);

    ConsultSession selectByPrimaryKey(Integer id);

    ConsultSession selectByOpenid(String openid);

    ConsultSession selectConsultDurationByOpenid(String openid);

    int updateByPrimaryKeySelective(ConsultSession consultSession);

    int updateSessionInfoByUserId(ConsultSession consultSession);

    int updateByPrimaryKey(ConsultSession consultSession);

    List<Map<String,Object>> selectByPrOpenid(String openId);
    
    List<ConsultSession> selectBySelective(ConsultSession consultSession);

    List<ConsultSession> selectBySelectiveOrder(ConsultSession consultSession);


    List<RichConsultSession> selectRichConsultSessions(RichConsultSession consultSession);

    List<ConsultSession> getCsUserByUserId(ConsultSession consultSession);

    List<HashMap<String,Object>> getOnlineCsListInfo(List<String> userList);

    List<ConsultSession> getAlreadyAccessUsers(ConsultSession consultSession);

    HashMap<String,Object> getAttention(String openId);

    //根据条件查询接待人数 sunxiao
    Integer getConsultSessionUserCount(Map map);

    Integer getConsultSessionByUserId(Map map);

    //根据条件查询会话列表 sunxiao
    List<ConsultSession> getConsultSessionListByInfo(Map map);

    int insertConsultSessionBatch(List<ConsultSession> consultSessionses);

    //根据医生姓名查询咨询医生次数
    Map<String,Object> getConsultCountsByDoctorName(String marketer);
    //根据marketer查询医生信息
    Map<String,Object> getDoctorInfoByMarketer(String marketer);
}