package com.cxqm.xiaoerke.modules.account.service;

import com.cxqm.xiaoerke.modules.account.entity.PayRecord;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wangbaowei on 15/11/5.
 */
@Transactional(readOnly = false)
public interface PayRecordService {

    void insertPayInfo(PayRecord payRecord);

    void updatePayInfoByPrimaryKeySelective(PayRecord payRecord,String token);
    
    PayRecord findSuccessOrderByOrderId(String orderId);

	List<PayRecord> findNewestOrderByOrderId(String orderId);

    boolean selectUserPayInfo(PayRecord record);

    PayRecord findById(String id);

	PayRecord findPayRecordByOrder(String orderId);

    int updateByOrderId(PayRecord record);


    PayRecord findRecordByOpenid(String openid,String type);
    /**
     *根据订单编号更新支付结果表(account_pay_record)
     */
    int updatePayRecordByOrderId(PayRecord payRecord);
}
