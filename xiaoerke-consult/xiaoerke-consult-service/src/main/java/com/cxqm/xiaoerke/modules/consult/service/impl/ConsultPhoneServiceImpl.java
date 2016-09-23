package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultPhoneRecordDao;
import com.cxqm.xiaoerke.modules.consult.entity.CallResponse;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultPhoneRecordVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultPhoneService;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhonePatientService;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.DoctorInfoService;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.DoctorMsgTemplate;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.sys.utils.PatientMsgTemplate;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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

    @Autowired
    private SystemService systemService;

    @Autowired
    private DoctorInfoService doctorInfoService;

    @Autowired
    private SysPropertyServiceImpl sysPropertyService;

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
//        发消息

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
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();

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

        Long serviceLength = (Long)phonepatientInfo.get("surplusTime");
        String talkDuration = vo.getTalkduration();

        ConsultPhoneRegisterServiceVo consultPhonevo = new ConsultPhoneRegisterServiceVo();

        consultPhonevo.setId(Integer.parseInt(userData));
        consultPhonevo.setUpdateTime(new Date());
        Map<String,Object> consultOrder = consultPhonePatientService.getPatientRegisterInfo(Integer.parseInt(userData));
        String type = (String)consultOrder.get("type");
        if("1234".indexOf(vo.getByetype())>-1&&Integer.parseInt(talkDuration)>0){
//             改状态
            consultPhonevo.setType("1");//已推送过消息
            consultPhonevo.setSurplusTime(serviceLength - Integer.parseInt(talkDuration) * 1000);//修改通话时间
            //发消息
            if ("0".equals(type)){
                User userInfo = systemService.getUserById((String)consultOrder.get("sys_user_id"));
                String url = sysPropertyVoWithBLOBsVo.getTitanWebUrl()+"/titan/phoneConsult#/orderDetail"+(String) consultOrder.get("doctorName")+","+userData+",phone";
                String connectUrl = sysPropertyVoWithBLOBsVo.getTitanWebUrl()+"/titan/phoneConsult#/phoneConReconnection/"+userData;
                Map<String,Object> parameter = systemService.getWechatParameter();
                String token = (String)parameter.get("token");
                PatientMsgTemplate.consultPhoneEvaluateWaring2Msg((String) consultOrder.get("babyName"), (String) consultOrder.get("doctorName"),(String) consultOrder.get("phone"), url,connectUrl,token);
                PatientMsgTemplate.evaluationRemind2Wechat(userInfo.getOpenid(),token,url,"您的订单可以评价了哦!",(String) consultOrder.get("orderNo"), (String) consultOrder.get("date"),"");
            }
        }else{
            //没接通
            //取消用户订单
            if("待接听".equals(consultOrder.get("state"))){
                LogUtils.saveLog(Servlets.getRequest(), "00000110", "电话咨询未接通取消" + consultOrder);//用户发起微信支付
                ConsultPhoneRegisterServiceVo registerServiceVo =  new ConsultPhoneRegisterServiceVo();
                registerServiceVo.setId(Integer.parseInt(userData));
                registerServiceVo.setUpdateTime(new Date());
                registerServiceVo.setState("4");
                consultPhonePatientService.updateOrderInfoBySelect(registerServiceVo);
                //并发送消息
                String url = sysPropertyVoWithBLOBsVo.getTitanWebUrl()+"/titan/phoneConsult#/orderDetail"+(String) consultOrder.get("doctorId")+","+(Integer) consultOrder.get("id")+",phone";
                PatientMsgTemplate.unConnectPhone2Msg((String) consultOrder.get("babyName"), (String) consultOrder.get("doctorName"), (Float) consultOrder.get("price") + "", (String) consultOrder.get("phone"), (String) consultOrder.get("orderNo"));
                Map tokenMap = systemService.getWechatParameter();
                String token = (String)tokenMap.get("token");
                String week = DateUtils.getWeekOfDate(DateUtils.StrToDate((String)consultOrder.get("date"),"yyyy/MM/dd"));
                String dateTime = (String)consultOrder.get("date")+week+(String)consultOrder.get("beginTime");
                PatientMsgTemplate.unConnectPhone2Wechat(dateTime, (String) consultOrder.get("userPhone"), (String) consultOrder.get("doctorName"), (Float) consultOrder.get("price") + "", url, (String) consultOrder.get("orderNo"), (String) consultOrder.get("openid"), token);

                //未接通时 给医生发消息提醒
                String doctorName =  (String) consultOrder.get("doctorName");
                String babyName =  (String) consultOrder.get("babyName");
                String doctorPhone =  (String)consultOrder.get("doctorPhone");
                DoctorMsgTemplate.doctorPhoneConsultRemindFail2Sms(doctorName, babyName, doctorPhone);

                String time = (String)consultOrder.get("date")+" "+week+" "+(String)consultOrder.get("beginTime")+"-"+(String)consultOrder.get("endTime");
                Map doctorTokenMap = systemService.getDoctorWechatParameter();
                String doctorToken = (String) doctorTokenMap.get("token");
                String openId = doctorInfoService.findOpenIdByDoctorId((String) consultOrder.get("doctorId"));
                if (StringUtils.isNotNull(openId)) {
                    DoctorMsgTemplate.doctorPhoneConsultRemindFail2Wechat(babyName, time,(String)consultOrder.get("orderNo"), doctorToken, "", openId);
                }
            };
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

    @Override
    public List<ConsultPhoneRecordVo> getConsultRecordInfo(String userData,String action) {
       return consultPhoneRecordDao.selectByUserData(userData,action);
    }

    public String getXmlFormObj(Object obj){
        XStream xStream=new XStream(new DomDriver());
        xStream.setMode(XStream.NO_REFERENCES);
        xStream.processAnnotations(new Class[]{CallResponse.class});
        return xStream.toXML(obj);
    }
}
