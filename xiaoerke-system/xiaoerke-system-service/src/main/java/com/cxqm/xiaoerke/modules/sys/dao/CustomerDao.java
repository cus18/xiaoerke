package com.cxqm.xiaoerke.modules.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.sys.entity.CustomerBabyInfo;
import com.cxqm.xiaoerke.modules.sys.entity.CustomerLog;

/**
 *  咨询记录（多客服）
 * @author 张博
 *
 */
@MyBatisDao
public interface CustomerDao {

	/**
	 * 	保存咨询者宝宝信息
	 * @param cbi
	 * @return
	 */
	public Integer saveBabyInfo(CustomerBabyInfo cbi);

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
	public List<Map<String,Object>>  getBabyInfoListNew(@Param("openid")String openid);
	
	/**
	 * 通过咨询者OpenID查询咨询用户userid
	 * @param openid
	 * @return
	 */
	public String  getUserid(@Param("openid")String openid);

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
	 * 获取咨询历史添加疾病
	 * @return
	 */
	public List<Map<String,Object>> getIllnessList();

	/**
	 * 	更新咨询者宝宝信息
	 * @param cbi
	 * @return
	 */
	public Integer updateBabyInfo(CustomerBabyInfo cbi);

	/**
	 * 通过OpenID获得微信昵称
	 * @param openid
	 * @return
	 */
	public String getNickName(@Param("openid")String openid);
	/**
	 * 通过Openid得到宝宝就诊信息
	 * @param openid
	 * @return
	 */
	public List<Map<String,Object>> getOrderInfoByOpenid(@Param("openid")String openid,@Param("babyName")String babyName);
	/**
	 * 通过Openid获取咨询历史
	 * @param openid
	 * @return
	 */
	public List<Map<String,Object>> getCustomerLogByOpenID(@Param("openid")String openid);

	
	/**
	 * 客服推送文章
	 * @param openid
	 * @return
	 */
	public List<Map<String,Object>> onIllnessKeydown(@Param("illness")String illness);

	/**
	 * 添加openid咨询的关键字
	 * @return
	 */
	public Integer saveOpenidKeywords(@Param("openid")String openid,@Param("keywords")String keywords);
	
	/**
	 * 查询openid咨询的关键字
	 * @return
	 */
	public Integer getKeywordsByOpenID(@Param("openid")String openid,@Param("keywords")String keywords);
	
	/**
	 * 查询openid咨询的关键字
	 * @return
	 */
	public String getLastOrderDate(@Param("openid")String openid,@Param("babyname")String babyName);
	
	/**
	 * 查询会员结束日期
	 * @return
	 */
	public String getVIPEndDate(@Param("openid")String openid);
	
	/**
	 * 通过BabyOpenid获取咨询历史
	 * @param openid
	 * @return
	 */
	public List<Map<String,Object>> getCustomerLogByBabyOpenID(@Param("openid")String openid);


	/**
	 * 通过主见id得到宝宝就诊信息
	 * @param babyId
	 * @return
	 */
	public Map<String,Object> selectByPrimaryKey(@Param("babyId")Integer babyId);


	/**
	 * 通过主见id得到宝宝咨询信息
	 * @param babyId
	 * @return
	 */
	public Map<String,Object> getCustomerLogByBabyId(@Param("babyId")Integer babyId);
}
