package com.cxqm.xiaoerke.modules.consult.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorInfoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultPhoneRecordVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisDao
public interface ConsultDoctorInfoDao {

    int insert(ConsultDoctorInfoVo vo);

    int insertSelective(ConsultDoctorInfoVo record);

    ConsultDoctorInfoVo selectByPrimaryKey(Integer id);

    List<ConsultDoctorInfoVo> getConsultDoctorByInfo(Map map);

    int updateByPrimaryKeySelective(ConsultDoctorInfoVo record);

    int updateByPrimaryKey(ConsultDoctorInfoVo record);

    List<Object> getConsultDoctorDepartment();
}