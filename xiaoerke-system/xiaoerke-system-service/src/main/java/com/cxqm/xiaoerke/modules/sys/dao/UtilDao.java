package com.cxqm.xiaoerke.modules.sys.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.sys.entity.ValidateBean;

@MyBatisDao
public interface UtilDao {
	
	/**
	 * 短信验证码
	 * @param num 用户电话号码
	 * @return 生成的随机验证码
	 * */
	int saveOrUpdateIdentify(ValidateBean num);
	

	/**
	 * 短信验证码
	 * @param phoneNum 用户电话号码
	 * @return 生成的随机验证码
	 * */
	ValidateBean getIdentifying(String phoneNum);

	int updateValidateCode(ValidateBean validateBean);

}
