/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.webapp.controller;

import com.alibaba.fastjson.JSONObject;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.SessionCache;
import com.cxqm.xiaoerke.modules.consult.service.core.ConsultSessionManager;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;


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

    @RequestMapping(value = "/conversation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    String conversation(@RequestParam(required=true) String openId,
                                     @RequestParam(required=true) String messageType,
                                     @RequestParam(required=false) String messageContent,
                                     @RequestParam(required=false) String mediaId) {

        //根据用户的openId，判断redis中，是否有用户正在进行的session
        Integer sessionId = sessionCache.getSessionIdByOpenId(openId);

        if(sessionId!=null){
            RichConsultSession consultSession = sessionCache.getConsultSessionBySessionId(sessionId);
            Channel csChannel = ConsultSessionManager.getSessionManager().getUserChannelMapping().get(consultSession.getCsUserId());

            try {
                JSONObject obj = new JSONObject();
                obj.put("sessionId", sessionId);
                obj.put("senderId", openId);
                obj.put("dateTime", DateUtils.DateToStr(new Date()));
                String nickName = "";
                //需要根据openId获取到nickname，如果拿不到nickName，则用利用openId换算出一个编号即可
                obj.put("senderName",nickName);

                if(messageType.equals("text")) {

                    obj.put("type", 0);
                    obj.put("content", URLDecoder.decode(messageContent, "utf-8"));

                    TextWebSocketFrame frame = new TextWebSocketFrame(obj.toJSONString());
                    csChannel.writeAndFlush(frame.retain());
                }else{
                    if(messageType.equals("image")){
                        obj.put("type", 1);
                    }else if(messageType.equals("voice")){
                        obj.put("type", 2);
                    }else if(messageType.equals("video")){
                        obj.put("type", 3);
                    }
                    //根据mediaId，从微信服务器上，获取到媒体文件，再将媒体文件，放置阿里云服务器，获取URL
                    String mediaURL = "";
                    obj.put("content", mediaURL);
                    TextWebSocketFrame frame = new TextWebSocketFrame(obj.toJSONString());
                    csChannel.writeAndFlush(frame.retain());
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //会话信息存入mongodb



        }


        return "";
    }

}
