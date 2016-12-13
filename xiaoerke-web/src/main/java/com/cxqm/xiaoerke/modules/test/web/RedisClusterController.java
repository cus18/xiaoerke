package com.cxqm.xiaoerke.modules.test.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 赵得良 on 2016/12/13 0013.
 */
@Controller
@RequestMapping(value = "redisCluster")
public class RedisClusterController {

    @Autowired
//    private RedisClusterCache redisClusterCache;

    @RequestMapping(value = "clusterSave",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object clusterSave(){
        //redisClusterCache.put("cluster","save cluster");
//        Token token = new Token();
//        token.setExpires_in(1000);
//        token.setAccess_token("hello world");
//        redisClusterCache.put("token",token);
        return "ok";
    }

    @RequestMapping(value = "getKey",method = RequestMethod.GET)
    @ResponseBody
    public Object getCluster(String key){
//        Object val = redisClusterCache.get(key);
        return 123;
    }
}
