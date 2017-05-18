package com.cxqm.xiaoerke.common.utils;

import net.sf.json.JSONObject;
import org.json.JSONArray;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
          //String url = "http://rest.ihiss.com:9000/user/current";   //http://rest.ihiss.com:9000/user/children
        /*String url = "http://rest.tx2010.com/user/current";
        //String token = "f09b10f3-a582-4164-987f-6663c1a7e82a";
        String token = "211f3d56-f815-4e92-a7c9-804adcb6b531";
        String access_token = "{'X-Access-Token':'" + token + "'}";
        String method = "GET";
        String dataType = "json";
        String str = getCurrentUserInfo(url, method, dataType, access_token,"",2);*/
        /*JSONObject jsonObject = JSONObject.fromObject(str);
        String imgUrl = (String)jsonObject.get("avatar");
        System.out.println("imgUrl===="+imgUrl);*/
//        String star = "[{\"id\": 2302898,  \"birthday\" : \"2012-01-01\",  \"sex\" : 2,  \"name\" : \"雪花梨孩子\"}, {  \"id\" : 12323123,  \"birthday\" : \"2015-01-01\",  \"sex\" : 2,  \"name\" : \"kkkk\"}]";
//        JSONArray jsonArray = new JSONArray(str);
/*       for(int i= 0; i<jsonArray.length();i++){
            System.out.println(jsonArray.length());
            JSONObject jsonObject = JSONObject.fromObject(jsonArray.get(i).toString());
             System.out.println(jsonObject.get("name"));
        }*/
//        System.out.println(str);

        /*String currentUrl = "http://101.201.154.75/angel/consult/cooperate/getConsultDataByCoop";
        String method = "POST";
        String dataType="json";
        net.sf.json.JSONObject noReadMsg = new net.sf.json.JSONObject();
        noReadMsg.put("startTime","2016-08-24 10:30:00");
        noReadMsg.put("endTime","2016-10-10 16:30:00");
        noReadMsg.put("source","COOP_BHQ");
        noReadMsg.put("secCode","091016COOP_BHQ");
        String contentJson = "{\"startTime\":\"2016-08-24 10:30:00\",\"endTime\":\"2016-08-25 16:30:00\",\"source\":\"COOP_BHQ\",\"secCode\":\"091016COOP_BHQ\"}";
        String str = getCurrentUserInfo(currentUrl, method, dataType, null, noReadMsg.toString(), 4);
        System.out.println(str);

        String message = "分诊李军：好的";
        if(message.contains("：")){
            System.out.println(message.substring(message.indexOf("：")+1));
        }else{
            System.out.println(message);
        }*/
//        String url = "http://101.200.180.132:8081/backend/util/clearRedisData";
//        String method = "POST";
//        String dataType="json";
//        String str = getCurrentUserInfo(url, method, dataType, null, null, 2);
//        System.out.println(str);
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(1476106285 * 1000L)));
        /*String fileName = "123.213.jp.jpeg";
        fileName = UUID.randomUUID().toString().replaceAll("-","")+fileName.substring(fileName.lastIndexOf("."),fileName.length());
        System.out.println(fileName);


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int currentYear = calendar.get(calendar.YEAR) - 2000;
        int currentMonth = calendar.get(calendar.MONTH) + 1;
        int currentDate = calendar.get(calendar.DAY_OF_MONTH);
        int currentHour = calendar.get(calendar.HOUR_OF_DAY);
        System.out.println(currentHour);*/
        /*Random random = new Random();
        int maxRandom = 92 ;
        int minRandom = 90 ;
        for(int i = 0;i<20;i++){
            int daShang = random.nextInt(3)+minRandom ;
            System.out.println(daShang);
        }*/
       /* float num = (float) 90 / 200*100;
        DecimalFormat df = new DecimalFormat("0");//格式化小数
        String s = df.format(num);//返回的是String类型
        System.out.println(s);*/
       // 2017-5-18 13:46:45
       /* StringBuilder sb = new StringBuilder();
        sb.append("点击领取：");
        sb.append("<a href=''>");
        sb.append("疫苗接种告知单及相关知识>>");
        sb.append("</a>");
        sb.append("\n\n");
        sb.append("如有疼痛发热等症状及其他育儿问题，点击左下角");
        sb.append("\n");
        sb.append("\"小键盘\"，即可咨询儿科专家医生");
        sb.append("\n");
        sb.append("预防接种科咨询时间：19：00—21：00");
        System.out.println(sb.toString());
        String eventKey = "YMJZ_AH_02";
        String marketer = "";
        if(eventKey.contains("qrscene_")){
            marketer = eventKey.replace("qrscene_", "").trim();
        }else{
            marketer = eventKey.trim();
        }
        marketer = marketer.substring(marketer.lastIndexOf("_")+1,marketer.length());
        System.out.println(marketer);
        int marketerId = 0 ;
        if(marketer.startsWith("0")){
            marketerId = Integer.valueOf(marketer.substring(1));
        }else{
            marketerId = Integer.valueOf(marketer);
        }
        System.out.println(marketerId);
        */
        /**
         * 2017-5-18 13:47:23 测试微信发送消息
         * (String url, String method, String dataType, String propertyJson , String contentJson ,int flag)
         */
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=gx8ouSxK4R4DaRj48xhKZszULccyRU1WxGCgsgxUsvTg6oIwrZAFTZTkphPbU79_xMuRIkOiGlFVuRk6NuSihHtVTnX55xyGlnYScjJ02zYnhiMFnA2bTRKRfztmQiW4FNHgADASAU";
        String method = "POST";
        String dataType = "json";
        String propertyJson = "";
        String contentJson = "{\"touser\":\"o3_NPwsmy0Qb7Bo-C7QOTzppIRzU\",\"msgtype\":\"text\",\"text\":{\"content\":\"姜忠阁测试你好123，收到请在微信告诉我\"}}";
        int flag = 4 ;
        String data = CoopConsultUtil.getCurrentUserInfo(url,method,dataType,propertyJson,contentJson,flag);
        System.out.print(data);
    }
}
