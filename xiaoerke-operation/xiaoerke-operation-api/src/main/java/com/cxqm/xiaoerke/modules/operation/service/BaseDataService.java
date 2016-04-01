package com.cxqm.xiaoerke.modules.operation.service;

import com.cxqm.xiaoerke.modules.operation.entity.StatisticsTitle;

import java.util.HashMap;
import java.util.List;

/**
 * 基础数据统计 Service
 *
 * @author deliang
 * @version 2015-11-05
 */

public interface BaseDataService{
    /**
     * 为StatisticsTitle表添加数据
     * @return
     */
    int insertStatisticsTitle();

    StatisticsTitle returnStatisticsTitleList(HashMap<String, String> m, int num);

    /**
     * 查询数据
     * @param startDate
     * @param endDate
     * @return
     */
    List<HashMap<String,Object>> findStatisticsTitleList(String startDate,String endDate);

    String findWechatToken();
}
