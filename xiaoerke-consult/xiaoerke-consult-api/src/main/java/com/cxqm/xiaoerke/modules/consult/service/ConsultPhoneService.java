package com.cxqm.xiaoerke.modules.consult.service;

import java.util.Date;
import java.util.Map;

/**
 * Created by wangbaowei on 16/3/10.
 */
public interface ConsultPhoneService {

    public String parseCallAuth( Map<String, Object> map);

    public String parseCallEstablish(  Map<String, Object> map);

    public String parseHangup( Map<String, Object> map);

}
