package com.cxqm.xiaoerke.modules.consult.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;

import java.util.Map;

/**
 * Created by wangbaowei on 16/6/27.
 */
@MyBatisDao
public interface ConsultPayUserDao {

    Integer umbrellaCheck(String openid);

    Integer CheckInsuranceByOpenid(String openid);


}
