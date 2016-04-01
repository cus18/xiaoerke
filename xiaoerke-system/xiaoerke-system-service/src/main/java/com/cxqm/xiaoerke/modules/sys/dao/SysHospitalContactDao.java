package com.cxqm.xiaoerke.modules.sys.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.sys.entity.SysHospitalContactVo;

@MyBatisDao
public interface SysHospitalContactDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SysHospitalContactVo record);

    int insertSelective(SysHospitalContactVo record);

    SysHospitalContactVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysHospitalContactVo record);

    int updateByPrimaryKey(SysHospitalContactVo record);

    int updateByHospitalId(SysHospitalContactVo record);

}