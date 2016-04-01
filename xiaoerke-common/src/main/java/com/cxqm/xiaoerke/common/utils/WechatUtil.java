package com.cxqm.xiaoerke.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import com.cxqm.xiaoerke.common.bean.AccessToken;
import com.cxqm.xiaoerke.common.bean.CustomBean;
import com.cxqm.xiaoerke.common.bean.JsApiTicket;
import com.cxqm.xiaoerke.common.bean.WechatRecord;
import com.cxqm.xiaoerke.common.bean.WxQrcode;
import com.cxqm.xiaoerke.modules.sys.entity.WechatBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by baoweiw on 2015/7/27.
 */
public class WechatUtil {

    //用户端微信参数
//    public static final String CORPID = "wx0baf90e904df0117";
//    public static final String SECTET = "b3dac0be3e1739af01fee0052ea7a68f";
//    public static final String CORPID = "wx0baf90e904df0117";
//    public static final String SECTET = "b3dac0be3e1739af01fee0052ea7a68f";
    //宝大夫医生端微信参数
    public static final String  DOCTORCORPID= "wxb6b6ad2a55af0567";
    public static final String   DOCTORSECTET= "1822bb2703511da89fa7bfa1a5549b31";

    //小儿科用户端微信参数
//    public static final String DOCTORCORPID = "wxa19496b1076e7352";
//    public static final String DOCTORSECTET = "f645d4bcf81c905b3ad628cda79bd7ee";

//    public static final String CORPID = "wx0baf90e904df0117";
//    public static final String SECTET = "b3dac0be3e1739af01fee0052ea7a68f";

    //小儿科医生端微信参数
//    public static final String CORPID = "wx9b663cd46164130c";
//    public static final String SECTET = "d0460e461a3bcf8598ce6e87443b3d0f";
    //用户端微信参数
//    public static final String DOCTORCORPID = "wx9b663cd46164130c";
//    public static final String DOCTORSECTET = "d0460e461a3bcf8598ce6e87443b3d0f";
//   宝大夫
    public static final String CORPID = "wx0baf90e904df0117";
    public static final String SECTET = "b3dac0be3e1739af01fee0052ea7a68f";
//
//    //医生端微信参数
//    public static final String CORPID = "wxa19496b1076e7352";
//    public static final String SECTET = "f645d4bcf81c905b3ad628cda79bd7ee";


    //在任务管理器中ScheduledTaskManager初始化。100分钟更新一次。
//    public static String TOKEN;
//    public static String JSAPI_Ticket;

    public static String getToken(String corpid,String sectet) throws IOException {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+corpid+"&secret="+sectet+"";
        String content = HttpRequestUtil.get(url);
        AccessToken token  =(AccessToken) JsonUtil.getObjFromJsonStr(content, AccessToken.class);
        System.out.println("token:" + content);
        return token.getaccess_token();
    }

    /**
     * 获取jsp页面验证用的jsapi-ticket
     * @return
     * @throws IOException
     */
    public static String getJsapiTicket(String token) throws IOException {
      String url="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+token+"&type=jsapi";
      String content = HttpRequestUtil.get(url);
      System.out.println("ticket:" + content);
      JsApiTicket ticket =(JsApiTicket) JsonUtil.getObjFromJsonStr(content, JsApiTicket.class);
      return ticket.getTicket();
    }

    /**
     * 获取jsp页面验证用的jsapi-ticket
     * @return
     * @throws IOException
     */
    public static String getOauth2Url(String backUrl) {
      backUrl = urlEncodeUTF8(backUrl);
      return  "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
              WechatUtil.CORPID +"&redirect_uri="+backUrl+"&response_type=code&scope=snsapi_base&connect_redirect=1#wechat_redirect";
    }


    public static String urlEncodeUTF8(String source){
        String result = source;
        try {
            result = java.net.URLEncoder.encode(source,"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     *生成微信带参二维码
     * @param accessToken  场景值ID
     * @param scene_str  token值
     * */
    public static String createQrcode(String accessToken,String scene_str) {
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+accessToken;
        String json = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"SCENESTR\"}}}";
        json = json.replace("SCENESTR",scene_str);
        System.out.println("chenjiaketest---"+json+"==========="+url);
        String object =  HttpRequestUtil.httpPost(json, url);
        System.out.println("chenjiaketest---"+object);
        JSONObject resultJson = new JSONObject(object);
        String ticket = (String)resultJson.get("ticket");
        return ticket;
    }

    /**
     * 获取某一天多客服聊天记录
     * */
    public static String getCustom(String accessToken,long endtime,Long starttime,int pageIndex,int pagesize){
        String url = " https://api.weixin.qq.com/customservice/msgrecord/getrecord?access_token="+accessToken;
        JSONObject json = new JSONObject();
        json.put("endtime",endtime);
        json.put("pageindex",pageIndex);
        json.put("pagesize",pagesize);
        json.put("starttime",starttime);
        String request = HttpRequestUtil.getConnectionResult(url,"POST", json.toString());
        System.out.println("request:"+request);
        return request;
    }

    /**
     * 获取多客服聊天记录
     * @param dateTime 时间
     * @param accessToken 唯一票据
     * @param pageIndex 起始页
     * @param li 聊天记录集合
     * */
    public static void setWechatInfoToDb(String dateTime,String accessToken,int pageIndex,List<WechatRecord> li) {
        long startTime = String2TimeStamp(dateTime+" 00:00");
        long endTime = String2TimeStamp(dateTime+" 23:00");
        String request = getCustom(accessToken,endTime,startTime,pageIndex,30);
        JSONObject resultJson = new JSONObject(request);
        JSONArray array = resultJson.getJSONArray("recordlist");
        for (int i = 0; i < array.length(); i++) {
            JSONObject jo = (JSONObject) array.get(i);
            String openid = (String)jo.get("openid");//用户的标识
            Integer opercode = (Integer)jo.get("opercode");//操作ID（会话状态）
            Integer time = (Integer)jo.get("time");//操作时间
            String worker = (String)jo.get("worker");//客服账号
            String text = (String)jo.get("text");//客服账号
            Long timestamp = Long.parseLong(time.toString())*1000;
            Date date = new Date(timestamp);
            text = EmojiFilter.filterEmoji(text);
            Long times = Long.parseLong(time.toString())*1000;
            Date dates= new Date(times);
            Timestamp tt=new Timestamp(dates.getTime());
            String uuid =UUID.randomUUID().toString().replaceAll("-", "");
            
            WechatRecord record = new WechatRecord();
            record.setId(uuid);
            record.setOpenid(openid);
            record.setinfoTime(tt);
            record.setOpercode(opercode + "");
            record.setText(text);
            record.setWorker(worker);
            li.add(record);
        }
        if(array.length()>0){
            setWechatInfoToDb(dateTime, accessToken,pageIndex+1,li);
        }
    }


    /**
     * 获取在线客服信息
     * @param token access_token
     * @return WechatBean 微信实体
     * */
    public static ArrayList<CustomBean> getcustomInfo(String token){
      String url = "https://api.weixin.qq.com/cgi-bin/customservice/getonlinekflist?access_token="+token;
      String json = HttpRequestUtil.getConnectionResult(url, "GET", "");
      JSONObject jsonObj = new JSONObject(json);
      JSONArray subArray = jsonObj.getJSONArray("kf_online_list");
      JSONObject jsonObject = null;
      CustomBean customBean = null;
      ArrayList<CustomBean> list=new ArrayList<CustomBean>();
      for (int i = 0; i <subArray.length() ; i++) {
        customBean = new CustomBean();
        jsonObject = subArray.getJSONObject(i);
        customBean.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        customBean.setKf_account(jsonObject.getString("kf_account"));
        customBean.setStatus(String.valueOf(jsonObject.getInt("status")));
        customBean.setKf_id(jsonObject.getString("kf_id"));
        customBean.setAuto_accept(String.valueOf(jsonObject.getInt("auto_accept")));
        customBean.setAccepted_case(String.valueOf(jsonObject.getInt("accepted_case")));
        list.add(customBean);
      }
        return list;
    }

    /**
     * 获取多客服会话状态 当会话超过10分钟未接通则通知相关人员
     * @param accessToken
     * */
    public static void getConversationInfo(String accessToken){
        String url = " https://api.weixin.qq.com/customservice/kfsession/getwaitcase?access_token="+accessToken;
        String json = HttpRequestUtil.getConnectionResult(url, "GET", "");
        JSONObject jsonObj = new JSONObject(json);
        JSONArray array = jsonObj.getJSONArray("waitcaselist");
        for (int i = 0; i < array.length(); i++) {
            JSONObject jo = (JSONObject) array.get(i);
            Integer opercode = (Integer)jo.get("openid");//操作ID（会话状态）
            Integer createtime = (Integer)jo.get("createtime");//操作时间
            Long timestamp = Long.parseLong(createtime.toString())*1000;
            Date date = new Date(timestamp);
            boolean flag = (date.getTime()+1000*60*10)<new Date().getTime();
            if(flag){
                //发短信给医生
                System.out.println("短信");
//                ChangzhuoMessageUtil.sendMsg("","");
            }
        }
    }

    /**
     * 获取微信用户基本信息
     * @param token access_token
     * @param openid 用户唯一标示
     * @return WechatBean 微信实体
     * */
    public static WechatBean getWechatName(String token,String openid){
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+token+"&openid="+openid+"&lang=zh_CN";
        String json = HttpRequestUtil.getConnectionResult(url, "GET", "");
        return JsonUtil.getObjFromJsonStr(json, WechatBean.class);
    }

    /**
     * 获取关注列表
     * @param token 标示
     * @param nextopenid 拉去的第一个openid
     *
     * */
    public static String getAttetionList(String token,String nextopenid){
        String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token="+token+"&next_openid="+nextopenid+"";
        String jsonObj =HttpRequestUtil.getConnectionResult(url,"GET", "");
        JSONObject obj =new JSONObject(jsonObj);
        JSONObject mapJson  = (JSONObject)obj.get("data");
        System.out.println((mapJson.get("openid")).toString());
        return (mapJson.get("openid")).toString().replace("[","").replace("]", "");
    }
    /**
     * 调用多客服接口指定发送消息
     * @param token 唯一票据
     * @param openId 用户的唯一标示
     * @param content 发送内容
     * */
    public static  void senMsgToWechat(String token,String openId,String content){
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+ token;
        try {
            String json = "{\"touser\":\""+openId+"\",\"msgtype\":\"text\",\"text\":" +
                    "{\"content\":\"CONTENT\"}"+"}";
            json = json.replace("CONTENT",content);
            String re = HttpRequestUtil.getConnectionResult(url,"POST", json);
            System.out.print(json+"--"+re);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用多客服接口指定发送消息
     * @param token 唯一票据
     * @param openId 用户的唯一标示
     * @param title 标题
     * @param description 内容
     * @param linkUrl 链接
     * @param picurl 图片链接
     * */
    public static  void senImgMsgToWechat(String token,String openId,String title,String description,String linkUrl,String picurl){
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+ token;
        try {
            String json = "{\"touser\":\""+openId+"\",\"msgtype\":\"news\",\"news\":" +
                    "{\"articles\":[{\"title\":\""+title+"\",\"description\":\""+description+"\",\"url\":\""+linkUrl+"\",\"picurl\":\""+picurl+"\"]}}"+"}";
            HttpRequestUtil.post(url, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getOnlineCoust(String accessToken){

    }

    /**
     * 将日期转换成unix时间戳
     *@param dateTime 需转化时间
     *@return 时间戳
     * */
    public static long String2TimeStamp(String dateTime){
        try {
            SimpleDateFormat myFmt=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = myFmt.parse(dateTime);
            return date.getTime()/1000;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0l;
    }
    public static void main(String args[]){
        String tic = createQrcode("YDm2FQGDF-3cFBZzFn2BXAyTCWygANrpCySVH3x_itYOOMonkCfVa9j4_5Pc_IqaF2ArnlWbMw1VYn88FzIFHQS1GSzfuBa8ewFJyjODoYU","76b766068ca242d78464669d0f6d4671");
        System.out.print(tic);
    }

    /**
     * 字符串转换成十六进制字符串
     */
    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString();
    }

    /**
     * 把16进制字符串转换成字节数组
     * @return byte[]
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**
     * 数组转换成十六进制字符串
     * @return HexString
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * emoji表情转换(hex -> utf-16)
     * @param hexEmoji
     * @return
     */

    public static String emoji(int hexEmoji) {
        return String.valueOf(Character.toChars(hexEmoji));
    }

    public static String getOpenId(HttpSession session,HttpServletRequest request){
        String openId = (String) session.getAttribute("openId");
        if(!StringUtils.isNotNull(openId)){
            openId = CookieUtils.getCookie(request,"openId");
        }
        return openId;
    }
    
    public static String post(String strURL, String params,String type) {
		try {
			URL url = new URL(strURL);// 创建连接
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestMethod(type); // 设置请求方式
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
}
