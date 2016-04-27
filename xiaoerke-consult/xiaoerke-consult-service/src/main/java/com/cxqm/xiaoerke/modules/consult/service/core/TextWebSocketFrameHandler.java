package com.cxqm.xiaoerke.modules.consult.service.core;

import java.util.Map;

import com.cxqm.xiaoerke.common.utils.ConstantUtil;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.springframework.beans.factory.annotation.Autowired;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
	
	private transient static final Logger log = LoggerFactory.getLogger(TextWebSocketFrameHandler.class);

	private SessionRedisCache sessionRedisCache = SpringContextHolder.getBean("sessionRedisCacheImpl");

	@Autowired
	private ConsultRecordService consultRecordService = SpringContextHolder.getBean("consultRecordServiceImpl");

	@Autowired
	private WechatAttentionService wechatAttentionService = SpringContextHolder.getBean("wechatAttentionServiceImpl");

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
		
		if(sessionId != null ) {
			RichConsultSession consultSession = sessionRedisCache.getConsultSessionBySessionId(sessionId);
			if(consultSession == null)
				return;
			String csUserId = consultSession.getCsUserId();
			String csUserName = consultSession.getCsUserName();
			String userId = consultSession.getUserId();
			Channel csChannel = ConsultSessionManager.getSessionManager().getUserChannelMapping().get(csUserId);
			if(msgType == 0){   //发送文字消息
				if(channel != csChannel && csChannel != null) {
					msgMap.put("csUserId",csUserId);
					msgMap.put("csUserName",csUserName);
					msg = (TextWebSocketFrame) JSON.toJSON(msgMap);
					csChannel.writeAndFlush(msg.retain());
					//保存聊天记录
					consultRecordService.buildRecordMongoVo(userId, String.valueOf(msgType), (String) msgMap.get("content"), consultSession);
				} else {
					if(consultSession.getSource().equals("h5cxqm")){
						Channel userChannel = ConsultSessionManager.getSessionManager().getUserChannelMapping().get(userId);
						userChannel.writeAndFlush(msg.retain());
						//保存聊天记录
						consultRecordService.buildRecordMongoVo(csUserId, String.valueOf(msgType), (String) msgMap.get("content"), consultSession);
					}else if(consultSession.getSource().equals("wxcxqm")){
						String st = (String) msgMap.get(ConsultSessionManager.KEY_CONSULT_CONTENT);
						WechatUtil.senMsgToWechat(ConstantUtil.TEST_TOKEN, consultSession.getUserId(), st);
						//保存聊天记录
						consultRecordService.buildRecordMongoVo(csUserId,String.valueOf(msgType), (String) msgMap.get("content"), consultSession);
					}

				}
			}else if(msgType == 1){  //发送图片消息
				if(channel != csChannel && csChannel != null) {
					msgMap.put("csUserId",csUserId);
					msgMap.put("csUserName",csUserName);
					msg = (TextWebSocketFrame) JSON.toJSON(msgMap);
					csChannel.writeAndFlush(msg.retain());
					//保存聊天记录
					consultRecordService.buildRecordMongoVo(userId, String.valueOf(msgType), (String) msgMap.get("content"), consultSession);
				} else {
					if(consultSession.getSource().equals("h5cxqm")){
						Channel userChannel = ConsultSessionManager.getSessionManager().getUserChannelMapping().get(userId);
						userChannel.writeAndFlush(msg.retain());
						//保存聊天记录
						consultRecordService.buildRecordMongoVo(csUserId, String.valueOf(msgType), (String) msgMap.get("content"), consultSession);
					}else if(consultSession.getSource().equals("wxcxqm")){
						String st = (String) msgMap.get(ConsultSessionManager.KEY_CONSULT_CONTENT);

						WechatUtil.senMsgToWechat(ConstantUtil.TEST_TOKEN, consultSession.getUserId(), st);
						//保存聊天记录
						consultRecordService.buildRecordMongoVo(csUserId,String.valueOf(msgType), (String) msgMap.get("content"), consultSession);
					}

				}
			}else if(msgType == 2){  //发送音频
				if(channel != csChannel && csChannel != null) {
					msgMap.put("csUserId",csUserId);
					msgMap.put("csUserName",csUserName);
					msg = (TextWebSocketFrame) JSON.toJSON(msgMap);
					csChannel.writeAndFlush(msg.retain());
					//保存聊天记录
					consultRecordService.buildRecordMongoVo(userId, String.valueOf(msgType), (String) msgMap.get("content"), consultSession);
				} else {
					if(consultSession.getSource().equals("h5cxqm")){
						Channel userChannel = ConsultSessionManager.getSessionManager().getUserChannelMapping().get(userId);
						userChannel.writeAndFlush(msg.retain());
						//保存聊天记录
						consultRecordService.buildRecordMongoVo(csUserId, String.valueOf(msgType), (String) msgMap.get("content"), consultSession);
					}else if(consultSession.getSource().equals("wxcxqm")){
						String st = (String) msgMap.get(ConsultSessionManager.KEY_CONSULT_CONTENT);
						WechatUtil.senMsgToWechat(ConstantUtil.TEST_TOKEN, consultSession.getUserId(), st);
						//保存聊天记录
						consultRecordService.buildRecordMongoVo(csUserId,String.valueOf(msgType), (String) msgMap.get("content"), consultSession);
					}

				}
			}else{ //发送视频
				if(channel != csChannel && csChannel != null) {
					msgMap.put("csUserId",csUserId);
					msgMap.put("csUserName",csUserName);
					msg = (TextWebSocketFrame) JSON.toJSON(msgMap);
					csChannel.writeAndFlush(msg.retain());
					//保存聊天记录
					consultRecordService.buildRecordMongoVo(userId, String.valueOf(msgType), (String) msgMap.get("content"), consultSession);
				} else {
					if(consultSession.getSource().equals("h5cxqm")){
						Channel userChannel = ConsultSessionManager.getSessionManager().getUserChannelMapping().get(userId);
						userChannel.writeAndFlush(msg.retain());
						//保存聊天记录
						consultRecordService.buildRecordMongoVo(csUserId, String.valueOf(msgType), (String) msgMap.get("content"), consultSession);
					}else if(consultSession.getSource().equals("wxcxqm")){
						String st = (String) msgMap.get(ConsultSessionManager.KEY_CONSULT_CONTENT);
						WechatUtil.senMsgToWechat(ConstantUtil.TEST_TOKEN, consultSession.getUserId(), st);
						//保存聊天记录
						consultRecordService.buildRecordMongoVo(csUserId,String.valueOf(msgType), (String) msgMap.get("content"), consultSession);
					}
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
