package com.cxqm.xiaoerke.modules.marketing.serviceimpl;

import com.cxqm.xiaoerke.modules.marketing.dao.LoveActivityCommentDao;
import com.cxqm.xiaoerke.modules.marketing.dao.LoveMarketingDao;
import com.cxqm.xiaoerke.modules.marketing.entity.LoveActivityComment;
import com.cxqm.xiaoerke.modules.marketing.entity.LoveMarketing;
import com.cxqm.xiaoerke.modules.marketing.service.LoveMarketingService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

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

    String token="NTRshTU0qoVkHE4oW3J2J3-d_SFfTmJTflG7jyGXS5kfNDzEG9YhRzb-Lsn4aPj_4oG8pOJPhlxFIUAJtTxCxNfxUT9UB01Aa_rYtWYb5IUyU_SvTPwsombSNN0QyiheHXZiADAVLN";

    @Override
    public Map<String, Object> getNicknameAndHeadImageByOpenid(String openid) {
        Map<String,Object> userAlone=new HashMap<String, Object>();
//        String strURL="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+systemService.getWechatParameter()+"&openid="+openid+"&lang=zh_CN";
        String strURL="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+token+"&openid="+openid+"&lang=zh_CN";
        String param="";
        String json=this.post(strURL, param, "GET");
        JSONObject jo=JSONObject.fromObject(json);
        if(jo.get("subscribe").toString().equals("0")){
            userAlone.put("subscribe","0");
            return userAlone;
        }
        userAlone.put("subscribe","1");
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
    public List<Map<String, Object>> getOpenidByMarketer(String id) {
        return loveMarketingDao.getOpenidByMarketer(id);
    }

    @Override
    public void updateInviteMan(String  id,String openid) {
        Map<String,Object> map= loveMarketingDao.getOpenidById(id);
        if(!map.get("openid").toString().equals(openid)) {
            return;
        }
        double lovemoney = map.get("lovemoney") != null ? Integer.parseInt(map.get("lovemoney").toString()) : 0;
        LoveMarketing l = new LoveMarketing();
        l.setId(id);
        l.setLoveMoney(lovemoney + 1);
            //二级邀请人 +0.2
        Map<String, Object> fm = loveMarketingDao.getOpenidByTopMarketer(id);
        if(fm.get("marketer")!=null&&!fm.get("marketer").toString().equals("")){
            String fmarketer=fm.get("marketer").toString();
            Map<String,Object> ffmap= loveMarketingDao.getOpenidById(fmarketer);
            double flovemoney = ffmap.get("lovemoney") != null ? Integer.parseInt(ffmap.get("lovemoney").toString()) : 0;
            LoveMarketing fl = new LoveMarketing();
            fl.setId(fmarketer);
            fl.setLoveMoney(flovemoney + 0.2);
            loveMarketingDao.updateLoveMarketing(l);
            loveMarketingDao.updateLoveMarketing(fl);
        }else{
            loveMarketingDao.updateLoveMarketing(l);
        }

    }

    @Override
    public Map<String, Object> getUserInfo(String openid) {
        Map<String, Object> map=loveMarketingDao.getLoveMarketingByOpenid(openid);
        if(map==null){
            return null;
        }
        String lovemoney=map.get("lovemoney").toString();
        Integer transcend=loveMarketingDao.transcendNum(lovemoney);
        Integer lovemoneyCount=loveMarketingDao.countMoney();
        transcend=transcend/lovemoneyCount*100;
        if(transcend==0){
            transcend=100;
        }
//        map.put("lovemoneyCount",lovemoneyCount);
        map.put("friendNum",loveMarketingDao.getOpenidByMarketer(map.get("id").toString()).size());
        map.put("transcend",transcend+"%");
        return map;
    }

    @Override
    public List<Map<String, Object>> getAll() {
        return loveMarketingDao.getAll();
    }

    @Override
    public int countMoney() {
        return loveMarketingDao.countMoney();
    }

    @Override
    public String getUserQRcode(String id) throws Exception{
        String url= "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+token;
        String jsonData="{\"expire_seconds\": 604800, \"action_name\": \"QR_SCENE\",\"action_info\": {\"scene\": {\"scene_id\": "+id+"}}}";
        String reJson=this.post(url, jsonData,"POST");
        System.out.println(reJson);
        JSONObject jb=JSONObject.fromObject(reJson);
        String qrTicket=jb.getString("ticket");
        String QRCodeURI="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+qrTicket;
        return "/Users/feibendechayedan/Downloads/"+id+"qrcode.jpg";
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





}


