package com.cxqm.xiaoerke.modules.sys.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;
import com.cxqm.xiaoerke.modules.sys.entity.CustomerBabyInfo;
import com.cxqm.xiaoerke.modules.sys.entity.CustomerLog;
import com.cxqm.xiaoerke.modules.sys.entity.CustomerReturn;

import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = false)
public interface CustomerService {

	/**
	 * 	保存咨询者宝宝信息
	 * @param cbi
	 * @return
	 */
	public Integer saveBabyInfo(BabyBaseInfoVo babyBaseInfoVo);

	/**
	 * 通过咨询者OpenID查询咨询登记的宝宝记录
	 * @param openid
	 * @return
	 */
	public List<Map<String,Object>>  getBabyInfoList(@Param("openid")String openid);
	
	/**
	 * 通过咨询者OpenID查询咨询登记的宝宝记录
	 * @param openid
	 * @return
	 */
	public List<Map<String,Object>>  getBabyInfoListNew(String openid);

	/**
	 * 存储历史咨询详情
	 * @param cl
	 * @return
	 */
	public Integer saveCustomerLog(CustomerLog cl);

	/**
	 * 添加疾病
	 * @return
	 */
	public Integer saveIllness(@Param("illness")String illness);

	/**
	 * 获取咨询历史
	 * @return
	 */
	public List<Map<String,Object>> getIllnessList();

	/**
	 * 	更新咨询者宝宝信息
	 * @param cbi
	 * @return
	 */
	public Integer updateBabyInfo(BabyBaseInfoVo babyBaseInfoVo);


	/**
	 * 通过OpenID获得微信昵称
	 * @param openid
	 * @return
	 */
	public String getNickName(String openid);

	/**
	 * 通过Openid获得近期订单信息
	 * @param openid
	 * @return
	 */
	public List<Map<String,Object>> getOrderInfoByOpenid(String openid,String babyName);

	/**
	 * 通过Openid获取咨询历史
	 * @param openid
	 * @return
	 */
	public List<Map<String,Object>> getCustomerLogByOpenID(String openid);



	/**
	 * 客服推送文章
	 * @param openid
	 * @return
	 */
	public List<Map<String,Object>> onIllnessKeydown(String illness);
	
	/**
	 * 添加openid咨询的关键字
	 * @return
	 */
	public Integer saveOpenidKeywords(String openid,String keywords);
	
	/**
	 * 查询openid咨询的关键字
	 * @return
	 */
	public Integer getKeywordsByOpenID(String openid,String keywords);
	
	/**
	 * 查询openid咨询的关键字
	 * @return
	 */
	public String getLastOrderDate(String openid,String babyName);
	
	/**
	 * 查询会员结束日期
	 * @return
	 */
	public String getVIPEndDate(String openid);
	
	/**
	 * 通过BabyOpenid获取咨询历史
	 * @param openid
	 * @return
	 */
	public List<Map<String,Object>> getCustomerLogByBabyOpenID(String openid);


	/**
	 * 通过BabyId主见获取咨询历史
	 * @param babyId
	 * @return
	 */
	Map<String,Object> selectByPrimaryKey(Integer babyId);


	/**
	 * 通过主见id得到宝宝咨询信息
	 * @param babyId
	 * @return
	 */
	public Map<String,Object> getCustomerLogByBabyId(@Param("babyId")Integer babyId);
	
	/**
	 * 添加回访记录
	 * @param customerReturn
	 * @return
	 */
	public  Integer saveCustomerReturn(CustomerReturn customerReturn);
	
	/**
	 * 	获取
	 * @param starDate
	 * @param endDate
	 * @return
	 */
	public void SendCustomerReturn();

}
