package com.cxqm.xiaoerke.modules.consult.service.impl;

import java.util.Collection;
import java.util.List;

import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.SessionCache;
import org.springframework.data.redis.core.RedisTemplate;

import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.websocket.Session;


@Service
@Transactional(readOnly = false)
public class SessionCacheRedisImpl implements SessionCache {

	private RedisTemplate<String, Object> redisTemplate = SpringContextHolder.getBean("redisTemplate");
	
	private static final String SESSIONID_CONSULTSESSION_KEY = "consult.sessionIdConsultSessionMapping";
	
	private static final String USER_SESSIONID_KEY = "consult.userSessionID";

	private static final String USER_WECHATSESSION_KEY = "consult.wechatSession";

	private static final String WECHAT_TOKEN = "wechat.token";
	
	@Override
	public RichConsultSession getConsultSessionBySessionId(Integer sessionId) {
		Object session = redisTemplate.opsForHash().get(SESSIONID_CONSULTSESSION_KEY, sessionId);
//		List<HV> multiGet(H key, Collection<HK> hashKeys);

		return session == null ? null : (RichConsultSession) session;
	}

	@Override
	public void removeConsultSession(Integer sessionId){
		redisTemplate.opsForHash().delete(SESSIONID_CONSULTSESSION_KEY, sessionId);
	}

	@Override
	public List<Object> getConsultSessionsByKey() {
		List<Object> objects = redisTemplate.opsForHash().values(SESSIONID_CONSULTSESSION_KEY);
		return objects;
	}

	@Override
	public List<Object> getConsultSessionsBySessionIds(Collection<Object> sessionIds) {
		List<Object> sessions = redisTemplate.opsForHash().multiGet(SESSIONID_CONSULTSESSION_KEY, sessionIds);
		return sessions;
	}

	@Override
	public Integer getSessionIdByOpenId(String openId) {
		Object sessionId = redisTemplate.opsForHash().get(USER_SESSIONID_KEY, openId);
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
	public void putopenIdSessionIdPair(String openId,
									   Integer sessionId) {
		redisTemplate.opsForHash().put(USER_SESSIONID_KEY, openId, sessionId);
	}

	@Override
	public void putWechatSessionByOpenId(String openId,RichConsultSession richConsultSession) {
		redisTemplate.opsForHash().put(USER_WECHATSESSION_KEY, openId, richConsultSession);
	}

	@Override
	public Session getWechatSessionByOpenId(String openId) {
		Object wechatSession = redisTemplate.opsForHash().get(USER_WECHATSESSION_KEY, openId);
		return wechatSession == null ? null : (Session) wechatSession;
	}

	@Override
	public String getWeChatToken(){
		Object Token = redisTemplate.opsForHash().get(WECHAT_TOKEN,"wechatToken");
		return Token == null ? null : (String) Token;
	}

	@Override
	public void putWeChatToken(String token){
		redisTemplate.opsForHash().put(WECHAT_TOKEN, "wechatToken", token);
	}

	@Override
	public void removeWeChatToken(){
		redisTemplate.opsForHash().delete(WECHAT_TOKEN, "wechatToken");
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
	public void removeWechatSessionPair(String openId){
		redisTemplate.opsForHash().delete(USER_WECHATSESSION_KEY, openId);
	}
}
