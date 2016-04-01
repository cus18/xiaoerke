package com.cxqm.xiaoerke.modules.operation.service;

import java.util.HashMap;

/**
 * 用户预约统计 Service
 *
 * @author deliang
 * @version 2015-11-05
 */

public interface UserAppoinService  {

    HashMap<String, Object> getUserAppoinStatisticData(HashMap<String, Object> newMap) throws Exception;
}


