package com.cxqm.xiaoerke.webapp.controller; /**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */


import com.alibaba.fastjson.JSONObject;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.consult.service.SessionCache;
import com.cxqm.xiaoerke.modules.consult.service.core.ConsultSessionManager;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;


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
    private SessionCache sessionCache;

    @Autowired
    private WechatAttentionService wechatAttentionService;

    @Autowired
    private ConsultRecordService consultRecordService;


    @RequestMapping(value = "/conversation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    String conversation(@RequestParam(required=true) String openId,
                                     @RequestParam(required=true) String messageType,
                                     @RequestParam(required=false) String messageContent,
                                     @RequestParam(required=false) String mediaId) {

        //根据用户的openId，判断redis中，是否有用户正在进行的session
        Integer sessionId = sessionCache.getSessionIdByOpenId(openId);
        HashMap<String,Object> createWechatConsultSessionMap = null;
        RichConsultSession consultSession = new RichConsultSession();

        ConsultRecordMongoVo consultRecordMongoVo = new ConsultRecordMongoVo();

        //需要根据openId获取到nickname，如果拿不到nickName，则用利用openId换算出一个编号即可
        SysWechatAppintInfoVo sysWechatAppintInfoVo = new SysWechatAppintInfoVo();
        sysWechatAppintInfoVo.setOpen_id(openId);
        SysWechatAppintInfoVo resultVo = wechatAttentionService.findAttentionInfoByOpenId(sysWechatAppintInfoVo);
        if(resultVo == null){
            return "";
        }
        String nickName = openId +  (new Date()).getTime();
        if(resultVo!=null){
            if(StringUtils.isNotNull(resultVo.getWechat_name())){
               nickName = resultVo.getWechat_name();
            }
        }
        Channel csChannel = null;
        //如果此用户不是第一次发送消息，则sessionId不为空
        if(sessionId!=null){
            consultSession = sessionCache.getConsultSessionBySessionId(sessionId);
            csChannel = ConsultSessionManager.getSessionManager().getUserChannelMapping().get(consultSession.getCsUserId());
        }else{//如果此用户是第一次发送消息，则sessionId为空
            consultSession.setCreateTime(new Date());
            consultSession.setOpenid(openId);
            consultSession.setNickName(nickName);
            //创建会话，发送消息给用户，给用户分配接诊员
            createWechatConsultSessionMap = ConsultSessionManager.getSessionManager().createWechatConsultSession(consultSession);
            csChannel = (Channel)createWechatConsultSessionMap.get("csChannel");
            consultSession = (RichConsultSession)createWechatConsultSessionMap.get("consultSession");
            sessionId = consultSession.getId();
        }
        //会话创建成功，给接诊员(或是医生)发送消息
        if(csChannel!=null){
            try {
                JSONObject obj = new JSONObject();
                obj.put("sessionId", sessionId);
                obj.put("senderId", openId);
                obj.put("dateTime", DateUtils.DateToStr(new Date()));
                obj.put("senderName",nickName);

                if(messageType.equals("text")) {
                    obj.put("type", 0);
                    obj.put("content", URLDecoder.decode(messageContent, "utf-8"));
                }else{
                    if(messageType.equals("image")){
                        obj.put("type", 1);
                    }else if(messageType.equals("voice")){
                        obj.put("type", 2);
                    }else if(messageType.equals("video")){
                        obj.put("type", 3);
                    }
                    //根据mediaId，从微信服务器上，获取到媒体文件，再将媒体文件，放置阿里云服务器，获取URL
                    try{
                        WechatUtil wechatUtil = new WechatUtil();
                        String mediaURL = wechatUtil.downloadMediaFromWx("bIkXki53G7M6Ogm8xGJR5gVBAVDLUiGJDx64V8xtr7gSIyKDprPQWgQZ1dysVJtp_1nXQY2SeBe-WvPSVL0woyLDqoI4Us57p4CUMI0FB48mSKd0k9zC1TDF0MyePfnwBYEeAIAIFL",mediaId,nickName,messageType);//sessionCache.getWeChatToken()
                        obj.put("content", mediaURL);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                TextWebSocketFrame frame = new TextWebSocketFrame(obj.toJSONString());
                csChannel.writeAndFlush(frame.retain());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        //保存聊天记录
        consultRecordService.buildRecordMongoVo(openId, messageType, messageContent, consultSession, consultRecordMongoVo, resultVo);

        //更新会话操作时间
        consultRecordService.saveConsultSessionStatus(sessionId,consultSession.getUserId());

        return "";
    }



}
