package com.cxqm.xiaoerke.modules.consult.dao;


import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorInfoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultPhoneRecordVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
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

    List<String> getConsultDoctorDepartment();

    ConsultDoctorInfoVo getConsultDoctorInfoByUserId(@Param("userId")String userId);

    //jiangzg add 2016年6月20日11:17:47
    List<Map> getDoctorInfoMoreByUserId(@Param("userId")String userId);

    List<User> findUserOrderByDepartment(User user);

    List<User> findUserByUserName(User user);

    // jiangzg add 查询具有某种权限的医生信息 2016-8-12 10:55:20
    List<ConsultDoctorInfoVo> findManagerDoctorInfoBySelective(ConsultDoctorInfoVo consultDoctorInfoVo);

    void saveLecture (ConsultDoctorInfoVo consultDoctorInfoVo);

    List<ConsultDoctorInfoVo> getConsultLecture(Map param);

    List<ConsultDoctorInfoVo> getStarDoctorList();
}