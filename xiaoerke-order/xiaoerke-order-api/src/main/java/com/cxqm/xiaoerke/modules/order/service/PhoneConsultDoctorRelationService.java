package com.cxqm.xiaoerke.modules.order.service;

import java.util.List;
import java.util.Map;

import com.cxqm.xiaoerke.modules.order.entity.ConsulPhonetDoctorRelationVo;
import net.sf.json.JSONObject;


/**
 * Created by sunxiao on 16/3/23.
 */
public interface PhoneConsultDoctorRelationService {
	/**
	 * 开通电话咨询
	 * sunxiao
	 * @param vo
	 * @return
	 */
	JSONObject openConsultPhone(ConsulPhonetDoctorRelationVo vo);
	
	/**
	 * 根据条件查询医生电话咨询信息
	 * sunxiao
	 * @param vo
	 * @return
	 */
	List<ConsulPhonetDoctorRelationVo> getPhoneConsultDoctorRelationByInfo(ConsulPhonetDoctorRelationVo vo);


	/**
	 * 根据医生主见查询关联的信息
	 * 王报伟
	 * @param doctorId
	 * @return vo
	 */
	ConsulPhonetDoctorRelationVo getPhoneConsultRigister(String doctorId);

//	获取电话咨询医生的基本信息
	Map<String, Object> findDoctorDetailInfo(String doctorId);

}
