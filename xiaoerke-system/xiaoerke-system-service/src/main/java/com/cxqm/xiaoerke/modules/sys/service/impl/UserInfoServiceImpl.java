package com.cxqm.xiaoerke.modules.sys.service.impl;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.CoopConsultUtil;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.consult.entity.CoopThirdBabyInfoVo;
import com.cxqm.xiaoerke.modules.consult.service.impl.CoopThirdBabyInfoServiceImpl;
import com.cxqm.xiaoerke.modules.sys.dao.IllnessDao;
import com.cxqm.xiaoerke.modules.sys.dao.PatientDao;
import com.cxqm.xiaoerke.modules.sys.dao.UserDao;
import com.cxqm.xiaoerke.modules.sys.entity.Office;
import com.cxqm.xiaoerke.modules.sys.entity.PatientVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.MessageService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.service.UserInfoService;

import net.sf.json.JSONObject;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	private UserDao userDao;

    @Autowired
    private CoopThirdBabyInfoServiceImpl coopThirdBabyInfoService = SpringContextHolder.getBean("coopThirdBabyInfoServiceImpl");

    private static ExecutorService threadExecutor = Executors.newSingleThreadExecutor();

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

    @Override
    public String createOrUpdateThirdPartPatientInfo(HashMap params) {

        User userNew = new User();
        String response = "";
        String source = (String)params.get("source");
        if(StringUtils.isNotNull(source) && "wjy".equalsIgnoreCase(source)){
            userNew.setLoginName((String)params.get("userPhone"));
            if (userdao.getUserByLoginName(new User(null,(String)params.get("userPhone"))) == null) {
                String sys_user_id = UUID.randomUUID().toString().replaceAll("-", "");
                userNew.setId(sys_user_id);
                userNew.setLoginName((String) params.get("userPhone"));
                userNew.setCreateDate(new Date());
                userNew.setPhone((String) params.get("userPhone"));
                userNew.setCompany(new Office("1"));
                userNew.setOffice(new Office("3"));
                userNew.setPassword(SystemService.entryptPassword("ILoveXiaoErKe"));
                userNew.setUserType(User.USER_TYPE_USER);
                userNew.setMarketer(source);
                userNew.setName((String) params.get("userName"));
                userNew.setOpenid((String) params.get("thirdId"));
                int result = userDao.insert(userNew);
                if (result == 1) {
                    params.put("sys_user_id",sys_user_id);
                    params.put("remoteUrl","http://rest.ihiss.com:9000/user/children");
                    Runnable thread = new saveCoopThirdBabyInfoThread(params);
                    threadExecutor.execute(thread);
                }
                response = sys_user_id ;
            }else{
                response = (String) userdao.getUserByLoginName(new User(null, (String)params.get("userPhone"))).get("id");
            }
        }else{
            userNew.setLoginName((String)params.get("userPhone"));
            if (userdao.getUserByLoginName(new User(null,(String)params.get("userPhone"))) == null) {
                String sys_user_id = UUID.randomUUID().toString().replaceAll("-", "");
                userNew.setId(sys_user_id);
                userNew.setLoginName((String) params.get("userPhone"));
                userNew.setCreateDate(new Date());
                userNew.setPhone((String) params.get("userPhone"));
                userNew.setCompany(new Office("1"));
                userNew.setOffice(new Office("3"));
                userNew.setPassword(SystemService.entryptPassword("ILoveXiaoErKe"));
                userNew.setUserType(User.USER_TYPE_USER);
                userNew.setMarketer(source);
                userNew.setName((String) params.get("userName"));
                int result = userDao.insert(userNew);
                if (result == 1) {
                    PatientVo patientVo = new PatientVo();
                    String sys_patient_id = UUID.randomUUID().toString().replaceAll("-", "");
                    patientVo.setId(sys_patient_id);
                    patientVo.setSysUserId(sys_user_id);
                    patientVo.setStatus("0");
                    patientVo.setGender((String)params.get("userSex"));
                    patientDao.insert(patientVo);
                }
                response = sys_user_id ;
            }else{
                response = (String) userdao.getUserByLoginName(new User(null, (String)params.get("userPhone"))).get("id");
            }
        }
        return response ;
    }

    public class saveCoopThirdBabyInfoThread implements Runnable {
        private HashMap<String, Object> params ;
        public saveCoopThirdBabyInfoThread(HashMap<String, Object> params) {
            this.params = params;
        }
        @Override
        public void run() {
            String childrenUrl = (String)params.get("remoteUrl"); //获取当前登录人的孩子信息
            String token = (String)params.get("token");
            String access_token = "{'X-Access-Token':'" + token + "'}";
            String method = "GET";
            String dataType = "json";
            String babyInfo = CoopConsultUtil.getCurrentUserInfo(childrenUrl, method, dataType, access_token, "", 2);
            JSONObject jsonObject = null ;
            if(StringUtils.isNotNull(babyInfo)){
                JSONArray jsonArray = new JSONArray(babyInfo);
                for(int i= 0; i<jsonArray.length();i++){
                    jsonObject = JSONObject.fromObject(jsonArray.get(i).toString());
                    CoopThirdBabyInfoVo coopThirdBabyInfoVo = new CoopThirdBabyInfoVo();
                    coopThirdBabyInfoVo.setCreateDate(new Date());
                    coopThirdBabyInfoVo.setDelFlag("0");
                    coopThirdBabyInfoVo.setSource((String)params.get("source"));
                    coopThirdBabyInfoVo.setSysUserId((String)params.get("sys_user_id"));
                    try {
                        coopThirdBabyInfoVo.setBirthday(new SimpleDateFormat("yyyy-mm-DD hh:MM:ss").parse((String) jsonObject.get("birthday")));
                        coopThirdBabyInfoVo.setGender((String) jsonObject.get("sex"));
                        coopThirdBabyInfoVo.setName((String) jsonObject.get("name"));
                        coopThirdBabyInfoVo.setStatus((String) jsonObject.get("id"));
                        coopThirdBabyInfoService.addCoopThirdBabyInfo(coopThirdBabyInfoVo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            PatientVo patientVo = new PatientVo();
            String sys_patient_id = UUID.randomUUID().toString().replaceAll("-", "");
            patientVo.setId(sys_patient_id);
            patientVo.setSysUserId((String)params.get("sys_user_id"));
            patientVo.setStatus("0");
            patientVo.setGender((String)params.get("userSex"));
            patientDao.insert(patientVo);
        }
    }
}
