package com.cxqm.xiaoerke.modules.plan.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.cms.entity.Article;
import com.cxqm.xiaoerke.modules.cms.entity.ArticleData;
import com.cxqm.xiaoerke.modules.cms.entity.Category;
import com.cxqm.xiaoerke.modules.cms.service.ArticleDataService;
import com.cxqm.xiaoerke.modules.cms.service.ArticleService;
import com.cxqm.xiaoerke.modules.cms.utils.BirthdayToAgeUtils;
import com.cxqm.xiaoerke.modules.plan.dao.NutritionManagementDao;
import com.cxqm.xiaoerke.modules.plan.entity.NutritionEvaluate;
import com.cxqm.xiaoerke.modules.plan.entity.PlanInfo;
import com.cxqm.xiaoerke.modules.plan.service.NutritionManagementService;
import com.cxqm.xiaoerke.modules.plan.service.PlanInfoService;

/**
 * 营养管理service实现
 *
 * @author sunxiao
 * @version 2016-3-2
 */

@Service
@Transactional(readOnly = false)
public class NutritionManagementServiceImpl implements NutritionManagementService {
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private ArticleDataService articleDataService;
	
	@Autowired
	private NutritionManagementDao nutritionManagementDao;
	
	@Autowired
	private PlanInfoService planInfoService;
	
	/**
	 * 获取一天食谱
	 * sunxiao
	 * @param birthday
	 */
	@Override
	public HashMap<String, Object> getRecipes(String birthday) {
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		long days = BirthdayToAgeUtils.getDays(birthday, DateUtils.DateToStr(new Date(),"date"));
		long year=days/365;
        long month=(days-365L*year)/30;
		long months = year*12 + month;
		String ageRange = "";
		String categoryId = "";
		if(months>=12 && months<=24){
			ageRange = "1";
			categoryId = "12d7f889ca4e496ba1b9238f054fba33";//19-24月龄食谱目录
		}else if(months>=25 && months<=36){
			ageRange = "2";
			categoryId = "7bd10ecdd8594656ab3b91a84a28672f";//25-36月龄食谱目录
		}else {
			ageRange = "outofage";
		}
		Article article = new Article();
		Category category = new Category();
		Calendar cal=Calendar.getInstance();
		category.setId(categoryId);//食谱栏目id
		article.setKeywords(ageRange+(cal.get(Calendar.DAY_OF_WEEK)-1));
		article.setCategory(category);
		Page<Article> page = articleService.findPage(new Page<Article>(1,30), article, true);
		for(int i=0;i<page.getList().size();i++)
		{
			HashMap<String,Object> articleMap = new HashMap<String,Object>();
			articleMap.put("id", ((Map) page.getList().get(i)).get("id"));
			articleMap.put("keywords", ((Map) page.getList().get(i)).get("keywords"));
			articleMap.put("description", ((Map) page.getList().get(i)).get("description"));
			articleMap.put("title",((Map) page.getList().get(i)).get("title"));
			articleMap.put("createDate",((Map) page.getList().get(i)).get("createDate"));
			articleMap.put("serverDate",System.currentTimeMillis());
			ArticleData articleData = articleDataService.get((String)(((Map) page.getList().get(i)).get("id")));
			articleMap.put("content",articleData.getContent());
			String keywords = (String)((Map) page.getList().get(i)).get("keywords");
			if(keywords.contains("am")&&!keywords.contains("amp")){
				returnMap.put("am", articleMap);
			}else if(keywords.contains("amp")){
				returnMap.put("amp", articleMap);
			}else if(keywords.contains("mm")&&!keywords.contains("mmp")){
				returnMap.put("mm", articleMap);
			}else if(keywords.contains("mmp")){
				returnMap.put("mmp", articleMap);
			}else if(keywords.contains("pm")){
				returnMap.put("pm", articleMap);
			}
		}
		return returnMap;
	}
	
	/**
	 * 获取今日必读文章
	 * sunxiao
	 */
	@Override
	public HashMap<String, Object> getTodayRead() {
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		Article article = new Article();
		Category category = new Category();
		category.setId("14b49addb6f44711b3a5dda5319ced6c");//饮食习惯目录
		article.setCategory(category);
		Page<Article> page = new Page<Article>(1,30);
		page.setOrderBy("create_date desc");
		Page<Article> pageList = articleService.findPage(page, article, true);
		if(pageList.getCount()!=0){
			HashMap<String,Object> habitArticleMap = new HashMap<String,Object>();
			habitArticleMap.put("id", ((Map) pageList.getList().get(0)).get("id"));
			habitArticleMap.put("title",((Map) pageList.getList().get(0)).get("title"));
			habitArticleMap.put("habitArticleCategoryId","14b49addb6f44711b3a5dda5319ced6c");
			returnMap.put("habitArticleMap", habitArticleMap);
		}
		
		category.setId("1e00f247ad5a4c3eac0cd57fe6a8e398");//饮食安全目录
		article.setCategory(category);
		pageList = articleService.findPage(page, article, true);
		if(pageList.getCount()!=0){
			HashMap<String,Object> safeArticleMap = new HashMap<String,Object>();
			safeArticleMap.put("id", ((Map) pageList.getList().get(0)).get("id"));
			safeArticleMap.put("title",((Map) pageList.getList().get(0)).get("title"));
			safeArticleMap.put("safeArticleCategoryId","1e00f247ad5a4c3eac0cd57fe6a8e398");
			returnMap.put("safeArticleMap", safeArticleMap);
		}
		
		return returnMap;
	}

	/**
	 * 保存或修改评估
	 * sunxiao
	 * @param params
	 */
	@Override
	public void saveUpdateEvaluate(Map<String, Object> params) {
		// TODO Auto-generated method stub
		String userId = (String)params.get("userId");
		params.put("createTime", new Date());
		List<NutritionEvaluate> list = nutritionManagementDao.getEvaluateListByInfo(params);
		NutritionEvaluate nutritionEvaluate = new NutritionEvaluate();
		if(StringUtils.isNotNull((String)params.get("oilSalt"))){
			nutritionEvaluate.setOilSalt((String)params.get("oilSalt"));
		}
		if(StringUtils.isNotNull((String)params.get("vegetables"))){
			nutritionEvaluate.setVegetables((String)params.get("vegetables"));	
		}
		if(StringUtils.isNotNull((String)params.get("millet"))){
			nutritionEvaluate.setMillet((String)params.get("millet"));
		}
		if(StringUtils.isNotNull((String)params.get("potato"))){
			nutritionEvaluate.setPotato((String)params.get("potato"));
		}
		if(StringUtils.isNotNull((String)params.get("milk"))){
			nutritionEvaluate.setMilk((String)params.get("milk"));
		}
		if(StringUtils.isNotNull((String)params.get("meat"))){
			nutritionEvaluate.setMeat((String)params.get("meat"));
		}
		if(StringUtils.isNotNull((String)params.get("fishEggs"))){
			nutritionEvaluate.setFishEggs((String)params.get("fishEggs"));
		}
		if(StringUtils.isNotNull((String)params.get("water"))){
			nutritionEvaluate.setWater((String)params.get("water"));
		}
		
		if(list.size()==0){
			nutritionEvaluate.setCreateTime(new Date());
			nutritionEvaluate.setCreateBy(userId);
			nutritionEvaluate.setUserId(userId);
			nutritionManagementDao.insertSelective(nutritionEvaluate);
		}else{
			nutritionEvaluate.setId(list.get(0).getId());
			nutritionEvaluate.setUpdateTime(new Date());
			nutritionEvaluate.setUpdateBy(userId);
			nutritionManagementDao.updateByPrimaryKeySelective(nutritionEvaluate);
		}
	}

	/**
	 * 获取评估
	 * sunxiao
	 * @param params
	 */
	@Override
	public Map<String, Object> getEvaluate(Map<String, Object> params) {
		// TODO Auto-generated method stub
		String flag = (String)params.get("flag");
		Map<String, Object> returnMap = new HashMap<String, Object>();
		if("index".equals(flag)){
			List dateList = new ArrayList();
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);
			Date yesterday = cal.getTime();
			dateList.add(new Date());
			dateList.add(yesterday);
			params.put("dateList", dateList);
			List<NutritionEvaluate> list = nutritionManagementDao.getEvaluateListByInfo(params);
			String todayflag = "";
			Map<String, String> yestodayEvaluateMap = new HashMap<String, String>();
			for(NutritionEvaluate ne : list){
				if(DateUtils.DateToStr(new Date(), "date").equals(DateUtils.DateToStr(ne.getCreateTime(), "date"))){
					todayflag = "yes";
				}
				if(DateUtils.DateToStr(yesterday, "date").equals(DateUtils.DateToStr(ne.getCreateTime(), "date"))){
					yestodayEvaluateMap.put("oilSalt", ne.getOilSalt());
					yestodayEvaluateMap.put("vegetables", ne.getVegetables());
					yestodayEvaluateMap.put("millet", ne.getMillet());
					yestodayEvaluateMap.put("potato", ne.getPotato());
					yestodayEvaluateMap.put("meat", ne.getMeat());
					yestodayEvaluateMap.put("fishEggs", ne.getFishEggs());
					yestodayEvaluateMap.put("milk", ne.getMilk());
					yestodayEvaluateMap.put("water", ne.getWater());
				}
			}
			if(StringUtils.isNotNull(todayflag)){
				returnMap.put("td", "yes");
			}else{
				if(yestodayEvaluateMap.size()==0){
					returnMap.put("ytn", "yes");
				}else{
					returnMap.put("yty", yestodayEvaluateMap);
				}
			}
		}else if("evaluate".equals(flag)){
			params.put("createTime", new Date());
			List<NutritionEvaluate> list = nutritionManagementDao.getEvaluateListByInfo(params);
			Map<String, String> todayEvaluateMap = new HashMap<String, String>();
			for(NutritionEvaluate ne : list){
				todayEvaluateMap.put("oilSalt", ne.getOilSalt());
				todayEvaluateMap.put("vegetables", ne.getVegetables());
				todayEvaluateMap.put("millet", ne.getMillet());
				todayEvaluateMap.put("potato", ne.getPotato());
				todayEvaluateMap.put("meat", ne.getMeat());
				todayEvaluateMap.put("fishEggs", ne.getFishEggs());
				todayEvaluateMap.put("milk", ne.getMilk());
				todayEvaluateMap.put("water", ne.getWater());
			}
			returnMap.put("todayEvaluateMap", todayEvaluateMap);
		}
		return returnMap;
	}

	/**
	 * 开关是否发送营养管理消息
	 * sunxiao
	 * @param params
	 */
	@Override
	public Map<String, Object> updateSendWechatMessage(Map<String, Object> param) {
		// TODO Auto-generated method stub
		param.put("planTemplateId", 2);
		param.put("status", "ongoing");
		List<Map<String,Object>> list = planInfoService.getPlanInfoByUserId(param);
		for(Map<String,Object> map : list){
			PlanInfo planInfo = new PlanInfo();
			planInfo.setId(Long.valueOf(String.valueOf(map.get("id"))));
			planInfo.setNotice((String)param.get("flag"));
			planInfoService.updatePlanInfoById(planInfo);
		}
		return null;
	}

	/**
	 * 重新评估
	 * sunxiao
	 * @param userId
	 */
	@Override
	public void reEvaluate(String userId) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("createTime", new Date());
		List<NutritionEvaluate> list = nutritionManagementDao.getEvaluateListByInfo(params);
		if(list.size()>0){
			NutritionEvaluate nutritionEvaluate = new NutritionEvaluate();
			nutritionEvaluate.setFishEggs("");
			nutritionEvaluate.setMeat("");
			nutritionEvaluate.setMilk("");
			nutritionEvaluate.setOilSalt("");
			nutritionEvaluate.setPotato("");
			nutritionEvaluate.setVegetables("");
			nutritionEvaluate.setMillet("");
			nutritionEvaluate.setWater("");
			nutritionEvaluate.setId(list.get(0).getId());
			nutritionEvaluate.setUpdateTime(new Date());
			nutritionEvaluate.setUpdateBy(userId);
			nutritionManagementDao.updateByPrimaryKeySelective(nutritionEvaluate);
		}
	}
	
}
