package com.cxqm.xiaoerke.modules.consult.service.core;

import com.cxqm.xiaoerke.modules.consult.service.impl.SessionCacheRedisImpl;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionForwardRecordsVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionForwardRecordsService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;

public class ConsultSessionManager {
	
	private transient static final Logger log = LoggerFactory.getLogger(ConsultSessionManager.class);
	
	public static final String REQUEST_TYPE_CHAT = "chat";
	
	public static final String REQUEST_TYPE_NOTIFICATION = "notification";
	
	public static final String KEY_SESSION_ID = "sessionId";
	
	public static final String KEY_REQUEST_TYPE = "type";
	
	//<userId or cs-userId, Channel>
	private final Map<String, Channel> userChannelMapping = new ConcurrentHashMap<String, Channel>();
	
	//<cs-userId, Channel>
	private final Map<String, Channel> csuserChannelMapping = new ConcurrentHashMap<String, Channel>();
	
	//<cs-userId, Channel>
	private final Map<String, Channel> distributors = new ConcurrentHashMap<String, Channel>();
	
	//<Channel, userId or cs-userId>
	private final Map<Channel, String> channelUserMapping = new ConcurrentHashMap<Channel, String>();

	private List<String> distributorsList = null;
	
	private AtomicInteger accessNumber = new AtomicInteger(1000);
	
	private SessionCache sessionCache = new SessionCacheRedisImpl();
	
	private ConsultSessionService consultSessionService = SpringContextHolder.getBean("consultSessionServiceImpl");
	
	private ConsultSessionForwardRecordsService sessionForwardService = SpringContextHolder.getBean("consultSessionForwardRecordsServiceImpl");
	
	private SystemService systemService = SpringContextHolder.getBean("systemService");
	
	private static ConsultSessionManager sessionManager = new ConsultSessionManager();
	
	private ConsultSessionManager(){
		String distributorsStr = Global.getConfig("distributors.list");
		distributorsList = Arrays.asList(distributorsStr.split(";"));
	}
	
	public static ConsultSessionManager getSessionManager(){
		return sessionManager;
	}
	
	void createSession(ChannelHandlerContext ctx, FullHttpRequest msg, String url) {
		Channel channel = ctx.channel();
		
		String[] args = url.split("&");
		String fromType = args[1];
		String userId = args[2];
		
		if(fromType.equals("user")) {
			doCreateSessionInitiatedByUser(userId, channel);
		} else if(fromType.equals("cs")) {
			doCreateSessionInitiatedByCs(userId, channel);
		} else if(fromType.equals("distributor")) {
			doCreateSessionInitiatedByDistributor(userId, channel);
		}
	}
	
	private void doCreateSessionInitiatedByCs(String csUserId, Channel channel){
		csuserChannelMapping.put(csUserId, channel);
		userChannelMapping.put(csUserId, channel);
		channelUserMapping.put(channel, csUserId);
	}
	
	private void doCreateSessionInitiatedByDistributor(String distributorUserId, Channel channel){
		if(distributorsList.contains(distributorUserId)) {
			distributors.put(distributorUserId, channel);
			csuserChannelMapping.put(distributorUserId, channel);
			userChannelMapping.put(distributorUserId, channel);
			channelUserMapping.put(channel, distributorUserId);
		} else {
			log.warn("Maybe a Simulated Distributor: The userId is " + distributorUserId);
		}
	}
	
	private void doCreateSessionInitiatedByUser(String userId, Channel channel){
		Integer sessionId = sessionCache.getSessionIdByUserId(userId);
		
		RichConsultSession consultSession = null;
		if(sessionId != null)
			consultSession = sessionCache.getConsultSessionBySessionId(sessionId);
		
		if(consultSession == null) {
			User user = systemService.getUserById(userId);
			consultSession = new RichConsultSession();
			consultSession.setCreateTime(new Date());
			InetSocketAddress address = (InetSocketAddress) channel.localAddress();
			consultSession.setServerAddress(address.getHostName());
			consultSession.setUserId(userId);
			consultSession.setUserName(user.getName() == null ? user.getLoginName() : user.getName());
			
			int number = accessNumber.getAndDecrement();
			if(number < 10)
				accessNumber.set(1000);
			
			int distributorsListSize = distributorsList.size();
			int index = number % distributorsListSize;
			String distributorUserId = distributorsList.get(index);
			Channel distributorChannel = distributors.get(distributorUserId);
			if(distributorChannel != null) {
				consultSession.setCsUserId(distributorUserId);
			} else {
				int length = index + distributorsListSize + 2;
				for(int i = index + 1;  i < length; i ++) {
					distributorUserId = distributorsList.get(i % distributorsListSize);
					distributorChannel = distributors.get(distributorUserId);
					if(distributorChannel != null) {
						if(distributorChannel.isActive()) {
							consultSession.setCsUserId(distributorUserId);
							User csUser = systemService.getUser(userId);
							consultSession.setCsUserName(csUser.getName() == null ? csUser.getLoginName() : csUser.getName());
							break;
						} else {
							distributors.remove(distributorUserId);
						}
					}
				}
			}
			
			if(distributorChannel == null) {
				//TODO send msg to user that no distributor is available
				//TextWebSocketFrame msg = new TextWebSocketFrame("no distributor");
				//channel.writeAndFlush(msg);
				return;
			}
			
			consultSessionService.saveConsultInfo(consultSession);
			
			sessionId = consultSession.getId();
			sessionCache.putSessionIdConsultSessionPair(sessionId, consultSession);
			sessionCache.putUserIdSessionIdPair(userId, sessionId);
			
			//send user
			JSONObject obj = new JSONObject();
			obj.put("type", 4);
			obj.put("notifyType", "0001");
			obj.put("session", consultSession);
			TextWebSocketFrame frame = new TextWebSocketFrame(obj.toJSONString());
			distributorChannel.writeAndFlush(frame.retain());
		}
		
		userChannelMapping.put(userId, channel);
		channelUserMapping.put(channel, userId);
	}
	
	public void transferSession(Integer sessionId, String toCsUserId, String remark){
		RichConsultSession session = sessionCache.getConsultSessionBySessionId(sessionId);
		Channel channel = userChannelMapping.get(toCsUserId);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("type", 6);
		jsonObj.put("notifyType", 1);
		jsonObj.put("session", session);
		jsonObj.put("remark", remark);
		channel.writeAndFlush(jsonObj.toJSONString());
		
		ConsultSessionForwardRecordsVo forwardRecord = new ConsultSessionForwardRecordsVo();
		forwardRecord.setConversationId(sessionId.longValue());
		forwardRecord.setCreateBy(session.getCsUserId());
		forwardRecord.setCreateTime(new Date());
		forwardRecord.setFromUserId(session.getCsUserId());
		forwardRecord.setToUserId(toCsUserId);
		forwardRecord.setRemark(remark);
		forwardRecord.setStatus(ConsultSessionForwardRecordsVo.REACT_TRANSFER_STATUS_WAITING);
		sessionForwardService.save(forwardRecord);
	}
	
	public void react2Transfer(Integer sessionId, Integer forwardRecordId, String toCsUserId, String toCsUserName, String operation){
		RichConsultSession session = sessionCache.getConsultSessionBySessionId(sessionId);
		String fromCsUserId = session.getCsUserId();
		session.setCsUserId(toCsUserId);
		session.setCsUserName(toCsUserName);
		Channel channel = userChannelMapping.get(fromCsUserId);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("type", 6);
		jsonObj.put("notifyType", 2);
		jsonObj.put("operation", operation);
		jsonObj.put("session", session);
		channel.writeAndFlush(jsonObj.toJSONString());
		
		ConsultSessionForwardRecordsVo forwardRecord = new ConsultSessionForwardRecordsVo();
		forwardRecord.setId(forwardRecordId.longValue());
		
		if(ConsultSessionForwardRecordsVo.REACT_TRANSFER_OPERATION_ACCEPT.equalsIgnoreCase(operation)){
			sessionCache.putSessionIdConsultSessionPair(sessionId, session);
			forwardRecord.setStatus(ConsultSessionForwardRecordsVo.REACT_TRANSFER_STATUS_ACCEPT);
			sessionForwardService.updateAcceptedTransfer(forwardRecord);
		} else {
			forwardRecord.setStatus(ConsultSessionForwardRecordsVo.REACT_TRANSFER_STATUS_REJECT);
			sessionForwardService.updateRejectedTransfer(forwardRecord);
		}
		
	}
	
	public void cancelTransferringSession(Integer sessionId, String toCsUserId, String remark){
		RichConsultSession session = sessionCache.getConsultSessionBySessionId(sessionId);
		ConsultSessionForwardRecordsVo forwardRecord = new ConsultSessionForwardRecordsVo();
		forwardRecord.setConversationId(sessionId.longValue());
		forwardRecord.setFromUserId(session.getCsUserId());
		forwardRecord.setToUserId(toCsUserId);
		forwardRecord.setStatus(ConsultSessionForwardRecordsVo.REACT_TRANSFER_STATUS_CANCELLED);
		int count = sessionForwardService.cancelTransfer(forwardRecord);
		
		if(count > 0) {
			Channel channel = userChannelMapping.get(toCsUserId);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("type", 6);
			jsonObj.put("notifyType", 3);
			jsonObj.put("session", session);
			channel.writeAndFlush(jsonObj.toJSONString());
		}
	}

	public List<String> getOnlineCsList() {
		List<String> userIds = new ArrayList<String>();
		Iterator<Entry<String, Channel>> it = csuserChannelMapping.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, Channel> entry = it.next();
			if(entry.getValue().isOpen())
				userIds.add(entry.getKey());
		}
		return userIds;
	}
	
	public Map<String, Channel> getUserChannelMapping() {
		return userChannelMapping;
	}
	
	public Map<Channel, String> getChannelUserMapping() {
		return channelUserMapping;
	}
	
	//TODO service for getting sessionId
	public Integer getSessionIdByUser(String userId) {
		return sessionCache.getSessionIdByUserId(userId);
	}
	
	//TODO service for getting sessionId
	public List<Object> getCurrentSessions(List<Object> sessionIds) {
		return sessionCache.getConsultSessionsBySessionIds(sessionIds);
	}
	
	public Map<String, Channel> getCsUserChannelMapping() {
		return csuserChannelMapping;
	}

}
