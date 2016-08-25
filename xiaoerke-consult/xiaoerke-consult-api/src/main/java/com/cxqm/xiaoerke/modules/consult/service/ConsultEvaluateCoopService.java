package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.consult.entity.ConsultEvaluateCoopVo;

import java.util.List;

/**
 * 合作方评价信息接口
 * Created by jiangzhongge on 2016-8-24.
 */
public interface ConsultEvaluateCoopService {

    int addConsultEvaluateCoops(ConsultEvaluateCoopVo consultEvaluateCoopVo);

    List<ConsultEvaluateCoopVo> getConsultEvaluateCoops (ConsultEvaluateCoopVo consultEvaluateCoopVo);

}
