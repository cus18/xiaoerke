package com.cxqm.xiaoerke.modules.consult.service.core;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.modules.consult.entity.*;
import com.cxqm.xiaoerke.modules.consult.service.*;
import com.cxqm.xiaoerke.modules.consult.service.impl.ConsultDoctorInfoServiceImpl;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.task.service.ScheduleTaskService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private transient static final Logger log = LoggerFactory.getLogger(TextWebSocketFrameHandler.class);

    private SessionRedisCache sessionRedisCache = SpringContextHolder.getBean("sessionRedisCacheImpl");

    private ConsultRecordService consultRecordService = SpringContextHolder.getBean("consultRecordServiceImpl");

    private ConsultSessionService consultSessionService = SpringContextHolder.getBean("consultSessionServiceImpl");

    private PatientRegisterPraiseService patientRegisterPraiseService = SpringContextHolder.getBean("patientRegisterPraiseServiceImpl");

    private ScheduleTaskService scheduleTaskService = SpringContextHolder.getBean("scheduleTaskServiceImpl");

    private SysPropertyServiceImpl sysPropertyService = SpringContextHolder.getBean("sysPropertyServiceImpl");

    private ConsultDoctorInfoServiceImpl consultDoctorInfoService  = SpringContextHolder.getBean("consultDoctorInfoServiceImpl");

    private ConsultMemberRedsiCacheService consultMemberRedsiCacheService = SpringContextHolder.getBean("consultMemberRedsiCacheServiceImpl");


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
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
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

        if(msgMap.containsKey("payStatus")){
            if("payFee".equalsIgnoreCase(String.valueOf(msgMap.get("payStatus")))){
                String userId = String.valueOf(msgMap.get("senderId"));
                String userSource = String.valueOf(msgMap.get("source"));
                String sendMsg = "";
                ConsultMemberVo consultMemberVo = consultMemberRedsiCacheService.getConsultMemberInfo(userId);
                if(consultMemberVo == null){  //第一次咨询
                     //List<ConsultSessionStatusVo> consultSessionStatusVoList = consultRecordService.querySessionStatusList(new Query().addCriteria(new Criteria().where("userId").is(userId).and("source").regex(userSource)).with(new Sort(Sort.Direction.DESC, "createDate")));
                    Query query = (new Query()).addCriteria(where("userId").is(userId)).with(new Sort(Sort.Direction.ASC, "createDate"));
                    ConsultSessionStatusVo consultSessionStatusVo = consultRecordService.findOneConsultSessionStatusVo(query);
                    // 推送消息
                    if(consultSessionStatusVo == null){

                    }else{
                        Date nowDate = new Date();
                        long timeLong = nowDate.getTime();
                        if(sessionId != null){
                            if(timeLong - consultSessionStatusVo.getCreateDate().getTime() > 30*60*1000){
                                //咨询时间到，如继续咨询请付费
                                JSONObject jsonObj = new JSONObject();
                                jsonObj.put("type", "4");
                                jsonObj.put("notifyType","7777");
                                TextWebSocketFrame payFeeRemindMsg = new TextWebSocketFrame(jsonObj.toJSONString());
                                channel.writeAndFlush(payFeeRemindMsg.retain());
                                return ;
                            }
                        }else{
                            if(timeLong - consultSessionStatusVo.getCreateDate().getTime() > 30*60*1000){
                                //需付费咨询
                                JSONObject jsonObj = new JSONObject();
                                jsonObj.put("type", "4");
                                jsonObj.put("notifyType","8888");
                                TextWebSocketFrame payFeeRemindMsg = new TextWebSocketFrame(jsonObj.toJSONString());
                                channel.writeAndFlush(payFeeRemindMsg.retain());
                                return ;
                            }
                        }
                    }
                }else{
                    boolean flag = consultMemberRedsiCacheService.cheackMemberTimeOut(userId);
                    if(!flag){ //会员过期提醒
                        Date nowDate = new Date();
                        long timeLong = nowDate.getTime();
                        String memberTime = consultMemberRedsiCacheService.getConsultMember(userId + memberRedisCachVo.MEMBER_END_DATE);
                        long meTime = Long.valueOf(DateUtils.StrToDate(memberTime,"yyyy-MM-dd HH:mm:ss").getTime());
                        if(timeLong > meTime){  //咨询时间到
                            if(sessionId == null){
                                JSONObject jsonObj = new JSONObject();
                                jsonObj.put("type", "4");
                                jsonObj.put("notifyType","8888");
                                TextWebSocketFrame payFeeRemindMsg = new TextWebSocketFrame(jsonObj.toJSONString());
                                channel.writeAndFlush(payFeeRemindMsg.retain());
                                return ;
                            }else{
                                JSONObject jsonObj = new JSONObject();
                                jsonObj.put("type", "4");
                                jsonObj.put("notifyType","7777");
                                TextWebSocketFrame payFeeRemindMsg = new TextWebSocketFrame(jsonObj.toJSONString());
                                channel.writeAndFlush(payFeeRemindMsg.retain());
                                return ;
                            }
                        }
                    }
                }
            }
        }

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

        //sessionId不为空  得良抽取，有问题随时沟通
        if (sessionId != null) {
            RichConsultSession richConsultSession = sessionRedisCache.getConsultSessionBySessionId(sessionId);
            LogUtils.saveLog(String.valueOf(sessionId),"doctorId="+richConsultSession.getCsUserId()+"userId="+richConsultSession.getUserId());
            if (richConsultSession == null)
                return;
            String csUserId = richConsultSession.getCsUserId();
            String userId = richConsultSession.getUserId();
            String senderId = (String) msgMap.get("senderId");
            if (senderId.equals(userId)) {
                //用户发给医生
                userSendToDoctor(msgMap, sessionId, msgType, richConsultSession, csUserId, userId);

            } else if (senderId.equals(csUserId)) {
                //医生发给用户
                if (doctorSendToUser(msg, msgMap, sysPropertyVoWithBLOBsVo, msgType, userWechatParam, richConsultSession, csUserId, userId))
                    return;
            }
            //更新会话操作时间
            consultRecordService.saveConsultSessionStatus(richConsultSession);
        }else if (sessionId == null) { //如果sessionId为空，首先看，消息，是不是从一个用户的H5channel过来的
            LogUtils.saveLog("sessionIdNull",String.valueOf(msgMap.get("source")));
            RichConsultSession consultSession = new RichConsultSession();
            if (msgMap.get("source").equals("h5cxqmUser") && msgMap.get("senderId") != null) {
                consultSession = ConsultSessionManager.INSTANCE.createUserH5ConsultSession((String) msgMap.get("senderId"), channel, "h5cxqm");
            }else if(msgMap.get("source").equals("h5wjyUser") && msgMap.get("senderId") != null){
                consultSession = ConsultSessionManager.INSTANCE.createUserH5ConsultSession((String) msgMap.get("senderId"), channel, "h5wjy");
            }else if(msgMap.get("source").equals("h5bhqUser") && msgMap.get("senderId") != null){
                consultSession = ConsultSessionManager.INSTANCE.createUserH5ConsultSession((String) msgMap.get("senderId"), channel, "h5bhq");
            }else if(msgMap.get("source").equals("h5ykdlUser") && msgMap.get("senderId") != null){
                consultSession = ConsultSessionManager.INSTANCE.createUserH5ConsultSession((String) msgMap.get("senderId"), channel, "h5ykdl");
            }else if(msgMap.get("source").equals("h5mtqUser") && msgMap.get("senderId") != null){
                consultSession = ConsultSessionManager.INSTANCE.createUserH5ConsultSession((String) msgMap.get("senderId"), channel, "h5mtq");
            }else if(msgMap.get("source").equals("h5YZUser") && msgMap.get("senderId") != null){
                consultSession = ConsultSessionManager.INSTANCE.createUserH5ConsultSession((String) msgMap.get("senderId"), channel, "h5YouZan");
            }else if(msgMap.get("source").equals("h5GuoWei") && msgMap.get("senderId") != null){
                consultSession = ConsultSessionManager.INSTANCE.createUserH5ConsultSession((String)msgMap.get("senderId"),channel,"h5GuoWei");
            }
            //保存聊天记录
            if (consultSession != null) {
                //将用户发过来的第一条消息，推送给分配好的接诊员，或者医生
                saveAndSendMessage(msgMap, msgType, consultSession);
            }
        }
    }

    private void saveAndSendMessage(Map<String, Object> msgMap, int msgType, RichConsultSession consultSession) {
        //将用户发过来的第一条消息，推送给分配好的接诊员，或者医生
        msgMap.put("sessionId", consultSession.getId());
        msgMap.put("serverAddress", consultSession.getServerAddress());
        /**
         * jiangzg 2016-9-8 18:23:58 增加新消息数量
         */
        msgMap.put("consultNum", consultSession.getConsultNum());
        Channel csChannel = ConsultSessionManager.INSTANCE.getUserChannelMapping().get(consultSession.getCsUserId());
        TextWebSocketFrame csUserMsg = new TextWebSocketFrame(JSONUtils.toJSONString(msgMap));
        csChannel.writeAndFlush(csUserMsg.retain());
        consultRecordService.buildRecordMongoVo((String) msgMap.get("senderId"), String.valueOf(msgType),
                (String) msgMap.get("content"), consultSession);
    }

    private boolean doctorSendToUser(TextWebSocketFrame msg, Map<String, Object> msgMap, SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo, int msgType, Map userWechatParam, RichConsultSession richConsultSession, String csUserId, String userId) {
        //渠道来源H5
        if (richConsultSession.getSource().startsWith("h5")){
            Channel userChannel = ConsultSessionManager.INSTANCE.getUserChannelMapping().get(userId);
            //channel是活跃的状态
            if (userChannel != null && userChannel.isActive()) {
                if (channelAlive(msg, msgMap, sysPropertyVoWithBLOBsVo, msgType, richConsultSession, csUserId, userId, userChannel))
                    return true;
            }else{ //channel不是活跃的状态
                channelDead(msgMap, sysPropertyVoWithBLOBsVo, msgType, richConsultSession, csUserId, userId);
            }
        }
        //渠道来源WX
        if (richConsultSession.getSource().equals("wxcxqm")) {
            if (sourceFromWX(msgMap, sysPropertyVoWithBLOBsVo, msgType, userWechatParam, richConsultSession, csUserId))
                return true;
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
        return false;
    }

    private void channelDead(Map<String, Object> msgMap, SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo, int msgType, RichConsultSession richConsultSession, String csUserId, String userId) {
        if(richConsultSession.getSource().equals("h5bhq")){
            net.sf.json.JSONObject noReadMsg = new net.sf.json.JSONObject();
            String content = (String) msgMap.get(ConsultSessionManager.KEY_CONSULT_CONTENT);
            int nameIndex = content.indexOf("：");
            String newContent = content.substring(nameIndex + 1, content.toCharArray().length);
            noReadMsg.put("action","doctorMessage");
            noReadMsg.put("uid",userId);
            noReadMsg.put("messageType",msgType);
            noReadMsg.put("messageContent",newContent);
            noReadMsg.put("doctorId",csUserId);
            String currentUrl = sysPropertyVoWithBLOBsVo.getCoopBhqUrl();
            if(StringUtils.isNull(currentUrl)){
                currentUrl = "http://3rd.baohuquan.com:20000/baodaifu";
            }
            String method = "POST";
            String dataType="json";
            String str = CoopConsultUtil.getCurrentUserInfo(currentUrl, method, dataType, null, noReadMsg.toString(), 4);
            net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(str);
            if(jsonObject.containsKey("error_code") && (Integer)jsonObject.get("error_code") != 0 ){
                CoopConsultUtil.getCurrentUserInfo(currentUrl, method, dataType, null, noReadMsg.toString(), 4);    //一次推送失败后，再推一次
            }
        }else if(richConsultSession.getSource().equals("h5ykdl")){
            net.sf.json.JSONObject noReadMsg = new net.sf.json.JSONObject();
            String content = (String) msgMap.get(ConsultSessionManager.KEY_CONSULT_CONTENT);
            int nameIndex = content.indexOf("：");
            String newContent = content.substring(nameIndex + 1, content.toCharArray().length);
            //    noReadMsg.put("action","doctorMessage");
            /*if(content.substring(0,nameIndex).contains("宝大夫")){
                noReadMsg.put("doctorName",content.substring(0,nameIndex));
            }else{
                noReadMsg.put("doctorName","宝大夫"+content.substring(0,nameIndex));
            }*/
            noReadMsg.put("doctorName",content.substring(0,nameIndex));
            noReadMsg.put("uid",userId);
            noReadMsg.put("messageType",msgType);
            noReadMsg.put("messageContent",newContent);
            noReadMsg.put("doctorId",csUserId);
            String currentUrl = sysPropertyVoWithBLOBsVo.getCoopYkdlUrl()+"/consult_interrupted";
            if(StringUtils.isNull(currentUrl)){
                currentUrl = "https://wxsp.ykhys.com/thirdparty/baodaifu/consult_interrupted";
            }
            String method = "POST";
            String dataType="json";
            String str = CoopConsultUtil.getCurrentUserInfo(currentUrl, method, dataType, null, noReadMsg.toString(), 4);
            net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(str);
            if(jsonObject.containsKey("error_msg") && !"success".equals(jsonObject.get("error_msg"))){
                CoopConsultUtil.getCurrentUserInfo(currentUrl, method, dataType, null, noReadMsg.toString(), 4);    //一次推送失败后，再推一次
            }
        }
    }

    private boolean channelAlive(TextWebSocketFrame msg, Map<String, Object> msgMap, SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo, int msgType, RichConsultSession richConsultSession, String csUserId, String userId, Channel userChannel) {
        if(richConsultSession.getSource().equals("h5bhq") || richConsultSession.getSource().equals("h5ykdl") || richConsultSession.getSource().equals("h5mtq")){
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
                if(richConsultSession.getSource().equals("h5ykdl") || richConsultSession.getSource().equals("h5mtq")){
                    if("true".equalsIgnoreCase(messageContentFilter(msgMap))){
                        Channel csChannel = ConsultSessionManager.INSTANCE.getUserChannelMapping().get(csUserId);
                        JSONObject jsonObj = new JSONObject();
                        jsonObj.put("type", "4");
                        jsonObj.put("notifyType", "0017");
                        TextWebSocketFrame frame = new TextWebSocketFrame(jsonObj.toJSONString());
                        if (csChannel != null && csChannel.isActive()) {
                            csChannel.writeAndFlush(frame.retain());
                        }
                        return true;
                    }else{
                        userChannel.writeAndFlush(msg.retain());
                    }
                }else{
                    userChannel.writeAndFlush(msg.retain());
                }
            }else{
                if(richConsultSession.getSource().equals("h5ykdl") || richConsultSession.getSource().equals("h5mtq")){
                    if("true".equalsIgnoreCase(messageContentFilter(msgMap))){
                        Channel csChannel = ConsultSessionManager.INSTANCE.getUserChannelMapping().get(csUserId);
                        JSONObject jsonObj = new JSONObject();
                        jsonObj.put("type", "4");
                        jsonObj.put("notifyType", "0017");
                        TextWebSocketFrame frame = new TextWebSocketFrame(jsonObj.toJSONString());
                        if (csChannel != null && csChannel.isActive()) {
                            csChannel.writeAndFlush(frame.retain());
                        }
                        return true;
                    }else{
                        userChannel.writeAndFlush(msg.retain());
                    }
                }else{
                    userChannel.writeAndFlush(msg.retain());
                }
                channelDead(msgMap, sysPropertyVoWithBLOBsVo, msgType, richConsultSession, csUserId, userId);
            }
        }else{
            userChannel.writeAndFlush(msg.retain());
        }
        return false;
    }

    private boolean sourceFromWX(Map<String, Object> msgMap, SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo, int msgType, Map userWechatParam, RichConsultSession richConsultSession, String csUserId) {
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
                    return true;
                }
                stringBuilder.append("------------------\n");

//                            http://localhost/titan/consultDoctorHome#/consultDoctorHome/00034ads0d764sdsa66a2d6esd0e8ddf
                stringBuilder.append("<a href='"+sysPropertyVoWithBLOBsVo.getTitanWebUrl() +"titan/consultDoctorHome#/consultDoctorHome/"+richConsultSession.getCsUserId()+"'>");
                stringBuilder.append(content.substring(0, nameIndex));
                stringBuilder.append("</a>|");
//                            stringBuilder.append("<a href='http://s251.baodf.com/keeper/wxPay/patientPay.do?serviceType=customerPay&customerId=");
                Map param = new HashMap();
                param.put("userId", csUserId);
                String smartMallAddress = "https://h5.koudaitong.com/v2/showcase/homepage?kdt_id=17783033&redirect_count=1";
                List<ConsultDoctorInfoVo> consultDoctorByInfos= consultDoctorInfoService.getConsultDoctorByInfo(param) ;
                if(consultDoctorByInfos != null && consultDoctorByInfos.size() > 0){
                    if(consultDoctorByInfos.get(0) != null){
                       if(consultDoctorByInfos.get(0).getMicroMallAddress() != null && StringUtils.isNotNull(consultDoctorByInfos.get(0).getMicroMallAddress())){
                           smartMallAddress = consultDoctorByInfos.get(0).getMicroMallAddress() ;
                       }
                    }
                }
                stringBuilder.append("<a href='"+smartMallAddress);
                stringBuilder.append("'>我的推荐</a>|");
                stringBuilder.append("<a href='"+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"keeper/wechatInfo/fieldwork/wechat/author?url="+sysPropertyVoWithBLOBsVo.getKeeperWebUrl()+"/keeper/wechatInfo/getUserWechatMenId?url=42,ZXYQ_YQY_WXCD");
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
                if(!sendResult.equals("messageOk")){
                    if (sendResult.equals("tokenIsInvalid")) {
                        WechatUtil.sendMsgToWechat((String) userWechatParam.get("token"), "o3_NPwuDSb46Qv-nrWL-uTuHiB8U", csUserId+"=tokenIsInvalid info =="+sendResult+"="+userWechatParam.get("token")+"=UId="+richConsultSession.getUserId()+"=MSG="+stringBuilder.toString());
                        updateWechatParameter();
                    }
                    WechatUtil.sendMsgToWechat((String) userWechatParam.get("token"), "o3_NPwuDSb46Qv-nrWL-uTuHiB8U", csUserId+"=发送失败"+sendResult+"=UId="+richConsultSession.getUserId()+"=MSG="+stringBuilder.toString()+"==token="+userWechatParam.get("token"));
                    Channel csChannel = ConsultSessionManager.INSTANCE.getUserChannelMapping().get(csUserId);
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("type", "4");
                    jsonObj.put("notifyType", "0016");
                    jsonObj.put("sendResult",sendResult);
                    jsonObj.put("token",userWechatParam.get("token")+"=UId="+richConsultSession.getUserId()+"=MSG="+stringBuilder.toString());
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
        return false;
    }

    private void userSendToDoctor(Map<String, Object> msgMap, Integer sessionId, int msgType, RichConsultSession richConsultSession, String csUserId, String userId) {
        //如果是用户作为发送者，则发给医生接收
        Channel csChannel = ConsultSessionManager.INSTANCE.getUserChannelMapping().get(csUserId);
        /**
         * jiangzg 新增消息数量 2016-9-8 18:28:29
         */
        int currentNum = richConsultSession.getConsultNum() + 1;
        richConsultSession.setConsultNum(currentNum);
        sessionRedisCache.putSessionIdConsultSessionPair(sessionId , richConsultSession);
        if (csChannel != null && csChannel.isActive()) {
            Map newConsultMap = msgMap ;
            newConsultMap.put("consultNum", richConsultSession.getConsultNum());
            TextWebSocketFrame frame =  new TextWebSocketFrame(JSONUtils.toJSONString(newConsultMap));
            csChannel.writeAndFlush(frame.retain());
        }
        //保存聊天记录
        consultRecordService.buildRecordMongoVo(userId, String.valueOf(msgType), (String) msgMap.get("content"), richConsultSession);
    }

    //消息过滤
    private String messageContentFilter(Map<String, Object> msgMap) {
        if(msgMap != null  && msgMap.size() > 0 ) {
            String content = (String) msgMap.get(ConsultSessionManager.KEY_CONSULT_CONTENT);
            //悦康动力用户
            if (content.contains("宝大夫") ||content.indexOf("https://kdt.im") != -1 || content.indexOf("https://h5.koudaitong.com") != -1){
                return "true";
            }
        }
        return "false";
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

            SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
            System.out.print("用户端微信参数更新");
            String token = WechatUtil.getToken(sysPropertyVoWithBLOBsVo.getUserCorpid(), sysPropertyVoWithBLOBsVo.getUserSectet());
            String ticket = WechatUtil.getJsapiTicket(token);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("token", token);
            map.put("ticket", ticket);
            map.put("id", "1");
            scheduleTaskService.updateWechatParameter(map);
            sessionRedisCache.putWeChatParamToRedis(map);

            System.out.print("医生端微信参数更新");
            token = WechatUtil.getToken(sysPropertyVoWithBLOBsVo.getDoctorCorpid(), sysPropertyVoWithBLOBsVo.getDoctorSectet());
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
