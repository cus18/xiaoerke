package com.cxqm.xiaoerke.modules.consult.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.consult.service.util.ConsultUtil;
import org.springframework.data.redis.core.RedisTemplate;

import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = false)
public class SessionRedisCacheImpl implements SessionRedisCache {

	private RedisTemplate<String, Object> redisTemplate = SpringContextHolder.getBean("redisTemplate");
	
	private static final String SESSIONID_CONSULTSESSION_KEY = "consult.sessionIdConsultSessionMapping";
	
	private static final String USER_SESSIONID_KEY = "consult.userSessionID";

	private static final String USER_WECHATSESSION_KEY = "consult.wechatSession";

	private static final String WECHAT_TOKEN = "wechat.token";
	
	@Override
	public RichConsultSession getConsultSessionBySessionId(Integer sessionId) {
		HashMap<String,Object> sessionMap = (HashMap<String,Object>) redisTemplate.opsForHash().get(SESSIONID_CONSULTSESSION_KEY, sessionId);
		return sessionMap == null ? null : ConsultUtil.transferMapToRichConsultSession(sessionMap);
	}

	@Override
	public void putSessionIdConsultSessionPair(Integer sessionId,
											   RichConsultSession consultSession) {
		if(sessionId!=null||consultSession!=null){
			redisTemplate.opsForHash().put(SESSIONID_CONSULTSESSION_KEY,
					sessionId, ConsultUtil.transferRichConsultSessionToMap(consultSession));
		}
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
	public Integer getSessionIdByUserId(String userId) {
		Object sessionId = redisTemplate.opsForHash().get(USER_SESSIONID_KEY, userId);
		return sessionId == null ? null : (Integer) sessionId;
	}
	

	@Override
	public void putUserIdSessionIdPair(String userId, Integer sessionId) {
		if(StringUtils.isNotNull(userId)||sessionId!=null){
			redisTemplate.opsForHash().put(USER_SESSIONID_KEY, userId, sessionId);
		}
	}

	@Override
	public String getWeChatToken(){
		Object Token = redisTemplate.opsForHash().get(WECHAT_TOKEN,"wechatToken");
		return Token == null ? null : (String) Token;
	}

	@Override
	public void putWeChatToken(String token){
		if(StringUtils.isNotNull(token)){
			redisTemplate.opsForHash().put(WECHAT_TOKEN, "wechatToken", token);
		}
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

}
