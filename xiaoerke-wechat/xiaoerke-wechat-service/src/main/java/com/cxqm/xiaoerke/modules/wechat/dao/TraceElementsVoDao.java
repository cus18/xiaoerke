package com.cxqm.xiaoerke.modules.wechat.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.wechat.entity.TraceElementsVo;

@MyBatisDao
public interface TraceElementsVoDao {

    int deleteByPrimaryKey(Integer id);

    int insert(TraceElementsVo record);

    int insertSelective(TraceElementsVo record);

    TraceElementsVo selectByPrimaryKey(Integer id);

    TraceElementsVo selectByOpenid(String openid);

    int updateByPrimaryKeySelective(TraceElementsVo record);

    int updateByPrimaryKey(TraceElementsVo record);
}