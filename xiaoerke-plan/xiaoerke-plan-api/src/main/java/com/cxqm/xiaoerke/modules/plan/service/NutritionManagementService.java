package com.cxqm.xiaoerke.modules.plan.service;

import java.util.HashMap;
import java.util.Map;

/**
 * 营养管理service实现
 * Created by sunxiao on 16/3/2.
 */
public interface NutritionManagementService {

	/**
	 * 获取一天食谱
	 * sunxiao
	 * @param birthday
	 */
	public HashMap<String, Object> getRecipes(String birthday);
	
	/**
	 * 获取今日必读文章
	 * sunxiao
	 * @param 
	 */
	public HashMap<String, Object> getTodayRead();
	
	/**
	 * 保存或修改评估
	 * sunxiao
	 * @param params
	 */
	public void saveUpdateEvaluate(Map<String, Object> params);
	
	/**
	 * 获取评估报告
	 * sunxiao
	 * @param params
	 */
	public Map<String, Object> getEvaluate(Map<String, Object> param);
	
	/**
	 * 是否关闭微信消息推送
	 * sunxiao
	 * @param params
	 */
	public Map<String, Object> updateSendWechatMessage(Map<String, Object> param);
	
	public void reEvaluate(String userId);
	
}
