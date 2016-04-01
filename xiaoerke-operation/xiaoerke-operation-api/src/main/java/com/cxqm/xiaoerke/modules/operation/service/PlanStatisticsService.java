package com.cxqm.xiaoerke.modules.operation.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

public interface PlanStatisticsService {

	public Map<String,Object>  findPlanData(String startDate);
	
	/**
	 * 用户留存
	 * @param date
	 * @return
	 */
	int userStayForDays(String startDate,String endDate,List<String> openid);
	
	/**
	 * 查看单位时间内参与计划的openid
	 * @param startDate 
	 * @return
	 */
	List<String> findPlanOpenids(Map<String, Object> map);

}
