package com.cxqm.xiaoerke.modules.search.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.search.entity.SearchKeyword;

@MyBatisDao
public interface SearchKeywordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SearchKeyword record);

    int insertSelective(SearchKeyword record);

    SearchKeyword selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SearchKeyword record);

    int updateByPrimaryKey(SearchKeyword record);
}