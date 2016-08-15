package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionPropertyVo;

/**
 * Created by jiangzhongge on 2016-8-11.
 */
public interface ConsultSessionPropertyService {

    ConsultSessionPropertyVo findConsultSessionPropertyByUserId(String userId);

    int updateByPrimaryKey(ConsultSessionPropertyVo consultSessionPropertyVo);

    int insertUserConsultSessionProperty(ConsultSessionPropertyVo record);

    int addPermTimes(String userId);

    int updateMonthTime();

}
