package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultPhoneRecordDao;
import com.cxqm.xiaoerke.modules.consult.entity.*;
import com.cxqm.xiaoerke.modules.consult.service.ConsultPhoneService;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhonePatientService;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * Created by deliang on 16/3/10.
 */

@Service
public class ConsultPhoneServiceImpl implements ConsultPhoneService {

    @Autowired
    private ConsultPhonePatientService consultPhonePatientService;

    @Autowired
    private ConsultPhoneRecordDao consultPhoneRecordDao;


    @Override
    public String parseCallAuth(Map<String, Object> map) {
        ConsultPhoneRecordVo vo = new ConsultPhoneRecordVo();
        vo.setType((String) map.get("type"));
        vo.setOrderid((String) map.get("orderid"));
        vo.setSubid((String) map.get("subid"));
        vo.setCaller((String) map.get("caller"));
        vo.setCalled((String) map.get("called"));
        vo.setCallsid((String) map.get("callSid"));
        vo.setUserdata((String) map.get("userData"));
        vo.setAction((String) map.get("action"));
        vo.setSubtype((String) map.get("subtype"));

        consultPhoneRecordDao.insertSelective(vo);


        //返回的数据,如果需要控制呼叫时长需要增加sessiontime
        //业务层 先查询该订单是否是带接听状态, 确认通话时间
        Map<String,Object> phonepatientInfo = consultPhonePatientService.getPatientRegisterInfo(Integer.parseInt((String)map.get("userData")));
//        phonepatientInfo.get("server_length");
        CallResponse response = new CallResponse();
        response.setStatuscode("1111");
        if("待接听".equals(phonepatientInfo.get("state"))||"待评价".equals(phonepatientInfo.get("state"))){
          response.setStatuscode("0000");
        };
        response.setStatusmsg("");
        response.setRecord("1");//录音
        response.setRecordPoint("1");//录音方式

        Integer serviceLength = (Integer) phonepatientInfo.get("server_length")*60;
        response.setSessiontime(serviceLength + "");//通话时长
        String result = getXmlFormObj(response);
        return result;
    }
    /**
     * 摘机请求
     *
     * */
    @Override
    public String parseCallEstablish(Map<String, Object> map) {
        //返回的数据,如果需要控制呼叫时长需要增加sessiontime
        ConsultPhoneRecordVo vo = new ConsultPhoneRecordVo();
        String startTime = (String) map.get("starttime");
        if(StringUtils.isNotNull(startTime)){
            vo.setStarttime(DateUtils.StrToDate(startTime, "datetimesec"));
        }
        String userDate = (String) map.get("userData");
        vo.setUserdata(userDate);
        vo.setAction((String) map.get("action"));
        vo.setType((String) map.get("type"));
        vo.setSubtype((String) map.get("subtype"));
        vo.setOrderid((String) map.get("orderid"));
        vo.setSubid((String) map.get("subid"));
        vo.setCaller((String) map.get("caller"));
        vo.setCalled((String) map.get("called"));
        vo.setCallsid((String) map.get("callSid"));
        consultPhoneRecordDao.insertSelective(vo);
        //如果有此条请求则说明用户已经摘机接通电话,电话订单转成待评价
        ConsultPhoneRegisterServiceVo consultPhonevo = new ConsultPhoneRegisterServiceVo();
        consultPhonevo.setId(Integer.parseInt(userDate));
        consultPhonevo.setState("2");//带评价
        consultPhonevo.setUpdateTime(new Date());
        int state = consultPhonePatientService.updateOrderInfoBySelect(consultPhonevo);
        CallResponse response = new CallResponse();
        response.setStatuscode("0000");
        response.setStatusmsg(state + "");
        response.setBilldata("测试bill");//呼叫的计费私有数据,挂机时原样传回给第三方
//        response.setSessiontime("");//通话时长
        String result = getXmlFormObj(response);
        return result;
    }

    @Override
    public String parseHangup(Map<String, Object> map) {
        ConsultPhoneRecordVo vo = new ConsultPhoneRecordVo();
        vo.setByetype((String) map.get("byetype"));
        vo.setNoAnswerEndtime((String) map.get("noAnswerEndtime"));
        String startTime = (String) map.get("starttime");
        if(StringUtils.isNotNull(startTime)){
          vo.setStarttime(DateUtils.StrToDate(startTime, "datetimesec"));
        }
        String endTime = (String) map.get("endtime");
        if(StringUtils.isNotNull(endTime)){
          vo.setEndtime(DateUtils.StrToDate(endTime, "datetimesec"));
        }
        vo.setTalkduration((String) map.get("talkDuration"));
        vo.setAlertingduration((String) map.get("alertingDuration"));
        vo.setBilldata((String) map.get("billdata"));
        String userData = (String) map.get("userData");
        vo.setUserdata(userData);
        vo.setAction((String) map.get("action"));
        vo.setType((String) map.get("type"));
        vo.setSubtype((String) map.get("subtype"));
        vo.setOrderid((String) map.get("orderid"));
        vo.setSubid((String) map.get("subid"));
        vo.setCaller((String) map.get("caller"));
        vo.setCalled((String) map.get("called"));
        vo.setCallsid((String) map.get("callSid"));
        vo.setRecordurl((String) map.get("recordurl"));
        consultPhoneRecordDao.insertSelective(vo);
        //挂机请求 ,根据剩余时长给用户推送消息,让用户在意外挂断的情况下可以再次接通 判断订单状态是否已推送过 此消息
        Map<String,Object> phonepatientInfo = consultPhonePatientService.getPatientRegisterInfo(Integer.parseInt(userData));
        Integer serviceLength = (Integer)phonepatientInfo.get("server_length");
        String talkDuration = vo.getTalkduration();

        ConsultPhoneRegisterServiceVo consultPhonevo = new ConsultPhoneRegisterServiceVo();
        consultPhonevo.setSurplusTime(serviceLength-Integer.parseInt(talkDuration)*1000);
        consultPhonevo.setId(Integer.parseInt(userData));
        consultPhonevo.setUpdateTime(new Date());
        if(Integer.parseInt(talkDuration)<serviceLength*60-10&&"0".equals(phonepatientInfo.get("type"))){
            //发消息 改状态
            consultPhonevo.setType("1");//已推送过消息
        }
        int state = consultPhonePatientService.updateOrderInfoBySelect(consultPhonevo);
        //返回的数据
        CallResponse response = new CallResponse();
        response.setStatuscode("0000");
        response.setStatusmsg("");
        response.setTotalfee("");//呼叫费用
        String result = getXmlFormObj(response);
        return result;
    }

    public String getXmlFormObj(Object obj){
        XStream xStream=new XStream(new DomDriver());
        xStream.setMode(XStream.NO_REFERENCES);
        xStream.processAnnotations(new Class[]{CallResponse.class});
        return xStream.toXML(obj);
    }
}
