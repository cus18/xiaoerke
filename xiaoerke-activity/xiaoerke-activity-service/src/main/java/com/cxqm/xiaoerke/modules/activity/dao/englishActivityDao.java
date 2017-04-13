package com.cxqm.xiaoerke.modules.activity.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.activity.entity.EnglishActivityVo;

@MyBatisDao
public interface EnglishActivityDao {
    int deleteByPrimaryKey(Integer id);

    int insert(EnglishActivityVo record);

    int insertSelective(EnglishActivityVo record);

    EnglishActivityVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EnglishActivityVo record);

    int updateByPrimaryKey(EnglishActivityVo record);

    EnglishActivityVo selectByopenId(String openid);
}