/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.cms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.service.CrudService;
import com.cxqm.xiaoerke.modules.cms.dao.CommentDao;
import com.cxqm.xiaoerke.modules.cms.entity.Comment;

/**
 * 评论Service
 * @author ThinkGem
 * @version 2013-01-15
 */
@Service
@Transactional(readOnly = false)
public class CommentService extends CrudService<CommentDao, Comment> {

	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private CommentDao commentDao;
	
	public Page<Comment> findPage(Page<Comment> page, Comment comment) {
		comment.getSqlMap().put("dsf", dataScopeFilter(comment.getCurrentUser(), "o", "u"));
		return commentDao.findList(page, comment);
	}
	
	public void delete(Comment entity, Boolean isRe) {
		super.delete(entity);
	}
	
	public void saveComment(Comment entity){
		super.save(entity);
		articleService.updateCommentAddOne(entity.getContentId());
	}
}
