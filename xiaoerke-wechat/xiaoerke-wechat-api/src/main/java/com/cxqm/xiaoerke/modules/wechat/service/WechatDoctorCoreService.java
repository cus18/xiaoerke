package com.cxqm.xiaoerke.modules.wechat.service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by wangbaowei on 15/11/4.
 */
public interface WechatDoctorCoreService {

    String processDoctorRequest(HttpServletRequest request) throws IOException;

}
