package com.cxqm.xiaoerke.webapp.bootstrap;

import com.cxqm.xiaoerke.webapp.handler.ChatServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class ChatServer {
	private final EventLoopGroup workerGroup = new NioEventLoopGroup();
	
	private Channel channel;
	
	public ChannelFuture start(InetSocketAddress address){
		ServerBootstrap boot = new ServerBootstrap();
		boot.group(workerGroup).channel(NioServerSocketChannel.class).childHandler(createInitializer());

        //绑定端口
		ChannelFuture f = boot.bind(address).syncUninterruptibly();
		channel = f.channel();
		return f;
	}

	protected ChannelHandler createInitializer() {
		return new ChatServerInitializer();
	}
	
	public void destroy(){
		if(channel != null)
			channel.close();
		workerGroup.shutdownGracefully();
	}
	
	public static void main(String[] args) {
		final ChatServer server = new ChatServer();
		ChannelFuture f = server.start(new InetSocketAddress(2048));
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
