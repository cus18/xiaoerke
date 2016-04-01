/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.dao;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorDataVo;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorHospitalRelationVo;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 医生和疾病关联表的DAO接口
 *
 * @author deliang
 * @version 2014-05-16
 */
@MyBatisDao
public interface DoctorHospitalRelationDao {

    //根据医院信息，分页查找所有医生列表
    Page<HashMap<String, Object>> findPageAllDoctorByHospital(HashMap<String, Object> doctorMap, Page<HashMap<String, Object>> page);

    //获取医院的部门信息名称
    Map getDepartmentName(HashMap<String, Object> Map);

    //插入关系表数据
    void insertDoctorAndHospitalRelation(HashMap<String,Object> hashMap);

    List<DoctorHospitalRelationVo> getDoctorHospitalRelationVo(HashMap<String, Object> Map);

    //----------------------------------------以下部分属于运维系统---------------------------------------
    /**
     * 查询当前医生与当前医院的关联信息
     * @return
     */
    DoctorHospitalRelationVo findDoctorHospitalRelation(HashMap<String,Object> hashMap);

    /**
     * “当前医生与当前医院的关联信息”修改后保存操作
     * @return
     */
    void saveDoctorHospitalRelation(DoctorHospitalRelationVo doctorHospitalRelationVo);
    /**
     * 删除医生与医院的关联关系
     * @param doctorHospitalRelationVo 关联表主键
     */
    void deleteDoctorHospitalRelation(DoctorHospitalRelationVo doctorHospitalRelationVo);

    Page<HashMap<String, Object>> findPageAllDoctorByDoctorIds(@Param("doctorIds") String[] doctorIds, @Param("hospitalId") String hospitalId, @Param("orderBy") String orderBy, Page<HashMap<String, Object>> page);
}
