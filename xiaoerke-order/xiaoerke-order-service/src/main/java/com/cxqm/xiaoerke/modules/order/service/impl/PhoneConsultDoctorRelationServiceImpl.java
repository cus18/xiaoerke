package com.cxqm.xiaoerke.modules.order.service.impl;

import java.util.List;
import java.util.Map;

import com.cxqm.xiaoerke.modules.order.dao.PhoneConsultDoctorRelationDao;
import com.cxqm.xiaoerke.modules.order.entity.ConsulPhonetDoctorRelationVo;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cxqm.xiaoerke.modules.order.service.PhoneConsultDoctorRelationService;

/**
 * Created by sunxiao on 16/3/23.
 */

@Service
public class PhoneConsultDoctorRelationServiceImpl implements PhoneConsultDoctorRelationService {

    @Autowired
    private PhoneConsultDoctorRelationDao phoneConsultDoctorRelationDao;

    /**
     * 开通电话咨询
     * sunxiao
     */
	@Override
	public JSONObject openConsultPhone(ConsulPhonetDoctorRelationVo vo) {
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


}
