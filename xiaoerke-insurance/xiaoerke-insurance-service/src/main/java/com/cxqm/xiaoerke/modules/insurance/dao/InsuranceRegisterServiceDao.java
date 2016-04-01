package com.cxqm.xiaoerke.modules.insurance.dao;

import java.util.List;
import java.util.Map;


import org.apache.ibatis.annotations.Param;


import com.cxqm.xiaoerke.common.persistence.Page;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.insurance.entity.InsuranceRegisterService;

@MyBatisDao
public interface InsuranceRegisterServiceDao {

	/**
	 * 添加保险
	 * @param insuranceRegisterService
	 * @return
	 */
	public Integer saveInsuranceRegisterService(InsuranceRegisterService insuranceRegisterService);
	
	/**
	 * 根据id查询一个保险信息
	 * @param id
	 * @return
	 */
	public InsuranceRegisterService getInsuranceRegisterServiceById(String id);
	
	/**
	 * 获取当前用户下的保险信息
	 * @param userid
	 * @return
	 */
	public List<Map<String,Object>> getInsuranceRegisterServiceListByUserid(String userid);
	

	/**
	 * 验证宝宝是否已经存在订单
	 * @param babyId
	 * @return
	 */
	public List<Map<String,Object>> getInsuranceRegisterServiceIfValid(@Param("babyId")String babyId);
	

	/**
	 * 查询列表
	 * @param page
	 * @param vo
	 * @return
	 */
	public Page<InsuranceRegisterService> findInsuranceServiceList(Page<InsuranceRegisterService> page ,InsuranceRegisterService vo);
	

	/**
	 *  验证此Openid是否查看过Lead页
	 * @param babyId
	 * @return
	 */
	public List<Map<String,Object>> getInsuranceRegisterServiceVisitLeadPageLogByOpenid(@Param("openid")String openid);
	
	/**
	 *  验证此Openid是否存在保险订单
	 * @param babyId
	 * @return
	 */
	public List<Map<String,Object>> getInsuranceRegisterServiceByOpenid(@Param("openid")String openid);
	
	
	/**
	 * 获取当前用户下的有效保险信息
	 * @param userid
	 * @return
	 */
	public List<Map<String,Object>> getValidInsuranceRegisterServiceListByUserid(String userid);
	
	/**
	 * 获取当前用户下的无效保险信息
	 * @param userid
	 * @return
	 */
	public List<Map<String,Object>> getInvalidInsuranceRegisterServiceListByUserid(String userid);
	
	/**
	 * 获取所有状态为0或1的保险订单
	 * @param userid
	 * @return
	 */
	public List<Map<String,Object>> getInsuranceRegisterServiceByState();
	
	/**
	 * 修改保险状态
	 * @param insuranceRegisterService
	 * @return
	 */
	public Integer updateInsuranceOrder();
	
	/**
	 * 修改过期保险状态
	 * @param insuranceRegisterService
	 * @return
	 */
	public Integer updateEndInsuranceOrder();
	
	/**
	 * 修改订单状态
	 * sunxiao
	 * @param insuranceRegisterService
	 */
	public void updateInsuranceRegisterService(InsuranceRegisterService insuranceRegisterService);
	
	/**
	 * 查询订单ID
	 * @param id
	 * @return
	 */
	public Map<String,Object> getPayRecordById(@Param("aprid")String id);
	
	/**
	 * 根据条件查询保险信息
	 * sunxiao
	 * @param insuranceRegisterService
	 */
	List<InsuranceRegisterService> getInsuranceRegisterServiceByInfo(InsuranceRegisterService insuranceRegisterService);
}
