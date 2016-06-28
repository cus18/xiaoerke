package com.cxqm.xiaoerke.modules.consult.dao;

import com.cxqm.xiaoerke.modules.consult.entity.ConsultStatisticVo;

import java.util.List;
import java.util.Map;

public interface ConsultStatisticDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ConsultStatisticVo record);

    int insertSelective(ConsultStatisticVo record);

    ConsultStatisticVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConsultStatisticVo record);

    int updateByPrimaryKey(ConsultStatisticVo record);

    List<Integer> getConsultStatistic(Map map);
}