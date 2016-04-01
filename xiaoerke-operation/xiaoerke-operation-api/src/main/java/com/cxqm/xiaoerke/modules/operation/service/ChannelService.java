package com.cxqm.xiaoerke.modules.operation.service;

import java.util.HashMap;
import java.util.List;

/**
 * 渠道统计 Service
 *
 * @author deliang
 * @version 2015-11-05
 */

public interface ChannelService  {

    //获取渠道统计数据
    List<HashMap<String, Object>> getTuiStatisticData(HashMap hashMap);
}
