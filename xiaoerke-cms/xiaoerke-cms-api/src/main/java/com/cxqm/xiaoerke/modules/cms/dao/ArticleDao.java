/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.cms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cxqm.xiaoerke.common.persistence.CrudDao;
import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.cms.entity.Article;
import com.cxqm.xiaoerke.modules.cms.entity.Category;

/**
 * 文章DAO接口
 * @author ThinkGem
 * @version 2013-8-23
 */
@MyBatisDao
public interface ArticleDao extends CrudDao<Article> {
	
	public List<Article> findByIdIn(String[] ids);
//	{
//		return find("from Article where id in (:p1)", new Parameter(new Object[]{ids}));
//	}
	
	//用户阅读文章，阅读次数加一 sunxiao
	public int updateHitsAddOne(String id);
	
	//用户分享文章，分享次数加一 sunxiao
	public int updateShareAddOne(String id);
	
	//用户点赞更新点赞次数加一 sunxiao
	public int updatePraiseNumber(@Param("articleId")String articleId,@Param("praiseNumber")String praiseNumber);
	
	//用户评论文章，评论次数加一 sunxiao
	public int updateCommentAddOne(String id);
//	{
//		return update("update Article set hits=hits+1 where id = :p1", new Parameter(id));
//	}
	
	public int updateExpiredWeight(Article article);
	
	public List<Category> findStats(Category category);
//	{
//		return update("update Article set weight=0 where weight > 0 and weightDate < current_timestamp()");
//	}
	Page<Article> findList(Page<Article> page,Article article);
}
