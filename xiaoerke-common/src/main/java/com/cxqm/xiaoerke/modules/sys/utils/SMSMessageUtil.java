package com.cxqm.xiaoerke.modules.sys.utils;

import com.cxqm.xiaoerke.common.config.Global;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import java.io.*;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 短信发送接口,现采用畅卓科技的短信接口
 * 该接口只涉及发送接口,其他接收以及状态查询并未集成
 * Created by wangbaowei on 15/11/30.
 */

public class SMSMessageUtil {

    //短信服务商账户
    private static String account = "xiaoerke";
    //短信服务商密码
    private static String password = "682497";
    public final static String RECEIVER_TYPE_USER = "RECEIVER_TYPE_USER";
    public final static String RECEIVER_TYPE_DOCTOR = "RECEIVER_TYPE_DOCTOR";
    public final static String RECEIVER_TYPE_ADMIN = "RECEIVER_TYPE_ADMIN";
    public final static String MSG_SWITCH_STATUS_ON = "on";
    public final static String MSG_SWITCH_STATUS_OFF = "off";

    /**
     * 发送短信接口,根据开关与否进行短信发送
     * @param phoneNum 发送短信手机号码
     * @param content  发送短息内容
     * @return 发送短信的结果
     * */
    public static String sendMsg(String phoneNum, String content) {
        //短信开关
        String msgStatus = Global.getConfig("shortMessageSwitch");
        LogUtils.saveLog("sendmsg",phoneNum+"-"+content+"-");//短信
        if("on".equals(msgStatus)&&phoneNum!=""&&content !=null){
            System.out.print("短信发送："+phoneNum+"|"+content);
            content = content+"【宝大夫】";
            String msgSendUrl_utf = "http://sms.chanzor.com:8001/sms.aspx";
            String postData = "action=send&account=" + account + "&password="
                    + password + "&mobile=" + phoneNum + "&content=" + content
                    + "&sendTime=";
            String result = getConnectionResult(postData,msgSendUrl_utf);
            try {
                ChangzhuaoMessageBean messageBean = parseXml(result);
                String status = messageBean.getReturnstatus();
                String Prompt = messageBean.getMessage();
                LogUtils.saveLog("msgStatus",phoneNum+"-"+status);//短信
                if("Faild".equals(status)){
                    LogUtils.saveLog("sendMsgFail","00000008"+phoneNum+"-"+content+"-"+Prompt);//短信
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
            content = content+"【宝大夫】";
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
                LogUtils.saveLog("00000009",phoneNum+"-"+content+"-"+Prompt);//定时器短信
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }
        return "";
    }

    /**
     *验证码发送接口
     * @param phoneNum 手机号码
     * @return 随机数的验证码
     * */
    public static String sendIdentifying(String phoneNum){
        if(phoneNum!=""){
            String identify = createRandom(true, 6);
            String content = "验证码：" + identify + ",欢迎使用宝大夫，为宝宝的健康保驾护航";
            content = content+"【宝大夫】";
            String msgSendUrl_utf = "http://sms.chanzor.com:8001/sms.aspx";
            String postData = "action=send&account=" + account + "&password="
                    + password + "&mobile=" + phoneNum + "&content=" + content
                    + "&sendTime=";
            String result = getConnectionResult(postData,msgSendUrl_utf);
            System.out.println(result);
            return identify;
        }
        return "";
    }

    /**
     * 短信发送请求接口
     * @param postUrl url请求地址
     * @param postData 短信消息内容
     * @return 服务商返回信息
     * */
    private static String getConnectionResult(String postData, String postUrl) {
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
     * 返回的短信状态转换成实体
     * @param request 请求接口后的返回结果
     * @return ChangzhuaoMessageBean 转换结果
     * */
    private static ChangzhuaoMessageBean parseXml(String request) throws Exception{
        ChangzhuaoMessageBean v = new ChangzhuaoMessageBean();
        // 将解析结果存储在HashMap
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
     * 将用户的订购信息提供给管理
     * */
	public static void sendMsgToManager(String content){
      String managerNum = "13601025165,13910933071,18911617317,13718256705,18301031173\n";
	  String xml = sendMsg(managerNum, content, RECEIVER_TYPE_DOCTOR);

	}

    /**
     * 首字母转大写
     * */
    private static String captureName(String name) {
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }

    /**
     * 随机数生成工具
     * @param numberFlag 是否是纯数字
     * @param length 随机数长度
     * */
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

    public static void main(String[] args) {
        sendMsg("123", "23");
    }
}
