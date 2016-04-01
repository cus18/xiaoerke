package com.cxqm.xiaoerke.modules.operation.dao;

import com.cxqm.xiaoerke.common.persistence.CrudDao;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.operation.entity.SysStatistics;

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
}