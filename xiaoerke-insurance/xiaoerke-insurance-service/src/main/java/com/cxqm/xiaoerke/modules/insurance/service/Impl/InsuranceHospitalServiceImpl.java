package com.cxqm.xiaoerke.modules.insurance.service.Impl;

import java.util.List;

import com.cxqm.xiaoerke.common.utils.StringUtils;
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
		int count = 0;
		if(StringUtils.isNotNull(vo.getId()+"")){
			InsuranceHospitalVo param = new InsuranceHospitalVo();
			param.setId(vo.getId());
			List<InsuranceHospitalVo> list = getInsuranceHospitalListByInfo(param);
			if(list.size()==0){
				count = insuranceHospitalDao.saveInsuranceHospital(vo);
			}else{
				count = insuranceHospitalDao.updateInsuranceHospital(vo);
			}
		}else{
			count = insuranceHospitalDao.saveInsuranceHospital(vo);
		}
		return count;
	}

	@Override
	public Page<InsuranceHospitalVo> findInsuranceHospitalListByInfo(
			Page<InsuranceHospitalVo> page, InsuranceHospitalVo vo) {
		// TODO Auto-generated method stub
		return insuranceHospitalDao.findInsuranceHospitalListByInfo(page, vo);
	}

	@Override
	public int delInsuranceHospital(Integer id) {
		return insuranceHospitalDao.delInsuranceHospital(id);
	}
}
