package com.cxqm.xiaoerke.common.utils;

import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import net.sf.json.util.JSONTokener;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import sun.applet.Main;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;

/**
 * Created by baoweiw on 2015/7/27.
 */
public class HttpRequestUtil {

    /**
     * 服务端发起get请求
     * @param url
     * @return
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        HttpClient client = new DefaultHttpClient();
        HttpResponse resp = client.execute(httpGet);

        HttpEntity entity = resp.getEntity();
        String respContent = EntityUtils.toString(entity, "utf-8").trim();
        httpGet.abort();
        client.getConnectionManager().shutdown();

        return respContent;
    }

    /**
     * 服务端发起post请求
     * @param url
     * @param content
     * @return
     * @throws IOException
     */
    public static int post(String url, String content) throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        try {
            httppost.setEntity(new StringEntity(content, "utf-8"));
            HttpResponse response = httpclient.execute(httppost);
            return response.getStatusLine().getStatusCode();
        } catch (IOException e) {
            e.printStackTrace();
            return 500;
        }
    }


    public static JSONObject postByJson(String url,String json){
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
                    response = new JSONObject(line);
                }

//                response = new JSONObject(new JSONTokener(new InputStreamReader(is));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }


    public static String httpPost(String postData, String postUrl) {
        try {
            URL url = new URL(postUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setUseCaches(false);
            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Length", "" + postData.length());
            OutputStreamWriter out = new OutputStreamWriter(
                    conn.getOutputStream(), "UTF-8");
            out.write(postData);
            out.flush();
            out.close();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("connect failed!");
                return "";
            }
            String line, result = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), "utf-8"));
            while ((line = in.readLine()) != null) {
                result += line + "\n";
            }
            in.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        return "";
    }


    /**
     * 发起https请求并获取结果
     *
     * @param urlPath 请求地址
     * @param method 请求方式（GET、POST）
     * @param content 提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static String getConnectionResult(String urlPath,String method,String content){
        try {
            URL url;
            url = new URL(urlPath);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod(method.toUpperCase());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setReadTimeout(10000);  //超时时间
            if ("GET".equalsIgnoreCase(method))connection.connect();
            if(!content.isEmpty()){
                OutputStream os=connection.getOutputStream();
                BufferedOutputStream bos=new BufferedOutputStream(os);
                bos.write(content.getBytes("utf-8"));
                bos.flush();
                bos.close();
            }
            InputStream is=connection.getInputStream();
            String str="";
            StringBuffer outputValue=new StringBuffer();
            BufferedReader br=new BufferedReader(new InputStreamReader(is, "utf-8"));
            while((str=br.readLine())!=null)
            {
                outputValue.append(str);
                outputValue.append("\n");
            }
            br.close();
            String result=outputValue.toString();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送https请求
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return 返回微信服务器响应的信息
     */
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



//    public static String httpsRequest1(String requestUrl, String requestMethod, String outputStr) {
//        try {
//            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
//            TrustManager[] tm = { new MyX509TrustManager() };
//
//            KeyStore keyStore  = KeyStore.getInstance("PKCS12");
//            FileInputStream instream = new FileInputStream(new File("/Users/wangbaowei/Downloads/cert/apiclient_cert.p12"));
//            try {
//                keyStore.load(instream, ConstantUtil.ARTNER.toCharArray());
//            } finally {
//                instream.close();
//            }
//            SSLContext sslContext = SSLContexts.custom()
//                    .loadKeyMaterial(keyStore,ConstantUtil.PARTNER.toCharArray())
//                    .build();
//
//
//
//
////            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
//            sslContext.init(null, tm, new java.security.SecureRandom());
//            // 从上述SSLContext对象中得到SSLSocketFactory对象
//            SSLSocketFactory ssf = sslContext.getSocketFactory();
//            URL url = new URL(requestUrl);
//            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//            conn.setSSLSocketFactory(ssf);
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setUseCaches(false);
//            // 设置请求方式（GET/POST）
//            conn.setRequestMethod(requestMethod);
//            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
//            // 当outputStr不为null时向输出流写数据
//            if (null != outputStr) {
//                OutputStream outputStream = conn.getOutputStream();
//                // 注意编码格式
//                outputStream.write(outputStr.getBytes("UTF-8"));
//                outputStream.close();
//            }
//            // 从输入流读取返回内容
//            InputStream inputStream = conn.getInputStream();
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            String str = null;
//            StringBuffer buffer = new StringBuffer();
//            while ((str = bufferedReader.readLine()) != null) {
//                buffer.append(str);
//            }
//            // 释放资源
//            bufferedReader.close();
//            inputStreamReader.close();
//            inputStream.close();
//            inputStream = null;
//            conn.disconnect();
//            return buffer.toString();
//        } catch (ConnectException ce) {
//            ce.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }



    /**
     * 发送HttpPost请求
     *
     * @param strURL 服务地址
     * @param params json字符串,例如: "{ \"id\":\"12345\" }" ;其中属性名必须带双引号<br/>
     * @return 成功:返回json字符串<br/>
     */
    public static String wechatpost(String strURL, String params) {
        try {
            URL url = new URL(strURL);// 创建连接
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST"); // 设置请求方式
            connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter(
                    connection.getOutputStream(), "UTF-8"); // utf-8编码
            out.append(params);
            out.flush();
            out.close();
            // 读取响应
            int length = (int) connection.getContentLength();// 获取长度
            InputStream is = connection.getInputStream();
            if (length != -1) {
                byte[] data = new byte[length];
                byte[] temp = new byte[512];
                int readLen = 0;
                int destPos = 0;
                while ((readLen = is.read(temp)) > 0) {
                    System.arraycopy(temp, 0, data, destPos, readLen);
                    destPos += readLen;
                }
                String result = new String(data, "UTF-8"); // utf-8编码
                return result;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null; // 自定义错误信息
    }


    public static void main(String args[]) {
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";
    }


    public  static String clientCustomSSLS(String url,String postDate,SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo) throws Exception {
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        String i = System.getProperty("user.dir");
        System.out.print(i);
        FileInputStream instream = new FileInputStream(new File(i+"/apiclient_cert.p12"));
        try {
            keyStore.load(instream, sysPropertyVoWithBLOBsVo.getPartner().toCharArray());
        } finally {
            instream.close();
        }
        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore,sysPropertyVoWithBLOBsVo.getPartner().toCharArray())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity requestEntity = new StringEntity(postDate, "utf-8");
            requestEntity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(requestEntity);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(responseEntity.getContent() ,"utf-8"));
                    String text;
                    StringBuffer sbuf = new StringBuffer();
                    while ((text = bufferedReader.readLine()) != null) {
                        sbuf.append(text);
                    }
                    return sbuf.toString();
                }
                EntityUtils.consume(responseEntity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return "";
    }


}
