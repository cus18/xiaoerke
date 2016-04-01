package com.cxqm.xiaoerke.modules.bankend.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorHospitalRelationVo;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorLocationVo;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;

import org.springframework.transaction.annotation.Transactional;

/**
 * 医院相关
 * @author 得良
 * @version 2015-07-29
 */
@Transactional(readOnly = false)
public interface DoctorService {
	/**
	 * 获取医生信息
	 * @param page
	 * @param doctorVo
	 * @return
	 */
	Page<DoctorVo> findDoctor(Page<DoctorVo> page, DoctorVo doctorVo);

	//根据doctorVo中的参数获取医生的基本信息 @author 09_zdl
	List<DoctorVo> findDoctorDetailInfo(DoctorVo doctorVo);

	void deleteDoctorHospitalRelation(String doctorHospitalRelationId, DoctorHospitalRelationVo doctorHospitalRelationVo);

	void saveDoctorHospitalRelation(DoctorHospitalRelationVo doctorHospitalRelationVo, Map<String, Object> map, Map<String, Object> oldmap, Map<String, Object> tempmap);

	/**
	 * @author ft
	 * 
	 * @param key 可以为  doctorId
	 * @param length 文件长度
	 * @param in 文件流
	 */
	void uploadDoctorPic(String key, Long length, InputStream in);

	void deleteDoctorByDoctorId(String doctorId);

	void saveEditDoctor(DoctorVo doctorVo);

	String findAllHospitalByDoctorId(DoctorVo doctorVo);

	List<String> findAllHospitalListByDoctorId(DoctorVo doctorVo);

	DoctorHospitalRelationVo findDoctorHospitalRelation(String hospitalName, String doctorId);

	void  saveDoctorHospitalRelation(HashMap<String, Object> hashMap, List<DoctorLocationVo> list);

	DoctorHospitalRelationVo findDoctorHospitalRelationById(String doctorHospitalRelationId);

	void deleteDoctorLocation(DoctorHospitalRelationVo doctorHospitalRelationVo);

	DoctorHospitalRelationVo findDoctorHospitalRelationByIds(String hospitalId,String doctorId);

}
