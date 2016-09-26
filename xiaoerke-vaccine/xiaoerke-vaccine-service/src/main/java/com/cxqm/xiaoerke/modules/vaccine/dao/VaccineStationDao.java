package com.cxqm.xiaoerke.modules.vaccine.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineStationVo;
import org.springframework.stereotype.Repository;

@MyBatisDao
@Repository
public interface VaccineStationDao {
    int deleteByPrimaryKey(Integer id);

    int insert(VaccineStationVo record);

    int insertSelective(VaccineStationVo record);

    VaccineStationVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VaccineStationVo record);

    int updateByPrimaryKeyWithBLOBs(VaccineStationVo record);

    int updateByPrimaryKey(VaccineStationVo record);
}