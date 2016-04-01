package com.cxqm.xiaoerke.modules.operation.dao;

import java.util.HashMap;
import java.util.List;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;

@MyBatisDao
public interface OperationComprehensiveDao {

    int insertBatchUserStatistic(List<HashMap<String, Object>> userLog);

    List<HashMap<String, Object>> getUserConsultListTimes(HashMap<String, Object> Map);

    HashMap<String, Object> getUserOperationStatistic(HashMap<String, Object> data);

    List<HashMap<String, Object>> getStatisticData(HashMap<String, Object> Map);

    List<HashMap<String, Object>> getAppUserListBetweenStartAndEndDate(HashMap<String, Object> Map);

    List<HashMap<String, Object>> getConsultUserListBetweenStartAndEndDate(HashMap<String, Object> Map);

    List<HashMap<String, Object>> getOverallStatisticData(HashMap<String, Object> Map);

    List<HashMap<String, Object>> getTuiStatisticData(HashMap<String, Object> Map);

    //新增订单数
    int findNewAddOrderNmuber(HashMap<String, Object> hashMap);

    //不成功订单数
    int findUnSuccessOrderNumber(HashMap<String, Object> hashMap);

    //累计成功订单数
    int findCountOrderNumber();

    int getUserConsultTimes(HashMap<String, Object> Map);

    int getAppointmentNum(HashMap paramMap);

    int getUserAppointmentNum(HashMap paramMap);

    List<WechatAttention> getQDStatisticData(HashMap paramMap);

    List<WechatAttention> getQDMarketerData(HashMap paramMap);

    List<WechatAttention> getQDCancelStatisticData(HashMap paramMap);

    List<DoctorVo> getDoctorStatisticInfo(HashMap paramMap);

    int getConsultStatisticInfo(HashMap paramMap);

    List<HashMap<String, Object>> getValidReserveConsultCount(HashMap paramMap);

    List<HashMap<String, Object>> getValidReserveConsult(HashMap paramMap);

    //渠道统计
    List<HashMap<String, Object>> getChannelStatisticData(HashMap hashMap);

}
