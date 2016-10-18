package com.cxqm.xiaoerke.modules.operation.service.impl;

import com.cxqm.xiaoerke.common.bean.WechatRecord;
import com.cxqm.xiaoerke.common.utils.EmojiFilter;
import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.account.service.impl.AccountServiceImpl;
import com.cxqm.xiaoerke.modules.operation.dao.DataStatisticsDao;
import com.cxqm.xiaoerke.modules.operation.dao.OperationComprehensiveDao;
import com.cxqm.xiaoerke.modules.operation.service.DataStatisticService;
import com.cxqm.xiaoerke.modules.operation.service.OperationHandleService;
import com.cxqm.xiaoerke.modules.order.dao.PatientRegisterServiceDao;
import com.cxqm.xiaoerke.modules.order.dao.RegisterServiceDao;
import com.cxqm.xiaoerke.modules.search.service.util.RdsDataSourceJDBC;
import com.cxqm.xiaoerke.modules.sys.dao.*;
import com.cxqm.xiaoerke.modules.sys.entity.*;
import com.cxqm.xiaoerke.modules.sys.service.LogMongoDBServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.wechat.dao.WechatAttentionDao;
import com.cxqm.xiaoerke.modules.wechat.dao.WechatInfoDao;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 得良 on 2015/6/17.
 */
@Service
@Transactional(readOnly = false)
public class OperationHandleServiceImpl implements OperationHandleService {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private DoctorHospitalRelationDao doctorHospitalRelationDao;

    @Autowired
    private PatientRegisterServiceDao patientRegisterServiceDao;

    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private HospitalDao hospitalDao;

    @Autowired
    private IllnessDao illnessDao;

    @Autowired
    private RegisterServiceDao registerServiceDao;

    @Autowired
    private DoctorIllnessRelationDao doctorIllnessRelationDao;

    @Autowired
    private RdsDataSourceJDBC rdsDataSourceJDBC;

    @Autowired
    private MongoDBService<MongoLog> mongoDBServiceLog;

    @Autowired
    private MongoDBService<WechatAttention> mongoDBServiceAttention;

    @Autowired
    private MongoDBService<WechatRecord> mongoDBServiceWeChatRecord;

    @Autowired
    private DataStatisticsDao dataStatisticsDao;

    @Autowired
    private AccountServiceImpl accountServiceImpl;

    @Autowired
    private DataStatisticService dataStatisticService;

    @Autowired
    private DoctorCaseDao doctorCaseDao;

    @Autowired
    private DoctorLocationDao doctorLocationDao;

    @Autowired
    private WechatInfoDao wechatInfoDao;

    @Autowired
    private WechatAttentionDao wechatAttentionDao;

    @Autowired
    private OperationComprehensiveDao OperationComprehensiveDao;

    @Autowired
    private SystemService systemService;

    @Autowired
    private LogMongoDBServiceImpl logMongoDBService;

    //获取医生的基本信息
    @Override
    public List<HashMap> findDoctorByInfo(HashMap<String, Object> hashMap) {
        return doctorDao.findDoctorByInfo(hashMap);
    }

    //导医生案例数据
    @Override
    public void saveDoctorCase(DoctorCaseVo doctorCaseVo) {
        doctorCaseDao.saveDoctorCase(doctorCaseVo);
    }

    @Override
    public Map<String, Object> getLogInfoByLogContent(Map<String, Object> params) {

        HashMap<String, Object> resultMap = new HashMap<String, Object>();

        if (null != params.get("userId") && null != params.get("logContent")) {
            String userId = (String) params.get("userId");
            String logContent = (String) params.get("logContent");

            Query query = new Query();

            try {
                query.addCriteria(new Criteria("userId").is(userId)).addCriteria(new Criteria("params").is("logContent="+logContent));
                List<MongoLog> resultList = logMongoDBService.queryList(query);
                resultMap.put("logCount", resultList.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return resultMap;
    }

    /**
     * 创建医生用户 deliang
     */
    public int CreateDoctor(HashMap<String, Object> hashMap) {
//         陈哥，跟洪磊已确认，数据采集的时候肯定会采集到医生的phone，所以在这根据医生手机号创建用户
        int result = 0;
        User userSearch = new User();
        String loginName = (String) hashMap.get("phone");
        userSearch.setLoginName(loginName);
        Map user = userDao.getUserByLoginName(userSearch);
        //用户不存在，创建新用户  ,这里暂时只考虑医生属于当前医院的情况，须扩展，医生在此医院坐诊的情况（是否坐诊分为两个excle）
        if (user == null) {
            String sys_doctor_id = UUID.randomUUID().toString().replaceAll("-", "");
            String marketer = "saomatuiguang_" + sys_doctor_id + "_attention";

            User userNew = new User();
            String sys_user_id = UUID.randomUUID().toString().replaceAll("-", "");
            userNew.setId(sys_user_id);
            userNew.setLoginName(loginName);
            userNew.setCompany(new Office("1"));
            userNew.setOffice(new Office("4"));
            userNew.setPhone(loginName);
            userNew.setCreateDate(new Date());
            userNew.setName((String) hashMap.get("doctorName"));
            userNew.setPassword(SystemService.entryptPassword("ILoveXiaoErKe"));
            userNew.setUserType(User.USER_TYPE_DOCTOR);
            userNew.setMarketer(marketer);
            result = userDao.insert(userNew);
            if (result == 1) {
                //生成医生微信二维码地址
//                String ticket = WechatUtil.createQrcode((String) hashMap.get("token"), "doc"+sys_doctor_id);
//                String extensionQrcode = WechatUtil.createQrcode((String) hashMap.get("token"), marketer);
                hashMap.put("sysDoctorId", sys_doctor_id);
                hashMap.put("sys_user_id", sys_user_id);
                String date = (String) hashMap.get("career_time");
                Date dateDate = StrToDate(date);
                hashMap.put("career_time", dateDate);
                hashMap.put("fans_number", 0);
//                hashMap.put("ticket", ticket);
//                hashMap.put("extensionQrcode", extensionQrcode);
                hashMap.put("create_date", new Date());
                doctorDao.insertDoctor(hashMap);//插入医生信息
                insertRelation(hashMap);//插入医生与医院的关联表
                //插入医生账户信息
                accountServiceImpl.createAccountInfo(sys_user_id, 0f);
                //将医生表同步到rds上，以便搜索时能搜到
//                rdsDataSourceJDBC.insertDoctorToRds(hashMap, "yes");
            }
        } else {//医生已存在，更新医生信息
            DoctorVo doctorVo = new DoctorVo();
            Map map = doctorDao.getDoctorIdByUserId(user);
            if (map != null && map.size() > 0) {//更新
                doctorVo.setId((String) map.get("id"));
                doctorVo.setSysUserId((String) user.get("id"));
                setValue(hashMap, doctorVo);
                doctorDao.update(doctorVo);
            } else {
                //此用户已经添加过了，result 为 2，代表此用户非医生是患者
                result = 2;
            }
        }
        return result;
    }

    //往sys_location表里插入增加数据
    public void insertSysDoctorLocation(HashMap<String, Object> hashMap) {
        doctorLocationDao.insertSysDoctorLocation(hashMap);
    }

    //插入号源信息
    public void insertsysRegisterService(HashMap<String, Object> hashMap) {
        registerServiceDao.insertSysRegisterServiceExecute(hashMap);
    }

    //插入号源信息(测试)
    public void insertSysRegisterServiceTest(HashMap<String, Object> hashMap) {
        registerServiceDao.insertSysRegisterServiceTest(hashMap);
    }

    //查询当前地址在sys_doctor_location表中是否存在
    public String findDoctorExistLocation(HashMap<String, Object> hashMap) {
        HashMap<String, Object> resultHashMap = doctorLocationDao.findDoctorExistLocation(hashMap);
        if (resultHashMap != null && resultHashMap.size() > 0) {
            return (String) resultHashMap.get("sysDoctorLocationId");
        }
        return null;
    }


    //根据医院名称查询hospitalId
    public String findhospitalId(String hospitalName) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("hospitalName", hospitalName);
        HashMap<String, Object> relationMap = hospitalDao.findHospitalIdByHospitalName(hashMap);
        if (relationMap != null) {
            String hospitalId = (String) relationMap.get("hospitalId");
            return hospitalId;
        }
        return null;
    }

    //根据手机号查询doctorId
    public String findDoctorIdByPhone(String phone) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("phone", phone);
        HashMap<String, Object> relationMap = doctorDao.findDoctorIdByPhoneExecute(hashMap);
        if (relationMap != null) {
            String doctorId = (String) relationMap.get("doctorId");
            return doctorId;
        }
        return null;
    }

    /**
     * 临时导数据用 根据医生姓名查询医生主键
     *
     * @param doctorName
     * @return
     */
    public String findDoctorIdByname(String doctorName) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("doctorName", doctorName);
        HashMap<String, Object> relationMap = doctorDao.findDoctorIdByname(hashMap);
        return (String) relationMap.get("id");

    }

    //根据level_2查询sys_illness主键
    public String findIllnessIdBylevel2(String level_2) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("level_2", level_2);
        HashMap<String, Object> relationMap = illnessDao.findIllnessIdByLevel2Execute(hashMap);
        String illnessId = (String) relationMap.get("illnessId");
        return illnessId;
    }


    //批量插入疾病信息
    public void insertIllness(List<HashMap<String, Object>> arrayList) {
        illnessDao.insertIllnessExecute(arrayList);
    }

    //批量插入医生与疾病关联信息表数据
    public void insertIllnessRelation(List<HashMap<String, Object>> arrayList) {
        doctorIllnessRelationDao.insertIllnessRelationExecute(arrayList);
    }

    private void insertRelation(HashMap<String, Object> hashMap) {
        //根据医院名称获取医院id
        HashMap<String, Object> relationMap = hospitalDao.findHospitalIdByHospitalName(hashMap);
        String id = IdGen.uuid();
        relationMap.put("id", id);
        relationMap.put("sys_doctor_id", hashMap.get("sysDoctorId"));
        relationMap.put("place_detail", hashMap.get("place_detail"));
        relationMap.put("relation_type", hashMap.get("relation_type"));
        relationMap.put("department_level1", hashMap.get("department_level1"));
        relationMap.put("department_level2", hashMap.get("department_level2"));
        relationMap.put("contact_person", hashMap.get("contact_person"));
        relationMap.put("contact_person_phone", hashMap.get("contact_person_phone"));
        doctorHospitalRelationDao.insertDoctorAndHospitalRelation(relationMap);//插入关系表数据
    }

    //批量插入医院信息
    public void insertHospitalList(List<HashMap<String, Object>> arrayList) {
        hospitalDao.insertHospitalListExecute(arrayList);
    }

    private void setValue(HashMap<String, Object> hashMap, DoctorVo doctorVo) {
        doctorVo.setHospital((String) hashMap.get("hospitalName"));
        doctorVo.setPosition1((String) hashMap.get("position1"));
        doctorVo.setPosition2((String) hashMap.get("position2"));
        Date date = (Date) hashMap.get("career_time");
        doctorVo.setCareerTime(date);
        doctorVo.setExperience((String) hashMap.get("experince"));
        doctorVo.setPersonDetails((String) hashMap.get("personal_details"));
    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @return str
     */
    public static String DateToStr(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @return date
     */
    public static Date StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    //根据patientRegisterServiceId查询sys_register_service和patient_register_service表信息
    public HashMap<String, Object> findSysRegisterServiceByPRSId(HashMap<String, Object> hashMap) {
        HashMap<String, Object> resultHashMap = patientRegisterServiceDao.findSysRegisterServiceByPRSIdExecute(hashMap);
        return resultHashMap;
    }

    public void prepareOperationStatisticData() {
        //通过接口，获取所有关注平台的openid
        WechatUtil wxUtil = new WechatUtil();
        Map token = systemService.getWechatParameter();

        //获取昨天的日期
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -3);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());

        //在日志表中，获取昨天使用平台的用户ID
        List<HashMap<String, Object>> userListYesterday = wechatInfoDao.getUserListYesterday(yesterday);
        List<HashMap<String, Object>> userLog = new ArrayList<HashMap<String, Object>>();

        for (Map userListInfo : userListYesterday) {
            HashMap<String, Object> userStatisticInfo = new HashMap<String, Object>();
            //根据用户的openid，获取用户的名称，来源，关注时间
            if (userListInfo.get("open_id") != null) {
                WechatBean userinfo = wxUtil.getWechatName((String) token.get("token"), (String) userListInfo.get("open_id"));
                HashMap<String, Object> userAttention = wechatAttentionDao.getAttention((String) userListInfo.get("open_id"));
                userStatisticInfo.put("id", IdGen.uuid());
                userStatisticInfo.put("openid", userListInfo.get("open_id"));
                userStatisticInfo.put("date", yesterday);
                if (userinfo.getNickname() != null) {
                    userStatisticInfo.put("name", EmojiFilter.coverEmoji(userinfo.getNickname()));
                } else {
                    userStatisticInfo.put("name", null);
                }
                if (userAttention != null) {
                    userStatisticInfo.put("marketid", userAttention.get("marketer"));
                    userStatisticInfo.put("attentiondate", userAttention.get("date"));
                } else {
                    userStatisticInfo.put("marketid", null);
                    userStatisticInfo.put("attentiondate", null);
                }

                HashMap<String, Object> dataParam = new HashMap<String, Object>();
                dataParam.put("open_id", userListInfo.get("open_id"));
                dataParam.put("date", yesterday);
                dataParam.put("alhos", "00000043 ");
                dataParam.put("ahosaldoc", "00000076");
                dataParam.put("ahosaldep", "00000044");
                dataParam.put("ahosadepaldoc", "00000075");
                dataParam.put("alfirstill", "00000046");
                dataParam.put("afirstalsecill", "00000047");
                dataParam.put("asecillaldoc", "00000077");
                dataParam.put("asecillahosaldoc", "00000094");
                dataParam.put("asecillalhos", "00000045");
                dataParam.put("adatealhos", "00000034");
                dataParam.put("adatealdoc", "00000032");
                dataParam.put("adateahosaldoc", "00000035");
                dataParam.put("adocdetail", "00000073");
                dataParam.put("adocadateserv", "00000030");
                dataParam.put("adocaservdetail", "00000029");
                dataParam.put("apdoc", "00000019");
                dataParam.put("canceldoc", "00000014");
                dataParam.put("appraisedoc", "00000012");
                dataParam.put("sharedoc", "00000013");
                dataParam.put("servattention", "00000095");
                dataParam.put("attentiondoc", "00000010");
                dataParam.put("checkservroute", "00000036");
                dataParam.put("checkservstatus", "00000096");
                dataParam.put("checkdocaplocation", "00000042");
                dataParam.put("checkdoc7daylocation", "00000074");
                dataParam.put("checkdoc7dayapbylocation", "00000031");
                dataParam.put("myfirstpage", "00000080");
                dataParam.put("myappointment", "00000097");
                dataParam.put("myapdetail", "00000017");
                dataParam.put("myselfinfo", "00000098");
                dataParam.put("attdoc", "00000011");
                HashMap<String, Object> userOperationStatistic = OperationComprehensiveDao.getUserOperationStatistic(dataParam);

                userStatisticInfo.put("alhos", Integer.valueOf(userOperationStatistic.get("alhos").toString()));
                userStatisticInfo.put("ahosaldoc", Integer.valueOf(userOperationStatistic.get("ahosaldoc").toString()));
                userStatisticInfo.put("ahosaldep", Integer.valueOf(userOperationStatistic.get("ahosaldep").toString()));
                userStatisticInfo.put("ahosadepaldoc", Integer.valueOf(userOperationStatistic.get("ahosadepaldoc").toString()));
                userStatisticInfo.put("alfirstill", Integer.valueOf(userOperationStatistic.get("alfirstill").toString()));
                userStatisticInfo.put("afirstalsecill", Integer.valueOf(userOperationStatistic.get("afirstalsecill").toString()));
                userStatisticInfo.put("asecillaldoc", Integer.valueOf(userOperationStatistic.get("asecillaldoc").toString()));
                userStatisticInfo.put("asecillahosaldoc", Integer.valueOf(userOperationStatistic.get("asecillahosaldoc").toString()));
                userStatisticInfo.put("asecillalhos", Integer.valueOf(userOperationStatistic.get("asecillalhos").toString()));
                userStatisticInfo.put("adatealhos", Integer.valueOf(userOperationStatistic.get("adatealhos").toString()));
                userStatisticInfo.put("adatealdoc", Integer.valueOf(userOperationStatistic.get("adatealdoc").toString()));
                userStatisticInfo.put("adateahosaldoc", Integer.valueOf(userOperationStatistic.get("adateahosaldoc").toString()));
                userStatisticInfo.put("adocdetail", Integer.valueOf(userOperationStatistic.get("adocdetail").toString()));
                userStatisticInfo.put("adocadateserv", Integer.valueOf(userOperationStatistic.get("adocadateserv").toString()));
                userStatisticInfo.put("adocaservdetail", Integer.valueOf(userOperationStatistic.get("adocaservdetail").toString()));
                userStatisticInfo.put("apdoc", Integer.valueOf(userOperationStatistic.get("apdoc").toString()));
                userStatisticInfo.put("canceldoc", Integer.valueOf(userOperationStatistic.get("canceldoc").toString()));
                userStatisticInfo.put("appraisedoc", Integer.valueOf(userOperationStatistic.get("appraisedoc").toString()));
                userStatisticInfo.put("sharedoc", Integer.valueOf(userOperationStatistic.get("sharedoc").toString()));
                userStatisticInfo.put("servattention", Integer.valueOf(userOperationStatistic.get("servattention").toString()));
                userStatisticInfo.put("attentiondoc", Integer.valueOf(userOperationStatistic.get("attentiondoc").toString()));
                userStatisticInfo.put("checkservroute", Integer.valueOf(userOperationStatistic.get("checkservroute").toString()));
                userStatisticInfo.put("checkservstatus", Integer.valueOf(userOperationStatistic.get("checkservstatus").toString()));
                userStatisticInfo.put("checkdocaplocation", Integer.valueOf(userOperationStatistic.get("checkdocaplocation").toString()));
                userStatisticInfo.put("checkdoc7daylocation", Integer.valueOf(userOperationStatistic.get("checkdoc7daylocation").toString()));
                userStatisticInfo.put("checkdoc7dayapbylocation", Integer.valueOf(userOperationStatistic.get("checkdoc7dayapbylocation").toString()));
                userStatisticInfo.put("myfirstpage", Integer.valueOf(userOperationStatistic.get("myfirstpage").toString()));
                userStatisticInfo.put("myappointment", Integer.valueOf(userOperationStatistic.get("myappointment").toString()));
                userStatisticInfo.put("myapdetail", Integer.valueOf(userOperationStatistic.get("myapdetail").toString()));
                userStatisticInfo.put("myselfinfo", Integer.valueOf(userOperationStatistic.get("myselfinfo").toString()));
                userStatisticInfo.put("attdoc", Integer.valueOf(userOperationStatistic.get("attdoc").toString()));
                userLog.add(userStatisticInfo);
            }
        }
        int k = OperationComprehensiveDao.insertBatchUserStatistic(userLog);
    }


    public String getNickName(String openid) {
        String name = "";
        WechatUtil wxUtil = new WechatUtil();
        Map token = systemService.getWechatParameter();
        WechatBean userinfo = wxUtil.getWechatName((String) token.get("token"), openid);
        name = userinfo.getNickname();
        return name;
    }


    public List<HashMap<String, Object>> getOverallStatisticData(HashMap<String, Object> hashMap) {
        List<HashMap<String, Object>> resultHashMap = OperationComprehensiveDao.getOverallStatisticData(hashMap);
        return resultHashMap;
    }

    public List<HashMap<String, Object>> getTuiStatisticData(HashMap<String, Object> hashMap) {
        List<HashMap<String, Object>> resultHashMap = OperationComprehensiveDao.getTuiStatisticData(hashMap);
        return resultHashMap;
    }


    public List<WechatAttention> getQDStatisticData(HashMap<String, Object> hashMap) {
        return OperationComprehensiveDao.getQDStatisticData(hashMap);
    }

    public List<WechatAttention> getQDCancelStatisticData(HashMap<String, Object> hashMap) {
        return OperationComprehensiveDao.getQDCancelStatisticData(hashMap);
    }

    public List<WechatAttention> getQDMarketerData(HashMap<String, Object> hashMap) {
        return OperationComprehensiveDao.getQDMarketerData(hashMap);
    }

    public int getUserConsultTimes(HashMap<String, Object> hashMap) {
        return OperationComprehensiveDao.getUserConsultTimes(hashMap);
    }

    public void getUserStatisticInfo(HashMap<String, Object> params, HashMap<String, Object> response) {
        List<WechatAttention> listMarketer = OperationComprehensiveDao.getQDMarketerData(params);
        List<WechatAttention> listData = OperationComprehensiveDao.getQDStatisticData(params);
        List<WechatAttention> cancelListData = OperationComprehensiveDao.getQDCancelStatisticData(params);

        int newAddNum = 0;
        int cancelNum = 0;
        for (WechatAttention dataMarketer : listMarketer) {
            if (!(dataMarketer.getMarketer() == null)) {
                if (!(dataMarketer.getMarketer().equals(""))) {
                    HashMap<String, Object> dataMap = new HashMap<String, Object>();
                    for (WechatAttention data : listData) {
                        if (!(data.getMarketer() == null)) {
                            if (!(data.getStatus() == null)) {
                                if (data.getMarketer().equals(dataMarketer.getMarketer()) && data.getStatus().equals("0")) {
                                    newAddNum++;
                                    for (WechatAttention cancelData : cancelListData) {
                                        if (cancelData.getOpenid().equals(data.getOpenid())) {
                                            cancelNum++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        int jinAddNum = newAddNum - cancelNum;
        response.put("newAddNum", newAddNum);
        response.put("cancelNum", cancelNum);
        response.put("jinAddNum", jinAddNum);

        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("startDate", "2015-01-01");
        param.put("endDate", params.get("endDate"));
        listMarketer.clear();
        listData.clear();
        cancelListData.clear();
        listMarketer = OperationComprehensiveDao.getQDMarketerData(param);
        listData = OperationComprehensiveDao.getQDStatisticData(param);
        cancelListData = OperationComprehensiveDao.getQDCancelStatisticData(param);

        newAddNum = 0;
        cancelNum = 0;
        for (WechatAttention dataMarketer : listMarketer) {
            if (!(dataMarketer.getMarketer() == null)) {
                if (!(dataMarketer.getMarketer().equals(""))) {
                    HashMap<String, Object> dataMap = new HashMap<String, Object>();
                    for (WechatAttention data : listData) {
                        if (!(data.getMarketer() == null)) {
                            if (!(data.getStatus() == null)) {
                                if (data.getMarketer().equals(dataMarketer.getMarketer()) && data.getStatus().equals("0")) {
                                    newAddNum++;
                                    for (WechatAttention cancelData : cancelListData) {
                                        if (cancelData.getOpenid().equals(data.getOpenid())) {
                                            cancelNum++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        int leiJiNum = newAddNum - cancelNum;
        response.put("leiJiNum", leiJiNum);
    }

    public void getDoctorStatisticInfo(HashMap<String, Object> params, HashMap<String, Object> response) {
        List<DoctorVo> doctorList = OperationComprehensiveDao.getDoctorStatisticInfo(params);
        int doctorNewNum = doctorList.size();
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("startDate", "2015-01-01");
        param.put("endDate", params.get("endDate"));
        String doctorAllNum = dataStatisticService.findCountDoctorCountNmuber();
        response.put("doctorNewNum", doctorNewNum);
        response.put("doctorAllNum", doctorAllNum);
    }

    public void getAppointmentStatisticInfo(HashMap<String, Object> params, HashMap<String, Object> response) {
        params.put("type", "new");
        int appointmentJinNum = OperationComprehensiveDao.getAppointmentNum(params);
        params.put("type", "cancel");
        int cancelAppointmentNum = OperationComprehensiveDao.getAppointmentNum(params);
        int appointmentNum = appointmentJinNum + cancelAppointmentNum;

        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("startDate", "2015-01-01");
        param.put("endDate", params.get("endDate"));
        param.put("type", "new");
        int appointmentAllNum = OperationComprehensiveDao.getAppointmentNum(param);
        param.put("type", "cancel");
        int cancelAppointmentAllNum = OperationComprehensiveDao.getAppointmentNum(param);
        int appointmentAllJinNum = appointmentAllNum + cancelAppointmentAllNum;

        int newUserAppointmentNum = OperationComprehensiveDao.getUserAppointmentNum(params);

        response.put("appointmentNum", appointmentNum);
        response.put("cancelAppointmentNum", cancelAppointmentNum);
        response.put("appointmentJinNum", appointmentJinNum);
        response.put("appointmentAllJinNum", appointmentAllJinNum);
        response.put("newUserAppointmentNum", newUserAppointmentNum);
    }

    public void getConsultStatisticInfo(HashMap<String, Object> params, HashMap<String, Object> response) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("startDate", params.get("startDate"));
        param.put("endDate", params.get("endDate"));
        param.put("type", "allConsult");
        int allConsult = OperationComprehensiveDao.getConsultStatisticInfo(param);
        response.put("allConsult", allConsult);//新增咨询人数

        param.put("type", "noUseConsult");
        int noUseConsult = OperationComprehensiveDao.getConsultStatisticInfo(param);
        response.put("noUseConsult", noUseConsult);
        response.put("jinConsult", allConsult - noUseConsult);

        param.put("type", "newUseAllConsult");
        int newUseAllConsult = OperationComprehensiveDao.getConsultStatisticInfo(param);

        param.put("type", "newUseNoUseConsult");
        int newUseNoUseConsult = OperationComprehensiveDao.getConsultStatisticInfo(param);

        response.put("newUserAllUseConsult", newUseAllConsult - newUseNoUseConsult);
        param = new HashMap<String, Object>();
        param.put("startDate", "2015-01-01");
        param.put("endDate", params.get("endDate"));
        param.put("type", "allConsult");
        allConsult = OperationComprehensiveDao.getConsultStatisticInfo(param);
        param.put("type", "noUseConsult");
        noUseConsult = OperationComprehensiveDao.getConsultStatisticInfo(param);
        response.put("leiJiUseConsult", allConsult - noUseConsult);
    }

    public void getNormalUserStatistic(HashMap<String, Object> params, HashMap<String, Object> response) {

        //计算累计关注用户数
        List<HashMap<String, Object>> listData = getOverallStatisticData(params);
        response.put("allUserNum", listData.get(0).get("sum_user"));

        int activeUserNormalStatistic = activeUserNormalStatistic((String) params.get("startDate"), (String) params.get("endDate"));
        int activeUserNormalStatistic1day = activeUserNormalStatistic(dateCaculate((String) params.get("endDate"), -1), (String) params.get("endDate"));
        int activeUserNormalStatistic7day = activeUserNormalStatistic(dateCaculate((String) params.get("endDate"), -7), (String) params.get("endDate"));
        int activeUserNormalStatistic1month = activeUserNormalStatistic(dateCaculate((String) params.get("endDate"), -30), (String) params.get("endDate"));
        int activeUserNormalStatistic1quarter = activeUserNormalStatistic(dateCaculate((String) params.get("endDate"), -90), (String) params.get("endDate"));

        response.put("activeUserNormalStatistic", activeUserNormalStatistic);
        response.put("activeUserNormalStatistic1day", activeUserNormalStatistic1day);
        response.put("activeUserNormalStatistic7day", activeUserNormalStatistic7day);
        response.put("activeUserNormalStatistic1month", activeUserNormalStatistic1month);
        response.put("activeUserNormalStatistic1quarter", activeUserNormalStatistic1quarter);
    }

    public void getServiceUseStatistic(HashMap<String, Object> params, HashMap<String, Object> response) {
        int activeUserStatistic = activeUserStatistic((String) params.get("startDate"), (String) params.get("endDate"));
        int activeUserStatistic1day = activeUserStatistic(dateCaculate((String) params.get("endDate"), -1), (String) params.get("endDate"));
        int activeUserStatistic7day = activeUserStatistic(dateCaculate((String) params.get("endDate"), -7), (String) params.get("endDate"));
        int activeUserStatistic1month = activeUserStatistic(dateCaculate((String) params.get("endDate"), -30), (String) params.get("endDate"));
        int activeUserStatistic1quarter = activeUserStatistic(dateCaculate((String) params.get("endDate"), -90), (String) params.get("endDate"));

        response.put("activeUserStatistic", activeUserStatistic);
        response.put("activeUserStatistic1day", activeUserStatistic1day);
        response.put("activeUserStatistic7day", activeUserStatistic7day);
        response.put("activeUserStatistic1month", activeUserStatistic1month);
        response.put("activeUserStatistic1quarter", activeUserStatistic1quarter);
    }

    //有效咨询用户  zdl
    public void getValidConsultStatistic(HashMap<String, Object> params, HashMap<String, Object> response) {
        int getConsultStatistic = getConsultStatistic((String) params.get("startDate"), (String) params.get("endDate"));
        int getConsultStatistic1day = getConsultStatistic(dateCaculate((String) params.get("endDate"), -1), (String) params.get("endDate"));
        int getConsultStatistic7day = getConsultStatistic(dateCaculate((String) params.get("endDate"), -7), (String) params.get("endDate"));
        int getConsultStatistic1month = getConsultStatistic(dateCaculate((String) params.get("endDate"), -30), (String) params.get("endDate"));
        int activeUserStatistic1quarter = getConsultStatistic(dateCaculate((String) params.get("endDate"), -90), (String) params.get("endDate"));

        response.put("getConsultStatistic", getConsultStatistic);
        response.put("getConsultStatistic1day", getConsultStatistic1day);
        response.put("getConsultStatistic7day", getConsultStatistic7day);
        response.put("getConsultStatistic1month", getConsultStatistic1month);
        response.put("activeUserStatistic1quarter", activeUserStatistic1quarter);
    }

    //有效咨询频次 zdl 最少 最多 平均
    public void getFrequencyValidConsult(HashMap<String, Object> params, HashMap<String, Object> response) {
        List<Integer> getFrequencyValidConsultSum = getFrequencyValidConsultCount((String) params.get("startDate"), (String) params.get("endDate"));
        List<Integer> getFrequencyValidConsult1day = getFrequencyValidConsultCount(dateCaculate((String) params.get("endDate"), -1), (String) params.get("endDate"));
        List<Integer> getFrequencyValidConsult7day = getFrequencyValidConsultCount(dateCaculate((String) params.get("endDate"), -7), (String) params.get("endDate"));
        List<Integer> getFrequencyValidConsult1month = getFrequencyValidConsultCount(dateCaculate((String) params.get("endDate"), -30), (String) params.get("endDate"));
        List<Integer> getFrequencyValidConsult1quarter = getFrequencyValidConsultCount(dateCaculate((String) params.get("endDate"), -90), (String) params.get("endDate"));
        if (getFrequencyValidConsultSum != null && getFrequencyValidConsultSum.size() > 0) {
            response.put("getConsultStatisticMin", getFrequencyValidConsultSum.get(0));
            response.put("getConsultStatisticMax", getFrequencyValidConsultSum.get(1));
            response.put("getConsultStatisticAverage", getFrequencyValidConsultSum.get(2));
        } else {
            response.put("getConsultStatisticMin", "无");
            response.put("getConsultStatisticMax", "无");
            response.put("getConsultStatisticAverage", "无");
        }
        if (getFrequencyValidConsult1day != null && getFrequencyValidConsult1day.size() > 0) {
            response.put("getConsultStatistic1dayMin", getFrequencyValidConsult1day.get(0));
            response.put("getConsultStatistic1dayMax", getFrequencyValidConsult1day.get(1));
            response.put("getConsultStatistic1dayAverage", getFrequencyValidConsult1day.get(2));
        } else {
            response.put("getConsultStatistic1dayMin", "无");
            response.put("getConsultStatistic1dayMax", "无");
            response.put("getConsultStatistic1dayAverage", "无");
        }
        if (getFrequencyValidConsult7day != null && getFrequencyValidConsult7day.size() > 0) {
            response.put("getConsultStatistic7dayMin", getFrequencyValidConsult7day.get(0));
            response.put("getConsultStatistic7dayMax", getFrequencyValidConsult7day.get(1));
            response.put("getConsultStatistic7dayAverage", getFrequencyValidConsult7day.get(2));
        } else {
            response.put("getConsultStatistic7dayMin", "无");
            response.put("getConsultStatistic7dayMax", "无");
            response.put("getConsultStatistic7dayAverage", "无");
        }
        if (getFrequencyValidConsult1month != null && getFrequencyValidConsult1month.size() > 0) {
            response.put("getConsultStatistic1monthMin", getFrequencyValidConsult1month.get(0));
            response.put("getConsultStatistic1monthMax", getFrequencyValidConsult1month.get(1));
            response.put("getConsultStatistic1monthAverage", getFrequencyValidConsult1month.get(2));
        } else {
            response.put("getConsultStatistic1monthMin", "无");
            response.put("getConsultStatistic1monthMax", "无");
            response.put("getConsultStatistic1monthAverage", "无");
        }
        if (getFrequencyValidConsult1quarter != null && getFrequencyValidConsult1quarter.size() > 0) {
            response.put("activeUserStatistic1quarterMin", getFrequencyValidConsult1quarter.get(0));
            response.put("activeUserStatistic1quarterMax", getFrequencyValidConsult1quarter.get(1));
            response.put("activeUserStatistic1quarterAverage", getFrequencyValidConsult1quarter.get(2));
        } else {
            response.put("activeUserStatistic1quarterMin", "无");
            response.put("activeUserStatistic1quarterMax", "无");
            response.put("activeUserStatistic1quarterAverage", "无");
        }
    }

    //咨询激活时长
    public void getDateValidConsult(HashMap<String, Object> params, HashMap<String, Object> response) {
        List<Integer> getDateValidConsultSum = getDateValidConsultCount((String) params.get("startDate"), (String) params.get("endDate"));

        response.put("getConsultStatisticMin", getDateValidConsultSum.size() > 0 ? getDateValidConsultSum.get(0) : "无");
        response.put("getConsultStatisticMax", getDateValidConsultSum.size() > 1 ? getDateValidConsultSum.get(1) : "无");
        response.put("getConsultStatisticAverage", getDateValidConsultSum.size() > 2 ? getDateValidConsultSum.get(2) : "无");
    }

    public List<Integer> getDateValidConsultCount(String startDate, String endDate) {
        List<Integer> arrInteger = new ArrayList<Integer>();
        //查出这段时间咨询过的Openid,和最早咨询时间
        Query queryWeChatRecord = new Query();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            queryWeChatRecord.addCriteria(new Criteria("infoTime").gte(sdf.parse(startDate)).lte(sdf.parse(endDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<String> WechatRecordList = mongoDBServiceWeChatRecord.queryStringListDistinct(queryWeChatRecord, "openid");

        //计算并记录从关注到咨询的天数
        for (String s : WechatRecordList) {
            //查询最早关注时间
            Query queryDate = new Query();
            Criteria criteria = Criteria.where("openid").is(s);
            queryDate.addCriteria(criteria);
            queryDate.with(new Sort(new Sort.Order(Sort.Direction.ASC, "date"))).limit(1);
            List<WechatAttention> attentionList = mongoDBServiceAttention.queryList(queryDate);

            //查询最早咨询时间
            Query queryConsuleDate = new Query();
            Criteria queryAttentionCri = Criteria.where("openid").is(s);
            queryConsuleDate.addCriteria(queryAttentionCri);
            queryConsuleDate.with(new Sort(new Sort.Order(Sort.Direction.ASC, "infoTime"))).limit(1);
            List<WechatRecord> wechatRecords = mongoDBServiceWeChatRecord.queryList(queryConsuleDate);
            try {
                if (wechatRecords.get(0).getinfoTime() != null && attentionList.get(0).getDate() != null) {
                    long t = wechatRecords.get(0).getinfoTime().getTime() - attentionList.get(0).getDate().getTime();
                    Integer pastDate = StringUtils.toInteger(t / (24 * 60 * 60 * 1000));
                    arrInteger.add(pastDate);
                }

            } catch (Exception e) {
                System.out.print("----------------时间为空---------------");
                e.printStackTrace();
            }
        }
        //算出最少时间、最多时间、平均时间
        if (arrInteger != null && arrInteger.size() > 0) {
            calculate(arrInteger);
        }
        return arrInteger;
    }


    public List<Integer> getFrequencyValidConsultCount(String startDate, String endDate) {
        List<Integer> countList = new ArrayList<Integer>();
        //查出这段时间咨询过的Openid
        Query queryWeChatRecord = new Query();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            queryWeChatRecord.addCriteria(new Criteria("infoTime").gte(sdf.parse(startDate)).lte(sdf.parse(endDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<String> WechatRecordList = mongoDBServiceWeChatRecord.queryStringListDistinct(queryWeChatRecord, "openid");

        //查询此人的有效咨询次数，并记录
        for (String openId : WechatRecordList) {
            int valid = 0;//初始化此人有效咨询
            int count = 0;
            Query queryWeChatRecordOne = new Query();
            try {
                //所有的聊天记录按照时间排序
                queryWeChatRecordOne.addCriteria(new Criteria("openid").is(openId));
                queryWeChatRecordOne.with(new Sort(new Sort.Order(Sort.Direction.ASC, "infoTime")));
                List<WechatRecord> Recordlists = mongoDBServiceWeChatRecord.queryList(queryWeChatRecordOne);

                //遍历所有记录
                for (WechatRecord wechatRec : Recordlists) {
                    if ("2002".equals(wechatRec.getOpercode()) && " /:share".equals(wechatRec.getText())) {
                        valid = 2;//无效咨询
                    } else if ("1004".equals(wechatRec.getOpercode())) {//结束会话
                        if (valid != 2) {
                            count++;//有效咨询
                        }
                    }
                }
                countList.add(count);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //遍历集合，找出最小咨询次数，找出最多咨询次数，找出平均咨询次数
        if (countList != null && countList.size() > 0) {
            calculate(countList);
        }
        return countList;
    }

    private void calculate(List<Integer> countList) {
        int min = 100;
        int max = 0;
        int sum = 0;
        for (Integer count : countList) {
            if (min > count) {//最小
                min = count;
            } else if (max < count) {//最大
                max = count;
            }
            sum = sum + count;//总数,算平均用
        }
        int countsize = countList.size();
        countList.clear();
        countList.add(min);
        countList.add(max);
        countList.add(sum / countsize);
    }

    String dateCaculate(String date, long n) {
        String dateBack = "";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long value = n * 24 * 60 * 60 * 1000;
            dateBack = df.format(new Date(df.parse(date).getTime() + value));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateBack;
    }

    public int activeUserStatistic(String startDate, String endDate) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //获取预约总人数
        List<MongoLog> logList = getMongoAppointUser(startDate, endDate, sdf);

        //获取咨询总人数
        List<WechatRecord> weChatList = getWechatRecordsUser(startDate, endDate, sdf);

        //获取访问郑玉巧说的总人数
        List<String> zhengyuqiaoList = getZhengYuQiaoUser(startDate, endDate);
        int sum = 0;
        int activeUserNum = 0;
        for (int i = 0; i < logList.size(); i++) {
            activeUserNum++;//在预约模块
            for (int j = 0; j < weChatList.size(); j++) {
                activeUserNum++;//在咨询模块
                for (int k = 0; k < zhengyuqiaoList.size(); k++) {
                    activeUserNum++;//郑玉巧说模块
                    Object a = logList.get(i);
                    Object b = weChatList.get(j);
                    Object c = zhengyuqiaoList.get(k);
                    if (a.equals(b) || a.equals(c) || b.equals(c)) {
                        activeUserNum--;//同时在两个模块
                    }
                    ++sum;
                    System.out.println("-------------------i=" + i + "-----------------j=" + j + "--------------k=" + k);
                    System.out.println(sum);
                }
            }
        }
        return activeUserNum;
    }

    public List<String> getZhengYuQiaoUser(String startDate, String endDate) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("startDate", startDate);
        hashMap.put("endDate", endDate);
        hashMap.put("searchlist", "查看某篇文章");
        return dataStatisticsDao.getZhengYuQiaoShuoUser(hashMap);
    }

    public List<WechatRecord> getWechatRecordsUser(String startDate, String endDate, SimpleDateFormat sdf) {
        Query queryWeChatLog = new Query();
        try {
            queryWeChatLog.addCriteria(new Criteria("infoTime").gte(sdf.parse(startDate)).lte(sdf.parse(endDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mongoDBServiceWeChatRecord.queryListDistinct(queryWeChatLog, "openid");
    }

    public List<MongoLog> getMongoAppointUser(String startDate, String endDate, SimpleDateFormat sdf) {
        Query querySysLog = new Query();
        try {
            querySysLog.addCriteria(
                    Criteria.where("title").regex("用户预约医生").andOperator(
                            Criteria.where("create_date").lte(sdf.parse(endDate)),
                            Criteria.where("create_date").gte(sdf.parse(startDate))
                    ));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mongoDBServiceLog.queryListDistinct(querySysLog, "open_id");
    }

    //获取访问咨询频道总人数
    public int getConsultUser(HashMap<String, Object> searchMap) {
        int consultUser = dataStatisticsDao.getConsultUser(searchMap);
        return consultUser;
    }


    int getConsultStatistic(String startDate, String endDate) {//待优化
        int ConsultStatisticNum = 0;
        Query queryWeChatRecord = new Query();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            queryWeChatRecord.addCriteria(new Criteria("infoTime").gte(sdf.parse(startDate)).lte(sdf.parse(endDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //查出这段时间咨询过的Openid
        List<String> WechatRecordList = mongoDBServiceWeChatRecord.queryStringListDistinct(queryWeChatRecord, "openid");

        //根据OpenId查询该用户的所有聊天记录（根据时间升序排序）
        for (String openId : WechatRecordList) {
            int valid = 0;//初始化此人咨询是否有效咨询
            Query queryWeChatRecordOne = new Query();
            try {
                //所有的聊天记录按照时间排序
                queryWeChatRecordOne.addCriteria(new Criteria("openid").is(openId));
                queryWeChatRecordOne.with(new Sort(new Sort.Order(Sort.Direction.ASC, "infoTime")));
                List<WechatRecord> Recordlists = mongoDBServiceWeChatRecord.queryList(queryWeChatRecordOne);

                //遍历所有记录
                for (WechatRecord wechatRec : Recordlists) {
                    if ("2002".equals(wechatRec.getOpercode()) && " /:share".equals(wechatRec.getText())) {
                        valid = 2;//无效咨询
                    } else if ("1004".equals(wechatRec.getOpercode())) {//结束会话
                        if (valid != 2)//如果此次对话不存在无效咨询标志（说明是有效咨询），则跳出循环，判断下一个人
                            break;
                    }
                }
                ConsultStatisticNum++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ConsultStatisticNum;
    }

    int activeUserNormalStatistic(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Query querySysLog = new Query();
        try {
            querySysLog.addCriteria(
                    Criteria.where("title").regex("获取系统内所有医院").andOperator(
                            Criteria.where("create_date").lte(sdf.parse(endDate)),
                            Criteria.where("create_date").gte(sdf.parse(startDate))
                    ));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<MongoLog> logList = mongoDBServiceLog.queryListDistinct(querySysLog, "open_id");

//        Query querySysLogNormal = new Query();
//        try {
//            querySysLogNormal.addCriteria(
//                    new Criteria("create_date").lte(sdf.parse(endDate)).andOperator(
//                            new Criteria("create_date").gte(sdf.parse(startDate))
//                    ));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        List<MongoLog> logNormalList = mongoDBServiceLog.queryListDistinct(querySysLogNormal, "open_id");

//        for(int i=0;i<logNormalList.size();i++)
//        {
//            activeUserNum++;
//            for(int j=0;j<logList.size();j++)
//            {
//                boolean k = String.valueOf(logList.get(j)).equals(String.valueOf(logNormalList.get(i)));
//                if(k)
//                {
//                    activeUserNum--;
//                }
//
//            }
//        }
        return logList.size();
    }

    //有效预约用户  zhangbo
    public void getcountValidReserve(HashMap<String, Object> params, HashMap<String, Object> response) {
        int getConsultStatistic = countValidReserve((String) params.get("startDate"), (String) params.get("endDate"));
        int getConsultStatistic1day = countValidReserve(dateCaculate((String) params.get("endDate"), -1), (String) params.get("endDate"));
        int getConsultStatistic7day = countValidReserve(dateCaculate((String) params.get("endDate"), -7), (String) params.get("endDate"));
        int getConsultStatistic1month = countValidReserve(dateCaculate((String) params.get("endDate"), -30), (String) params.get("endDate"));
        int activeUserStatistic1quarter = countValidReserve(dateCaculate((String) params.get("endDate"), -90), (String) params.get("endDate"));

        response.put("getcountValidReserve", getConsultStatistic);
        response.put("getcountValidReserve1day", getConsultStatistic1day);
        response.put("getcountValidReserve7day", getConsultStatistic7day);
        response.put("getcountValidReserve1month", getConsultStatistic1month);
        response.put("getcountValidReserve1quarter", activeUserStatistic1quarter);
    }

    int countValidReserve(String startDate, String endDate) {
        HashMap<String, String> date = new HashMap<String, String>();
        date.put("startDate", startDate);
        date.put("endDate", endDate);
        int count = patientRegisterServiceDao.countValidReserve(date);
        return count;
    }

    // 沉寂用户 zhangbo
    public void getcountStillnessUser(HashMap<String, Object> params,
                                      HashMap<String, Object> response) {
        response.put("getcountStillnessUser",
                countStillnessUser((String) params.get("startDate"), (String) params.get("endDate")));
    }

    int countStillnessUser(String startDate, String endDate) {
        // 查询所有用户
        List<String> openidList;
        List<String> inWeChat;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Query query = new Query();
//			query.addCriteria(new Criteria("date").lte(sdf.parse(startDate)).gte(sdf.parse(endDate)).andOperator(new Criteria("openid").ne("null")));
            query.addCriteria(new Criteria("openid").ne(null));
            openidList = mongoDBServiceAttention.queryStringListDistinct(query, "openid");
            // 所有用户中预约过的用户
            Query queryInLog = new Query();
            queryInLog.addCriteria(new Criteria("title")
                    .is("用户预约支付")
                    .andOperator(
                            new Criteria("create_date").gte(
                                    sdf.parse(startDate)).lte(
                                    sdf.parse(endDate))));
            List<String> inLog = mongoDBServiceLog.queryStringListDistinct(
                    queryInLog, "open_id");
            // 所有用户中通过微信咨询过的用户
            Query querInWeChat = new Query();
            querInWeChat.addCriteria(new Criteria("openid").ne("null")
                    .andOperator(
                            new Criteria("infoTime").lte(
                                    sdf.parse(endDate)).gte(
                                    sdf.parse(startDate))));
            inWeChat = mongoDBServiceWeChatRecord
                    .queryStringListDistinct(querInWeChat, "openid");
            // 去除openidList,inLog,inWeChat中重复数据,并将两个List组合在一起
            HashSet openid = new HashSet(openidList);
            openidList.clear();
            openidList.addAll(openid);
            HashSet log = new HashSet(inLog);
            HashSet weChat = new HashSet(inWeChat);
            inWeChat.clear();
            inWeChat.addAll(log);
            inWeChat.addAll(weChat);
            return openidList.size() - inWeChat.size();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }

    //有效预约频次 张博

    public void getValidReserveConsultCounts(HashMap<String, Object> params, HashMap<String, Object> response) {
        List<HashMap<String, Object>> getConsultStatistic = getValidReserveConsultCount((String) params.get("startDate"), (String) params.get("endDate"));
        List<HashMap<String, Object>> getConsultStatistic1day = getValidReserveConsultCount(dateCaculate((String) params.get("endDate"), -1), (String) params.get("endDate"));
        List<HashMap<String, Object>> getConsultStatistic7day = getValidReserveConsultCount(dateCaculate((String) params.get("endDate"), -7), (String) params.get("endDate"));
        List<HashMap<String, Object>> getConsultStatistic1month = getValidReserveConsultCount(dateCaculate((String) params.get("endDate"), -30), (String) params.get("endDate"));
        List<HashMap<String, Object>> activeUserStatistic1quarter = getValidReserveConsultCount(dateCaculate((String) params.get("endDate"), -90), (String) params.get("endDate"));

        response.put("getValidReserveConsultCountMax", getConsultStatistic.get(0) != null ? getConsultStatistic.get(0).get("max") : "无");
        response.put("getValidReserveConsultCountMin", getConsultStatistic.get(0) != null ? getConsultStatistic.get(0).get("min") : "无");
        response.put("getValidReserveConsultCountAvg", getConsultStatistic.get(0) != null ? getConsultStatistic.get(0).get("min") : "无");
        String max = "无";
        String min = "无";
        String avg = "无";
        if (getConsultStatistic1day.get(0) != null && !getConsultStatistic1day.get(0).equals("null")) {
            max = getConsultStatistic1day.get(0).get("max") + "";
            min = getConsultStatistic1day.get(0).get("min") + "";
            avg = getConsultStatistic1day.get(0).get("avg") + "";
            if (max.equals(min)) {
                min = "无";
            }
        }
        response.put("getValidReserveConsultCount1dayMax", max);
        response.put("getValidReserveConsultCount1dayMin", min);
        response.put("getValidReserveConsultCount1dayAvg", avg);

        response.put("getValidReserveConsultCount7dayMax", getConsultStatistic7day.get(0).get("max") != null ? getConsultStatistic7day.get(0).get("max") : "无");
        response.put("getValidReserveConsultCount7dayMin", getConsultStatistic7day.get(0).get("min") != null ? getConsultStatistic7day.get(0).get("min") : "无");
        response.put("getValidReserveConsultCount7dayAvg", getConsultStatistic7day.get(0).get("avg") != null ? getConsultStatistic7day.get(0).get("avg") : "无");

        response.put("getValidReserveConsultCount1monthMax", getConsultStatistic1month.get(0).get("max") != null ? getConsultStatistic1month.get(0).get("max") : "无");
        response.put("getValidReserveConsultCount1monthMin", getConsultStatistic1month.get(0).get("min") != null ? getConsultStatistic1month.get(0).get("min") : "无");
        response.put("getValidReserveConsultCount1monthAvg", getConsultStatistic1month.get(0).get("avg") != null ? getConsultStatistic1month.get(0).get("avg") : "无");

        response.put("getValidReserveConsultCount1quarterMax", activeUserStatistic1quarter.get(0).get("max") != null ? activeUserStatistic1quarter.get(0).get("max") : "无");
        response.put("getValidReserveConsultCount1quarterMin", activeUserStatistic1quarter.get(0).get("min") != null ? activeUserStatistic1quarter.get(0).get("min") : "无");
        response.put("getValidReserveConsultCount1quarterAvg", activeUserStatistic1quarter.get(0).get("avg") != null ? activeUserStatistic1quarter.get(0).get("avg") : "无");
    }

    public List<HashMap<String, Object>> getValidReserveConsultCount(String startDate, String endDate) {


        HashMap<String, String> date = new HashMap<String, String>();
        date.put("startDate", startDate);
        date.put("endDate", endDate);
        List count = OperationComprehensiveDao.getValidReserveConsultCount(date);
        return count;
    }


    public void getValidReserveConsults(HashMap<String, Object> params, HashMap<String, Object> response) {
        List<HashMap<String, Object>> getConsultStatistic = getValidReserveConsult((String) params.get("startDate"), (String) params.get("endDate"));

        response.put("getValidReserveConsultMax", getConsultStatistic.get(0) != null ? getConsultStatistic.get(0).get("max") : "无");
        response.put("getValidReserveConsultMin", getConsultStatistic.get(0) != null ? getConsultStatistic.get(0).get("min") : "无");
        response.put("getValidReserveConsultAvg", getConsultStatistic.get(0) != null ? getConsultStatistic.get(0).get("avg") : "无");
    }

    public List<HashMap<String, Object>> getValidReserveConsult(String startDate, String endDate) {


        HashMap<String, String> date = new HashMap<String, String>();
        date.put("startDate", startDate);
        date.put("endDate", endDate);
        List count = OperationComprehensiveDao.getValidReserveConsult(date);
        return count;
    }

    public List<HashMap<String, Object>> getValidReserveList(String startDate, String endDate, String openid) {
        HashMap<String, String> date = new HashMap<String, String>();
        date.put("startDate", startDate);
        date.put("endDate", endDate);
        date.put("open_id", openid);
        List<HashMap<String, Object>> count = patientRegisterServiceDao.getValidReserveList(date);
        return count;
    }

    //详细路线图页面 张博
    public long getMapLineCount(String startDate,
                                String endDate) throws ParseException {
        // 某时间段进入过路线图页面的用户
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Query queryInLog = new Query();
        queryInLog.addCriteria(new Criteria("title").is("获取地理位置信息").andOperator(
                new Criteria("create_date").gte(sdf.parse(startDate)).lte(
                        sdf.parse(endDate))));
        long inLogCount = mongoDBServiceLog.queryCount(queryInLog);
        return inLogCount;
    }

    //取消预约
    public long getCancelOrderCount(String startDate,
                                    String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Query queryInLog = new Query();
        queryInLog
                .addCriteria(Criteria.where("title").regex("取消预约")
                        .andOperator(
                                new Criteria("create_date").gte(sdf.parse(startDate)).lte(
                                        sdf.parse(endDate))));
        long inLogCount = mongoDBServiceLog.queryCount(
                queryInLog);
        return inLogCount;
    }

    //取消预约成功
    public long getCancelOrderVictoryCount(String startDate,
                                           String endDate) throws ParseException {
        // 某时间段进入过路线图页面的用户
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Query queryInLog = new Query();
        queryInLog
                .addCriteria(Criteria.where("title").is("取消预约成功")
                        .andOperator(
                                new Criteria("create_date").gte(sdf.parse(startDate)).lte(
                                        sdf.parse(endDate))));
        long inLogCount = mongoDBServiceLog.queryCount(
                queryInLog);
        return inLogCount;
    }

    //访问医院首页的总人数
    public int getVisitCountHospital(String startDate,
                                     String endDate) throws ParseException {
        // 某时间段进入过路线图页面的用户
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Query queryInLog = new Query();
        queryInLog
                .addCriteria(Criteria.where("title").is("获取系统内所有医院")
                        .andOperator(
                                new Criteria("create_date").gte(sdf.parse(startDate)).lte(
                                        sdf.parse(endDate))));
        long inLogCount = mongoDBServiceLog.queryCount(
                queryInLog);


        List<MongoLog> logNormalList = mongoDBServiceLog.queryListDistinct(queryInLog, "open_id");
        return logNormalList.size();
    }

    //时间首页的总人数
    public int getVisitDateFirstPageTitleUser(String startDate,
                                              String endDate) throws ParseException {
        // 某时间段进入过路线图页面的用户
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Query queryInLog = new Query();
        queryInLog
                .addCriteria(Criteria.where("title").is("获取往后七天日期数据")
                        .andOperator(
                                new Criteria("create_date").gte(sdf.parse(startDate)).lte(
                                        sdf.parse(endDate))));
        long inLogCount = mongoDBServiceLog.queryCount(
                queryInLog);


        List<MongoLog> logNormalList = mongoDBServiceLog.queryListDistinct(queryInLog, "open_id");
        return logNormalList.size();
    }

    //我的页面
    public long getMyPageCount(String startDate,
                               String endDate) throws ParseException {
        // 某时间段进入过路线图页面的用户
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Query queryInLog = new Query();
        queryInLog
                .addCriteria(Criteria.where("title").is("我的面板-个人信息-个人信息")
                        .andOperator(
                                new Criteria("create_date").gte(sdf.parse(startDate)).lte(
                                        sdf.parse(endDate))));
        long inLogCount = mongoDBServiceLog.queryCount(
                queryInLog);
        return inLogCount;
    }

    //我的关注
    public long getMyFollowerCount(String startDate,
                                   String endDate) throws ParseException {
        // 某时间段进入过路线图页面的用户
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Query queryInLog = new Query();
        queryInLog
                .addCriteria(Criteria.where("title").is("查询我的关注医生信息")
                        .andOperator(
                                new Criteria("create_date").gte(sdf.parse(startDate)).lte(
                                        sdf.parse(endDate))));
        long inLogCount = mongoDBServiceLog.queryCount(
                queryInLog);
        return inLogCount;
    }

    //我的预约
    public long getMyOrderCount(String startDate,
                                String endDate) throws ParseException {
        // 某时间段进入过路线图页面的用户
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Query queryInLog = new Query();
        queryInLog
                .addCriteria(Criteria.where("title").is("进入我的预约版块")
                        .andOperator(
                                new Criteria("create_date").gte(sdf.parse(startDate)).lte(
                                        sdf.parse(endDate))));
        long inLogCount = mongoDBServiceLog.queryCount(
                queryInLog);
        return inLogCount;
    }

    //用户详细行为记录
    public List getUserFullToDoList(String startDate,
                                    String endDate, String openid) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Query queryInLog = new Query();
        if (openid != null && !openid.equals("")) {
            queryInLog
                    .addCriteria(Criteria.where("open_id").is(openid)
                            .andOperator(
                                    new Criteria("create_date").gte(sdf.parse(startDate)).lte(
                                            sdf.parse(endDate))));
            queryInLog.with(new Sort(new Order(Direction.ASC, "create_date")));
        } else {
            queryInLog
                    .addCriteria(new Criteria("create_date").gte(sdf.parse(startDate)).lte(
                            sdf.parse(endDate)));
            queryInLog.with(new Sort(new Order(Direction.ASC, "create_date")));
        }
        List list = mongoDBServiceLog.queryList(queryInLog);
        return list;
    }
}
