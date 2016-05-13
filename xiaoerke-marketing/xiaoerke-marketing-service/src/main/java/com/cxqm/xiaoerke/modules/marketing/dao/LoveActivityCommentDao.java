package com.cxqm.xiaoerke.modules.marketing.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.marketing.entity.LoveActivityComment;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jiangzhongge on 2016-5-11.
 */
@MyBatisDao
public interface LoveActivityCommentDao {

    public void saveLoveActivityComment(LoveActivityComment loveActivityComment);

    public List<LoveActivityComment> findLoveActivityComment();

}
