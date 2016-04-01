package com.cxqm.xiaoerke.modules.operation.dao;

import java.util.HashMap;
import java.util.List;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.operation.entity.StatisticsTitle;

@MyBatisDao
public interface StatisticsTitleDao {
/**
 * 插入统计数据
 * @param st
 * @return
 */
	int insertStatisticsTitle(StatisticsTitle list);
	/**
	 * 按照日期查询
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	 List<HashMap<String,Object>> findStatisticsTitleList(HashMap paramMap);
	/**
	 * 统计数据中订单部分
	 * @return
	 */
	List<HashMap<String,Object>> selectIntoOrder(HashMap paramMap);
	/**
	 * 订单累计有效部分
	 * @return
	 */
	List<HashMap<String,Object>> selectIntoDayToDaytotalOrder(String createdate);
	/**
	 * 咨询累计有效部分
	 * @return
	 */
	List<HashMap<String,Object>> 	selectIntoDayToDaytotalRecord(HashMap<String,String> m);
	/**
	 * 郑玉巧说：新增、新增人次、转发人次
	 * @return
	 */
	List<HashMap<String,Object>> 	selectIntoZYQSayStatistics(HashMap paramMap);
	/**
	 * 郑玉巧说：累计人数、累计人次
	 * @return
	 */
	List<HashMap<String,Object>> 	selectIntoZYQSayTotal(String createdate);
	/**
	 * 用户数和医生数：新增、净增
	 * @return
	 */
	List<HashMap<String,Object>> 	selectIntoUserAndDoctorNumsStatistics(HashMap paramMap);
	/**
	 * 用户数和医生数：累计
	 * @return
	 */
	List<HashMap<String,Object>> 	selectIntoUserAndDoctorTotalStatistics(String createdate);
	/**
	 * 查询今天所有查询（openid去重）
	 * @param createdate
	 * @return
	 */
	List<HashMap<String,Object>> 	selectRecord(String createdate);
	
	/**
	 * 查询前一天的累計有效查詢
	 * @param createdate
	 * @return
	 */
	List<HashMap<String,Object>>  selectLastDayTotalRecord(String createdate);
	
	/**
	 * 查询实时Token
	 * @return
	 */
	String findWeChatToken();
	
	/**
	 * 咨询累计有效部分十一月份以后使用
	 * @return
	 */
	List<HashMap<String,Object>> 	selectIntoDayToDaytotalRecordAfterNov(HashMap<String,String> m);
	
	/**
	 * 累计已就诊
	 * @return
	 */
	List<HashMap<String,Object>> 	selectLastDayTotalOrder(String createdate);
	
	/**
	 * 累计阅读
	 * @return
	 */
	List<HashMap<String,Object>> 	selectLastDayTotalReadNums(String createdate);
	
	/**
	 * 真实订单和付费订单
	 * @return
	 */
	List<HashMap<String,Object>> 	getTruthOrderAndPayOrder(String createdate);
	
	/**
	 * 知识库注册人数统计
	 * @param createdate
	 * @return
	 */
	List<HashMap<String,Object>> 	getPatienBabyNum(String createdate);
	
	/**
	 * 知识库注册人数统计
	 * @param createdate
	 * @return
	 */
	List<HashMap<String,Object>> 	getPatienBabyNumLastday(String createdate);


	/**
	 * 宝大夫登录人数
	 * @param paramMap
	 * @return
	 */
	int getVisiteUserNum(HashMap paramMap);
	
	/**
	 * 每日会员新增
	 * @return
	 */
	int getAddVipNum(HashMap paramMap);
	
	/**
	 * 每日会员退款
	 * @return
	 */
	int getDiffVipNum(HashMap paramMap);
	
	
	/**
	 * 每日会员总数
	 * @return
	 */
	int getTotalVipNum(HashMap paramMap);
}
