package com.cxqm.xiaoerke.modules.sys.service.impl;


import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.sys.dao.DoctorDao;
import com.cxqm.xiaoerke.modules.sys.dao.DoctorHospitalRelationDao;
import com.cxqm.xiaoerke.modules.sys.dao.DoctorIllnessRelationDao;
import com.cxqm.xiaoerke.modules.sys.dao.IllnessDao;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorHospitalRelationVo;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;
import com.cxqm.xiaoerke.modules.sys.entity.IllnessVo;
import com.cxqm.xiaoerke.modules.sys.service.DoctorInfoService;
import com.cxqm.xiaoerke.modules.sys.service.HospitalInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = false)
public class DoctorInfoServiceImpl implements DoctorInfoService {

	@Autowired
	private HospitalInfoService hospitalInfoService;

	@Autowired
	private DoctorDao doctorDao;

	@Autowired
	private DoctorHospitalRelationDao doctorHospitalRelationDao;

	@Autowired
	private IllnessDao illnessDao;
	
	@Autowired
	private DoctorIllnessRelationDao doctorIllnessRelationDao;
	


    //根据医院的信息，获取医院里面的医生信息列表
	@Override
    public Page<HashMap<String, Object>> findDoctorByHospital(HashMap<String, Object> doctorMap, Page<HashMap<String, Object>> page) {
        Page<HashMap<String, Object>> pageVo = doctorHospitalRelationDao.findPageAllDoctorByHospital(doctorMap, page);
        return pageVo;
    }
    @Override
    public Map getDepartmentName(HashMap<String, Object> Map){
        return doctorHospitalRelationDao.getDepartmentName(Map);
    }

    @Override
    public List<DoctorHospitalRelationVo> getDoctorHospitalRelationVo(HashMap<String, Object> Map){
        return doctorHospitalRelationDao.getDoctorHospitalRelationVo(Map);
    }

    @Override
    public String getDoctorNameByDoctorId(String doctorId) {
        String doctorName = null;
        HashMap<String, Object> doctor = new HashMap<String, Object>();
        doctor.put("doctorId", doctorId);
        List<HashMap<String, Object>> dr = doctorDao.findDoctorByDoctorId(doctor);
        for (HashMap<String, Object> doctorlist:dr) {
            doctorName = (String) doctorlist.get("doctorName");
        }
        return doctorName == null ? "" : doctorName;
    }

    //获取一个医生的从业经历
	@Override
    public String getDoctorCardExpertiseById(String doctorId) {
        HashMap<String, Object> searchMap = new HashMap<String, Object>();
        searchMap.put("doctorId", doctorId);
        HashMap<String, Object> expertiseInfo = doctorIllnessRelationDao.getDoctorCardExpertise(searchMap);
        String expertise = (String) expertiseInfo.get("illExpertise");
        return expertise;
    }

    //获取一个医院的专业擅长领域
    @Override
    public String getDoctorExpertiseById(String doctorId, String hospitalId, String departmentLevel1) {
        //获取医生所在的科室
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("doctorId", doctorId);
        hashMap.put("hospitalId", hospitalId);
        if( departmentLevel1 == null ) {
        	Map relationInfo = doctorHospitalRelationDao.getDepartmentName(hashMap);
            departmentLevel1 = relationInfo == null ? "" : (String) relationInfo.get("department_level1");;
        }
        //获取医生所有的一类疾病
        List<IllnessVo> iVoList = illnessDao.findSysIllness_1BySysDoctorId(hashMap);
        //医生擅长 = 医生所在科室 + 医生所擅长的一类疾病
        StringBuffer expertise = new StringBuffer();
        expertise.append(departmentLevel1);
        for (IllnessVo vo : iVoList) {
            expertise.append("  " + vo.getLevel_1() );
        }
        return expertise.toString();
    }

    //获取一个医院的专业擅长领域
    @Override
    public Map<String,Object> getPhoneExpertiseById(String doctorId, String hospitalId, String departmentLevel1) {
        //获取医生所在的科室
        Map<String,Object> resultMap = new HashMap<String, Object>();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("doctorId", doctorId);
        hashMap.put("hospitalId", hospitalId);
        if( departmentLevel1 == null ) {
            Map relationInfo = doctorHospitalRelationDao.getDepartmentName(hashMap);
            departmentLevel1 = relationInfo == null ? "" : (String) relationInfo.get("department_level1");;
        }
        //获取医生所有的一类疾病
        List<IllnessVo> iVoList = illnessDao.findSysIllness_1BySysDoctorId(hashMap);
        //医生擅长 = 医生所在科室 + 医生所擅长的一类疾病
        StringBuffer expertise = new StringBuffer();
        expertise.append(departmentLevel1);
        for (IllnessVo vo : iVoList) {
            expertise.append("  " + vo.getLevel_1() );
        }
        resultMap.put("expertise",expertise.toString());
        resultMap.put("iVoList",iVoList);
        return resultMap;
    }
    
    //查询系统内部所有医生
    @Override
    public Page<HashMap<String, Object>> findPageAllDoctor(Page<HashMap<String, Object>> page) {

        return doctorDao.findPageAllDoctor(page);
    }

    //查询一个医院的科室下面的所有医生
    @Override
    public Page<HashMap<String, Object>> findPageDoctorByDepartment(HashMap<String, Object> hospitalInfo, Page<HashMap<String, Object>> page) {
        return doctorDao.findPageDoctorByDepartment(hospitalInfo, page);
    }

    //查询一个医院的科室下面的所有医生
    @Override
    public Page<HashMap<String, Object>> findPageConsultaDoctorByDepartment(HashMap<String, Object> hospitalInfo, Page<HashMap<String, Object>> page) {
        return doctorDao.findPageConsultaDoctorByDepartment(hospitalInfo, page);
    }

    @Override
    public String findOpenIdByDoctorId(String doctorId) {
        HashMap<String,Object> map = doctorDao.findOpenIdByDoctorId(doctorId);
        String openId = map==null?"": (String) map.get("openid");
        return openId == null ? "" : openId;
    }

    //获取系统内的推荐医生
    @Override
    public Page<HashMap<String, Object>> findPageDoctorByRecommend(HashMap<String, Object> recommendInfo, Page<HashMap<String, Object>> page) {
        return doctorDao.findPageDoctorByRecommend(recommendInfo, page);
    }
    
    @Override
    public Page<HashMap<String, Object>> findPageDoctorByIllnessSecond(HashMap<String, Object> secondIllnessInfo, Page<HashMap<String, Object>> page) {

        return doctorDao.findPageDoctorByIllnessSecond(secondIllnessInfo, page);
    }

    @Override
    public Page<HashMap<String, Object>> findPageDoctorByIllnessSecondAndHospital(HashMap<String, Object> secondIllnessInfo, Page<HashMap<String, Object>> page) {

        return doctorDao.findPageDoctorByIllnessSecondAndHospital(secondIllnessInfo, page);
    }
    
    //获取某个预约日期下的可预约医生
    @Override
    public Page<HashMap<String, Object>> findPageDoctorByTime(HashMap<String, Object> dateInfo, Page<HashMap<String, Object>> page) {
        return doctorDao.findPageDoctorByTime(dateInfo, page);
    }

    @Override
    public Page<HashMap<String, Object>> findPageDoctorByTime4Consult(HashMap<String, Object> dateInfo, Page<HashMap<String, Object>> page) {
        return doctorDao.findPageDoctorByTime4Consult(dateInfo, page);
    }

    //获取某个医院的某个预约日期下的可预约医生
    @Override
    public Page<HashMap<String, Object>> findPageDoctorByTimeAndHospital(HashMap<String, Object> dateInfo, Page<HashMap<String, Object>> page) {

        return doctorDao.findPageDoctorByTimeAndHospital(dateInfo, page);
    }
    
    //根据医院的信息，获取医院里面的医生信息列表
    @Override
    public Page<HashMap<String, Object>> findPageAllDoctorByDoctorIds(String[] doctorIds, String hospitalId, String orderBy, Page<HashMap<String, Object>> page) {

        Page<HashMap<String, Object>> pageVo = doctorHospitalRelationDao.findPageAllDoctorByDoctorIds(doctorIds, hospitalId, orderBy, page);

        return pageVo;
    }
    
    //根据用户id获取当前用户的所有信息
    @Override
    public HashMap<String, Object> findDoctorDetailInfoByUserId(String userId) {
        HashMap<String,Object> hashMap=new HashMap<String, Object>();
        hashMap.put("userId",userId);
        HashMap<String, Object> resultmap = doctorDao.findDoctorDetailInfoByUserIdExecute(hashMap);
        return resultmap;
    }

	@Override
    public Page<HashMap<String, Object>> listSecondIllnessDoctor(HashMap<String, Object> secondIllnessInfo, Page<HashMap<String, Object>> page)
	{
		Page<HashMap<String, Object>> resultPage = doctorDao.findPageDoctorByIllnessSecond(secondIllnessInfo, page);
		return resultPage;
	}

    @Override
    public Page<HashMap<String, Object>> listSecondIllnessDoctor4Consult(HashMap<String, Object> secondIllnessInfo, Page<HashMap<String, Object>> page)
    {
        Page<HashMap<String, Object>> resultPage = doctorDao.findPageDoctorByIllnessSecond4Consult(secondIllnessInfo, page);
        return resultPage;
    }

	@Override
	public Map<String, Object> findDoctorDetailInfo(Map map) {
		return doctorDao.findDoctorDetailInfo(map);
	}

	@Override
	public Map<String, Object> findDoctorDetailInfoAndType(Map map) {
		return doctorDao.findDoctorDetailInfoAndType(map);
	}

	@Override
	public HashMap<String, Object> findDoctorScoreInfo(String sys_doctor_id) {
		return doctorDao.findDoctorScoreInfo(sys_doctor_id);
	}

	@Override
	public List<HashMap<String, Object>> findAppointmentInfoDetail(
			HashMap<String, Object> appointmentInfo) {
		return doctorDao.findAppointmentInfoDetail(appointmentInfo);
	}

	@Override
	public List<HashMap<String, Object>> getDoctorHospitalInfo(String doctorId) {
		return doctorDao.getDoctorHospitalInfo(doctorId);
	}

	@Override
	public List<HashMap<String, Object>> getDoctorAppointmentTime(
			HashMap<String, Object> map) {
		return doctorDao.getDoctorAppointmentTime(map);
	}

	@Override
	public DoctorVo findDoctorByRegisterId(String registerId) {
		return doctorDao.findDoctorByRegisterId(registerId);
	}

	@Override
	public void updateDoctorFansExecute(HashMap<String, Object> hashMap) {
		doctorDao.updateDoctorFansExecute(hashMap);
	}

    @Override
    public List<DoctorVo> findAllOrderDoctorList(HashMap<String, Object> seachMapd) {
        return doctorDao.findAllOrderDoctorList(seachMapd);
    }

    @Override
    public int findCountDoctorNumber(HashMap<String, Object> hashMap) {
        return doctorDao.findCountDoctorNumber(hashMap);
    }

    @Override
    public int findCountDoctorCountNmuber() {
        return doctorDao.findCountDoctorCountNmuber();
    }

    @Override
    public List<HashMap<String, Object>> findDoctorByDoctorId(HashMap<String, Object> hashMap) {
        return doctorDao.findDoctorByDoctorId(hashMap);
    }

    @Override
    public int findDoctorCanAppointNumber(HashMap<String, Object> hashMap) {
        return doctorDao.findDoctorCanAppointNumber(hashMap);
    }

    @Override
    public int findDoctorAlreadyAppointNumber(HashMap<String, Object> hashMap) {
        return doctorDao.findDoctorAlreadyAppointNumber(hashMap);
    }

    @Override
    public List<HashMap> findDoctorByInfo(HashMap<String, Object> hashMap){
        return doctorDao.findDoctorByInfo(hashMap);
    };

    @Override
    public void insertDoctor(HashMap<String,Object> hashMap){
        doctorDao.insertDoctor(hashMap);
    };

    @Override
    public Map getDoctorIdByUserIdExecute(Map sysUserId){
        return doctorDao.getDoctorIdByUserId(sysUserId);
    };

    @Override
    public void update(DoctorVo doctorVo){
        doctorDao.update(doctorVo);
    };
}
