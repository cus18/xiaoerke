package com.cxqm.xiaoerke.modules.umbrella.serviceimpl;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;
import com.cxqm.xiaoerke.modules.sys.service.BabyBaseInfoService;
import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.WechatMessageUtil;
import com.cxqm.xiaoerke.modules.umbrella.dao.BabyUmbrellaInfoDao;
import com.cxqm.xiaoerke.modules.umbrella.dao.UmbrellaFamilyInfoDao;
import com.cxqm.xiaoerke.modules.umbrella.entity.BabyUmbrellaInfo;
import com.cxqm.xiaoerke.modules.umbrella.entity.UmbrellaFamilyInfo;
import com.cxqm.xiaoerke.modules.umbrella.entity.UmbrellaMongoDBVo;
import com.cxqm.xiaoerke.modules.umbrella.service.BabyUmbrellaInfoService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

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

    @Autowired
    private UmbrellaFamilyInfoDao umbrellaFamilyInfoDao;

    @Autowired
    private BabyBaseInfoService babyBaseInfoService;

    @Autowired
    private MongoDBService<UmbrellaMongoDBVo> umbrellaMongoDBService;

    @Override
    public int saveBabyUmbrellaInfo(BabyUmbrellaInfo babyUmbrellaInfo) {
        return babyUmbrellaInfoDao.saveBabyUmbrellaInfo(babyUmbrellaInfo);
    }

    @Override
    public int updateBabyUmbrellaInfo(BabyUmbrellaInfo babyUmbrellaInfo) {
        return babyUmbrellaInfoDao.updateBabyUmbrellaInfo(babyUmbrellaInfo);
    }

    @Override
    public int updateBabyUmbrellaInfoById(BabyUmbrellaInfo babyUmbrellaInfo) {
        return babyUmbrellaInfoDao.updateBabyUmbrellaInfoById(babyUmbrellaInfo);
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
        String jsonData="{\"expire_seconds\": 604800, \"action_name\": \"QR_SCENE\",\"action_info\": {\"scene\": {\"scene_id\"" + ":" + Integer.parseInt(id) + "}}}";
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

    @Override
    public int getUmbrellaCount() {
        return babyUmbrellaInfoDao.getUmbrellaCount();
    }

    @Override
    public void umbrellaSendWechatMessage(){
        Map<String, Object> notShareParam = new HashMap<String, Object>();
        notShareParam.put("notShareOrActiveDays","1");
        notShareParam.put("notShare","notShare");
        List<Map<String,Object>> notShareList = babyUmbrellaInfoDao.getBabyUmbrellaInfo(notShareParam);
        Map tokenMap = systemService.getWechatParameter();
        String token = (String)tokenMap.get("token");

        for(Map<String, Object> map : notShareList){//一天未分享
            String title = "快把宝护伞分享给好友吧，每个通过你分享而来的朋友都会为你助力2万保障，呼朋唤友来领取40万最高保障吧！";
            String templateId = "b_ZMWHZ8sUa44JrAjrcjWR2yUt8yqtKtPU8NXaJEkzg";
            String keyword1 = "保障金处于最低额度";
            String keyword2 = StringUtils.isNotNull((String) map.get("baby_id"))?"观察期":"待激活";
            String remark = "点击分享";
            String url = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=31";
            String openid = (String)map.get("openid");
            WechatMessageUtil.templateModel(title, keyword1, keyword2, "", "", remark, token, url, openid, templateId);
        }

        /*Map<String, Object> notActiveParam = new HashMap<String, Object>();
        notActiveParam.put("notActive","notActive");
        notActiveParam.put("notShareOrActiveDays","30");
        List<Map<String,Object>> notActivelist = babyUmbrellaInfoDao.getBabyUmbrellaInfo(notActiveParam);

        for(Map<String, Object> map : notActivelist){//30天未激活
            String title = "您刚领取的20万保障金还未激活";
            String templateId = "lJIuV_O_zRMav4Fcv32e9cD7YG7cb0WVOPXNjhg_UpU";
            String keyword1 = map.get("id") + "";
            String keyword2 = "保护伞——宝大夫儿童重疾互助计划";
            String remark = "马上点击，完善信息即可激活保障金! ";
            String url = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=31";
            String openid = (String)map.get("openid");
            WechatMessageUtil.templateModel(title, keyword1, keyword2, "", "", remark, token, url, openid, templateId);
        }*/
    }

    @Override
    public int saveFamilyUmbrellaInfo(UmbrellaFamilyInfo vo) {
      return umbrellaFamilyInfoDao.insertSelective(vo);
    }

    @Override
    public List<UmbrellaFamilyInfo> getFamilyUmbrellaList(Integer umbrella_id) {

      return umbrellaFamilyInfoDao.selectByumbrellaId(umbrella_id);
    }

    @Override
    public BabyBaseInfoVo getBabyBaseInfo(Integer umbrella_id) {
        BabyUmbrellaInfo babyUmbrellaInfo = babyUmbrellaInfoDao.selectByPrimaryKey(umbrella_id);
        if(null != babyUmbrellaInfo&&StringUtils.isNotNull(babyUmbrellaInfo.getBabyId())){
            Integer babyId = Integer.parseInt(babyUmbrellaInfo.getBabyId());
            return babyBaseInfoService.selectByPrimaryKey(Integer.parseInt(babyUmbrellaInfo.getBabyId()));
        }
      return null;
    }

    @Override
    public Map getUmbrellaNum(Map<String, Object> map) {
      return  umbrellaFamilyInfoDao.getUmbrellaNum(map);
    }

    @Override
    public int updateBabyUmbrellaInfoIfShare(BabyUmbrellaInfo babyUmbrellaInfo) {
        return babyUmbrellaInfoDao.updateBabyUmbrellaInfoIfShare(babyUmbrellaInfo);
    }

    @Override
    public int newSaveBabyUmbrellaInfo(BabyUmbrellaInfo babyUmbrellaInfo) {
        return babyUmbrellaInfoDao.newSaveBabyUmbrellaInfo(babyUmbrellaInfo);
    }

    @Override
    public int deleteUmbrellaByOpenid(String openid) {
        return babyUmbrellaInfoDao.deleteUmbrellaByOpenid(openid);
    }

    @Override
    public int updateBabyUmbrellaInfoStatus(BabyUmbrellaInfo babyUmbrellaInfo) {
        return babyUmbrellaInfoDao.updateBabyUmbrellaInfoStatus(babyUmbrellaInfo);
    }

    @Override
    public int deleteByUmbrellaId(Integer id) {
        return umbrellaFamilyInfoDao.deleteByUmbrellaId(id);
    }

    @Override
    public int getUmbrellaRank(Map<String, Object> map) {
        return babyUmbrellaInfoDao.getUmbrellaRank(map);
    }


    @Override
    public int saveOpenidToMongoDB(UmbrellaMongoDBVo entity) {
        umbrellaMongoDBService.insert(entity);
        return 0;
    }

    @Override
    public List<UmbrellaMongoDBVo> getUmbrellaMongoDBVoList(Query query) {
        return umbrellaMongoDBService.queryList(query);
    }

    @Override
    public List<HashMap<String, Object>> findStatisticsList(String start, String end) {
        Date startDate = DateUtils.StrToDate(start,"date");
        Date endDate = DateUtils.StrToDate(end,"date");

        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        Map<String, Object> searchMap0 = new HashMap<String, Object>();
        searchMap0.put("date", DateUtils.addDays(startDate,-1));
        Integer totalUser0 = babyUmbrellaInfoDao.getBabyUmbrellaInfoTotalUser(searchMap0);

        Integer totalFamily0 = babyUmbrellaInfoDao.getBabyUmbrellaInfoTotalFamily(searchMap0);

        while(startDate.getTime() < endDate.getTime()){
            Map<String, Object> searchMap = new HashMap<String, Object>();
            searchMap.put("date", startDate);
            Integer totalUser = babyUmbrellaInfoDao.getBabyUmbrellaInfoTotalUser(searchMap);
            Integer totalFamily = babyUmbrellaInfoDao.getBabyUmbrellaInfoTotalFamily(searchMap);
            Integer addUser = totalUser-totalUser0;
            Integer addFamily = totalFamily-totalFamily0;

            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("date",DateUtils.formatDate(startDate));
            map.put("totalUser",totalUser);
            map.put("totalFamily",totalFamily);
            map.put("addUser",addUser);
            map.put("addFamily",addFamily);
            list.add(map);

            startDate = DateUtils.addDays(startDate,1);
            totalFamily0 = totalFamily;
            totalUser0 = totalUser;
        }

        return list;
    }


}
