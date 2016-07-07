package com.cxqm.xiaoerke.modules.umbrella.serviceimpl;

import com.cxqm.xiaoerke.modules.sys.dao.PatientDao;
import com.cxqm.xiaoerke.modules.sys.dao.UserDao;
import com.cxqm.xiaoerke.modules.sys.dao.UtilDao;
import com.cxqm.xiaoerke.modules.sys.entity.Office;
import com.cxqm.xiaoerke.modules.sys.entity.PatientVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.entity.ValidateBean;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.umbrella.dao.BabyUmbrellaInfoDao;
import com.cxqm.xiaoerke.modules.umbrella.service.BabyUmbrellaInfoThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by guozengguang on 16/7/6.
 */

@Service
@Transactional(readOnly = false)
public class BabyUmbrellaInfoThirdPartyServiceImpl implements BabyUmbrellaInfoThirdPartyService {

    @Autowired private BabyUmbrellaInfoDao babyUmbrellaInfoDao;

    @Autowired private UtilDao utilDao;

    @Autowired private UserDao userDao;

    @Autowired private PatientDao patientDao;

    /**
     * 根据手机号查询该用户是否购买宝护伞
     */
    public boolean ifBuyUmbrella(Map<String, Object> map) {
        List<Map<String, Object>> list = babyUmbrellaInfoDao.getifBuyUmbrellaInfo(map);
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据手机号查询该用户是否已经关注平台
     */
    public Map<String, Object> getStatusByPhone(Map<String, Object> map) {
        Map<String, Object> statusMap = babyUmbrellaInfoDao.getStatusByPhone(map);
        return statusMap;
    }

    /**
     * 根据手机号查询验证码
     */
    public ValidateBean getIdentifying(String phoneNum) {
        return utilDao.getIdentifying(phoneNum);
    }

    /**
     * 根据手机号创建用户信息
     */
    public User saveUserInfo(String phone) {

        PatientVo patientVoResult = new PatientVo();
        User userSearch = new User();
        userSearch.setLoginName(phone);
        Map user = userDao.getUserByLoginName(userSearch);
        //用户不存在，创建新用户
        User userNew = new User();
        if (user == null) {
            String sys_user_id = UUID.randomUUID().toString().replaceAll("-", "");
            userNew.setId(sys_user_id);
            userNew.setLoginName(phone);
            userNew.setCreateDate(new Date());
            userNew.setPhone(phone);
            userNew.setCompany(new Office("1"));
            userNew.setOffice(new Office("3"));
            Random r = new Random();
            userNew.setPassword(SystemService.entryptPassword("ILoveXiaoErKe"));
            userNew.setUserType(User.USER_TYPE_USER);
            int result = userDao.insert(userNew);
            if (result == 1) {
                PatientVo patientVo = new PatientVo();
                String sys_patient_id = UUID.randomUUID().toString().replaceAll("-", "");
                patientVo.setId(sys_patient_id);
                patientVo.setSysUserId(sys_user_id);
                patientVo.setStatus("0");
                patientDao.insert(patientVo);
            }
            //patient创建成功后，根据user的login_name来重新获取patientId
            user = userDao.getUserByLoginName(userSearch);
        } else {
            String sys_user_id = (String) user.get("id");
            userNew.setId(sys_user_id);
            userNew.setLoginName(phone);
            userNew.setPhone(phone);
            userNew.setCompany(new Office((String) user.get("company_id")));
            userNew.setOffice(new Office((String) user.get("office_id")));
            userNew.setPassword((String) user.get("password"));
            userNew.setName((String) user.get("name"));
            userNew.setUserType((String) user.get("user_type"));
            userNew.setLoginFlag((String) user.get("login_flag"));
            userNew.setCreateDate((Date) user.get("create_date"));
            userNew.setDelFlag((String) user.get("del_flag"));
            userDao.update(userNew);
        }
        HashMap<String, Object> idMap = new HashMap<String, Object>();
        idMap.put("sysUserId", user.get("id"));
        Map userInfo = null;
        userInfo = patientDao.getPatientIdByUserIdExecute(idMap);
        if (userInfo != null) {
            patientVoResult.setId((String) userInfo.get("id"));
            patientVoResult.setSysUserId((String) user.get("id"));
            patientVoResult.setStatus("1");
            patientDao.update(patientVoResult);
        }
        return userNew;
    }
}