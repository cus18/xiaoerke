package com.cxqm.xiaoerke.modules.consult.service.core;

import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.sys.entity.ReceiveXmlEntity;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class patientConsultWechatServiceThread extends Thread{

	@Autowired
	private SystemService systemService;

	@Autowired
	private ConsultSessionService consultSessionService;

	@Autowired
	private SessionCache sessionCache;

	private ReceiveXmlEntity xmlEntity;

	private static boolean sessionCloseFlag = false;

	public patientConsultWechatServiceThread(ReceiveXmlEntity xmlEntity) {
		this.xmlEntity = xmlEntity;
	}

	public void run() {
		/**根据openId，在redis中查询，此用户是否目前还存在有效的session，
		 * 如果没有，则推送欢迎词，建立与在线接诊员的会话联系
		 * */
		Session wechatSession = sessionCache.getWechatSessionByOpenId(xmlEntity.getFromUserName());
		if(wechatSession == null){
			MyClientApp client = new MyClientApp();
			client.start();

			try {
				//此处保证开启的线程不死
				do{
					sleep(100);
				}while(!sessionCloseFlag);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}else{
			try {
				wechatSession.getBasicRemote().sendText(xmlEntity.getContent());
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public class MyClientApp {

		public Session session;

		protected void start() {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			String uri = "ws://120.25.161.33:2048/ws&wechatUser&" + xmlEntity.getFromUserName();
			System.out.println("Connecting to" + uri);
			try {
				session = container.connectToServer(MyClient.class, URI.create(uri));
				if(session!=null){
					sessionCache.putWechatSessionByOpenId(xmlEntity.getFromUserName(),session);
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
			System.out.println("Connected to endpoint:"+ session.getBasicRemote());
			Map parameter = systemService.getWechatParameter();
			String token = (String) parameter.get("token");
			String st = "尊敬的用户，正在帮您接通医生....";
			WechatUtil.senMsgToWechat(token, xmlEntity.getFromUserName(), st);
		}

		@OnMessage
		public void onMessage(String message) {
			Map parameter = systemService.getWechatParameter();
			String token = (String) parameter.get("token");
			WechatUtil.senMsgToWechat(token, xmlEntity.getFromUserName(), message);
			System.out.println("receive Message from" + xmlEntity.getFromUserName() + "===" + message);
		}

		@OnClose
		public void onClose(Session session, CloseReason closeReason) {
			sessionCache.removeWechatSessionPair(xmlEntity.getFromUserName());
			sessionCloseFlag = true;
		}

		@OnError
		public void onError(Throwable t) {
			sessionCache.removeWechatSessionPair(xmlEntity.getFromUserName());
			sessionCloseFlag = true;
			t.printStackTrace();
		}
	}

}
