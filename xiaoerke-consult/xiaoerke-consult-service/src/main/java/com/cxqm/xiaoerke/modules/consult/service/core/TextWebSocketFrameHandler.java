package com.cxqm.xiaoerke.modules.consult.service.core;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.task.service.ScheduleTaskService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private transient static final Logger log = LoggerFactory.getLogger(TextWebSocketFrameHandler.class);

    private SessionRedisCache sessionRedisCache = SpringContextHolder.getBean("sessionRedisCacheImpl");

    private ConsultRecordService consultRecordService = SpringContextHolder.getBean("consultRecordServiceImpl");

    private ConsultSessionService consultSessionService = SpringContextHolder.getBean("consultSessionServiceImpl");

    private PatientRegisterPraiseService patientRegisterPraiseService = SpringContextHolder.getBean("patientRegisterPraiseServiceImpl");

    private ScheduleTaskService scheduleTaskService = SpringContextHolder.getBean("scheduleTaskServiceImpl");

    public TextWebSocketFrameHandler() {
        super();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            ctx.pipeline().remove(HttpRequestHandler.class);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                TextWebSocketFrame msg) throws Exception {
        String msgText = msg.text();
        Channel channel = ctx.channel();
        Map<String, Object> msgMap = null;

        try {
            msgMap = (Map<String, Object>) JSON.parse(msgText);
        } catch (JSONException ex) {
            log.info("Parse json error: " + ex.getMessage() + " : " + msgText);
            return;
        }

        Integer sessionId = msgMap.get(ConsultSessionManager.KEY_SESSION_ID) == null ?
                null : (Integer) msgMap.get(ConsultSessionManager.KEY_SESSION_ID);
        int msgType = msgMap.get(ConsultSessionManager.KEY_REQUEST_TYPE) == null ?
                0 : (Integer) msgMap.get(ConsultSessionManager.KEY_REQUEST_TYPE);

        if (msgType == 5) {
            String csUserId = (String) msgMap.get("csUserId");
            //来的是医生心跳消息，回心跳确认消息给医生
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("type", "5");
            Channel csChannel = ConsultSessionManager.INSTANCE.getUserChannelMapping().get(csUserId);
            TextWebSocketFrame heartBeatCsUser = new TextWebSocketFrame(jsonObj.toJSONString());
            csChannel.writeAndFlush(heartBeatCsUser.retain());
            return;
        }

        if (msgType == 6) {
            String csUserId = (String) msgMap.get("csUserId");
            Iterator<Map.Entry<String, Date>> it2 = ConsultSessionManager.INSTANCE.getCsUserConnectionTimeMapping().entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry<String, Date> entry = it2.next();
                try{
                    if (csUserId.equals(entry.getKey())) {
                        ConsultSessionManager.INSTANCE.getUserConnectionTimeMapping().put(entry.getKey(), new Date());
                    }
                }catch (Exception e){
                }
            }
            return;
        }

        if(msgType == 7){
            String userId = (String) msgMap.get("userId");
            //来自用户H5的心跳，回心跳确认给用户
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("type", "7");
            Channel csChannel = ConsultSessionManager.INSTANCE.getUserChannelMapping().get(userId);
            TextWebSocketFrame heartBeatCsUser = new TextWebSocketFrame(jsonObj.toJSONString());
            csChannel.writeAndFlush(heartBeatCsUser.retain());
            return;
        }

        if(msgType==8){
            String userId = (String) msgMap.get("userId");
            Iterator<Map.Entry<String, Date>> it2 = ConsultSessionManager.INSTANCE.getCsUserConnectionTimeMapping().entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry<String, Date> entry = it2.next();
                try{
                    if (userId.equals(entry.getKey())) {
                        ConsultSessionManager.INSTANCE.getUserConnectionTimeMapping().put(entry.getKey(), new Date());
                    }
                }catch (Exception e){
                }
            }
            return;
        }

        Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
        if (sessionId != null) {
            RichConsultSession richConsultSession = sessionRedisCache.getConsultSessionBySessionId(sessionId);
            if (richConsultSession == null)
                return;
            String csUserId = richConsultSession.getCsUserId();
            String userId = richConsultSession.getUserId();
            String senderId = (String) msgMap.get("senderId");
            if (senderId.equals(userId)) {
                //如果是用户作为发送者，则发给医生接收
                Channel csChannel = ConsultSessionManager.INSTANCE.getUserChannelMapping().get(csUserId);
                if (csChannel != null && csChannel.isActive()) {
                    csChannel.writeAndFlush(msg.retain());
                    //保存聊天记录
                    consultRecordService.buildRecordMongoVo(userId, String.valueOf(msgType), (String) msgMap.get("content"), richConsultSession);
                }
            } else if (senderId.equals(csUserId)) {
                //如果是医生作为发送者，则用户接收
                if (richConsultSession.getSource().equals("h5cxqm")||richConsultSession.getSource().equals("h5wjy") || richConsultSession.getSource().equals("h5bhq")) {
                    Channel userChannel = ConsultSessionManager.INSTANCE.getUserChannelMapping().get(userId);
                    if (userChannel != null && userChannel.isActive()) {
                        if(richConsultSession.getSource().equals("h5bhq")){
                            Map<String, Date> userConnectionTimeMapping = ConsultSessionManager.INSTANCE.getUserConnectionTimeMapping();
                            Date oldDate = null;
                            if(userConnectionTimeMapping.containsKey(richConsultSession.getUserId())){
                                oldDate = userConnectionTimeMapping.get(richConsultSession.getUserId());
                            }else{
                                oldDate = new Date();
                                userConnectionTimeMapping.put(richConsultSession.getUserId(),oldDate);
                            }
                            Boolean flag = false ;
                            for(int i= 0 ; i<= 1;i++){
                                JSONObject jsonObj = new JSONObject();
                                jsonObj.put("type", "4");
                                jsonObj.put("notifyType", "0100");
                                TextWebSocketFrame frame = new TextWebSocketFrame(jsonObj.toJSONString());
                                userChannel.writeAndFlush(frame.retain());
                                if(i == 1){
                                   Date nowDate =  userConnectionTimeMapping.get(richConsultSession.getUserId());
                                   if(nowDate != null && nowDate != oldDate){
                                       flag = true;
                                   }
                                }else{
                                    userConnectionTimeMapping.put(richConsultSession.getUserId(), new Date());
                                }
                            }
                            if(flag){
                                userChannel.writeAndFlush(msg.retain());
                            }else{
                                userChannel.writeAndFlush(msg.retain());
                                net.sf.json.JSONObject noReadMsg = new net.sf.json.JSONObject();
                                String content = (String) msgMap.get(ConsultSessionManager.KEY_CONSULT_CONTENT);
                                int nameIndex = content.indexOf("：");
                                String newContent = content.substring(nameIndex + 1, content.toCharArray().length);
                                noReadMsg.put("action","doctorMessage");
                                noReadMsg.put("uid",userId);
                                noReadMsg.put("messageType",msgType);
                                noReadMsg.put("messageContent",newContent);
                                noReadMsg.put("doctorId",csUserId);
                                String currentUrl = Global.getConfig("COOP_BHQ_URL");
                                if(StringUtils.isNull(currentUrl)){
                                    currentUrl = "http://coapi.baohuquan.com/baodaifu";
                                }
                                String method = "POST";
                                String dataType="json";
                                String str = CoopConsultUtil.getCurrentUserInfo(currentUrl, method, dataType, null, noReadMsg.toString(), 4);
                                net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(str);
                                if(jsonObject.containsKey("error_code") && (Integer)jsonObject.get("error_code") != 0 ){
                                    CoopConsultUtil.getCurrentUserInfo(currentUrl, method, dataType, null, noReadMsg.toString(), 4);    //一次推送失败后，再推一次
                                }
                            }
                        }else{
                            userChannel.writeAndFlush(msg.retain());
                        }
                    }
                }
                if (richConsultSession.getSource().equals("wxcxqm")) {
                    String sendResult = "";
                    if (msgType == 0) {
                        String content = (String) msgMap.get(ConsultSessionManager.KEY_CONSULT_CONTENT);
                        StringBuilder stringBuilder = new StringBuilder();
                        //根据sessionId查询Evaluation表id
                        //jiangzg 暂时注掉，发消息带评价信息
                        Map praiseParam = new HashMap();
                        praiseParam.put("consultSessionId", richConsultSession.getId());
                        praiseParam.put("doctorId", csUserId);
                        List<Map<String, Object>> praiseList = patientRegisterPraiseService.getCustomerEvaluationListByInfo(praiseParam);
                        if (praiseList != null && praiseList.size() > 0) {
                            int nameIndex = content.indexOf("：");
                            String newContent = content.substring(nameIndex + 1, content.toCharArray().length);
                            if (StringUtils.isNotNull(newContent) && !"\n".equalsIgnoreCase(newContent)) {
                                if (newContent.endsWith("\n")) {
                                    //newContent = newContent.substring(0,newContent.lastIndexOf("\n"));
                                    stringBuilder.append(newContent);
                                } else {
                                    stringBuilder.append(newContent + "\n");
                                    //stringBuilder.append(newContent);
                                }
                            } else {
                                return;
                            }
                            stringBuilder.append("------------------\n");

//                            http://localhost/titan/consultDoctorHome#/consultDoctorHome/00034ads0d764sdsa66a2d6esd0e8ddf
//                            stringBuilder.append("<a href='http://s123.xiaork.com/titan/consultDoctorHome#/consultDoctorHome/"+richConsultSession.getCsUserId()+"'>");
                            stringBuilder.append(content.substring(0, nameIndex));
//                            stringBuilder.append("</a>|");
                            stringBuilder.append("|");

//                            stringBuilder.append("<a href='http://s251.baodf.com/keeper/wxPay/patientPay.do?serviceType=customerPay&customerId=");
                            stringBuilder.append("<a href='http://s123.xiaork.com/keeper/wxPay/patientPay.do?serviceType=customerPay&customerId=");
                            stringBuilder.append(praiseList.get(0).get("id"));
                            stringBuilder.append("&sessionId=");
                            stringBuilder.append(sessionId);
                            stringBuilder.append("'>评价医生</a>|");
                            stringBuilder.append("<a href='http://s123.xiaork.com/keeper/playtour#/playtourShare/6");
                            stringBuilder.append("'>分享</a>");
                            sendResult = WechatUtil.sendMsgToWechat((String) userWechatParam.get("token"), richConsultSession.getUserId(), stringBuilder.toString());
                            //发送消息
                            /*if(StringUtils.isNotNull(content) && !"\n".equalsIgnoreCase(content)){
                                if (content.endsWith("\n")) {
                                    content = content.substring(0,content.lastIndexOf("\n"));
                                    stringBuilder.append(content);
                                } else {
                                    // stringBuilder.append(newContent + "\n");
                                    stringBuilder.append(content);
                                }
                            }else{
                                return ;
                            }
                            sendResult = WechatUtil.sendMsgToWechat((String) userWechatParam.get("token"), richConsultSession.getUserId(), stringBuilder.toString());*/
                            if (sendResult.equals("tokenIsInvalid")) {
                                updateWechatParameter();
                            }else if(!sendResult.equals("messageOk")){
                                Channel csChannel = ConsultSessionManager.INSTANCE.getUserChannelMapping().get(csUserId);
                                JSONObject jsonObj = new JSONObject();
                                jsonObj.put("type", "4");
                                jsonObj.put("notifyType", "0016");
                                TextWebSocketFrame frame = new TextWebSocketFrame(jsonObj.toJSONString());
                                if (csChannel != null && csChannel.isActive()) {
                                    csChannel.writeAndFlush(frame.retain());
                                }
                            }
                        }
                    } else if (msgType != 0) {
                        //发送多媒体消息
                        String noTextMsg = (String) msgMap.get("wscontent");
                        WechatUtil.sendNoTextMsgToWechat((String) userWechatParam.get("token"), richConsultSession.getUserId(), noTextMsg, msgType);
                    }
                }
                //保存聊天记录
                consultRecordService.buildRecordMongoVo(csUserId, String.valueOf(msgType), (String) msgMap.get("content"), richConsultSession);

                if (!(msgMap.get("consultFlag").equals("noFlag"))) {
                    System.out.print("sessionId===" + msgMap.get("sessionId"));
                    ConsultSession consultSessionVO = new ConsultSession();
                    consultSessionVO.setId((Integer) msgMap.get("sessionId"));
                    consultSessionVO.setFlag((String) msgMap.get("consultFlag"));
                    consultSessionService.updateSessionInfo(consultSessionVO);
                }
            }

            //更新会话操作时间
            consultRecordService.saveConsultSessionStatus(richConsultSession);
        } else if (sessionId == null) {
            //如果sessionId为空，首先看，消息，是不是从一个用户的H5channel过来的
            if (msgMap.get("source").equals("h5cxqmUser") && msgMap.get("senderId") != null) {
                RichConsultSession consultSession = ConsultSessionManager.INSTANCE.
                        createUserH5ConsultSession((String) msgMap.get("senderId"), channel, "h5cxqm");

                //保存聊天记录
                if (consultSession != null) {
                    //将用户发过来的第一条消息，推送给分配好的接诊员，或者医生
                    msgMap.put("sessionId", consultSession.getId());
                    msgMap.put("serverAddress", consultSession.getServerAddress());
                    Channel csChannel = ConsultSessionManager.INSTANCE.getUserChannelMapping().get(consultSession.getCsUserId());
                    TextWebSocketFrame csUserMsg = new TextWebSocketFrame(JSONUtils.toJSONString(msgMap));
                    csChannel.writeAndFlush(csUserMsg.retain());
                    consultRecordService.buildRecordMongoVo((String) msgMap.get("senderId"), String.valueOf(msgType),
                            (String) msgMap.get("content"), consultSession);
                }
            }else if(msgMap.get("source").equals("h5wjyUser") && msgMap.get("senderId") != null){
                RichConsultSession consultSession = ConsultSessionManager.INSTANCE.
                        createUserH5ConsultSession((String) msgMap.get("senderId"), channel, "h5wjy");

                //保存聊天记录
                if (consultSession != null) {
                    //将用户发过来的第一条消息，推送给分配好的接诊员，或者医生
                    msgMap.put("sessionId", consultSession.getId());
                    msgMap.put("serverAddress", consultSession.getServerAddress());
                    Channel csChannel = ConsultSessionManager.INSTANCE.getUserChannelMapping().get(consultSession.getCsUserId());
                    TextWebSocketFrame csUserMsg = new TextWebSocketFrame(JSONUtils.toJSONString(msgMap));
                    csChannel.writeAndFlush(csUserMsg.retain());
                    consultRecordService.buildRecordMongoVo((String) msgMap.get("senderId"), String.valueOf(msgType),
                            (String) msgMap.get("content"), consultSession);
                }
            }else if(msgMap.get("source").equals("h5bhqUser") && msgMap.get("senderId") != null){
                RichConsultSession consultSession = ConsultSessionManager.INSTANCE.
                        createUserH5ConsultSession((String) msgMap.get("senderId"), channel, "h5bhq");

                //保存聊天记录
                if (consultSession != null) {
                    //将用户发过来的第一条消息，推送给分配好的接诊员，或者医生
                    msgMap.put("sessionId", consultSession.getId());
                    msgMap.put("serverAddress", consultSession.getServerAddress());
                    Channel csChannel = ConsultSessionManager.INSTANCE.getUserChannelMapping().get(consultSession.getCsUserId());
                    TextWebSocketFrame csUserMsg = new TextWebSocketFrame(JSONUtils.toJSONString(msgMap));
                    csChannel.writeAndFlush(csUserMsg.retain());
                    consultRecordService.buildRecordMongoVo((String) msgMap.get("senderId"), String.valueOf(msgType),
                            (String) msgMap.get("content"), consultSession);
                }
            }
        }
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
            ConsultSessionManager.INSTANCE.getCsUserChannelMapping().remove(userId);
        }
        log.info("finish channelInactive()");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
        cause.printStackTrace();
    }

    public void updateWechatParameter() {
        try {
            System.out.print("用户端微信参数更新");
            String token = WechatUtil.getToken(ConstantUtil.CORPID, ConstantUtil.SECTET);
            String ticket = WechatUtil.getJsapiTicket(token);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("token", token);
            map.put("ticket", ticket);
            map.put("id", "1");
            scheduleTaskService.updateWechatParameter(map);
            sessionRedisCache.putWeChatParamToRedis(map);

            System.out.print("医生端微信参数更新");
            token = WechatUtil.getToken(ConstantUtil.DOCTORCORPID, ConstantUtil.DOCTORSECTET);
            ticket = WechatUtil.getJsapiTicket(token);
            map = new HashMap<String, Object>();
            map.put("token", token);
            map.put("ticket", ticket);
            map.put("id", "2");
            scheduleTaskService.updateWechatParameter(map);
            sessionRedisCache.putWeChatParamToRedis(map);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
