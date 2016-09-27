package com.cxqm.xiaoerke.modules.vaccine.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineStationVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@MyBatisDao
@Repository
public interface VaccineStationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(VaccineStationVo record);

    int insertSelective(VaccineStationVo record);

    List<VaccineStationVo> selectByVaccineStationVo(VaccineStationVo record);

    int updateByPrimaryKeySelective(VaccineStationVo record);

    int updateByPrimaryKeyWithBLOBs(VaccineStationVo record);

    int updateByPrimaryKey(VaccineStationVo record);
}