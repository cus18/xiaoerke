package com.cxqm.xiaoerke.modules.sys.service;


import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.ObjectUtils;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorHospitalRelationVo;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional(readOnly = false)
public interface DoctorInfoService {

	Page<HashMap<String, Object>> findDoctorByHospital(HashMap<String, Object> doctorMap, Page<HashMap<String, Object>> page);

	Map getDepartmentName(HashMap<String, Object> Map);

	List<DoctorHospitalRelationVo> getDoctorHospitalRelationVo(HashMap<String, Object> Map);

	String getDoctorNameByDoctorId(String doctorId);

	String getDoctorExpertiseById(String doctorId, String hospitalId, String departmentLevel1);

	String getDoctorCardExpertiseById(String doctorId);

	HashMap<String, Object> findDoctorDetailInfoByUserId(String userId);

	Page<HashMap<String, Object>> listSecondIllnessDoctor(HashMap<String, Object> secondIllnessInfo, Page<HashMap<String, Object>> page);

	Page<HashMap<String, Object>> listSecondIllnessDoctor4Consult(HashMap<String, Object> secondIllnessInfo, Page<HashMap<String, Object>> page);

	Page<HashMap<String, Object>> findPageAllDoctor(Page<HashMap<String, Object>> page);
	
	public Page<HashMap<String, Object>> findPageAllDoctorByDoctorIds(String[] doctorIds, String hospitalId, String orderBy, Page<HashMap<String, Object>> page);

	Map<String, Object> findDoctorDetailInfo(Map map);
	
	Map<String, Object> findDoctorDetailInfoAndType(Map map);
	
	HashMap<String,Object> findDoctorScoreInfo(String sys_doctor_id);
	
	//分页查询一个可预约日期下的医生
    Page<HashMap<String, Object>> findPageDoctorByTime(HashMap<String, Object> dateInfo, Page<HashMap<String, Object>> page);

	Page<HashMap<String, Object>> findPageDoctorByTime4Consult(HashMap<String, Object> dateInfo, Page<HashMap<String, Object>> page);
    
    //获取某个医院的某个可预约日期下的医生
    Page<HashMap<String, Object>> findPageDoctorByTimeAndHospital(HashMap<String, Object> dateInfo, Page<HashMap<String, Object>> page);

    //获取医生一个加号的详细信息
    List<HashMap<String, Object>> findAppointmentInfoDetail(HashMap<String, Object> appointmentInfo);
    
  //查找一个医院的科室下面的所有医生
    Page<HashMap<String, Object>> findPageDoctorByDepartment(HashMap<String, Object> hospitalInfo, Page<HashMap<String, Object>> page);

	Page<HashMap<String, Object>> findPageDoctorByRecommend(HashMap<String, Object> recommendInfo, Page<HashMap<String, Object>> page);

	Page<HashMap<String, Object>> findPageDoctorByIllnessSecond(HashMap<String, Object> secondIllnessInfo, Page<HashMap<String, Object>> page);

	Page<HashMap<String, Object>> findPageDoctorByIllnessSecondAndHospital(HashMap<String, Object> secondIllnessInfo, Page<HashMap<String, Object>> page);
	
	List<HashMap<String,Object>> getDoctorHospitalInfo(String doctorId);
	
	List<HashMap<String,Object>> getDoctorAppointmentTime(HashMap<String,Object> map);
	
	DoctorVo findDoctorByRegisterId(@Param("registerId") String registerId);
	
	//关注后更新该医生的粉丝数量
    void updateDoctorFansExecute(HashMap<String,Object> hashMap);

	List<DoctorVo> findAllOrderDoctorList(HashMap<String,Object> seachMapd);

	int findCountDoctorNumber(HashMap<String,Object> hashMap);

	int findCountDoctorCountNmuber();

	List<HashMap<String,Object>> findDoctorByDoctorId(HashMap<String, Object> hashMap);

	//根据doctorId查询“可预约号源数”
	int findDoctorCanAppointNumber(HashMap<String,Object> hashMap);

	//根据doctorId查询“已预约号源数”
	int findDoctorAlreadyAppointNumber(HashMap<String,Object> hashMap);

	List<HashMap> findDoctorByInfo(HashMap<String, Object> hashMap);

	void insertDoctor(HashMap<String,Object> hashMap);

	Map getDoctorIdByUserIdExecute(Map sysUserId);

	void update(DoctorVo doctorVo);

	Page<HashMap<String, Object>>findPageConsultaDoctorByDepartment(HashMap<String, Object> hospitalInfo, Page<HashMap<String, Object>> page);

    String findOpenIdByDoctorId(String doctorId);

	List<Map<String, Object>> findPageConsultaDoctorByDepartment(String hospitalInfo);
}
