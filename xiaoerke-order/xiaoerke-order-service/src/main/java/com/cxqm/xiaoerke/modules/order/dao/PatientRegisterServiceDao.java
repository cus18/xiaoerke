package com.cxqm.xiaoerke.modules.order.dao;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.order.entity.OrderServiceVo;
import com.cxqm.xiaoerke.modules.order.entity.PatientRegisterServiceVo;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorHospitalRelationVo;
import com.cxqm.xiaoerke.modules.sys.entity.PerAppDetInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MyBatisDao
public interface PatientRegisterServiceDao {

    int deleteByPrimaryKey(String id);

    int insert(PatientRegisterServiceVo record);

    int insertSelective(PatientRegisterServiceVo record);

    PatientRegisterServiceVo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PatientRegisterServiceVo record);

    int updateByPrimaryKey(PatientRegisterServiceVo record);

    /**
     * 获取未绑定用户的的订单信息
     * */
    Page<PatientRegisterServiceVo> getUnBindUserPhoneOrder(Page<PatientRegisterServiceVo> page,HashMap<String, Object> param);

    //根据手机号获取未支付订单
    Page<PatientRegisterServiceVo> findPersonAppointDetailInfoListByPhone(Page<PatientRegisterServiceVo> page,
                                                                          HashMap<String, Object> hashMap);

    //获取个人的预约信息详情 @author 得良
    Map findPersonAppointDetailInfo(PerAppDetInfoVo PerAppDetInfoVo);

    //获取个人中的预约信息列表  @author 03_zdl
    Page<PatientRegisterServiceVo> findPersonAppointDetailInfoList(Page<PatientRegisterServiceVo> page, HashMap<String, Object> hashMap);

    /**
     * 获取未绑定状态下用户订单信息
     * */
    List<HashMap<Integer, Object>> getUnBindUserNum(HashMap<String,Object> map);

    //获取个人中心主页的信息 @ author 13_zdl
    int getPerCenterPageInfo(HashMap<String, Object> hashMap);

    void savePatientRegisterService(HashMap<String, Object> executeMap);

    Page<PatientRegisterServiceVo> findRegisterPatientList(Page<PatientRegisterServiceVo> page , PatientRegisterServiceVo patientRegisterServiceVo);

    HashMap<String,Object> getPatientRegisterInfo(HashMap<String, Object> executeMap);

    void updatePatientRegisterStatusCancelById(Map<String, Object> executeMap);

    //根据用户信息，获取用户订购业务的分页信息
    List<PatientRegisterServiceVo> findPageRegisterServiceByPatient(Map map);

    //（选择支付方式，完成支付）插入和更改一天用户订购的加号业务信息
    void InsertOrUpdatePatientRegisterService(HashMap<String, Object> executeMap);

    //（选择支付方式，完成支付） @author 14_zdl
    void UpdatePatientRegisterServiceIsPay(HashMap<String, Object> executeMap);

    //对预约的状态进行操作（取消预约） @author 14_zdl
    void CancelPatientRegisterService(HashMap<String, Object> executeMap);

    //对预约的状态进行操作（进行评价） @author 14_zdl
    void PatientRegisterServiceIsService(HashMap<String, Object> executeMap);

    //完成分享更新patient_register_service状态
    void completeShareExecute(HashMap<String, Object> executeMap);

    //根据patientRegisterServiceId查询sys_register_service表信息
    HashMap<String,Object> findSysRegisterServiceByPRSIdExecute(HashMap<String,Object> hashMap);

    //获取该医生这一段时间内的所有成功的订单
    List<PatientRegisterServiceVo> findAllPatientRegisterService(HashMap seachMap);

    List<PatientRegisterServiceVo> findOrders(PatientRegisterServiceVo patientRegisterServiceVo);

    int countValidReserve(HashMap paramMap);

    List<HashMap<String,Object>> findDoctorSettlementAppointmentInfoByDate(Map paramData);

    List<HashMap<String,Object>> getValidReserveList(HashMap paramMap);

    HashMap<String,Object> getNeedPayStatusByRegisterServiceId(String register_service_id);

    /**
     * 每天同一个手机号不能超过2个号
     * @param phone
     * @return 
     */
    Integer getOrderCheckEverydayForPhone(String phone);
    
    /**
     * 每月同一个手机号不能超过5个号
     * @param phone
     * @return
     */
    Integer getOrderCheckEverymonthForPhone(String phone);
    
    /**
     * 每天同一个手机号，同一个儿童不能预约同一医生的两个号
     * @return
     */
    List<HashMap<String,Object>> getOrderCheckEverydayForDoctor(HashMap<String,String> m);
   
    /**
     * 通过医生ID获取医生姓名
     * @param doctorid
     * @return
     */
    String getDoctorName(String doctorid);
    
    //每天同一个手机号，同一个儿童不能预约同时间段的两个号。（就诊时间，间隔1小时）
    /**
     *  通过手机号和宝宝姓名，查询号源ID
     * @return
     */
    List<HashMap<String,String>> getOrderCheckEverydaySpaceOneHoursOfRegisterID(HashMap<String,String> m);
    
    /**
     * 通过号源ID查询时间
     * @param reid
     * @return
     */
    HashMap<String,String> getOrderCheckEverydaySpaceOneHoursOfRegisterTime(String reid);

    //根据patientId获取用户的订单号   @author 001_zdl
    Page<HashMap<String,Object>> findPersonRegisterNoByPatientIdExecute(HashMap<String, Object> saveHash, Page<HashMap<String,Object>> page);

    Integer checkCattleOrder(String openid);

    PatientRegisterServiceVo selectOrderInfoById(PatientRegisterServiceVo patientRegisterServiceVo);


    void UpdatePatientRegisterService(HashMap<String, Object> executeMap);
    
    List<PatientRegisterServiceVo> getPatientRegisterList(PatientRegisterServiceVo vo);

    /**
     * 根据订单主键查询该订单下的医生与医院的关系
     * @param pVo
     * @return
     */
    DoctorHospitalRelationVo getRegisterAttributeOfHospital(PatientRegisterServiceVo pVo);

    List<PatientRegisterServiceVo> findAttentionInfoByOpenIdLists(
            PatientRegisterServiceVo patientRegisterServiceVo);

    HashMap<String,Object> getOrderInfoByPatientRegistId(HashMap<String, Object> executeMap);


    /**
     * 获取健康管理的宝宝预约信息内容
     * @param userPhone 用户手机
     * @param babyId 宝宝姓名
     * @return Map
     */
    List<Map<String, Object>> getHealthRecordsAppointmentInfo(@Param("babyId")String babyId, @Param("userPhone")String userPhone);

    /**
     * 获取最近一次用户预约的信息
     * */
    HashMap<String, Object> getlastPatientRegisterInfo(@Param("userPhone")String userPhone);

    //预约订单列表
    List<HashMap<String,Object>> getAppointOrderList(String userId);
    /**
     * 预约订单列表(带分页)
     * @param page
     * @param searchMap
     * @return
     * @author chenxiaoqiong
     */
    Page<OrderServiceVo> getAppointOrderPageList(Page<OrderServiceVo> page, HashMap<String, Object> searchMap);
}