package com.cxqm.xiaoerke.common.utils;

import net.sf.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

/**
 * Created by jiangzhongge on 2016-7-12.
 */
public class CoopConsultUtil {

    /**
     * @author jiangzg 2016-7-13 12:19:06
     * @param url           http请求url
     * @param method        http请求类型
     * @param dataType      http请求数据类型
     * @param propertyJson  http请求headers参数
     * @param contentJson   http请求发送的数据
     * @param flag          1：代表contentJson发送流信息  2：代表contentJson为null 其他：代表contentJson数据是json形式发送
     * @return
     */
    public static String getCurrentUserInfo(String url, String method, String dataType, String propertyJson , String contentJson ,int flag) {
        URL sendCoopUrl;
        String reResult = null;
        BufferedReader reader = null;
        try {
            sendCoopUrl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) sendCoopUrl.openConnection();
            if("POST".equalsIgnoreCase(method)){
                httpURLConnection.setUseCaches(false); // post方式不能使用缓存
            }
            httpURLConnection.setRequestMethod(method);
            httpURLConnection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            //媒体类型可自行添加
            if(StringUtils.isNotNull(dataType)){
                if("json".equalsIgnoreCase(dataType)){
                    httpURLConnection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
                }else if(dataType.contains("xml") || dataType.contains("text/xml")){
                    httpURLConnection.setRequestProperty("Content-Type", "text/xml");
                }else if(dataType.contains("form-data") || dataType.contains("multipart")){
                    httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data"); //需要在表单中进行文件上传时，就需要使用该格式
                }else if(dataType.contains("x-www-form-urlencoded") || dataType.contains("application/x-www-form-urlencoded")){
                    httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    //<form encType="">中默认的encType，form表单数据被编码为key/value格式发送到服务器（表单默认的提交数据的格式）
                }else{
                    httpURLConnection.setRequestProperty("Content-Type", "application/octet-stream"); //二进制流数据（如常见的文件下载）
                }
            }else{
                return null ;
            }
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // 设置请求头信息
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            if(StringUtils.isNotNull(propertyJson)){
                JSONObject jsonObject = JSONObject.fromObject(propertyJson);
                Iterator it = jsonObject.keys();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    String value = jsonObject.getString(key);
                    System.out.println("======"+key+"======"+value);
                    httpURLConnection.setRequestProperty(key, value);
                }
            }
            if(flag ==1 ){  //以流形式推信息
                if(StringUtils.isNotNull(contentJson)){
                    OutputStream os = new DataOutputStream(httpURLConnection.getOutputStream());
                    JSONObject jsonObject = JSONObject.fromObject(contentJson);
                    Iterator it = jsonObject.keys();
                    String key = null ;
                    Object value = null ;
                    while (it.hasNext()) {
                        key = (String) it.next();
                        value = jsonObject.get(key);
                    }
                    DataInputStream in = new DataInputStream((InputStream)value);
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        os.write(bufferOut, 0, bytes);
                    }
                    in.close();
                    os.flush();
                    os.close();
                }else{
                    return null ;
                }
            }else if(flag == 2){ //以json为数据类型推送
                OutputStream os = httpURLConnection.getOutputStream();
                os.flush();
                os.close();
            }else{   ////以json为数据类型推送
                if(StringUtils.isNotNull(contentJson)){
                    OutputStream os = httpURLConnection.getOutputStream();
                    os.write(contentJson.getBytes("UTF-8"));// 传入参数
                    os.flush();
                    os.close();
                }else{
                    return null;
                }

            }
            //字节读取
            /*InputStream is = httpURLConnection.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            reResult = new String(jsonBytes, "UTF-8");
            System.out.println("请求返回结果:" + reResult);
            is.close();*/
            //字符读取
            StringBuffer buffer = new StringBuffer();
            reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"utf-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            /*System.out.println(buffer.toString());
            byte[] temp = buffer.toString().getBytes("gbk");
            System.out.println(new String(temp, "UTF-8"));*/
            if (reResult == null) {
                reResult = buffer.toString();
            }
            if(reader != null){
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reResult;
    }


    public static void main(String[] args) {
        String url = "http://rest.ihiss.com:9000/user/current";   //http://rest.ihiss.com:9000/user/children
        String token = "f09b10f3-a582-4164-987f-6663c1a7e82a";
        String access_token = "{'X-Access-Token':'" + token + "'}";
        String method = "GET";
        String dataType = "json";
        String str = getCurrentUserInfo(url, method, dataType, access_token,"",2);
        JSONObject jsonObject = JSONObject.fromObject(str);
        String imgUrl = (String)jsonObject.get("avatar");
        System.out.println("imgUrl===="+imgUrl);
        System.out.println(str);
    }
}
