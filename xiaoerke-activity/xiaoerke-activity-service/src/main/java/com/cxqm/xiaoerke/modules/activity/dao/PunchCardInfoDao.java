package com.cxqm.xiaoerke.modules.activity.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.activity.entity.PunchCardInfoVo;

import java.util.List;

@MyBatisDao
public interface PunchCardInfoDao {

    int deleteByPrimaryKey(String id);

    int insert(PunchCardInfoVo record);

    int insertSelective(PunchCardInfoVo record);

    PunchCardInfoVo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PunchCardInfoVo record);

    int updateByPrimaryKey(PunchCardInfoVo record);

    List<PunchCardInfoVo> getPunchCardInfoBySelective(PunchCardInfoVo vo);

    List<PunchCardInfoVo> getLastOnePunchCardInfoVo();

}