package com.cxqm.xiaoerke.modules.account.service.impl;

import com.cxqm.xiaoerke.modules.account.dao.PayRecordDao;
import com.cxqm.xiaoerke.modules.account.entity.PayRecord;
import com.cxqm.xiaoerke.modules.account.service.PayRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wangbaowei on 15/11/5.
 */
@Service
@Transactional(readOnly = false)
public class PayRecordServiceImpl implements PayRecordService {

    @Autowired
    private PayRecordDao payRecordDao;

	@Override
    public void insertPayInfo(PayRecord payRecord) {
        payRecordDao.insertSelective(payRecord);
    }

    @Override
    public void updatePayInfoByPrimaryKeySelective(PayRecord payRecord,String token) {
        payRecordDao.updateByPrimaryKeySelective(payRecord);
    }
    
	@Override
	public PayRecord findSuccessOrderByOrderId(String orderId) {
		return payRecordDao.selectSuccessOrderByOrderId(orderId);
	}

	@Override
	public int updateByOrderId(PayRecord record){
		return payRecordDao.updateByOrderId(record);
	}

	@Override
	public PayRecord findRecordByOpenid(String openid,String type) {
		return payRecordDao.findRecordByOpenid(openid, type);
	}


	@Override
	public List<PayRecord> findNewestOrderByOrderId(String orderId) {
		return payRecordDao.selectNewestOrderByOrderId(orderId);
	}

	@Override
	public boolean selectUserPayInfo(PayRecord record) {
		List<PayRecord> payRecords =  payRecordDao.selectUserPayInfo(record);
		return  payRecords.size()>0?true:false;
	}


	@Override
	public PayRecord findById(String id) {
		return payRecordDao.selectByPrimaryKey(id);
	}
	
	@Override
	public PayRecord findPayRecordByOrder(String orderId) {
		return payRecordDao.selectByOrder(orderId);
	}

  /**
   *
   */
    public int updatePayRecordByOrderId(PayRecord payRecord){
        return payRecordDao.updatePayRecordByOrderId(payRecord);
    }
}
