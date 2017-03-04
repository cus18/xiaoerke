package com.cxqm.xiaoerke.modules.activity.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.activity.entity.RedpackageResultsInfoVo;

import java.util.List;

@MyBatisDao
public interface RedpackageResultsInfoDao {
    int deleteByPrimaryKey(String id);

    int insert(RedpackageResultsInfoVo record);

    int insertSelective(RedpackageResultsInfoVo record);

    RedpackageResultsInfoVo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RedpackageResultsInfoVo record);

    int updateByPrimaryKey(RedpackageResultsInfoVo record);

    List<RedpackageResultsInfoVo> getRedPackageResultsBySelective(RedpackageResultsInfoVo record);

}