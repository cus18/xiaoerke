package com.cxqm.xiaoerke.modules.plan.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.modules.plan.dao.PlanTemplateTaskDao;
import com.cxqm.xiaoerke.modules.plan.service.PlanTemplateTaskService;

@Service
@Transactional(readOnly = false)
public class PlanTemplateTaskServiceImpl implements PlanTemplateTaskService {

	@Autowired
	private PlanTemplateTaskDao planTemplateTaskDao;
	
	
	@Override
	public List<Map<String, Object>> getPlanTemplateTaskListByPlanTemplateId(String templateId) {
		// TODO Auto-generated method stub
		return planTemplateTaskDao.getPlanTemplateTaskListByPlanTemplateId(templateId);
	}

}
