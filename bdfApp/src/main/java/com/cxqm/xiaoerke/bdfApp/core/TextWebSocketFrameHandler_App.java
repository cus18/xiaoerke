package com.cxqm.xiaoerke.bdfApp.core;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.cxqm.xiaoerke.common.utils.ConstantUtil;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.consult.service.core.ConsultSessionManager;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.task.service.ScheduleTaskService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TextWebSocketFrameHandler_App extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private transient static final Logger log = LoggerFactory.getLogger(TextWebSocketFrameHandler_App.class);

    private SessionRedisCache sessionRedisCache = SpringContextHolder.getBean("sessionRedisCacheImpl");

    private ConsultRecordService consultRecordService = SpringContextHolder.getBean("consultRecordServiceImpl");

    private ConsultSessionService consultSessionService = SpringContextHolder.getBean("consultSessionServiceImpl");

    private PatientRegisterPraiseService patientRegisterPraiseService = SpringContextHolder.getBean("patientRegisterPraiseServiceImpl");

    private ScheduleTaskService scheduleTaskService = SpringContextHolder.getBean("scheduleTaskServiceImpl");



    private static ExecutorService threadExecutor = Executors.newSingleThreadExecutor();

    public TextWebSocketFrameHandler_App() {
        super();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            ctx.pipeline().remove(com.cxqm.xiaoerke.bdfApp.core.HttpRequestHandler_App.class);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                TextWebSocketFrame msg) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(msg.text());
        System.out.println(channel);
        Map<String, Object> msgMap = null;
        msgMap = (Map<String, Object>) JSON.parse(msg.text());
        String userId = (String)msgMap.get("userId");
        ConsultSessionManager_App consultSessionManager_App = ConsultSessionManager_App.getSessionManager();
        Map<String, Channel> userChannelMapping= ConsultSessionManager_App.getSessionManager().getUserChannelMapping();
        Channel userChannel = userChannelMapping.get(userId);

        Map<String,Object> replyMap = new HashMap<String, Object>();
        replyMap.put("reply_content","哈哈,小样,我已经收到你的消息了!");
        replyMap.put("reply_doctor","宝大夫的专业医生");
        TextWebSocketFrame csUserMsg = new TextWebSocketFrame(JSONUtils.toJSONString(replyMap));
        userChannel.writeAndFlush(csUserMsg.retain());

        //发送post请求到angel(误删)
        //Runnable thread = new processConsultMessageThread(msgMap);
        //threadExecutor.execute(thread);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        this.channelInactive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("enter channelInactive()");
        String userId = ConsultSessionManager.INSTANCE.getChannelUserMapping().get(ctx.channel());
        ConsultSessionManager.INSTANCE.getChannelUserMapping().remove(ctx.channel());
        if (userId != null) {
            ConsultSessionManager.INSTANCE.getUserChannelMapping().remove(userId);
            //ConsultSessionManager.INSTANCE.getCsUserChannelMapping().remove(userId);
        }
        log.info("finish channelInactive()");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
        cause.printStackTrace();
    }


    public class processConsultMessageThread extends Thread {
        private Map<String,Object> msg;

        public processConsultMessageThread(Map<String,Object> msg) {
            this.msg = msg;
        }

        public void run() {

            try {
                System.out.println(msg.get("content").toString());
                if(msg.get("messageType").toString().equals("text")){
                    this.sendPost(ConstantUtil.ANGEL_WEB_URL + "angel/consult/wechat/conversation",
                        "openId=" + msg.get("userId").toString() +
                            "&messageType=" + msg.get("messageType").toString() +
                            "&messageContent=" + URLEncoder.encode(msg.get("content").toString(), "UTF-8"));
                }else{
                    this.sendPost(ConstantUtil.ANGEL_WEB_URL + "angel/consult/wechat/conversation",
                        "openId=" + msg.get("userId").toString() +
                            "&messageType=" + msg.get("messageType").toString() +
                            "&mediaId=" + msg.get("mediaId").toString());
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        /**
         * 向指定 URL 发送POST方法的请求
         *
         * @param url
         *            发送请求的 URL
         * @param param
         *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
         * @return 所代表远程资源的响应结果
         */
        public String sendPost(String url, String param) {
            PrintWriter out = null;
            BufferedReader in = null;
            String result = "";
            try {
                URL realUrl = new URL(url);
                // 打开和URL之间的连接
                URLConnection conn = realUrl.openConnection();
                // 设置通用的请求属性
                conn.setRequestProperty("accept", "*/*");
                conn.setRequestProperty("connection", "Keep-Alive");
                conn.setRequestProperty("user-agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                // 发送POST请求必须设置如下两行
                conn.setDoOutput(true);
                conn.setDoInput(true);
                // 获取URLConnection对象对应的输出流
                out = new PrintWriter(conn.getOutputStream());
                // 发送请求参数
                out.print(param);
                // flush输出流的缓冲
                out.flush();
                // 定义BufferedReader输入流来读取URL的响应
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            } catch (Exception e) {
                System.out.println("发送 POST 请求出现异常！"+e);
                e.printStackTrace();
            }
            //使用finally块来关闭输出流、输入流
            finally{
                try{
                    if(out!=null){
                        out.close();
                    }
                    if(in!=null){
                        in.close();
                    }
                }
                catch(IOException ex){
                    ex.printStackTrace();
                }
            }
            return result;
        }
    }

}


