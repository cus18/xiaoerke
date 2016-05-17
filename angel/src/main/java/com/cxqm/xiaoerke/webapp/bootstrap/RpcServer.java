package com.cxqm.xiaoerke.webapp.bootstrap;

import com.cxqm.xiaoerke.modules.consult.entity.RpcRequest;
import com.cxqm.xiaoerke.modules.consult.entity.RpcResponse;
import com.cxqm.xiaoerke.modules.consult.service.RpcService;
import com.cxqm.xiaoerke.modules.consult.service.core.RpcHandler;
import com.cxqm.xiaoerke.modules.consult.service.util.RpcDecoder;
import com.cxqm.xiaoerke.modules.consult.service.util.RpcEncoder;
import com.cxqm.xiaoerke.webapp.handler.ChatServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.collections.MapUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.imageio.spi.ServiceRegistry;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class RpcServer implements ApplicationContextAware, InitializingBean {

	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(RpcServer.class);

	private String serverAddress;

	public RpcServer(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel channel) throws Exception {
							channel.pipeline()
									.addLast(new RpcDecoder(RpcRequest.class)) // 将 RPC 请求进行解码（为了处理请求）
									.addLast(new RpcEncoder(RpcResponse.class)) // 将 RPC 响应进行编码（为了返回响应）
									.addLast(new RpcHandler()); // 处理 RPC 请求
						}
					})
					.option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);

			String[] array = serverAddress.split(":");
			String host = array[0];
			int port = Integer.parseInt(array[1]);

			ChannelFuture future = bootstrap.bind(host, port).sync();

			future.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

}
