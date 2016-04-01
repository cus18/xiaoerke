package com.cxqm.xiaoerke.modules.operation.service;

import com.cxqm.xiaoerke.common.bean.WechatRecord;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorCaseVo;
import com.cxqm.xiaoerke.modules.sys.entity.MongoLog;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangbaowei on 15/12/16.
 */
public interface OperationHandleService {
    //获取医生的基本信息
    List<HashMap> findDoctorByInfo(HashMap<String, Object> hashMap);


    Map<String, Object> getLogInfoByLogContent(Map<String, Object> params);

    int CreateDoctor(HashMap<String, Object> hashMap);

    //往sys_location表里插入增加数据
    void insertSysDoctorLocation(HashMap<String, Object> hashMap);

    //插入号源信息
    void insertsysRegisterService(HashMap<String, Object> hashMap);

    //插入号源信息(测试)
    void insertSysRegisterServiceTest(HashMap<String, Object> hashMap);

    //查询当前地址在sys_doctor_location表中是否存在
    String findDoctorExistLocation(HashMap<String, Object> hashMap);

    //根据医院名称查询hospitalId
    String findhospitalId(String hospitalName);

    //根据手机号查询doctorId
    String findDoctorIdByPhone(String phone);

    String findDoctorIdByname(String doctorName);

    //根据level_2查询sys_illness主键
    String findIllnessIdBylevel2(String level_2);

    //批量插入疾病信息
    void insertIllness(List<HashMap<String, Object>> arrayList);

    //批量插入医生与疾病关联信息表数据
    void insertIllnessRelation(List<HashMap<String, Object>> arrayList);

    //批量插入医院信息
    void insertHospitalList(List<HashMap<String, Object>> arrayList);

    //根据patientRegisterServiceId查询sys_register_service和patient_register_service表信息
    HashMap<String, Object> findSysRegisterServiceByPRSId(HashMap<String, Object> hashMap);

    void prepareOperationStatisticData();

    String getNickName(String openid);

    List<HashMap<String, Object>> getOverallStatisticData(HashMap<String, Object> hashMap);

    List<HashMap<String, Object>> getTuiStatisticData(HashMap<String, Object> hashMap);

    List<WechatAttention> getQDStatisticData(HashMap<String, Object> hashMap);

    List<WechatAttention> getQDCancelStatisticData(HashMap<String, Object> hashMap);

    List<WechatAttention> getQDMarketerData(HashMap<String, Object> hashMap);

    int getUserConsultTimes(HashMap<String, Object> hashMap);

    void getUserStatisticInfo(HashMap<String, Object> params, HashMap<String, Object> response);

    void getDoctorStatisticInfo(HashMap<String, Object> params, HashMap<String, Object> response);

    void getAppointmentStatisticInfo(HashMap<String, Object> params, HashMap<String, Object> response);

    void getConsultStatisticInfo(HashMap<String, Object> params, HashMap<String, Object> response);

    void getNormalUserStatistic(HashMap<String, Object> params, HashMap<String, Object> response);

    void getServiceUseStatistic(HashMap<String, Object> params, HashMap<String, Object> response);

    //有效咨询用户  zdl
    void getValidConsultStatistic(HashMap<String, Object> params, HashMap<String, Object> response);

    //有效咨询频次 zdl 最少 最多 平均
    void getFrequencyValidConsult(HashMap<String, Object> params, HashMap<String, Object> response);

    //咨询激活时长
    void getDateValidConsult(HashMap<String, Object> params, HashMap<String, Object> response);

    List<Integer> getDateValidConsultCount(String startDate, String endDate);

    List<Integer> getFrequencyValidConsultCount(String startDate, String endDate);

    int activeUserStatistic(String startDate, String endDate);

    List<String> getZhengYuQiaoUser(String startDate, String endDate);

    List<WechatRecord> getWechatRecordsUser(String startDate, String endDate, SimpleDateFormat sdf);

    List<MongoLog> getMongoAppointUser(String startDate, String endDate, SimpleDateFormat sdf);

    //获取访问咨询频道总人数
    int getConsultUser(HashMap<String, Object> searchMap);

    //有效预约用户  zhangbo
    void getcountValidReserve(HashMap<String, Object> params, HashMap<String, Object> response);

    // 沉寂用户 zhangbo
    void getcountStillnessUser(HashMap<String, Object> params,
                               HashMap<String, Object> response);

    void getValidReserveConsultCounts(HashMap<String, Object> params, HashMap<String, Object> response);

    List<HashMap<String, Object>> getValidReserveConsultCount(String startDate, String endDate);

    void getValidReserveConsults(HashMap<String, Object> params, HashMap<String, Object> response);

    List<HashMap<String, Object>> getValidReserveConsult(String startDate, String endDate);

    List<HashMap<String, Object>> getValidReserveList(String startDate, String endDate, String openid);

    //详细路线图页面 张博
    long getMapLineCount(String startDate,
                         String endDate) throws ParseException;

    //取消预约
    long getCancelOrderCount(String startDate,
                             String endDate) throws ParseException;

    //取消预约成功
    long getCancelOrderVictoryCount(String startDate,
                                    String endDate) throws ParseException;

    //访问医院首页的总人数
    int getVisitCountHospital(String startDate,
                              String endDate) throws ParseException;

    //时间首页的总人数
    int getVisitDateFirstPageTitleUser(String startDate,
                                       String endDate) throws ParseException;

    //我的页面
    long getMyPageCount(String startDate,
                        String endDate) throws ParseException;

    //我的关注
    long getMyFollowerCount(String startDate,
                            String endDate) throws ParseException;

    //我的预约
    long getMyOrderCount(String startDate,
                         String endDate) throws ParseException;

    //用户详细行为记录
    List getUserFullToDoList(String startDate,
                             String endDate, String openid) throws ParseException;

	void saveDoctorCase(DoctorCaseVo doctorCaseVo);
}
