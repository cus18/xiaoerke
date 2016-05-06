package com.cxqm.xiaoerke.modules.bankend.service;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.sys.entity.SysHospitalContactVo;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.modules.sys.entity.HospitalVo;

/**
 * 医院相关
 * @author 得良
 * @version 2015-07-29
 */
@Transactional(readOnly = false)
public interface HospitalService {
	/**
	 * 插入医院信息数据
	 * @param hospitalVo
	 */
	void insertHospitalData(HospitalVo hospitalVo);

	/**
	 * 医院信息数据
	 * @param id
	 */
	HospitalVo getHospital(String id);

	Page<HospitalVo> findAllHospital(Page<HospitalVo> page, HospitalVo hospitalVo);

	void saveEditHospital(HospitalVo hospitalVo,SysHospitalContactVo contactVo);

	void deleteHospitalByHospitalId(String hopitalId);
}
