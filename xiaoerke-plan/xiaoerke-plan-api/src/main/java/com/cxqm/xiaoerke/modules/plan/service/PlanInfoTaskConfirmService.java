package com.cxqm.xiaoerke.modules.plan.service;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.modules.plan.entity.PlanInfo;
import com.cxqm.xiaoerke.modules.plan.entity.PlanInfoTask;
import com.cxqm.xiaoerke.modules.plan.entity.PlanInfoTaskConfirm;

@Transactional(readOnly = false)
public interface PlanInfoTaskConfirmService {

	/**
	 * 打卡
	 * 张博
	 * @param planInfoTaskConfirm
	 * @return
	 */
	public boolean punchTicket(long planInfoTaskId);
	
	/**
	 * 打卡详情
	 * sunxiao
	 * @param planInfoTaskConfirm
	 * @return
	 */
	HashMap<String, Object> confirmDetail(PlanInfoTask planInfoTask);
	
	/**
	 * 获取打卡情况
	 * sunxiao
	 * @return
	 */
	String getConfirmSituation(PlanInfo planInfo);
	
	
	 /**
	  * 通过PlanInfoTaskId获取PlanInfoTaskConfirm实体
	  * 张博
	  * @param planInfoTaskId
	  * @return
	  */
	PlanInfoTaskConfirm getPlanInfoTaskConfirmByPlanInfoTaskId(@Param("planInfoTaskId")String planInfoTaskId);
	
	/**
	 * 任务列表获取打卡情况
	 * sunxiao
	 * @param planInfoId
	 * @return
	 */
	HashMap<String, Object> taskListConfirm(String planInfoId);
}
