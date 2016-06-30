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

    //记录付费用户推送的消息
    void saveChargeUser(Integer sessionid, String openid);

    //检测费用户信息第一次消息推送
    String getChargeInfo(Integer sessionid);

    //推消息
    void sendMessageToConsult(String openid,int type);

    //查询需要付费的用户信息
    HashMap<String,Object> getneepPayConsultSession(Integer sessionid);

    //保存需要付费的用户信息
    void putneepPayConsultSession(Integer sessionid, HashMap<String,Object> payInfo);

    //通知接诊人员超时

}
