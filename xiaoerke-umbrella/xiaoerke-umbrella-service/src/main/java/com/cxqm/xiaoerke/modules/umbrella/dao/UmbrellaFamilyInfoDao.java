package com.cxqm.xiaoerke.modules.umbrella.dao;

import com.cxqm.xiaoerke.modules.umbrella.entity.UmbrellaFamilyInfo;

public interface UmbrellaFamilyInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(UmbrellaFamilyInfo record);

    int insertSelective(UmbrellaFamilyInfo record);

    UmbrellaFamilyInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UmbrellaFamilyInfo record);

    int updateByPrimaryKey(UmbrellaFamilyInfo record);
}