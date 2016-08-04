package com.cxqm.xiaoerke.modules.wechat.service.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangbaowei on 16/7/21.
 */
public class ChangeUtil {

    private static HashMap<String, String> map = new HashMap<String, String>();
//    public ChangeUtil(){
//        map.put("gh_e1fcf06ed10b", "user");
//        map.put("gh_e1fcf06ed10a", "doctor");
//    }
    public static String chooseWxTokenId(String key){
        return map.get(key);
    }
    static {
        map.put("gh_79b4851ffafe", "user");
        map.put("gh_c758788430e5", "doctor");
    }

    /**
     * 根据文件id下载文件
     *
     * @param mediaId
     *            媒体id
     * @throws Exception
     */
    public static InputStream getInputStream(String mediaId,String token) {
        InputStream is = null;
        String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="
                + token + "&media_id=" + mediaId;
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
            is = http.getInputStream();
//            Long picLen = http.getContentLengthLong();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;
    }

    public static String uploadMedia(InputStream in,String type){
        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        String picTitle = "/pic_"+System.currentTimeMillis();
        if("image".equals(type)){
            picTitle  += ".jpg";
        }else if("voice".equals(type)){
            picTitle  += ".amr";
        }else if("video".equals(type)){
            picTitle += ".mp4";
        }else if("shortvideo".equals(type)){
            picTitle += ".mp4";
        }
        String dirPath = System.getProperty("user.dir").replace("bin","wechatImg");;
        File sf=new File(dirPath);
        if(!sf.exists()){
            sf.mkdirs();
        }
        OutputStream os = null;
//        File wxFile= null;
//        InputStream is = null;
        try {
            os = new FileOutputStream(dirPath+picTitle);
            // 开始读取
            while ((len = in.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
//            wxFile=new File(dirPath+picTitle);
//            is = new FileInputStream(wxFile);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                os.close();
//                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dirPath+picTitle;
    }

}
