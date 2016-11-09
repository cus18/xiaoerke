package com.cxqm.xiaoerke.modules.sys.utils;

import com.cxqm.xiaoerke.common.bean.TemplateData;
import com.cxqm.xiaoerke.common.bean.WxTemplate;
import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.utils.HttpRequestUtil;
import com.cxqm.xiaoerke.common.utils.JsonUtil;
import com.cxqm.xiaoerke.common.utils.PropertiesLoader;

import com.cxqm.xiaoerke.common.utils.StringUtils;
import net.sf.json.JSONObject;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.InputSource;

import java.io.*;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.jdom.Document;
//import org.jdom.Element;
//import org.jdom.input.SAXBuilder;

/**
 *
 * @author Administrator
 */
public class ChangzhuoMessageUtil {


	private static String account = "xiaoerke";
	private static String password = "682497";
	public final static String RECEIVER_TYPE_USER = "RECEIVER_TYPE_USER";
	public final static String RECEIVER_TYPE_DOCTOR = "RECEIVER_TYPE_DOCTOR";
	public final static String RECEIVER_TYPE_ADMIN = "RECEIVER_TYPE_ADMIN";
	public final static String MSG_SWITCH_STATUS_ON = "on";
	public final static String MSG_SWITCH_STATUS_OFF = "off";
	
	public static String getConnectionResult(String urlPath,String msg){
		try {
			URL url;
			url = new URL(urlPath);
			HttpURLConnection connection=(HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setReadTimeout(10000);  //超时时间
			OutputStream os=connection.getOutputStream();
			BufferedOutputStream bos=new BufferedOutputStream(os);
			bos.write(msg.getBytes("utf-8"));
			bos.close();
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


	public static void sendMsgToManagerForAccount(String content,String token){
		String managerPhone = Global.getConfig("webapp.ManagerOpenid");
		String [] st = managerPhone.split(",");
		for(String str:st){
			templateModel("预约成功","宝大夫",content,"","","请及时查看",token,"",str,"jSCx1TKBj2Hx8E3HGha4U4BqKufy7E-oIWjklQsx2ko");
		}
	}

	public static String SMS(String postData, String postUrl) {
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


	public static void getinf(String content) {
		String postUrl = "http://sms.chanzor.com:8001/sms.aspx";
		String postData = "action=checkkeyword&userid=&account=" + account
				+ "&password=" + password + "&content=" + content + "";
		String result = SMS(postData, postUrl);
		System.out.println(result);
	}


	public static void getInMsgInfo() throws Exception {
		String postUrl_utf = "http://sms.chanzor.com:8001/sms.aspx";
		String postData = "action=overage&userid=&account=" + account
				+ "&password=" + password + "";
		String result = SMS(postData, postUrl_utf);
//		ChangzhuaoMessageBean bean = parseXml(result);
//		System.out.println(bean.getPayinfo());
	}

	public static String sendMsg(String phoneNum, String content) {
		String msgStatus = Global.getConfig("shortMessageSwitch");
		if("on".equals(msgStatus)&&phoneNum!=""&&content !=null){
			System.out.print("短信发送："+phoneNum+"|"+content);
			content = content+"【宝大夫】";
			String msgSendUrl_utf = "http://sms.chanzor.com:8001/sms.aspx";
			String postData = "action=send&account=" + account + "&password="
					+ password + "&mobile=" + phoneNum + "&content=" + content
					+ "&sendTime=";
			String result = getConnectionResult(msgSendUrl_utf, postData);
//			JSONObject jsonObject = JSONObject.fromObject(result);
			try {
				ChangzhuaoMessageBean messageBean = parseXml(result);
				String status = messageBean.getReturnstatus();
				String Prompt = messageBean.getMessage();
				if("Faild".equals(status)){
					LogUtils.saveLog("00000008",phoneNum+"-"+content+"-"+Prompt);//00000008短信
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		return "";
	}

	/**
	 * 
	 * @param phoneNum
	 * @param content
	 * @param receiverType the type of receiver
	 * @return
	 */
	public static String sendMsg(String phoneNum, String content, String receiverType) {
		String msgStatus = Global.getConfig("shortMessageSwitch");
		if("on".equals(msgStatus)&&phoneNum!=""&&content !=null){
			String msgSwitchStatus4Doctor = Global.getConfig("shortMessageSwitch.receiverType.doctor");
			if(RECEIVER_TYPE_DOCTOR.equals(receiverType) && !MSG_SWITCH_STATUS_ON.equals(msgSwitchStatus4Doctor))
				return "";
			System.out.print("短信发送："+phoneNum+"|"+content);
//			content = content+"【宝大夫】";
			String msgSendUrl_utf = "http://sms.chanzor.com:8001/sms.aspx";
			String postData = "action=send&account=" + account + "&password="
					+ password + "&mobile=" + phoneNum + "&content=" + content
					+ "&sendTime=";
			String result = getConnectionResult( msgSendUrl_utf,postData);
//			JSONObject jsonObject = JSONObject.fromObject(result);
			try {
			  ChangzhuaoMessageBean messageBean = parseXml(result);
			  String status = messageBean.getReturnstatus();
			  String Prompt = messageBean.getMessage();
			  LogUtils.saveLog("00000009:",phoneNum+"-"+content+"-"+Prompt);//定时器短信
			} catch (Exception e) {
				e.printStackTrace();
			}

			return result;
		}
		return "";
	}
	
	public static void sendStatue() {
		String postUrl = "http://sms.chanzor.com:8001/statusApi.aspx";
		String postData = "action=query&userid=&account=" + account
				+ "&password=" + password + "";
		String result = SMS(postData, postUrl);
		System.out.println(result);
	}

	public static void getMessageInfo() {
		String postUrl = "http://sms.chanzor.com:8001/callApi.aspx";
		String postData = "action=query&userid=&account=" + account
				+ "&password=" + password + "";
		String result = SMS(postData, postUrl);
		System.out.println(result);

	}

	public static void main(String[] args) {
		try {
			// getInMsgInfo();
			// getinf("签证");
			// sendStatue();
//			sendIdentifying("13161046284");
//			String xml = sendMsg("18618323267","尊敬的陈奕鑫小朋友家长您好，感谢您关注宝大夫。我是健康顾问梁医生，您预约了7月25号 09:45分 儿研所皮肤科刘晓雁的专家号，此号源为特需号，挂号费为300元，可能在平台上暂时没有显示，特此告知，如给您带来不变 ，敬请谅解。如需更改或其他医疗方面的疑问，敬请咨询400-6237-120");
//	System.out.print(xml);
			ChangzhuoMessageUtil.receptionReminder("今日新增接诊", "2015-10-09", "1", "  尊敬的" + "郑玉巧" + "医生，" +
							"测试" + "宝宝及家长预约了您在" + "2015-10-10" + " " +"09:15" + "的门诊。",
					"若您因紧急情况不能按时出诊，请联系客服：400-623-7120。宝大夫祝您工作顺利.", "QcBkQVdlvXMMJttVO_kxZzo7oOSLJU14l6up10TVEGUa6iVVa0PKoJHf7A8iv0U47lJtggkWwerGhg5fjpIGFCPCuhfnRs03GotbZzOLQnI", "", "o3_NPwnpYevGPIQU4uXAK3RqNRe8");} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 返回的短信状态转换成实体
	 * */
	public static ChangzhuaoMessageBean parseXml(String request) throws Exception{
		ChangzhuaoMessageBean v = new ChangzhuaoMessageBean();
		// 将解析结果存储在HashMap�?
//		Map<String, String> map = new HashMap<String, String>();
		   /* 直接传�?�String字符�?*/
        StringReader read = new StringReader(request);
        InputSource source = new InputSource(read);
        SAXBuilder sb = new SAXBuilder();
        Document doc = sb.build(source);
        Element root = doc.getRootElement();
        List<Element> list = root.getChildren();
        for (int i = 0; i < list.size(); i++) {
        	Element element = list.get(i);
            String name = element.getName();
            String value = element.getText();
            String methodName = "set" +captureName(name);
            Method m = v.getClass().getMethod(methodName, new Class[] { String.class });
            m.invoke(v, new Object[] { value });
        }
        return v;

	}
	/**
	 * 首字母转大写
	 * */
	public static String captureName(String name) {
		char[] cs=name.toCharArray();
		cs[0]-=32;
		return String.valueOf(cs);
	}

	public static String createRandom(boolean numberFlag, int length){
		String retStr = "";
		String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
		int len = strTable.length();
		boolean bDone = true;
		do {
			retStr = "";
			int count = 0;
			for (int i = 0; i < length; i++) {
				double dblR = Math.random() * len;
				int intR = (int) Math.floor(dblR);
				char c = strTable.charAt(intR);
				if (('0' <= c) && (c <= '9')) {
					count++;
				}
				retStr += strTable.charAt(intR);
			}
			if (count >= 2) {
				bDone = false;
			}
		} while (bDone);

		return retStr;
	}
/**
 * 将用户的订购信息提供给管理
 * */
	public static void sendMsgToManager(String content){

      String managerNum = "13601025165,13910933071,18911617317,13718256705,18301031173\n";
	  String xml = sendMsg(managerNum, content, RECEIVER_TYPE_DOCTOR);

	}

	public static String sendIdentifying(String phoneNum){
		if(phoneNum!=""){
			String identify = createRandom(true, 4);
			String content = "验证码：" + identify + ",欢迎使用宝大夫，为宝宝的健康保驾护航";
			content = content+"【宝大夫】";
			String msgSendUrl_utf = "http://sms.chanzor.com:8001/sms.aspx";
			String postData = "action=send&account=" + account + "&password="
					+ password + "&mobile=" + phoneNum + "&content=" + content
					+ "&sendTime=";
			String result = getConnectionResult( msgSendUrl_utf,postData);
			System.out.println(result);
			return identify;
		}
		return "";
	}


//	多客服接口
	public static  void sendMsgToWechat(String token,String openId,ArrayList<Object> obj){
		String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+ token;
		try {
			String str = JsonUtil.getJsonStrFromList(obj);
			String json = "{\"touser\":\""+openId+"\",\"msgtype\":\"news\",\"news\":" +
					"{\"articles\":" +str +"}"+"}";
			System.out.println(json);
			String result = HttpRequestUtil.httpPost(json, url);
			System.out.print(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 接诊提醒
	 * @param first 说明
	 * @param keyword1 接诊时间
	 * @param keyword2 接诊人数
	 * @param remark 注意提示
	 * @param token 微信token
	 * @param url 模板链接地址
	 * */
	public static void billReminderEveryDay(String first,String keyword1,String keyword2,String remark,String token,String url,String openid){
		WxTemplate t = new WxTemplate();
		t.setUrl(url);
		t.setTouser(openid);
		t.setTopcolor("#000000");
		t.setTemplate_id("Y35f0JnXhPs9e22dGEaw6NF8qlC2hnGARoqHFJdaN5g");
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();

		TemplateData templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(first);
		m.put("first", templateData);

		templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(keyword1);
		m.put("keyword1", templateData);

		templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(keyword2);
		m.put("keyword2", templateData);

		templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(remark);
		m.put("remark", templateData);

		t.setData(m);
		System.out.print( JSONObject.fromObject(t).toString());

		String jsonobj = HttpRequestUtil.httpsRequest("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token+"","POST", JSONObject.fromObject(t).toString());
		JSONObject jsonObject = JSONObject.fromObject(jsonobj);
		if(jsonobj!=null){
			if("0".equals(jsonObject.getString("errcode"))){
				System.out.println("发送模板消息成功！");
			}else{
				System.out.println(jsonObject.getString("errcode"));
			}
		}


	}

	/**
	 * 接诊提醒
	 * @param first 说明
	 * @param keyword1 接诊时间
	 * @param keyword2 接诊人数
	 * @param keyword3 患者名单
	 * @param remark 注意提示
	 * @param token 微信token
	 * @param url 模板链接地址
	 * */
	public static void receptionReminder(String first,String keyword1,String keyword2,String keyword3,String remark,String token,String url,String openid){
		WxTemplate t = new WxTemplate();
		t.setUrl(url);
		t.setTouser(openid);
		t.setTopcolor("#000000");
		t.setTemplate_id("1joU-7Q4eiqoGsRmZ0zVRvgmlzqTs4jzW50KUfEgXIg");
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();

		TemplateData templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(first);
		m.put("first", templateData);

		templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(keyword1);
		m.put("keyword1", templateData);

		templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(keyword2);
		m.put("keyword2", templateData);

		templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(keyword3);
		m.put("keyword3", templateData);

		templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(remark);
		m.put("remark", templateData);

		t.setData(m);
		String jsonobj = HttpRequestUtil.httpsRequest("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+
				token+"","POST", JSONObject.fromObject(t).toString());
		JSONObject jsonObject = JSONObject.fromObject(jsonobj);
		if(jsonobj!=null){
			if("0".equals(jsonObject.getString("errcode"))){
				System.out.println("发送模板消息成功！");
			}else{
				System.out.println(jsonObject.getString("errcode"));
			}
		}
	}

	public static void evaluateRemind(String first,String keyword1,String keyword2,String remark,String token,String url,String openid){
		WxTemplate t = new WxTemplate();
		t.setUrl(url);
		t.setTouser(openid);
		t.setTopcolor("#000000");
		t.setTemplate_id("NBPM7A-baR6cij7xrggjNe2GD3sf2MPmjZX2b0AC-f0");
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();

		TemplateData templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(first);
		m.put("first", templateData);

		templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(keyword1);
		m.put("keyword1", templateData);

		templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(keyword2);
		m.put("keyword2", templateData);

		templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(remark);
		m.put("remark", templateData);

		t.setData(m);
		String jsonobj = HttpRequestUtil.httpsRequest("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+
				token+"","POST", JSONObject.fromObject(t).toString());
		JSONObject jsonObject = JSONObject.fromObject(jsonobj);
		if(jsonobj!=null){
			if("0".equals(jsonObject.getString("errcode"))){
				System.out.println("发送模板消息成功！");
			}else{
				System.out.println(jsonObject.getString("errcode"));
			}
		}

	}
	/**
	 * 新增接诊提醒
	 * @param first 说明
	 * @param keyword1 接诊时间
	 * @param keyword2 接诊人数
	 * @param keyword3 患者名单
	 * @param remark 注意提示
	 * @param token 微信token
	 * @param url 模板链接地址
	 * */
	public static void appointmentReminder(String first,String keyword1,String keyword2,String keyword3,String remark,String token,String url,String openid){
		WxTemplate t = new WxTemplate();
		t.setUrl(url);
		t.setTouser(openid);
		t.setTopcolor("#000000");
		t.setTemplate_id("1joU-7Q4eiqoGsRmZ0zVRvgmlzqTs4jzW50KUfEgXIg");
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();

		TemplateData templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(first);
		m.put("first", templateData);

		templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(keyword1);
		m.put("keyword1", templateData);

		templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(keyword2);
		m.put("keyword2", templateData);

		templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(keyword3);
		m.put("keyword3", templateData);

		templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(remark);
		m.put("remark", templateData);

		t.setData(m);
		String jsonobj = HttpRequestUtil.httpsRequest("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token+"","POST", JSONObject.fromObject(t).toString());
		JSONObject jsonObject = JSONObject.fromObject(jsonobj);
		if(jsonobj!=null){
			if("0".equals(jsonObject.getString("errcode"))){
				System.out.println("发送模板消息成功！");
			}else{
				System.out.println(jsonObject.getString("errcode"));
			}
		}
	}

	/**
	 * 公共模板模型
	 * @param first 模板标题
	 * @param keyword1 keyword1内容
	 * @param keyword2 keyword2内容
	 * @param keyword3 keyword3内容
	 * @param keyword4 keyword4内容
	 * @paracm remark remark详细说明
	 * @paracm token 维系in公众平台token
	 * @paracm url 模板链接地址
	 * @paracm openid 接收消息用户
	 * @paracm templateId 模板id
	 *
	 * */
	public static void templateModel(String first,String keyword1,String keyword2,String keyword3,String keyword4,String remark,String token,String url,String openid,String templateId){
		WxTemplate t = new WxTemplate();
		if(StringUtils.isNotNull(url))t.setUrl(url);
		t.setTouser(openid);
		t.setTopcolor("#000000");
		t.setTemplate_id(templateId);
		Map<String,TemplateData> m = new HashMap<String,TemplateData>();

		TemplateData templateData = new TemplateData();
		templateData.setColor("#000000");
		templateData.setValue(first);
		m.put("first", templateData);

		if(StringUtils.isNotNull(keyword1)){
			templateData = new TemplateData();
			templateData.setColor("#000000");
			templateData.setValue(keyword1);
			m.put("keyword1", templateData);
		}

		if(StringUtils.isNotNull(keyword2)){
			templateData = new TemplateData();
			templateData.setColor("#000000");
			templateData.setValue(keyword2);
			m.put("keyword2", templateData);
		}

		if(StringUtils.isNotNull(keyword3)){
			templateData = new TemplateData();
			templateData.setColor("#000000");
			templateData.setValue(keyword3);
			m.put("keyword3", templateData);
		}

		if(StringUtils.isNotNull(keyword4)){
			templateData = new TemplateData();
			templateData.setColor("#000000");
			templateData.setValue(keyword4);
			m.put("keyword4", templateData);
		}

		if(StringUtils.isNotNull(remark)){
			templateData = new TemplateData();
			templateData.setColor("#000000");
			templateData.setValue(remark);
			m.put("remark", templateData);
		}
		t.setData(m);

		String jsonobj = HttpRequestUtil.httpsRequest("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+
				token+"","POST", JSONObject.fromObject(t).toString());
		JSONObject jsonObject = JSONObject.fromObject(jsonobj);
		if(jsonobj!=null){
			if("0".equals(jsonObject.getString("errcode"))){
				System.out.println("发送模板消息成功！");
			}else{
				System.out.println(jsonObject.getString("errcode"));
			}
		}
	}

}
