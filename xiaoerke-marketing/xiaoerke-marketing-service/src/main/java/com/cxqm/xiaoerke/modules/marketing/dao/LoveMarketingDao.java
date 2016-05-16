package com.cxqm.xiaoerke.modules.marketing.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.marketing.entity.LoveMarketing;

import java.util.List;
import java.util.Map;

/**
 * Created by feibendechayedan on 16/5/11.
 */
@MyBatisDao
public interface LoveMarketingDao {

    Map<String,Object> getLoveMarketingByOpenid(String openid);

    int saveLoveMarketing(LoveMarketing loveMarketing);

    int updateLoveMarketing(LoveMarketing loveMarketing);

    List<Map<String,Object>> getOpenidByMarketer(String id);

    Map<String,Object> getOpenidById(String id);

    Map<String,Object> getOpenidByTopMarketer(String id);
}
