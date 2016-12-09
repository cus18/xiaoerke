package com.cxqm.xiaoerke.modules.consult.service;

/**
 * Created by wangbaowei on 16/12/8.
 */
public interface ConsultMemberRedsiCacheService {


    void saveConsultMember(String key,String value);

    String getConsultMember(String match);

    boolean cheackConsultMember(String key);

    boolean useFreeChance(String openid,String timeLength);
}
