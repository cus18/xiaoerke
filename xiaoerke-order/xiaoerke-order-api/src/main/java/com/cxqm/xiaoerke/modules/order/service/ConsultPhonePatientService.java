package com.cxqm.xiaoerke.modules.order.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.exception.CreateOrderException;

/**
 * Created by wangbaowei on 16/3/18.
 */
public interface ConsultPhonePatientService {

    Map<String,Object> getPatientRegisterInfo(Integer patientRegisterId);

    int PatientRegister(String openid, String babyId, String babyName, Date birthDay, String phoneNum, String illnessDesc, int sysConsultPhoneId) throws CreateOrderException;

    List<HashMap<String,Object>> getOrderList(String userId);

    int cancelOrder(Integer phoneConsultaServiceId,String cancelReason);

    int updateOrderInfoBySelect(ConsultPhoneRegisterServiceVo vo);

    /**
     * 分页查询电话咨询订单列表
     * sunxiao
     */
    Page<ConsultPhoneRegisterServiceVo> findConsultPhonePatientList(Page<ConsultPhoneRegisterServiceVo> page,ConsultPhoneRegisterServiceVo vo);
}
