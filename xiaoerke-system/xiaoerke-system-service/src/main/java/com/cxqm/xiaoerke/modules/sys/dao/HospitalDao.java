/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;
import com.cxqm.xiaoerke.modules.sys.entity.HospitalVo;

/**
 * 审批DAO接口
 *
 * @author thinkgem
 * @version 2014-05-16
 */
@MyBatisDao
public interface HospitalDao {

    //获取所有的医院分页信息
    Page<HashMap<String, Object>> findPageAllHospital(Page<HashMap<String, Object>> page);

    //查询医院下面的科室
    Page<HashMap<String, Object>> findPageDepartmentByHospital(HashMap<String, Object> hospitalInfo,
                                                               Page<HashMap<String, Object>> page);

    //分页获取可预约日期下的医院
    Page<HashMap<String, Object>> findPageHospitalByTime(HashMap<String, Object> hospitalInfo,
                                                         Page<HashMap<String, Object>> page);

    //分页获取二类疾病下所关联的医院
    Page<HashMap<String, Object>> findPageHospitalByIllnessSecond(HashMap<String, Object> secondIllnessInfo,
                                                                  Page<HashMap<String, Object>> page);
    
    //批量插入医院信息
    void insertHospitalListExecute(List<HashMap<String, Object>> arrayList);

    //根据医院名称获取医院id
    HashMap<String,Object> findHospitalIdByHospitalName(HashMap<String,Object> hashMap);

    //==============================以下部分属于运维系统==============================================

    //插入医院信息(单条) zdl
    void insertHospital(HospitalVo hospitalVo);

    //根据医院id查询医院
    HospitalVo getHospital(HospitalVo hospitalVo);

    //获取系统内的所有医院
    List<HospitalVo> findAllHospital(HospitalVo hospitalVo);

    //医院修改操作
    void  saveEditHospital(HospitalVo hospitalVo);

    /**
     * 删除医院操作
     */
    void deleteHospitalByHospitalId(String hopitalId);

    //获取所有有订单存在的医院
    List<HospitalVo> findAllOrderHospital(HashMap<String,Object> newMap);
    
    List<HospitalVo> findHospitalsByDoctorIds(@Param("doctorIds") String[] doctorIds);
    
    /**
     * 查询所有与该医生有关的医院
     */
    List<DoctorVo> findAllHospitalByDoctorId(DoctorVo doctorVo);

    Page<HashMap<String, Object>> findAllHospitalByConsulta(Page<HashMap<String, Object>> page);
    
    /**
     * 根据条件修改sys_hospital表
     * sunxiao
     * @param param
     */
    void updateSysHospital(Map<String, Object> param);

}
