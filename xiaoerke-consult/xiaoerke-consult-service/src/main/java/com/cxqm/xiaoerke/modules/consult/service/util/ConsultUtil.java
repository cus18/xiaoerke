package com.cxqm.xiaoerke.modules.consult.service.util;

import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;

import java.util.Date;
import java.util.HashMap;

public class ConsultUtil {

	public static RichConsultSession transferMapToRichConsultSession(HashMap<String, Object> consultSessionMap){
		RichConsultSession consultSession = new RichConsultSession();
		consultSession.setUserName((String) consultSessionMap.get("userName"));
		consultSession.setUserId((String) consultSessionMap.get("userId"));
		consultSession.setServerAddress((String) consultSessionMap.get("serverAddress"));
		consultSession.setCreateTime((Date) consultSessionMap.get("createTime"));
		consultSession.setCsUserName((String) consultSessionMap.get("csUserName"));
		consultSession.setOpenid((String) consultSessionMap.get("openId"));
		consultSession.setNickName((String) consultSessionMap.get("nickName"));
		consultSession.setCsUserId((String) consultSessionMap.get("csUserId"));
		consultSession.setStatus((String) consultSessionMap.get("status"));
		consultSession.setTitle((String) consultSessionMap.get("title"));
		consultSession.setId((Integer) consultSessionMap.get("id"));
		return consultSession;
	}

	public static HashMap<String,Object> transferRichConsultSessionToMap(RichConsultSession consultSession){
		HashMap<String,Object> consultSessionMap = new HashMap<String, Object>();
		consultSessionMap.put("userName",consultSession.getUserName());
		consultSessionMap.put("userId",consultSession.getUserId());
		consultSessionMap.put("serverAddress",consultSession.getServerAddress());
		consultSessionMap.put("createTime",consultSession.getCreateTime());
		consultSessionMap.put("csUserName",consultSession.getCsUserName());
		consultSessionMap.put("openId",consultSession.getOpenid());
		consultSessionMap.put("nickName",consultSession.getNickName());
		consultSessionMap.put("csUserId",consultSession.getCsUserId());
		consultSessionMap.put("status",consultSession.getStatus());
		consultSessionMap.put("title",consultSession.getTitle());
		consultSessionMap.put("id",consultSession.getId());
		return consultSessionMap;
	}
}
