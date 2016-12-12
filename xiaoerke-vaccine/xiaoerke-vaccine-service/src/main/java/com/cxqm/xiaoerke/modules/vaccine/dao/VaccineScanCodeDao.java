package com.cxqm.xiaoerke.modules.vaccine.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineScanCodeVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@MyBatisDao
@Repository
public interface VaccineScanCodeDao {

    int deleteByPrimaryKey(Integer id);

    int insertSelective(VaccineScanCodeVo record);

    int updateByPrimaryKeySelective(VaccineScanCodeVo record);

    List<VaccineScanCodeVo> findVaccineScanCodeList(VaccineScanCodeVo vo);


}