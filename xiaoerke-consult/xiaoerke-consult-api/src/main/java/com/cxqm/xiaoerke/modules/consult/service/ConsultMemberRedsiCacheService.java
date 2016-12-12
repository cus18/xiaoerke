package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.consult.entity.ConsultMemberVo;

/**
 * Created by wangbaowei on 16/12/8.
 */
public interface ConsultMemberRedsiCacheService {

    void saveConsultMemberInfo(ConsultMemberVo vo);

    void updateConsultMemberInfo(ConsultMemberVo vo);

    ConsultMemberVo getConsultMemberInfo(String openid);


    void saveConsultMember(String key,String value);

    String getConsultMember(String match);

    boolean cheackConsultMember(String key);

    boolean useFreeChance(String openid,String timeLength);

    void payConsultMember(String openid,String timeLength,String totalFee);
}
