package com.cxqm.xiaoerke.webapp.bootstrap;

import com.cxqm.xiaoerke.webapp.handler.ChatServerInitializer;
import com.cxqm.xiaoerke.webapp.handler.RpcServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class RpcServer {
	private final EventLoopGroup workerGroup = new NioEventLoopGroup();
	
	private Channel channel;
	
	public ChannelFuture start(InetSocketAddress address){
		ServerBootstrap boot = new ServerBootstrap();
		boot.group(workerGroup).channel(NioServerSocketChannel.class).childHandler(createInitializer())
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true);;

        //绑定端口
		ChannelFuture f = boot.bind(address).syncUninterruptibly();
		channel = f.channel();
		return f;
	}

	protected ChannelHandler createInitializer() {
		return new RpcServerInitializer();
	}
	
	public void destroy(){
		if(channel != null)
			channel.close();
		workerGroup.shutdownGracefully();
	}
	
	public static void main(String[] args) {
		final RpcServer server = new RpcServer();
		ChannelFuture f = server.start(new InetSocketAddress(2049));
		System.out.println("server start................");
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				server.destroy();
			}
		});
		f.channel().closeFuture().syncUninterruptibly();
	}
	
}
