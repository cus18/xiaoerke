package com.cxqm.xiaoerke.bdfApp.consult.web;

import com.alibaba.druid.support.json.JSONUtils;
import com.cxqm.xiaoerke.bdfApp.core.ConsultSessionManager_App;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 该类用来接收咨询端医生回复的内容请求
 * Created by guozengguang on 2016/6/30.
 */
@Controller
@RequestMapping(value = "consult/transfer")
public class ConsultBdfAppUserController {

    private static ExecutorService threadExecutor = Executors.newSingleThreadExecutor();

    @RequestMapping(value = "/toBdfApp", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody Map<String,Object> toBdfApp(HttpServletRequest request,
        @RequestParam(required=true) String openId,
        @RequestParam(required=true) String messageType,
        @RequestParam(required=false) String messageContent,
        @RequestParam(required=false) String mediaId) {

        HashMap<String,Object> result = new HashMap<String,Object>();
        System.out.println("openId="+openId);
        HashMap<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("openId",openId);
        paramMap.put("messageType",messageType);
        paramMap.put("messageContent",messageContent);
        paramMap.put("mediaId",mediaId);

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


            String openId = (String) this.param.get("openId");
            String messageType = (String) this.param.get("messageType");
            String messageContent = (String) this.param.get("messageContent");
            String mediaId = (String) this.param.get("mediaId");

            ConsultSessionManager_App consultSessionManager_App = ConsultSessionManager_App.getSessionManager();
            Map<String, Channel> userChannelMapping= ConsultSessionManager_App.getSessionManager().getUserChannelMapping();
            Channel userChannel = userChannelMapping.get(openId);

            Map<String,Object> replyMap = new HashMap<String, Object>();
            replyMap.put("messageContent",messageContent);
            replyMap.put("messageType",messageType);
            TextWebSocketFrame csUserMsg = new TextWebSocketFrame(JSONUtils.toJSONString(replyMap));
            userChannel.writeAndFlush(csUserMsg.retain());


        }
    }
}
