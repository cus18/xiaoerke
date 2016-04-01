package com.cxqm.xiaoerke.modules.plan.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.plan.entity.PlanInfo;

@MyBatisDao
public interface PlanInfoDao {
    int deleteByPrimaryKey(Long id);

    int insert(PlanInfo record);

    int insertSelective(PlanInfo record);

    PlanInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PlanInfo record);

    int updateByPrimaryKey(PlanInfo record);
    
    List<Map<String,Object>> getPlanInfoByUserId(Map map);
    
    String getUserLastFoodList(@Param("userId")String userId);
}