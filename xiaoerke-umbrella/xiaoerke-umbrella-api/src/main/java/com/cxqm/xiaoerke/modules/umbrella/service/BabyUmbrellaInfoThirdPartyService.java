package com.cxqm.xiaoerke.modules.umbrella.service;

import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.entity.ValidateBean;

import java.util.List;
import java.util.Map;

/**
 * Created by guozengguang on 16/7/6.
 */
public interface BabyUmbrellaInfoThirdPartyService {

    /**
     *根据手机号查询该用户是否购买宝护伞
     */
    List<Map<String, Object>> getIfBuyUmbrellaByOpenidOrPhone(Map<String,Object> map);

    /**
     *根据手机号查询该用户是否已经关注平台
     */
    Map<String,Object> getStatusByPhone(Map<String,Object> map);

    /**
     *根据手机号查询验证码
     */
    ValidateBean getIdentifying(String phoneNum);

    /**
     * 根据手机号创建用户信息
     */
    User saveUserInfo(String phone);
}
