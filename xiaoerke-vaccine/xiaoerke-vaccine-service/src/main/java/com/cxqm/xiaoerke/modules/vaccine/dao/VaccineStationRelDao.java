package com.cxqm.xiaoerke.modules.vaccine.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineStationRelVo;
import org.springframework.stereotype.Repository;

@MyBatisDao
@Repository
public interface VaccineStationRelDao {
    int insert(VaccineStationRelVo record);

    int insertSelective(VaccineStationRelVo record);
}