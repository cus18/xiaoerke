package com.cxqm.xiaoerke.modules.sys.service;


import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.sys.entity.HospitalVo;
import com.cxqm.xiaoerke.modules.sys.entity.SysHospitalContactVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional(readOnly = false)
public interface HospitalInfoService {

	String getDepartmentFullName(String doctorId, String hospitalId);

	Map<String,Object> listAllHospital(Map<String,Object> params);

	Map<String,Object> listHospitalDepartment(Map<String,Object> params);

	Page<HashMap<String, Object>> findPageAllHospital(
			Page<HashMap<String, Object>> page);

	Map<String, Object> listSecondIllnessHospital(Map<String, Object> params);
	
	List<HospitalVo> findHospitalsByDoctorIds(String[] doctorIds);
	
	//分页获取可预约日期下的医院
    Page<HashMap<String, Object>> findPageHospitalByTime(HashMap<String, Object> hospitalInfo,
                                                         Page<HashMap<String, Object>> page);
    
    //根据医院名称获取医院id
    HashMap<String,Object> findHospitalIdByHospitalName(HashMap<String,Object> hashMap);

	List<HospitalVo> findAllOrderHospital(HashMap<String, Object> newMap);

	HashMap<String,Object> getHospitalDetailInfo(String hospitalId);

	/**
	 * 插入合作机构联系人信息
	 * @param sysHospitalContactVo
	 * @return
	 */
	int insertSysHospitalContactVo(SysHospitalContactVo sysHospitalContactVo);

	Page<HashMap<String, Object>> getHospitalListByConsulta(Page<HashMap<String, Object>> page);

	//获取合作医院信息sunxiao
	List<SysHospitalContactVo> getHospitalContact(Map map);

	Map<String, Object> listDepartmentHospital(Map<String, Object> params);
}
