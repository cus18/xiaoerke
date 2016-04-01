package com.cxqm.xiaoerke.modules.plan.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.modules.plan.dao.PlanTemplateDao;
import com.cxqm.xiaoerke.modules.plan.service.PlanTemplateService;

@Service
@Transactional(readOnly = false)
public class PlanTemplateServiceImpl implements PlanTemplateService {

	@Autowired
	PlanTemplateDao planTemplateDao;
	
	@Override
	public List<Map<String, Object>> getPlanTemplateList(String type) {
		// TODO Auto-generated method stub
		return planTemplateDao.getPlanTemplateList(type);
	}

}
