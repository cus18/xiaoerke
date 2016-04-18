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

	void putWeChatToken(String token);

	void removeWeChatToken();

	void removeSessionIdConsultSessionPair(Integer sessionId);

	void removeUserIdSessionIdPair(String userId);

	void removeCsIdSessionIdPair(String csUserId);

	void removeWechatSessionPair(String openId);

	void removeConsultSessionBySessionId(Integer sessionId);

	List<Object> getConsultSessionsByKey();

	List<Object> getConsultSessionsBySessionIds(Collection<Object> sessionIds);

	Integer getSessionIdByOpenId(String openId);

	void putOpenIdSessionIdPair(String openId, Integer sessionId);

	void putWechatSessionByOpenId(String openId, RichConsultSession wechatSession);

	RichConsultSession getWechatSessionByOpenId(String openId);

	RichConsultSession getConsultSessionByCsId(String csUserId);

	String getWeChatToken();

	void putCsIdConsultSessionPair(String csUserId, RichConsultSession consultSession);
}
