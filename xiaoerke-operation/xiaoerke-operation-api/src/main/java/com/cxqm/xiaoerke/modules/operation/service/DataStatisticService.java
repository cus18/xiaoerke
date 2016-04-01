package com.cxqm.xiaoerke.modules.operation.service;

import com.cxqm.xiaoerke.modules.order.entity.PatientRegisterServiceVo;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;
import com.cxqm.xiaoerke.modules.sys.entity.HospitalVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangbaowei on 15/12/16.
 */
public interface DataStatisticService {
    List<HashMap<String, Object>> findStatistics(HashMap<String, Object> hashMap);

    String findStatisticsConcern();

    //新增医生数
    String findCountDoctorNumber(HashMap<String, Object> hashMap);

    //系统内总的医生数
    String findCountDoctorCountNmuber();

    //新增订单数
    String findNewAddOrderNmuber(HashMap<String, Object> hashMap);

    //不成功订单数
    String findUnSuccessOrderNumber(HashMap<String, Object> hashMap);

    //累计成功订单数
    String findCountOrderNumber();

    //获取所有当天所有预约信息（包括openId）
    List<HashMap<String, Object>> findAllAppointByDate(HashMap<String, Object> hashMap);

    //获取所有当天所有咨询信息(包括openId)
    List<HashMap<String, Object>> findAllConsultByDate(HashMap<String, Object> hashMap);

    //获取访问郑玉巧说的所有用户的openid/create_date
    List<HashMap<String, Object>> findAllReaderZhengYuQiao(HashMap<String, Object> hashMap);

    //此预约如果是老用户，返回0，新用户返回1
    Boolean judgeAppIsNew(HashMap<String, Object> hashMap);

    //此咨询如果是老用户，返回0，新用户返回1
    Boolean judgeconIsNew(HashMap<String, Object> hashMap);

    //医院首页-医院医生列表页面
    int gethospitalDoctorListUser(String startDate, String endDate, String searchlist);

    //判断此用户之前是否咨询过
    Boolean judgeNewConIsExistWeChat(HashMap<String, Object> hashMap);

    //查询总活跃用户数
    int findSumActive(HashMap<String, Object> hashMap);

    //获取已咨询人数
    int alreadyConNumber();

    //保存统计数据
    void saveDataStatistics(HashMap<String, Object> saveMap);

    //判断此用户是否既预约又咨询
    Boolean judgeNewOrOldAppCon(HashMap<String, Object> hashMap);

    //获取所有有订单存在的医院
    List<HospitalVo> findAllOrderHospital(HashMap<String, Object> newMap);

    //根据医院id查询该医院下有订单存在的所有医生
    List<DoctorVo> findAllOrderDoctorList(HashMap<String, Object> seachMapd);

    //获取该医生这一段时间内的所有成功的订单
    List<PatientRegisterServiceVo> findAllPatientRegisterService(HashMap<String, Object> seachMap);

    //医生信息统计 zdl
    void getDoctorStatisticData(HashMap<String, Object> params, HashMap<String, Object> response);

    //用户行为统计分析
    void getUserActionStatistic(HashMap<String, Object> params, Map<String, Object> response);

    //页面统计
    void getPageStatistic(HashMap<String, Object> params, Map<String, Object> response);

    //获取用户分析中的次数   张博
    int getTotalNum(String startDate, String endDate, String searchlist);

    int findStatisticsCancelConcern();
}
