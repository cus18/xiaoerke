package com.cxqm.xiaoerke.modules.sys.service;


import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.sys.entity.PatientVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional(readOnly = false)
public interface UserInfoService {

	String getPatientIdByUserId(String sysUserId);

	HashMap<String, Object> findPersonDetailInfoByUserId(String userId);

	HashMap<String, Object> findPersonInfoExecute(
			HashMap<String, Object> hashMap);

	String getUserIdByPatient(String patientRestId);

	int savePatient(PatientVo vo);

	//分页查询用户列表sunxiao
	Page<User> findUserList(Page<User> page,User user);

	User doctorOper(User user) throws Exception;

	//根据条件查询用户信息sunxiao
	List<User> getUserListByInfo(User user);

	Map createOrUpdateThirdPartPatientInfo(HashMap map);
}
