package com.cxqm.xiaoerke.modules.marketing.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.marketing.entity.MarketingActivities;
@MyBatisDao
public interface MarketingActivitiesDao {

	public List<Map<String,Object>>  getMarketingActivitiesByOpenid(@Param("openid")String openid,@Param("score")String score);
	
	public Integer saveMarketingActivities(MarketingActivities marketingActivities);
	
	public Integer updateMarketingActivities(MarketingActivities marketingActivities);
	
}
