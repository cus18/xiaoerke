package com.cxqm.xiaoerke.modules.operation.dao;

import com.cxqm.xiaoerke.common.persistence.CrudDao;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.operation.entity.ChannelInfo;
import com.cxqm.xiaoerke.modules.operation.entity.SysStatistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    //获取用户统计（部门）
    List<HashMap<String, Object>> getUserStatisticsDepartment(HashMap hashMap);
    //获取用户统计（渠道）
    public List<HashMap<String, Object>> getUserStatisticsChannel(HashMap hashMap);
    //获取所有的渠道
    List<String> getAllChannels();
    List<HashMap<String, Object>> getTotalConsultCountsByChannel(Map<String, Object> map);
    List<HashMap<String, Object>> getNewConsultCountsByChannel(Map<String, Object> map);

    List<HashMap<String, Object>> getTotalConsultCountsByDepartment(Map<String, Object> map);
    List<HashMap<String, Object>> getNewConsultCountsByDepartment(Map<String, Object> map);

    List<Map<String, Object>> getAllChannelsByMarketer(Map<String, Object> map);

    int deleteChannelInfo(String channelId);

    List<HashMap<String, Object>> getNewUserAttentionAndRemainStatistics(Map<String, Object> map);
}