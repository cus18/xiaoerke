package com.cxqm.xiaoerke.modules.plan.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.modules.plan.dao.PlanInfoDao;
import com.cxqm.xiaoerke.modules.plan.entity.PlanInfo;
import com.cxqm.xiaoerke.modules.plan.entity.PlanInfoTask;
import com.cxqm.xiaoerke.modules.plan.service.PlanInfoService;
import com.cxqm.xiaoerke.modules.plan.service.PlanInfoTaskService;

@Service
@Transactional(readOnly = false)
public class PlanInfoServiceImpl implements PlanInfoService {
	
	@Autowired
	private PlanInfoDao planInfoDao;
	
	@Autowired
	private PlanInfoTaskService planInfoTaskService;
	
	/**
	 * 取消计划
	 * sunxiao
	 * @param planInfo
	 */
	@Override
	public void cancelPlan(PlanInfo planInfo) {
		// TODO Auto-generated method stub
		planInfo.setStatus("completed");
		planInfoDao.updateByPrimaryKeySelective(planInfo);
		PlanInfoTask planInfoTask = new PlanInfoTask();
		planInfoTask.setPlanInfoId(planInfo.getId());
		List<PlanInfoTask> list = planInfoTaskService.getPlanInfoTaskByInfo(planInfoTask);
		for(PlanInfoTask p : list){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id",p.getId());
			map.put("remindMe",false);
			planInfoTaskService.updatePlanTask(map);
		}
	}

	@Override
	public List<Map<String,Object>> getPlanInfoByUserId(Map map) {
		// TODO Auto-generated method stub
		return planInfoDao.getPlanInfoByUserId(map);
	}

	@Override
	public String getUserLastFoodList(String userId) {
		// TODO Auto-generated method stub
		return planInfoDao.getUserLastFoodList(userId);
	}

	@Override
	public void updatePlanInfoById(PlanInfo planInfo) {
		// TODO Auto-generated method stub
		planInfoDao.updateByPrimaryKeySelective(planInfo);
	}

	@Override
	public int savePlanInfo(PlanInfo planInfo) {
		// TODO Auto-generated method stub
		return planInfoDao.insertSelective(planInfo);
	}

     
}
