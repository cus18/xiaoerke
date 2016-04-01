package com.cxqm.xiaoerke.modules.sys.service;


import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.modules.sys.entity.PatientVo;

import java.util.HashMap;

@Transactional(readOnly = false)
public interface UserInfoService {

	String getPatientIdByUserId(String sysUserId);

	HashMap<String, Object> findPersonDetailInfoByUserId(String userId);

	HashMap<String, Object> findPersonInfoExecute(
			HashMap<String, Object> hashMap);

	String getUserIdByPatient(String patientRestId);

	int savePatient(PatientVo vo);
}
