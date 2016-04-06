package com.cxqm.xiaoerke.webapp.webSocket;

import com.cxqm.xiaoerke.modules.consult.service.core.SessionCache;
import com.cxqm.xiaoerke.modules.consult.service.impl.SessionCacheRedisImpl;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Map;

/**
 *
 *
 * 类型解释：Spring启动完成后执行初始化操作
 * 类型表述：预读某些实体的Key-Value，放入map，方便以后使用
 * @author
 * @version
 *
 */
@Component("initialServlet")
public class InitialServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private SessionCache sessionCache = new SessionCacheRedisImpl();

	private static String CLIENT_SERVER_ID = "keeperServerFirst";

	@Autowired
	private SystemService systemService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InitialServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		Session wechatSession = sessionCache.getWechatSessionByClientServerId(CLIENT_SERVER_ID);
		if (wechatSession == null) {
			MyClientApp client = new MyClientApp();
			client.start();
		}
	}

	public class MyClientApp {

		public Session session;

		protected void start() {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			String uri = "ws://120.25.161.33:2048/ws&wechatUser&" + CLIENT_SERVER_ID;
			System.out.println("Connecting to" + uri);
			try {
				session = container.connectToServer(MyClient.class, URI.create(uri));
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
		}

		@OnError
		public void onError(Throwable t) {
//			sessionCache.removeWechatSessionPair(xmlEntity.getFromUserName());
			t.printStackTrace();
		}
	}

}
