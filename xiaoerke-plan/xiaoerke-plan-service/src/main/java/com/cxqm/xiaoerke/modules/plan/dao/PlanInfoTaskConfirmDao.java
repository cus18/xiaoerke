package com.cxqm.xiaoerke.modules.plan.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.plan.entity.PlanInfoTaskConfirm;


@MyBatisDao
public interface PlanInfoTaskConfirmDao {
    int deleteByPrimaryKey(Long id);

    int insert(PlanInfoTaskConfirm record);

    int insertSelective(PlanInfoTaskConfirm record);

    PlanInfoTaskConfirm selectByPrimaryKey(Long id);

    List<PlanInfoTaskConfirm> getConfirmByInfo(PlanInfoTaskConfirm planInfoTaskConfirm);
    
    int updateByPrimaryKeySelective(PlanInfoTaskConfirm record);

    int updateByPrimaryKey(PlanInfoTaskConfirm record);
    
    PlanInfoTaskConfirm getPlanInfoTaskConfirmByPlanInfoTaskId(@Param("planInfoTaskId")String planInfoTaskId);
}