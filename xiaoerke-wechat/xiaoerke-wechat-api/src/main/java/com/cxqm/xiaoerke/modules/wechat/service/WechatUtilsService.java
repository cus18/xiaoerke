package com.cxqm.xiaoerke.modules.wechat.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 微信关注服务接口
 * @author Frank
 * @date 2015-12-15
 */

@Service
@Transactional(readOnly = false)
public interface WechatUtilsService {

	boolean checkUserAppointment(String openId);

}
