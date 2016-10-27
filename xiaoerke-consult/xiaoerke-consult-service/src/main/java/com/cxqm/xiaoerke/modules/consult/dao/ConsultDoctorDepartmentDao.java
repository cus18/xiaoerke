package com.cxqm.xiaoerke.modules.consult.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorDepartmentVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorInfoVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisDao
public interface ConsultDoctorDepartmentDao {

    List<ConsultDoctorDepartmentVo> getConsultDoctorDepartmentByInfo(Map map);

    void saveDepartment(ConsultDoctorDepartmentVo vo);

    int deleteByPrimaryKey(String id);

    int insert(ConsultDoctorDepartmentVo record);

    int insertSelective(ConsultDoctorDepartmentVo record);

    ConsultDoctorDepartmentVo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ConsultDoctorDepartmentVo record);

    int updateByPrimaryKey(ConsultDoctorDepartmentVo record);

    List<ConsultDoctorDepartmentVo> getShowDepartmentList();

    void updateDepartment(ConsultDoctorDepartmentVo vo);

    List<ConsultDoctorDepartmentVo> findDepartmentListByInfo(ConsultDoctorDepartmentVo vo);

    void deleteDepartment(ConsultDoctorDepartmentVo vo);
}