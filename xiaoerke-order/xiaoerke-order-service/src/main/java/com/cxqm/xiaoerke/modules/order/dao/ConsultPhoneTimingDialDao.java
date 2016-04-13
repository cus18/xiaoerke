package com.cxqm.xiaoerke.modules.order.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneManuallyConnectVo;

import java.util.HashMap;

/**
 * 电话咨询手动接通电话dao
 * @author sunxiao
 * @version 2016-04-13
 */
@MyBatisDao
public interface ConsultPhoneTimingDialDao {

    //修改手动接通任务信息
    int updateConsultPhoneTimingDialInfo(ConsultPhoneManuallyConnectVo vo);

    //插入手动接通任务信息
    void saveConsultPhoneTimingDialInfo(ConsultPhoneManuallyConnectVo UserReturnVisitVo);

    //查询手动接通任务信息
    ConsultPhoneManuallyConnectVo getConsultPhoneTimingDialByInfo(HashMap<String, Object> hashMap);
}
