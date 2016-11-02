package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.consult.dao.SendMindCouponDao;
import com.cxqm.xiaoerke.modules.consult.entity.SendMindCouponVo;
import com.cxqm.xiaoerke.modules.consult.service.SendMindCouponService;
import com.cxqm.xiaoerke.modules.consult.service.SendMindCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文案配置实现类
 * @author sunxiao
 * 2016-10-31
 */

@Service
public class SendMindCouponServiceImpl implements SendMindCouponService {

    @Autowired
    SendMindCouponDao messageContentConfDao;

    @Override
    public List<SendMindCouponVo> findSendMindCouponByInfo(SendMindCouponVo vo) {
        return messageContentConfDao.findSendMindCouponByInfo(vo);
    }

    @Override
    public String saveSendMindCoupon(SendMindCouponVo vo) {
        if(StringUtils.isNotNull(vo.getId()+"")){
            messageContentConfDao.updateSendMindCoupon(vo);
            return "修改成功";

        }else{
            messageContentConfDao.saveSendMindCoupon(vo);
            return "保存成功";
        }
    }


    @Override
    public void deleteSendMindCoupon(SendMindCouponVo vo) {
        messageContentConfDao.deleteSendMindCoupon(vo);
    }
}
