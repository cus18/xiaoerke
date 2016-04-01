package com.cxqm.xiaoerke.modules.sys.dao;

import java.util.HashMap;
import java.util.Map;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.sys.entity.MsgAppointmentInfo;

@MyBatisDao
public interface MsgAppointmentInfoDao {
    int deleteByPrimaryKey(String id);

    int insert(MsgAppointmentInfo record);

    int insertSelective(MsgAppointmentInfo record);

    MsgAppointmentInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MsgAppointmentInfo record);

    int updateByPrimaryKey(MsgAppointmentInfo record);
    
    int addMsgAppointment(Map<String, Object> executeMap);
    
    HashMap<String,Object> msgAppointmentStatus(HashMap<String, Object> executeMap);
}