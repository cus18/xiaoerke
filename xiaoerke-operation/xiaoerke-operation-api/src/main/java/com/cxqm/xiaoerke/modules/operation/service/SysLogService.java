package com.cxqm.xiaoerke.modules.operation.service;

import java.util.HashMap;
import java.util.List;

/**
 * syslog Service
 *
 * @author deliang
 * @version 2015-11-05
 */

public interface SysLogService  {
	List<HashMap<String,Object>> getUserListYesterday(String date);
	
	HashMap<String,Object> getUserOperationStatistic(HashMap<String,Object> data);
}
