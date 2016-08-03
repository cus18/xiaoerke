package com.cxqm.xiaoerke.modules.activity.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGamesVo;

import java.util.List;

@MyBatisDao
public interface OlyBabyGamesDao {
    int deleteByPrimaryKey(Integer id);

    int insert(OlyBabyGamesVo record);

    int insertSelective(OlyBabyGamesVo record);

    OlyBabyGamesVo selectByOlyBabyGamesVo(OlyBabyGamesVo olyBabyGamesVo);

    int updateByPrimaryKeySelective(OlyBabyGamesVo record);

    int updateByPrimaryKey(OlyBabyGamesVo record);

    int updateByOpenId(OlyBabyGamesVo record);

    String getMarketerByOpenid(String openId);

    /**
     * 获取日期最近的5个用户的奖品列表
     * sunxiao
     * @return
     */
    List<OlyBabyGamesVo> getUserPrizeList();
}