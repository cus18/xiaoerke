package com.cxqm.xiaoerke.modules.sys.service;

import com.cxqm.xiaoerke.modules.sys.entity.SysActivityVo;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = false)
public interface ActivityService {
	/**
	 * 判断活动有效期  true 过期  false 没过期
	 * @param qrCode  二维码code
	 * @return
	 */
	Boolean judgeActivityValidity(String qrCode) ;

	SysActivityVo selectByQrcode(SysActivityVo sysActivityVo);

}
