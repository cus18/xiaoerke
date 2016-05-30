package com.cxqm.xiaoerke.modules.consult.service.core;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
	
	private transient static final Logger log = LoggerFactory.getLogger(TextWebSocketFrameHandler.class);

	private SessionRedisCache sessionRedisCache = SpringContextHolder.getBean("sessionRedisCacheImpl");

	@Autowired
	private ConsultRecordService consultRecordService = SpringContextHolder.getBean("consultRecordServiceImpl");

	public TextWebSocketFrameHandler() {
		super();
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if(evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE){
			ctx.pipeline().remove(HttpRequestHandler.class);
		}else{
			super.userEventTriggered(ctx, evt);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void channelRead0(ChannelHandlerContext ctx,
			TextWebSocketFrame msg) throws Exception {
		String msgText = msg.text();
		Channel channel = ctx.channel();
		
		Map<String, Object> msgMap = null;
		
		try {
			msgMap = (Map<String, Object>) JSON.parse(msgText);
		} catch (JSONException ex) {
			log.info("Parse json error: " + ex.getMessage() + " : " + msgText);
			return;
		}

		Integer sessionId = msgMap.get(ConsultSessionManager.KEY_SESSION_ID) == null ?
				null : (Integer) msgMap.get(ConsultSessionManager.KEY_SESSION_ID);
		int msgType = msgMap.get(ConsultSessionManager.KEY_REQUEST_TYPE)== null ?
				0 : (Integer) msgMap.get(ConsultSessionManager.KEY_REQUEST_TYPE);

		if(msgType == 5){
			String csUserId = (String) msgMap.get("csUserId");
			//来的是医生心跳消息，回心跳确认消息给医生
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("type", "5");
			Channel csChannel = ConsultSessionManager.getSessionManager().getUserChannelMapping().get(csUserId);
			TextWebSocketFrame heartBeatCsUser = new TextWebSocketFrame(jsonObj.toJSONString());
			csChannel.writeAndFlush(heartBeatCsUser.retain());
			return;
		}

		Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
		if(sessionId != null ) {
			RichConsultSession richConsultSession = sessionRedisCache.getConsultSessionBySessionId(sessionId);
			if(richConsultSession == null)
				return;
			String csUserId = richConsultSession.getCsUserId();
			String userId = richConsultSession.getUserId();
			Channel csChannel = ConsultSessionManager.getSessionManager().getUserChannelMapping().get(csUserId);

			if(channel != csChannel && csChannel != null) {
				csChannel.writeAndFlush(msg.retain());
				//保存聊天记录
				consultRecordService.buildRecordMongoVo(userId, String.valueOf(msgType), (String) msgMap.get("content"), richConsultSession);

			} else {
				if(richConsultSession.getSource().equals("h5cxqm")){
					Channel userChannel = ConsultSessionManager.getSessionManager().getUserChannelMapping().get(userId);
					userChannel.writeAndFlush(msg.retain());
				}else if(richConsultSession.getSource().equals("wxcxqm")){
					if(msgType==0){
						//直接发送文本消息
						String st = (String) msgMap.get(ConsultSessionManager.KEY_CONSULT_CONTENT);
						WechatUtil.sendMsgToWechat((String) userWechatParam.get("token"), richConsultSession.getUserId(), st);
					}else if(msgType!=0){
						//发送多媒体消息
						String noTextMsg = (String) msgMap.get("wscontent");
						WechatUtil.sendNoTextMsgToWechat((String) userWechatParam.get("token"),richConsultSession.getUserId(),noTextMsg,msgType);
					}
				}
				//保存聊天记录
				consultRecordService.buildRecordMongoVo(csUserId, String.valueOf(msgType), (String) msgMap.get("content"), richConsultSession);
			}
			//更新会话操作时间
			consultRecordService.saveConsultSessionStatus(richConsultSession);
		}else if(sessionId==null){
			//如果sessionId为空，首先看，消息，是不是从一个用户的H5channel过来的
			if(msgMap.get("source").equals("h5cxqmUser") && msgMap.get("senderId")!=null) {
				RichConsultSession consultSession = ConsultSessionManager.getSessionManager().
						createUserH5ConsultSession((String) msgMap.get("senderId"),channel,"h5cxqm");

				//保存聊天记录
				if(consultSession!=null){
					//将用户发过来的第一条消息，推送给分配好的接诊员，或者医生
					msgMap.put("sessionId",consultSession.getId());
					msgMap.put("serverAddress",consultSession.getServerAddress());
					Channel csChannel = ConsultSessionManager.getSessionManager().getUserChannelMapping().get(consultSession.getCsUserId());
					TextWebSocketFrame csUserMsg = new TextWebSocketFrame(JSONUtils.toJSONString(msgMap));
					csChannel.writeAndFlush(csUserMsg.retain());
					consultRecordService.buildRecordMongoVo((String) msgMap.get("senderId"), String.valueOf(msgType),
							(String) msgMap.get("content"), consultSession);
				}

			}
		}
	}


	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		this.channelInactive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.info("enter channelInactive()");
		String userId = ConsultSessionManager.getSessionManager().getChannelUserMapping().get(ctx.channel());
		ConsultSessionManager.getSessionManager().getChannelUserMapping().remove(ctx.channel());
		if(userId != null) {
			ConsultSessionManager.getSessionManager().getUserChannelMapping().remove(userId);
			ConsultSessionManager.getSessionManager().getCsUserChannelMapping().remove(userId);
		}
		log.info("finish channelInactive()");
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		ctx.close();
		cause.printStackTrace();
	}
	
	public static void main(String[] args){
	}
	
}
