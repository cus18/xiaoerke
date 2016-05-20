package com.cxqm.xiaoerke.modules.consult.web; /**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */


import com.alibaba.fastjson.JSONObject;
import com.cxqm.xiaoerke.common.utils.ConstantUtil;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.entity.RpcRequest;
import com.cxqm.xiaoerke.modules.consult.entity.RpcResponse;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.consult.service.core.ConsultSessionManager;
import com.cxqm.xiaoerke.modules.consult.service.core.RpcClient;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
    private ConsultSessionService consultSessionService;


    private static ExecutorService threadExecutor = Executors.newSingleThreadExecutor();

    @RequestMapping(value = "/conversation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String,Object> conversation(HttpServletRequest request,
                        @RequestParam(required=true) String openId,
                        @RequestParam(required=true) String messageType,
                        @RequestParam(required=false) String messageContent,
                        @RequestParam(required=false) String mediaId) {
        HashMap<String,Object> result = new HashMap<String,Object>();

        HashMap<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("openId",openId);
        paramMap.put("messageType",messageType);
        paramMap.put("messageContent",messageContent);
        if(messageType.contains("voice")||messageType.contains("video")||messageType.contains("image")){
            paramMap.put("mediaId",mediaId);
        }
        paramMap.put("serverAddress",StringUtils.getRemoteAddr(request));

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
            System.out.println(messageContent);
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

            Channel csChannel = null;
            System.out.println("userId------"+userId);
            //根据用户的openId，判断redis中，是否有用户正在进行的session
            Integer sessionId = sessionRedisCache.getSessionIdByUserId(userId);
            HashMap<String,Object> createWechatConsultSessionMap = null;
            RichConsultSession consultSession = new RichConsultSession();

            //如果此用户不是第一次发送消息，则sessionId不为空
            if(sessionId!=null){
                consultSession = sessionRedisCache.getConsultSessionBySessionId(sessionId);
                csChannel = ConsultSessionManager.getSessionManager().getUserChannelMapping().get(consultSession.getCsUserId());
                if(!csChannel.isActive()){
                    consultSession.setStatus(ConsultSession.STATUS_COMPLETED);
                    consultSessionService.updateSessionInfo(consultSession);

                    createWechatConsultSessionMap = ConsultSessionManager.getSessionManager().createUserWXConsultSession(consultSession);
                    if(createWechatConsultSessionMap!=null){
                        csChannel = (Channel)createWechatConsultSessionMap.get("csChannel");
                        consultSession = (RichConsultSession)createWechatConsultSessionMap.get("consultSession");
                        sessionId = consultSession.getId();
                    }
                }
            }else{//如果此用户是第一次发送消息，则sessionId为空
                consultSession.setCreateTime(new Date());
                consultSession.setUserId(userId);
                consultSession.setUserName(userName);
                consultSession.setSource(source);
                //创建会话，发送消息给用户，给用户分配接诊员
                createWechatConsultSessionMap = ConsultSessionManager.getSessionManager().createUserWXConsultSession(consultSession);
                if(createWechatConsultSessionMap!=null){
                    csChannel = (Channel)createWechatConsultSessionMap.get("csChannel");
                    consultSession = (RichConsultSession)createWechatConsultSessionMap.get("consultSession");
                    sessionId = consultSession.getId();
                }
            }

            //会话创建成功，拿到了csChannel,给接诊员(或是医生)发送消息
            if(csChannel!=null){
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("sessionId", sessionId);
                    obj.put("senderId", userId);
                    obj.put("dateTime", DateUtils.DateToStr(new Date()));
                    obj.put("senderName",userName);
                    obj.put("fromServer",serverAddress);
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

                        //根据mediaId，从微信服务器上，获取到媒体文件，再将媒体文件，放置阿里云服务器，获取URL
                        if(messageType.contains("voice")||messageType.contains("video")||messageType.contains("image")){
                            try{
                                WechatUtil wechatUtil = new WechatUtil();
                                Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
                                String mediaURL = wechatUtil.downloadMediaFromWx((String) userWechatParam.get("token"),
                                        (String) this.param.get("mediaId"),messageType);
                                obj.put("content", mediaURL);
                                messageContent = mediaURL;
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    }
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



}
