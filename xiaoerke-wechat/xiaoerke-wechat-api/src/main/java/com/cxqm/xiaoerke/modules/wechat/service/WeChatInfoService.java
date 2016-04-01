package com.cxqm.xiaoerke.modules.wechat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cxqm.xiaoerke.common.bean.CustomBean;

import org.apache.ibatis.annotations.Param;

import com.cxqm.xiaoerke.modules.wechat.entity.UserQRCode;

public interface WeChatInfoService {

	
	/**
	 * 根据用户OpenID添加推广码
	 * @param usercode
	 * @return
	 */
	int	 insertUserQRCode(UserQRCode usercode);
		
		/**
		 * 根据OpenID获取QRCode
		 * @param openid
		 * @return
		 */
	Map<String,Object> getQRCodeFromOpenid(@Param("openid")String openid);
	
	List<Map<String,Object>> getShareFromOpenid(Map<String,Object> m); 
	
	
	List<Map<String,Object>> getShareFromOpenidAndAPITime(Map<String,Object> m);

	void getCustomerOnlineTime(ArrayList<CustomBean> list);

	List<Map<String, Object>> findAttentions(String userId, String date);

}
