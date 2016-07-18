package com.cxqm.xiaoerke.webapp.test;

/**
 * Created by guozengguang on 2016/4/22.
 */
public class TestConcurrentDemo {

    //public  boolean isFinished = false;
    //private static String url = "http://localhost:8080/keeper/patient/wxChat";
    private static String url = "http://xiaork.com:80/keeper/patient/wxChat";
    private static final int MAX_THREADS_COUNT = 100;//初始化开启最大线程数
    //private static final int MAX_THREADS_COUNT = 1;
    private static final int MESSAGE_COUNT = 100;//模拟一个用户向服务器发送的消息总数

    public static String getUrl(){
        return url;
    }
    public static int getMessageCount(){
        return MESSAGE_COUNT;
    }

    public void process(){
        //ExecutorService executorService =  Executors.newFixedThreadPool(5000);
        //executorService.execute(new TestSimulator(1));
        int maxThreadsCount = MAX_THREADS_COUNT;
        for (int i = 0; i < maxThreadsCount; i++) {

            new Thread(new TestSimulator(i,"线程" + i)).start();
            //System.out.println("线程" +i);
        }


        //isFinished = true;
    }

    public static void main(String[] args) {
        //TestConcurrentDemo testConcurrentDemo = new TestConcurrentDemo();
        //testConcurrentDemo.process();
        //System.out.println();
        //Map<String,Object> map1 =new HashMap<String, Object>();
        //Map<String,Object> map2 =new HashMap<String, Object>();
        //map1.put("a",1);
        //map2.put("b",2);
        //map1.putAll(map2);
        //System.out.println(map1);
        String id = "99" + "120006306".substring(1);
        System.out.println("begin:"+id);
        System.out.println("还原:1"+id.substring(2));
    }

}
