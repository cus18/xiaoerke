package com.cxqm.xiaoerke.modules.sys.service.impl;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.sys.dao.IllnessDao;
import com.cxqm.xiaoerke.modules.sys.dao.PatientDao;
import com.cxqm.xiaoerke.modules.sys.dao.UserDao;
import com.cxqm.xiaoerke.modules.sys.entity.Office;
import com.cxqm.xiaoerke.modules.sys.entity.PatientVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.MessageService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.service.UserInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    //分页查询用户列表sunxiao
    @Override
    public Page<User> findUserList(Page<User> page,User user){
        if(StringUtils.isNull(user.getUserType())){
            user.setUserType("both");//分诊小宝，电话咨询医生
        }
        return userdao.findUserList(page,user);
    }

    //添加修改用户信息
    @Override
    public void doctorOper(User user) throws Exception{
        HashMap param = new HashMap();
        param.put("phone",user.getPhone());
        List<User> list = userdao.getUserListByInfo(param);
        if(list.size()==0){
            if(StringUtils.isNull(user.getId())){
                String sys_user_id = UUID.randomUUID().toString().replaceAll("-", "");
                user.setId(sys_user_id);
                user.setCreateDate(new Date());
                user.setLoginName(user.getPhone());
                user.setCompany(new Office("1"));
                user.setOffice(new Office("3"));
                user.setPassword(SystemService.entryptPassword("ILoveXiaoErKe"));
                userdao.insert(user);
            }else{
                user.setUpdateDate(new Date());
                userdao.updateUserElementsExecute(user);
            }
        }else{
            user.setId(list.get(0).getId());
            user.setUpdateDate(new Date());
            userdao.updateUserElementsExecute(user);
        }
    }

    @Override
    public List<User> getUserListByInfo(User user) {
        List<User> list = new ArrayList<User>();
        if(StringUtils.isNull(user.getId())){
            list.add(user);
        }else{
            HashMap map = new HashMap();
            map.put("id",user.getId());
            list = userdao.getUserListByInfo(map);
        }
        return list;
    }
}
