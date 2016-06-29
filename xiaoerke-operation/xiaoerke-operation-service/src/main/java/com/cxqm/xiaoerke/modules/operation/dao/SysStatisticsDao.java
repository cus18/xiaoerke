package com.cxqm.xiaoerke.modules.operation.dao;

import com.cxqm.xiaoerke.common.persistence.CrudDao;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.operation.entity.ChannelInfo;
import com.cxqm.xiaoerke.modules.operation.entity.SysStatistics;

import java.util.HashMap;
import java.util.List;

@MyBatisDao
public interface SysStatisticsDao extends CrudDao<SysStatisticsDao> {
    int deleteByPrimaryKey(String id);

    int insert(SysStatistics record);

    int insertSelective(SysStatistics record);

    SysStatistics selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysStatistics record);

    int updateByPrimaryKey(SysStatistics record);

    //获取业务综合统计数据
    List<SysStatistics> getOperationsComprehensiveList(SysStatistics sysStatisticsVo);

    //添加渠道
    int insertChannel(ChannelInfo channelInfo);
    //渠道查询
    List<ChannelInfo> getChannelInfos();
    //查询渠道分类统计数据
    List<HashMap<String, Object>> getChannelCategoryStatistics(HashMap hashMap);
    //查询渠道细分统计数据
    List<HashMap<String, Object>> getChannelDetailStatistics(HashMap hashMap);
    //获取所有的渠道
    List<String> getAllChannels();
}