package com.cxqm.xiaoerke.modules.sys.service;


import java.util.Map;

import com.cxqm.xiaoerke.modules.sys.entity.PatientVo;

public interface UtilService {

	String bindUser4ConsultDoctor(String mobile, String verifyCode, String openId);

	PatientVo CreateUser(String num,String openid,String type);

	String bindUser4Doctor(String mobile, String verifyCode, String openId);

	Map<String, Object> sendIdentifying(String num);

	String bindUser(String num, String code, String openid);

}
