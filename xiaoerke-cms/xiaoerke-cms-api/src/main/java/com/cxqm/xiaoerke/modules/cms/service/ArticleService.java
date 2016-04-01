/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.cms.service;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.service.CrudService;
import com.cxqm.xiaoerke.common.utils.CacheUtils;
import com.cxqm.xiaoerke.common.utils.OSSObjectTool;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.cms.dao.ArticleDao;
import com.cxqm.xiaoerke.modules.cms.dao.ArticleDataDao;
import com.cxqm.xiaoerke.modules.cms.dao.CategoryDao;
import com.cxqm.xiaoerke.modules.cms.entity.Article;
import com.cxqm.xiaoerke.modules.cms.entity.ArticleData;
import com.cxqm.xiaoerke.modules.cms.entity.BabyEmrVo;
import com.cxqm.xiaoerke.modules.cms.entity.Category;
import com.cxqm.xiaoerke.modules.cms.utils.BirthdayToAgeUtils;
import com.cxqm.xiaoerke.modules.sys.entity.MongoLog;
import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import com.google.common.collect.Lists;

/**
 * 文章Service
 * @author ThinkGem
 * @version 2013-05-15
 */
@Service
@Transactional(readOnly = false)
public class ArticleService extends CrudService<ArticleDao, Article> {

	@Autowired
	private ArticleDataDao articleDataDao;
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private ArticleDataService articleDataService;

	@Autowired
	private MongoDBService<MongoLog> mongoDBService;
	
	@Autowired
	private BabyEmrService babyEmrService;
	
	@Autowired
	private CategoryService categoryService;

	/**
	 * 获取今日精选和今日必读文章
	 * 2015年12月1日 上午11:17:12
	 * @return Map<String,Object>
	 * @author sunxiao
	 */
	public Map<String, Object> getTodaySelectAndReadArticleList(Map<String, Object> params,String openid){
		HashMap<String, Object> response = new HashMap<String, Object>();
		Random r = new Random();
		List<BabyEmrVo> list = babyEmrService.getBabyEmrList(openid);
		Date birthday = list.get(0).getBirthday();
		List<Category> secondCategoryList = Lists.newArrayList();
		List<Article> todayReadArticleList = new ArrayList<Article>();
		secondCategoryList = categoryService.findByIds("35de9ba381cf40ddb87e4cf7e4978b7d");//精选目录
		Article article2 = new Article();
		Category category2 = new Category();
		category2.setId(secondCategoryList.get(0).getId());
		category2.setParentIds(secondCategoryList.get(0).getId());
		article2.setCategory(category2);
		List<Article> articleList2 = articleDao.findAllList(article2);
		Article a2= articleList2.get(r.nextInt(articleList2.size()));
		Category c2 = new Category();
		c2.setId("35de9ba381cf40ddb87e4cf7e4978b7d");
		c2.setName("精选");
		a2.setCategory(c2);
		todayReadArticleList.add(a2);
		LogUtils.saveLog(Servlets.getRequest(), "每日必读文章id:" + a2.getId());
		
		//生长发育、善事喂养、生活护理目录
		secondCategoryList = categoryService.findByIds("199c280020844a7faf3cb289860b3301," +
				"def37485092b484aa6f9dda427461ef0," +
				"1a6cf07694194a9aba9c22f8a1defaaf");
		
		List<Category> thirdCategoryList = Lists.newArrayList();
		Category randomCat = secondCategoryList.get(r.nextInt(secondCategoryList.size()));
		thirdCategoryList = categoryService.findByParentId(randomCat.getId(), "1");
		Map<String, String> thirdCategoryMap = new HashMap<String, String>();
		for(Category cat : thirdCategoryList){
			thirdCategoryMap.put(cat.getName(),cat.getId());
		}
		
		String age = BirthdayToAgeUtils.BirthdayToAge(birthday);
		String articleCatId = thirdCategoryMap.get(age);
		Article article = new Article();
		Category category = new Category();
		category.setId(articleCatId);
		category.setParentIds(articleCatId);
		article.setCategory(category);
		List<Article> articleList = articleDao.findAllList(article);
		if(articleList.size()!=0){
			Article a1= articleList.get(r.nextInt(articleList.size()));
			Category c1 = new Category();
			c1.setId(randomCat.getId());
			c1.setName(randomCat.getName());
			a1.setCategory(c1);
			todayReadArticleList.add(a1);
			LogUtils.saveLog(Servlets.getRequest(), "每日必读文章id:" + a1.getId());
		}

		//今日精选
		List<Category> secondCategoryList2 = Lists.newArrayList();
		secondCategoryList2 = categoryService.findByParentId("8f133f3a6db7490cb4e5274649b047aa", "1");
		List<Category> listFrontTwoCat = new ArrayList<Category>();
		List<Category> listOtherCat = new ArrayList<Category>();
		
		for(Category c : secondCategoryList2){
			if("35b577e77bc64cb881414867904fb45c".equals(c.getId())){
				if("0-28天".equals(age)){
					listFrontTwoCat.add(c);
				}
			}else if("84bb0e4f30514129bdcbd3750c52ffc2".equals(c.getId())){
				if(!"0-28天".equals(age)){
					listFrontTwoCat.add(c);
				}
			}else{
				listOtherCat.add(c);
			}
		}
		
		List<Article> todaySelectArticleList = new ArrayList<Article>();
		
		Article article3 = new Article();
		Category category3 = new Category();
		String catId1 = listFrontTwoCat.get(0).getId();
		category3.setId(catId1);
		category3.setParentIds(catId1);
		article3.setCategory(category3);
		List<Article> articleList3 = articleDao.findAllList(article3);
		
		Article article4 = new Article();
		Category category4 = new Category();
		Category cat2 = listOtherCat.get(r.nextInt(listOtherCat.size()));
		category4.setId(cat2.getId());
		category4.setParentIds(cat2.getId());
		article4.setCategory(category4);
		List<Article> articleList4 = articleDao.findAllList(article4);
		if(articleList3.size()!=0){
			Article a = articleList3.get(r.nextInt(articleList3.size()));
			a.setCategory(listFrontTwoCat.get(0));
			todaySelectArticleList.add(a);
			LogUtils.saveLog(Servlets.getRequest(), "每日精选文章id:" + a.getId());
		}
		if(articleList4.size()!=0){
			Article a = articleList4.get(r.nextInt(articleList4.size()));
			a.setCategory(cat2);
			todaySelectArticleList.add(a);
			LogUtils.saveLog(Servlets.getRequest(), "每日精选文章id:" + a.getId());
		}
		
		response.put("todaySelectArticleList", todaySelectArticleList);
		response.put("todayReadArticleList", todayReadArticleList);
		return response;
	}
	
	/**
	 * 获取文章列表
	 * 2015年12月1日 上午11:17:37
	 * @return Map<String,Object>
	 * @author sunxiao
	 */
	public Map<String, Object> getArticleList(Map<String, Object> params)
	{
		Page<Article> pages = null;
		if(params.get("pageNo")==null||params.get("pageSize")==null){
			pages = new Page<Article>();
		}else{
			Integer pageNo = (Integer) params.get("pageNo");
			Integer pageSize = (Integer) params.get("pageSize");
			pages = new Page<Article>(pageNo,pageSize);
		}
		HashMap<String, Object> response = new HashMap<String, Object>();
		Article article = new Article();
		Category category =new Category();
		category.setId((String)params.get("id"));
		article.setCategory(category);
		article.setTitle((String)params.get("title"));
		article.setKeywords((String)params.get("keywords"));
		// 记录日志
		if((params.get("title")==null)&&(!(params.get("id")==null)))
		{
			LogUtils.saveLog(Servlets.getRequest(), "点击文章栏目:" + params.get("id")+":generalize-list:"+params.get("generalize"));
		}
		else if(!((params.get("title")==null)&&(params.get("id")==null)))
		{
			LogUtils.saveLog(Servlets.getRequest(), "搜索专家库文章:" + params.get("title")+":generalize-list:"+params.get("generalize"));
		}
		else if( !((params.get("title")==null))&&(!(params.get("id")==null)))
		{
			LogUtils.saveLog(Servlets.getRequest(), "点击某个栏目下的某类文章:" + params.get("id")+":"+params.get("title")+":generalize-list:"+params.get("generalize"));
		}

        Page<Article> page = findPage(pages, article, true);

		List<HashMap<String,Object>> articleList = new ArrayList();
		//根据文章的ID，获取文章的阅读次数，和分享的次数
		for(int i=0;i<page.getList().size();i++)
		{
			HashMap<String,Object> articleMap = new HashMap<String,Object>();
			articleMap.put("id", ((Map) page.getList().get(i)).get("id"));
			articleMap.put("keywords", ((Map) page.getList().get(i)).get("keywords"));
			articleMap.put("description", ((Map) page.getList().get(i)).get("description"));
			articleMap.put("articleReadCount",((Map) page.getList().get(i)).get("hits"));
			articleMap.put("articleShareCount",((Map) page.getList().get(i)).get("share"));
			articleMap.put("articlePraiseCount",((Map) page.getList().get(i)).get("praise"));
			articleMap.put("articleCommentCount",((Map) page.getList().get(i)).get("comment"));
			articleMap.put("title",((Map) page.getList().get(i)).get("title"));
			articleMap.put("createDate",((Map) page.getList().get(i)).get("createDate"));
			articleMap.put("serverDate",System.currentTimeMillis());
			articleList.add(articleMap);
		}
        response.put("articleList", articleList);
		return response;
	}

	@Transactional(readOnly = false)
	public Page<Article> findPage(Page<Article> page, Article article, boolean isDataScopeFilter) {
		// 更新过期的权重，间隔为“6”个小时
		Date updateExpiredWeightDate =  (Date)CacheUtils.get("updateExpiredWeightDateByArticle");
		if (updateExpiredWeightDate == null || (updateExpiredWeightDate != null 
				&& updateExpiredWeightDate.getTime() < new Date().getTime())){
			dao.updateExpiredWeight(article);
			CacheUtils.put("updateExpiredWeightDateByArticle", DateUtils.addHours(new Date(), 6));
		}
		if (article.getCategory()!=null && StringUtils.isNotBlank(article.getCategory().getId()) &&
				!Category.isRoot(article.getCategory().getId())){
			Category category = categoryDao.get(article.getCategory().getId());
			if (category==null){
				category = new Category();
			}
			category.setParentIds(category.getId());
			category.setSite(category.getSite());
			article.setCategory(category);
		}
		else{
			article.setCategory(new Category());
		}
		return articleDao.findList(page, article);
	}

	@Transactional(readOnly = false)
	public void save(Article article) {
		if (article.getArticleData().getContent()!=null){
			article.getArticleData().setContent(StringEscapeUtils.unescapeHtml4(
					article.getArticleData().getContent()));
		}
		// 如果没有审核权限，则将当前内容改为待审核状态
		if (!UserUtils.getSubject().isPermitted("cms:article:audit")){
			article.setDelFlag(Article.DEL_FLAG_AUDIT);
		}
		// 如果栏目不需要审核，则将该内容设为发布状态
		if (article.getCategory()!=null&&StringUtils.isNotBlank(article.getCategory().getId())){
			Category category = categoryDao.get(article.getCategory().getId());
			if (!Global.YES.equals(category.getIsAudit())){
				article.setDelFlag(Article.DEL_FLAG_NORMAL);
			}
		}
		article.setUpdateBy(UserUtils.getUser());
		article.setUpdateDate(new Date());
        if (StringUtils.isNotBlank(article.getViewConfig())){
            article.setViewConfig(StringEscapeUtils.unescapeHtml4(article.getViewConfig()));
        }
        
        ArticleData articleData = new ArticleData();;
		if (StringUtils.isBlank(article.getId())){
			article.preInsert();
			articleData = article.getArticleData();
	        articleData.setContent(tranceContent(article.getId(),articleData.getContent()));
			articleData.setId(article.getId());
			try {
				if(!"".equals(article.getImage())){
					uploadArticleImage(article.getId(),article.getImageSrc());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dao.insert(article);
			articleDataDao.insert(articleData);
		}else{
			article.preUpdate();
			articleData = article.getArticleData();
			articleData.setContent(tranceContent(article.getId(),articleData.getContent()));
			articleData.setId(article.getId());
			try {
				if(!"".equals(article.getImage())){
					uploadArticleImage(article.getId(),article.getImageSrc());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dao.update(article);
			articleDataDao.update(article.getArticleData());
		}
	}
	
	private String tranceContent(String id , String content){
		Matcher m = Pattern.compile("src.*?/>").matcher(content);
        int i = 0;
        while(m.find()){
        	String[] imgpath = m.group().split("\"");
        	content = content.replace(imgpath[1].substring(0,imgpath[1].lastIndexOf("/")+1),
					"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/");
        	content = content.replace(imgpath[1].substring(imgpath[1].lastIndexOf("/")+1,imgpath[1].length()), id+"-"+i);
	        	try {
				uploadArticleImage(id+"-"+i,imgpath[1]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	i++;
        }
        return content;
	}
	private void uploadArticleImage(String id , String src) throws Exception{
		File file = new File(System.getProperty("user.dir").replace("bin", "webapps")+URLDecoder.decode(src, "utf-8"));
		FileInputStream inputStream = new FileInputStream(file);
		long length = file.length();
		//上传图片至阿里云
		OSSObjectTool.uploadFileInputStream(id, length, inputStream, OSSObjectTool.BUCKET_ARTICLE_PIC);
	}
	
	@Transactional(readOnly = false)
	public void delete(Article article, Boolean isRe) {
		super.delete(article);
	}
	
	/**
	 * 通过编号获取内容标题
	 * @return new Object[]{栏目Id,文章Id,文章标题}
	 */
	public List<Object[]> findByIds(String ids) {
		if(ids == null){
			return new ArrayList<Object[]>();
		}
		List<Object[]> list = Lists.newArrayList();
		String[] idss = StringUtils.split(ids,",");
		Article e = null;
		for(int i=0;(idss.length-i)>0;i++){
			e = dao.get(idss[i]);
			list.add(new Object[]{e.getCategory().getId(),e.getId(),StringUtils.abbr(e.getTitle(),50)});
		}
		return list;
	}
	
	/**
	 * 点击数加一
	 */
	@Transactional(readOnly = false)
	public void updateHitsAddOne(String id) {
		dao.updateHitsAddOne(id);
	}
	
	/**
	 * 分享数加一 sunxiao
	 */
	@Transactional(readOnly = false)
	public void updateShareAddOne(String id) {
		dao.updateShareAddOne(id);
	}
	
	/**
	 * 更新点赞次数 sunxiao
	 */
	@Transactional(readOnly = false)
	public int updatePraiseNumber(String articleId,String praiseNumber) {
		return dao.updatePraiseNumber(articleId,praiseNumber);
	}
	
	/**
	 * 更新评论次数 sunxiao
	 */
	@Transactional(readOnly = false)
	public int updateCommentAddOne(String articleId) {
		return dao.updateCommentAddOne(articleId);
	}
	
	/**
	 * 更新索引
	 */
	public void createIndex(){
		//dao.createIndex();
	}
	
	/**
	 * 全文检索
	 */
	//FIXME 暂不提供检索功能
	public Page<Article> search(Page<Article> page, String q, String categoryId, String beginDate, String endDate){
		
		// 设置查询条件
//		BooleanQuery query = dao.getFullTextQuery(q, "title","keywords","description","articleData.content");
//		
//		// 设置过滤条件
//		List<BooleanClause> bcList = Lists.newArrayList();
//
//		bcList.add(new BooleanClause(new TermQuery(new Term(Article.FIELD_DEL_FLAG, Article.DEL_FLAG_NORMAL)), Occur.MUST));
//		if (StringUtils.isNotBlank(categoryId)){
//			bcList.add(new BooleanClause(new TermQuery(new Term("category.ids", categoryId)), Occur.MUST));
//		}
//		
//		if (StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)) {   
//			bcList.add(new BooleanClause(new TermRangeQuery("updateDate", beginDate.replaceAll("-", ""),
//					endDate.replaceAll("-", ""), true, true), Occur.MUST));
//		}   
		
		//BooleanQuery queryFilter = dao.getFullTextQuery((BooleanClause[])bcList.toArray(new BooleanClause[bcList.size()]));

//		System.out.println(queryFilter);
		
		// 设置排序（默认相识度排序）
		//FIXME 暂时不提供lucene检索
		//Sort sort = null;//new Sort(new SortField("updateDate", SortField.DOC, true));
		// 全文检索
		//dao.search(page, query, queryFilter, sort);
		// 关键字高亮
		//dao.keywordsHighlight(query, page.getList(), 30, "title");
		//dao.keywordsHighlight(query, page.getList(), 130, "description","articleData.content");
		
		return page;
	}
	
	/**
	 * 通过编号获取内容标题
	 * @return new Object[]{栏目Id,文章Id,文章标题}
	 */
	public List<Map<String, String>> findTitleByIds(String ids) {
		if(ids == null){
			return new ArrayList<Map<String, String>>();
		}
		List<Map<String, String>> list = Lists.newArrayList();
		String[] idss = StringUtils.split(ids,",");
		Article e = null;
		for(int i=0;(idss.length-i)>0;i++){
			e = articleDao.get(idss[i]);
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", e.getId());
			map.put("title", StringUtils.abbr(e.getTitle(),50));
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 获取短期处理方法
	 * sunxiao
	 * @return
	 */
	public List<Article> findArticleListByType(String type){
		List<Article> list = new ArrayList<Article>();
		String[] ids = new String[]{"",""};
		list = articleDao.findByIdIn(ids);
		return list;
	}
	
}
