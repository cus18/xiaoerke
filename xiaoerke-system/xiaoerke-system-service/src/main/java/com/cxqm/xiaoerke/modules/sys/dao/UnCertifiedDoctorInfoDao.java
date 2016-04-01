package com.cxqm.xiaoerke.modules.sys.dao;

import java.util.List;

import com.cxqm.xiaoerke.common.persistence.CrudDao;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.sys.entity.UnCertifiedDoctorInfo;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;

@MyBatisDao
public interface UnCertifiedDoctorInfoDao extends CrudDao<DoctorVo> {
    int deleteByPrimaryKey(Long id);

    int insert(UnCertifiedDoctorInfo record);

    int insertSelective(UnCertifiedDoctorInfo record);

    UnCertifiedDoctorInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UnCertifiedDoctorInfo record);

    int updateByPrimaryKey(UnCertifiedDoctorInfo record);
    
    List<UnCertifiedDoctorInfo> findPageUnCertifiedDoctorInfo(UnCertifiedDoctorInfo page);
}