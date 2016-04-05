package com.cxqm.xiaoerke.modules.consult.service.core;

import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.sys.entity.ReceiveXmlEntity;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;

public class patientConsultWechatServiceThread extends Thread{

	@Autowired
	private SystemService systemService;

	private ReceiveXmlEntity xmlEntity;

	public patientConsultWechatServiceThread(ReceiveXmlEntity xmlEntity) {
		this.xmlEntity = xmlEntity;
	}

	public void run() {
		Map parameter = systemService.getWechatParameter();
		String token = (String) parameter.get("token");
		String st = "尊敬的用户，正在帮您接通医生....";
		WechatUtil.senMsgToWechat(token, xmlEntity.getFromUserName(), st);

	}

}
