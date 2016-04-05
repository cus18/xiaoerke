package com.cxqm.xiaoerke.modules.order.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.order.entity.ConsulPhonetDoctorRelationVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisDao
public interface PhoneConsultDoctorRelationDao {

    int deleteByPrimaryKey(Integer id);

    int insert(ConsulPhonetDoctorRelationVo record);

    int insertSelective(ConsulPhonetDoctorRelationVo record);

    ConsulPhonetDoctorRelationVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConsulPhonetDoctorRelationVo record);

    int updateByPrimaryKey(ConsulPhonetDoctorRelationVo record);

    /**
     * 根据条件查询医生电话咨询的信息
     * sunxiao
     */
    List<ConsulPhonetDoctorRelationVo> getConsultPhoneDoctorRelationByInfo(ConsulPhonetDoctorRelationVo vo);

    ConsulPhonetDoctorRelationVo selectByDoctorId(@Param("doctorId") String doctorId);
}