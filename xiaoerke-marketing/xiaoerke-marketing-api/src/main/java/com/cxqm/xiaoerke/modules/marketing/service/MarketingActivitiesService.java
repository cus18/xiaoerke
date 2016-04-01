package com.cxqm.xiaoerke.modules.marketing.service;

import java.util.List;
import java.util.Map;

import com.cxqm.xiaoerke.modules.marketing.entity.MarketingActivities;

public interface MarketingActivitiesService {

	public List<Map<String,Object>>  getMarketingActivitiesByOpenid(String openid,String score);
	
	public Integer saveMarketingActivities(MarketingActivities marketingActivities);
	
	public Integer updateMarketingActivities(MarketingActivities marketingActivities);
	
}
