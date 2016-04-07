package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;

import javax.websocket.Session;
import java.util.Collection;
import java.util.List;

public interface SessionCache {
	
	void putSessionIdConsultSessionPair(Integer sessionId, RichConsultSession consultSession);
	
	void putUserIdSessionIdPair(String userId, Integer sessionId);
	
	RichConsultSession getConsultSessionBySessionId(Integer sessionId);
	
	Integer getSessionIdByUserId(String userId);
	
	void removeSessionIdConsultSessionPair(Integer sessionId);

	void removeUserIdSessionIdPair(String userId);

	void removeWechatSessionPair(String openId);

	List<Object> getConsultSessionsBySessionIds(Collection<Object> sessionIds);

	Integer getSessionIdByOpenId(String openId);

	void putopenIdSessionIdPair(String openId, Integer sessionId);

	void putWechatSessionByOpenId(String openId, Session wechatSession);

	Session getWechatSessionByOpenId(String openId);
}
