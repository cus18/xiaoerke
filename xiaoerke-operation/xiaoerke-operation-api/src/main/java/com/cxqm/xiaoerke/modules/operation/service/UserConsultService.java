package com.cxqm.xiaoerke.modules.operation.service;

import org.apache.shiro.crypto.hash.Hash;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户咨询统计 Service
 *
 * @author deliang
 * @version 2015-11-05
 */
public interface UserConsultService  {

    HashMap<String, Object> getConsultStatisticData(@RequestBody Map<String, Object> params);
}
