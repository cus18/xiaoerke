package com.cxqm.xiaoerke.modules.consult.service.core;

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

	void removeWechatSessionPair(String clientServerId);

	List<Object> getConsultSessionsBySessionIds(Collection<Object> sessionIds);

	Integer getSessionIdByClientServerId(String clientServerId);

	void putClientServerIdSessionIdPair(String clientServerId, Integer sessionId);

	void putWechatSessionByClientServerId(String clientServerId,Session wechatSession);

	Session getWechatSessionByClientServerId(String clientServerId);
}
