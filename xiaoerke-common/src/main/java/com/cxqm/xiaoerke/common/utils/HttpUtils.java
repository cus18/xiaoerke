package com.cxqm.xiaoerke.common.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP 工具类
 * 
 * @author whs
 *
 */
public class HttpUtils {
	
	/**
	 * 日志对象
	 */
	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	
	public static String doGet(String url) {

		String response = "";
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();

		HttpGet httpGet = new HttpGet(url);
		
		logger.info("Http请求地址：" + httpGet.getRequestLine());
		try {
			// 执行get请求
			HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
			// 获取响应消息实体
			HttpEntity entity = httpResponse.getEntity();
			// 响应状态
			logger.info("Http响应码:" + httpResponse.getStatusLine());
			// 判断响应实体是否为空
			if (entity != null) {
				response = EntityUtils.toString(entity);
				logger.info("Http响应信息:" + response);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭流并释放资源
				closeableHttpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return response;
	}
	
	/**
     * HTTP Post 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params 请求的参数
     * @param charset    编码格式
     * @return    页面内容
     */
    public static String doPost(String url,Map<String,String> params,String charset){
        if(StringUtils.isBlank(url)){
            return null;
        }
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        try {
            List<NameValuePair> pairs = null;
            if(params != null && !params.isEmpty()){
                pairs = new ArrayList<NameValuePair>(params.size());
                for(Map.Entry<String,String> entry : params.entrySet()){
                    String value = entry.getValue();
                    if(value != null){
                        pairs.add(new BasicNameValuePair(entry.getKey(),value));
                    }
                }
            }
            HttpPost httpPost = new HttpPost(url);
            if(pairs != null && pairs.size() > 0){
                httpPost.setEntity(new UrlEncodedFormEntity(pairs,charset));
            }
            if(StringUtils.isNotBlank(params.get("Authorization"))){
            	httpPost.addHeader("Authorization", params.get("Authorization"));
            }
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity, charset);
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String doPost(String url,Map<String,String> params){
    	String charset = "UTF-8";
    	return doPost(url, params, charset);
    }
    
	public static String postJson() throws Exception {
		RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
		CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

		//String url = "http://172.21.32.112:18080/ErxCloud/restservices/Login/getLogin";
		String url = "http://121.40.64.44:18080/ErxCloud/restservices/Login/getLogin";
		//String url = "http://121.199.8.229:80/ErxCloud/restservices/Login/getLogin";
		HttpPost method = new HttpPost(url);

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		Map<String, String> params = new HashMap<String, String>();
		//params.put("requestdata", "{\"channelId\":\"1422527604293\",\"msgUserid\":\"Aoy_1TSkPhbG0-DUuYoXQagtS11f9hhMRi26QBO4d_gm\",\"password\":\"2D576ED180C468B8C2888D037CEE5CE3\",\"username\":\"18611867932\"}");
		//params.put("requestdata","{\"channelId\":\"1422866946947\",\"msgUserid\":\"152e86775da856654cb5f99f45524f6eaac5424d339216b1f33404b1b7ca1b80\",\"password\":\"830BF0D448D2EBD313AAB817B35359C1\",\"username\":\"18510120253\"}");
		params.put("requestdata","{\"channelId\":\"1422866946947\",\"msgUserid\":\"AlmobGkHvaijUjLVXckZoNcraSwq-Oivgps-OXuEPhL6\",\"password\":\"B4B7FBFD0D464F50AC89F7A36CA7562E\",\"username\":\"18600640785\"}");
		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
		}

		method.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

		HttpResponse result = httpClient.execute(method);
		HttpEntity entity1 = result.getEntity();
		System.out.println(EntityUtils.toString(entity1));
		Header[] headers = result.getAllHeaders();
		String sessionid = "";
		for (Header header : headers) {
			String name = header.getName();
			if (name.equals("Set-Cookie")) {
				String value = header.getValue();
				sessionid = value.split(";")[0].split("=")[1];
				System.out.println(sessionid);
			}

		}
		method.releaseConnection();

		// String url2 = "http://172.21.32.21:9191/openhis/public/api/v2/relationship/getRelations.json?userId=60&relType=d-p&status=confirm";
		//String url2 = "http://172.21.32.82:8080/API/public/api/v2/relationship/getRelations.json?userId=60&relType=d-p&status=confirm";
		//String url2 = "http://172.21.32.82:8080/API/public/api/v2/relationship/getRelations.json?userId=60&relType=d-p&status=confirm";
		String url2 = "http://health.alijk.com/API/public/api/v2/relationship/getRelations.json?userId=60&relType=d-p&status=confirm";
		HttpPost method2 = new HttpPost(url2);
		Map<String, Object> params2 = new HashMap<String, Object>();
		params2.put("userId", 60);
		params2.put("relType", "d-p");
		params2.put("status", "confirm");
		Set<String> keySet2 = params2.keySet();
		List<NameValuePair> nvps2 = new ArrayList<NameValuePair>();
		for (String key : keySet2) {
			nvps2.add(new BasicNameValuePair(key, params2.get(key).toString()));
		}
		//String sessionid = "A8FA288E628524CA6D4044FDD1D75CC6-n1";
		System.out.println(sessionid);
		method2.addHeader("cookie", "JSESSIONID=" + sessionid);
		method2.addHeader("Content-Type", "application/json");
		method2.setEntity(new UrlEncodedFormEntity(nvps2, HTTP.UTF_8));
		HttpResponse result2 = httpClient.execute(method2);
		Header[] headers2 = result2.getAllHeaders();
		HttpEntity entity = result2.getEntity();
		System.out.println(EntityUtils.toString(entity));

		return "";
	}
	
	public static void testMemcachedSession() throws Exception{
		RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
		CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		
		//String url = "http://localhost:8080/openhis-webapp/public/api/r/v1/user/testSession.json";
		String url = "http://172.21.32.82:8080/API/public/api/r/v1/user/testSession.json";
		HttpPost method = new HttpPost(url);

		HttpResponse result = httpClient.execute(method);
		HttpEntity entity1 = result.getEntity();
		System.out.println(EntityUtils.toString(entity1));
		Header[] headers = result.getAllHeaders();
		String sessionid = "";
		for (Header header : headers) {
			String name = header.getName();
			if (name.equals("Set-Cookie")) {
				String value = header.getValue();
				sessionid = value.split(";")[0].split("=")[1];
				System.out.println(sessionid);
			}

		}
		method.releaseConnection();

		// String url2 = "http://172.21.32.21:9191/openhis/public/api/v2/relationship/getRelations.json?userId=60&relType=d-p&status=confirm";
		//String url2 = "http://172.21.32.82:8080/API/public/api/v2/relationship/getRelations.json?userId=60&relType=d-p&status=confirm";
		String url2 = "http://172.21.32.82:8080/API/public/api/v2/relationship/getRelations.json?userId=60&relType=d-p&status=confirm";
		HttpPost method2 = new HttpPost(url2);
		Map<String, Object> params2 = new HashMap<String, Object>();
		params2.put("userId", 60);
		params2.put("relType", "d-p");
		params2.put("status", "confirm");
		Set<String> keySet2 = params2.keySet();
		List<NameValuePair> nvps2 = new ArrayList<NameValuePair>();
		for (String key : keySet2) {
			nvps2.add(new BasicNameValuePair(key, params2.get(key).toString()));
		}
		//String sessionid = "A8FA288E628524CA6D4044FDD1D75CC6-n1";
		System.out.println(sessionid);
		method2.addHeader("cookie", "JSESSIONID=" + sessionid);
		method2.addHeader("Content-Type", "application/json");
		method2.setEntity(new UrlEncodedFormEntity(nvps2, HTTP.UTF_8));
		HttpResponse result2 = httpClient.execute(method2);
		Header[] headers2 = result2.getAllHeaders();
		HttpEntity entity = result2.getEntity();
		System.out.println(EntityUtils.toString(entity));

		
	}


	public static String getRealIp(SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo) throws SocketException {
			String[] ret = null;
			try {
				/**获得主机名*/
				String hostName = getLocalHostName();
				if(hostName.length()>0) {
					/**在给定主机名的情况下，根据系统上配置的名称服务返回其 IP 地址所组成的数组。*/
					InetAddress[] addrs = InetAddress.getAllByName(hostName);
					if(addrs.length>0) {
						ret = new String[addrs.length];
						for(int i=0 ; i< addrs.length ; i++) {
							/**.getHostAddress()   返回 IP 地址字符串（以文本表现形式）。*/
							ret[i] = addrs[i].getHostAddress();
						}
					}
				}

			}catch(Exception ex) {
				ret = null;
			}
		for(String s:ret){
			if(s.contains(sysPropertyVoWithBLOBsVo.getFirstAddress()) || s.contains(sysPropertyVoWithBLOBsVo.getSecondAddress())){
				return s;
			}
		}

		String s = sysPropertyVoWithBLOBsVo.getFirstAddress();
		LogUtils.saveLog("ip address",s);
		return s;
	}


	/**
	 * 或者主机名：
	 * @return
	 */
	public static String getLocalHostName() {
		String hostName;
		try {
			/**返回本地主机。*/
			InetAddress addr = InetAddress.getLocalHost();
			/**获取此 IP 地址的主机名。*/
			hostName = addr.getHostName();
		}catch(Exception ex){
			hostName = "";
		}

		return hostName;
	}

	public static void main(String[] args){
/*		String str = "";
		try {
			str = URLEncoder.encode("中文Ab12~`!@#$%^&*()-+={}[]\\|<>? 【阿里健康】", "GB2312");
			//str = new String("ceshi中文".getBytes("UTF-8"),"GB2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = "http://114.255.71.158:8061/?username=esysj&password=esysj321&message="+str+"&phone=15210035282&epid=109698&linkid=&subcode=";
		System.out.println(url);
		String result = HttpUtils.doGet(url);
		System.out.println(result);*/
		//http://localhost:8080/webapp/oauth/token.json?client_secret=12345678&client_id=androidClient&username=admin&password=admin&grant_type=password&scope=test
		try {
			HttpUtils.testMemcachedSession();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
/*		Map<String,String> params = new HashMap<String,String>();
		params.put("grant_type","password");
		params.put("scope","test");
		params.put("username", "admin");
		params.put("password", "admin");
		params.put("client_id", "androidClient");
		params.put("client_secret", "12345678");
		String str = HttpUtils.doPost("http://localhost:8080/webapp/oauth/token.json", params, "UTF-8");
		System.out.println(str);*/
		
	}
}
