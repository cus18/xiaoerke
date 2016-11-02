package com.cxqm.xiaoerke.modules.consult.service;

import com.cxqm.xiaoerke.modules.consult.entity.SendMindCouponVo;
import com.cxqm.xiaoerke.modules.consult.entity.SendMindCouponVo;

import java.util.List;

/**
 * 文案配置service
 * @author sunxiao
 * 2016-10-31
 */
public interface SendMindCouponService {

    List<SendMindCouponVo> findSendMindCouponByInfo(SendMindCouponVo vo);

    String saveSendMindCoupon(SendMindCouponVo vo);

    void deleteSendMindCoupon(SendMindCouponVo vo);

}
