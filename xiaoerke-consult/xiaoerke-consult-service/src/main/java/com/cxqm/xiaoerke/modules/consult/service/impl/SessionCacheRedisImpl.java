package com.cxqm.xiaoerke.modules.consult.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.SessionCache;
import com.cxqm.xiaoerke.modules.consult.service.util.ConsultUtil;
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

	private static final String CS_SESSION_KEY = "consult.csSessionID";

	private static final String WECHAT_TOKEN = "wechat.token";
	
	@Override
	public RichConsultSession getConsultSessionBySessionId(Integer sessionId) {
		HashMap<String,Object> sessionMap = (HashMap<String,Object>) redisTemplate.opsForHash().get(SESSIONID_CONSULTSESSION_KEY, sessionId);
		return sessionMap == null ? null : ConsultUtil.transferMapToRichConsultSession(sessionMap);
	}

	@Override
	public void putSessionIdConsultSessionPair(Integer sessionId,
											   RichConsultSession consultSession) {
		redisTemplate.opsForHash().put(SESSIONID_CONSULTSESSION_KEY,
				sessionId, ConsultUtil.transferRichConsultSessionToMap(consultSession));
	}

	@Override
	public void removeConsultSessionBySessionId(Integer sessionId){
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
	public void putUserIdSessionIdPair(String userId,
			Integer sessionId) {
		redisTemplate.opsForHash().put(USER_SESSIONID_KEY, userId, sessionId);
	}


	@Override
	public void putOpenIdSessionIdPair(String openId,
									   Integer sessionId) {
		redisTemplate.opsForHash().put(USER_SESSIONID_KEY, openId, sessionId);
	}

	@Override
	public void putWechatSessionByOpenId(String openId,RichConsultSession richConsultSession) {
		redisTemplate.opsForHash().put(USER_WECHATSESSION_KEY,
				openId, ConsultUtil.transferRichConsultSessionToMap(richConsultSession));
	}

	@Override
	public RichConsultSession getWechatSessionByOpenId(String openId) {
		HashMap<String,Object> wechatSession = (HashMap<String,Object>) redisTemplate.opsForHash().get(USER_WECHATSESSION_KEY, openId);
		return wechatSession == null ? null : ConsultUtil.transferMapToRichConsultSession(wechatSession);
	}

	@Override
	public List<Object> getConsultSessionByCsId(Collection<Object> csUserId) {
		List<Object> sessions = redisTemplate.opsForHash().multiGet(CS_SESSION_KEY, csUserId);
		return sessions;
	}


	@Override
	public String getWeChatToken(){
		Object Token = redisTemplate.opsForHash().get(WECHAT_TOKEN,"wechatToken");
		return Token == null ? null : (String) Token;
	}

	@Override
	public void putCsIdConsultSessionPair(String csUserId, RichConsultSession consultSession) {
		HashMap<String,Object> map = ConsultUtil.transferRichConsultSessionToMap(consultSession);
		redisTemplate.opsForHash().put(CS_SESSION_KEY,
				csUserId, ConsultUtil.transferRichConsultSessionToMap(consultSession));
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
	public void removeCsIdSessionIdPair(String csUserId) {
		redisTemplate.opsForHash().delete(CS_SESSION_KEY, csUserId);
	}

	@Override
	public void removeWechatSessionPair(String openId){
		redisTemplate.opsForHash().delete(USER_WECHATSESSION_KEY, openId);
	}

}
