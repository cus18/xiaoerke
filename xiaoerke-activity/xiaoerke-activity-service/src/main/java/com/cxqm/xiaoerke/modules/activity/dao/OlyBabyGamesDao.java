package com.cxqm.xiaoerke.modules.activity.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGamesVo;
@MyBatisDao
public interface OlyBabyGamesDao {
    int deleteByPrimaryKey(Integer id);

    int insert(OlyBabyGamesVo record);

    int insertSelective(OlyBabyGamesVo record);

    OlyBabyGamesVo selectByOlyBabyGamesVo(OlyBabyGamesVo olyBabyGamesVo);

    int updateByPrimaryKeySelective(OlyBabyGamesVo record);

    int updateByPrimaryKey(OlyBabyGamesVo record);
}