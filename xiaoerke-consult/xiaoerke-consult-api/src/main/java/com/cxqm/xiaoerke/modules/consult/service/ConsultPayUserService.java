package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionPropertyVo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wangbaowei on 16/6/27.
 */
public interface ConsultPayUserService {

    //检测用户是否需要付费
    boolean angelChargeCheck(String openid);

    //推送消息记录--排队
    void saveChargeUser(Integer sessionid, String openid);

    //检测是否给需付费用户推送了消息--排队
    String getChargeInfo(Integer sessionid);

    //推消息--四种消息
    void sendMessageToConsult(String openid,int type);

    //查询需要付费的用户信息
    ConcurrentHashMap<String,Object> getneepPayConsultSession(String csuserId);

    ConsultSessionPropertyVo selectUserSessionPropertyByVo(ConsultSessionPropertyVo consultSessionPropertyVo);

    //保存需要付费的用户信息
    void putneepPayConsultSession(String csuserId,  ConcurrentHashMap<String,Object> payInfo);

    //移除需要付费的用户信息
    void removePayConsultSession(String openid,String csuserid);

}
