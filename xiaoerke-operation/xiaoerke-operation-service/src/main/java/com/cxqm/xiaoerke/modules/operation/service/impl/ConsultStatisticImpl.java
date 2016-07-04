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
    public List<Integer> getConsultStatistic(HashMap hashmap){
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

}
