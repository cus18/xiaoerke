/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.dao;

import com.cxqm.xiaoerke.common.persistence.CrudDao;
import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;
import org.apache.ibatis.annotations.Param;

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
public interface DoctorDao extends CrudDao<DoctorVo> {

    //分页  显示所有的医生列表
    Page<HashMap<String, Object>> findPageAllDoctor(Page<HashMap<String, Object>> page);

    //根据doctorVo中的参数获取医生的基本信息
    Map<String, Object> findDoctorDetailInfo(Map map);

    Map<String, Object> findDoctorDetailInfoAndType(Map map);

    //查找一个医院的科室下面的所有医生
    Page<HashMap<String, Object>> findPageDoctorByDepartment(HashMap<String, Object> hospitalInfo, Page<HashMap<String, Object>> page);

    //获取系统内部的推荐医生
    Page<HashMap<String, Object>> findPageDoctorByRecommend(HashMap<String, Object> recommendInfo, Page<HashMap<String, Object>> page);

    //分页获取二类疾病下所关联的医生
    Page<HashMap<String, Object>> findPageDoctorByIllnessSecond(HashMap<String, Object> secondIllnessInfo, Page<HashMap<String, Object>> page);

    Page<HashMap<String, Object>> findPageDoctorByIllnessSecond4Consult(HashMap<String, Object> secondIllnessInfo, Page<HashMap<String, Object>> page);

    //分页获取二类疾病和医院下所关联的医生
    Page<HashMap<String, Object>> findPageDoctorByIllnessSecondAndHospital(HashMap<String, Object> secondIllnessInfo, Page<HashMap<String, Object>> page);

    //分页查询一个可预约日期下的医生
    Page<HashMap<String, Object>> findPageDoctorByTime(HashMap<String, Object> dateInfo, Page<HashMap<String, Object>> page);

    Page<HashMap<String, Object>> findPageDoctorByTime4Consult(HashMap<String, Object> dateInfo, Page<HashMap<String, Object>> page);

    //获取某个医院的某个可预约日期下的医生
    Page<HashMap<String, Object>> findPageDoctorByTimeAndHospital(HashMap<String, Object> dateInfo, Page<HashMap<String, Object>> page);

    //获取医生一个加号的详细信息
    List<HashMap<String, Object>> findAppointmentInfoDetail(HashMap<String, Object> appointmentInfo);

    //关注后更新该医生的粉丝数量
    void updateDoctorFansExecute(HashMap<String,Object> hashMap);

    List<HashMap> findDoctorByInfo(HashMap<String, Object> hashMap);

    //根据userId去获取医生表的所有信息 @author zdl
    Map getDoctorIdByUserId(Map HashMap);
    
    void insertDoctor(HashMap<String,Object> hashMap);

    //根据手机号查询doctorId
    HashMap<String, Object> findDoctorIdByPhoneExecute(HashMap<String, Object> hashMap);

    HashMap<String, Object> findDoctorIdByname(HashMap<String, Object> hashMap);

    List<HashMap<String,Object>> findDoctorByDoctorId(HashMap<String, Object> hashMap);

    //新增医生数
    int findCountDoctorNumber(HashMap<String,Object> hashMap);
    //系统内总的医生数
    int findCountDoctorCountNmuber();
    
    //根据用户(医生)id获取当前用户的所有信息 @author zdl
    HashMap<String, Object> findDoctorDetailInfoByUserIdExecute(HashMap<String,Object> userId);

	//==============================运维系统部分==============================================
    List<DoctorVo> findDoctorByNameOrByHospitalName(DoctorVo doctorVo);

    /**
     * 根据doctorVo中的参数获取医生的基本信息
     * @param doctorVo
     * @return
     */
    List<DoctorVo> findDoctorVoDetailInfo(DoctorVo doctorVo);

    void deleteDoctorByDoctorId(String  doctorId) ;

    void deleteDoctorByHospitalId(String hopitalId);
    /**
     * 医生信息修改后保存操作---修改医生的信息
     */
    void saveEditDoctor(DoctorVo doctorVo);

    /**
     * 医生信息修改后保存操作---修改医生所属用户的信息
     */
    void saveEditUser(DoctorVo doctorVo);

    /**
     * 根据医院id查询该医院下有订单存在的所有医生
     */
    List<DoctorVo> findAllOrderDoctorList(HashMap<String,Object> seachMapd);


    //---------------------------------------运营系统部分-------------------------------------------------

    //根据doctorId查询“可预约号源数”
    int findDoctorCanAppointNumber(HashMap<String,Object> hashMap);

    //根据doctorId查询“已预约号源数”
    int findDoctorAlreadyAppointNumber(HashMap<String,Object> hashMap);

    List<HashMap<String,Object>> getDoctorHospitalInfo(String doctorId);

    List<HashMap<String,Object>> getDoctorAppointmentTime(HashMap<String,Object> map);

    HashMap<String,Object> findDoctorScoreInfo(String sys_doctor_id);
    
    /**
     * 通过医生ID获取医生姓名
     * @param doctorId
     * @return
     */
    String getDoctorName(String doctorId);
    
    DoctorVo findDoctorByRegisterId(@Param("registerId") String registerId);

    //查询电话咨询的医生列表
    Page<HashMap<String, Object>> findPageConsultaDoctorByDepartment(HashMap<String, Object> hospitalInfo, Page<HashMap<String, Object>> page);
    
    /**
     * 医生开通电话咨询时修改医生表 
     * @param map
     */
    void updateDoctor(Map<String, Object> map);
    
    List<Map<String, Object>> getDoctorInfo(Map<String, Object> map);

    HashMap<String,Object> findOpenIdByDoctorId(@Param(value="doctorId")String doctorId);

    List<Map<String, Object>> findPageConsultaDepartment(@Param(value="hospitalId")String hospitalId);
}
