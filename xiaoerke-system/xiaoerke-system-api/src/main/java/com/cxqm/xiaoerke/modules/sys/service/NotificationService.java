package com.cxqm.xiaoerke.modules.sys.service;


import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.sys.entity.Notification;
import com.cxqm.xiaoerke.modules.sys.entity.RemovedOrderNotification;

import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = false)
public interface NotificationService {
	
	Page<RemovedOrderNotification> findRemovedOrderNotifications(Page<RemovedOrderNotification> page);

	int saveNotification(Notification record);
	
}
