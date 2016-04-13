package com.cxqm.xiaoerke.modules.order.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneManuallyConnectVo;

import java.util.HashMap;

/**
 * 电话咨询手动接通电话记录
 * @author sunxiao
 * @version 2016-04-13
 */
@MyBatisDao
public interface ConsultPhoneManuallyConnectRecordDao {

    //修改手动接通电话记录信息
    int updateManuallyConnectRecordInfo(ConsultPhoneManuallyConnectVo vo);

    //插入手动接通电话记录信息
    void saveManuallyConnectRecordInfo(ConsultPhoneManuallyConnectVo vo);

    //查询手动接通电话记录信息
    ConsultPhoneManuallyConnectVo getManuallyConnectRecordByInfo(HashMap<String, Object> hashMap);
}
