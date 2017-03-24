package com.cxqm.xiaoerke.modules.activity.service;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.activity.entity.PunchCardRewardsVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangzhongge on 2017-3-17.
 */
public interface PunchCardRewardsService {

    int deleteByPrimaryKey(String id);

    int insert(PunchCardRewardsVo record);

    int insertSelective(PunchCardRewardsVo record);

    PunchCardRewardsVo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PunchCardRewardsVo record);

    int updateByPrimaryKey(PunchCardRewardsVo record);

    Map<String,Object> getSelfRewardsInfo(String openId);

    List<Map<String,Object>> getPunchCardRewards(PunchCardRewardsVo vo);

    int batchInsertPunchCardRewards(List list);

    Page<Map<String,Object>> findPunchCardRewardsByPage(Page<PunchCardRewardsVo> page,
                                                        HashMap<String, Object> hashMap);

}
