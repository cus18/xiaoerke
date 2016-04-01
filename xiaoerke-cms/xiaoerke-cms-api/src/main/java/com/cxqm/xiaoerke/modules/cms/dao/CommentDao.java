package com.cxqm.xiaoerke.modules.cms.dao;

import com.cxqm.xiaoerke.common.persistence.CrudDao;
import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.cms.entity.Comment;

/**
 * 评论DAO接口
 * @author sunxiao
 * @version 2015-11-27
 */
@MyBatisDao
public interface CommentDao extends CrudDao<Comment> {

	/**
	 * 获取评论列表
	 * 2015年12月3日 下午12:11:07
	 * @return Page<Comment>
	 * @author sunxiao
	 */
	Page<Comment> findList(Page<Comment> page,Comment comment);
	
}
