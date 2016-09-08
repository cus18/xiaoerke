package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;

import javax.websocket.Session;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SessionRedisCache {
	
	void putSessionIdConsultSessionPair(Integer sessionId, RichConsultSession consultSession);

	List<Object> getSessionIdByKey();

	void putUserIdSessionIdPair(String userId, Integer sessionId);
	
	RichConsultSession getConsultSessionBySessionId(Integer sessionId);

	InetSocketAddress getIpAddressByUserId(String userId);

	void removeIpAddressByUserId(String userId);

	Integer getSessionIdByUserId(String userId);

	Map getWeChatParamFromRedis(String paramType);

	void putWeChatParamToRedis(Map wechatParam);

	void removeSessionIdConsultSessionPair(Integer sessionId);

	void removeUserIdSessionIdPair(String userId);

	void removeConsultSessionBySessionId(Integer sessionId);

	List<Object> getConsultSessionsByKey();

	List<Object> getConsultSessionsBySessionIds(Collection<Object> sessionIds);

	void putUserIdIpAddressPair(InetSocketAddress inetSocketAddress, String csUserId);

	void clearInstantConsultationList();

	void addInstantConsultationList(String openid);

	Long num4InstantConsultationList();
}
