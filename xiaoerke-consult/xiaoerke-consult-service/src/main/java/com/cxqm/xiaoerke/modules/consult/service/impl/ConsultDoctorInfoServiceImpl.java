package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultDoctorInfoDao;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultDoctorTimeGiftDao;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultSessionPropertyDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorInfoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorTimeGiftVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionPropertyVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultDoctorInfoService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.sys.dao.SysPropertyDao;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.service.UserInfoService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
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

    @Autowired
    private ConsultRecordMongoDBServiceImpl consultRecordMongoDBService;

    @Autowired
    private ConsultDoctorTimeGiftDao consultDoctorTimeGiftDao;

    @Autowired
    private ConsultSessionPropertyDao consultSessionPropertyDao;

    @Autowired
    SystemService systemService;

    @Autowired
    SysPropertyDao sysPropertyDao;

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

        Map lectureMap = new HashMap();
        lectureMap.put("userId", user.getId());
        List<ConsultDoctorInfoVo> lectureList = getConsultLecture(lectureMap);
        map.put("lectureList",lectureList);
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
    public SysPropertyVoWithBLOBsVo getSysProperty() {
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyDao.selectByPrimaryKey(1);
        return sysPropertyVoWithBLOBsVo;
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

    @Override
    public Page<ConsultDoctorTimeGiftVo> findConsultDoctorOrderListByInfo(Page<ConsultDoctorTimeGiftVo> page, ConsultDoctorTimeGiftVo vo) {
        return consultDoctorTimeGiftDao.findConsultDoctorOrderListByInfo(page,vo);
    }

    @Override
    public Page<ConsultSessionPropertyVo> findConsultUserInfoListByInfo(Page<ConsultSessionPropertyVo> page, ConsultSessionPropertyVo vo) {
        return consultSessionPropertyDao.findConsultUserInfoListByInfo(page, vo);
    }

    @Override
    public void consultTimeGift(ConsultSessionPropertyVo vo) {
        consultSessionPropertyDao.consultTimeGift(vo);//修改consult_session_property表的永久次数
        ConsultDoctorTimeGiftVo giftVo = new ConsultDoctorTimeGiftVo();
        giftVo.setOpenid(vo.getSysUserId());
        giftVo.setReceiveDate(DateUtils.DateToStr(new Date(), "datetime"));
        giftVo.setNickname(vo.getNickname());
        giftVo.setFeeType("doctorConsultGift");
        giftVo.setStatus("doctorConsultGift");
        giftVo.setAmount(vo.getPermTimes());
        consultDoctorTimeGiftDao.insert(giftVo);//添加赠送记录
    }

    @Override
    public void saveLecture(ConsultDoctorInfoVo consultDoctorInfoVo) {
        consultDoctorInfoDao.saveLecture(consultDoctorInfoVo);
    }

    @Override
    public List<ConsultDoctorInfoVo> getConsultLecture(Map param) {
        return consultDoctorInfoDao.getConsultLecture(param);
    }

    @Override
    public HashMap<String, Object> getConsultDoctorHomepageInfo(String csUserId) {
        HashMap<String, Object> returnMap = new HashMap<String, Object>();
        ConsultDoctorInfoVo vo = consultDoctorInfoDao.getConsultDoctorInfoByUserId(csUserId);
        Query query = new Query().addCriteria(new Criteria().where("csUserId").regex(csUserId));
        long num = consultRecordMongoDBService.consultCount(query);
        Map<String,Object> param = new HashMap<String, Object>();
        param.put("doctorId", csUserId);
        List<Map<String,Object>> evaluationList = patientRegisterPraiseService.findDoctorEvaluationById(param);
        param.put("startRowNo",0);
        param.put("pageSize",10000);
        List<Map<String,Object>> allEvaluationList = patientRegisterPraiseService.findDoctorAllEvaluationByInfo(param);
        for(Map<String,Object> temp : evaluationList){
            Map<String, Object> wechatMap = getWechatMessage((String)temp.get("openid"));
            if(wechatMap.get("subscribe")!=null && (Integer)wechatMap.get("subscribe") == 1){
                temp.put("nickname",wechatMap.get("nickname"));
                temp.put("headimgurl",wechatMap.get("headimgurl"));
            }
        }
        Integer sing = Integer.parseInt(patientRegisterPraiseService.getCustomerStarSingById(csUserId).get("startNum").toString()) + 200;
        Integer count = Integer.parseInt(patientRegisterPraiseService.getCustomerStarCountById(csUserId).get("startNum").toString()) + 200;
        float rate = (float) sing / count;
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        returnMap.put("rate",df.format(rate));
        returnMap.put("practitionerCertificateNo",vo.getPractitionerCertificateNo());
        returnMap.put("doctorName",vo.getName());
        returnMap.put("gender",vo.getGender());
        returnMap.put("title",vo.getTitle());
        returnMap.put("department",vo.getDepartment());
        returnMap.put("description",vo.getDescription() + " " + vo.getSkill());
        returnMap.put("evaluationList",evaluationList);
        returnMap.put("allEvaluationNum",allEvaluationList.size());
        returnMap.put("personNum",num);
        return returnMap;
    }

    @Override
    public HashMap<String, Object> findDoctorAllEvaluation(Map<String, Object> param) {
        HashMap<String, Object> returnMap = new HashMap<String, Object>();
        List<Map<String,Object>> allEvaluationList = patientRegisterPraiseService.findDoctorAllEvaluationByInfo(param);
        for(Map<String,Object> temp : allEvaluationList){
            Map<String, Object> wechatMap = getWechatMessage((String)temp.get("openid"));
            if(wechatMap.get("subscribe")!=null && (Integer)wechatMap.get("subscribe") == 1){
                temp.put("nickname",wechatMap.get("nickname"));
                temp.put("headimgurl",wechatMap.get("headimgurl"));
            }
        }
        returnMap.put("allEvaluationList",allEvaluationList);
        return returnMap;
    }

    private Map<String, Object> getWechatMessage(String openId){
        Map<String,Object> parameter = systemService.getWechatParameter();
        String token = (String)parameter.get("token");

        String strURL="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+token+"&openid="+openId+"&lang=zh_CN";
        String param="";
        String json= WechatUtil.post(strURL, param, "GET");
        JSONObject jasonObject = JSONObject.fromObject(json);
        Map<String, Object> jsonMap = (Map) jasonObject;

        return jsonMap;
    }

}
