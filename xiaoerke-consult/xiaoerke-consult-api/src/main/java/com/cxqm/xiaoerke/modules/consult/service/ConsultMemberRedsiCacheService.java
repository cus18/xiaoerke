package com.cxqm.xiaoerke.modules.consult.service;

/**
 * Created by wangbaowei on 16/12/8.
 */
public interface ConsultMemberRedsiCacheService {


    long saveConsultMember(String... value);

    String getConsultMember(String match);

    boolean cheackConsultMember(String key);
}
