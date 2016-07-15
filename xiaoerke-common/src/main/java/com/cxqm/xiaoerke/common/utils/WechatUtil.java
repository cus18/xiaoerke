package com.cxqm.xiaoerke.common.utils;

import com.cxqm.xiaoerke.common.bean.*;
import com.cxqm.xiaoerke.modules.sys.entity.Article;
import com.cxqm.xiaoerke.modules.sys.entity.WechatBean;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by baoweiw on 2015/7/27.
 */
public class WechatUtil {

    public static String getToken(String corpid, String sectet) throws IOException {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + corpid + "&secret=" + sectet + "";
        String content = HttpRequestUtil.get(url);
        AccessToken token = JsonUtil.getObjFromJsonStr(content, AccessToken.class);
        System.out.println("token:" + content);
        return token.getaccess_token();
    }

    /**
     * 获取jsp页面验证用的jsapi-ticket
     *
     * @return
     * @throws IOException
     */
    public static String getJsapiTicket(String token) throws IOException {
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + token + "&type=jsapi";
        String content = HttpRequestUtil.get(url);
        System.out.println("ticket:" + content);
        JsApiTicket ticket = JsonUtil.getObjFromJsonStr(content, JsApiTicket.class);
        return ticket.getTicket();
    }

    /**
     * 获取jsp页面验证用的jsapi-ticket
     *
     * @return
     * @throws IOException
     */
    public static String getOauth2Url(String backUrl) {
        backUrl = urlEncodeUTF8(backUrl);
        return "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
                ConstantUtil.CORPID + "&redirect_uri=" + backUrl + "&response_type=code&scope=snsapi_base&connect_redirect=1#wechat_redirect";
    }


    public static String urlEncodeUTF8(String source) {
        String result = source;
        try {
            result = java.net.URLEncoder.encode(source, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取某一天多客服聊天记录
     */
    public static String getCustom(String accessToken, long endtime, Long starttime, int pageIndex, int pagesize) {
        String url = " https://api.weixin.qq.com/customservice/msgrecord/getrecord?access_token=" + accessToken;
        JSONObject json = new JSONObject();
        json.put("endtime", endtime);
        json.put("pageindex", pageIndex);
        json.put("pagesize", pagesize);
        json.put("starttime", starttime);
        String request = HttpRequestUtil.getConnectionResult(url, "POST", json.toString());
        System.out.println("request:" + request);
        return request;
    }

    /**
     * 获取多客服聊天记录
     *
     * @param dateTime    时间
     * @param accessToken 唯一票据
     * @param pageIndex   起始页
     * @param li          聊天记录集合
     */
    public static void setWechatInfoToDb(String dateTime, String accessToken, int pageIndex, List<WechatRecord> li) {
        long startTime = (new Date().getTime()-30*60*1000)/1000;
        long endTime = new Date().getTime()/1000;
        String request = getCustom(accessToken, endTime, startTime, pageIndex, 30);
        JSONObject resultJson = new JSONObject(request);
        JSONArray array = resultJson.getJSONArray("recordlist");
        for (int i = 0; i < array.length(); i++) {
            JSONObject jo = (JSONObject) array.get(i);
            String openid = (String) jo.get("openid");//用户的标识
            Integer opercode = (Integer) jo.get("opercode");//操作ID（会话状态）
            Integer time = (Integer) jo.get("time");//操作时间
            String worker = (String) jo.get("worker");//客服账号
            String text = (String) jo.get("text");//客服账号
            Long timestamp = Long.parseLong(time.toString()) * 1000;
            Date date = new Date(timestamp);
            text = EmojiFilter.filterEmoji(text);
            Long times = Long.parseLong(time.toString()) * 1000;
            Date dates = new Date(times);
            Timestamp tt = new Timestamp(dates.getTime());
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");

            WechatRecord record = new WechatRecord();
            record.setId(uuid);
            record.setOpenid(openid);
            record.setinfoTime(tt);
            record.setOpercode(opercode + "");
            record.setText(text);
            record.setWorker(worker);
            li.add(record);
        }
        if (array.length() > 0) {
            setWechatInfoToDb(dateTime, accessToken, pageIndex + 1, li);
        }
    }


    /**
     * 获取在线客服信息
     *
     * @param token access_token
     * @return WechatBean 微信实体
     */
    public static ArrayList<CustomBean> getcustomInfo(String token) {
        String url = "https://api.weixin.qq.com/cgi-bin/customservice/getonlinekflist?access_token=" + token;
        String json = HttpRequestUtil.getConnectionResult(url, "GET", "");
        JSONObject jsonObj = new JSONObject(json);
        JSONArray subArray = jsonObj.getJSONArray("kf_online_list");
        JSONObject jsonObject = null;
        CustomBean customBean = null;
        ArrayList<CustomBean> list = new ArrayList<CustomBean>();
        for (int i = 0; i < subArray.length(); i++) {
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
     * 获取微信用户基本信息
     *
     * @param token  access_token
     * @param openid 用户唯一标示
     * @return WechatBean 微信实体
     */
    public static WechatBean getWechatName(String token, String openid) {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + token + "&openid=" + openid + "&lang=zh_CN";
        String json = HttpRequestUtil.getConnectionResult(url, "GET", "");
        return JsonUtil.getObjFromJsonStr(json, WechatBean.class);
    }

    /**
     * 调用多客服接口指定发送消息
     *
     * @param token   唯一票据
     * @param openId  用户的唯一标示
     * @param content 发送内容
     */
    public static String sendMsgToWechat(String token, String openId, String content) {
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + token;
        String result = "failure";
        try {
            String json = "{\"touser\":\"" + openId + "\",\"msgtype\":\"text\",\"text\":" +
                    "{\"content\":\"CONTENT\"}" + "}";
            json = json.replace("CONTENT", content);
            String re = HttpRequestUtil.getConnectionResult(url, "POST", json);
            System.out.print(json + "--" + re);
            if(re.contains("access_token is invalid")){
                //token已经失效，重新获取新的token
                result = "tokenIsInvalid";
            }
            JSONObject obj = new JSONObject(re);
            Integer resultStatus = (Integer)obj.get("errcode");
            if(resultStatus != null && resultStatus == 0){
                result = "messageOk";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
      return result ;
    }


    /**
     * emoji表情转换(hex -> utf-16)
     *
     * @param hexEmoji
     * @return
     */

    public static String emoji(int hexEmoji) {
        return String.valueOf(Character.toChars(hexEmoji));
    }

    public static String getOpenId(HttpSession session, HttpServletRequest request) {
        String openId = (String) session.getAttribute("openId");
        if (!StringUtils.isNotNull(openId)) {
            openId = CookieUtils.getCookie(request, "openId");
        }
        return openId;
    }

    public static String post(String strURL, String params, String type) {
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

    /**
     * 从微信服务器下载多媒体文件
     *
     * @author deliang
     */
    public String downloadMediaFromWx(String accessToken, String mediaId, String messageType) throws IOException {

        if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(mediaId)) return "";
        Long picLen = 0L;
        InputStream inputStream = null;
        String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="
                + accessToken + "&media_id=" + mediaId;
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet
                    .openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            // 获取文件转化为byte流
            inputStream = http.getInputStream();
            picLen = http.getContentLengthLong();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //返回图片的阿里云地址getConsultMediaBaseUrl
        String mediaName = mediaId;
        if(messageType.contains("image")){
            mediaName = mediaName+".jpg";
        }else if(messageType.contains("voice")){
            String mediaNameAmr = mediaName+".amr";
            String mediaNameMp3 = mediaName+".mp3";
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            if(ConstantUtil.AMR_TOMP3_FUNC.equals("windows")){
                FileOutputStream fos = new FileOutputStream(ConstantUtil.AMR_TOMP3_WINDOWSPATHTEMP+mediaNameAmr);
                byte[] buf = new byte[8096];
                int size = 0;
                while ((size = bis.read(buf)) != -1)
                    fos.write(buf, 0, size);
                fos.close();
                bis.close();
                ToMp3(ConstantUtil.AMR_TOMP3_WINDOWSPATH, ConstantUtil.AMR_TOMP3_WINDOWSPATHTEMP + mediaName);
                inputStream = new FileInputStream(new File(ConstantUtil.AMR_TOMP3_WINDOWSPATHTEMP+mediaNameMp3));
                StringUtils.deleteFile(new File(ConstantUtil.AMR_TOMP3_WINDOWSPATHTEMP));
            }else if(ConstantUtil.AMR_TOMP3_FUNC.equals("linux")){
                FileOutputStream fos = new FileOutputStream(ConstantUtil.AMR_TOMP3_LINUXPATH+mediaNameAmr);
                byte[] buf = new byte[8096];
                int size = 0;
                while ((size = bis.read(buf)) != -1)
                    fos.write(buf, 0, size);
                fos.close();
                bis.close();
                ToMp3(ConstantUtil.AMR_TOMP3_WINDOWSPATH, ConstantUtil.AMR_TOMP3_LINUXPATH + mediaName);
                inputStream = new FileInputStream(new File(ConstantUtil.AMR_TOMP3_LINUXPATH+mediaNameMp3));
                StringUtils.deleteFile(new File(ConstantUtil.AMR_TOMP3_LINUXPATH));
            }
            mediaName = mediaNameMp3;
        }else if(messageType.contains("video")){
            mediaName = mediaName+".mp4";
        }

        //上传图片到阿里云
        OSSObjectTool.uploadFileInputStream(mediaName, picLen, inputStream, OSSObjectTool.BUCKET_CONSULT_PIC);

        String mediaURL = OSSObjectTool.getConsultMediaBaseUrl()+ mediaName;
        return mediaURL;
    }

    public void ToMp3(String webroot, String sourcePath){
        String targetPath = sourcePath+".mp3";//转换后文件的存储地址，直接将原来的文件名后加mp3后缀名
        Runtime run = null;
        try {
            run = Runtime.getRuntime();
            long start=System.currentTimeMillis();
            String path = "";
            if(ConstantUtil.AMR_TOMP3_FUNC.equals("windows")){
                path = webroot + "ffmpeg -i "+sourcePath+".amr"+" -acodec libmp3lame "+targetPath;
            }else if(ConstantUtil.AMR_TOMP3_FUNC.equals("linux")){
                path = "ffmpeg -i "+sourcePath+".amr"+" -acodec libmp3lame "+targetPath;
            }
            Process p = run.exec(path);
            p.getOutputStream().close();
            p.getInputStream().close();
            p.getErrorStream().close();
            p.waitFor();
            long end=System.currentTimeMillis();
            System.out.println(sourcePath+" convert success, costs:"+(end-start)+"ms");
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //run调用lame解码器最后释放内存
            run.freeMemory();
        }
    }


    /**
     * 获取短链接信息
     * @param accessToken
     * @param longUrl
     * @return
     */
    public static String getShortUrl(String accessToken,String longUrl){
        String url = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token="+accessToken;
        ShortUrlCreate shortUrlCreate  = new ShortUrlCreate();
        shortUrlCreate.setAction("long2short");
        shortUrlCreate.setLong_url(longUrl);
        String object = HttpRequestUtil.httpsRequest(url, "POST", net.sf.json.JSONObject.fromObject(shortUrlCreate).toString());
        JSONObject resultJson = new JSONObject(object);
        String shortUrl = resultJson == null ?"":(String) resultJson.get("short_url");
        return shortUrl;
    }

    /**
     * 上传H5医生向微信用户发送图片消息
     * @param token 微信唯一票据
     * @param openId 微信用户唯一标识
     * @param content 发送内容
     * @param msgType 发送信息类型
     * */
    public static void sendNoTextMsgToWechat(String token, String openId, String content,int msgType){
        String type = null ;
        if(msgType == 1){
            type = "image";
        }else if(msgType ==2){
            type = "voice";
        }else{
            type = "video";
        }
        String sendUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + token;
        String json = "{\"touser\": \""+openId+"\",\"msgtype\": \""+type+"\", \""+type+"\": {\"media_id\": \""+content+"\"}}";
        sendNoTextToWX(sendUrl,json);
    }

    /**
     * 上传H5医生向微信用户发送图片消息
     * @param  token 微信唯一票据
     * @param  urlStr 上传微信服务器地址
     * @param  msgType 发送文件类型
     * @param  fileName 上传文件名字
     * @param  inputStream 上传文件流
     * */
    public static JSONObject uploadNoTextMsgToWX(String token,String urlStr,String msgType,String fileName,InputStream inputStream){
        String upLoadUrl = urlStr + "?access_token="+token+"&type="+msgType;
        String result = null;
        BufferedReader reader = null;
        try {
            URL openUrl= new URL(upLoadUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) openUrl.openConnection();
            httpURLConnection.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false); // post方式不能使用缓存
            // 设置请求头信息
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 设置边界
            String BOUNDARY = "----------" + System.currentTimeMillis();
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary="+BOUNDARY);
            // 请求正文信息
            // 第一部分：
            StringBuilder sb = new StringBuilder();
            sb.append("--"); // 必须多两道线
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"file\";filename=\""+ fileName + "\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");
            byte[] head = sb.toString().getBytes("utf-8");
            // 获得输出流
            OutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
            // 输出表头
            out.write(head);
            // 文件正文部分
            // 把文件已流文件的方式推入到url中
            DataInputStream in = new DataInputStream(inputStream);
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            in.close();
            byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
            out.write(foot);
            out.flush();
            out.close();

            StringBuffer buffer = new StringBuffer();
            // 定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            System.out.println(buffer.toString());
            if (result == null) {
                result = buffer.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        JSONObject jsonObj = new JSONObject(result);
        return jsonObj;
    }

    /**
     *  向微信端发送非文字消息
     *  @param sendUrl 发送微信地址
     *  @param  json 发送json类型消息
     */
    public static String sendNoTextToWX(String sendUrl,String json){
        URL sendWXUser;
        String reResult = null;
        try {
            sendWXUser = new URL(sendUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) sendWXUser.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            httpURLConnection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            os.write(json.getBytes("UTF-8"));// 传入参数
            os.flush();
            os.close();
            InputStream is = httpURLConnection.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            reResult = new String(jsonBytes, "UTF-8");
            System.out.println("请求返回结果:"+reResult);
            LogUtils.saveLog(json+"----"+reResult);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reResult;
    }

    /**
     * 调用多客服接口指定发送消息
     *
     * @param token       唯一票据
     * @param openId      用户的唯一标示
     * @param articleList 图文集合
     */
    public static void senImgMsgToWechat(String token, String openId, List<Article> articleList) {
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + token;
        try {
            String newStr =   "";
            for(Article article:articleList){
                newStr +=  "{\"title\":\"" + article.getTitle() + "\",\"description\":\"" + article.getDescription() + "\",\"url\":\"" + article.getUrl()+ "\",\"picurl\":\"" + article.getPicUrl() + "\"}," ;
            }
            String json = "{\"touser\":\"" + openId + "\",\"msgtype\":\"news\",\"news\":" +
                    "{\"articles\":[" +newStr+"]" + "}";
            String s = HttpRequestUtil.getConnectionResult(url, "POST", json.substring(0,json.length()-1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     *  向用户发送模板消息
     *  @author jiangzg
     *  @version 1.0
     *  2016年6月27日12:28:59
     */
    public static String sendTemplateMsgToUser(String token , String openId ,String templateId ,String content){
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token;
        String result = "failure";
        try {
            String json = "{\"touser\":\"" + openId + "\",\"template_id\":\""+templateId+"\",\"url\":\"\"," +
                    "\"data\":" + "{"+content+"}}";
            String re = HttpRequestUtil.getConnectionResult(url, "POST", json);
            System.out.print(json + "--" + re);
            JSONObject jsonObject = new JSONObject(re);
            if(re.contains("access_token is invalid")){
                //token已经失效，重新获取新的token
                result = "tokenIsInvalid";
            }
            Integer resultStatus = (Integer)jsonObject.get("errcode");
            if(resultStatus != null && resultStatus == 0){
                result = "messageOk";
                System.out.println("------"+resultStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

   /* public static void main(String[] args){
        List<String> openIds = new ArrayList<String>();
        openIds.add("o3_NPwsDuiEk1LW1dFvpBlozafu4");
        openIds.add("o3_NPwh8Jqkf9Dr2YsuFSSoAyzpc");
        openIds.add("o3_NPwq71mM836w64VUVdKi7gNEA");
        openIds.add("o3_NPwq71sdf36w64VeVxKi7defg");
        openIds.add("o3_NPwsnm69zdjkDSZBSgNJwcecM");
        openIds.add("o3_NPwjhZzRyR0-GndSzoJQOTB3k");
        openIds.add("o3_NPwsTM1OZAA_doMkc5IOy2MFU");
        openIds.add("o3_NPwhLIhjZ0OpyF2EVCKKsg0yE");
        String tokenId = "3NjH5MIJTPhUm_pdhjihQZF3dR3GSt74Xo-eTvWdXdT2Jl_0nchJ22b8GweAoaqEYUhpHjYV8d10FeIATSyo8V8i7ZNwmYTH7a2UYzFCblfH8MxHlGLDyxqH0nuVdHkqMUZgAEACKP";
        String templateId = "xP7QzdilUu1RRTFzVv8krwwMOyv-1pg9l0ABsooub14";
        String message = "\"first\":{\"value\":\"有用户对咨询服务做出了评价，请及时处理！\",\"color\":\"#173177\"}," +
                "\"keyword1\":{\"value\":\"" + "测试" + "\",\"color\":\"#173177\"}," +
                "\"keyword2\":{\"value\":\"" + "测试" + "\",\"color\":\"#173177\"}," +
                "\"keyword3\":{\"value\":\"" + new Date() + "\",\"color\":\"#173177\"},";

            message = message + "\"keyword4\":{\"value\":\"" + "测试" + "\",\"color\":\"#173177\"},";

        message = message + "\"keyword5\":{\"value\":\"" + "测试评价推送" + "\",\"color\":\"#173177\"}," +
                "\"remark\":{\"value\":\"\",\"color\":\"#173177\"}";
        String failureMessage = "";
        if (openIds != null && openIds.size() > 0) {
            for (String openId : openIds) {
                String result = WechatUtil.sendTemplateMsgToUser(tokenId, openId, templateId, message);
                if("messageOk".equals(result)){
                    continue ;
                }else{
                    failureMessage = failureMessage + openId +","+"\n";
                }
            }
        }
        if(StringUtils.isNotNull(failureMessage)){
            failureMessage = "以下用户差评提醒消息发送失败："+failureMessage+"请查看！";
            WechatUtil.sendMsgToWechat(tokenId,"o3_NPwuDSb46Qv-nrWL-uTuHiB8U",failureMessage);
        }
    }*/
}
