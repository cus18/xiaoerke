package com.cxqm.xiaoerke.modules.insurance.service;




import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.insurance.entity.InsuranceRegisterService;

/**
 * 
 * @author Cnto
 *
 */
public interface InsuranceRegisterServiceService {

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
	 * 分页查询列表
	 * @param page
	 * @param vo
	 * @return
	 */
	public Page<InsuranceRegisterService> findInsuranceServiceList(Page<InsuranceRegisterService> page ,InsuranceRegisterService vo);
	
	/**
	 * 查询所有订单
	 * sunxiao
	 * @param vo
	 * @return
	 */
	List<InsuranceRegisterService> getInsuranceServiceList(InsuranceRegisterService vo);
	
	public List<Map<String,Object>> getInsuranceRegisterServiceIfValid(Map<String, Object> dataMap);
	
	/**
	 *  验证此Openid是否查看过Lead页
	 * @param babyId
	 * @return
	 */
	public List<Map<String,Object>> getInsuranceRegisterServiceVisitLeadPageLogByOpenid(String openid);
	
	/**
	 *  验证此Openid是否存在保险订单
	 * @param babyId
	 * @return
	 */
	public List<Map<String,Object>> getInsuranceRegisterServiceByOpenid(String openid);
	
	/**
	 * 获取当前用户下的有效保险信息
	 * @param userid
	 * @return
	 */
	public List<Map<String,Object>> getValidInsuranceRegisterServiceListByUserid(Map<String, Object> dataMap);
	
	/**
	 * 获取当前用户下的无效保险信息
	 * @param userid
	 * @return
	 */
	public List<Map<String,Object>> getInvalidInsuranceRegisterServiceListByUserid(Map<String, Object> dataMap);
	
	/**
	 * 修改所有状态为0或1的保险订单
	 * @param userid
	 * @return
	 */
	public void updateInsuranceRegisterServiceByState();
	
	/**
	 * 修改订单状态
	 * sunxiao
	 */
	public void updateInsuranceRegisterService(InsuranceRegisterService vo);
	
	
	/**
	 * 查询订单ID
	 * @param id
	 * @return
	 */
	public Map<String,Object> getPayRecordById(@Param("aprid")String id);
	
	/**
	 * 运维后台赠送保险
	 * sunxiao
	 * @param insuranceRegisterService
	 */
	String giveInsuranceRegisterService(InsuranceRegisterService insuranceRegisterService);
	
}
