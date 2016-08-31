package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorInfoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultPhoneRecordVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 咨询医生信息service
 * @author sunxiao
 * 2016-04-26
 */
public interface ConsultDoctorInfoService {

    int saveConsultDoctorInfo(ConsultDoctorInfoVo vo);

    Map getConsultDoctorInfo(User user);

    List<ConsultDoctorInfoVo> getConsultDoctorByInfo(Map map);

    int consultDoctorInfoOper(ConsultDoctorInfoVo vo);

    List<String> getConsultDoctorDepartment();

    ConsultDoctorInfoVo getConsultDoctorInfoByUserId(String userId);

    List<User> findUserOrderByDepartment(User user);

    List<User> findUserByUserName(User user);

    List<Map> getDoctorInfoMoreByUserId(String userId);

    List<ConsultDoctorInfoVo> findManagerDoctorInfoBySelective(ConsultDoctorInfoVo consultDoctorInfoVo);

    void saveLecture(ConsultDoctorInfoVo consultDoctorInfoVo);

    List<ConsultDoctorInfoVo> getConsultLecture(Map param);

    HashMap<String, Object> getConsultDoctorHomepageInfo(String userId);

    HashMap<String, Object> findDoctorAllEvaluation(Map<String, Object> param);
}
