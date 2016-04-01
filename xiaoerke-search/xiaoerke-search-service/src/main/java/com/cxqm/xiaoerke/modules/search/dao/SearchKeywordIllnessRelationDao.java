package com.cxqm.xiaoerke.modules.search.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.search.entity.SearchKeywordIllnessRelation;

@MyBatisDao
public interface SearchKeywordIllnessRelationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SearchKeywordIllnessRelation record);

    int insertSelective(SearchKeywordIllnessRelation record);

    SearchKeywordIllnessRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SearchKeywordIllnessRelation record);

    int updateByPrimaryKey(SearchKeywordIllnessRelation record);
}