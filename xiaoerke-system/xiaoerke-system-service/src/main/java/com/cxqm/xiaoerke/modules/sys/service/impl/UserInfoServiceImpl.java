package com.cxqm.xiaoerke.modules.sys.service.impl;

import com.cxqm.xiaoerke.modules.sys.dao.IllnessDao;
import com.cxqm.xiaoerke.modules.sys.dao.PatientDao;
import com.cxqm.xiaoerke.modules.sys.dao.UserDao;
import com.cxqm.xiaoerke.modules.sys.entity.PatientVo;
import com.cxqm.xiaoerke.modules.sys.service.MessageService;
import com.cxqm.xiaoerke.modules.sys.service.UserInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = false)
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private IllnessDao illnessDao;

	@Autowired
	private PatientDao patientDao;

	@Autowired
	private UserDao userdao;
	
	@Autowired
	private MessageService messageService;

    //根据用户id获取当前用户的所有信息
    public HashMap<String, Object> findPersonDetailInfoByUserId(String userId) {
        HashMap<String,Object> hashMap=new HashMap<String, Object>();
        hashMap.put("userId",userId);
        HashMap<String, Object> resultmap = userdao.findUserDetailInfoByUserIdExecute(hashMap);
        return resultmap;
    }
    
    /**
     * 根据userId去获取patientId @author 02_zdl
     */
    @Override
    public String getPatientIdByUserId(String sysUserId) {
        HashMap<String, Object> HashMap = new HashMap<String, Object>();
        HashMap.put("sysUserId", sysUserId);
        Map expertiseInfo = patientDao.getPatientIdByUserIdExecute(HashMap);
        String patientId = (String) expertiseInfo.get("id");
        if (patientId != null && !("".equals(patientId))) {
            return patientId;
        }
        return null;
    }

    //获取个人信息 
    @Override
    public HashMap<String, Object> findPersonInfoExecute(HashMap<String, Object> hashMap) {
    	return patientDao.findPersonInfoExecute(hashMap);
    }
    
    @Override
    public String getUserIdByPatient(String patientRestId){
    	return patientDao.getUserIdByPatient(patientRestId);
    }
    
    @Override
    public int savePatient(PatientVo vo){
    	return patientDao.insert(vo);
    }

}
