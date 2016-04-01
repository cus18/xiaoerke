package com.cxqm.xiaoerke.modules.operation.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.modules.operation.dao.PlanStatisticsDao;
import com.cxqm.xiaoerke.modules.operation.service.PlanStatisticsService;

@Service
@Transactional(readOnly = false)
public class PlanStatisticsServiceImpl implements PlanStatisticsService {

	@Autowired
	PlanStatisticsDao planStatisticsDao;
	
	@Override
	public Map<String, Object> findPlanData(String startDate) {
		Map<String,Object> result=new HashMap<String,Object>();
		//访问次数
		result.put("visitPlanNums", planStatisticsDao.visitPlanNums(startDate));
		//访问人次
		result.put("visitPlanPeoples", planStatisticsDao.visitPlanPeoples(startDate));
		//新增用户数
		result.put("newUsers", planStatisticsDao.newUsers(startDate));
		//按摩连续打卡用户数
		result.put("continuousPunchUsersForMassage", planStatisticsDao.continuousPunchUsersForMassage(startDate));
		//排便连续打卡用户数
		result.put("continuousPunchUsersForDefecate", planStatisticsDao.continuousPunchUsersForDefecate(startDate));
		//饮食连续打卡用户数
		result.put("continuousPunchUsersForFood", planStatisticsDao.continuousPunchUsersForFood(startDate));
		//运动连续打卡用户数
		result.put("continuousPunchUsersForSport", planStatisticsDao.continuousPunchUsersForSport(startDate));
		//点击购买链接次数
		List<Map<String, Object>> clickShopNums=planStatisticsDao.clickShopNums(startDate);
		for (Map<String, Object> map : clickShopNums) {
				String key=map.get("para").toString();
				if(key.equals("0")){
					result.put("clickDrugNums", map.get("count"));
				}
				if(key.equals("1")){
					result.put("clickFoodNums", map.get("count"));
				}
				if(key.equals("2")){
					result.put("clickBedpanNums", map.get("count"));
				}
				if(key.equals("3")){
					result.put("clickBookNums", map.get("count"));
				}
		}
		//反馈点击
			List<Map<String, Object>> feedBackNums=planStatisticsDao.feedBackNums(startDate);
			for (Map<String, Object> map : feedBackNums) {
					String key=map.get("para").toString();
					if(key.equals("好")){
						result.put("feedBackGoodNums", map.get("count"));
					}
					if(key.equals("轻")){
						result.put("feedBackLightNums", map.get("count"));
					}
					if(key.equals("中")){
						result.put("feedBackMidNums", map.get("count"));
					}
					if(key.equals("重")){
						result.put("feedBackWeightNums", map.get("count"));
					}
					if(key.equals("无")){
						result.put("feedBackNothingNums", map.get("count"));
					}
					if(key.equals("给")){
						result.put("clickLikeNums", map.get("count"));
					}
			}
		return result;
	}

	@Override
	public int userStayForDays(String startDate,String endDate,List<String> openid) {
		int nums=0;
		for (String string : openid) {
			if(planStatisticsDao.punchNums(string, startDate, endDate)>0){
				nums++;
				continue;
			}
			if(planStatisticsDao.saveCommentNums(string, startDate, endDate)>0){
				nums++;
				continue;
			}
			if(planStatisticsDao.cancelPlanNums(string, startDate, endDate)>0){
				nums++;
				continue;
			}
			if(planStatisticsDao.savePlanNums(string, startDate, endDate)>0){
				nums++;
				continue;
			}
			if(planStatisticsDao.updatePlanRemindNums(string, startDate, endDate)>0){
				nums++;
				continue;
			}
			if(planStatisticsDao.getPlanListNums(string, startDate, endDate)>0){
				nums++;
				continue;
			}
			if(planStatisticsDao.getPlanTemplateListNums(string, startDate, endDate)>0){
				nums++;
				continue;
			}
			if(planStatisticsDao.getFoodListNums(string, startDate, endDate)>0){
				nums++;
				continue;
			}
			if(planStatisticsDao.saveFoodNums(string, startDate, endDate)>0){
				nums++;
				continue;
			}
			if(planStatisticsDao.getFoodPunchNums(string, startDate, endDate)>0){
				nums++;
				continue;
			}
			if(planStatisticsDao.updatePlanIDNums(string, startDate, endDate)>0){
				nums++;
				continue;
			}
			if(planStatisticsDao.updatePlanRemindIDNums(string, startDate, endDate)>0){
				nums++;
				continue;
			}
			if(planStatisticsDao.cilckShopURLNums(string, startDate, endDate)>0){
				nums++;
				continue;
			}
			if(planStatisticsDao.cilckDefecateNums(string, startDate, endDate)>0){
				nums++;
				continue;
			}
			if(planStatisticsDao.cilckDefecateNums(string, startDate, endDate)>0){
				nums++;
				continue;
			}
		}
		return nums;
	}

	@Override
	public List<String> findPlanOpenids(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return planStatisticsDao.findPlanOpenids( map);
	}



}
