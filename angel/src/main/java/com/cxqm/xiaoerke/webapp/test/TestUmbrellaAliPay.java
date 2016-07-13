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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by guozengguang on 2016/06/07.
 */
public class TestUmbrellaAliPay {

    //public  boolean isFinished = false;
    //private static String url = "http://localhost:8080/keeper/patient/wxChat";
    //private static String url = "http://s2.xiaork.cn/wisdom/umbrella/addFamily";
    private static String url = "http://localhost:8080/wisdom/umbrella/thirdParty/alipayment";
    //private static String url = "http://xiaork.com/umbrella/checkFamilyMembers";
    //private static String url = "http://xiaork.com/umbrella/checkFamilyMembers";
    private static final int MAX_THREADS_COUNT = 1;//初始化开启最大线程数
    //private static final int MAX_THREADS_COUNT = 1;
    private static final int MESSAGE_COUNT = 1;//模拟一个用户向服务器发送的消息总数

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
            System.out.println("线程" +i);
        }
    }

    public class TestSimulator implements Runnable {

        private TestUmbrellaAliPay testUmbrellaDemo  = new TestUmbrellaAliPay();;
        private String totleFee = "0.01";
        private String body = "支付测试";
        private String threadName ;
        private String describe = "支付测试";
        private String showUrl = "http://www.baidu.com";


        public TestSimulator(int i,String threadName){
            this.threadName = threadName;
        }

        @Override
        public void run() {
            String url = "";
            url = testUmbrellaDemo.getUrl();
            //sendPost(url);
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("totleFee",totleFee);
                jsonObject.put("body", URLEncoder.encode(body, "UTF-8"));
                jsonObject.put("describe",URLEncoder.encode(describe, "UTF-8"));
                jsonObject.put("showUrl",showUrl);
                postByJson(url, jsonObject.toString());
            } catch (UnsupportedEncodingException e) {
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
        TestUmbrellaAliPay testConcurrentDemo = new TestUmbrellaAliPay();
        testConcurrentDemo.process();
        //System.out.println();
    }

}
