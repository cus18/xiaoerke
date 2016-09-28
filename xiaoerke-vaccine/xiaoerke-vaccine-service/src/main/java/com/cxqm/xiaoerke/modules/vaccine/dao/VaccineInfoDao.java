package com.cxqm.xiaoerke.modules.vaccine.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineInfoVo;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineInfoWithBLOBsVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@MyBatisDao
@Repository
public interface VaccineInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(VaccineInfoWithBLOBsVo record);

    int insertSelective(VaccineInfoWithBLOBsVo record);

    VaccineInfoWithBLOBsVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VaccineInfoWithBLOBsVo record);

    int updateByPrimaryKeyWithBLOBs(VaccineInfoWithBLOBsVo record);

    int updateByPrimaryKey(VaccineInfoWithBLOBsVo record);

    List<VaccineInfoWithBLOBsVo> findVaccineList(VaccineInfoWithBLOBsVo vo);


}