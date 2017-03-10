package com.cxqm.xiaoerke.modules.activity.service;

import java.util.Map;

/**
 * Created by wangbaowei on 17/2/28.
 */
public interface AdvisorySharingService {

    //获取某一个会话的聊天记录
    Map<String ,Object> conversationRecord(String sessionid,int pageNo,int pageSize);

//    分享动作
    void sharSeConsult(String sessionid,String content);
}
