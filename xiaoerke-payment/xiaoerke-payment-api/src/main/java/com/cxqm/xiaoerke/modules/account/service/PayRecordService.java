package com.cxqm.xiaoerke.modules.account.service;

import java.util.List;

import com.cxqm.xiaoerke.modules.account.entity.PayRecord;

import org.springframework.transaction.annotation.Transactional;

/**
 * Created by wangbaowei on 15/11/5.
 */
@Transactional(readOnly = false)
public interface PayRecordService {

    void insertPayInfo(PayRecord payRecord);

    void updatePayInfoByPrimaryKeySelective(PayRecord payRecord,String token);
    
    PayRecord findSuccessOrderByOrderId(String orderId);

	List<PayRecord> findNewestOrderByOrderId(String orderId);

	PayRecord findById(String id);

	PayRecord findPayRecordByOrder(String orderId);

    int updateByOrderId(PayRecord record);



}
