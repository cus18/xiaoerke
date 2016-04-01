package com.cxqm.xiaoerke.modules.sys.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.sys.dao.NotificationDao;
import com.cxqm.xiaoerke.modules.sys.entity.Notification;
import com.cxqm.xiaoerke.modules.sys.entity.RemovedOrderNotification;
import com.cxqm.xiaoerke.modules.sys.service.NotificationService;

@Service
@Transactional(readOnly = false)
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private NotificationDao notificationDao;

	@Override
	public Page<RemovedOrderNotification> findRemovedOrderNotifications(
			Page<RemovedOrderNotification> page) {
		Page<RemovedOrderNotification> pageList = notificationDao.findRemovedOrderNotifications(page);
		return pageList;
	}

	@Override
	public int saveNotification(Notification record) {
		return notificationDao.insert(record);
	}

}
