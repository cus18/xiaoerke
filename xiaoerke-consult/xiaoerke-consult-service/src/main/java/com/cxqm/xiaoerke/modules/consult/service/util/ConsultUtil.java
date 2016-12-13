package com.cxqm.xiaoerke.modules.consult.service.util;

import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;

import java.util.*;

public class ConsultUtil {

	public static RichConsultSession transferMapToRichConsultSession(Map<Object, Object> consultSessionMap){
		RichConsultSession consultSession = new RichConsultSession();
		consultSession.setUserName((String) consultSessionMap.get("userName"));
		consultSession.setUserId((String) consultSessionMap.get("userId"));
		consultSession.setServerAddress((String) consultSessionMap.get("serverAddress"));
		consultSession.setCreateTime((Date) consultSessionMap.get("createTime"));
		consultSession.setCsUserName((String) consultSessionMap.get("csUserName"));
		consultSession.setSource((String) consultSessionMap.get("source"));
		consultSession.setCsUserId((String) consultSessionMap.get("csUserId"));
		consultSession.setStatus((String) consultSessionMap.get("status"));
		consultSession.setTitle((String) consultSessionMap.get("title"));
		consultSession.setId((Integer) consultSessionMap.get("id"));
		consultSession.setPayStatus((String) consultSessionMap.get("payStatus"));
		consultSession.setNickName((String) consultSessionMap.get("nickName"));
		consultSession.setConsultNum((Integer)consultSessionMap.get("consultNum"));
		return consultSession;
	}

	public static HashMap<String,Object> transferRichConsultSessionToMap(RichConsultSession consultSession){
		HashMap<String,Object> consultSessionMap = new HashMap<String, Object>();
		consultSessionMap.put("userName", consultSession.getUserName());
		consultSessionMap.put("userId", consultSession.getUserId());
		consultSessionMap.put("serverAddress", consultSession.getServerAddress());
		consultSessionMap.put("createTime", consultSession.getCreateTime());
		consultSessionMap.put("csUserName", consultSession.getCsUserName());
		consultSessionMap.put("source", consultSession.getSource());
		consultSessionMap.put("csUserId", consultSession.getCsUserId());
		consultSessionMap.put("status", consultSession.getStatus());
		consultSessionMap.put("title", consultSession.getTitle());
		consultSessionMap.put("payStatus", consultSession.getPayStatus());
		consultSessionMap.put("nickName", consultSession.getNickName());
		consultSessionMap.put("id", consultSession.getId());
		consultSessionMap.put("consultNum", consultSession.getConsultNum());
		return consultSessionMap;
	}


	public static List<HashMap<String,Object>> transformCurrentUserListData(List<ConsultRecordMongoVo> dataList) {
		List<HashMap<String,Object>> listData = new ArrayList<HashMap<String, Object>>();
		for(ConsultRecordMongoVo dataVo:dataList){
			HashMap<String,Object> dataMap = new HashMap<String, Object>();
			dataMap.put("type",dataVo.getType());
			dataMap.put("content", dataVo.getMessage());
			dataMap.put("dateTime", (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(dataVo.getCreateDate()));
			dataMap.put("senderId",dataVo.getSenderId());
			dataMap.put("senderName",dataVo.getSenderName());
			dataMap.put("sessionId",dataVo.getSessionId());
			listData.add(dataMap);
		}
		return listData;
	}

	public static Integer transformMessageTypeToType(String messageType){
		if(messageType.equals("text")) {
			return 0;
		}else if(messageType.contains("image")){
			return 1;
		}else if(messageType.contains("voice")){
			return 2;
		}else{
			return 3;
		}
	}
}
