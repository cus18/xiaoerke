package com.cxqm.xiaoerke.modules.consult.service.core;

import java.util.Collection;
import java.util.List;

public interface SessionCache {
	
	void putSessionIdConsultSessionPair(Integer sessionId, RichConsultSession consultSession);
	
	void putUserIdSessionIdPair(String userId, Integer sessionId);
	
	RichConsultSession getConsultSessionBySessionId(Integer sessionId);
	
	Integer getSessionIdByUserId(String userId);
	
	void removeSessionIdConsultSessionPair(Integer sessionId);

	void removeUserIdSessionIdPair(String userId);

	List<Object> getConsultSessionsBySessionIds(Collection<Object> sessionIds);
	
}
