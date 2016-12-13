package com.cxqm.xiaoerke.modules.test.web;

/**
 * Created by 赵得良 on 2016/12/13 0013.
 */
import redis.clients.jedis.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by fengdezitai on 2016/10/13.
 */
public class JedisClusterClient {

    private static int count = 0;

    private static final JedisClusterClient redisClusterClient = new JedisClusterClient();

    /**
     * 私有构造函数
     */
    private JedisClusterClient() {}

    public static JedisClusterClient getInstance() {
        return redisClusterClient;
    }

    private JedisPoolConfig getPoolConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(1000);
        config.setMaxIdle(100);
        config.setTestOnBorrow(true);
        return config;
    }

    public void SaveRedisCluster() {
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort("192.168.31.245", 7000));
        jedisClusterNodes.add(new HostAndPort("192.168.31.245", 7001));
        jedisClusterNodes.add(new HostAndPort("192.168.31.245", 7002));
        jedisClusterNodes.add(new HostAndPort("192.168.31.210", 7003));
        jedisClusterNodes.add(new HostAndPort("192.168.31.210", 7004));
        jedisClusterNodes.add(new HostAndPort("192.168.31.210", 7005));

        JedisCluster jc = new JedisCluster(jedisClusterNodes,getPoolConfig());
        jc.set("cluster", "this is a redis cluster");
        String result = jc.get("cluster");
        System.out.println(result);
    }

    public static void main(String[] args) {
        JedisClusterClient jedisClusterClient = JedisClusterClient.getInstance();
        jedisClusterClient.SaveRedisCluster();
    }
}