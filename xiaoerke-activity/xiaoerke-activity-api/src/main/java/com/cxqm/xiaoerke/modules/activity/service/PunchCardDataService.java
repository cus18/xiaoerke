package com.cxqm.xiaoerke.modules.activity.service;

import com.cxqm.xiaoerke.modules.activity.entity.PunchCardDataVo;

import java.util.List;

/**
 * Created by jiangzhongge on 2017-3-17.
 */
public interface PunchCardDataService {

    int deleteByPrimaryKey(String id);

    int insert(PunchCardDataVo record);

    int insertSelective(PunchCardDataVo record);

    PunchCardDataVo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PunchCardDataVo record);

    int updateByPrimaryKey(PunchCardDataVo record);

    List<PunchCardDataVo> getLastOneData();

}
