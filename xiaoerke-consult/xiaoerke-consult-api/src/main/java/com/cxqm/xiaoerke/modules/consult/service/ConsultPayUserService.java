package com.cxqm.xiaoerke.modules.consult.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
    HashMap<String,Object> getneepPayConsultSession(String csuserId);

    //保存需要付费的用户信息
    void putneepPayConsultSession(String csuserId, HashMap<String,Object> payInfo);

    //移除需要付费的用户信息
    void removePayConsultSession(String openid,String csuserid);

}
