package com.cxqm.xiaoerke.webapp.test;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by guozengguang on 2016/06/07.
 */
public class TestUmbrellaDemo {

    //public  boolean isFinished = false;
    //private static String url = "http://localhost:8080/keeper/patient/wxChat";
    //private static String url = "http://s2.xiaork.cn/wisdom/umbrella/addFamily";
    private static String url = "http://xiaork.com/wisdom/umbrella/addFamily";
    //private static String url = "http://xiaork.com/umbrella/cheackFamilyMembers";
    //private static String url = "http://xiaork.com/umbrella/cheackFamilyMembers";
    private static final int MAX_THREADS_COUNT = 200;//初始化开启最大线程数
    //private static final int MAX_THREADS_COUNT = 1;
    private static final int MESSAGE_COUNT = 10;//模拟一个用户向服务器发送的消息总数

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
        for (int i = 120000200; i < maxThreadsCount +120000200; i++) {

            new Thread(new TestSimulator(i,"线程" + i)).start();
            System.out.println("线程" +i);
        }
    }

    public class TestSimulator implements Runnable {

        private TestUmbrellaDemo testUmbrellaDemo  = new TestUmbrellaDemo();;
        private String id = "";
        private String name = "";
        private String threadName ;
        private String birthDay = "2016-06-07";
        private String sex = "0";


        public TestSimulator(int i,String threadName){
            this.threadName = threadName;
            this.id = String.valueOf(i);
            this.name = id;
        }

        @Override
        public void run() {
            String url = "";
            url = testUmbrellaDemo.getUrl();
            //sendPost(url);
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",id);
                jsonObject.put("name",name);
                jsonObject.put("sex",sex);
                jsonObject.put("birthDay",birthDay);
                postByJson(url, jsonObject.toString());
                Thread.sleep(3 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public  JSONObject postByJson(String url,String json){
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            JSONObject response = null;
            try {
                StringEntity s = new StringEntity(json.toString());
                s.setContentEncoding("UTF-8");
                s.setContentType("application/json");
                post.setEntity(s);
                HttpResponse res = client.execute(post);
                if(res.getStatusLine().getStatusCode() == HttpStatus.OK.value()){
                    HttpEntity entity = res.getEntity();
                    InputStream is = entity.getContent();
                    //                String charset = EntityUtils.getContentCharSet(entity);
                    if (entity != null) {
                        // 将InputStream转换为Reader，并使用缓冲读取，提高效率，同时可以按行读取内容
                        BufferedReader br = new BufferedReader(new InputStreamReader(
                            is, "UTF-8"));
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            System.out.println(line);
                        }
                        is.close();
                        //response = new JSONObject(line);
                    }

                    //                response = new JSONObject(new JSONTokener(new InputStreamReader(is));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return response;
        }
    }

    public static void main(String[] args) {
        TestUmbrellaDemo testConcurrentDemo = new TestUmbrellaDemo();
        testConcurrentDemo.process();
        //System.out.println();
    }

}
