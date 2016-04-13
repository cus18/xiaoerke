/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.sys.entity.MessageVo;
import com.cxqm.xiaoerke.modules.sys.entity.PerAppDetInfoVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 审批DAO接口
 *
 * @author deliang
 * @version 2014-05-16
 */
@MyBatisDao
public interface MessageDao {

	HashMap<String,Object> findMessageNeedToInsert(HashMap<String,Object> map);

    //支付完成生成消息需要的数据
    HashMap<String, Object> findMessageNeedToInsertExecute(HashMap<String, Object> hashMap);

    HashMap<String,Object> findAppointMessageExecute(HashMap<String,Object> hashMap);
  
    //支付完成插入消息
    void InsertMessageExecute(HashMap<String, Object> executeMap);

    //根据订单号查询消息
    List<MessageVo> findMessageByRegisterNoExecute(HashMap<String, Object> hashMap);

    //查询插入评价提醒消息，所需要的信息，就诊后
    List<HashMap<String,Object>> evaluateReminderExecute();

    //查询插入评价提醒消息，所需要的信息，就诊前三分钟
    List<HashMap<String,Object>> evaluateAppointReminderExecute();

    //查询插入出发提醒消息，所需要的信息
    List<HashMap<String,Object>> LetsGoReminderExecute();

    //订单生成5分钟内未支付 @author zdl
    List<HashMap<String,Object>> AppointNoPayExecute();

    //获取分享信息详情 @author zdl
    HashMap<String,Object> findShareDetailInfoExecute(HashMap<String,Object> hashMap);
    HashMap<String,Object> findPhoneConsultShareDetailInfoExecute(HashMap<String, Object> hashMap);

    //插入监听器
    void insertMonitorExecute(HashMap<String,Object> monitorMap);

    //更新sys_monitor表状态
    void setMonitorStatusExecute(HashMap<String,Object> hashMap);

    public void saveAdvice(Map<String, Object>  hashMap);

    HashMap<String,Object> getCancelAppointmentDocNum(String patientRegisterServiceId);

    List<HashMap<String, Object>> getTrackOrder();

    //获取个人的预约信息详情 @author 得良
    Map fidPersonAppointDetailInfoExcut(PerAppDetInfoVo perAppDetInfoVO);

    Map consultPhoneMsgRemind(String id);

    void insertMonitorConsultPhone(HashMap<String,Object> monitorMap);
}
