package com.cxqm.xiaoerke.modules.order.service.impl;

import com.cxqm.xiaoerke.modules.order.dao.ConsultPhoneRegisterServiceDao;
import com.cxqm.xiaoerke.modules.order.dao.PatientRegisterServiceDao;
import com.cxqm.xiaoerke.modules.order.dao.SysConsultPhoneServiceDao;
import com.cxqm.xiaoerke.modules.order.entity.OrderServiceVo;
import com.cxqm.xiaoerke.modules.order.entity.SysConsultPhoneServiceVo;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhoneOrderService;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
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
    ConsultPhoneRegisterServiceDao consultPhoneRegisterServiceDao;

    @Autowired
    PatientRegisterServiceDao patientRegisterServiceDao;

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
    public List<HashMap<String, Object>> getOrderList(String type) {
        //userId
        User user = UserUtils.getUser();
        String userId = user.getId();

        //电话咨询订单列表
        List<HashMap<String, Object>> consultPhoneOrderList = consultPhoneRegisterServiceDao.getPhoneConsultaList(userId,null);

        //预约就诊订单列表
        List<HashMap<String, Object>> appointOrderList = patientRegisterServiceDao.getAppointOrderList(userId);

        //融合两种订单列表并排序
        List<OrderServiceVo> list = new ArrayList<OrderServiceVo>();
        if((type!=null && type.equals("all")) || (type!=null && type.equals("phone"))) {
            for (HashMap<String, Object> orderMap : consultPhoneOrderList) {
                OrderServiceVo order = new OrderServiceVo();
                order.setDoctorName((String) orderMap.get("name"));
                order.setPosition((String) orderMap.get("position2"));
                order.setHospital((String) orderMap.get("hospitalName"));
                order.setDate((String) orderMap.get("date"));
                order.setBeginTime((String) orderMap.get("beginTime"));
                order.setEndTime((String) orderMap.get("endTime"));
                order.setStatus((String) orderMap.get("status"));
                order.setClassify("phone");

                order.setDoctorId((String) orderMap.get("doctorId"));
                order.setOrderId(orderMap.get("orderId")+"");
                list.add(order);
            }
        }
        if((type!=null && type.equals("all")) || (type!=null && type.equals("ap"))){
            for(HashMap<String, Object> orderMap:appointOrderList){
                OrderServiceVo order = new OrderServiceVo();
                order.setDoctorName((String)orderMap.get("name"));
                order.setPosition((String) orderMap.get("position2"));
                order.setHospital((String) orderMap.get("hospitalName"));
                order.setDate((String) orderMap.get("date"));
                order.setBeginTime((String) orderMap.get("beginTime"));
                order.setEndTime((String) orderMap.get("endTime"));
                order.setStatus((String) orderMap.get("status"));
                order.setClassify("ap");

                order.setDoctorId((String) orderMap.get("doctorId"));
                order.setOrderId((String) orderMap.get("orderId"));
                list.add(order);
            }
        }
        Collections.sort(list);

        //返回数据
        List<HashMap<String, Object>> OrderList = new ArrayList<HashMap<String, Object>>();
        for(OrderServiceVo orderServiceVo:list){
            HashMap<String,Object> map = new HashMap<String, Object>();
            map.put("doctorName",orderServiceVo.getDoctorName());
            map.put("position",orderServiceVo.getPosition());
            map.put("hospital",orderServiceVo.getHospital());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat format1 = new SimpleDateFormat("MM/dd");
            try {
                Date date = format.parse(orderServiceVo.getDate());
                String date1 = format1.format(date);
                map.put("date",date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            map.put("time",orderServiceVo.getBeginTime()+"-"+orderServiceVo.getEndTime());
            map.put("status",orderServiceVo.getStatus());
            map.put("classify",orderServiceVo.getClassify());

            map.put("doctorId", orderServiceVo.getDoctorId());
            map.put("orderId", orderServiceVo.getOrderId());
            OrderList.add(map);
        }
        return OrderList;

    }


}
