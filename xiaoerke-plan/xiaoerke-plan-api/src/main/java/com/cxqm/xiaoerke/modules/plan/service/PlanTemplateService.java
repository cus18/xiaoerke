package com.cxqm.xiaoerke.modules.plan.service;

import java.util.List;
import java.util.Map;

public interface PlanTemplateService {
	/**
	 * 获取任务模板列表
	 * @return
	 */
	public List<Map<String,Object>> getPlanTemplateList(String type); 
	
}
