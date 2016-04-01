/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.cms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.common.service.CrudService;
import com.cxqm.xiaoerke.modules.cms.dao.ArticleDataDao;
import com.cxqm.xiaoerke.modules.cms.entity.ArticleData;

import java.util.Map;

/**
 * 站点Service
 * @author ThinkGem
 * @version 2013-01-15
 */
@Service
@Transactional(readOnly = false)
public class ArticleDataService extends CrudService<ArticleDataDao, ArticleData> {

    @Autowired
    private ArticleDataDao articleDataDao;

    /**
     * 更新文章阅读量`分享数等信息
     * */
    public void updateArticleInfo(Map<String, Object> params){
        articleDataDao.updateArticleInfo(params);
    }

}
