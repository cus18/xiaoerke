package com.cxqm.xiaoerke.modules.sys.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.sys.dao.DoctorLocationDao;
import com.cxqm.xiaoerke.modules.sys.service.DoctorLocationService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;

@Service
@Transactional(readOnly = false)
public class DoctorLocationServiceImpl implements DoctorLocationService{

	
	@Autowired
	private DoctorLocationDao doctorLocationDao;
	
	/**
     * 根据订单主键查询用户的就诊地址 location_id
     */
	@Override
    public String findPatientLocationId(String patientRegisterServiceId) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("patientRegisterServiceId", patientRegisterServiceId);
        HashMap<String, Object> resultMap = doctorLocationDao.findPatientLocationId(hashMap);
        if (resultMap != null && resultMap.size() > 0) {
            return (String) resultMap.get("location_id");
        }
        return null;
    }
	
	   //获取医生的就诊地址信息
	@Override
    public List<HashMap<String, Object>> findDoctorLocationByDoctorIdService(String sysDoctorId) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("sysDoctorId", sysDoctorId);
        List<HashMap<String, Object>> resultList = doctorLocationDao.findDoctorLocationByDoctorId(hashMap);
        return resultList;
    }
	
	/**
	 * // * @param getPatientRegisterInfo
	 */
	@Override
	public List getDoctorLocationInfo(String doctorId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("doctorId", doctorId);
		List list = doctorLocationDao.getDoctorLocationInfo(map);
		return list;
	}
	
	@Override
	public Map<String,Object> findDoctorLocationByDoctorId(Map<String, Object> params)
	{
		String sysDoctorId = (String) params.get("sys_doctor_id");
		//获取医生的就诊地址信息
		List<HashMap<String, Object>> locationList = this.findDoctorLocationByDoctorIdService(sysDoctorId);
		HashMap<String, Object> response = new HashMap<String, Object>();

		// 记录日志
		LogUtils.saveLog(Servlets.getRequest(), "00000042","搜索医生的就诊地址信息:" + sysDoctorId);

		response.put("location", locationList);
		return response;
	}

	@Override
	public void updateWaiteTime(HashMap<String, Object> updateMap) {
		doctorLocationDao.updateWaiteTime(updateMap);
	}
	
}
