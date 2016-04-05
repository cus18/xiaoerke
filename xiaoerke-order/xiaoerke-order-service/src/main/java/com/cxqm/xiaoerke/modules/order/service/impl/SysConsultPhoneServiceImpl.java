package com.cxqm.xiaoerke.modules.order.service.impl;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.modules.order.dao.SysConsultPhoneServiceDao;
import com.cxqm.xiaoerke.modules.order.service.SysConsultPhoneService;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangbaowei on 16/3/21.
 */
@Service
public class SysConsultPhoneServiceImpl implements SysConsultPhoneService {

    @Autowired
    SysConsultPhoneServiceDao sysConsultPhoneServiceDao;


    public Map<String, Object> getDoctorConsultDate(HashMap<String,Object> dataMap) {

        Map<String, Object> response = new HashMap<String, Object>();
        List<HashMap<String, Object>> resultList =  sysConsultPhoneServiceDao.getConsultDateList(dataMap);

        List<HashMap<String, Object>> consultDateList = new LinkedList<HashMap<String, Object>>();
        if(resultList != null && !resultList.isEmpty()){
            for(HashMap<String,Object> map:resultList){
                HashMap<String,Object> consultDate = new HashMap<String, Object>();
                consultDate.put("date",(String)map.get("date"));
                consultDateList.add(consultDate);
            }
        }
        response.put("dateList",consultDateList);

        return response;
    }

    @Override
    public Integer getCount() {
        return sysConsultPhoneServiceDao.finCountOfService();
    }

    @Override
    public Map<String, Object> doctorConsultPhoneOfDay(Map<String, Object> params) {
        HashMap<String, Object> response = new HashMap<String, Object>();

        String doctorId = (String) params.get("doctorId");
        Date date = DateUtils.formatDate(params);

        // 根据日期得到周几
        String DateToStr = DateUtils.DateToStr(date);
        int week = 0;
        try {
            week = DateUtils.dayForWeek(DateToStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String dayWeek = DateUtils.getDayWeek(week);

        StringBuffer bf = new StringBuffer();
        bf.append(DateToStr.substring(0, 10));
        bf.append("  " + dayWeek).toString();
        response.put("date", bf.toString());

        HashMap<String, Object> dataInfo = new HashMap<String, Object>();
        dataInfo.put("doctorId", doctorId);
        dataInfo.put("date", date);
        List<HashMap<String, Object>> resultList = sysConsultPhoneServiceDao.findConsultPhoneTimeListByDoctorAndDate(dataInfo);

        // 记录日志 TODO：
//		LogUtils.saveLog(Servlets.getRequest(), "00000030" ,"医生主键："+ doctorId
//				+ "date:" + date);
        DateFormat formart = new SimpleDateFormat("hh:mm");
        List<HashMap<String, Object>> consultPhoneTimeList = new LinkedList<HashMap<String, Object>>();
        if(resultList != null && !resultList.isEmpty()){
            for(HashMap<String, Object> map:resultList){
                HashMap<String, Object> consultPhoneTime = new HashMap<String, Object>();
                consultPhoneTime.put("price", (String) map.get("price"));
                consultPhoneTime.put("id",(Integer)map.get("id"));
                consultPhoneTime.put("begin_time",(String)map.get("begin_time"));
                try {
                    Date start = formart.parse((String)map.get("begin_time"));
                    Date now = formart.parse(formart.format(new Date()));

                    if(date.getTime()>=new Date().getTime()){
                        consultPhoneTime.put("state",(String)map.get("state"));
                    }else{
                        if(start.getTime()>now.getTime()){
                            consultPhoneTime.put("state",(String)map.get("state"));
                        }else{
                            consultPhoneTime.put("state","1");
                        }

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                consultPhoneTime.put("end_time", (String) map.get("end_time"));

                consultPhoneTime.put("serviceType",(String)map.get("serviceType"));
                consultPhoneTimeList.add(consultPhoneTime);
            }
        }

        response.put("consultPhoneTimeList", consultPhoneTimeList);

        return response;
    }

    /**
     * 返回一个医生指定日期的号源
     */
    @Override
    public JSONObject getRegisterTime(String doctorId , String date) {
        // TODO Auto-generated method stub
        HashMap<String, Object> dataInfo = new HashMap<String, Object>();
        dataInfo.put("doctorId", doctorId);
        dataInfo.put("date", date);
        List<HashMap<String, Object>> consultPhoneTimeList = sysConsultPhoneServiceDao.findConsultPhoneTimeListByDoctorAndDate(dataInfo);

        return null;
    }
}
