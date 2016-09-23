package com.cxqm.xiaoerke.modules.order.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.cxqm.xiaoerke.common.utils.ConstantUtil;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.utils.DoctorMsgTemplate;
import com.cxqm.xiaoerke.modules.sys.utils.PatientMsgTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxqm.xiaoerke.common.bean.WechatArticle;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.modules.order.dao.PatientRegisterServiceDao;
import com.cxqm.xiaoerke.modules.sys.service.MessageService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.ChangzhuoMessageUtil;

@Service
@Transactional(readOnly = false)
class OrderMessageService {

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private PatientRegisterServiceDao patientRegisterServiceDao;


	@Autowired
	private SysPropertyServiceImpl sysPropertyService;
	
	@Autowired
	private SystemService systemService;
	
	public void sendMessage(Map<String, Object> params, Boolean flag) {
		SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
		Map<String, Object> parameter = systemService.getWechatParameter();
		String shot_message = "";
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("patientRegisterServiceId", params.get("patient_register_service_id"));
		//查询需要的数据
		resultMap = messageService.findMessageNeedToInsert(resultMap);
		//根据手机号查询或创建用户  有则查，没有则创建
		if (flag) {
			/**
			 * 预约成功
			 * 【预约成功】  王国  小朋友家长，您好，您已成功预约  XX  医生，  XX  月  XX  日（周X）XX：XX-XX：XX，
			 * 北京市儿童医院，订单号：XXXXXXXXX。您就诊后完成评价即可获得新的邀请码。【宝大夫】
			 * 需要构建的字段 id message_type sys_user_id message_title   message_content create_date  appointmentNo
			 * */
			//给定日期查询星期几
			Date date = (Date) resultMap.get("date_week");
			String DateToStr = DateUtils.DateToStr(date);
			int week = 0;
			try {
				week = DateUtils.dayForWeek(DateToStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String dayWeek = DateUtils.getDayWeek(week);


			//消息修改
			PatientMsgTemplate patientMsgTemplate = new PatientMsgTemplate();
			patientMsgTemplate.appointmentSuccess2Sms((String) resultMap.get("phone"),
					(String)resultMap.get("babyName"),(String)resultMap.get("doctorName"),
					(String)resultMap.get("date"),dayWeek,(String)resultMap.get("begin_time"),
					(String)resultMap.get("end_time"),(String)resultMap.get("position"),
					(String)resultMap.get("contactHospitalName"),(String)resultMap.get("location"),
					sysPropertyVoWithBLOBsVo.getTitanWebUrl() +"titan/appoint#/toBeTreatedDetail/" + params.get("patient_register_service_id") + ",1");

			if(params.containsKey("openId")&&params.get("openId")!=null)
			{
				PatientMsgTemplate.appointmentSuccess2Wechat((String)resultMap.get("babyName"),(String)resultMap.get("doctorName"),(String)resultMap.get("date"),(String)dayWeek,(String)resultMap.get("begin_time"),(String)resultMap.get("end_time"),(String)resultMap.get("position"),(String)resultMap.get("contactHospitalName"),(String)resultMap.get("location"),(String)resultMap.get("register_no"),(String) params.get("openId"),(String) parameter.get("token"),sysPropertyVoWithBLOBsVo.getTitanWebUrl()+"titan/appoint#/toBeTreatedDetail/" + params.get("patient_register_service_id") + ",1");
			}
			/**
			 * 【交通信息】
			 * "北京市西城区南礼士路56号北京儿童医院
			 * 开车可在医院大门进入医院地下停车场；
			 * *地铁可在南礼士路站下车，进入门诊大厅后可乘电梯到2楼，到达后找到相诮的诊室即可。"
			 * */
			//======================交通信息消息插入=====================
			StringBuffer messageContentTraffic = new StringBuffer();
			messageContentTraffic.append("(交通信息)");
			messageContentTraffic.append(resultMap.get("root"));
			shot_message = messageContentTraffic.toString();
			//短信用户提示
			shot_message = shot_message.substring(shot_message.lastIndexOf("；") + 1, shot_message.length());
			if(params.containsKey("openId")&&params.get("openId")!=null) {
			}
			ChangzhuoMessageUtil.sendMsg((String) resultMap.get("phone"), "亲爱的宝妈宝爸，就诊前如果您对宝宝的病情有任何疑问，" +
					"请及时到宝大夫平台在线咨询，专业儿科医生将为您提供服务，祝宝宝早日康复。微信公众号：宝大夫。");
		} else {
			/**
			 * 【取消预约】您已取消XX医生XX月XX日（周X) XX：XX的预约订单号：XXXXXXXXX。此邀请可继续使用，您的邀请码：XXXXXXXX。
			 * */
			//======================取消预约=====================
			//给定日期查询星期几
			Date date = (Date) resultMap.get("date_week");
			String DateToStr = DateUtils.DateToStr(date);
			int week = 0;
			try {
				week = DateUtils.dayForWeek(DateToStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String dayWeek = DateUtils.getDayWeek(week);

			PatientMsgTemplate.cancelNotice2Sms((String) resultMap.get("phone"), (String) resultMap.get("doctorName"),
					(String) resultMap.get("date"), dayWeek, (String) resultMap.get("begin_time"));
			//微信推送消息
			if(params.containsKey("openId")&&params.get("openId")!=null) {
				String contetn = "医生:"+resultMap.get("doctorName")+"\n时间:"+resultMap.get("date")+" "+
						dayWeek+" "+resultMap.get("begin_time")+"\n地点:"+resultMap.get("contactHospitalName")+" "+(String)resultMap.get("location")+"\n订单号:"+(String)resultMap.get("register_no")+"(已取消)";
				PatientMsgTemplate.cancelNotice2Wechat((String)params.get("openId"), (String) parameter.get("token"),
						contetn, sysPropertyVoWithBLOBsVo.getTitanWebUrl()+"titan/appoint#/toBeTreatedDetail/" +
								params.get("patient_register_service_id") + ",3");
			}

			//此处分为发送给医生或者接口人
			if (!(resultMap.get("hospitalContactPhone").equals(""))) {
				String content = resultMap.get("babyName") + "宝宝取消了" + resultMap.get("doctorName") +
						"医生" + resultMap.get("date") + "(" + dayWeek + ")" +
						resultMap.get("begin_time") + "-" + resultMap.get("end_time") +"的"+resultMap.get("contactHospitalName")+"门诊";
				ChangzhuoMessageUtil.sendMsg((String) resultMap.get("hospitalContactPhone"),content);
			}
			else{
				messageService.sendMsg2Doctor((String) params.get("patient_register_service_id"), (String) resultMap.get("doctorName"), (String) resultMap.get("babyName"), (String) resultMap.get("phone"), (String) resultMap.get("date"), dayWeek, (String) resultMap.get("begin_time"), (String) resultMap.get("contactHospitalName"));
			}
		}
	}
    
}
