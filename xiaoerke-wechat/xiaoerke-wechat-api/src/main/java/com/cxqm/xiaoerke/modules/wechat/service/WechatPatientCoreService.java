package com.cxqm.xiaoerke.modules.wechat.service;

import com.cxqm.xiaoerke.modules.wechat.entity.HealthRecordMsgVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by wangbaowei on 15/11/4.
 */
public interface WechatPatientCoreService {

    String processPatientRequest(HttpServletRequest request,HttpServletResponse response) throws IOException;


    boolean findHealthRecordMsgByOpenid(String openid, String type);

    int insertHealthRecordMsg(HealthRecordMsgVo healthRecordMsgVo);

    void updateKeywordRecovery();
}
