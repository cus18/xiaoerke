package com.cxqm.xiaoerke.modules.plan.service.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.cms.entity.Article;
import com.cxqm.xiaoerke.modules.cms.entity.ArticleData;
import com.cxqm.xiaoerke.modules.cms.entity.Category;
import com.cxqm.xiaoerke.modules.cms.service.ArticleDataService;
import com.cxqm.xiaoerke.modules.cms.service.ArticleService;
import com.cxqm.xiaoerke.modules.plan.service.PlanInfoTaskService;
import com.cxqm.xiaoerke.modules.plan.dao.PlanInfoTaskDao;
import com.cxqm.xiaoerke.modules.plan.entity.PlanInfoTask;

@Service
@Transactional(readOnly = false)
public class PlanInfoTaskServiceImpl implements PlanInfoTaskService {

	@Autowired
	private PlanInfoTaskDao planInfoTaskDao;
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private ArticleDataService articleDataService;
	
	@Override
	public void updatePlanTask(Map<String, Object> map) {
		// TODO Auto-generated method stub
		planInfoTaskDao.updateByPrimaryKeySelective(map);
	}

	@Override
	public List<Map<String, Object>> getPlanInfoTaskListByPlanInfoId(
			String planInfoId) {
		// TODO Auto-generated method stub
		return planInfoTaskDao.getPlanInfoTaskListByPlanInfoId(planInfoId);
	}

	@Override
	public void savePlanTask(Map<String, Object> map) {
		// TODO Auto-generated method stub
		planInfoTaskDao.insertSelective(map);
	}

	@Override
	public HashMap<String, Object> getTasksInfo(String type) {
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		Article article = new Article();
		Category category = new Category();
		Calendar cal=Calendar.getInstance();
		if("food".equals(type)){
			category.setId("2b569cffee5c42c8964fb0f8bbdcf5f0");//食谱栏目id
			article.setKeywords((cal.get(Calendar.DAY_OF_WEEK)-1)+"");
			article.setCategory(category);
		}else if("sport".equals(type)){
			if((cal.get(Calendar.DAY_OF_WEEK)-1)>4){
				article.setKeywords((cal.get(Calendar.DAY_OF_WEEK)-1)-4+"");
			}else{
				article.setKeywords((cal.get(Calendar.DAY_OF_WEEK)-1)+"");
			}
			category.setId("b69a4ca259d14d44b75ce6561cf79c19");//运动栏目id
			article.setCategory(category);
		}
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
			if(((String)((Map) page.getList().get(i)).get("keywords")).contains("am")){
				returnMap.put("am", articleMap);
			}else if(((String)((Map) page.getList().get(i)).get("keywords")).contains("mm")){
				returnMap.put("mm", articleMap);
			}else if(((String)((Map) page.getList().get(i)).get("keywords")).contains("pm")){
				returnMap.put("pm", articleMap);
			}
		}
		return returnMap;
	}
	
	@Override
	public List<PlanInfoTask> getPlanInfoTaskByInfo(PlanInfoTask planInfoTask) {
		// TODO Auto-generated method stub
		List<PlanInfoTask> list = planInfoTaskDao.selectByInfo(planInfoTask);
		return list;
	}

}