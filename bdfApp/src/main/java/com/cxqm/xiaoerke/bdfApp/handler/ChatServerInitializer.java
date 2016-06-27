package com.cxqm.xiaoerke.bdfApp.handler;


import com.cxqm.xiaoerke.bdfApp.core.HttpRequestHandler_App;
import com.cxqm.xiaoerke.bdfApp.core.TextWebSocketFrameHandler_App;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ChatServerInitializer extends ChannelInitializer<Channel> {

	public ChatServerInitializer() {
		super();
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		pipeline.addLast(new HttpServerCodec());

		pipeline.addLast(new ChunkedWriteHandler());

		pipeline.addLast(new HttpObjectAggregator(64*1024));
		
		pipeline.addLast(new HttpRequestHandler_App("/ws"));
		
		pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
		
		pipeline.addLast(new TextWebSocketFrameHandler_App());

	}

}
