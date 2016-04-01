package com.cxqm.xiaoerke.modules.operation.service;

import java.util.HashMap;

/**
 * 用户行为统计 Service
 *
 * @author deliang
 * @version 2015-11-19
 */

public interface UserActionStatisticService {

    HashMap<String, Object> userOperationData(HashMap<String, Object> newMap) throws Exception;


}
