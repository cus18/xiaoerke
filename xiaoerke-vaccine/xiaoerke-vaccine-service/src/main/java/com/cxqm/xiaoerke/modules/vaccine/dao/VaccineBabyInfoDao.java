package com.cxqm.xiaoerke.modules.vaccine.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineBabyInfoVo;
import org.springframework.stereotype.Repository;

@MyBatisDao
@Repository
public interface VaccineBabyInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(VaccineBabyInfoVo record);

    int insertSelective(VaccineBabyInfoVo record);

    VaccineBabyInfoVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VaccineBabyInfoVo record);

    int updateByPrimaryKey(VaccineBabyInfoVo record);
}