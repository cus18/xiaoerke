package com.cxqm.xiaoerke.modules.activity.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.activity.entity.PunchCardDataVo;

import java.util.List;

@MyBatisDao
public interface PunchCardDataDao {

    int deleteByPrimaryKey(String id);

    int insert(PunchCardDataVo record);

    int insertSelective(PunchCardDataVo record);

    PunchCardDataVo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PunchCardDataVo record);

    int updateByPrimaryKey(PunchCardDataVo record);

    List<PunchCardDataVo> getLastOneData();
}