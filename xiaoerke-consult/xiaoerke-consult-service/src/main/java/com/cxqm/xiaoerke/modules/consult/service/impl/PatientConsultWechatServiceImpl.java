package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.modules.consult.service.PatientConsultWechatService;
import com.cxqm.xiaoerke.modules.consult.service.core.SessionCache;
import com.cxqm.xiaoerke.modules.sys.entity.ReceiveXmlEntity;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import javax.websocket.*;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Transactional(readOnly = false)
public class PatientConsultWechatServiceImpl implements PatientConsultWechatService {

	@Autowired
	private SystemService systemService;

	private SessionCache sessionCache = new SessionCacheRedisImpl();

	private static boolean sessionCloseFlag = false;

	private static ExecutorService threadExecutor = Executors.newCachedThreadPool();

	@Override
	public void patientWechatConsultService(ReceiveXmlEntity xmlEntity) {
		System.out.println(xmlEntity);
		Runnable thread = new patientConsultWechatServiceThread(xmlEntity);
		threadExecutor.execute(thread);
	}

	public class MyClientApp {

		public Session session;

		private ReceiveXmlEntity xmlEntityApp;

		public MyClientApp(ReceiveXmlEntity xmlEntity) {
			this.xmlEntityApp = xmlEntity;
		}

		protected void start() {
			System.out.println(xmlEntityApp.getFromUserName());
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			String uri = "ws://120.25.161.33:2048/ws&wechatUser&" + xmlEntityApp.getFromUserName();
			System.out.println("Connecting to" + uri);
			try {
				session = container.connectToServer(MyClient.class, URI.create(uri));
				if (session != null) {
					sessionCache.putWechatSessionByOpenId(xmlEntityApp.getFromUserName(), session);
				}
			} catch (DeploymentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@ClientEndpoint
	public class MyClient {
		@OnOpen
		public void onOpen(Session session) {
			System.out.println("Connected to endpoint:" + session.getBasicRemote());
			Map parameter = systemService.getWechatParameter();
			String token = (String) parameter.get("token");
			String st = "尊敬的用户，正在帮您接通医生....";
//			WechatUtil.senMsgToWechat(token, xmlEntity.getFromUserName(), st);
		}

		@OnMessage
		public void onMessage(String message) {
			Map parameter = systemService.getWechatParameter();
			String token = (String) parameter.get("token");

			JSONObject messageObj = new JSONObject(message);
			String type = messageObj.getString("type");

//			WechatUtil.senMsgToWechat(token, xmlEntity.getFromUserName(), message);
//			System.out.println("receive Message from" + xmlEntity.getFromUserName() + "===" + message);
		}

		@OnClose
		public void onClose(Session session, CloseReason closeReason) {
//			sessionCache.removeWechatSessionPair(xmlEntity.getFromUserName());
			sessionCloseFlag = true;
		}

		@OnError
		public void onError(Throwable t) {
//			sessionCache.removeWechatSessionPair(xmlEntity.getFromUserName());
			sessionCloseFlag = true;
			t.printStackTrace();
		}
	}

	public class patientConsultWechatServiceThread extends Thread {

		private ReceiveXmlEntity xmlEntity;

		public patientConsultWechatServiceThread(ReceiveXmlEntity xmlEntity) {
			this.xmlEntity = xmlEntity;
		}

		public void run() {
			/**根据openId，在redis中查询，此用户是否目前还存在有效的session，
			 * 如果没有，则推送欢迎词，建立与在线接诊员的会话联系
			 * */
			synchronized(this){
				Session wechatSession = sessionCache.getWechatSessionByOpenId(xmlEntity.getFromUserName());
				if (wechatSession == null) {
					MyClientApp client = new MyClientApp(xmlEntity);
					client.start();

					try {
						//此处保证开启的线程不死
						do {
							sleep(100);
						} while (!sessionCloseFlag);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				} else {
					try {
						wechatSession.getBasicRemote().sendText(xmlEntity.getContent());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}


}
