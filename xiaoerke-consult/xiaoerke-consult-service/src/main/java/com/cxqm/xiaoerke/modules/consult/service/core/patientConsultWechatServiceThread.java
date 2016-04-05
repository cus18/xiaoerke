package com.cxqm.xiaoerke.modules.consult.service.core;

import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.sys.entity.ReceiveXmlEntity;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;

public class patientConsultWechatServiceThread extends Thread{

	@Autowired
	private SystemService systemService;

	@Autowired
	private SessionCache sessionCache;

	private ReceiveXmlEntity xmlEntity;

	public patientConsultWechatServiceThread(ReceiveXmlEntity xmlEntity) {
		this.xmlEntity = xmlEntity;
	}

	public void run() {
		/**根据openId，在redis中查询，此用户是否目前还存在有效的session，
		 * 如果没有，则推送欢迎词，建立与在线接诊员的会话联系
		 * */
		Integer sessionId = sessionCache.getSessionIdByOpenId(xmlEntity.getFromUserName());

		Map parameter = systemService.getWechatParameter();
		String token = (String) parameter.get("token");
		String st = "尊敬的用户，正在帮您接通医生....";
		WechatUtil.senMsgToWechat(token, xmlEntity.getFromUserName(), st);


	}

}
