package com.cxqm.xiaoerke.modules.activity.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.activity.entity.PunchCardRewardDataVo;

import java.util.List;

@MyBatisDao
public interface PunchCardRewardDataDao {
    int deleteByPrimaryKey(String id);

    int insert(PunchCardRewardDataVo record);

    int insertSelective(PunchCardRewardDataVo record);

    PunchCardRewardDataVo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PunchCardRewardDataVo record);

    int updateByPrimaryKey(PunchCardRewardDataVo record);

    List<PunchCardRewardDataVo> getMoreDataBySelective(PunchCardRewardDataVo record);

    int batchInsertPunchCardData(List list);
}