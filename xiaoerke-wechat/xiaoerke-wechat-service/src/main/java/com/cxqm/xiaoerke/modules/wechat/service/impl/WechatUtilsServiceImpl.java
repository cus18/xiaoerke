package com.cxqm.xiaoerke.modules.wechat.service.impl;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.modules.sys.dao.UserDao;
import com.cxqm.xiaoerke.modules.wechat.service.WechatUtilsService;

/**
 * @author wangbaowei
 * @date 2015-11-04
 */

@Service
@Transactional(readOnly = false)
public class WechatUtilsServiceImpl implements WechatUtilsService{

	@Autowired
	private UserDao userDao;
	
	@Override
    public boolean checkUserAppointment(String openId){
	      int appointmentNum = userDao.checkUserAppointment(openId);
	      if(appointmentNum>0){
	          return false;
	      }
	        return true;
	    }
   
   /**
	 * 发起https请求并获取结果
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
}
