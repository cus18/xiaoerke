package com.cxqm.xiaoerke.modules.sys.dao;

import java.util.HashMap;
import java.util.List;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorLocationRef;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorLocationVo;

@MyBatisDao
public interface DoctorLocationDao {
    int deleteByPrimaryKey(String id);

    int insert(DoctorLocationRef record);

    int insertSelective(DoctorLocationRef record);

    DoctorLocationRef selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DoctorLocationRef record);

    int updateByPrimaryKey(DoctorLocationRef record);
    
    //往sys_doctor_location表里插入增加数据
    void insertSysDoctorLocation(HashMap<String,Object> hashMap);

    //查询当前地址在sys_doctor_location表中是否存在
    HashMap<String, Object> findDoctorExistLocation(HashMap<String, Object> hashMap);
    
    List<HashMap<String, Object>> findDoctorLocationByDoctorId(HashMap<String,Object> hashMap);
    
    void updateWaiteTime(HashMap<String, Object> updateMap);
    
    HashMap<String, Object> findPatientLocationId(HashMap<String, Object> hashMap);
    
    void deleteDoctorLocation(DoctorLocationVo doLocationVo);
    
    List getDoctorLocationInfo(HashMap<String,Object> map);
    
    void insertDoctorLocation(DoctorLocationVo doLocationVo);
    
    void updateDoctorLocation(DoctorLocationVo doctorLocationVo);
}