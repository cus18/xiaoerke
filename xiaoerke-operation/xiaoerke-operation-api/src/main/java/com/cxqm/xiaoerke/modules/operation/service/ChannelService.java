package com.cxqm.xiaoerke.modules.operation.service;

import com.cxqm.xiaoerke.modules.operation.entity.ChannelInfo;

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
    //添加渠道
    int insertChannel(ChannelInfo channelInfo);
    //渠道查询
    List<ChannelInfo> getChannelInfos();
    //渠道分类统计数据
    List<HashMap<String, Object>> getChannelCategoryStatistics(HashMap hashMap);
    //渠道明细统计数据
    List<HashMap<String, Object>> getChannelDetailStatistics(HashMap hashMap);
    //获取用户统计（部门）
    List<HashMap<String, Object>> getUserStatisticsDepartment(HashMap hashMap);
    //获取用户统计（渠道）
    List<HashMap<String, Object>> getUserStatisticsChannel(HashMap hashMap);
    //获取所有的渠道
    List<String> getAllChannels();
}
