/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorCaseVo;

import java.util.HashMap;
import java.util.List;

/**
 * 审批DAO接口
 *
 * @author deliang
 * @version 2014-10-16
 */
@MyBatisDao
public interface DoctorCaseDao {

    //保存案例信息 @author zdl
    void saveDoctorCaseInfo(HashMap<String, Object> executeMap) ;

    //获取医生案例信息
    List<DoctorCaseVo> findDoctorCase(HashMap<String, Object> executeMap);

    //获取医生案例总数
    Integer findDoctorCaseNumber(HashMap<String, Object> executeMap);

    //更改案例数
    void updateDoctorCaseInfo(HashMap<String, Object> executeMap);

    //删除案例
    void deleteDoctorCaseById(HashMap<String, Object> executeMap);

    //更新案例信息
    void updateDoctorCase(DoctorCaseVo doctorCaseVo);

    //导医生案例数据
    void saveDoctorCase(DoctorCaseVo doctorCaseVo);

    int findDoctorCaseNumberByName();

}
