package com.cxqm.xiaoerke.modules.sys.service.impl;


import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.sys.dao.MessageDao;
import com.cxqm.xiaoerke.modules.sys.dao.MsgAppointmentInfoDao;
import com.cxqm.xiaoerke.modules.sys.entity.MessageVo;
import com.cxqm.xiaoerke.modules.sys.entity.PerAppDetInfoVo;
import com.cxqm.xiaoerke.modules.sys.service.DoctorInfoService;
import com.cxqm.xiaoerke.modules.sys.service.MessageService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.DoctorMsgTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = false)
public class MessageServiceImpl implements MessageService{
    @Autowired
	private MessageDao messageDao;
	
	@Autowired
	private MsgAppointmentInfoDao msgAppointmentinfoDao;
	
	@Autowired
	private DoctorInfoService doctorInfoService;
	
	@Autowired
	private SystemService systemService;
	
	//支付完成生成消息，需要的数据
	@Override
    public HashMap<String, Object> findMessageNeedToInsert(HashMap<String, Object> hashMap) {
        HashMap<String, Object> resultMap = messageDao.findMessageNeedToInsertExecute(hashMap);
        return resultMap;
    }

    //支付完成插入消息
	@Override
    public void insertMessage(HashMap<String, Object> executeMap) {
        messageDao.InsertMessageExecute(executeMap);
    }

	@Override
	public String getMsgAppointmentStatus(Map<String, Object> params)
	{
		Map returnValue = null;
		String status = "0";
		if (params.get("phoneNum") != null && params.get("doctorId") != null) {
			returnValue = msgAppointmentinfoDao.msgAppointmentStatus((HashMap) params);
			if (returnValue != null) {
				status = "1";
			}
		}
		return status;
	}


	 /**
     * 短信预约接口
     * @param
     */
	@Override
    public int addMsgAppointment(Map<String, Object> executeMap) {
        return msgAppointmentinfoDao.addMsgAppointment(executeMap);
    }

	@Override
    public void sendMsg2Doctor(String patientRegisterServiceId,String doctorName,String babyName,String userPhone,String date,String week,String beginTime,String hostpitalName){
		HashMap<String,Object> resultMap = messageDao.getCancelAppointmentDocNum(patientRegisterServiceId);
		if(null !=resultMap){
			String num = (String)resultMap.get("login_name");
			String openid = (String)resultMap.get("openid");
			Map tokenMap = systemService.getDoctorWechatParameter();
			String token = (String)tokenMap.get("token");
			String content = babyName + "宝宝取消了" + doctorName +"医生" + date + "(" + week + ")" + beginTime+"的"+hostpitalName+"门诊";
			DoctorMsgTemplate.cancelDoctorAppointment2Wechat(content,date,token,"",openid);
			if(null !=num){
				DoctorMsgTemplate.cancelDoctorAppointment2Sms(doctorName, babyName, num, date, week, beginTime, hostpitalName,userPhone);
			}
		}

    }
	
	 //根据订单号查询消息
	@Override
    public List<MessageVo> findMessageByRegisterNo(String appointmentNo) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("appointmentNo", appointmentNo);
        List<MessageVo> list = messageDao.findMessageByRegisterNoExecute(hashMap);
        return list;
    }

	@Override
    public void sendDoctorWithDrawMessage(String userId,Float money, String token)
    {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        String dateNowTimeStr = sdfTime.format(d);

        HashMap<String, Object> doctorInfo = doctorInfoService.findDoctorDetailInfoByUserId(userId);

        String st = "余额提现\n\n" + dateNowStr + "\n您申请了一笔余额提现，资金预计" + sdf.format(new Date(d.getTime() + 7 * 24 * 60 * 60 * 1000))
                + " " + "24:00前到账，请注意查收" + "\n 提现金额：" + money + "元\n" +"提现时间："+ dateNowTimeStr+"\n"+
                "提现方式：微信钱包"+
                "【<a href='https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx9b663cd46164130c&redirect_uri=" +
                "http%3a%2f%2fxiaoxiaoerke.com%2fxiaoerke-doctor%2fgetWechatOpenid%3furl%3d1"+"&response_type=" +
                "code&scope=snsapi_base&connect_redirect=1#wechat_redirect'>查看详情</a>】\n";
        WechatUtil.sendMsgToWechat(token, (String) doctorInfo.get("openid"), st);
    }
	
	//插入监听器
	@Override
    public void insertMonitorExecute(HashMap<String,Object> monitorMap) {
		messageDao.insertMonitorExecute(monitorMap);
	}
	@Override
	public void insertMonitorConsultPhone(HashMap<String, Object> monitorMap){
		messageDao.insertMonitorConsultPhone(monitorMap);
	};

	@Override
	public Map fidPersonAppointDetailInfoExcut(PerAppDetInfoVo perAppDetInfoVO) {
		return messageDao.fidPersonAppointDetailInfoExcut(perAppDetInfoVO);
	}

	@Override
	public HashMap<String, Object> findAppointMessageExecute(
			HashMap<String, Object> hashMap) {
		return messageDao.findAppointMessageExecute(hashMap);
	}

    @Override
    public HashMap<String, Object> findShareDetailInfoExecute(
            HashMap<String, Object> hashMap) {
        return messageDao.findShareDetailInfoExecute(hashMap);
    }

    @Override
    public HashMap<String, Object> findPhoneConsultShareDetailInfoExecute(
            HashMap<String, Object> hashMap) {
        return messageDao.findPhoneConsultShareDetailInfoExecute(hashMap);
    }

	@Override
	public void saveAdvice(Map<String, Object> hashMap) {
		messageDao.saveAdvice(hashMap);
	}

	@Override
	public List<HashMap<String,Object>> evaluateReminderExecute(){ return messageDao.evaluateReminderExecute();};

	@Override
	public List<HashMap<String,Object>> AppointNoPayExecute(){ return messageDao.AppointNoPayExecute();};

	@Override
	public void setMonitorStatusExecute(HashMap<String,Object> hashMap){messageDao.setMonitorStatusExecute(hashMap);};

    @Override
    public void setMonitorStatusByID(HashMap<String, Object> hashMap) {
        messageDao.setMonitorStatusById(hashMap);
    }
	@Override
	public List<HashMap<String,Object>> LetsGoReminderExecute(){return messageDao.LetsGoReminderExecute();};

	@Override
	public List<HashMap<String, Object>> getTrackOrder(){return messageDao.getTrackOrder();};

	@Override
	public List<Map> consultPhoneMsgRemind(String id){return messageDao.consultPhoneMsgRemind(id);};
    
}
