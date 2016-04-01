package com.cxqm.xiaoerke.modules.sys.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = false)
public interface DoctorLocationService {

	String findPatientLocationId(String patientRegisterServiceId) ;

	List<HashMap<String, Object>> findDoctorLocationByDoctorIdService(
			String sysDoctorId);

	List getDoctorLocationInfo(String doctorId);

	Map<String, Object> findDoctorLocationByDoctorId(Map<String, Object> params);
	
	void updateWaiteTime(HashMap<String, Object> updateMap);
    
}
