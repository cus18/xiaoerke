package com.cxqm.xiaoerke.modules.wechat.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cxqm.xiaoerke.common.bean.CustomBean;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.wechat.dao.WechatInfoDao;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.modules.wechat.dao.UserQRCodeDao;
import com.cxqm.xiaoerke.modules.wechat.entity.UserQRCode;
import com.cxqm.xiaoerke.modules.wechat.service.WeChatInfoService;

@Service
@Transactional(readOnly = false)
public class WeChatServiceImpl implements WeChatInfoService {
	
	@Autowired
	private UserQRCodeDao ucd;
	
	@Autowired
	private WechatInfoDao wechatInfoDao;
	
	@Autowired
	private SystemService systemService;

	@Override
	public int insertUserQRCode(UserQRCode usercode) {
		// TODO Auto-generated method stub
		return ucd.insertUserQRCode(usercode);
	}

	@Override
	public Map<String,Object> getQRCodeFromOpenid(String openid) {
		// TODO Auto-generated method stub
		return ucd.getQRCodeFromOpenid(openid);
	}
	
	public  List<Map<String,Object>> getShareFromOpenid(Map<String,Object> m){
		List<String> openidList=ucd.getFollowerListByMarketer(m.get("openid").toString());
		m.put("openidList", openidList);
		return ucd.getShareFromOpenid(m);
	}

	@Override
	public List<Map<String, Object>> getShareFromOpenidAndAPITime(
			Map<String, Object> m) {
		// TODO Auto-generated method stub
		return ucd.getShareFromOpenidAndAPITime(m);
	}

	@Override
	public void getCustomerOnlineTime(ArrayList<CustomBean> list) {
		wechatInfoDao.getCustomerOnlineTime(list);
	}
	
	@Override
	public List<Map<String, Object>> findAttentions(String userId, String date) {
		return wechatInfoDao.selectAttentions(userId, date);
	}

}
