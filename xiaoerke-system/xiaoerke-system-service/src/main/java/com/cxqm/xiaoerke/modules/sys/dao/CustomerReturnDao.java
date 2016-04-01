package com.cxqm.xiaoerke.modules.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.sys.entity.CustomerReturn;

@MyBatisDao
public interface CustomerReturnDao {

	/**
	 * 添加回访记录
	 * @param customerReturn
	 * @return
	 */
	public Integer saveCustomerReturn(CustomerReturn customerReturn);
	
	/**
	 *  查询需要回访的记录
	 * @param starDate
	 * @param endDate
	 * @return
	 */
	public List<Map<String,Object>> getCustomerReturn(@Param("startDate")String startDate,@Param("endDate")String endDate);
	
}
