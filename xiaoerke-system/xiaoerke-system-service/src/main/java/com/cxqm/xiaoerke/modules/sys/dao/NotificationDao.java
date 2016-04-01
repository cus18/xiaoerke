package com.cxqm.xiaoerke.modules.sys.dao;

import java.util.List;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.sys.entity.Notification;
import com.cxqm.xiaoerke.modules.sys.entity.RemovedOrderNotification;

@MyBatisDao
public interface NotificationDao {
    int deleteByPrimaryKey(Long id);

    int insert(Notification record);

    int insertSelective(Notification record);

    Notification selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Notification record);

    int updateByPrimaryKey(Notification record);
    
    int insertByBatch(List<Notification> records);
    
    Page<RemovedOrderNotification> findRemovedOrderNotifications(Page<RemovedOrderNotification> page);
}