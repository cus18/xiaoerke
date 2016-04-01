package com.cxqm.xiaoerke.modules.healthRecords.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.healthRecords.entity.BabyIllnessInfoVo;

@MyBatisDao
public interface BabyIllnessInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(BabyIllnessInfoVo record);

    int insertSelective(BabyIllnessInfoVo record);

    BabyIllnessInfoDao selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BabyIllnessInfoVo record);

    int updateByPrimaryKey(BabyIllnessInfoVo record);
}