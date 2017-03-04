package com.cxqm.xiaoerke.modules.activity.service.impl;

import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.activity.dao.OlyBabyGameDetailDao;
import com.cxqm.xiaoerke.modules.activity.dao.OlyBabyGamesDao;
import com.cxqm.xiaoerke.modules.activity.dao.OlyGamePrizeDao;
import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGameDetailVo;
import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGamesVo;
import com.cxqm.xiaoerke.modules.activity.service.OlyGamesService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by deliang on 16/8/01.
 */

@Service
public class OlyGamesServiceImpl implements OlyGamesService {

    @Autowired
    private OlyBabyGamesDao olyBabyGamesDao;

    @Autowired
    private OlyGamePrizeDao olyGamePrizeDao;


    @Autowired
    private OlyBabyGameDetailDao olyBabyGameDetailDao;

    @Override
    public OlyBabyGamesVo selectByOlyBabyGamesVo(OlyBabyGamesVo olyBabyGamesVo) {
        return olyBabyGamesDao.selectByOlyBabyGamesVo(olyBabyGamesVo);
    }

    @Override
    public int getGameMemberNum() {
        return olyBabyGamesDao.getGameMemberNum();
    }

    @Override
    public void updateLevelCurrentTimes(OlyBabyGamesVo olyBabyGamesVo){
        olyBabyGamesDao.updateLevelCurrentTimes(olyBabyGamesVo);
    }

    @Override
    public int addGamePlayerInfo(OlyBabyGamesVo olyBabyGamesVo) {
        return olyBabyGamesDao.insert(olyBabyGamesVo);
    }

    @Override
    public int getNewAttentionByOpenId(String userId) {
        return olyBabyGamesDao.getNewAttentionByOpenId(userId);
    }

    /**
     * 获取所有奖品列表
     * sunxiao
     * @return
     */
    @Override
    public List<Map<String, Object>> getOlyGamePrizeList(Map<String, Object> prizeMap) {
        return olyGamePrizeDao.getOlyGamePrizeList(prizeMap);
    }

    /**
     * 获取日期最近的5个用户的奖品列表
     * sunxiao
     * @return
     */
    public List<OlyBabyGamesVo> getUserPrizeList(){
        return olyBabyGamesDao.getUserPrizeList();
    }
    /**
     * 更新奖品信息
     * sunxiao
     * @return
     */
    public void updateOlyGamePrizeInfo(Map<String, Object> prizeMap){
        olyGamePrizeDao.updateOlyGamePrizeInfo(prizeMap);
    }

    @Override
    public int insertOlyBabyGamesVo(OlyBabyGamesVo record) {
        return olyBabyGamesDao.insertSelective(record);
    }

    @Override
    public int insertOlyBabyGameDetailVo(OlyBabyGameDetailVo record){
        return olyBabyGameDetailDao.insertSelective(record);
    }

    @Override
    public int updateOlyBabyGamesByOpenId(OlyBabyGamesVo record){
       return olyBabyGamesDao.updateByOpenId(record);
    }

    @Override
    public int updateInviteFriendNumber(String marketer) {
        return olyBabyGamesDao.updateInviteFriendNumber(marketer);
    }

    @Override
    public int updateByPrimaryKeySelective(OlyBabyGamesVo record){
        return olyBabyGamesDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public String getLastNewMarkter(){
        String result = olyBabyGamesDao.getLastNewMarkter();
        if(!"".equals(result) && result != null){
            return result;
        }else{
            return null;
        }
    }

    ;
    @Autowired
    SystemService systemService;

    /**
     * 获取微信头像url
     * @param openId
     * @return
     */
    @Override
    public String getWechatMessage(String openId){
        String headimgurl = null;

        Map<String,Object> parameter = systemService.getWechatParameter();
        String token = (String)parameter.get("token");

        String strURL="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+token+"&openid="+openId+"&lang=zh_CN";
        String param="";
        String json= WechatUtil.post(strURL, param, "GET");
        JSONObject jasonObject = JSONObject.fromObject(json);
        Map<String, Object> jsonMap = (Map) jasonObject;

        if(jsonMap.get("subscribe")!=null && (Integer)jsonMap.get("subscribe") == 1){
            headimgurl = (String) jsonMap.get("headimgurl");
        }

        return headimgurl;
    }

    @Override
    public String getMarketerByOpenid(String openId) {
        return olyBabyGamesDao.getMarketerByOpenid(openId);
    }

    @Override
    public OlyBabyGamesVo getBaseByMarketer(String marketer) {
        return olyBabyGamesDao.getBaseByMarketer(marketer);
    }

    /**
     * 获取邀请卡二维码链接
     * @param id
     * @return
     */
    @Override
    public String getUserQRCode(String id) {
        Map<String,Object> parameter = systemService.getWechatParameter();
        String token = (String)parameter.get("token");
        String url= "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+token;
        String jsonData="{\"expire_seconds\": 604800, \"action_name\": \"QR_SCENE\",\"action_info\": {\"scene\": {\"scene_id\"" + ":" + Integer.parseInt(id) + "}}}";
        String reJson=this.post(url, jsonData,"POST");
        System.out.println(reJson);
        JSONObject jb=JSONObject.fromObject(reJson);
        String qrTicket=jb.getString("ticket");
        String QRCodeURI="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+qrTicket;
        return QRCodeURI;
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
    public String post(String strURL, String params,String type) {
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

    public String uploadMedia(InputStream in){
        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        String picTitle = "/pic_"+System.currentTimeMillis()+".jpg";
        String dirPath = System.getProperty("user.dir").replace("bin","wechatImg");;
        File sf=new File(dirPath);
        if(!sf.exists()){
            sf.mkdirs();
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(dirPath+picTitle);
            while ((len = in.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dirPath+picTitle;
    }
}
