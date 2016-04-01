package com.cxqm.xiaoerke.modules.insurance.dao;

import java.util.List;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.insurance.entity.InsuranceHospitalVo;

/**
 * 保险关联的医院dao
 * @author sunxiao
 *
 */
@MyBatisDao
public interface InsuranceHospitalDao {
	
	/**
	 * sunxiao
	 * 根据条件查询保险相关医院
	 */
	List<InsuranceHospitalVo> getInsuranceHospitalListByInfo(InsuranceHospitalVo vo);
	
	/**
	 * sunxiao
	 * 保存保险相关医院
	 */
	int saveInsuranceHospital(InsuranceHospitalVo vo);
	
	/**
	 * sunxiao
	 * 分页查询保险相关医院
	 */
	Page<InsuranceHospitalVo> findInsuranceHospitalListByInfo(Page<InsuranceHospitalVo> page , InsuranceHospitalVo vo);
}
