package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionPropertyVo;

/**
 * Created by jiangzhongge on 2016-8-11.
 */
public interface ConsultSessionPropertyService {

    int updateByUserId(String userId);

    ConsultSessionPropertyVo findConsultSessionPropertyByUserId(String userId);

}
