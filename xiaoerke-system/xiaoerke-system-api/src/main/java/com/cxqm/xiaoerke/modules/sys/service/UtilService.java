package com.cxqm.xiaoerke.modules.sys.service;


import com.cxqm.xiaoerke.modules.sys.entity.PatientVo;
import com.cxqm.xiaoerke.modules.sys.entity.ValidateBean;

import java.util.Map;

public interface UtilService {

	String bindUser4ConsultDoctor(String mobile, String verifyCode, String openId);

	PatientVo CreateUser(String num,String openid,String type);

	String bindUser4Doctor(String mobile, String verifyCode, String openId);

	int updateValidateCode(ValidateBean validateBean);

	Map<String, Object> sendIdentifying(String num);

	String bindUser(String num, String code, String openid);

    PatientVo bindUserForThirdParty(String num, String openid);

}
