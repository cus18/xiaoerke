package com.cxqm.xiaoerke.modules.plan.service;


import com.cxqm.xiaoerke.modules.plan.entity.HealthPlanAddItemVo;

import java.util.List;

public interface PlanMessageService {

	public void everyMorningSendSMS();
	
	public void TimingSendWechatMessage();
	
	public void TimingSendWechatMessageByPunchTicket();
	
	public void nutritionManagementSendWechatMessage();

	List<HealthPlanAddItemVo> findHealthPlanAddItem(HealthPlanAddItemVo healthPlanAddItemVo) throws Exception;

	int insertHealthPlanAddItem(HealthPlanAddItemVo healthPlanAddItemVo) throws Exception;
}
