package com.cxqm.xiaoerke.modules.healthRecords.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.healthRecords.entity.BabyIllnessInfoVo;

import java.util.HashMap;
import java.util.Map;

@MyBatisDao
public interface BabyIllnessInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(BabyIllnessInfoVo record);

    int insertSelective(BabyIllnessInfoVo record);

    BabyIllnessInfoVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BabyIllnessInfoVo record);

    int updateByPrimaryKey(BabyIllnessInfoVo record);

    /***
     * 根据（已预约）号源id获取病情描述
     * @param dataMap
     * @return
     * @author chenxiaoqiong
     */
    Map<String,Object> getIllnessDetail(Map<String,Object> dataMap);
}