package com.cxqm.xiaoerke.modules.healthRecords.service.impl;

import com.cxqm.xiaoerke.common.utils.OSSObjectTool;
import com.cxqm.xiaoerke.modules.healthRecords.dao.BabyIllnessInfoDao;
import com.cxqm.xiaoerke.modules.healthRecords.entity.BabyIllnessInfoVo;
import com.cxqm.xiaoerke.modules.healthRecords.service.HealthRecordsService;
import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;

import com.cxqm.xiaoerke.modules.sys.service.BabyBaseInfoService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangbaowei on 16/1/20.
 */

@Service
@Transactional(readOnly = false)
public class HealthRecordsServiceImpl implements HealthRecordsService {

    @Autowired
    private BabyBaseInfoService babyBaseInfoService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private BabyIllnessInfoDao babyIllnessInfoDao;



    @Override
    public BabyBaseInfoVo selectByPrimaryKey(Integer id) {
      return  babyBaseInfoService.selectByPrimaryKey(id);
    }

    @Override
    public List<BabyBaseInfoVo> getUserBabyInfolist(String userId) {
      return  babyBaseInfoService.selectByUserId(userId);
    }

    @Override
    public List<BabyBaseInfoVo> getUserBabyInfolistByOpenId(String openId) {
        return  babyBaseInfoService.selectByOpenid(openId);
    }

    @Override
    public int insertBabyInfo(BabyBaseInfoVo bean) {
        return babyBaseInfoService.insertSelective(bean);
    }

    @Override
    public int updateBabyInfo(BabyBaseInfoVo bean) {
        return babyBaseInfoService.updateByPrimaryKey(bean);
    }

    @Override
    public int insertBabyIllnessInfo(BabyIllnessInfoVo bean) {
       return babyIllnessInfoDao.insertSelective(bean);

    }

    public void uploadPic(String key ,String mediaId){
        try {
            uploadArticleImage(key,mediaId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    @Override
    public List<BabyBaseInfoVo> selectUserBabyInfo(String openId) {
        return  babyBaseInfoService.selectUserBabyInfo(openId);
    }



    ;

    /**
     * 根据文件id下载文件
     *
     * @param mediaId
     *            媒体id
     * @throws Exception
     */
    public Map<String,Object> getInputStream(String mediaId) {

    Map<String,Object> resultMap = new HashMap<String, Object>();
    InputStream is = null;
    long picLen = 0;
    Map<String,Object> parameter = systemService.getWechatParameter();
    String token = (String)parameter.get("token");
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
      picLen = http.getContentLengthLong();
    } catch (Exception e) {
      e.printStackTrace();
    }
        resultMap.put("inputStream",is);
        resultMap.put("contentLength",picLen);
        return resultMap;
    }

    private void uploadArticleImage(String id , String mediaId) throws Exception{
        //上传图片至阿里云
        Map<String,Object> result = getInputStream(mediaId);
        OSSObjectTool.uploadFileInputStream(id, (Long)result.get("contentLength"), (InputStream)result.get("inputStream"), OSSObjectTool.BUCKET_ARTICLE_PIC);
    }

	@Override
	public int deleteBabyInfo(Integer id) {
		// TODO Auto-generated method stub
		return   babyBaseInfoService.deleteByPrimaryKey(id);
	}



}
