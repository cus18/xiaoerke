package com.cxqm.xiaoerke.modules.order.dao;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.order.entity.RegisterServiceVo;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorLocationVo;

import java.util.Date;
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
public interface RegisterServiceDao {

    /**
     * 通过号源ID查询时间
     *
     * @param reid
     * @return
     */
    HashMap<String, String> getOrderCheckEverydaySpaceOneHoursOfRegisterTime(String reid);

    //插入号源信息
    void insertSysRegisterServiceExecute(HashMap<String, Object> hashMap);

    //插入号源信息（测试）
    void insertSysRegisterServiceTest(HashMap<String, Object> hashMap);

    //获取号源状态
    HashMap<String, Object> findSysRegisterServiceStatusExecute(HashMap<String, Object> hashMap);

    //根据信息查询号源列表  @author sunxiao
    List<RegisterServiceVo> getRegisterListByInfo(Map map);

    //查询号源列表 @author sunxiao
    Page<RegisterServiceVo> findRegisterList(Page<RegisterServiceVo> page, RegisterServiceVo RegisterServiceVo);

    //根据信息查询号源 @author sunxiao
    RegisterServiceVo getRegisterByInfo(Map map);

    RegisterServiceVo getRegisterById(Map map);

    void updateSysRegisterServiceStatusUsed(String id);

    //更新号源状态为删除 @author sunxiao
    void updateSysRegisterServiceStatusCancel(String id);

    HashMap<String, Object> getSysRegisterServiceInfo(HashMap<String, Object> executeMap);

    int updateSysRegisterStatusCancel(Map<String, Object> executeMap);

    void updateRegisterService(HashMap<String, Object> map);

    //根据location_id删除号源信息
    void deleteRegisterServiceByLocationId(DoctorLocationVo doctorLocationVo);

    //根据doctorId查询这一段时间该医生“号源总数”
    int findDoctorRegisterServiceByData(HashMap<String, Object> hashMap);

    //查询即将没有号源的医生信息   @author sunxiao
    Page<RegisterServiceVo> findWillNoRegisterList(Page<RegisterServiceVo> page);

    /**
     * 获取某医生最近的可约的详细信息
     *
     * @param id doctorId
     * @return List 详细信息列表
     */
    List<HashMap<String, Object>> getDoctorVisitInfoById(String id);

    List<HashMap<String, Object>> getDoctorVisitInfoByIdAndDate(HashMap<String, Object> Map);

    List<HashMap<String, Object>> getDoctorVisitInfo(String id);

    List<HashMap<String, Object>> getDoctorVisitInfoByLocation(Map data);

    //更新sys_register_service数据    @author zdl
    int UpdateSysRegisterService(HashMap<String, Object> executeMap);

    //对预约的状态进行操作（取消预约） @author 14_zdl
    void CancelSysRegisterService(HashMap<String, Object> executeMap);

    //获取某个医生某天的预约列表信息
    List<HashMap<String, Object>> findAllAppointmentInfoByDoctor(HashMap<String, Object> dataInfo);

    List<Date> findDatesWithRegisters(Map<String, Object> params);

    //根据sys_doctor_id查询它的最近可约时间
    HashMap<String, Object> findrecentlyDateAppDateExcute(HashMap<String, Object> hashMap);

    //批量插入号源 @author sunxiao
    void batchInsertRegister(List<HashMap<String, Object>> excuteList);

    //查询订单的就诊时间
    String getVisitTimeById(Map map);

    HashMap<String, Object> getNeedPayStatusByRegisterServiceId(String register_service_id);

    int getAppointmentDoctorNum(HashMap<String, Object> map);

    int getAppointmentNum(HashMap<String, Object> map);

    int judgeUserScanCode(HashMap<String, Object> map);

    //根据location_id删除号源信息
    void deleteRegisterServiceBySysDoctorId(DoctorLocationVo doctorLocationVo);

    HashMap<String, Object> getCooperationHospitalTypeBySrsId(String register_service_id);
}
