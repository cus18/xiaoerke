package com.cxqm.xiaoerke.modules.test.web;

import org.springframework.stereotype.Service;

/**
 * Created by 赵得良 on 2016/12/13 0013.
 */
@Service
public class RedisClusterService {
    public void save() throws Exception {
        //调用 JedisClusterClient 中的方法
        JedisClusterClient jedisClusterClient = JedisClusterClient.getInstance();
        try {
            jedisClusterClient.SaveRedisCluster();
        } catch (Exception e) {
            throw e;
        }
    }
}
