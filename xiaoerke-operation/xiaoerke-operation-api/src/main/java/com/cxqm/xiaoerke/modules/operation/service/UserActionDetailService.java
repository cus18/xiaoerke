package com.cxqm.xiaoerke.modules.operation.service;

import java.text.ParseException;
import java.util.HashMap;

/**
 * 用户行为详细记录 Service
 *
 * @author deliang
 * @version 2015-11-05
 */

public interface UserActionDetailService  {


    HashMap<String, Object> getUserFullToDoList(HashMap<String, Object> params) throws ParseException;

}
