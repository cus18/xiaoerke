package com.cxqm.xiaoerke.modules.operation.dao;

import java.util.HashMap;
import java.util.List;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.operation.entity.SysLog;
import com.cxqm.xiaoerke.modules.operation.entity.SysLogWithBLOBs;

@MyBatisDao
public interface SysLogDao {
    int deleteByPrimaryKey(String id);

    int insert(SysLogWithBLOBs record);

    int insertSelective(SysLogWithBLOBs record);

    SysLogWithBLOBs selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysLogWithBLOBs record);

    int updateByPrimaryKey(SysLog record);
    
    List<HashMap<String,Object>> getUserListYesterday(String date);
    
    HashMap<String,Object> getUserOperationStatistic(HashMap<String,Object> data);
}