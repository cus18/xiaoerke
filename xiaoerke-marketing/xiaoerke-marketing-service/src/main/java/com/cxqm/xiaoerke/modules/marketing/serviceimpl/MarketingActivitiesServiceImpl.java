package com.cxqm.xiaoerke.modules.marketing.serviceimpl;

import java.util.List;
import java.util.Map;

import com.cxqm.xiaoerke.modules.marketing.dao.MarketingActivitiesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.modules.marketing.entity.MarketingActivities;
import com.cxqm.xiaoerke.modules.marketing.service.MarketingActivitiesService;


@Service
@Transactional(readOnly = false)
public class MarketingActivitiesServiceImpl implements
		MarketingActivitiesService {

	@Autowired
	MarketingActivitiesDao marketingActivitiesDao;

	@Override
	public List<Map<String, Object>> getMarketingActivitiesByOpenid(
			String openid, String score) {
		// TODO Auto-generated method stub
		return marketingActivitiesDao.getMarketingActivitiesByOpenid(openid,score);
	}

	@Override
	public Integer saveMarketingActivities(
			MarketingActivities marketingActivities) {
		// TODO Auto-generated method stub
		return marketingActivitiesDao.saveMarketingActivities(marketingActivities);
	}

	@Override
	public Integer updateMarketingActivities(
			MarketingActivities marketingActivities) {
		// TODO Auto-generated method stub
		return marketingActivitiesDao.updateMarketingActivities(marketingActivities);
	}



}
