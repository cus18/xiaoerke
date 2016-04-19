package com.cxqm.xiaoerke.modules.order.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cxqm.xiaoerke.modules.order.dao.PhoneConsultDoctorRelationDao;
import com.cxqm.xiaoerke.modules.order.entity.ConsulPhonetDoctorRelationVo;
import com.cxqm.xiaoerke.modules.order.service.PhoneConsultDoctorRelationService;
import com.cxqm.xiaoerke.modules.sys.dao.DoctorDao;
import com.cxqm.xiaoerke.modules.sys.dao.HospitalDao;
import com.cxqm.xiaoerke.modules.sys.service.DoctorGroupInfoService;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by sunxiao on 16/3/23.
 */

@Service
public class PhoneConsultDoctorRelationServiceImpl implements PhoneConsultDoctorRelationService {

    @Autowired
    private PhoneConsultDoctorRelationDao phoneConsultDoctorRelationDao;

    @Autowired
    HospitalDao hospitalDao;
    
    @Autowired
    DoctorDao doctorDao;
    
    @Autowired
    private DoctorGroupInfoService doctorGroupInfoService;
    
    /**
     * 开通电话咨询
     * sunxiao
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public JSONObject openConsultPhone(ConsulPhonetDoctorRelationVo vo) throws Exception{
		// TODO Auto-generated method stub
		JSONObject result = new JSONObject();
		ConsulPhonetDoctorRelationVo param = new ConsulPhonetDoctorRelationVo();
		param.setDoctorId(vo.getDoctorId());
		List<ConsulPhonetDoctorRelationVo> relist = phoneConsultDoctorRelationDao.getConsultPhoneDoctorRelationByInfo(param);
		if(relist.size()==0){
			phoneConsultDoctorRelationDao.insert(vo);
		}else{
			ConsulPhonetDoctorRelationVo uparam = new ConsulPhonetDoctorRelationVo();
			uparam.setPrice(vo.getPrice());
			uparam.setServerLength(vo.getServerLength());
			uparam.setDoctorAnswerPhone(vo.getDoctorAnswerPhone());
			uparam.setId(relist.get(0).getId());
			phoneConsultDoctorRelationDao.updateByPrimaryKeySelective(uparam);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isConsultPhone", "1");
		map.put("id", vo.getDoctorId());
		doctorDao.updateDoctor(map);//更新doctor表
		List<Map<String, Object>> list = doctorDao.getDoctorInfo(map);
		if(list.size()!=0){
			if(list.get(0).get("sys_doctor_group_id")!=null){
				map.put("id", list.get(0).get("sys_doctor_group_id"));
				doctorGroupInfoService.updateSysDoctorGroup(map);//更新doctorgroup表
			}
			if(list.get(0).get("sys_hospital_id")!=null){
				map.put("id", list.get(0).get("sys_hospital_id"));//更新hospital表
				hospitalDao.updateSysHospital(map);
			}
		}
		return result;
	}

	@Override
	public List<ConsulPhonetDoctorRelationVo> getPhoneConsultDoctorRelationByInfo(
			ConsulPhonetDoctorRelationVo vo) {
		// TODO Auto-generated method stub
		return phoneConsultDoctorRelationDao.getConsultPhoneDoctorRelationByInfo(vo);
	}

	@Override
	public ConsulPhonetDoctorRelationVo getPhoneConsultRigister(String doctorId) {
		return phoneConsultDoctorRelationDao.selectByDoctorId(doctorId);
	}

	@Override
	public Map<String, Object> findDoctorDetailInfo(String doctorId) {

	  return	phoneConsultDoctorRelationDao.findDoctorDetailInfo(doctorId);
	}


}
