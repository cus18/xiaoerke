package com.cxqm.xiaoerke.modules.consult.service.core;

import java.util.Map;

import com.cxqm.xiaoerke.modules.consult.service.impl.SessionCacheRedisImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
	
	private transient static final Logger log = LoggerFactory.getLogger(TextWebSocketFrameHandler.class);
	
	private SessionCache sessionCache = new SessionCacheRedisImpl();
	
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

		System.out.println(msgMap);
		Integer sessionId = msgMap.get(ConsultSessionManager.KEY_SESSION_ID) == null ? null : (Integer) msgMap.get(ConsultSessionManager.KEY_SESSION_ID);
		int msgType = msgMap.get(ConsultSessionManager.KEY_REQUEST_TYPE)== null ? 0 : (Integer) msgMap.get(ConsultSessionManager.KEY_REQUEST_TYPE);
		
		if(sessionId != null && msgType == 0) {
			RichConsultSession consultSession = sessionCache.getConsultSessionBySessionId(sessionId);
			if(consultSession == null)
				return;
			
			String csUserId = consultSession.getCsUserId();
			Channel csChannel = ConsultSessionManager.getSessionManager().getUserChannelMapping().get(csUserId);
			if(channel != csChannel && csChannel != null) {
				csChannel.writeAndFlush(msg.retain());
			} else {
				String userId = consultSession.getUserId();
				Channel userChannel = ConsultSessionManager.getSessionManager().getUserChannelMapping().get(userId);
				userChannel.writeAndFlush(msg.retain());
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
