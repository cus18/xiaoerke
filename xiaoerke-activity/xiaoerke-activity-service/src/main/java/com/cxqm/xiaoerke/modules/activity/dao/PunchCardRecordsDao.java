package com.cxqm.xiaoerke.modules.activity.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.activity.entity.PunchCardRecordsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisDao
public interface PunchCardRecordsDao {

    int deleteByPrimaryKey(String id);

    int insert(PunchCardRecordsVo record);

    int insertSelective(PunchCardRecordsVo record);

    PunchCardRecordsVo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PunchCardRecordsVo record);

    int updateByPrimaryKey(PunchCardRecordsVo record);

    int getSelfPunchCardCount(@Param(value = "userId") String openId);

    List<PunchCardRecordsVo> getSelfPunchCardRecords(PunchCardRecordsVo vo);

    List<PunchCardRecordsVo> getLastPunchCardRecord(PunchCardRecordsVo vo);

    List<Map<String,Object>> getTodayPunchCardRecords(Map map);

    List<Map<String,Object>> getTodayPayPersonNum(Map map);

}