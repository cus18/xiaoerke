package com.cxqm.xiaoerke.modules.marketing.serviceimpl;

import com.cxqm.xiaoerke.modules.marketing.dao.LoveMarketingDao;
import com.cxqm.xiaoerke.modules.marketing.entity.LoveMarketing;
import com.cxqm.xiaoerke.modules.marketing.dao.LoveActivityCommentDao;
import com.cxqm.xiaoerke.modules.marketing.entity.LoveActivityComment;
import com.cxqm.xiaoerke.modules.marketing.service.LoveMarketingService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feibendechayedan on 16/5/11.
 */
@Service
@Transactional(readOnly = false)
public class LoveMarketingServiceImpl implements LoveMarketingService {

    @Autowired
    private SystemService systemService;

    @Autowired
    private LoveMarketingDao loveMarketingDao;

    @Autowired
    private LoveActivityCommentDao loveActivityCommentDao;

    @Override
    public Map<String, Object> getNicknameAndHeadImageByOpenid(String openid) {
        Map<String,Object> userAlone=new HashMap<String, Object>();
        String strURL="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+systemService.getWechatParameter()+"&openid="+openid+"&lang=zh_CN";
        String param="";
        String json=this.post(strURL, param, "GET");
        JSONObject jo=JSONObject.fromObject(json);
        if(jo.get("subscribe").toString().equals("0")){
            userAlone.put("subscribe","0");
            return userAlone;
        }
        userAlone.put("headImage", jo.get("headimgurl"));
        userAlone.put("name", jo.get("nickname"));
        return userAlone;
    }

    @Override
    public void saveLoveActivityComment(LoveActivityComment loveActivityComment) {
         loveActivityCommentDao.saveLoveActivityComment(loveActivityComment);
    }

    @Override
    public LoveActivityComment findLoveActivityComment() {
        List<LoveActivityComment> loveActivityCommentlist= loveActivityCommentDao.findLoveActivityComment();
        if(loveActivityCommentlist != null && loveActivityCommentlist.size() >0){
            LoveActivityComment loveActivityComment = loveActivityCommentlist.get(0);
            return loveActivityComment;
        }else{
            return null;
        }
    }

    @Override
    public String getUserQRcode(String id) throws Exception{
        String url= "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+systemService.getWechatParameter();
        String jsonData="{\"expire_seconds\": 604800, \"action_name\": \"QR_SCENE\",\"action_info\": {\"scene\": {\"scene_id\": "+id+"}}}";
        String reJson=this.post(url, jsonData,"POST");
        System.out.println(reJson);
        JSONObject jb=JSONObject.fromObject(reJson);
        String qrTicket=jb.getString("ticket");
        String QRCodeURI="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+qrTicket;
        this.download(QRCodeURI,id,"/Users/feibendechayedan/Downloads/");
        return "/Users/feibendechayedan/Downloads/"+id;
    }

    /**
     * 发送HttpPost请求
     *
     * @param strURL
     *            服务地址
     * @param params
     *            json字符串,例如: "{ \"id\":\"12345\" }" ;其中属性名必须带双引号<br/>
     *            type (请求方式：POST,GET)
     * @return 成功:返回json字符串<br/>
     */
    public  String post(String strURL, String params,String type) {
        System.out.println(strURL);
        System.out.println(params);
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
                System.out.println(result);
                return result;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null; // 自定义错误信息
    }

    /**
     * 下载图片
     * @param urlString
     * @param filename
     * @param savePath
     * @throws Exception
     */
    @Override
    public  void download(String urlString, String filename,String savePath) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        //设置请求超时为5s
        con.setConnectTimeout(5*1000);
        // 输入流
        InputStream is = con.getInputStream();

        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        File sf=new File(savePath);
        if(!sf.exists()){
            sf.mkdirs();
        }
        OutputStream os = new FileOutputStream(sf.getPath()+"\\"+filename);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
    }

    @Override
    public Map<String, Object> getLoveMarketingByOpenid(String openid) {
        return loveMarketingDao.getLoveMarketingByOpenid(openid);
    }

    @Override
    public int saveLoveMarketing(LoveMarketing loveMarketing) {
        return loveMarketingDao.saveLoveMarketing(loveMarketing);
    }

    @Override
    public int updateLoveMarketing(LoveMarketing loveMarketing) {
        return loveMarketingDao.updateLoveMarketing(loveMarketing);
    }


}


