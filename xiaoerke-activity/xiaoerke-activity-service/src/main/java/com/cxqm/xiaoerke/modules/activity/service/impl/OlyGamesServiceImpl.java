package com.cxqm.xiaoerke.modules.activity.service.impl;

import com.cxqm.xiaoerke.modules.activity.dao.OlyBabyGameDetailDao;
import com.cxqm.xiaoerke.modules.activity.dao.OlyBabyGamesDao;
import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGameDetailVo;
import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGamesVo;
import com.cxqm.xiaoerke.modules.activity.service.OlyGamesService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by deliang on 16/8/01.
 */

@Service
public class OlyGamesServiceImpl implements OlyGamesService {

    @Autowired
    private OlyBabyGamesDao olyBabyGamesDao;

    @Autowired
    private OlyBabyGameDetailDao olyBabyGameDetailDao;

    @Override
    public OlyBabyGamesVo selectByOlyBabyGamesVo(OlyBabyGamesVo olyBabyGamesVo) {
        return olyBabyGamesDao.selectByOlyBabyGamesVo(olyBabyGamesVo);
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

    @Autowired
    SystemService systemService;

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

}
