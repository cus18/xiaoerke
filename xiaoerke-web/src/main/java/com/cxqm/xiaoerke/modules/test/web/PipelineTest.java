package com.cxqm.xiaoerke.modules.test.web;

/**
 * Created by 赵得良 on 2016/11/28 0028.
 */

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class PipelineTest {

    public static void main(String[] args) {
        int count = 1000;
        withoutPipeline(count);
        usePipeline(count);
    }


    private static void withoutPipeline(int count) {
        Jedis jr = null;
        try {
            jr = new Jedis("10.10.224.44", 6379);
            for (int i = 0; i < count; i++) {
                jr.incr("testKey1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jr != null) {
                jr.disconnect();
            }
        }
    }

    private static void usePipeline(int count) {
        Jedis jr = null;
        try {
            jr = new Jedis("10.10.224.44", 6379);
            Pipeline pl = jr.pipelined();
            for (int i = 0; i < count; i++) {
                pl.incr("testKey2");
            }
            pl.sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jr != null) {
                jr.disconnect();
            }
        }
    }
}