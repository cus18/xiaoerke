package com.cxqm.xiaoerke.modules.operation.service.impl;

import com.cxqm.xiaoerke.modules.consult.dao.ConsultStatisticDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultStatisticVo;
import com.cxqm.xiaoerke.modules.operation.service.ConsultStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 咨询统计 实现
 * @author deliang
 * @version 2015-11-05
 */
@Service
@Transactional(readOnly = false)
public class ConsultStatisticImpl implements ConsultStatisticService {

    @Autowired
    private ConsultStatisticDao consultStatisticDao;

    @Override
    public List<Float> getConsultStatistic(HashMap hashmap){
        return consultStatisticDao.getConsultStatistic(hashmap);
    }

    @Override
    public int insertSelective(ConsultStatisticVo record){
        return consultStatisticDao.insertSelective(record);
    }

    @Override
    public List<ConsultStatisticVo> getConsultStatisticList(String startDate, String endDate){
        HashMap<String,String> hashMap = new HashMap<String, String>();
        hashMap.put("startDate",startDate);
        hashMap.put("endDate",endDate);
        return consultStatisticDao.getConsultStatisticList(hashMap);
    }

    @Override
    public List<Map<String, Object>> getConsultDoctorDatalist(Map<String,Object> map){
        List<Map<String, Object>> validateConsultAndFeedBackCountsList = consultStatisticDao.getValidateConsultAndFeedBackCounts(map);
        List<Map<String, Object>> sendHeartPersonAndMoneyCountsList =  consultStatisticDao.getSendHeartPersonAndMoneyCounts(map);
        for (int i = 0; i < validateConsultAndFeedBackCountsList.size(); i++) {
            Map<String, Object> validateMap = validateConsultAndFeedBackCountsList.get(i);
            for (int j = 0; j < sendHeartPersonAndMoneyCountsList.size(); j++) {
                Map<String, Object> sendMap = sendHeartPersonAndMoneyCountsList.get(j);
                if(sendMap.get("date").toString().equalsIgnoreCase(validateMap.get("date").toString())){
                    validateMap.put("sendHeartPersonCount",sendMap.get("sendHeartPersonCount").toString());
                    validateMap.put("sendHeartMoneyCount",sendMap.get("sendHeartMoneyCount").toString());
                }
            }
        }

        return validateConsultAndFeedBackCountsList;
    }

}
