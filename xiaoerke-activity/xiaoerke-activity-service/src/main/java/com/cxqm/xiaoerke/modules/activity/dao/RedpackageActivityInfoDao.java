package com.cxqm.xiaoerke.modules.activity.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.activity.entity.RedpackageActivityInfoVo;

import java.util.List;

@MyBatisDao
public interface RedpackageActivityInfoDao {
    int deleteByPrimaryKey(String id);

    int insert(RedpackageActivityInfoVo record);

    int insertSelective(RedpackageActivityInfoVo record);

    RedpackageActivityInfoVo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RedpackageActivityInfoVo record);

    int updateByPrimaryKey(RedpackageActivityInfoVo record);

    List<RedpackageActivityInfoVo> getRedpackageActivityBySelective(RedpackageActivityInfoVo vo);

    int getRedPackageActivityTatalCount();
}