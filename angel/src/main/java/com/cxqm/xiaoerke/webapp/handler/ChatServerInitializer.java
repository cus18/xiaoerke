package com.cxqm.xiaoerke.webapp.handler;


import com.cxqm.xiaoerke.modules.consult.entity.RpcRequest;
import com.cxqm.xiaoerke.modules.consult.entity.RpcResponse;
import com.cxqm.xiaoerke.modules.consult.service.core.HttpRequestHandler;
import com.cxqm.xiaoerke.modules.consult.service.core.RpcHandler;
import com.cxqm.xiaoerke.modules.consult.service.core.TextWebSocketFrameHandler;

import com.cxqm.xiaoerke.modules.consult.service.util.RpcDecoder;
import com.cxqm.xiaoerke.modules.consult.service.util.RpcEncoder;
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

		pipeline.addLast(new RpcDecoder(RpcRequest.class)); // 将 RPC 请求进行解码（为了处理请求）

		pipeline.addLast(new RpcEncoder(RpcResponse.class)); // 将 RPC 响应进行编码（为了返回响应）

		pipeline.addLast(new HttpServerCodec());

		pipeline.addLast(new ChunkedWriteHandler());

		pipeline.addLast(new HttpObjectAggregator(64*1024));
		
		pipeline.addLast(new HttpRequestHandler("/ws"));
		
		pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
		
		pipeline.addLast(new TextWebSocketFrameHandler());

		pipeline.addLast(new RpcHandler()); // 处理 RPC 请求
	}

}
