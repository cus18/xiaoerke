package com.cxqm.xiaoerke.modules.sys.service;

import java.util.HashMap;

import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.sys.entity.UnCertifiedDoctorInfo;


/**
 * 未审核医生
 * @author 张博
 * @version 2015年9月23日
 */
@Transactional(readOnly = false)
public interface UnCertifiedDoctorInfoService {

	//插入未审核医生信息
	int insertUnCertifiedDoctorInfo(UnCertifiedDoctorInfo record);
	
	 int updateByPrimaryKeySelective(UnCertifiedDoctorInfo record);
	
	//查询全部未审核
	Page<UnCertifiedDoctorInfo> findPageUnCertifiedDoctorInfo(Page<UnCertifiedDoctorInfo> page,UnCertifiedDoctorInfo doctorInfo);
}
