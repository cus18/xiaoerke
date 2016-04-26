/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.task.dao;

import com.cxqm.xiaoerke.common.bean.WechatRecord;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;

import java.util.HashMap;
import java.util.List;

/**
 * Task DAO接口
 *
 * @author thinkgem
 * @version 2014-05-16
 */
@MyBatisDao
public interface ScheduledTaskDao {

    
  //监听就诊结束时间，就诊时间结束的话更新sys_register_service表status字段和patient_register_service表status字段 zdl
    void updateSrSAndPrsStatusExecute();
    void updatePatRegSerStatusExecute();

  //订单生成30分钟内未支付,则取消订单(更新patient_register_service中的status，并且更新sys_register_service中的状态)
    void CancelAppointNoPayExecute_1();
    void CancelAppointNoPayExecute_2();
    
    /**
     * 查询30分钟内的预约订单
     * */
    List<HashMap<String,Object>> getOrderInfoToDoc();
    
    /**
     * 更新医生短信发送状态
     * */
    int updateOrderInfoToDoc(List<String> classList);
    
    /**
     * 获取当天订单详情
     * */
    List<HashMap<String,Object>> getOrderInfoToday();

    List<HashMap<String,Object>> getOrderDoctorToday();
    
    List<HashMap<String,Object>> getSettlementDocToday(String date);
    
    List<HashMap<String,Object>> getOrderInfoAtMorning();

    List<HashMap<String,Object>> getOrderDoctorAtMorning();

    void updateWechatParameter(HashMap<String, Object> Map);
    
    void saveWechatRecoder(List<WechatRecord> list);

    List<HashMap<String,Object>> getUserOrderNestWeek();

    /**
     * 5min之前预约的电话咨询订单
     * @return
     */
    List<HashMap<String,Object>> getOrderInfoToDoc5minAfterSuccess();
}
