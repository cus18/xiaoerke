package com.cxqm.xiaoerke.modules.consult.web;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/6/24 0024.
 */
@Controller
@RequestMapping(value = "consult/bdfApp")
public class ConsultAppController {


    private static ExecutorService threadExecutor = Executors.newSingleThreadExecutor();

    @RequestMapping(value = "/conversation", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String,Object> conversation(HttpServletRequest request,TextWebSocketFrame msg) {
        HashMap<String,Object> result = new HashMap<String,Object>();

        HashMap<String,Object> paramMap = new HashMap<String, Object>();
        paramMap.put("",msg);
        Runnable thread = new processUserMessageThread(paramMap);
        threadExecutor.execute(thread);
        return result;
    }

    //发送消息给医生或分诊员
    public class processUserMessageThread extends Thread {
        private HashMap<String,Object> param;

        public processUserMessageThread(HashMap<String,Object> paramMap) {
            this.param = paramMap;
        }

        public void run() {

        }
    }
}
