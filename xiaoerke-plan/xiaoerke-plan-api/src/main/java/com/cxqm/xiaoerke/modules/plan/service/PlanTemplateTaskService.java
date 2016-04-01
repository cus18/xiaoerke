package com.cxqm.xiaoerke.modules.plan.service;

import java.util.List;
import java.util.Map;


public interface PlanTemplateTaskService {

	 
    /**
     * 获取模板列表
     * 张博
     * @return
     */
    public List<Map<String, Object>> getPlanTemplateTaskListByPlanTemplateId(String templateId);
	
}
