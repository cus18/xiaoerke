package com.cxqm.xiaoerke.modules.activity.service;

import com.cxqm.xiaoerke.modules.activity.entity.RedpackageResultsInfoVo;

import java.util.List;

/**
 * Created by jiangzhongge on 2017-2-20.
 */
public interface RedPackageResultsInfoService {

    int deleteByPrimaryKey(String id);

    int insert(RedpackageResultsInfoVo record);

    int insertSelective(RedpackageResultsInfoVo record);

    RedpackageResultsInfoVo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RedpackageResultsInfoVo record);

    int updateByPrimaryKey(RedpackageResultsInfoVo record);

    List<RedpackageResultsInfoVo> getRedPackageResultsBySelective(RedpackageResultsInfoVo record);

}
