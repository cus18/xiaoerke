package com.cxqm.xiaoerke.modules.sys.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorRecommend;

@MyBatisDao
public interface DoctorRecommendDao {
    int deleteByPrimaryKey(String id);

    int insert(DoctorRecommend record);

    int insertSelective(DoctorRecommend record);

    DoctorRecommend selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DoctorRecommend record);

    int updateByPrimaryKey(DoctorRecommend record);
}