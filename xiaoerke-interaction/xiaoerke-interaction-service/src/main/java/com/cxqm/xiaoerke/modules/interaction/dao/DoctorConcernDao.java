package com.cxqm.xiaoerke.modules.interaction.dao;

import java.util.HashMap;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.interaction.entity.DoctorConcern;

@MyBatisDao
public interface DoctorConcernDao {

    int deleteByPrimaryKey(String id);

    int insert(DoctorConcern record);

    int insertSelective(DoctorConcern record);

    DoctorConcern selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DoctorConcern record);

    int updateByPrimaryKey(DoctorConcern record);
    
    //关注后，往sys_concern表中插入数据
    void insertSysConcernExecute(HashMap<String,Object> hashMap);

    //判断用户是否已经关注该医生
    HashMap<String, Object> JudgeUserConcernDoctor(HashMap<String, Object> hashMap);

    //获取我关注的医生信息列表
    Page<HashMap<String, Object>> findMyConcernDoctorInfo(HashMap<String, Object> doctorMap, Page<HashMap<String, Object>> page);
}