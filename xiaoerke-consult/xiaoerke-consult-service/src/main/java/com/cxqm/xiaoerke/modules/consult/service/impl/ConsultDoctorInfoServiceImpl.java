package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultDoctorInfoDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorInfoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultDoctorInfoService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 咨询医生信息实现类
 * @author sunxiao
 * 2016-04-26
 */

@Service
public class ConsultDoctorInfoServiceImpl implements ConsultDoctorInfoService {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ConsultDoctorInfoDao consultDoctorInfoDao;

    @Autowired
    private PatientRegisterPraiseService patientRegisterPraiseService;

    @Autowired
    private ConsultSessionService consultSessionService;

    @Override
    public int saveConsultDoctorInfo(ConsultDoctorInfoVo vo) {
        return 0;
    }

    @Override
    public Map getConsultDoctorInfo(User user) {
        Map map = new HashMap();
        List<User> list = userInfoService.getUserListByInfo(user);
        Map param = new HashMap();
        param.put("userId",user.getId());
        List<ConsultDoctorInfoVo> doctorList = getConsultDoctorByInfo(param);
        map.put("user",list.get(0));
        map.put("doctor",doctorList.get(0));
        Map praiseParam = new HashMap();
        praiseParam.put("doctorId",user.getId());
        List<Map<String,Object>> praiseList = patientRegisterPraiseService.getCustomerEvaluationListByInfo(praiseParam);
        float redPacket = 0;
        int satisfy = 0;
        int verysatisfy = 0;
        int unsatisfy = 0;
        int redPacketPerson = 0;
        for(Map<String,Object> temp : praiseList){
            if(StringUtils.isNotNull((String) temp.get("serviceAttitude"))){
                if("1".equals((String) temp.get("serviceAttitude"))){
                    unsatisfy += 1;
                }else if("3".equals((String) temp.get("serviceAttitude"))){
                    satisfy += 1;
                }else if("5".equals((String) temp.get("serviceAttitude"))){
                    verysatisfy += 1;
                }
            }

            if(StringUtils.isNotNull((String) temp.get("redPacket"))&&temp.get("payStatus")!=null){
                redPacket += Float.parseFloat((String)temp.get("redPacket"));
                redPacketPerson++;
            }
        }
        map.put("redPacket",redPacket);
        map.put("satisfy",satisfy);
        map.put("verysatisfy",verysatisfy);
        map.put("unsatisfy", unsatisfy);
        map.put("redPacketPerson", redPacketPerson);
        Integer sessionCount = consultSessionService.getConsultSessionUserCount(praiseParam);
        map.put("sessionCount", sessionCount);
        List<ConsultSession> sessionList = consultSessionService.getConsultSessionListByInfo(praiseParam);
        Map<String,Integer> sessionMap = new LinkedHashMap<String, Integer>();
        for(ConsultSession temp : sessionList){
            sessionMap.put(DateUtils.DateToStr(temp.getCreateTime(),"date"),sessionMap.get(DateUtils.DateToStr(temp.getCreateTime(),"date"))==null?1:sessionMap.get(DateUtils.DateToStr(temp.getCreateTime(),"date"))+1);
        }
        map.put("sessionMap",sessionMap);
        return map;
    }

    @Override
    public List<ConsultDoctorInfoVo> getConsultDoctorByInfo(Map map) {
        List<ConsultDoctorInfoVo> list = new ArrayList<ConsultDoctorInfoVo>();
        List<ConsultDoctorInfoVo> doctorList = consultDoctorInfoDao.getConsultDoctorByInfo(map);
        if(doctorList.size()==0){
            list.add(new ConsultDoctorInfoVo());
        }else{
            list.add(doctorList.get(0));
        }
        return list;
    }

    @Override
    public int consultDoctorInfoOper(ConsultDoctorInfoVo vo) {
        Map param = new HashMap();
        int count = 0;
        param.put("userId", vo.getUserId());
        List<ConsultDoctorInfoVo> doctorList = consultDoctorInfoDao.getConsultDoctorByInfo(param);
        if(doctorList.size()==0){
            count = consultDoctorInfoDao.insertSelective(vo);
        }else{
            vo.setId(doctorList.get(0).getId());
            count = consultDoctorInfoDao.updateByPrimaryKeySelective(vo);
        }
        return count;
    }

    /**
     * 获取咨询医生所有科室
     * @author jiangzg
     * 2016-5-17
     */
    @Override
    public List<String> getConsultDoctorDepartment() {
        List<String> departmentList= consultDoctorInfoDao.getConsultDoctorDepartment();
        if(departmentList != null && departmentList.size() > 0){
            return departmentList;
        }else{
            return null;
        }
    }

    @Override
    public ConsultDoctorInfoVo getConsultDoctorInfoByUserId(String userId) {
        ConsultDoctorInfoVo consultDoctorInfoVo = consultDoctorInfoDao.getConsultDoctorInfoByUserId(userId);
        if(consultDoctorInfoVo !=null){
            return consultDoctorInfoVo;
        }
        return null;
    }

    @Override
    public List<User> findUserOrderByDepartment(User user) {
        List<User>	users = consultDoctorInfoDao.findUserOrderByDepartment(user);
        return users;
    }

    @Override
    public List<User> findUserByUserName(User user) {
        List<User>	users = consultDoctorInfoDao.findUserByUserName(user);
        return users;
    }

    @Override
    public List<Map> getDoctorInfoMoreByUserId(String userId) {
        List<Map> result = null;
        if(StringUtils.isNotNull(userId)){
            result = consultDoctorInfoDao.getDoctorInfoMoreByUserId(userId);
        }
        return result;
    }


    @Override
    public List<ConsultDoctorInfoVo> findManagerDoctorInfoBySelective(ConsultDoctorInfoVo consultDoctorInfoVo) {
        List<ConsultDoctorInfoVo> result = consultDoctorInfoDao.findManagerDoctorInfoBySelective(consultDoctorInfoVo);
        return result;
    }
}
