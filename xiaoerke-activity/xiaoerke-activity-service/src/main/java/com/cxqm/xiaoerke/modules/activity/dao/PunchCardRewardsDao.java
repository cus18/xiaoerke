package com.cxqm.xiaoerke.modules.activity.dao;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.activity.entity.PunchCardRewardsVo;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MyBatisDao
public interface PunchCardRewardsDao {

    int deleteByPrimaryKey(String id);

    int insert(PunchCardRewardsVo record);

    int insertSelective(PunchCardRewardsVo record);

    PunchCardRewardsVo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PunchCardRewardsVo record);

    int updateByPrimaryKey(PunchCardRewardsVo record);

    Map<String,Object> getSelfRewardsInfo(@Param(value = "openId")String openId);

    List<Map<String,Object>> getPunchCardRewards(PunchCardRewardsVo vo);

    int batchInsertPunchCardRewards(List list);

    Page<Map<String,Object>> findPunchCardRewardsByPage(Page<Map<String,Object>> page,
                                                             HashMap<String, Object> hashMap);
}