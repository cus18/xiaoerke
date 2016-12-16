package com.cxqm.xiaoerke.modules.consult.service.impl;

import java.net.InetSocketAddress;
import java.util.*;

import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.consult.service.util.ConsultUtil;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.data.redis.core.RedisTemplate;

import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional(readOnly = false)
public class SessionRedisCacheImpl implements SessionRedisCache {

	@Resource(name = "redisTemplate")
	private RedisTemplate<String, Object> redisTemplate;

	private static final String SESSIONID_CONSULTSESSION_KEY = "consult.sessionIdConsultSessionMapping";

	private static final String USER_SESSIONID_KEY = "consult.userSessionID";

	private static final String WECHAT_USER_PARAM = "wechat.user.param";

	private static final String WECHAT_DOCTOR_PARAM = "wechat.doctor.param";

	private static final String INSTANTCONSULT_LIST = "InstantConsult.ationList";

	@Override
	public RichConsultSession getConsultSessionBySessionId(Integer sessionId) {
		Map<Object,Object> sessionMap = redisTemplate.opsForHash().entries(String.valueOf(sessionId));
		return sessionMap == null ? null : ConsultUtil.transferMapToRichConsultSession(sessionMap);
	}

	@Override
	public void putSessionIdConsultSessionPair(Integer sessionId,
											   RichConsultSession consultSession) {
		if(sessionId!=null||consultSession!=null){
			redisTemplate.opsForHash().putAll(sessionId+"",ConsultUtil.transferRichConsultSessionToMap(consultSession));
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
	public void deleteUserSessionID() {
		redisTemplate.delete(USER_SESSIONID_KEY);
	}

	@Override
	public Integer getSessionIdByUserId(String userId) {
		Object sessionId = redisTemplate.opsForHash().get(USER_SESSIONID_KEY, userId);
		return sessionId == null ? null : Integer.parseInt(String.valueOf(sessionId)) ;
	}

	@Override
	public List<Object> getSessionIdByKey() {
		return redisTemplate.opsForHash().values(USER_SESSIONID_KEY);
	}


	@Override
	public void putUserIdSessionIdPair(String userId, Integer sessionId) {
		if(StringUtils.isNotNull(userId)||sessionId!=null){
			redisTemplate.opsForHash().put(USER_SESSIONID_KEY, userId, String.valueOf(sessionId));
		}
	}

	@Override
	public Map getWeChatParamFromRedis(String paramType){
		Object wechatParam = null;
		if(StringUtils.isNotNull(paramType)){
			if(paramType.equals("user")){
				wechatParam = redisTemplate.opsForHash().entries(WECHAT_USER_PARAM);
			}else if(paramType.equals("doctor")){
				wechatParam = redisTemplate.opsForHash().entries(WECHAT_DOCTOR_PARAM);
			}
		}
		return wechatParam == null ? null : (Map) wechatParam;
	}

	@Override
	public void putWeChatParamToRedis(HashMap<String, Object> wechatParam){
		if(wechatParam!=null){
			if(wechatParam.get("id").equals("1")){
				redisTemplate.opsForHash().putAll(WECHAT_USER_PARAM, wechatParam);
			}else if(wechatParam.get("id").equals("2")){
				redisTemplate.opsForHash().putAll(WECHAT_DOCTOR_PARAM, wechatParam);
			}

		}
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
	public void clearInstantConsultationList() {
		redisTemplate.delete(INSTANTCONSULT_LIST);
	}

	@Override
	public void addInstantConsultationList(String openid) {
		redisTemplate.opsForSet().add(INSTANTCONSULT_LIST, openid);
	}

	@Override
	public Long num4InstantConsultationList() {
		return	redisTemplate.opsForSet().size(INSTANTCONSULT_LIST);
	}

}
