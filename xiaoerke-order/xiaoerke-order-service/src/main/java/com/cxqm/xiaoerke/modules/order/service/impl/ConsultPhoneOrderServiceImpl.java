package com.cxqm.xiaoerke.modules.order.service.impl;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.FrontUtils;
import com.cxqm.xiaoerke.modules.order.dao.ConsultPhoneRegisterServiceDao;
import com.cxqm.xiaoerke.modules.order.dao.PatientRegisterServiceDao;
import com.cxqm.xiaoerke.modules.order.dao.SysConsultPhoneServiceDao;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.entity.OrderServiceVo;
import com.cxqm.xiaoerke.modules.order.entity.SysConsultPhoneServiceVo;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhoneOrderService;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangbaowei on 16/3/18.
 */

@Service
public class ConsultPhoneOrderServiceImpl implements ConsultPhoneOrderService {

    @Autowired
    private SysConsultPhoneServiceDao sysConsultPhoneServiceDao;

    @Autowired
    private ConsultPhoneRegisterServiceDao consultPhoneRegisterServiceDao;

    @Autowired
    private PatientRegisterServiceDao patientRegisterServiceDao;

    //获取预约挂号的号源
    public Map findDoctorAppointmentInfo(String doctorId){
        HashMap<String,Object> resultMap = new HashMap<String, Object>();
        List<SysConsultPhoneServiceVo> list = sysConsultPhoneServiceDao.selectConsultList(doctorId, null);
        resultMap.put("appointment",list);
        return resultMap;
    }

    //根据号源id获取号源的基本详情
    @Override
    public SysConsultPhoneServiceVo getConsultServiceInfo(Integer consultServiceId){
        return sysConsultPhoneServiceDao.selectByPrimaryKey(consultServiceId);
    }

    @Override
    public Map<String, Object> getOrderList(Map<String,Object> params) {
        User user = UserUtils.getUser();
        String userId = user.getId();

        String pageNo = (String) params.get("pageNo");
        String pageSize = (String) params.get("pageSize");
        String status = (String) params.get("status");//
        String type = (String) params.get("type");

        Page<ConsultPhoneRegisterServiceVo> page = FrontUtils.generatorPage(pageNo, pageSize);
        HashMap<String, Object> searchMap = new HashMap<String, Object>();
        if (status == null) {
            searchMap.put("state", null);
        } else {
            searchMap.put("state", status);
        }
        searchMap.put("userId", userId);

        Page<OrderServiceVo> resultPage = null;
        //取数据
        if(type != null && type.equals("ap")){
            //TODO:cxq
        }else if(type != null && type.equals("phone")){
            resultPage = consultPhoneRegisterServiceDao.getPhoneConsultPageList(page, searchMap);
        }else{
            resultPage = consultPhoneRegisterServiceDao.getOrderAllPageList(page, searchMap);
        }

        List<OrderServiceVo> list = resultPage.getList();
        Collections.sort(list);

        //返回数据
        Map<String, Object> response = new HashMap<String, Object>();
        //组装json数据
        response.put("pageNo", resultPage.getPageNo());
        response.put("pageSize", resultPage.getPageSize());
        long tmp = FrontUtils.generatorTotalPage(resultPage);
        response.put("pageTotal", tmp + "");

        List<HashMap<String, Object>> orderList = new ArrayList<HashMap<String, Object>>();
        for(OrderServiceVo order:list){
            HashMap<String,Object> map = new HashMap<String, Object>();
            map.put("doctorName", order.getDoctorName());
            map.put("position", order.getPosition());
            map.put("hospital",order.getHospitalName());

            SimpleDateFormat format = new SimpleDateFormat("MM/dd");
            map.put("date",format.format(order.getDate()));

            SimpleDateFormat format1 = new SimpleDateFormat("HH:ss");
            map.put("time",format1.format(order.getBeginTime()) + "-" + format1.format(order.getEndTime()));

            map.put("status",order.getStatus());
            map.put("classify",order.getClassify());

            map.put("doctorId", order.getDoctorId());
            map.put("orderId", order.getOrderId());
            orderList.add(map);
        }

        response.put("orderList",orderList);

        return response;

    }

    @Override
    public Map<String, Object> getUserOrderPhoneConsultList(Map<String, Object> params) {
        User user = UserUtils.getUser();
        String userId = user.getId();

        String pageNo = (String) params.get("pageNo");
        String pageSize = (String) params.get("pageSize");
        String status = (String) params.get("status");//

        Page<ConsultPhoneRegisterServiceVo> page = FrontUtils.generatorPage(pageNo, pageSize);
        HashMap<String, Object> searchMap = new HashMap<String, Object>();
        if (status == null) {
            searchMap.put("state", null);
        } else {
            searchMap.put("state", status);
        }
        searchMap.put("userId", userId);

        //取数据
        Page<OrderServiceVo> resultPage = consultPhoneRegisterServiceDao.getPhoneConsultPageList(page, searchMap);

        Map<String, Object> response = new HashMap<String, Object>();
        //组装json数据
        response.put("pageNo", resultPage.getPageNo());
        response.put("pageSize", resultPage.getPageSize());
        long tmp = FrontUtils.generatorTotalPage(resultPage);
        response.put("pageTotal", tmp + "");
        List<OrderServiceVo> resultList = resultPage.getList();
        List<HashMap<String,Object>> orderList = new ArrayList<HashMap<String, Object>>();
        for(OrderServiceVo order:resultList){
            HashMap<String,Object> map = new HashMap<String, Object>();
            map.put("doctorName", order.getDoctorName());
            map.put("position", order.getPosition());
            map.put("hospital",order.getHospitalName());

            SimpleDateFormat format = new SimpleDateFormat("MM/dd");
            map.put("date",format.format(order.getDate()));

            SimpleDateFormat format1 = new SimpleDateFormat("HH:ss");
            map.put("time",format1.format(order.getBeginTime()) + "-" + format1.format(order.getEndTime()));

            map.put("status",order.getStatus());
            map.put("classify",order.getClassify());

            map.put("doctorId", order.getDoctorId());
            map.put("orderId", order.getOrderId());
            orderList.add(map);
        }
        response.put("orderList",orderList);

        return response;
    }

    @Override
    public List<HashMap<String, Object>> getOrderPhoneConsultListByTime(String state) {
      return consultPhoneRegisterServiceDao.getOrderPhoneConsultListByTime(state);
    }


}
