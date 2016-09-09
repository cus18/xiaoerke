package com.cxqm.xiaoerke.modules.consult.service.core;

import java.io.RandomAccessFile;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	
	private final String wsUri;
	
	public HttpRequestHandler(String wsUri) {
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
			ConsultSessionManager.INSTANCE.createSocket(ctx, url);
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