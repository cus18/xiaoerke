package com.cxqm.xiaoerke.modules.consult.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.consult.entity.SendMindCouponVo;
import com.cxqm.xiaoerke.modules.consult.service.SendMindCouponService;

import java.util.List;

/**
 * 送心意优惠券dao
 *
 * Created by sunxiao on 2016-10-31.
 */
@MyBatisDao
public interface SendMindCouponDao {

    List<SendMindCouponVo> findSendMindCouponByInfo(SendMindCouponVo vo);

    SendMindCouponVo getSendMindCouponInof(Integer id);

    void saveSendMindCoupon(SendMindCouponVo vo);

    void updateSendMindCoupon(SendMindCouponVo vo);

    void deleteSendMindCoupon(SendMindCouponVo vo);
}
