package com.cxqm.xiaoerke.modules.plan.service;

import java.util.List;
import java.util.Map;


import com.cxqm.xiaoerke.modules.plan.entity.PlanInfo;

public interface PlanInfoService {

	/**
	 * 取消计划
	 * sunxiao
	 * @param planInfo
	 */
	void cancelPlan(PlanInfo planInfo);
	
	/**
	 * 通过UserId获取计划实体
	 * 张博
	 * @param userId
	 * @return
	 */
	List<Map<String,Object>> getPlanInfoByUserId(Map map);
	
	/**
	 * 获取用户历史食材
	 * 张博
	 * @param userId
	 * @return
	 */
	String getUserLastFoodList(String userId);
	
	void updatePlanInfoById(PlanInfo planInfo);
	
	int savePlanInfo(PlanInfo planInfo);
}
