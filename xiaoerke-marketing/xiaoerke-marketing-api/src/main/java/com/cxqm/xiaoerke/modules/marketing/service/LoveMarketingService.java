package com.cxqm.xiaoerke.modules.marketing.service;

import com.cxqm.xiaoerke.modules.marketing.entity.LoveMarketing;

import java.util.Map;

/**
 * Created by feibendechayedan on 16/5/11.
 */
public interface LoveMarketingService {

    /**
     * 通过Openid获取用户头像和昵称
     * @param openid
     * @return
     */
    Map<String,Object> getNicknameAndHeadImageByOpenid(String openid);

    /**
     * 通过Openid 生成用户专属临时二维码
     * @param openid
     * @return 二维码图片地址
     */
    String getUserQRcode(String openid) throws Exception;

    /**
     * 根据链接下载文件
     * @param urlString
     * @param filename
     * @param savePath
     * @return
     */
    void download(String urlString, String filename,String savePath) throws Exception;


    Map<String,Object> getLoveMarketingByOpenid(String openid);

    int saveLoveMarketing(LoveMarketing loveMarketing);

    int updateLoveMarketing(LoveMarketing loveMarketing);
}
