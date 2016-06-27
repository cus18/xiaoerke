package com.cxqm.xiaoerke.bdfApp.core;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConsultSessionManager_App {

    //<userId or cs-userId, Channel>
    public final Map<String, Channel> userChannelMap = new ConcurrentHashMap<String, Channel>();
    //<userId or cs-userId, Channel>
    public final Map<Channel, String> channelUserMap = new ConcurrentHashMap<Channel, String>();

//    private SystemService systemService = SpringContextHolder.getBean("systemService");

//    private UserInfoServiceImpl userInfoService = SpringContextHolder.getBean("userInfoServiceImpl");
//
//    private static ExecutorService threadExecutor = Executors.newCachedThreadPool();

    private static ConsultSessionManager_App sessionManager = new ConsultSessionManager_App();

    private ConsultSessionManager_App() {

    }

    public static ConsultSessionManager_App getSessionManager() {
        return sessionManager;
    }

    void createSocket(ChannelHandlerContext ctx, String url) {

        Channel channel = ctx.channel();

        String[] args = url.split("&");
        String userId = args[1];
        userChannelMap.put(userId, channel);
        channelUserMap.put(channel,userId);
    }

    public Map<String, Channel> getUserChannelMap() {
        return userChannelMap;
    }

    public Map<Channel, String> getChannelUserMap() {
        return channelUserMap;
    }
}
