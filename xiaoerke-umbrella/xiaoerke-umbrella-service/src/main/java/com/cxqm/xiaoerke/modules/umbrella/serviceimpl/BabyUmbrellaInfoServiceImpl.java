package com.cxqm.xiaoerke.modules.umbrella.serviceimpl;

import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.umbrella.dao.BabyUmbrellaInfoDao;
import com.cxqm.xiaoerke.modules.umbrella.entity.BabyUmbrellaInfo;
import com.cxqm.xiaoerke.modules.umbrella.service.BabyUmbrellaInfoService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by feibendechayedan on 16/5/20.
 */

@Service
@Transactional(readOnly = false)
public class BabyUmbrellaInfoServiceImpl implements BabyUmbrellaInfoService {

    @Autowired
    private SystemService systemService;

    @Autowired
    private BabyUmbrellaInfoDao babyUmbrellaInfoDao;

    @Override
    public int saveBabyUmbrellaInfo(BabyUmbrellaInfo babyUmbrellaInfo) {
        return babyUmbrellaInfoDao.saveBabyUmbrellaInfo(babyUmbrellaInfo);
    }

    @Override
    public int updateBabyUmbrellaInfo(BabyUmbrellaInfo babyUmbrellaInfo) {
        return babyUmbrellaInfoDao.updateBabyUmbrellaInfo(babyUmbrellaInfo);
    }

    @Override
    public List<Map<String, Object>> getBabyUmbrellaInfo(Map<String, Object> map) {
        return babyUmbrellaInfoDao.getBabyUmbrellaInfo(map);
    }

    @Override
    public Integer getUserShareNums(String id) {
        return babyUmbrellaInfoDao.getUserShareNums(id);
    }



    @Override
    public String getUserQRCode(String id) {
        Map<String,Object> parameter = systemService.getWechatParameter();
        String token = (String)parameter.get("token");
        String url= "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+token;
        String jsonData="{\"expire_seconds\": 604800, \"action_name\": \"QR_SCENE\",\"action_info\": {\"scene\": {\"scene_id\": "+id+"}}}";
        String reJson=this.post(url, jsonData,"POST");
        System.out.println(reJson);
        JSONObject jb=JSONObject.fromObject(reJson);
        String qrTicket=jb.getString("ticket");
        String QRCodeURI="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+qrTicket;
        return QRCodeURI;
    }

    @Override
    public Integer getBabyUmbrellaInfoTotal(Map<String, Object> map) {
        return babyUmbrellaInfoDao.getBabyUmbrellaInfoTotal(map);
    }

    @Override
    public Map<String, Object> getOpenidStatus(String openid) {
        return babyUmbrellaInfoDao.getOpenidStatus(openid);
    }

    @Override
    public Integer getTotalBabyUmbrellaInfoMoney(Map<String, Object> map) {
        return babyUmbrellaInfoDao.getTotalBabyUmbrellaInfoMoney(map);
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
