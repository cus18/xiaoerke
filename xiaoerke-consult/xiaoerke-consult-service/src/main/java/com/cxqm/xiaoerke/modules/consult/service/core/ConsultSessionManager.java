package com.cxqm.xiaoerke.modules.consult.service.core;

import com.alibaba.fastjson.JSONObject;
import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionForwardRecordsVo;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionForwardRecordsService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.service.impl.UserInfoServiceImpl;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsultSessionManager {
	
	private transient static final Logger log = LoggerFactory.getLogger(ConsultSessionManager.class);
	
	public static final String REQUEST_TYPE_CHAT = "chat";
	
	public static final String REQUEST_TYPE_NOTIFICATION = "notification";
	
	public static final String KEY_SESSION_ID = "sessionId";
	
	public static final String KEY_REQUEST_TYPE = "type";

	public static final String KEY_CONSULT_CONTENT = "content";
	
	//<userId or cs-userId, Channel>
	private final Map<String, Channel> userChannelMapping = new ConcurrentHashMap<String, Channel>();
	
	//<cs-userId, Channel>
	private final Map<String, Channel> csUserChannelMapping = new ConcurrentHashMap<String, Channel>();
	
	//<cs-userId, Channel>
	private final Map<String, Channel> distributors = new ConcurrentHashMap<String, Channel>();
	
	//<Channel, userId or cs-userId>
	private final Map<Channel, String> channelUserMapping = new ConcurrentHashMap<Channel, String>();

	private List<String> distributorsList = null;
	
	private AtomicInteger accessNumber = new AtomicInteger(1000);

	private SessionRedisCache sessionRedisCache = SpringContextHolder.getBean("sessionRedisCacheImpl");

	private ConsultSessionService consultSessionService = SpringContextHolder.getBean("consultSessionServiceImpl");

	private ConsultSessionForwardRecordsService consultSessionForwardRecordsService = SpringContextHolder.getBean("consultSessionForwardRecordsServiceImpl");
	
	private SystemService systemService = SpringContextHolder.getBean("systemService");

	private UserInfoServiceImpl userInfoService = SpringContextHolder.getBean("userInfoServiceImpl");

	private static ExecutorService threadExecutor = Executors.newCachedThreadPool();

	private static ConsultSessionManager sessionManager = new ConsultSessionManager();

	private ConsultSessionManager(){
		String distributorsStr = Global.getConfig("distributors.list");
		distributorsList = Arrays.asList(distributorsStr.split(";"));
	}
	
	public static ConsultSessionManager getSessionManager(){
		return sessionManager;
	}
	
	void createSession(ChannelHandlerContext ctx, String url) {

		Channel channel = ctx.channel();
		
		String[] args = url.split("&");
		String fromType = args[1];

		User user = UserUtils.getUser();
		if(args.length>2){
			if(fromType.equals("user")) {
				String userId = args[2];
				String source = args[3];
				doCreateSessionInitiatedByUser(userId,source,channel);
			}else if(fromType.equals("cs")) {
				String userId = args[2];
				doCreateSessionInitiatedByCs(userId,channel);
			} else if(fromType.equals("distributor")) {
				String userId = args[2];
				doCreateSessionInitiatedByDistributor(userId, channel);
			}
		}
	}
	
	private void doCreateSessionInitiatedByCs(String csUserId, Channel channel){
		csUserChannelMapping.put(csUserId, channel);
		userChannelMapping.put(csUserId, channel);
		channelUserMapping.put(channel, csUserId);
	}

	private void doCreateSessionInitiatedByDistributor(String distributorUserId, Channel channel){
		if(distributorsList.contains(distributorUserId)) {
			distributors.put(distributorUserId, channel);
			csUserChannelMapping.put(distributorUserId, channel);
			userChannelMapping.put(distributorUserId, channel);
			channelUserMapping.put(channel, distributorUserId);
		} else {
			log.warn("Maybe a Simulated Distributor: The userId is " + distributorUserId);
		}
	}

	public void removeUserSession(String userId){
		Iterator iterator = userChannelMapping.keySet().iterator();
		while (iterator.hasNext()){
			String key = (String) iterator.next();
			if (userId.equals(key)) {
				iterator.remove();
				userChannelMapping.remove(key);
			}
		}
	}

	private void doCreateSessionInitiatedByUser(String userId, String source,Channel channel){
		Integer sessionId = sessionRedisCache.getSessionIdByUserId(userId);
		
		RichConsultSession consultSession = null;
		if(sessionId != null)
			consultSession = sessionRedisCache.getConsultSessionBySessionId(sessionId);
		
		if(consultSession == null) {
			User user = systemService.getUserById(userId);
			consultSession = new RichConsultSession();
			consultSession.setCreateTime(new Date());
			InetSocketAddress address = (InetSocketAddress) channel.localAddress();
			consultSession.setServerAddress(address.getHostName());
			consultSession.setUserId(userId);
			consultSession.setUserName(user.getName() == null ? user.getLoginName() : user.getName());
			consultSession.setSource(source);

			int number = accessNumber.getAndDecrement();
			if(number < 10)
				accessNumber.set(1000);
			
			int distributorsListSize = distributorsList.size();
			int index = number % distributorsListSize;
			Channel distributorChannel;

			/***在此处，先随机为用户分配一个在线的接诊员，如果，此接诊员不在线，再顺序分配一个在线接诊员***/
			String distributorUserId = distributorsList.get(index);
			distributorChannel = distributors.get(distributorUserId);
			if(distributorChannel != null) {
				if(distributorChannel.isActive()){
					consultSession.setCsUserId(distributorUserId);
					User csUser = systemService.getUserById(distributorUserId);
					consultSession.setCsUserName(csUser.getName() == null ? csUser.getLoginName() : csUser.getName());
				}else{
					distributorChannel = null;
					distributors.remove(distributorUserId);
					csUserChannelMapping.remove(distributorUserId);
				}
			}

			//顺序分配在线的接诊员
			if(distributorChannel==null){
				int length = index + distributorsListSize + 2;
				for(int i = index + 1;  i < length; i ++) {
					distributorUserId = distributorsList.get(i % distributorsListSize);
					distributorChannel = distributors.get(distributorUserId);
					if(distributorChannel != null) {
						if(distributorChannel.isActive()) {
							consultSession.setCsUserId(distributorUserId);
							User csUser = systemService.getUserById(distributorUserId);
							consultSession.setCsUserName(csUser.getName() == null ? csUser.getLoginName() : csUser.getName());
							break;
						} else {
							distributors.remove(distributorUserId);
							csUserChannelMapping.remove(distributorUserId);
						}
					}
				}
			}
			/***在此处，先随机为用户分配一个在线的接诊员，如果，此接诊员不在线，再顺序分配一个在线接诊员***/

			if(distributorChannel == null) {
				if(csUserChannelMapping.size()!=0){
					//所有的接诊员不在线，随机分配一个在线医生
					Iterator<Entry<String, Channel>> csUserChannel = csUserChannelMapping.entrySet().iterator();
					List<HashMap<String,Object>> doctorOnLineList = new ArrayList();
					while (csUserChannel.hasNext()) {
						HashMap<String,Object> doctorOnLineMap = new HashMap<String,Object>();
						Map.Entry<String, Channel> entry = csUserChannel.next();
						Channel csChannel = entry.getValue();
						if(csChannel.isActive()){
							doctorOnLineMap.put("csUserId", entry.getKey());
							doctorOnLineMap.put("channel", entry.getValue());
							doctorOnLineList.add(doctorOnLineMap);
						}
					}
					//通过一个随机方法，从doctorOnLineList选择一个医生，为用户提供服务
					Random rand = new Random();
					if(doctorOnLineList!=null && doctorOnLineList.size()>0){
						int indexCS = rand.nextInt(doctorOnLineList.size());
						consultSession.setCsUserId((String) doctorOnLineList.get(indexCS).get("csUserId"));
						Channel csChannel = (Channel) doctorOnLineList.get(indexCS).get("channel");
						if(csChannel.isActive()){
							//csChannel如果活着的话，证明，此医生处于登陆状态
							User csUser = systemService.getUserById((String) doctorOnLineList.get(indexCS).get("csUserId"));
							consultSession.setCsUserName(csUser.getName() == null ? csUser.getLoginName() : csUser.getName());
						}else{
							csUserChannelMapping.remove(doctorOnLineList.get(indexCS).get("csUserId"));
						}
					}
				}else{
					//TextWebSocketFrame msg = new TextWebSocketFrame("no distributor");
					//channel.writeAndFlush(msg);
					return;
				}
			}
			
			consultSessionService.saveConsultInfo(consultSession);
			
			sessionId = consultSession.getId();
			sessionRedisCache.putSessionIdConsultSessionPair(sessionId, consultSession);
			sessionRedisCache.putUserIdSessionIdPair(userId, sessionId);
			
			//send doctor or distributor
			JSONObject obj = new JSONObject();
      JSONObject csobj = new JSONObject();
      if(distributorChannel != null ){
          obj.put("type", 4);
          obj.put("notifyType", "0001");
          obj.put("session", consultSession);
          obj.put("content", "");
          TextWebSocketFrame frame = new TextWebSocketFrame(obj.toJSONString());
          distributorChannel.writeAndFlush(frame.retain());

          //sender，告诉会有哪个医生或者接诊员提供服务
          csobj.put("type",5);
          csobj.put("notifyType","0003");
          csobj.put("doctorName",consultSession.getCsUserName());
          csobj.put("content", "您好,您当前已进入宝大夫咨询平台,接下来可以进行咨询!");
          TextWebSocketFrame csframe = new TextWebSocketFrame(csobj.toJSONString());
          channel.writeAndFlush(csframe.retain());
      }else{
          TextWebSocketFrame csframe = new TextWebSocketFrame("no distribute,please wait a while ...");
          channel.writeAndFlush(csframe.retain());
          return;
      }
		}
		
		userChannelMapping.put(userId, channel);
		channelUserMapping.put(channel, userId);
	}

	public HashMap<String,Object> createUserWXConsultSession(RichConsultSession consultSession){

		HashMap<String,Object> response = new HashMap<String, Object>();
		Channel  csChannel = null;

		int number = accessNumber.getAndDecrement();
		if(number < 10)
			accessNumber.set(1000);
		int distributorsListSize = distributorsList.size();
		int index = number % distributorsListSize;
		Channel distributorChannel;

		/***在此处，先随机为用户分配一个在线的接诊员，如果，此接诊员不在线，再顺序分配一个在线接诊员***/
		String distributorUserId = distributorsList.get(index);
		distributorChannel = distributors.get(distributorUserId);
		if(distributorChannel != null) {
			if(distributorChannel.isActive()){
				consultSession.setCsUserId(distributorUserId);
				User csUser = systemService.getUserById(distributorUserId);
				consultSession.setCsUserName(csUser.getName() == null ? csUser.getLoginName() : csUser.getName());
				csChannel = distributorChannel;
			}else{
				distributorChannel = null;
				distributors.remove(distributorUserId);
				csUserChannelMapping.remove(distributorUserId);
			}
		}

		if(distributorChannel==null){
			int length = index + distributorsListSize + 2;
			for(int i = index + 1;  i < length; i ++) {
				distributorUserId = distributorsList.get(i % distributorsListSize);
				distributorChannel = distributors.get(distributorUserId);
				if(distributorChannel != null) {
					if(distributorChannel.isActive()) {
						consultSession.setCsUserId(distributorUserId);
						User csUser = systemService.getUserById(distributorUserId);
						consultSession.setCsUserName(csUser.getName() == null ? csUser.getLoginName() : csUser.getName());
						csChannel = distributorChannel;
						break;
					} else {
						distributors.remove(distributorUserId);
						csUserChannelMapping.remove(distributorUserId);
					}
				}
			}
		}
		/***在此处，先随机为用户分配一个在线的接诊员，如果，此接诊员不在线，再顺序分配一个在线接诊员***/


		if(distributorChannel == null) {
			if(csUserChannelMapping.size()!=0){
				//所有的接诊员不在线，随机分配一个在线医生
				Iterator<Entry<String, Channel>> csUserChannel = csUserChannelMapping.entrySet().iterator();
				List<HashMap<String,Object>> doctorOnLineList = new ArrayList();
				while (csUserChannel.hasNext()) {
					HashMap<String,Object> doctorOnLineMap = new HashMap<String,Object>();
					Map.Entry<String, Channel> entry = csUserChannel.next();
					Channel csTempChannel = entry.getValue();
					if(csTempChannel.isActive()){
						doctorOnLineMap.put("csUserId", entry.getKey());
						doctorOnLineMap.put("channel", entry.getValue());
						doctorOnLineList.add(doctorOnLineMap);
					}
				}
				//通过一个随机方法，从doctorOnLineList选择一个医生，为用户提供服务
				Random rand = new Random();
				if(doctorOnLineList!=null && doctorOnLineList.size()>0){
					int indexCS = rand.nextInt(doctorOnLineList.size());
					consultSession.setCsUserId((String) doctorOnLineList.get(indexCS).get("csUserId"));
					csChannel = (Channel) doctorOnLineList.get(indexCS).get("channel");
					if(csChannel.isActive()){
						//csChannel如果活着的话，证明，此医生处于登陆状态
						User csUser = systemService.getUserById((String) doctorOnLineList.get(indexCS).get("csUserId"));
						consultSession.setCsUserName(csUser.getName() == null ? csUser.getLoginName() : csUser.getName());
					}else{
						csUserChannelMapping.remove(doctorOnLineList.get(indexCS).get("csUserId"));
					}
				}
			}else{
				//如果没有任何医生在线，给用户推送微信消息，告知没有医生在线，稍后在使用服务
				return null;
			}
		}


		HashMap<String, Object> perInfo = new HashMap<String, Object>();
		if ( StringUtils.isNotNull(consultSession.getCsUserId())) {
			perInfo = userInfoService.findPersonDetailInfoByUserId(consultSession.getCsUserId());
		}
		consultSession.setCsUserName((String) perInfo.get("name"));

		//可开启线程进行记录
		if(consultSession.getCsUserId()!=null){
			consultSessionService.saveConsultInfo(consultSession);
			Integer sessionId = consultSession.getId();
			sessionRedisCache.putSessionIdConsultSessionPair(sessionId, consultSession);
			sessionRedisCache.putUserIdSessionIdPair(consultSession.getUserId(), sessionId);
			response.put("csChannel", csChannel);
			response.put("sessionId", sessionId);
			response.put("consultSession", consultSession);
			return response;
		}else{
			return null;
		}

	}

	public int transferSession(Integer sessionId, String toCsUserId, String remark){
		try{
			RichConsultSession session = sessionRedisCache.getConsultSessionBySessionId(sessionId);

			ConsultSessionForwardRecordsVo consultSessionForwardRecordsVo = new ConsultSessionForwardRecordsVo();
			consultSessionForwardRecordsVo.setConversationId(Long.parseLong(String.valueOf(sessionId)));
			consultSessionForwardRecordsVo.setToUserId(toCsUserId);
			consultSessionForwardRecordsVo.setFromUserId(session.getCsUserId());
			List<ConsultSessionForwardRecordsVo> consultSessionForwardRecordsVoList =
					consultSessionForwardRecordsService.selectConsultForwardList(consultSessionForwardRecordsVo);

			if(consultSessionForwardRecordsVoList.size()>0){
				for(ConsultSessionForwardRecordsVo consultSessionForwardRecords:consultSessionForwardRecordsVoList){
					String status = consultSessionForwardRecords.getStatus();
					if(status.equals(ConsultSessionForwardRecordsVo.REACT_TRANSFER_STATUS_WAITING)){
						return 2;
					}
				}
			}

			User toCsUser = systemService.getUser(toCsUserId);
			Channel channelToCsUser = userChannelMapping.get(toCsUserId);
			Channel channelFromCsUser = userChannelMapping.get(session.getCsUserId());
			if(channelFromCsUser.isActive()){

				//通知发起转接的人，转接正在处理中在5秒钟内，接诊员有机会取消转接，如果，5秒后，接诊员不取消，则接诊员不能再取消转接
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("type", "4");
				jsonObj.put("notifyType", "0011");
				jsonObj.put("session", session);
				jsonObj.put("remark", remark);
				jsonObj.put("toCsUserId", toCsUserId);
				jsonObj.put("toCsUserName", toCsUser.getName());
				TextWebSocketFrame frameFromCsUser = new TextWebSocketFrame(jsonObj.toJSONString());
				channelFromCsUser.writeAndFlush(frameFromCsUser.retain());

				ConsultSessionForwardRecordsVo forwardRecord = new ConsultSessionForwardRecordsVo();
				forwardRecord.setConversationId(sessionId.longValue());
				forwardRecord.setCreateBy(session.getCsUserId());
				forwardRecord.setCreateTime(new Date());
				forwardRecord.setFromUserId(session.getCsUserId());
				forwardRecord.setToUserId(toCsUserId);
				forwardRecord.setRemark(remark);
				forwardRecord.setStatus(ConsultSessionForwardRecordsVo.REACT_TRANSFER_STATUS_WAITING);
				consultSessionForwardRecordsService.save(forwardRecord);

				Runnable thread = new processTransferThread(forwardRecord.getId(),channelToCsUser,channelFromCsUser,session,toCsUser,remark);
				threadExecutor.execute(thread);

				return 1;
			}else{
				return 0;
			}
		}catch (Exception e){
			return 0;
		}
	}

	public class processTransferThread extends Thread {
		private long forwardRecordId;
		private Channel channelToCsUser;
		private Channel channelFromCsUser;
		private RichConsultSession session;
		private User toCsUser;
		private String remark;

		public processTransferThread(long forwardRecordId,Channel channelToCsUser,Channel channelFromCsUser,RichConsultSession session,User toCsUser,String remark) {
			this.forwardRecordId = forwardRecordId;
			this.channelToCsUser = channelToCsUser;
			this.channelFromCsUser = channelFromCsUser;
			this.session = session;
			this.toCsUser = toCsUser;
			this.remark = remark;
		}

		public void run() {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//查询SessionForwardRecords，如果此条记录，已经被接诊员取消，则不再通知医生转接
			ConsultSessionForwardRecordsVo sessionForwardRecordsVo = consultSessionForwardRecordsService.selectByPrimaryKey(forwardRecordId);
			if(!(sessionForwardRecordsVo.getStatus().equals(ConsultSessionForwardRecordsVo.REACT_TRANSFER_STATUS_CANCELLED))){
				if(channelToCsUser.isActive()&&channelFromCsUser.isActive()){
					//通知被转接咨询医生，有用户需要转接
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("type", "4");
					jsonObj.put("notifyType", "0009");
					jsonObj.put("session", session);
					jsonObj.put("toCsUserName", toCsUser.getName());
					jsonObj.put("remark", remark);
					TextWebSocketFrame frameToCsUser = new TextWebSocketFrame(jsonObj.toJSONString());
					channelToCsUser.writeAndFlush(frameToCsUser.retain());

					//通知接诊员，不能再取消转接
					jsonObj.clear();
					jsonObj.put("type", "4");
					jsonObj.put("notifyType", "0013");
					jsonObj.put("session", session);
					TextWebSocketFrame frameFromCsUser = new TextWebSocketFrame(jsonObj.toJSONString());
					channelFromCsUser.writeAndFlush(frameFromCsUser.retain());

					try {
						//一分钟后判断，如果，该会话，没有被医生转接走，则取消该次转接，将会话，还给接诊员
						Thread.sleep(60000);
						ConsultSessionForwardRecordsVo sessionForwardRecordsVoLater = consultSessionForwardRecordsService.selectByPrimaryKey(forwardRecordId);
						if(sessionForwardRecordsVoLater.getStatus().equals(ConsultSessionForwardRecordsVo.REACT_TRANSFER_STATUS_WAITING)){
							Long sessionId = sessionForwardRecordsVoLater.getConversationId();
							RichConsultSession session = sessionRedisCache.getConsultSessionBySessionId(Integer.parseInt(String.valueOf(sessionId)));
							ConsultSessionForwardRecordsVo forwardRecord = new ConsultSessionForwardRecordsVo();
							forwardRecord.setConversationId(sessionId);
							forwardRecord.setFromUserId(session.getCsUserId());
							forwardRecord.setToUserId(sessionForwardRecordsVoLater.getToUserId());
							forwardRecord.setStatus(ConsultSessionForwardRecordsVo.REACT_TRANSFER_STATUS_CANCELLED);
							int count = consultSessionForwardRecordsService.cancelTransfer(forwardRecord);
							if(count > 0) {
								//通知医生，转接取消
								Channel channelToCsUser = userChannelMapping.get(sessionForwardRecordsVoLater.getToUserId());
								jsonObj.clear();
								jsonObj.put("type", "4");
								jsonObj.put("notifyType", "0012");
								jsonObj.put("session", session);
								TextWebSocketFrame frame1 = new TextWebSocketFrame(jsonObj.toJSONString());
								channelToCsUser.writeAndFlush(frame1.retain());

								//通知接诊员，退回此次转接
								Channel channelFromCsUser = userChannelMapping.get(sessionForwardRecordsVoLater.getFromUserId());
								jsonObj.clear();
								jsonObj.put("type", "4");
								jsonObj.put("notifyType", "0014");
								jsonObj.put("session", session);
								TextWebSocketFrame frame2 = new TextWebSocketFrame(jsonObj.toJSONString());
								channelFromCsUser.writeAndFlush(frame2.retain());
							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public void react2Transfer(Integer sessionId, Integer forwardRecordId, String toCsUserId, String toCsUserName, String operation){
		RichConsultSession session = sessionRedisCache.getConsultSessionBySessionId(sessionId);
		if(session!=null){
			String fromCsUserId = session.getCsUserId();
			session.setCsUserId(toCsUserId);
			session.setCsUserName(toCsUserName);
			Channel channelFromCsUser = userChannelMapping.get(fromCsUserId);
			if(channelFromCsUser.isActive()){
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("type", "4");
				jsonObj.put("notifyType", "0010");
				jsonObj.put("operation", operation);
				jsonObj.put("session", session);
				TextWebSocketFrame frame = new TextWebSocketFrame(jsonObj.toJSONString());
				channelFromCsUser.writeAndFlush(frame.retain());

				ConsultSessionForwardRecordsVo forwardRecord = consultSessionForwardRecordsService.selectByPrimaryKey(forwardRecordId.longValue());

				if(ConsultSessionForwardRecordsVo.REACT_TRANSFER_OPERATION_ACCEPT.equalsIgnoreCase(operation)){
					if(session!=null){
						sessionRedisCache.putSessionIdConsultSessionPair(sessionId, session);

						ConsultSession consultSession = new ConsultSession();
						consultSession.setId(sessionId);
						consultSession.setCsUserId(session.getCsUserId());
						consultSessionService.updateSessionInfo(consultSession);
					}
					forwardRecord.setStatus(ConsultSessionForwardRecordsVo.REACT_TRANSFER_STATUS_ACCEPT);
					consultSessionForwardRecordsService.updateAcceptedTransfer(forwardRecord);
				} else {
					forwardRecord.setStatus(ConsultSessionForwardRecordsVo.REACT_TRANSFER_STATUS_REJECT);
					consultSessionForwardRecordsService.updateRejectedTransfer(forwardRecord);
				}
			}
		}

	}



	public  void  putSessionIdConsultSessionPair(Integer sessionId,RichConsultSession session){
		sessionRedisCache.putSessionIdConsultSessionPair(sessionId, session);
	}

	public void putUserIdSessionIdPair(String userId, Integer sessionId) {
		sessionRedisCache.putUserIdSessionIdPair(userId,sessionId);
	}


	public void cancelTransferringSession(Integer sessionId, String toCsUserId, String remark){
		RichConsultSession session = sessionRedisCache.getConsultSessionBySessionId(sessionId);
		ConsultSessionForwardRecordsVo forwardRecord = new ConsultSessionForwardRecordsVo();
		forwardRecord.setConversationId(sessionId.longValue());
		forwardRecord.setFromUserId(session.getCsUserId());
		forwardRecord.setToUserId(toCsUserId);
		forwardRecord.setStatus(ConsultSessionForwardRecordsVo.REACT_TRANSFER_STATUS_CANCELLED);
		consultSessionForwardRecordsService.cancelTransfer(forwardRecord);
	}

	public List<String> getOnlineCsList() {
		List<String> userIds = new ArrayList<String>();
		Iterator<Entry<String, Channel>> it = csUserChannelMapping.entrySet().iterator();
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

	public Integer getSessionIdByUser(String userId) {
		return sessionRedisCache.getSessionIdByUserId(userId);
	}

	public List<Object> getCurrentSessions(List<Object> sessionIds) {
		return sessionRedisCache.getConsultSessionsBySessionIds(sessionIds);
	}
	
	public Map<String, Channel> getCsUserChannelMapping() {
		return csUserChannelMapping;
	}

}
