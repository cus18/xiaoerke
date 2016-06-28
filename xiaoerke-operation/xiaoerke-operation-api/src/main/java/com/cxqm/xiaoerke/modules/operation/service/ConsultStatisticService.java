package com.cxqm.xiaoerke.modules.operation.service;

import com.cxqm.xiaoerke.modules.consult.entity.ConsultStatisticVo;

import java.util.List;
import java.util.Map;

/**
 * 咨询统计 Service
 *
 * @author deliang
 * @version 2015-11-05
 */

public interface ConsultStatisticService {
    List<Integer> getConsultStatistic(Map map);

    int insertSelective(ConsultStatisticVo record);
}
