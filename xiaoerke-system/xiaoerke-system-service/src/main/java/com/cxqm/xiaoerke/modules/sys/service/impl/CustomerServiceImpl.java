package com.cxqm.xiaoerke.modules.sys.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.common.bean.WechatArticle;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.sys.dao.BabyBaseInfoDao;
import com.cxqm.xiaoerke.modules.sys.dao.CustomerDao;
import com.cxqm.xiaoerke.modules.sys.dao.CustomerReturnDao;
import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;
import com.cxqm.xiaoerke.modules.sys.entity.CustomerBabyInfo;
import com.cxqm.xiaoerke.modules.sys.entity.CustomerLog;
import com.cxqm.xiaoerke.modules.sys.entity.CustomerReturn;
import com.cxqm.xiaoerke.modules.sys.service.CustomerService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.WechatMessageUtil;

@Service
@Transactional(readOnly = false)
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerDao cd;
	@Autowired
	BabyBaseInfoDao babyBaseInfoDao;
	@Autowired
	CustomerReturnDao customerReturnDao;
	@Autowired
	SystemService systemService;
	
	WechatMessageUtil wechatMessageUtil=new WechatMessageUtil();

	@Override
	public Integer saveBabyInfo(BabyBaseInfoVo babyBaseInfoVo) {
		// TODO Auto-generated method stub
//		return cd.saveBabyInfo(cbi);
		return babyBaseInfoDao.insertSelective(babyBaseInfoVo);
	}

	@Override
	public List<Map<String, Object>> getBabyInfoList(String openid) {
		// TODO Auto-generated method stub
		return cd.getBabyInfoList(openid);
	}

	@Override
	public List<Map<String, Object>> getBabyInfoListNew(String openid) {
		// TODO Auto-generated method stub
		String userid=cd.getUserid(openid);
		//没有手机号
		if(userid==""){
			return this.getBabyInfoList(openid);
		}
		//有手机号
		List<Map<String, Object>> list=cd.getBabyInfoListNew(userid);
		//有手机号但没宝宝
		if(list.size()==0){
			return this.getBabyInfoList(openid);
		}
		return list;
	}
	
	@Override
	public Integer saveCustomerLog(CustomerLog cl) {
		// TODO Auto-generated method stub
		return cd.saveCustomerLog(cl);
	}

	@Override
	public Integer saveIllness(String illness) {
		// TODO Auto-generated method stub
		return cd.saveIllness(illness);
	}

	@Override
	public List<Map<String, Object>> getIllnessList() {
		// TODO Auto-generated method stub
		return cd.getIllnessList();
	}

	@Override
	public Integer updateBabyInfo(BabyBaseInfoVo babyBaseInfoVo) {
		// TODO Auto-generated method stub
//		return cd.updateBabyInfo(cbi);
		return babyBaseInfoDao.updateByPrimaryKeySelective(babyBaseInfoVo);
	}

	@Override
	public String getNickName(String openid) {
		// TODO Auto-generated method stub
		return cd.getNickName(openid);
	}

	@Override
	public List<Map<String, Object>> getOrderInfoByOpenid(String openid,String babyName) {
		// TODO Auto-generated method stub
		return cd.getOrderInfoByOpenid(openid,babyName);
	}

	@Override
	public List<Map<String, Object>> getCustomerLogByOpenID(String openid) {
		// TODO Auto-generated method stub
		return cd.getCustomerLogByOpenID(openid);
	}

	@Override
	public List<Map<String, Object>> onIllnessKeydown(String illness) {
		// TODO Auto-generated method stub
		return cd.onIllnessKeydown(illness);
	}

	@Override
	public Integer saveOpenidKeywords(String openid, String keywords) {
		// TODO Auto-generated method stub
		return cd.saveOpenidKeywords(openid, keywords);
	}

	@Override
	public Integer getKeywordsByOpenID(String openid, String keywords) {
		// TODO Auto-generated method stub
		return cd.getKeywordsByOpenID(openid, keywords);
	}

	@Override
	public String getLastOrderDate(String openid, String babyName) {
		// TODO Auto-generated method stub
		return cd.getLastOrderDate(openid, babyName);
	}

	@Override
	public String getVIPEndDate(String openid) {
		// TODO Auto-generated method stub
		return cd.getVIPEndDate(openid);
	}

	@Override
	public List<Map<String, Object>> getCustomerLogByBabyOpenID(String openid) {
		// TODO Auto-generated method stub
		return cd.getCustomerLogByBabyOpenID(openid);
	}

	@Override
	public Map<String, Object> selectByPrimaryKey(Integer babyId) {
		return cd.selectByPrimaryKey(babyId);
	}

	/**
	 * 通过主见id得到宝宝咨询信息
	 * @param babyId
	 * @return
	 */
	public Map<String,Object> getCustomerLogByBabyId(@Param("babyId")Integer babyId){
		return cd.getCustomerLogByBabyId(babyId);
	}

	@Override
	public Integer saveCustomerReturn(CustomerReturn customerReturn) {
		// TODO Auto-generated method stub
		return customerReturnDao.saveCustomerReturn(customerReturn);
	}

	@Override
	public void SendCustomerReturn() {
		// TODO Auto-generated method stub
		Date now=new Date();
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
		Integer hour=now.getHours();
		String startDate="";
		String endDate="";
		if(hour==8){
			startDate=sdf.format(now)+"  00:00:00";
			endDate=sdf.format(now)+"  11:59:59";
		}
		if(hour==14){
			startDate=sdf.format(now)+"  12:00:00";
			endDate=sdf.format(now)+"  23:59:59";
		}
//		Map<String,Object> param=new HashMap<String, Object>();
//		param.put("satarDate", startDate);
//		param.put("endDate", endDate);
		List<Map<String,Object>> list=customerReturnDao.getCustomerReturn(startDate, endDate);
		for (Map<String, Object> map : list) {
			String st = "您好，通过昨天在宝大夫的咨询，宝宝的情况有没有好转一些呢？"
					+ "如果还需要帮助，医生24h在线，可马上回复您喔！";
			String openid=map.get("openID").toString();
		        if(StringUtils.isNotNull(openid))
		        { 
		        	Map parameter = systemService.getWechatParameter();
					String token = (String)parameter.get("token");
					WechatUtil.senMsgToWechat(token,openid, st);
		        }
		}
	}

}
