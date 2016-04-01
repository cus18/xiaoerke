package com.cxqm.xiaoerke.modules.operation.service;

import com.cxqm.xiaoerke.modules.operation.entity.SysStatistics;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务综合统计 Service
 *
 * @author deliang
 * @version 2015-11-05
 */

public interface OperationsComprehensiveService  {

    //获取业务综合统计数据表
    List<SysStatistics> getOperationsComprehensiveList(SysStatistics sysStatisticsVo);

    int insertBatchUserStatistic(List<HashMap<String, Object>> userLog);

    List<WechatAttention> getQDStatisticData(HashMap paramMap);

    List<WechatAttention> getQDMarketerData(HashMap paramMap);

    List<WechatAttention> getQDCancelStatisticData(HashMap paramMap);

    List<HashMap<String,Object>>  getChannelStatisticData(HashMap hashMap);
}
