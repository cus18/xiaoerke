package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;

import javax.websocket.Session;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SessionRedisCache {
	
	void putSessionIdConsultSessionPair(Integer sessionId, RichConsultSession consultSession);
	
	void putUserIdSessionIdPair(String userId, Integer sessionId);
	
	RichConsultSession getConsultSessionBySessionId(Integer sessionId);
	
	Integer getSessionIdByUserId(String userId);

	Map getWeChatToken(String paramType);

	void putWeChatToken(Map wechatParam);

	void removeSessionIdConsultSessionPair(Integer sessionId);

	void removeUserIdSessionIdPair(String userId);

	void removeConsultSessionBySessionId(Integer sessionId);

	List<Object> getConsultSessionsByKey();

	List<Object> getConsultSessionsBySessionIds(Collection<Object> sessionIds);

}
