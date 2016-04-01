/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.dao;

import com.cxqm.xiaoerke.common.persistence.CrudDao;
import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.sys.entity.PatientVo;
import com.cxqm.xiaoerke.modules.sys.entity.AccountVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 审批DAO接口
 *
 * @author chenjiake
 * @version 2014-05-16
 */
@MyBatisDao
public interface PatientDao extends CrudDao<PatientVo> {

    //根据userId去获取patientId @author 02_zdl
    Map getPatientIdByUserIdExecute(HashMap sysUserId);

    //获取个人信息  @author 11_zdl
    HashMap<String, Object> findPersonInfoExecute(HashMap<String, Object> hashMap);
	
    String getUserIdByPatient(String patientRestId);
    
}
