package com.cxqm.xiaoerke.modules.consult.service.impl;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private static final String WECHAT_USER_PARAM = "wechat.user.param";

	private static final String WECHAT_DOCTOR_PARAM = "wechat.doctor.param";

	private static final String USER_ADDRESS = "user.address";
	
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
	public void putUserIdIpAddressPair(InetSocketAddress inetSocketAddress, String userId) {
		if(StringUtils.isNotNull(userId)||inetSocketAddress!=null){
			redisTemplate.opsForHash().put(USER_ADDRESS, userId, inetSocketAddress);
		}
	}

	@Override
	public InetSocketAddress getIpAddressByUserId(String userId) {
		if(StringUtils.isNotNull(userId)){
			Object userIpAddress = redisTemplate.opsForHash().get(USER_ADDRESS, userId);
			return userIpAddress == null ? null : (InetSocketAddress) userIpAddress;
		}else{
			return null;
		}
	}

	@Override
	public void removeIpAddressByUserId(String userId){
		redisTemplate.opsForHash().delete(USER_ADDRESS, userId);
	}

	@Override
	public Integer getSessionIdByUserId(String userId) {
		Object sessionId = redisTemplate.opsForHash().get(USER_SESSIONID_KEY, userId);
		return sessionId == null ? null : (Integer) sessionId;
	}

	@Override
	public List<Object> getSessionIdByKey() {
		return redisTemplate.opsForHash().values(USER_SESSIONID_KEY);
	}
	

	@Override
	public void putUserIdSessionIdPair(String userId, Integer sessionId) {
		if(StringUtils.isNotNull(userId)||sessionId!=null){
			redisTemplate.opsForHash().put(USER_SESSIONID_KEY, userId, sessionId);
		}
	}

	@Override
	public Map getWeChatParamFromRedis(String paramType){
		Object wechatParam = null;
		if(StringUtils.isNotNull(paramType)){
			if(paramType.equals("user")){
				wechatParam = redisTemplate.opsForHash().get(WECHAT_USER_PARAM,"wechatUserParam");
			}else if(paramType.equals("doctor")){
				wechatParam = redisTemplate.opsForHash().get(WECHAT_DOCTOR_PARAM,"wechatDoctorParam");
			}
		}
		return wechatParam == null ? null : (Map) wechatParam;
	}

	@Override
	public void putWeChatParamToRedis(Map wechatParam){
		if(wechatParam!=null){
			if(wechatParam.get("id").equals("1")){
				redisTemplate.opsForHash().put(WECHAT_USER_PARAM, "wechatUserParam", wechatParam);
			}else if(wechatParam.get("id").equals("2")){
				redisTemplate.opsForHash().put(WECHAT_DOCTOR_PARAM, "wechatDoctorParam", wechatParam);
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

}
