package com.cxqm.xiaoerke.modules.umbrella.service;

import java.util.Map;

/**
 * Created by guozengguang on 16/7/6.
 */
public interface BabyUmbrellaInfoThirdPartyService {

    /**
     *根据手机号查询该用户是否购买宝护伞
     */
    boolean ifBuyUmbrella(Map<String,Object> map);

    /**
     *根据手机号查询该用户是否已经关注平台
     */
    Map<String,Object> getStatusByPhone(Map<String,Object> map);
}
