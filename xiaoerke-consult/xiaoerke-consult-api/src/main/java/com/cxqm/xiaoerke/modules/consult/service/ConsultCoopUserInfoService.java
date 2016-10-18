package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.consult.entity.ConsultCoopUserInfoVo;

import java.util.List;

/**
 * Created by jiangzhongge on 2016-9-21.
 *
 */
public interface ConsultCoopUserInfoService {

    int saveConsultCoopUserInfo(ConsultCoopUserInfoVo consultCoopUserInfo);

    int deleteConsultCoopUserInfo(ConsultCoopUserInfoVo consultCoopUserInfo);

    int updateConsultCoopUserInfo(ConsultCoopUserInfoVo consultCoopUserInfo);

    ConsultCoopUserInfoVo findConsultCoopUserInfoById(Integer id);

    List<ConsultCoopUserInfoVo> findConsultCoopUserInfoByCondition(ConsultCoopUserInfoVo consultCoopUserInfo);

}
