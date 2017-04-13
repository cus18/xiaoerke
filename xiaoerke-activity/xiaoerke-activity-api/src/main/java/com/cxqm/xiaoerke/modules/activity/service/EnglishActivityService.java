package com.cxqm.xiaoerke.modules.activity.service;

import com.cxqm.xiaoerke.modules.activity.entity.EnglishActivityVo;

/**
 * Created by wangbaowei on 2017/4/12.
 */
public interface EnglishActivityService {

    int deleteByPrimaryKey(Integer id);

    int insert(EnglishActivityVo record);

    int insertSelective(EnglishActivityVo record);

    EnglishActivityVo selectByPrimaryKey(Integer id);

    EnglishActivityVo selectByopenId(String openid);

    int updateByPrimaryKeySelective(EnglishActivityVo record);

    int updateByPrimaryKey(EnglishActivityVo record);
}
