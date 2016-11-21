package com.cxqm.xiaoerke.modules.test.web;

/**
 * Created by 赵得良 on 2016/11/21 0021.
 */

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Producer implements Runnable {

    private String name;
    private volatile boolean isRunning = true;
    private BlockingQueue queue;
    private static AtomicInteger count = new AtomicInteger();
    private static final int DEFAULT_RANGE_FOR_SLEEP = 1000;

    public Producer(String name, BlockingQueue queue) {
        this.queue = queue;
        this.name = name;
    }

    public void run() {
        String data = null;
        Random r = new Random();

        System.out.println("启动生产者线程！" + name);
        try {
            while (isRunning) {
                System.out.println(name + "正在生产数据...");
                Thread.sleep(r.nextInt(DEFAULT_RANGE_FOR_SLEEP));

                data = "data:" + count.incrementAndGet();
                System.out.println(name + "将数据：" + data + "放入队列...");
                if (!queue.offer(data, 2, TimeUnit.SECONDS)) {
                    System.out.println(name + "放入数据失败：" + data);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            System.out.println(name + "退出生产者线程！");
        }
    }

    public void stop() {
        isRunning = false;
    }


}