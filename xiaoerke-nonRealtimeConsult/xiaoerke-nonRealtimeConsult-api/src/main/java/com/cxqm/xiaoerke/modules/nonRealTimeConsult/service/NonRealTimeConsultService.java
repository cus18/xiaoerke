package com.cxqm.xiaoerke.modules.nonRealTimeConsult.service;

import com.cxqm.xiaoerke.modules.nonRealTimeConsult.entity.NonRealTimeConsultSessionVo;

import java.util.List;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public interface NonRealTimeConsultService {

    List<NonRealTimeConsultSessionVo> selectByNonRealTimeConsultSessionVo(NonRealTimeConsultSessionVo realTimeConsultSessionVo);
}
