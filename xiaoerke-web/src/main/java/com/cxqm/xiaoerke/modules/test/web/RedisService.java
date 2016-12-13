package com.cxqm.xiaoerke.modules.test.web;

import com.cxqm.xiaoerke.modules.test.web.JedisClient;
import org.springframework.stereotype.Service;

/**
 * Created by fengdezitai on 2016/10/9.
 */
@Service
public class RedisService {

    public String get(String key) throws Exception{
        JedisClient jedisClient = JedisClient.getInstance(); //上面实现的JedisClient
        String result = "";
        try {
            result = jedisClient.get("hello");
        }catch (Exception e){
            throw e;
        }
        return result;
    }
}