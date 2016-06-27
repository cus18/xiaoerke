package com.cxqm.xiaoerke.modules.umbrella.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.umbrella.entity.UmbrellaFamilyInfo;

import java.util.List;
import java.util.Map;

@MyBatisDao
public interface UmbrellaFamilyInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(UmbrellaFamilyInfo record);

    int insertSelective(UmbrellaFamilyInfo record);

    UmbrellaFamilyInfo selectByPrimaryKey(Integer id);

    List<UmbrellaFamilyInfo> selectByumbrellaId(Integer umbrellaId);

    int updateByPrimaryKeySelective(UmbrellaFamilyInfo record);

    int updateByPrimaryKey(UmbrellaFamilyInfo record);

    Map getUmbrellaNum(Map<String, Object> map);


    /**
     * 根据宝护伞ID 删除成员信息
     * @param id
     * @return
     */
    int deleteByUmbrellaId(Integer id);
}