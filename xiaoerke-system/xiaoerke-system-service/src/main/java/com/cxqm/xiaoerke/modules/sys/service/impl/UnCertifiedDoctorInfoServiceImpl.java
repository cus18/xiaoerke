package com.cxqm.xiaoerke.modules.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.sys.dao.UnCertifiedDoctorInfoDao;
import com.cxqm.xiaoerke.modules.sys.entity.UnCertifiedDoctorInfo;
import com.cxqm.xiaoerke.modules.sys.service.UnCertifiedDoctorInfoService;

@Service
@Transactional(readOnly = false)
public class UnCertifiedDoctorInfoServiceImpl implements
		UnCertifiedDoctorInfoService {

	@Autowired
	private UnCertifiedDoctorInfoDao uncertifileDoctorInfoDao;
	
	public int insertUnCertifiedDoctorInfo(UnCertifiedDoctorInfo record) {
		return uncertifileDoctorInfoDao.insert(record);
	}


	@Override
	public Page<UnCertifiedDoctorInfo> findPageUnCertifiedDoctorInfo(
			Page<UnCertifiedDoctorInfo> page,UnCertifiedDoctorInfo doctorInfo) {
		// TODO Auto-generated method stub
		doctorInfo.setPage(page);
		page.setList(uncertifileDoctorInfoDao.findPageUnCertifiedDoctorInfo(doctorInfo));
		return page;
	}


	@Override
	public int updateByPrimaryKeySelective(UnCertifiedDoctorInfo record) {
		// TODO Auto-generated method stub
		return uncertifileDoctorInfoDao.updateByPrimaryKeySelective(record);
	}

}
