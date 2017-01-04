package com.cxqm.xiaoerke.webapp.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by guozengguang on 2016/4/22.
 */
public class TestSimulator implements Runnable {

    private TestConcurrentDemo testConcurrentDemo  = new TestConcurrentDemo();;
    private static final String MsgType = "MsgType";//模拟微信端发送的消息类型
    private static final String TEXT = "text";
    private static final String FromUserName = "FromUserName";//模拟微信端用户id
    private String userNo = "";
    private String name ;


    public TestSimulator(int i,String name){
        this.name = name;

        String openId = String.valueOf(i);
        if(openId.length() == 1){
            userNo = "a000" + openId;
            //userNo = "b000" + openId;
        }else if(openId.length() == 2){
            userNo = "a00" + openId;
        }else if(openId.length() == 3){
            userNo = "a0" + openId;
        }else if(openId.length() == 4){
            userNo = "a" + openId;
        }
        //userNo = String.valueOf(i);
        //testConcurrentDemo = new TestConcurrentDemo();
    }

    @Override
    public void run() {
        String url = "";
        url = testConcurrentDemo.getUrl();
            //sendPost(url);
        for (int i = 1; i <= testConcurrentDemo.getMessageCount(); i++) {
            try {
                    sendPost(url,i);
//                    Thread.sleep(3 * 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    public void sendPost(String url,int messageNo){
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            System.out.println(Thread.currentThread().getName());

            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            //发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());

            // 发送请求参数
            //String param = "MsgType=" + TEXT +
            //    "&FromUserName=" + userNo +
            //    "&messageContent=a";
            String param = "<xml>" + " <ToUserName><![CDATA[toUser]]></ToUserName>"
                + " <FromUserName>" + userNo + "-" +  messageNo + "</FromUserName>"
                + " <CreateTime>1348831860</CreateTime>" + " <MsgType>text</MsgType>"
                + " <Content>" + userNo +  "</Content>"
                + " <MsgId>1234567890123456</MsgId>" + " </xml>";
            out.print(param);

            //flush输出流的缓冲
            out.flush();
            //定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line + "\n";
            }
            System.out.println(result);
            //try {
            //    Thread.sleep(3000);
            //    sendPost(url);
            //} catch (InterruptedException e) {
            //    e.printStackTrace();
            //}
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e.getMessage());
            e.printStackTrace();
        } finally{//使用finally块来关闭输出流、输入流
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
    }
}


