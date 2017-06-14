package com.cxqm.xiaoerke.modules.order.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneManuallyConnectVo;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.exception.CancelOrderException;
import com.cxqm.xiaoerke.modules.order.exception.CreateOrderException;
import net.sf.json.JSONObject;

/**
 * Created by wangbaowei on 16/3/18.
 */
public interface ConsultPhonePatientService {

    Map<String,Object> getPatientRegisterInfo(Integer patientRegisterId);

    int PatientRegister(String openid, String babyId, String babyName, Date birthDay, String phoneNum, String illnessDesc, int sysConsultPhoneId) throws CreateOrderException;

    Map<String,Object> createConsultOrder(String babyName,String phoneNum, String illnessDesc, String doctorId) throws CreateOrderException;


    List<HashMap<String,Object>> getOrderList(String userId);

    Float cancelOrder(Integer phoneConsultaServiceId,String cancelReason,String cancelState) throws CancelOrderException;//cancelState:0是取消订单，号源设置为可用，2是删除号源

    int updateOrderInfoBySelect(ConsultPhoneRegisterServiceVo vo);

    /**
     * 分页查询电话咨询订单列表
     * sunxiao
     */
    Page<ConsultPhoneRegisterServiceVo> findConsultPhonePatientList(Page<ConsultPhoneRegisterServiceVo> page,ConsultPhoneRegisterServiceVo vo);

    /**
     * 根据条件查询订单
     * sunxiao
     * @param map
     * @return
     */
    List<Map<String, Object>> getConsultPhoneRegisterListByInfo(Map map);
    List<ConsultPhoneRegisterServiceVo> getAllConsultPhoneRegisterListByInfo(ConsultPhoneRegisterServiceVo vo);

    /**
     * 获取手动接通页面数据
     * sunxiao
     * @param vo
     */
    Map manuallyConnectFormInfo(ConsultPhoneRegisterServiceVo vo);

    /**
     * 手动接通电话
     * sunxiao
     * @param vo
     */
    JSONObject manuallyConnect(ConsultPhoneManuallyConnectVo vo) throws Exception;

    //查询手动接通任务信息
    List<ConsultPhoneManuallyConnectVo> getConsultPhoneTimingDialByInfo(Map<String, Object> map);
    int updateConsultPhoneTimingDialInfo(ConsultPhoneManuallyConnectVo vo);

    /**
     * 获取订单数
     * sunxiao
     * @param state
     * @return
     */
    int getNewOrderCount(String state);

    ConsultPhoneRegisterServiceVo selectByPrimaryKey(Integer phoneConsultaServiceId);

    int CancelAppointNoPay();

}
