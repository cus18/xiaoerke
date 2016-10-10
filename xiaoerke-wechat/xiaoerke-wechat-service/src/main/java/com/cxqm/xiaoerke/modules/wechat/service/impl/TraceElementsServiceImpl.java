package com.cxqm.xiaoerke.modules.wechat.service.impl;

import com.cxqm.xiaoerke.modules.wechat.dao.TraceElementsVoDao;
import com.cxqm.xiaoerke.modules.wechat.entity.TraceElementsVo;
import com.cxqm.xiaoerke.modules.wechat.service.TraceElementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wangbaowei on 16/9/30.
 *
 */

@Service
public class TraceElementsServiceImpl implements TraceElementsService {

    @Autowired
    private TraceElementsVoDao traceElementsVoDao;

    @Override
    public int insertSelective(TraceElementsVo record) {
        return traceElementsVoDao.insertSelective(record);
    }

    @Override
    public TraceElementsVo selectByOpenid(String openid) {
        return traceElementsVoDao.selectByOpenid(openid);
    }

    @Override
    public int updateByPrimaryKeySelective(TraceElementsVo record){
        return  traceElementsVoDao.updateByPrimaryKeySelective(record);
    };
}
