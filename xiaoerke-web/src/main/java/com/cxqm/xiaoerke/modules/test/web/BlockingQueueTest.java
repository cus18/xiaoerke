package com.cxqm.xiaoerke.modules.test.web;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
/**
 * Created by 赵得良 on 2016/11/21 0021.
 */
/**
 * Throws exception    Specical value        TimeOut                    Block
 * add(e)                       offer(e)               offer(e,time,unit)         put
 * remove                      poll                   poll(time,unit)             take
 */
public class BlockingQueueTest {

    public static void main(String[] args) throws InterruptedException {
        // 声明一个容量为10的缓存队列
        //SynchronousQueue<String> 只有有人来拿的时候数据才能放的进去
        BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10);

        Producer producer1 = new Producer("rain",queue);
        Producer producer2 = new Producer("tom",queue);
        Producer producer3 = new Producer("jack",queue);
        Consumer consumer = new Consumer(queue);

        // 借助Executors 线程池
        ExecutorService service = Executors.newCachedThreadPool();
        // 启动线程
        service.execute(producer1);
        service.execute(producer2);
        service.execute(producer3);
        service.execute(consumer);

        // 执行3s
        TimeUnit.SECONDS.sleep(3);
        producer1.stop();
        producer2.stop();
        producer3.stop();

        // 退出Executor
        service.shutdown();
    }
}
