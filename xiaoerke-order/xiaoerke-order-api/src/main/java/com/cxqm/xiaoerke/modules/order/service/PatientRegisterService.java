package com.cxqm.xiaoerke.modules.order.service;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.service.ServiceException;
import com.cxqm.xiaoerke.modules.account.entity.PayRecord;
import com.cxqm.xiaoerke.modules.order.entity.OrderPropertyVo;
import com.cxqm.xiaoerke.modules.order.entity.PatientRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.entity.UserReturnVisitVo;
import com.cxqm.xiaoerke.modules.sys.entity.PerAppDetInfoVo;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangbaowei on 15/11/12.
 */
public interface PatientRegisterService {
	//对预约的状态进行操作（预约确认） @author 14_zdl
    Map<String, Object> orderAppointOperation(Map<String, Object> params, HttpSession session,HttpServletRequest request);

	//获取一个医生某天的预约列表
    List<HashMap<String, Object>> findAllAppointmentInfoByDoctor(HashMap<String, Object> dataInfo);

    Page<HashMap<String, Object>> findPersonRegisterNoBypatientId(HashMap<String, Object> saveHash, Page<HashMap<String, Object>> page);

    Date getOrderCreateDate(String patientRegistId);

	//根据手机号查询该用户的未支付订单
    Page<PatientRegisterServiceVo> appointmentByPhone(Page<PatientRegisterServiceVo> page, HashMap<String, Object> hashMap);

    String getUserIdByPatientRegisterId(String patientRegistId);

	//获取个人的预约信息详情 @author 0_zdl
    Map fidPersonAppointDetailInfo(PerAppDetInfoVo PerAppDetInfoVo);

	//获取个人中的预约信息列表  @author 03_zdl
    Page<PatientRegisterServiceVo> findPersonAppointDetailInfoList(Page<PatientRegisterServiceVo> page, HashMap<String, Object> hashMap);

	//获取个人中心主页的信息 @ author 13_zdl
    HashMap getPerCenterPageInfo(HashMap<String, Object> hashMap);

    List<HashMap<Integer, Object>> getUnBindUserOrder(String unBindUserPhoneNum);

    HashMap<String, Object> checkOrder(String phone, String babyName,
                                       String reid) throws ParseException;

	//获取医生某个出诊日期的预约信息
    void findDoctorAppointmentInfoByDate(String doctorId, String date, HashMap<String, Object> response);

    Map<String, Object> orderPayOperation(Map<String, Object> params, OrderPropertyVo orderPropertyVo, HttpSession session, HttpServletRequest request);

    Map<String, Object> orderCancelOperation(Map<String, Object> params, HashMap<String, Object> excuteMap, HttpSession session);

	//一鍵預約
    Map<String, Object> checkUserAppointment(Map<String, Object> params, HttpSession session);

    void insertUserAppointmentNum(Map<String, Object> params, HttpSession session);

    Map<String, Object> getUserOrderDetail(Map<String, Object> params);

    Map<String, Object> getUserOrderList(Map<String, Object> params);

    Map<String, Object> getOrderListByUserPhone(Map<String, Object> params, HttpServletRequest request, HttpServletResponse httpResponse);

    Map<String, Object> checkOrderInfo(Map<String, Object> params, HttpServletRequest request, HttpServletResponse httpResponse);

    Map<String, Object> getReminderOrder(Map<String, Object> params);

	//获取医生某个出诊日期的结算信息
    void findDoctorSettlementAppointmentInfoByDate(String doctorId, String userId, String date, HashMap<String, Object> response);

    int saveDoctorAppointmentInfo(Map<String, Object> map);

    Map<String, Object> getUnNormalCheckOrder(Map<String, Object> params);

    Page<PatientRegisterServiceVo> findRegisterPatientList(
            Page<PatientRegisterServiceVo> page, PatientRegisterServiceVo pvo,
            String warn);

    List<PatientRegisterServiceVo> getPatientRegisterList(
            PatientRegisterServiceVo vo);

    void cancelAppointment(String registerId, String patientRegisterId,String memberServiceId,String keep,String reason,String deleteBy);

    void savePatientRegisterService(
            PatientRegisterServiceVo patientRegisterServiceVo, OrderPropertyVo orderPropertyVo);

    HashMap<String, Object> getPatientRegisterInfo(
            HashMap<String, Object> paramMap);

    HashMap<String, Object> patientReturnVisitDetail(String patientId,
                                                     String sysRegisterId);

    void saveUserReturnVisit(UserReturnVisitVo UserReturnVisitVo);

    int getNeedPayStatusByRegisterService(String register_service_id, String openid);

    PatientRegisterServiceVo selectByPrimaryKey(String id);

    Map findPersonAppointDetailInfo(PerAppDetInfoVo PerAppDetInfoVo);

//    public void handleWxNotifyInfo(HttpServletRequest request,String accountPay, OrderPropertyVo orderPropertyVo, HttpSession session,PayRecord payRecord,String token);

	HashMap<String, Object> findSysRegisterServiceByPRSIdExecute(HashMap<String, Object> hashMap);

	//完成分享更新patient_register_service状态
    void completeShareExecute(HashMap<String, Object> executeMap);
    
  //根据用户信息，获取用户订购业务的分页信息
    List<PatientRegisterServiceVo> findPageRegisterServiceByPatient(Map map);
    
  //对预约的状态进行操作（进行评价） @author 14_zdl
    void PatientRegisterServiceIsService(HashMap<String, Object> executeMap);

    List<PatientRegisterServiceVo> findAllPatientRegisterService(HashMap seachMap);

    void handleWxMemberServiceNotifyInfo(String accountPay, PayRecord payRecord, String token);

    PatientRegisterServiceVo selectOrderInfoByPatientId(PatientRegisterServiceVo patientRegisterServiceVo);

    Boolean judgeUserOrderRealtion(HttpSession session, HttpServletRequest request);

    void orderFreePayOperation(String openId,String patient_register_service_id);

    @Transactional(rollbackFor = Exception.class)
    HashMap<String,Object> updateMemberOrderStatus(String patientRegisterServiceId, String memSrsItemSrsRelId)throws ServiceException;

    /**
     * 根据订单主键查询医生与医院的关联关系
     * @param patientRegisterServiceId
     * @return
     */
    String getRegisterAttributeOfHospital(String patientRegisterServiceId);

    HashMap<String, Object> getOrderInfoById(
            HashMap<String, Object> paramMap);

    List<Map<String, Object>> getHealthRecordsAppointmentInfo(String userPhone, String babyName);


    HashMap<String, Object> getlastPatientRegisterInfo(String userId);
}
