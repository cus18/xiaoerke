package com.cxqm.xiaoerke.modules.insurance.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.insurance.dao.InsuranceHospitalDao;
import com.cxqm.xiaoerke.modules.insurance.entity.InsuranceHospitalVo;
import com.cxqm.xiaoerke.modules.insurance.service.InsuranceHospitalService;

/**
 * 保险关联的医院实现类
 * 
 * @author sunxiao
 * 
 */
@Service
@Transactional(readOnly = false)
public class InsuranceHospitalServiceImpl implements InsuranceHospitalService {
	
	@Autowired
    private InsuranceHospitalDao insuranceHospitalDao;
	
	/**
	 * sunxiao
	 * 根据条件查询保险相关医院
	 */
	@Override
	public List<InsuranceHospitalVo> getInsuranceHospitalListByInfo(
			InsuranceHospitalVo vo) {
		// TODO Auto-generated method stub
		return insuranceHospitalDao.getInsuranceHospitalListByInfo(vo);
	}

	@Override
	public int saveInsuranceHospital(InsuranceHospitalVo vo) {
		// TODO Auto-generated method stub
		return insuranceHospitalDao.saveInsuranceHospital(vo);
	}

	@Override
	public Page<InsuranceHospitalVo> findInsuranceHospitalListByInfo(
			Page<InsuranceHospitalVo> page, InsuranceHospitalVo vo) {
		// TODO Auto-generated method stub
		return insuranceHospitalDao.findInsuranceHospitalListByInfo(page, vo);
	}
}
