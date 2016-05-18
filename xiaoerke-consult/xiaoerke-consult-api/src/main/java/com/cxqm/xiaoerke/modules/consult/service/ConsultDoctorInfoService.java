package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorInfoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultPhoneRecordVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;

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
}
