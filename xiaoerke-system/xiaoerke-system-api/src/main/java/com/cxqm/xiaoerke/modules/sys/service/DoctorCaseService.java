package com.cxqm.xiaoerke.modules.sys.service;


import java.util.HashMap;
import java.util.List;

import com.cxqm.xiaoerke.modules.sys.entity.DoctorCaseVo;

import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = false)
public interface DoctorCaseService {

	List<DoctorCaseVo> findDoctorCase(String doctorId);

	int findDoctorCaseNumber(String doctorId);
	
	//更改案例数
    void updateDoctorCaseInfo(HashMap<String, Object> executeMap);
    
  //保存案例信息 @author zdl
    void saveDoctorCaseInfo(HashMap<String, Object> executeMap) ;

    void saveDoctorCase(DoctorCaseVo doctorCaseVo);

    Integer findDoctorCaseNumberByName(String doctorId,String caseName);
	
}
