package com.cxqm.xiaoerke.bdfApp.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

public class HttpRequestHandler_App extends SimpleChannelInboundHandler<FullHttpRequest> {
	
	private final String wsUri;
	
	public HttpRequestHandler_App(String wsUri) {
		super();
		this.wsUri = wsUri;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg)
			throws Exception {
		ctx.fireChannelInactive();
		
		if(wsUri.equalsIgnoreCase(msg.getUri())){
			ctx.fireChannelRead(msg.retain());
		}
		
		String url = msg.getUri();
		if(url.startsWith(wsUri)){
			msg.setUri(wsUri);
			ConsultSessionManager_App.getSessionManager().createSocket(ctx, url);
			ctx.fireChannelRead(msg.retain());
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		ctx.close();
		cause.printStackTrace(System.err);
	}

}