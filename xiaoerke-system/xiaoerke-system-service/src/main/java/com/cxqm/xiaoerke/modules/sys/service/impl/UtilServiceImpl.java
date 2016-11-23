package com.cxqm.xiaoerke.modules.sys.service.impl;


import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.account.entity.AccountInfo;
import com.cxqm.xiaoerke.modules.account.service.AccountService;
import com.cxqm.xiaoerke.modules.sys.dao.MessageDao;
import com.cxqm.xiaoerke.modules.sys.dao.PatientDao;
import com.cxqm.xiaoerke.modules.sys.dao.UserDao;
import com.cxqm.xiaoerke.modules.sys.dao.UtilDao;
import com.cxqm.xiaoerke.modules.sys.entity.*;
import com.cxqm.xiaoerke.modules.sys.service.BabyBaseInfoService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.service.UtilService;
import com.cxqm.xiaoerke.modules.sys.utils.ChangzhuoMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = false)
public class UtilServiceImpl implements UtilService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private UtilDao utilDao;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BabyBaseInfoService babyBaseInfoService;


    public void init() {
        //在这里加载缓存..
        utilDao.getIdentifying("18510525441");
    }

    @Override
    public int updateValidateCode(ValidateBean validateBean) {
        return utilDao.updateValidateCode(validateBean);
    }

    /**
     * 短信验证码
     *
     * @param num 用户电话号码
     * @return 生成的随机验证码
     * status为1表示获取验证码成功，为0表示获取验证码失败
     */
    @Override
    public Map<String, Object> sendIdentifying(String num, SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo) {
        System.out.print("sendIdentifying()...." + new Date());
        HashMap<String, Object> response = new HashMap<String, Object>();
        if (num != null) {
            ValidateBean validateBean = utilDao.getIdentifying(num);
            if (null != validateBean) {
                Date date = validateBean.getCreateTime();
                boolean flag = (date.getTime() + 1000 * 60) > new Date().getTime();
                if (flag) {
                    response.put("status", "0");
                    return response;
                }
            }
            String identify = ChangzhuoMessageUtil.sendIdentifying(num);
            //如果不是生产环境
            if (sysPropertyVoWithBLOBsVo.getAppId().indexOf("wx0baf90e904df0117") == -1) {
                identify = "123456";
            }
            ValidateBean bean = new ValidateBean();
            bean.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            bean.setUserPhone(num);
            bean.setCreateTime(new Date());
            bean.setCode(identify);
            bean.setStatus("1");
            int status = utilDao.saveOrUpdateIdentify(bean);
            response.put("code", identify);
            response.put("status", status);
        } else {
            response.put("status", "0");
        }

        return response;
    }

    @Override
    public String bindUser(String num, String code, String openid) {
        System.out.print("openid---------------------------------------------" + openid);
        String response = null;
        //根据用户的手机号，来判断短信下发的code是否还有效
        ValidateBean validateBean = utilDao.getIdentifying(num);
        if (null == validateBean) {
            return "0";
        }
        Date date = validateBean.getCreateTime();
        boolean flag = (date.getTime() + 1000 * 60) > new Date().getTime();
        if (validateBean != null && flag && code.equals(validateBean.getCode())) {
            //code有效，根据用户的手机号（切记，目前手机号，都是用户user表中的login_name）
            System.out.println(code + "|||||||||||||" + validateBean.getCode());
            PatientVo patientVo = CreateUser(num, openid, "patient");//zdl 抽取
            if (patientVo.getId() == null) {
                response = "0";
            } else {
                response = "1";//验证码有效，且完成账户绑定
            }
        } else {
            response = "0";//验证码无效，绑定没有完成
        }
        return response;
    }

    @Override
    public String bindUser4Doctor(String mobile, String verifyCode, String openId) {
        String response = null;
        //根据用户的手机号，来判断短信下发的code是否还有效
        ValidateBean validateBean = utilDao.getIdentifying(mobile);
        Date date = validateBean.getCreateTime();
        boolean flag = (date.getTime() + 1000 * 300) > new Date().getTime();
        if (validateBean != null && flag && verifyCode.equals(validateBean.getCode())) {
            //code有效，根据用户的手机号（切记，目前手机号，都是用户user表中的login_name）
            User userSearch = new User();
            userSearch.setLoginName(mobile);
            Map user = userDao.getUserByLoginName(userSearch);

            if (user != null) {
                String userType = (String) user.get("user_type");
                if (User.USER_TYPE_DOCTOR.equalsIgnoreCase(userType)) {
                    CreateUser(mobile, openId, "doctor");
                    response = "1";//验证码有效，且完成账户绑定
                } else
                    response = "2";//医生未认证
            } else {
                response = "2";//医生未认证
            }
        } else {
            response = "0";//验证码无效，绑定没有完成
        }

        return response;
    }

    @Override
    public String bindUser4ConsultDoctor(String mobile, String verifyCode, String openId) {
        String response = null;
        //根据用户的手机号，来判断短信下发的code是否还有效
        ValidateBean validateBean = utilDao.getIdentifying(mobile);
        Date date = validateBean.getCreateTime();
        boolean flag = (date.getTime() + 1000 * 300) > new Date().getTime();
        if (validateBean != null && flag && verifyCode.equals(validateBean.getCode())) {
            //code有效，根据用户的手机号（切记，目前手机号，都是用户user表中的login_name）
            User userSearch = new User();
            userSearch.setLoginName(mobile);
            Map user = userDao.getUserByLoginName(userSearch);

            if (user != null) {
                String userType = (String) user.get("user_type");
                if (User.USER_TYPE_DISTRIBUTOR.equalsIgnoreCase(userType)) {
                    CreateUser(mobile, openId, "distributor");
                    response = "1";//验证码有效，且完成账户绑定
                } else if (User.USER_TYPE_CONSULTDOCTOR.equalsIgnoreCase(userType)) {
                    CreateUser(mobile, openId, "consultDoctor");
                    response = "1";//验证码有效，且完成账户绑定
                } else
                    response = "2";//客服未认证
            } else {
                response = "2";//客服未认证
            }
        } else {
            response = "0";//验证码无效，绑定没有完成
        }

        return response;
    }

    /**
     * 创建用户
     *
     * @param num 手机号
     */
    @Override
    public PatientVo CreateUser(String num, String openid, String type) {

        PatientVo patientVoResult = new PatientVo();
        if (type.equals("doctor")) {
            User userSearch = new User();
            userSearch.setLoginName(num);
            Map user = userDao.getUserByLoginName(userSearch);
            User userNew = new User();
            String sys_user_id = (String) user.get("id");
            userNew.setId(sys_user_id);
            userNew.setLoginName(num);
            userNew.setPhone(num);
            userNew.setOpenid(openid);
            userNew.setCompany(new Office((String) user.get("company_id")));
            userNew.setOffice(new Office((String) user.get("office_id")));
            userNew.setPassword((String) user.get("password"));
            userNew.setName((String) user.get("name"));
            userNew.setUserType((String) user.get("user_type"));
            userNew.setLoginFlag((String) user.get("login_flag"));
            userNew.setCreateDate((Date) user.get("create_date"));
            userNew.setDelFlag((String) user.get("del_flag"));
            userDao.update(userNew);

            //创建账户
            AccountInfo accountInfo = accountService.findAccountInfoByUserId(sys_user_id);
            if (accountInfo == null) {
                accountInfo = new AccountInfo();
                accountInfo.setId(IdGen.uuid());
                accountInfo.setUserId(sys_user_id);
                accountInfo.setOpenId(openid);
                accountInfo.setBalance(Float.parseFloat("0"));
                accountInfo.setCreatedBy(sys_user_id);
                accountInfo.setCreateTime(new Date());
                accountInfo.setStatus("normal");
                accountInfo.setType("1");
                accountInfo.setUpdatedTime(new Date());
                accountService.saveOrUpdateAccountInfo(accountInfo);
            }
        } else if (type.equals("distributor") || type.equals("consultDoctor")) {
            User userSearch = new User();
            userSearch.setLoginName(num);
            Map user = userDao.getUserByLoginName(userSearch);
            User userNew = new User();
            String sys_user_id = (String) user.get("id");
            userNew.setId(sys_user_id);
            userNew.setLoginName(num);
            userNew.setPhone(num);
            userNew.setOpenid(openid);
            userNew.setEmail((String) user.get("email"));
            userNew.setCompany(new Office((String) user.get("company_id")));
            userNew.setOffice(new Office((String) user.get("office_id")));
            userNew.setPassword((String) user.get("password"));
            userNew.setName((String) user.get("name"));
            userNew.setUserType((String) user.get("user_type"));
            userNew.setLoginFlag((String) user.get("login_flag"));
            userNew.setCreateDate((Date) user.get("create_date"));
            userNew.setDelFlag((String) user.get("del_flag"));
            userDao.update(userNew);

            //更新账户
            AccountInfo accountInfo = accountService.findAccountInfoByUserId(sys_user_id);
            if (accountInfo == null) {
                accountInfo = new AccountInfo();
                accountInfo.setId(IdGen.uuid());
                accountInfo.setUserId(sys_user_id);
                accountInfo.setOpenId(openid);
                accountInfo.setBalance(Float.parseFloat("0"));
                accountInfo.setCreatedBy(sys_user_id);
                accountInfo.setCreateTime(new Date());
                accountInfo.setStatus("normal");
                accountInfo.setType("1");
                accountInfo.setUpdatedTime(new Date());
                accountService.saveOrUpdateAccountInfo(accountInfo);
            }

        } else if (type.equals("patient")) {
            User userSearch = new User();
            userSearch.setLoginName(num);
            Map user = userDao.getUserByLoginName(userSearch);
            //用户不存在，创建新用户
            if (user == null) {
                User userNew = new User();
                String sys_user_id = UUID.randomUUID().toString().replaceAll("-", "");
                userNew.setId(sys_user_id);
                userNew.setLoginName(num);
                userNew.setCreateDate(new Date());
                userNew.setPhone(num);
                userNew.setCompany(new Office("1"));
                userNew.setOffice(new Office("3"));
                userNew.setOpenid(openid);
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

                //绑定后同步宝宝信息到数据库
                if (StringUtils.isNotNull(openid)) {
                    BabyBaseInfoVo vo = new BabyBaseInfoVo();
                    vo.setUserid(sys_user_id);
                    vo.setState("0");
                    vo.setOpenid(openid);
                    babyBaseInfoService.updateUserId(vo);
                }
                //patient创建成功后，根据user的login_name来重新获取patientId
                user = userDao.getUserByLoginName(userSearch);
            } else {
                User userNew = new User();
                String sys_user_id = (String) user.get("id");
                userNew.setId(sys_user_id);
                userNew.setLoginName(num);
                userNew.setPhone(num);
                if (StringUtils.isNotNull(openid)) {
                    userNew.setOpenid(openid);
                } else {
                    userNew.setOpenid((String) user.get("openid"));
                }
                userNew.setCompany(new Office((String) user.get("company_id")));
                userNew.setOffice(new Office((String) user.get("office_id")));
                userNew.setPassword((String) user.get("password"));
                userNew.setName((String) user.get("name"));
                userNew.setUserType((String) user.get("user_type"));
                userNew.setLoginFlag((String) user.get("login_flag"));
                userNew.setCreateDate((Date) user.get("create_date"));
                userNew.setDelFlag((String) user.get("del_flag"));
                userDao.update(userNew);

                //绑定后同步宝宝信息到数据库
                BabyBaseInfoVo vo = new BabyBaseInfoVo();
                vo.setUserid(sys_user_id);
                vo.setState("0");
                vo.setOpenid(StringUtils.isNotNull(openid) ? openid : (String) user.get("openid"));
                babyBaseInfoService.updateUserId(vo);
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
        }
        return patientVoResult;
    }

    private void sendMsg2Doctor(String sysRegisterId, String content) {
        HashMap<String, Object> resultMap = messageDao.getCancelAppointmentDocNum(sysRegisterId);
        String num = (String) resultMap.get("phone");
        if (null != num) {
            ChangzhuoMessageUtil.sendMsg(num, content);
        }
    }

    /**
     * 非微信(第三方平台购买宝护伞后通过用户专属临时二维码扫码关注后的用户绑定)
     *
     * @param num    用户手机号
     * @param openid 用户openid
     * @return
     * @author guozengguang
     */
    @Override
    public PatientVo bindUserForThirdParty(String num, String openid) {
        System.out.print("openid---------------------------------------------" + openid);
        String response = null;
        PatientVo patientVo = CreateUser(num, openid, "patient");//zdl 抽取
        return patientVo;
    }


}
