package com.cxqm.xiaoerke.modules.activity.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGamesVo;

import java.util.List;
import java.util.Map;

/**
 * 抽奖dao
 * sunxiao
 * 2016-08-02
 */
@MyBatisDao
public interface OlyGamePrizeDao {

    /**
     * 获取所有奖品列表
     * sunxiao
     * @return
     */
    List<Map<String,Object>> getOlyGamePrizeList(Map<String, Object> prizeMap);

    /**
     * 获取日期最近的5个用户的奖品列表
     * sunxiao
     * @return
     */
    public List<OlyBabyGamesVo> getUserPrizeList();

    /**
     * 更新奖品信息
     * sunxiao
     * @return
     */
    void updateOlyGamePrizeInfo(Map<String, Object> prizeMap);
}