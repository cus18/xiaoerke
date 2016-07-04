package com.cxqm.xiaoerke.bdfApp.core;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConsultSessionManager_App {

    //<userId or cs-userId, Channel>
    public final Map<String, Channel> userChannelMapping = new ConcurrentHashMap<String, Channel>();
    //<userId or cs-userId, Channel>
    public final Map<Channel, String> channelUserMapping = new ConcurrentHashMap<Channel, String>();

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

        //Channel channel = ctx.channel();
        //
        //String[] args = url.split("&");
        //String userId = args[2];
        //userChannelMap.put(userId, channel);
        //channelUserMap.put(channel,userId);

        Channel channel = ctx.channel();

        String[] args = url.split("&");
        String fromType = args[1];


        if (args.length > 2) {
            if (fromType.equals("user")) {
                String userId = args[2];
                String source = args[3];
                doCreateSocketInitiatedByUser(userId, channel);
            }
        }
    }

    private void doCreateSocketInitiatedByUser(String userId, Channel channel) {
        userChannelMapping.put(userId, channel);
        channelUserMapping.put(channel, userId);
    }

    public Map<String, Channel> getUserChannelMapping() {
        return userChannelMapping;
    }

    public Map<Channel, String> getChannelUserMapping() {
        return channelUserMapping;
    }
}
