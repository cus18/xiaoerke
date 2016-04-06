package com.cxqm.xiaoerke.modules.consult.service.impl;

import java.util.Collection;
import java.util.List;

import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.SessionCache;
import org.springframework.data.redis.core.RedisTemplate;

import com.cxqm.xiaoerke.common.utils.SpringContextHolder;

import javax.websocket.Session;

public class SessionCacheRedisImpl implements SessionCache {

	private RedisTemplate<String, Object> redisTemplate = SpringContextHolder.getBean("redisTemplate");
	
	private static final String SESSIONID_CONSULTSESSION_KEY = "consult.sessionIdConsultSessionMapping";
	
	private static final String USER_SESSIONID_KEY = "consult.userSessionID";

	private static final String USER_WECHATSESSION_KEY = "consult.wechatSession";
	
	@Override
	public RichConsultSession getConsultSessionBySessionId(Integer sessionId) {
		Object session = redisTemplate.opsForHash().get(SESSIONID_CONSULTSESSION_KEY, sessionId);
		return session == null ? null : (RichConsultSession) session;
	}
	
	@Override
	public List<Object> getConsultSessionsBySessionIds(Collection<Object> sessionIds) {
		List<Object> sessions = redisTemplate.opsForHash().multiGet(SESSIONID_CONSULTSESSION_KEY, sessionIds);
		return sessions;
	}

	@Override
	public Integer getSessionIdByClientServerId(String clientServerId) {
		Object sessionId = redisTemplate.opsForHash().get(USER_SESSIONID_KEY, clientServerId);
		return sessionId == null ? null : (Integer) sessionId;
	}


	@Override
	public Integer getSessionIdByUserId(String userId) {
		Object sessionId = redisTemplate.opsForHash().get(USER_SESSIONID_KEY, userId);
		return sessionId == null ? null : (Integer) sessionId;
	}
	
	@Override
	public void putSessionIdConsultSessionPair(Integer sessionId,
			RichConsultSession consultSession) {
		redisTemplate.opsForHash().put(SESSIONID_CONSULTSESSION_KEY, sessionId, consultSession);
	}

	@Override
	public void putUserIdSessionIdPair(String userId,
			Integer sessionId) {
		redisTemplate.opsForHash().put(USER_SESSIONID_KEY, userId, sessionId);
	}

	@Override
	public void putClientServerIdSessionIdPair(String clientServerId,
									   Integer sessionId) {
		redisTemplate.opsForHash().put(USER_SESSIONID_KEY, clientServerId, sessionId);
	}

	@Override
	public void putWechatSessionByClientServerId(String clientServerId,Session wechatSession) {
		redisTemplate.opsForHash().put(USER_WECHATSESSION_KEY, clientServerId, wechatSession);
	}

	@Override
	public Session getWechatSessionByClientServerId(String clientServerId) {
		Object wechatSession = redisTemplate.opsForHash().get(USER_WECHATSESSION_KEY, clientServerId);
		return wechatSession == null ? null : (Session) wechatSession;
	}
	
	@Override
	public void removeSessionIdConsultSessionPair(Integer sessionId) {
		redisTemplate.opsForHash().delete(SESSIONID_CONSULTSESSION_KEY, sessionId);
	}

	@Override
	public void removeUserIdSessionIdPair(String userId) {
		redisTemplate.opsForHash().delete(USER_SESSIONID_KEY, userId);
	}

	@Override
	public void removeWechatSessionPair(String clientServerId){
		redisTemplate.opsForHash().delete(USER_WECHATSESSION_KEY, clientServerId);
	}
}
