package com.cxqm.xiaoerke.modules.activity.service;

import com.cxqm.xiaoerke.modules.activity.entity.PunchCardRecordsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by jiangzhongge on 2017-3-17.
 */
public interface PunchCardRecordsService {

    int deleteByPrimaryKey(String id);

    int insert(PunchCardRecordsVo record);

    int insertSelective(PunchCardRecordsVo record);

    PunchCardRecordsVo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PunchCardRecordsVo record);

    int updateByPrimaryKey(PunchCardRecordsVo record);

    int getSelfPunchCardCount(String openId);

    List<PunchCardRecordsVo> getSelfPunchCardRecords(PunchCardRecordsVo vo);

    List<PunchCardRecordsVo> getLastPunchCardRecord(PunchCardRecordsVo vo);

    List<Map<String,Object>> getTodayPunchCardRecords(Map map);

}
