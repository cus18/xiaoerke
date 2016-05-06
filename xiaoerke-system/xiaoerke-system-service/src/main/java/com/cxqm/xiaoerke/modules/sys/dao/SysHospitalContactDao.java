package com.cxqm.xiaoerke.modules.sys.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.sys.entity.SysHospitalContactVo;

import java.util.List;
import java.util.Map;

@MyBatisDao
public interface SysHospitalContactDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SysHospitalContactVo record);

    int insertSelective(SysHospitalContactVo record);

    SysHospitalContactVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysHospitalContactVo record);

    int updateByPrimaryKey(SysHospitalContactVo record);

    int updateByHospitalId(SysHospitalContactVo record);

    //获取合作医院信息sunxiao
    List<SysHospitalContactVo> getHospitalContactByInfo(Map map);
}