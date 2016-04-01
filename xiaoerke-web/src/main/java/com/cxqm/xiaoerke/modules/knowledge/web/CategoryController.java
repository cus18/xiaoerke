package com.cxqm.xiaoerke.modules.knowledge.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.cms.entity.Category;
import com.cxqm.xiaoerke.modules.cms.service.CategoryService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;

/**
 * 栏目Controller
 * @author sunxiao
 * @version 2015-10-21
 */
@Controller
@RequestMapping(value = "knowledge")
public class CategoryController extends BaseController {

	@Autowired
	private CategoryService categoryService;
	
	@ModelAttribute("category")
	public Category get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return categoryService.get(id);
		}else{
			return new Category();
		}
	}

	/**
	 * 获取文章目录列表
	 * 2015年11月27日 上午9:44:57
	 * @return Map<String,Object>
	 * @author sunxiao
	 */
	@RequestMapping(value = "/category/categoryList", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> categoryList(@RequestBody Map<String, Object> params) throws Exception {
		return categoryService.getCategoryList(params);
	}
	
	/**
	 * 根据一个目录的id，获取该目录下的目录列表
	 * 2015年11月27日 上午15:44:57
	 * @return Map<String,Object>
	 * @author sunxiao
	 */
	@RequestMapping(value = "/category/findByParentId", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> findByParentId(@RequestBody Map<String, Object> params) throws Exception {
		HashMap<String, Object> response = new HashMap<String, Object>();
		List<Category> list = categoryService.findByParentId((String)params.get("categoryId"),"1");
		List<HashMap<String,Object>> categorylist = new ArrayList<HashMap<String,Object>>();
		for(Category cat : list){
			HashMap<String,Object> categoryMap = new HashMap<String,Object>();
			categoryMap.put("categoryId", cat.getId());
			categoryMap.put("categoryName", cat.getName());
			categorylist.add(categoryMap);
		}
		response.put("categoryList", categorylist);
		LogUtils.saveLog(Servlets.getRequest(), "00000057","根据目录id查询该目录下的目录:" + (String)params.get("categoryId"));
		return response;
	}
	
}
