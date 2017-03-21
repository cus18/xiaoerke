package com.cxqm.xiaoerke.modules.activity.service;

import com.cxqm.xiaoerke.modules.activity.entity.PunchCardInfoVo;

import java.util.List;

/**
 * Created by jiangzhongge on 2017-3-17.
 */
public interface PunchCardInfoService {

    int deleteByPrimaryKey(String id);

    int insert(PunchCardInfoVo record);

    int insertSelective(PunchCardInfoVo record);

    PunchCardInfoVo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PunchCardInfoVo record);

    int updateByPrimaryKey(PunchCardInfoVo record);

    List<PunchCardInfoVo> getPunchCardInfoBySelective(PunchCardInfoVo vo);

    List<PunchCardInfoVo> getLastOnePunchCardInfoVo();

}
