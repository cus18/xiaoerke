package com.cxqm.xiaoerke.modules.sys.service.impl;


import com.cxqm.xiaoerke.modules.sys.dao.DoctorCaseDao;
import com.cxqm.xiaoerke.modules.sys.dao.DoctorDao;
import com.cxqm.xiaoerke.modules.sys.dao.DoctorHospitalRelationDao;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorCaseVo;
import com.cxqm.xiaoerke.modules.sys.service.DoctorCaseService;
import com.cxqm.xiaoerke.modules.sys.service.HospitalInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@Transactional(readOnly = false)
public class DoctorCaseServiceImpl implements DoctorCaseService {

	@Autowired
	private HospitalInfoService hospitalInfoService;

	@Autowired
	private DoctorDao doctorDao;

	@Autowired
	private DoctorHospitalRelationDao doctorHospitalRelationDao;
	
	@Autowired
	private DoctorCaseDao doctorCaseDao;
	
	//获取医生案例信息
	@Override
    public List<DoctorCaseVo> findDoctorCase(String doctorId) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("doctorId", doctorId);
        hashMap.put("display_status",'0');
        List<DoctorCaseVo> relationInfo = doctorCaseDao.findDoctorCase(hashMap);
        return relationInfo;
    }

    //获取医生案例总数
	@Override
    public int findDoctorCaseNumber(String doctorId) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("doctorId", doctorId);
        Integer sum = doctorCaseDao.findDoctorCaseNumber(hashMap);
        return sum;
    }

	//获取医生案例总数
	@Override
	public Integer findDoctorCaseNumberByName(String doctorId,String caseName) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("doctorId", doctorId);
		hashMap.put("caseName", caseName);
		Integer sum = doctorCaseDao.findDoctorCaseNumber(hashMap);
		return sum;
	}

	@Override
	public void updateDoctorCaseInfo(HashMap<String, Object> executeMap) {
		doctorCaseDao.updateDoctorCaseInfo(executeMap);
	}

	@Override
	public void saveDoctorCaseInfo(HashMap<String, Object> executeMap) {
		doctorCaseDao.saveDoctorCaseInfo(executeMap);
	}

	@Override
	public void saveDoctorCase(DoctorCaseVo doctorCaseVo){doctorCaseDao.saveDoctorCase(doctorCaseVo);};

}
