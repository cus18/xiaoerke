package com.cxqm.xiaoerke.modules.consult.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultStatisticVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MyBatisDao
public interface ConsultStatisticDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ConsultStatisticVo record);

    int insertSelective(ConsultStatisticVo record);

    ConsultStatisticVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConsultStatisticVo record);

    int updateByPrimaryKey(ConsultStatisticVo record);

    List<Float> getConsultStatistic(HashMap<String,Object> map);

    List<ConsultStatisticVo> getConsultStatisticList(HashMap<String,String> map);

    List<Map<String, Object>> getValidateConsultAndFeedBackCounts(Map<String,Object> map);

    List<Map<String, Object>> getSendHeartPersonAndMoneyCounts(Map<String,Object> map);

    List<HashMap<String,Object>> selectConsultStatisticVoByMap(HashMap<String,Object> map);
}