package com.cxqm.xiaoerke.modules.consult.web;

import com.alibaba.fastjson.JSONObject;
import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.utils.ConstantUtil;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultVoiceRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultPayUserService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.consult.service.core.ConsultSessionManager;
import com.cxqm.xiaoerke.modules.consult.service.impl.ConsultRecordMongoDBServiceImpl;
import com.cxqm.xiaoerke.modules.consult.service.impl.ConsultVoiceRecordMongoServiceImpl;
import com.cxqm.xiaoerke.modules.consult.service.util.ConsultUtil;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 会员Controller
 *
 * @author deliang
 * @version 2015-11-04
 */
@Controller
@RequestMapping(value = "consult/wechat")
public class ConsultWechatController extends BaseController {

    @Autowired
    private SessionRedisCache sessionRedisCache;

    @Autowired
    private WechatAttentionService wechatAttentionService;

    @Autowired
    private ConsultRecordService consultRecordService;

    @Autowired
    private ConsultPayUserService consultPayUserService;

    private static ExecutorService threadExecutor = Executors.newSingleThreadExecutor();

    @Autowired
    private ConsultVoiceRecordMongoServiceImpl consultVoiceRecordMongoService;

    @RequestMapping(value = "/conversation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String,Object> conversation(HttpServletRequest request,
                                    @RequestParam(required=true) String openId,
                                    @RequestParam(required=true) String messageType,
                                    @RequestParam(required=false) String messageContent,
                                    @RequestParam(required=false) String mediaId) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        HashMap<String,Object> result = new HashMap<String,Object>();
        System.out.println("openId="+openId);
        HashMap<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("openId",openId);
        paramMap.put("messageType",messageType);
        paramMap.put("messageContent",messageContent);
        if(messageType.contains("voice")||messageType.contains("video")||messageType.contains("image")){
            paramMap.put("mediaId",mediaId);
        }
        paramMap.put("serverAddress",ConstantUtil.SERVER_ADDRESS);
        Runnable thread = new processUserMessageThread(paramMap);
        threadExecutor.execute(thread);

        result.put("status", "success");
        return result;
    }

    public class processUserMessageThread extends Thread {
        private HashMap<String,Object> param;

        public processUserMessageThread(HashMap<String,Object> paramMap) {
            this.param = paramMap;
        }

        public void run() {

            //需要根据openId获取到nickname，如果拿不到nickName，则用利用openId换算出一个编号即可
            String openId = (String) this.param.get("openId");
            String messageType = (String) this.param.get("messageType");
            String messageContent = (String) this.param.get("messageContent");
            String serverAddress = (String) this.param.get("serverAddress");

            SysWechatAppintInfoVo sysWechatAppintInfoVo = new SysWechatAppintInfoVo();
            sysWechatAppintInfoVo.setOpen_id(openId);
            SysWechatAppintInfoVo wechatAttentionVo = wechatAttentionService.findAttentionInfoByOpenId(sysWechatAppintInfoVo);

            String userName = openId;
            String userId = openId;
            if(openId.length() > 20){
                userName = openId.substring(openId.length()-8,openId.length());
            }else{
                userName = openId.substring(0,10);
                userId = openId.substring(0,10);
            }

            if(wechatAttentionVo!=null){
                if(StringUtils.isNotNull(wechatAttentionVo.getWechat_name())){
                    userName = wechatAttentionVo.getWechat_name();
                }
            }
            //此处，将openId统一转换成userId，以后，在咨询系统中，所有表示咨询用户唯一来源的id统一都用userId表示，
            // 在所有的日志记录，还是缓存中，所有的会话，都引入一个字段，source，标示，这个会话，
            // 是基于微信，还是H5，还是合作第三方的来源，以便按照不同的逻辑来处理。
            String source = "wxcxqm";
            Integer notifyType = 1001;

            Channel csChannel = null;
            //根据用户的openId，判断redis中，是否有用户正在进行的session
            Integer sessionId = sessionRedisCache.getSessionIdByUserId(userId);
            System.out.println("sessionId------"+sessionId);
            HashMap<String,Object> createWechatConsultSessionMap = null;
            RichConsultSession consultSession = new RichConsultSession();

            //如果此用户不是第一次发送消息，则sessionId不为空
            if(sessionId!=null){
                //检测是否给用户推送以下消息-- 每个会话只推一次(您需要花点时间排队，请耐心等待哦)
                String sessionList = consultPayUserService.getChargeInfo(sessionId);
                if(null == sessionList&&consultPayUserService.angelChargeCheck(openId)){
                    consultPayUserService.saveChargeUser(sessionId,openId);
                    consultPayUserService.sendMessageToConsult(openId,4);
                }
                consultSession = sessionRedisCache.getConsultSessionBySessionId(sessionId);
                csChannel = ConsultSessionManager.getSessionManager().getUserChannelMapping().get(consultSession.getCsUserId());
                System.out.println("csChannel------"+csChannel);
                if(csChannel==null){
                    //保存聊天记录
                    consultRecordService.buildRecordMongoVo(userId, String.valueOf(ConsultUtil.transformMessageTypeToType(messageType)), messageContent, consultSession);
                    //更新会话操作时间
                    consultRecordService.saveConsultSessionStatus(consultSession);
                }else{
                    System.out.println("csChannel.isActive()------"+csChannel.isActive());
                    if(!csChannel.isActive()){
                        //保存聊天记录
                        consultRecordService.buildRecordMongoVo(userId, String.valueOf(ConsultUtil.transformMessageTypeToType(messageType)), messageContent, consultSession);
                        //更新会话操作时间
                        consultRecordService.saveConsultSessionStatus(consultSession);
                    }
                }
            }else{
                //如果此用户是第一次发送消息，则sessionId为空
                consultSession.setCreateTime(new Date());
                consultSession.setUserId(userId);
                consultSession.setUserName(userName);
                consultSession.setSource(source);
                consultSession.setServerAddress(serverAddress);
                //创建会话，发送消息给用户，给用户分配接诊员
                createWechatConsultSessionMap = ConsultSessionManager.getSessionManager().createUserWXConsultSession(consultSession);
                if(createWechatConsultSessionMap!=null){
                    csChannel = (Channel)createWechatConsultSessionMap.get("csChannel");
                    consultSession = (RichConsultSession)createWechatConsultSessionMap.get("consultSession");
                    sessionId = consultSession.getId();
                }
                //检测用户是否是收费用户,1001 为正常用户(无标签) ,1002 需要付款用户(等待),1003 已付款用户;
                try{
                    if(consultPayUserService.angelChargeCheck(openId)){
                        HashMap<String,Object> payInfo = new HashMap<String, Object>();
                        payInfo.put("openid",openId);
                        payInfo.put("create_time",new Date());
                        consultPayUserService.putneepPayConsultSession(sessionId,payInfo);
                        notifyType = 1002;
                        int type  = Integer.parseInt(Global.getConfig("consultPayMsgType"));
                        consultPayUserService.sendMessageToConsult(openId,type);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            //会话创建成功，拿到了csChannel,给接诊员(或是医生)发送消息
            if(csChannel!=null&&csChannel.isActive()){
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("sessionId", sessionId);
                    obj.put("senderId", userId);
                    obj.put("dateTime", DateUtils.DateToStr(new Date()));
                    obj.put("senderName",userName);
                    obj.put("notifyType",notifyType);
                    obj.put("serverAddress",serverAddress);
                    System.out.println("serverAddress------"+serverAddress);
                    obj.put("source",consultSession.getSource());

                    StringBuffer sbf = new StringBuffer();
                    if(messageType.equals("text")) {
                        obj.put("type", 0);
                        //将模拟微信端发过来的信息内容进行修改
                        if(openId.length() <= 11){
                            String[] openIdArr = openId.split("-");
                            sbf.append(openIdArr[0]).append("<--->").append(consultSession.getCsUserId());
                            sbf.append("<--->").append(openIdArr[1]);
                            messageContent = sbf.toString();
                            System.out.println(messageContent);
                        }
                        obj.put("content", URLDecoder.decode(messageContent, "utf-8"));

                    }else{
                        if(messageType.contains("image")){
                            obj.put("type", 1);
                        }else if(messageType.contains("voice")){
                            obj.put("type", 2);
                        }else if(messageType.contains("video")){
                            obj.put("type", 3);
                        }
                        //收到语音，发送通知给用户，提示为了更好地咨询，最好文字聊天
                        //根据mediaId，从微信服务器上，获取到媒体文件，再将媒体文件，放置阿里云服务器，获取URL
                        if(messageType.contains("voice")||messageType.contains("video")||messageType.contains("image")){
                            try{
                                WechatUtil wechatUtil = new WechatUtil();
                                Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
                                String mediaURL = wechatUtil.downloadMediaFromWx((String) userWechatParam.get("token"),
                                        (String) this.param.get("mediaId"),messageType);
                                obj.put("content", mediaURL);
                                messageContent = mediaURL;
                                if(messageType.contains("voice")){
                                    ConsultVoiceRecordMongoVo consultVoiceRecordMongoVo = new ConsultVoiceRecordMongoVo();
                                    consultVoiceRecordMongoVo.setCsUserId(consultSession.getCsUserId());
                                    consultVoiceRecordMongoVo.setSessionId(sessionId);
                                    long consultCount = consultVoiceRecordMongoService.countConsultByVoice(consultVoiceRecordMongoVo);
                                    if(consultCount < 1){
                                        consultVoiceRecordMongoVo.setCreateDate(new Date());
                                        consultVoiceRecordMongoVo.setType(messageType);
                                        consultVoiceRecordMongoVo.setUserId(consultSession.getUserId());
                                        consultVoiceRecordMongoVo.setCsUserName(consultSession.getCsUserName());
                                        consultVoiceRecordMongoVo.setUserName(consultSession.getUserName());
                                        consultVoiceRecordMongoVo.setContent(mediaURL);
                                        consultVoiceRecordMongoService.insert(consultVoiceRecordMongoVo);
                                        WechatUtil.sendMsgToWechat((String) userWechatParam.get("token"), consultSession.getUserId(), "亲，医生听不到语音哦，发送图文吧！");
                                    }
                                }
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    }
                    System.out.println("here csChannel is"+csChannel);
                    TextWebSocketFrame frame = new TextWebSocketFrame(obj.toJSONString());
                    csChannel.writeAndFlush(frame.retain());

                    //保存聊天记录
                    consultRecordService.buildRecordMongoVo(userId,String.valueOf(ConsultUtil.transformMessageTypeToType(messageType)), messageContent, consultSession);

                    //更新会话操作时间
                    consultRecordService.saveConsultSessionStatus(consultSession);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @RequestMapping(value = "/notifyPayInfo2Distributor", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String,Object> notifyPayInfo2Distributor(@RequestParam(required=true) String openId){
        Channel csChannel = null;
        //根据用户的openId，判断redis中，是否有用户正在进行的session
        Integer sessionId = sessionRedisCache.getSessionIdByUserId(openId);
        System.out.println("sessionId------"+sessionId);
        HashMap<String,Object> createWechatConsultSessionMap = null;
        RichConsultSession consultSession = new RichConsultSession();
        consultSession = sessionRedisCache.getConsultSessionBySessionId(sessionId);
        csChannel = ConsultSessionManager.getSessionManager().getUserChannelMapping().get(consultSession.getCsUserId());
        System.out.println("csChannel------"+csChannel);

        if(csChannel!=null&&csChannel.isActive()){
            try {
                JSONObject obj = new JSONObject();
                obj.put("sessionId", sessionId);
                obj.put("senderId", openId);
                obj.put("dateTime", DateUtils.DateToStr(new Date()));
                obj.put("notifyType", "1003");
                obj.put("type", "4");
                obj.put("source", consultSession.getSource());
                TextWebSocketFrame frame = new TextWebSocketFrame(obj.toJSONString());
                csChannel.writeAndFlush(frame.retain());
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return null;
    };



    /**
     * 只咨询客服调用此接口通知前台修改标示
     * */
    @RequestMapping(value = "/consultCustomOnly", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String,Object> consultCustomOnly(HttpSession session, HttpServletRequest request){
        String openId = WechatUtil.getOpenId(session,request);
        openId = "oogbDwCLH1_x-KLcQKqlrmUzG2ng";
        Channel csChannel = null;
        //根据用户的openId，判断redis中，是否有用户正在进行的session
        Integer sessionId = sessionRedisCache.getSessionIdByUserId(openId);
        System.out.println("sessionId------"+sessionId);
        HashMap<String,Object> createWechatConsultSessionMap = null;
        RichConsultSession consultSession = new RichConsultSession();
        if(null != sessionId){
        consultSession = sessionRedisCache.getConsultSessionBySessionId(sessionId);
        csChannel = ConsultSessionManager.getSessionManager().getUserChannelMapping().get(consultSession.getCsUserId());
        System.out.println("csChannel------"+csChannel);
        if(csChannel!=null&&csChannel.isActive()){
            try {
                JSONObject obj = new JSONObject();
                obj.put("sessionId", sessionId);
                obj.put("senderId", openId);
                obj.put("dateTime", DateUtils.DateToStr(new Date()));
                obj.put("notifyType", "1001");
                obj.put("type", "4");
                obj.put("source", consultSession.getSource());
                TextWebSocketFrame frame = new TextWebSocketFrame(obj.toJSONString());
                csChannel.writeAndFlush(frame.retain());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        }
        return null;
    };
}