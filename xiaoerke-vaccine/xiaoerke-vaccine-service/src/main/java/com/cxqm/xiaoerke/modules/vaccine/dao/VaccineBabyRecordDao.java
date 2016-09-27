package com.cxqm.xiaoerke.modules.vaccine.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineBabyRecordVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@MyBatisDao
@Repository
public interface VaccineBabyRecordDao {
    int deleteByPrimaryKey(Integer id);

    int insert(VaccineBabyRecordVo record);

    int insertSelective(VaccineBabyRecordVo record);

    List<VaccineBabyRecordVo> selectByVaccineBabyRecordVo(VaccineBabyRecordVo record);

    int updateByPrimaryKeySelective(VaccineBabyRecordVo record);

    int updateByPrimaryKey(VaccineBabyRecordVo record);
}