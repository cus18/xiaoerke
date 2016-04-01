package com.cxqm.xiaoerke.modules.operation.dao;

import java.util.List;
import java.util.Map;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;

/**
 *  便秘管理统计
 * @author 张博 2016年2月16日
 *
 */
@MyBatisDao
public interface PlanStatisticsDao {

	/**
	 * 访问次数
	 * @param date
	 * @return
	 */
	int visitPlanNums(String date);
	
	/**
	 * 访问人次
	 * @param date
	 * @return
	 */
	int visitPlanPeoples(String date);
	
	/**
	 * 新增用户数
	 * @param date
	 * @return
	 */
	int newUsers(String date);
	
	/**
	 * 查看单位时间内参与计划的openid
	 * @param startDate 
	 * @return
	 */
	List<String> findPlanOpenids(Map<String, Object> map);
	
	/**
	 * 单位时间内是否存在打卡记录
	 * @param openid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	int punchNums(String openid,String startDate,String endDate);
	
	/**
	 * 单位时间内是否存在保存评论记录行为
	 * @param openid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	int saveCommentNums(String openid,String startDate,String endDate);
	
	/**
	 * 单位时间内是否存在取消计划行为
	 * @param openid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	int cancelPlanNums(String openid,String startDate,String endDate);
	
	/**
	 * 单位时间内是否存在保存计划行为
	 * @param openid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	int savePlanNums(String openid,String startDate,String endDate);
	
	/**
	 * 单位时间内是否存在更新任务提醒设置行为
	 * @param openid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	int updatePlanRemindNums(String openid,String startDate,String endDate);
	
	/**
	 * 单位时间内是否存在获取任务列表行为
	 * @param openid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	int getPlanListNums(String openid,String startDate,String endDate);
	
	/**
	 * 单位时间内是否存在获取任务模板列表行为
	 * @param openid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	int getPlanTemplateListNums(String openid,String startDate,String endDate);
	
	/**
	 * 单位时间内是否存在获取获取食材列表行为
	 * @param openid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	int getFoodListNums(String openid,String startDate,String endDate);
	
	/**
	 * 单位时间内是否存在保存食材行为
	 * @param openid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	int saveFoodNums(String openid,String startDate,String endDate);
	
	/**
	 * 单位时间内是否存在获取饮食打卡行为
	 * @param openid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	int getFoodPunchNums(String openid,String startDate,String endDate);
	
	/**
	 * 单位时间内是否存在更新计划id行为
	 * @param openid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	int updatePlanIDNums(String openid,String startDate,String endDate);
	
	/**
	 * 单位时间内是否存在更新任务提醒设置id行为
	 * @param openid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	int updatePlanRemindIDNums(String openid,String startDate,String endDate);
	
	/**
	 * 单位时间内是否存在购买链接行为
	 * @param openid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	int cilckShopURLNums(String openid,String startDate,String endDate);
	
	/**
	 * 单位时间内是否存在便秘管理行为
	 * @param openid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	int cilckDefecateNums(String openid,String startDate,String endDate);
	
	/**
	 *  按摩连续打卡用户数
	 * @param date
	 * @return
	 */
	int continuousPunchUsersForMassage(String date);

	/**
	 *  排便连续打卡用户数
	 * @param date
	 * @return
	 */
	int continuousPunchUsersForDefecate(String date);
	
	/**
	 *  饮食连续打卡用户数
	 * @param date
	 * @return
	 */
	int continuousPunchUsersForFood(String date);
	
	/**
	 *  运动连续打卡用户数
	 * @param date
	 * @return
	 */
	int continuousPunchUsersForSport(String date);
	
	/**
	 * 点击购买链接次数
	 * @param date
	 * @return
	 */
	List<Map<String, Object>> clickShopNums(String date);
	

	
	/**
	 * 反馈 好 数量
	 * @param date
	 * @return
	 */
	List<Map<String, Object>> feedBackNums(String date);
	
}
