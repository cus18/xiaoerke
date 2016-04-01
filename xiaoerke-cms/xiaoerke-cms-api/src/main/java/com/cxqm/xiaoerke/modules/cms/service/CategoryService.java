/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.cms.service;

import java.util.*;

import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.sys.interceptor.SystemServiceLog;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.service.TreeService;
import com.cxqm.xiaoerke.modules.cms.dao.CategoryDao;
import com.cxqm.xiaoerke.modules.cms.entity.Category;
import com.cxqm.xiaoerke.modules.cms.entity.Site;
import com.cxqm.xiaoerke.modules.cms.utils.CmsUtils;
import com.cxqm.xiaoerke.modules.sys.entity.Office;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * 栏目Service
 * @author ThinkGem
 * @version 2013-5-31
 */
@Service
@Transactional(readOnly = false)
public class CategoryService extends TreeService<CategoryDao, Category> {

	public static final String CACHE_CATEGORY_LIST = "categoryList";
	
	private Category entity = new Category();

	@SystemServiceLog(description = "00000006")//进入专家库首页
	public Map<String, Object> getCategoryList(Map<String, Object> params)
	{
		HashMap<String, Object> response = new HashMap<String, Object>();
		Category category = new Category();
		category.setModule((String)params.get("module"));
		List<Category> categoryList = findByInfo(true, category);

		List<HashMap<String,Object>> listFirstMenuData = new ArrayList<HashMap<String, Object>>();
		for(Category categoryDataFirst:categoryList)
		{
			HashMap<String,Object> categoryFirstMap = new HashMap<String,Object>();
			if(categoryDataFirst.getParentId().equals("1"))
			{
				String hasThirdMenu = "false";
				categoryFirstMap.put("firstMenuId",categoryDataFirst.getId());
				categoryFirstMap.put("firstMenuName",categoryDataFirst.getName());
				categoryFirstMap.put("open","true");
				List<HashMap<String,Object>> listSecondMenuData = new ArrayList<HashMap<String, Object>>();
				for(Category categoryDataSecond:categoryList)
				{
					HashMap<String,Object> categorySecondMap = new HashMap<String,Object>();
					if(categoryDataSecond.getParentId().equals(categoryDataFirst.getId()))
					{
						categorySecondMap.put("secondMenuId",categoryDataSecond.getId());
						categorySecondMap.put("secondMenuName",categoryDataSecond.getName());
						List<HashMap<String,Object>> listThirdMenuData = new ArrayList<HashMap<String, Object>>();
						for(Category categoryDataThird:categoryList)
						{
							HashMap<String,Object> categoryThirdMap = new HashMap<String,Object>();
							if(categoryDataThird.getParentId().equals(categoryDataSecond.getId()))
							{
								categoryThirdMap.put("thirdMenuId",categoryDataThird.getId());
								categoryThirdMap.put("thirdMenuName",categoryDataThird.getName());
								listThirdMenuData.add(categoryThirdMap);
							}
						}
						if(listThirdMenuData.size()!=0)
						{
							categorySecondMap.put("thirdMenuData",listThirdMenuData);
							hasThirdMenu = "true";
						}
						else
						{
							hasThirdMenu = "false";
						}
						listSecondMenuData.add(categorySecondMap);
					}
				}
				categoryFirstMap.put("hasThirdMenu",hasThirdMenu);
				categoryFirstMap.put("secondMenuData",listSecondMenuData);
				listFirstMenuData.add(categoryFirstMap);
			};
		}
		response.put("categoryList", listFirstMenuData);
		return response;
	}
	
	@SuppressWarnings("unchecked")
	public List<Category> findByUser(boolean isCurrentSite, String module){
		
		List<Category> list = (List<Category>)UserUtils.getCache(CACHE_CATEGORY_LIST);
		if (list == null){
			User user = UserUtils.getUser();
			Category category = new Category();
			category.setOffice(new Office());
			category.getSqlMap().put("dsf", dataScopeFilter(user, "o", "u"));
			category.setSite(new Site());
			category.setParent(new Category());
			list = dao.findList(category);
			// 将没有父节点的节点，找到父节点
			Set<String> parentIdSet = Sets.newHashSet();
			for (Category e : list){
				if (e.getParent()!=null && StringUtils.isNotBlank(e.getParent().getId())){
					boolean isExistParent = false;
					for (Category e2 : list){
						if (e.getParent().getId().equals(e2.getId())){
							isExistParent = true;
							break;
						}
					}
					if (!isExistParent){
						parentIdSet.add(e.getParent().getId());
					}
				}
			}
			if (parentIdSet.size() > 0){
				//FIXME 暂且注释，用于测试
//				dc = dao.createDetachedCriteria();
//				dc.add(Restrictions.in("id", parentIdSet));
//				dc.add(Restrictions.eq("delFlag", Category.DEL_FLAG_NORMAL));
//				dc.addOrder(Order.asc("site.id")).addOrder(Order.asc("sort"));
//				list.addAll(0, dao.find(dc));
			}
			UserUtils.putCache(CACHE_CATEGORY_LIST, list);
		}
		
		if (isCurrentSite){
			List<Category> categoryList = Lists.newArrayList(); 
			for (Category e : list){
				if (Category.isRoot(e.getId()) || (e.getSite()!=null && e.getSite().getId() !=null 
						&& e.getSite().getId().equals(Site.getCurrentSiteId()))){
					if (StringUtils.isNotEmpty(module)){
						if (module.equals(e.getModule()) || "".equals(e.getModule())){
							categoryList.add(e);
						}
					}else{
						categoryList.add(e);
					}
				}
			}
			return categoryList;
		}
		return list;
	}

	public List<Category> findByParentId(String parentId, String siteId){
		Category parent = new Category();
		parent.setId(parentId);
		entity.setParent(parent);
		Site site = new Site();
		site.setId(siteId);
		entity.setSite(site);
		return dao.findByParentIdAndSiteId(entity);
	}
	
	public List<Category> findByInfo(boolean isCurrentSite, Category category){
		List<Category> list = (List<Category>)UserUtils.getCache(CACHE_CATEGORY_LIST);
		if (list == null){
			category.setOffice(new Office());
			category.setSite(new Site());
			category.setParent(new Category());
			list = dao.findList(category);
			UserUtils.putCache(CACHE_CATEGORY_LIST, list);
		}
		
		if (isCurrentSite){
			List<Category> categoryList = Lists.newArrayList(); 
			for (Category e : list){
				if (Category.isRoot(e.getId()) || (e.getSite()!=null && e.getSite().getId() !=null 
						&& e.getSite().getId().equals(Site.getCurrentSiteId()))){
					categoryList.add(e);
				}
			}
			return categoryList;
		}
		return list;
	}
	
	public Page<Category> find(Page<Category> page, Category category) {
		category.setPage(page);
		category.setInMenu(Global.SHOW);
		page.setList(dao.findModule(category));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(Category category) {
		category.setSite(new Site(Site.getCurrentSiteId()));
		if (StringUtils.isNotBlank(category.getViewConfig())){
            category.setViewConfig(StringEscapeUtils.unescapeHtml4(category.getViewConfig()));
        }
		super.save(category);
		UserUtils.removeCache(CACHE_CATEGORY_LIST);
		CmsUtils.removeCache("mainNavList_"+category.getSite().getId());
	}
	
	@Transactional(readOnly = false)
	public void delete(Category category) {
		super.delete(category);
		UserUtils.removeCache(CACHE_CATEGORY_LIST);
		CmsUtils.removeCache("mainNavList_"+category.getSite().getId());
	}
	
	/**
	 * 通过编号获取栏目列表
	 */
	public List<Category> findByIds(String ids) {
		List<Category> list = Lists.newArrayList();
		String[] idss = StringUtils.split(ids,",");
		if (idss.length>0){
			for(String id : idss){
				Category e = dao.get(id);
				if(null != e){
					list.add(e);
				}
			}
		}
		return list;
	}
	
}
