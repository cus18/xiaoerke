package com.cxqm.xiaoerke.bdfApp.listener;

import com.cxqm.xiaoerke.bdfApp.bootstrap.ChatServer;
import io.netty.channel.ChannelFuture;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.net.InetSocketAddress;

public class StartupListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		Runnable rn = new Runnable() {
		    public void run() {
		    	final ChatServer chatServer = new ChatServer();
				//ChannelFuture f1 = chatServer.start(new InetSocketAddress(2048));
        ChannelFuture f1 = chatServer.start(new InetSocketAddress(2050));
//				final RpcServer rpcServer = new RpcServer();
//				ChannelFuture f2 = rpcServer.start(new InetSocketAddress(2049));

				System.out.println("server start................");
				Runtime.getRuntime().addShutdownHook(new Thread() {
					@Override
					public void run() {
						chatServer.destroy();
//						rpcServer.destroy();
					}
				});
				f1.channel().closeFuture().syncUninterruptibly();
//				f2.channel().closeFuture().syncUninterruptibly();
		    }
		};
		
		new Thread(rn).start();
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

}
