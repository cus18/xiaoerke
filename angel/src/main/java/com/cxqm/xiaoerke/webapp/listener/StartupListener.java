package com.cxqm.xiaoerke.webapp.listener;

import io.netty.channel.ChannelFuture;

import java.net.InetSocketAddress;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.cxqm.xiaoerke.webapp.bootstrap.ChatServer;

public class StartupListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		Runnable rn = new Runnable() {
		    public void run() {
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
		};
		
		new Thread(rn).start();
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

}
