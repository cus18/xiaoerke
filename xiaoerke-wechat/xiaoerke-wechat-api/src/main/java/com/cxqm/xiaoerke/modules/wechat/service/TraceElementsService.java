package com.cxqm.xiaoerke.modules.wechat.service;

import com.cxqm.xiaoerke.modules.wechat.entity.TraceElementsVo;

/**
 * Created by wangbaowei on 16/9/30.
 */
public interface TraceElementsService {

    int insertSelective(TraceElementsVo record);

    TraceElementsVo selectByOpenid(String openid);

    int updateByPrimaryKeySelective(TraceElementsVo record);
}
