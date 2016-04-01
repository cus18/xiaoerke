package com.cxqm.xiaoerke.modules.operation.service.impl;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.operation.dao.DataStatisticsDao;
import com.cxqm.xiaoerke.modules.operation.dao.OperationComprehensiveDao;
import com.cxqm.xiaoerke.modules.operation.service.DataStatisticService;
import com.cxqm.xiaoerke.modules.order.entity.PatientRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.service.PatientRegisterService;
import com.cxqm.xiaoerke.modules.order.service.RegisterService;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;
import com.cxqm.xiaoerke.modules.sys.entity.HospitalVo;
import com.cxqm.xiaoerke.modules.sys.service.DoctorInfoService;
import com.cxqm.xiaoerke.modules.sys.service.HospitalInfoService;
import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import com.cxqm.xiaoerke.modules.wechat.entity.DoctorAttentionVo;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;

import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import net.sf.ehcache.search.impl.OrderComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by 得良 on 2015/6/17.
 */
@Service
@Transactional(readOnly = false)
public class DataStatisticServiceImpl implements DataStatisticService {

    @Autowired
    private DataStatisticsDao dataStatisticsDao;

    @Autowired
    private DoctorInfoService doctorInfoService;

    @Autowired
    private PatientRegisterService patientRegisterService;

    @Autowired
    private HospitalInfoService hospitalInfoService;

    @Autowired
    private WechatAttentionService wechatAttentionService;

    @Autowired
    private MongoDBService<WechatAttention> mongoDBServiceAttention;
    
    @Autowired
    private OperationComprehensiveDao OperationComprehensiveDao;

    @Autowired
    private RegisterService registerService;


    //获取统计数据
    @Override
    public List<HashMap<String, Object>> findStatistics(HashMap<String, Object> hashMap) {
        List<HashMap<String, Object>> resultList = dataStatisticsDao.findStatistics(hashMap);
        if (resultList != null && resultList.size() > 0) {
            return resultList;
        }
        return null;
    }

    //获取累计关注数
    @Override
    public String findStatisticsConcern() {
        String concern = dataStatisticsDao.findStatisticsConcern() - dataStatisticsDao.findStatisticsCancelConcern() - 152 + "";
        if (StringUtils.isNotNull(concern)) {
            return concern;
        }
        return null;
    }

    //新增医生数
    @Override
    public String findCountDoctorNumber(HashMap<String, Object> hashMap) {
        String doctorNumber = doctorInfoService.findCountDoctorNumber(hashMap) + "";
        if (StringUtils.isNotNull(doctorNumber)) {
            return doctorNumber;
        }
        return null;
    }

    //系统内总的医生数
    @Override
    public String findCountDoctorCountNmuber() {
        String doctorCountNumber = doctorInfoService.findCountDoctorCountNmuber() + "";
        if (StringUtils.isNotNull(doctorCountNumber)) {
            return doctorCountNumber;
        }
        return null;
    }


    //新增订单数
    @Override
    public String findNewAddOrderNmuber(HashMap<String, Object> hashMap) {
        String orderNumber = OperationComprehensiveDao.findNewAddOrderNmuber(hashMap) + "";
        if (StringUtils.isNotNull(orderNumber)) {
            return orderNumber;
        }
        return null;
    }

    //不成功订单数
    @Override
    public String findUnSuccessOrderNumber(HashMap<String, Object> hashMap) {
        String unOrderNumber = OperationComprehensiveDao.findUnSuccessOrderNumber(hashMap) + "";
        if (StringUtils.isNotNull(unOrderNumber)) {
            return unOrderNumber;
        }
        return null;
    }

    //累计成功订单数
    @Override
    public String findCountOrderNumber() {
        String OrderCount = OperationComprehensiveDao.findCountOrderNumber() + "";
        if (StringUtils.isNotNull(OrderCount)) {
            return OrderCount;
        }
        return null;
    }

    //获取所有当天所有预约信息（包括openId）
    @Override
    public List<HashMap<String, Object>> findAllAppointByDate(HashMap<String, Object> hashMap) {
        List<HashMap<String, Object>> resultList = dataStatisticsDao.findAllAppointByDate(hashMap);
        if (resultList != null && resultList.size() > 0) {
            return resultList;
        }
        return null;
    }

    //获取所有当天所有咨询信息(包括openId)
    @Override
    public List<HashMap<String, Object>> findAllConsultByDate(HashMap<String, Object> hashMap) {
        List<HashMap<String, Object>> resultList = dataStatisticsDao.findAllConsultByDate(hashMap);
        if (resultList != null && resultList.size() > 0) {
            return resultList;
        }
        return null;
    }

    //获取访问郑玉巧说的所有用户的openid/create_date
    @Override
    public List<HashMap<String, Object>> findAllReaderZhengYuQiao(HashMap<String, Object> hashMap) {
        List<HashMap<String, Object>> resultList = dataStatisticsDao.findAllReaderZhengYuQiao(hashMap);
        if (resultList != null && resultList.size() > 0) {
            return resultList;
        }
        return null;
    }

    //此预约如果是老用户，返回0，新用户返回1
    @Override
    public Boolean judgeAppIsNew(HashMap<String, Object> hashMap) {
        int appFlag = dataStatisticsDao.judgeAppIsNew(hashMap);
        if (appFlag > 0) {
            return true;
        }
        return false;
    }

    //此咨询如果是老用户，返回0，新用户返回1
    @Override
    public Boolean judgeconIsNew(HashMap<String, Object> hashMap) {
        int appFlag = dataStatisticsDao.judgeAppIsNew(hashMap);
        if (appFlag > 0) {
            return true;
        }
        return false;
    }

    //医院首页-医院医生列表页面
    @Override
    public int gethospitalDoctorListUser(String startDate, String endDate, String searchlist) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("startDate", startDate);
        hashMap.put("endDate", endDate);
        hashMap.put("searchlist",searchlist);
        return dataStatisticsDao.getZhengYuQiaoShuoUser(hashMap).size();
    }

    //判断此用户之前是否咨询过
    @Override
    public Boolean judgeNewConIsExistWeChat(HashMap<String, Object> hashMap) {
        int addFlag = dataStatisticsDao.judgeNewConIsExistWeChat(hashMap);
        if (addFlag > 0) {
            return true;
        }
        return false;
    }

    //查询总活跃用户数
    @Override
    public int findSumActive(HashMap<String, Object> hashMap){
        return dataStatisticsDao.findSumActive(hashMap);
    }

    //获取已咨询人数
    @Override
    public int alreadyConNumber() {
        return dataStatisticsDao.alreadyConNumber();
    }

    //保存统计数据
    @Override
    public void saveDataStatistics(HashMap<String, Object> saveMap) {
        dataStatisticsDao.saveDataStatistics(saveMap);
    }

    //判断此用户是否既预约又咨询
    @Override
    public Boolean judgeNewOrOldAppCon(HashMap<String, Object> hashMap) {
        int appCon = dataStatisticsDao.judgeNewOrOldAppCon(hashMap);
        if (appCon > 0) {
            return true;
        }
        return false;
    }

    //获取所有有订单存在的医院
    @Override
    public List<HospitalVo> findAllOrderHospital(HashMap<String, Object> newMap) {
        List<HospitalVo> resultList = hospitalInfoService.findAllOrderHospital(newMap);
        return resultList;
    }

    //根据医院id查询该医院下有订单存在的所有医生
    @Override
    public List<DoctorVo> findAllOrderDoctorList(HashMap<String, Object> seachMapd) {
        List<DoctorVo> resultList = doctorInfoService.findAllOrderDoctorList(seachMapd);
        if (resultList != null && resultList.size() > 0) {
            return resultList;
        }
        return null;
    }

    //获取该医生这一段时间内的所有成功的订单
    @Override
    public List<PatientRegisterServiceVo> findAllPatientRegisterService(HashMap<String, Object> seachMap) {
        List<PatientRegisterServiceVo> resultList = patientRegisterService.findAllPatientRegisterService(seachMap);
        if (resultList != null && resultList.size() > 0) {
            return resultList;
        }
        return null;
    }

    //医生信息统计 zdl
    @Override
    public void getDoctorStatisticData(HashMap<String, Object> params, HashMap<String, Object> response) {

        List<DoctorAttentionVo> responseList = new ArrayList<DoctorAttentionVo>();

        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");

        //获取所有医生的doctorName,openid,phone
        HashMap<String, Object> newHash = new HashMap<String, Object>();
        List<HashMap<String, Object>> doctors = doctorInfoService.findDoctorByDoctorId(newHash);
//        -----------------------不可以获取到医生的openid暂时用这个------------------------------
        for (Map doctorMap : doctors) {

            if (StringUtils.isNotNull((String) doctorMap.get("openid"))) {

                HashMap<String, Object> searchMap = new HashMap<String, Object>();

                if (doctorMap != null && doctorMap.size() > 0) {

                    searchMap.put("openid", doctorMap.get("openid"));
                    searchMap.put("startDate", startDate);
                    searchMap.put("endDate", endDate);
                    searchMap.put("id", doctorMap.get("id"));

                    //根据doctorId查询医生姓名、医生电话、医院、科室
                    DoctorAttentionVo doctorAttentionVo = wechatAttentionService.findDoctorAttentionVoInfoNoOpenId(searchMap);

                    if (doctorAttentionVo != null && doctorAttentionVo.getDate() != null) {
                        doctorAttentionVo.setDateDisplay(DateUtils.formatDateTime(doctorAttentionVo.getDate()));
                    }
                    //根据doctorId查询这一段时间该医生“号源总数”
                    int registerServiceCount = registerService.findDoctorRegisterServiceByData(searchMap);

                    //根据doctorId查询这一段时间该医生“已预约号源数”
                    int appointNumberAlready = doctorInfoService.findDoctorAlreadyAppointNumber(searchMap);

                    //根据doctorId查询这一段时间该医生“剩余预约号源数”
                    int appointNumber = doctorInfoService.findDoctorCanAppointNumber(searchMap);
                    //组合
                    if (doctorAttentionVo != null) {
                        doctorAttentionVo.setAppointNumber(String.valueOf(appointNumber) == null ? "无" : String.valueOf(appointNumber));
                        doctorAttentionVo.setAppointNumberAlready(String.valueOf(appointNumberAlready) == null ? "无" : String.valueOf(appointNumberAlready));
                        doctorAttentionVo.setRegisterServiceCount(String.valueOf(registerServiceCount) == null ? "无" : String.valueOf(registerServiceCount));
                        if ("".equals(doctorAttentionVo.getMarketer()) || doctorAttentionVo.getMarketer() == null) {
                            doctorAttentionVo.setMarketer("无");
                        }
                        if ("".equals(doctorAttentionVo.getNickname()) || doctorAttentionVo.getNickname() == null) {
                            doctorAttentionVo.setNickname("无");
                        }
                        if ("".equals(doctorAttentionVo.getOpenid()) || (doctorAttentionVo.getOpenid() == null)) {
                            doctorAttentionVo.setOpenid("无");
                        }
                        if ("".equals(doctorAttentionVo.getDate()) || (doctorAttentionVo.getDate() == null)) {
                            doctorAttentionVo.setDate(new Date());
                        }
                        responseList.add(doctorAttentionVo);
                    }
                }
            }
        }
        response.put("doctorInfo", responseList);
    }

    //用户行为统计分析
    @Override
    public void getUserActionStatistic(HashMap<String, Object> params, Map<String, Object> response) {

        List<HashMap<String, Object>> responseList = new ArrayList<HashMap<String, Object>>();

        //查询所有的openid
        Query queryDate = new Query();
        List<String> openidList = mongoDBServiceAttention.queryStringListDistinct(queryDate, "openid");

        //遍历所有关注用户
        for (String s : openidList) {
            HashMap<String, Object> conMap = new HashMap<String, Object>();
            //用来存放查询条件
            HashMap<String, Object> searchMap = new HashMap<String, Object>();
            searchMap.put("openid", "o3_NPwjH9_DCRw0YlBCOLk7bPvMw");//s
            //用户行为统计
            List<HashMap<String, Object>> resultList = dataStatisticsDao.findUserActionStatistic(searchMap);
            conMap.put("nickname", resultList.get(0).get("nickname") == null ? "无" : resultList.get(0).get("nickname"));//微信名
            conMap.put("openid", s == null ? "无" : s);                         //openid
            conMap.put("marketer", resultList.get(0).get("marketer") == null ? "无" : resultList.get(0).get("marketer"));//来源
            conMap.put("date", resultList.get(0).get("date") == null ? "无" : resultList.get(0).get("date"));//关注时间

            //    doctorCount  获取医院医生
            //    cancleAppcount 用户取消预约
            //    allhospitalcount  获取系统内所有医院
            //    concerndoctror 关注医生
            //    registerService7Adress 搜索医生7天内的出诊位置信息
            //    registerService7  根据医生的出诊地点获取医生7天内的出诊信息
            //    doctorInfo  某医生详细信息
            //    allDepartment 获取所有科室
            //    doctorServiceInfo 获取某个医生的某天的加号信息
            //    doctorServiceDetailInfo 获取某个医生的某个号的详细信息
            //    serviceTraficInfo  号源交通信息
            //    userShareInfo       用户分享详情
            //    userShare   用户分享
            //    illness2_major_Doctor 获取二级分类疾病下的专业医生
            //    personalHomepage  个人中心主页信息
            //    myselfAppoint  进入我的预约版块
            //    doctorServiceOneDay 获取某个医生的某天的加号信息
            //    canAppointDoctorByDate  获取某个预约日期下的可预约医生
            //    level1_classify 获取疾病一级分类
            //    level1_level2 获取一级疾病下的二级分类
            //    level2_classify_hospital 获取二级分类疾病下的所有关联医院
            //    level2_classify_doctor 获取二级分类疾病下的专业医生
            //    department_doctor 获取医院的某个科室的医生
            //    searchMySelfConcernDoctor 查询我的关注医生信息
            //    canAppointHospitalByDate 获取某个预约日期下的可预约的医院
            //    evaluateDoctor 用户评价医生
            //    enterDetail 进入待评价，待支付，待分享列表信息
            //    myselfAppointDetail 获取个人的预约信息详情
            //    registerRemind 用户预约挂号提醒
            int doctorCount = 0;
            int cancleAppcount = 0;
            int concerndoctro = 0;
            int registerService7Adress = 0;
            int registerService7 = 0;
            int doctorInfo = 0;
            int allDepartment = 0;
            int doctorServiceInfo = 0;
            int doctorServiceDetailInfo = 0;
            int serviceTraficInfo = 0;
            int userShareInfo = 0;
            int userShare = 0;
            int illness2_major_Doctor = 0;
            int personalHomepage = 0;
            int myselfAppoint = 0;
            int doctorServiceOneDay = 0;
            int canAppointDoctorByDate = 0;
            int level1_classify = 0;
            int level1_level2_classify = 0;
            int level2_classify_hospital = 0;
            int level2_classify_doctor = 0;
            int department_doctor = 0;
            int searchMySelfConcernDoctor = 0;
            int canAppointHospitalByDate = 0;
            int evaluateDoctor = 0;
            int enterDetail = 0;
            int myselfAppointDetail = 0;
            int registerRemind = 0;

            for (Map hashMap : resultList) {
                String title = (String) hashMap.get("title");
                String count = hashMap.get("count").toString();
                if ("00000043".equals(title)) {
                    conMap.put("allhospitalcount", hashMap.get("count"));//获取系统内所有医院
                } else if (title.length() > 5 && "00000076".equals(title.substring(0, 6))) {
                    doctorCount = doctorCount + Integer.parseInt(count);
                } else if (title.length() > 5 && "00000014".equals(title.substring(0, 6))) {
                    cancleAppcount = cancleAppcount + Integer.parseInt(count);
                } else if (title.length() > 3 && "00000011".equals(title.substring(0, 4))) {
                    concerndoctro = concerndoctro + Integer.parseInt(count);
                } else if (title.length() > 13 && "00000074".equals(title.substring(0, 14))) {
                    registerService7Adress = registerService7Adress + Integer.parseInt(count);
                } else if (title.length() > 20 && "00000031".equals(title.substring(0, 21))) {
                    registerService7 = registerService7 + Integer.parseInt(count);
                } else if (title.length() > 10 && "00000073".equals(title.substring(0, 11))) {
                    doctorInfo = doctorInfo + Integer.parseInt(count);
                } else if ("00000044".equals(title)) {
                    allDepartment = allDepartment + Integer.parseInt(count);
                } else if (title.length() > 13 && "00000030".equals(title.substring(0, 14))) {
                    doctorServiceInfo = doctorServiceInfo + Integer.parseInt(count);
                } else if (title.length() > 14 && "00000029".equals(title.substring(0, 15))) {
                    doctorServiceDetailInfo = doctorServiceDetailInfo + Integer.parseInt(count);
                } else if (title.length() > 8 && "00000036".equals(title.substring(0, 9))) {
                    serviceTraficInfo = serviceTraficInfo + Integer.parseInt(count);
                } else if (title.length() > 9 && "00000050".equals(title.substring(1, 10))) {
                    userShareInfo = userShareInfo + Integer.parseInt(count);
                } else if ("00000013".equals(title)) {
                    userShare = Integer.parseInt(count);
                } else if (title.length() > 13 && "00000077".equals(title.substring(0, 14))) {
                    illness2_major_Doctor = Integer.parseInt(count);
                } else if ("00000080".equals(title)) {
                    personalHomepage = Integer.parseInt(count);
                } else if ("00000097".equals(title)) {
                    myselfAppoint = Integer.parseInt(count);
                } else if ("00000030".equals(title)) {
                    doctorServiceOneDay = Integer.parseInt(count);
                } else if ("00000032".equals(title)) {
                    canAppointDoctorByDate = Integer.parseInt(count);
                } else if ("00000046".equals(title)) {
                    level1_classify = Integer.parseInt(count);
                } else if ("00000047".equals(title)) {
                    level1_level2_classify = Integer.parseInt(count);
                } else if (title.length() > 15 && "00000045".equals(title.substring(0, 16))) {
                    level2_classify_hospital = Integer.parseInt(count);
                } else if (title.length() > 13 && "00000077".equals(title.substring(0, 14))) {
                    level2_classify_doctor = Integer.parseInt(count);
                } else if (title.length() > 11 && "00000075".equals(title.substring(0, 12))) {
                    department_doctor = Integer.parseInt(count);
                } else if (title.length() > 11 && "00000075".equals(title.substring(0, 12))) {
                    searchMySelfConcernDoctor = Integer.parseInt(count);
                } else if (title.length() > 15 && "00000034".equals(title.substring(0, 16))) {
                    canAppointHospitalByDate = Integer.parseInt(count);
                } else if ("00000012".equals(title)) {
                    evaluateDoctor = Integer.parseInt(count);
                } else if ("00000099".equals(title)) {
                    enterDetail = Integer.parseInt(count);
                } else if (title.length() > 10 && "00000017".equals(title.substring(0, 11))) {
                    myselfAppointDetail = Integer.parseInt(count);
                } else if (title.length() > 7 && "00000095".equals(title.substring(0, 8))) {
                    registerRemind = Integer.parseInt(count);
                }
                conMap.put("doctorCount", doctorCount);//获取医院医生
                conMap.put("cancleAppcount", cancleAppcount);//用户取消预约
                conMap.put("concerndoctror", concerndoctro);//关注医生
                conMap.put("registerService7Adress", registerService7Adress);//搜索医生7天内的出诊位置信息
                conMap.put("registerService7", registerService7);//根据医生的出诊地点获取医生7天内的出诊信息
                conMap.put("doctorInfo", doctorInfo);//某医生详细信息
                conMap.put("allDepartment", allDepartment);//获取所有科室
                conMap.put("doctorServiceInfo", doctorServiceInfo);//获取某个医生的某天的加号信息
                conMap.put("doctorServiceDetailInfo", doctorServiceDetailInfo);//获取某个医生的某个号的详细信息
                conMap.put("serviceTraficInfo", serviceTraficInfo);//号源的交通信息
                conMap.put("userShareInfo", userShareInfo);//用户分享详情
                conMap.put("userShare", userShare);//用户分享详情
                conMap.put("illness2_major_Doctor", illness2_major_Doctor);//获取二级分类疾病下的专业医生
                conMap.put("personalHomepage", personalHomepage);//获取个人中心主页的信息
                conMap.put("myselfAppoint", myselfAppoint);//进入我的预约版块
                conMap.put("doctorServiceOneDay", doctorServiceOneDay);//获取某个医生的某天的加号信息
                conMap.put("canAppointDoctorByDate", canAppointDoctorByDate);//获取某个预约日期下的可预约医生
                conMap.put("level1_classify", level1_classify);//获取疾病一级分类
                conMap.put("level1_level2_classify", level1_level2_classify);//获取疾病一级分类
                conMap.put("level2_classify_hospital", level2_classify_hospital);//获取二级分类疾病下的所有关联医院
                conMap.put("level2_classify_doctor", level2_classify_doctor);//获取二级分类疾病下的专业医生
                conMap.put("department_doctor", department_doctor);//获取医院的某个科室的医生
                conMap.put("searchMySelfConcernDoctor", searchMySelfConcernDoctor);//查询我的关注医生信息
                conMap.put("canAppointHospitalByDate", canAppointHospitalByDate);//获取某个预约日期下的可预约的医院
                conMap.put("evaluateDoctor", evaluateDoctor);//用户评价医生
                conMap.put("enterDetail", enterDetail); //进入待评价，待支付，待分享列表信息
                conMap.put("myselfAppointDetail", myselfAppointDetail); //进入待评价，待支付，待分享列表信息
                conMap.put("registerRemind", registerRemind); //进入待评价，待支付，待分享列表信息
            }
            responseList.add(conMap);
        }
        response.put("result", responseList);
    }

    //页面统计
    @Override
    public void getPageStatistic(HashMap<String, Object> params, Map<String, Object> response) {

    }

    //获取用户分析中的次数   张博
    @Override
    public int getTotalNum(String startDate, String endDate, String searchlist) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("startDate", startDate);
        hashMap.put("endDate", endDate);
        hashMap.put("searchtitle",searchlist);
        return dataStatisticsDao.getTotalNum(hashMap);
    }

    @Override
    public int findStatisticsCancelConcern() {
        return dataStatisticsDao.findStatisticsCancelConcern();
    }
}
