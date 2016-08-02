package com.cxqm.xiaoerke.modules.activity.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGameDetailVo;
@MyBatisDao
public interface OlyBabyGameDetailDao {
    int deleteByPrimaryKey(Integer id);

    int insert(OlyBabyGameDetailVo record);

    int insertSelective(OlyBabyGameDetailVo record);

    OlyBabyGameDetailVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OlyBabyGameDetailVo record);

    int updateByPrimaryKey(OlyBabyGameDetailVo record);
}