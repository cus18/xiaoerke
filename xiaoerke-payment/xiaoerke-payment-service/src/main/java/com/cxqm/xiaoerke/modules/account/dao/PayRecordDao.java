package com.cxqm.xiaoerke.modules.account.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.account.entity.PayRecord;
import com.cxqm.xiaoerke.modules.account.entity.Record;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisDao
public interface PayRecordDao {

    int insertSelective(PayRecord record);

    int updateByPrimaryKeySelective(PayRecord record);

    PayRecord selectSuccessOrderByOrderId(String orderId);

    List<PayRecord> selectUserPayInfo(PayRecord record);
    
    List<PayRecord> selectNewestOrderByOrderId(String orderId);

    List<Record> selectPayRecordByUserId(String userId);

    int deleteByPrimaryKey(String id);

    int insert(PayRecord record);

    PayRecord selectByPrimaryKey(String id);

    int updateByPrimaryKey(PayRecord record);

    int updateByOrderId(PayRecord record);

    PayRecord selectByOrder(String orderId);

    PayRecord findRecordByOpenid(@Param("openid")String openid,@Param("type") String type);

    int updatePayRecordByOrderId(PayRecord payRecord);

}