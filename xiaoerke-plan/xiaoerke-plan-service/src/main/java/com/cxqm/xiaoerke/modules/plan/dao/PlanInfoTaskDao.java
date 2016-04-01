package com.cxqm.xiaoerke.modules.plan.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.plan.entity.PlanInfoTask;

@MyBatisDao
public interface PlanInfoTaskDao {
    int deleteByPrimaryKey(Long id);

    int insert(PlanInfoTask record);

    int insertSelective(Map<String, Object> map);

    PlanInfoTask selectByPrimaryKey(Long id);
    
    List<PlanInfoTask> selectByInfo(PlanInfoTask planInfoTask);

    int updateByPrimaryKeySelective(Map<String, Object> map);

    int updateByPrimaryKey(PlanInfoTask record);
    
    List<Map<String,Object>> getPlanInfoTaskListByPlanInfoId(@Param("planInfoId")String planInfoId);
    
    void updatePlanTask(PlanInfoTask planInfoTask);
}