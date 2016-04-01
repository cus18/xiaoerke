package com.cxqm.xiaoerke.modules.plan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cxqm.xiaoerke.modules.plan.entity.PlanInfoTask;
public interface PlanInfoTaskService {
	
	void updatePlanTask(Map<String, Object> map);
	
	void savePlanTask(Map<String, Object> map);

	 List<Map<String,Object>> getPlanInfoTaskListByPlanInfoId(String planInfoId);
	 
	 HashMap<String, Object> getTasksInfo(String type);
	 
	 List<PlanInfoTask> getPlanInfoTaskByInfo(PlanInfoTask planInfoTask);
}
