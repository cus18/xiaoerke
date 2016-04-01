package com.cxqm.xiaoerke.modules.sys.service;


import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorGroupVo;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorHospitalRelationVo;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional(readOnly = false)
public interface DoctorGroupInfoService {

	HashMap<String,Object> findPageAllDoctorGroup(Page<HashMap<String, Object>> page,HashMap<String, Object> param);

	HashMap<String,Object> getDoctorGroupInfo(String doctorGroupId);

	Page<HashMap<String, Object>> findDoctorByDoctorGroup(HashMap<String, Object> doctorMap, Page<HashMap<String, Object>> page);

	DoctorGroupVo getDoctorGroupInfoByDoctor(String doctorId);
	
	/**
     * 激活医生电话咨询时修改sys_doctor_group表
     * sunxiao
     */
	void updateSysDoctorGroup(Map<String, Object> map);
}
