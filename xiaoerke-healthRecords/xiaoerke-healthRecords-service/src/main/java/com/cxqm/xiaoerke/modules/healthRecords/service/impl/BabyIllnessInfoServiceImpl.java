package com.cxqm.xiaoerke.modules.healthRecords.service.impl;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.modules.healthRecords.dao.BabyIllnessInfoDao;
import com.cxqm.xiaoerke.modules.healthRecords.service.BabyIllnessInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenxiaoqiong
 * @version 2016/4/14
 */
@Service
@Transactional(readOnly = false)
public class BabyIllnessInfoServiceImpl implements BabyIllnessInfoService {

    @Autowired
    BabyIllnessInfoDao babyIllnessInfoDao;

    @Override
    public Map<String, Object> getIllnessInfoBySysPhoneConsultId(Integer sys_phoneConsult_id) {
        Map<String, Object> searchMap = new HashMap<String, Object>();
        searchMap.put("sys_phoneConsult_id",sys_phoneConsult_id);
        Map<String, Object> illnessMap = babyIllnessInfoDao.getIllnessDetail(searchMap);

        Map<String, Object> response = new HashMap<String, Object>();
        if(illnessMap != null){
            response.put("name", illnessMap.get("name"));
            response.put("content", illnessMap.get("illness"));

            Date birthday = (Date) illnessMap.get("birthday");
            int day = (int) DateUtils.getDistanceOfTwoDate(birthday,new Date());
            String age = "";
            int year = day/365;
            if(year > 0){
                age += year+"岁";
            }
            day = day % 365;
            int month = day/30;
            if(month > 0){
                age += month+"个月";
            }
            day = day % 30;
            if(day > 0){
                age += day+"天";
            }
            response.put("age",age);
        }
        return response;
    }
}
