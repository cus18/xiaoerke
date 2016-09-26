package com.cxqm.xiaoerke.modules.vaccine.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineBabyRecordVo;
import org.springframework.stereotype.Repository;

@MyBatisDao
@Repository
public interface VaccineBabyRecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(VaccineBabyRecordVo record);

    int insertSelective(VaccineBabyRecordVo record);

    VaccineBabyRecordVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VaccineBabyRecordVo record);

    int updateByPrimaryKey(VaccineBabyRecordVo record);
}