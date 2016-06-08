package com.cxqm.xiaoerke.modules.account.web;

import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.account.entity.PayRecord;
import com.cxqm.xiaoerke.modules.account.service.AccountService;
import com.cxqm.xiaoerke.modules.account.service.PayRecordService;
import com.cxqm.xiaoerke.modules.insurance.entity.InsuranceRegisterService;
import com.cxqm.xiaoerke.modules.insurance.service.InsuranceRegisterServiceService;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhonePatientService;
import com.cxqm.xiaoerke.modules.order.service.PatientRegisterService;
import com.cxqm.xiaoerke.modules.sys.entity.PerAppDetInfoVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.sys.utils.PatientMsgTemplate;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import com.cxqm.xiaoerke.modules.sys.utils.WechatMessageUtil;
import com.cxqm.xiaoerke.modules.umbrella.entity.BabyUmbrellaInfo;
import com.cxqm.xiaoerke.modules.umbrella.service.BabyUmbrellaInfoService;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 关于账户管理
 * @author frank
 * @date 2015-10-14
 */
@Controller
public class PayNotificationController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private PatientRegisterService patientRegisterService;

	@Autowired
	private ConsultPhonePatientService consultPhonePatientService;

	@Autowired
	private SystemService systemService;

	@Autowired
	private InsuranceRegisterServiceService insuranceService;
	
	@Autowired
    private PayRecordService payRecordService;

	@Autowired
	private PatientRegisterPraiseService patientRegisterPraiseService;

	@Autowired
	private BabyUmbrellaInfoService babyUmbrellaInfoService;

	@Autowired
	private WechatAttentionService wechatAttentionService;

	private static Lock lock = new ReentrantLock();

	/**
	 * 接收支付成后微信notify_url参数中传来的参数
	 * 支付完成 后服务器故障 事物无法回滚
	 * */
	@RequestMapping(value = "getPayNotifyInfo", method = {RequestMethod.POST, RequestMethod.GET})
	public synchronized
	@ResponseBody
	String getPayNotifyInfo(HttpServletRequest request) {
		lock.lock();
        InputStream inStream = null;
        try {
            inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            inStream.close();
            String result  = new String(outSteam.toByteArray(),"utf-8");
            Map<String, Object> map = XMLUtil.doXMLParse(result);

            //放入service层进行事物控制
            if("SUCCESS".equals(map.get("return_code"))){
				LogUtils.saveLog(Servlets.getRequest(), "00000048","用户微信支付完成:" + map.get("out_trade_no"));
				String accountNeedPay = (String) map.get("attach");
				PayRecord payRecord = new PayRecord();
				payRecord.setId((String) map.get("out_trade_no"));
				payRecord.setStatus("success");
				payRecord.setReceiveDate(new Date());
				Map wechatParameter = systemService.getWechatParameter();
				patientRegisterService.handleWxMemberServiceNotifyInfo(accountNeedPay, payRecord,
						(String) wechatParameter.get("token"));
			}
            return  XMLUtil.setXML("SUCCESS", "");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
			lock.unlock();
		}
		return "";
	}

	/**
	 * 接收支付成后微信notify_url参数中传来的参数
	 * 支付完成 后服务器故障 事物无法回滚
	 * */
	@RequestMapping(value = "/user/getCustomerPayNotifyInfo", method = {RequestMethod.POST, RequestMethod.GET})
	public synchronized
	@ResponseBody String getCustomerPayNotifyInfo(HttpServletRequest request) {
		lock.lock();
		InputStream inStream = null;
		try {
			inStream = request.getInputStream();
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			outSteam.close();
			inStream.close();
			String result  = new String(outSteam.toByteArray(),"utf-8");
			Map<String, Object> map = XMLUtil.doXMLParse(result);

			//放入service层进行事物控制
			if("SUCCESS".equals(map.get("return_code"))){
				LogUtils.saveLog(Servlets.getRequest(), "00000048","用户微信支付完成:" + map.get("out_trade_no"));
				PayRecord payRecord = new PayRecord();
				payRecord.setId((String) map.get("out_trade_no"));
				payRecord.setStatus("success");
				payRecord.setReceiveDate(new Date());
				Map<String,Object> insuranceMap= insuranceService.getPayRecordById(payRecord.getId());
				String insuranceId= insuranceMap.get("order_id").toString();
				System.out.println("orderId:" + insuranceId);
				if(insuranceMap.get("fee_type").toString().equals("customer")){
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("payStatus", "success");
					params.put("id", insuranceId);
					patientRegisterPraiseService.updateCustomerEvaluation(params);
					payRecord.getId();//修改pay_record表状态
					payRecord.setStatus("success");
					payRecord.setReceiveDate(new Date());
					payRecordService.updatePayInfoByPrimaryKeySelective(payRecord, "");
				}
			}
			return  XMLUtil.setXML("SUCCESS", "");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return "";
	}

	/**
	 * 接收支付成后微信notify_url参数中传来的参数
	 * 支付完成 后服务器故障 事物无法回滚
	 * */
	@RequestMapping(value = "/user/getInsurancePayNotifyInfo", method = {RequestMethod.POST, RequestMethod.GET})
	public synchronized
	@ResponseBody String getInsurancePayNotifyInfo(HttpServletRequest request) {
		lock.lock();
		InputStream inStream = null;
		try {
			inStream = request.getInputStream();
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			outSteam.close();
			inStream.close();
			String result  = new String(outSteam.toByteArray(),"utf-8");
			Map<String, Object> map = XMLUtil.doXMLParse(result);

			//放入service层进行事物控制
			if("SUCCESS".equals(map.get("return_code"))){
				LogUtils.saveLog(Servlets.getRequest(), "00000048","用户微信支付完成:" + map.get("out_trade_no"));
				PayRecord payRecord = new PayRecord();
				payRecord.setId((String) map.get("out_trade_no"));
				payRecord.setStatus("success");
				payRecord.setReceiveDate(new Date());
				Map<String,Object> insuranceMap= insuranceService.getPayRecordById(payRecord.getId());
				String insuranceId= insuranceMap.get("order_id").toString();
				System.out.println("orderId:" + insuranceId);
				if(insuranceMap.get("fee_type").toString().equals("insurance")){
					InsuranceRegisterService insurance=new InsuranceRegisterService();
					insurance.setId(insuranceId);
					insurance.setState("0");
					insuranceService.updateInsuranceRegisterService(insurance);
					payRecord.getId();//修改pay_record表状态
					payRecord.setStatus("success");
					payRecord.setReceiveDate(new Date());
					payRecordService.updatePayInfoByPrimaryKeySelective(payRecord, "");
				}
			}
			return  XMLUtil.setXML("SUCCESS", "");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return "";
	}

	/**
	 * 接收支付成后微信notify_url参数中传来的参数
	 * 支付完成 后服务器故障 事物无法回滚
	 * */
	@RequestMapping(value = "/user/getPhoneConsultPayNotifyInfo", method = {RequestMethod.POST, RequestMethod.GET})
	public synchronized
	@ResponseBody
	String getConsultPhonePayNotifyInfo(HttpServletRequest request) {
		lock.lock();
		InputStream inStream = null;
		try {
			inStream = request.getInputStream();
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			outSteam.close();
			inStream.close();
			String result  = new String(outSteam.toByteArray(),"utf-8");
			Map<String, Object> map = XMLUtil.doXMLParse(result);

			//放入service层进行事物控制
			if("SUCCESS".equals(map.get("return_code"))) {
				LogUtils.saveLog(Servlets.getRequest(), "00000048", "用户微信支付完成:" + map.get("out_trade_no"));
				PayRecord payRecord = new PayRecord();
				payRecord.setId((String) map.get("out_trade_no"));
				payRecord.setStatus("success");
				payRecord.setReceiveDate(new Date());
				Map<String, Object> consultPhoneMap = insuranceService.getPayRecordById(payRecord.getId());
				Map<String, Object> mapVo = consultPhonePatientService.getPatientRegisterInfo(Integer.parseInt((String) consultPhoneMap.get("order_id")));
				if ("待支付".equals(mapVo.get("state"))) {
					String consultPhoneId = consultPhoneMap.get("order_id").toString();
					System.out.println("orderId:" + consultPhoneId);
					if (consultPhoneMap.get("fee_type").toString().equals("consultPhone")) {

						ConsultPhoneRegisterServiceVo consultPhoneVo = new ConsultPhoneRegisterServiceVo();
						consultPhoneVo.setId(Integer.parseInt(consultPhoneId));
						consultPhoneVo.setState("1");
						consultPhoneVo.setUpdateTime(new Date());


						consultPhonePatientService.updateOrderInfoBySelect(consultPhoneVo);

						payRecord.setDoctorId((String) mapVo.get("doctorId"));

						payRecordService.updatePayInfoByPrimaryKeySelective(payRecord, "");
						Map<String, Object> consultOrder = consultPhonePatientService.getPatientRegisterInfo(Integer.parseInt(consultPhoneId));
						HashMap<String, Object> hashMap = new HashMap<String, Object>();
						hashMap.put("userId", consultOrder.get("sys_user_id"));
						User userInfo = systemService.getUserById((String) consultOrder.get("sys_user_id"));
						String week = DateUtils.getWeekOfDate(DateUtils.StrToDate((String) consultOrder.get("date"), "yyyy/MM/dd"));
						PatientMsgTemplate.consultPhoneSuccess2Msg((String) consultOrder.get("babyName"), (String) consultOrder.get("doctorName"), (String) consultOrder.get("date"), week, (String) consultOrder.get("beginTime"), (String) consultOrder.get("phone"), (String) consultOrder.get("orderNo"));
						Map<String, Object> parameter = systemService.getWechatParameter();
						String token = (String) parameter.get("token");
						String url = ConstantUtil.TITAN_WEB_URL + "/titan/phoneConsult#/orderDetail" + (String) consultOrder.get("doctorId") + "," + (Integer) consultOrder.get("orderId") + ",phone";
						PatientMsgTemplate.consultPhoneSuccess2Wechat((String) consultOrder.get("doctorName"), (String) consultOrder.get("date"), week, (String) consultOrder.get("beginTime"), (String) consultOrder.get("endTime"), (String) consultOrder.get("phone"), (String) consultOrder.get("orderNo"), userInfo.getOpenid(), token, url);
					}
				}
			}
			return  XMLUtil.setXML("SUCCESS", "");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return "";
	}


	/**
	 * 接收支付成后微信notify_url参数中传来的参数
	 * 支付完成 后服务器故障 事物无法回滚
	 * */
	@RequestMapping(value = "/user/getUmbrellaPayNotifyInfo", method = {RequestMethod.POST, RequestMethod.GET})
	public synchronized
	@ResponseBody String getUmbrellaPayNotifyInfo(HttpServletRequest request) {
		lock.lock();
		InputStream inStream = null;
		try {
			inStream = request.getInputStream();
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			outSteam.close();
			inStream.close();
			String result  = new String(outSteam.toByteArray(),"utf-8");
			Map<String, Object> map = XMLUtil.doXMLParse(result);

			//放入service层进行事物控制
			if("SUCCESS".equals(map.get("return_code"))){
				LogUtils.saveLog(Servlets.getRequest(), "00000048","用户微信支付完成:" + map.get("out_trade_no"));
				PayRecord payRecord = new PayRecord();
				payRecord.setId((String) map.get("out_trade_no"));
				payRecord.setStatus("success");
				payRecord.setReceiveDate(new Date());
				Map<String,Object> insuranceMap= insuranceService.getPayRecordById(payRecord.getId());
				String insuranceId= insuranceMap.get("order_id").toString();
				String[] umbrellaId=insuranceId.split("_");
				System.out.println("orderId:" + umbrellaId[0]);
				System.out.println("shareId:" + umbrellaId[1]);
				sendWechatMessage(umbrellaId[0], umbrellaId[1]);

				if(insuranceMap.get("fee_type").toString().equals("umbrella")){
					BabyUmbrellaInfo babyUmbrellaInfo=new BabyUmbrellaInfo();
					babyUmbrellaInfo.setId(Integer.parseInt(umbrellaId[0]));
					babyUmbrellaInfo.setPayResult("success");
					babyUmbrellaInfo.setActivationTime(new Date());
					babyUmbrellaInfoService.updateBabyUmbrellaInfoById(babyUmbrellaInfo);
					payRecord.getId();//修改pay_record表状态
					payRecord.setStatus("success");
					payRecord.setReceiveDate(new Date());
					payRecordService.updatePayInfoByPrimaryKeySelective(payRecord, "");
				}
			}
			return  XMLUtil.setXML("SUCCESS", "");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return "";
	}

	private void sendWechatMessage(String toId, String fromId){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id",fromId);
		List<Map<String,Object>> list = babyUmbrellaInfoService.getBabyUmbrellaInfo(param);
		Map parameter = systemService.getWechatParameter();
		String token = (String)parameter.get("token");
		String toOpenId = "";
		if(list.size()!=0){
			if((Integer) list.get(0).get("umbrella_money")<400000){
				String fromOpenId = (String)list.get(0).get("openid");//分享者openid
				String babyId = (String)list.get(0).get("baby_id");

				BabyUmbrellaInfo babyUmbrellaInfo = new BabyUmbrellaInfo();
				babyUmbrellaInfo.setId(Integer.parseInt(fromId));
				int umbrellaMoney = (Integer) list.get(0).get("umbrella_money")+20000;
				babyUmbrellaInfo.setUmberllaMoney(umbrellaMoney);
				babyUmbrellaInfoService.updateBabyUmbrellaInfoById(babyUmbrellaInfo);
				Map<String, Object> param_ = new HashMap<String, Object>();
				param_.put("id", toId);
				List<Map<String,Object>> tolist = babyUmbrellaInfoService.getBabyUmbrellaInfo(param_);
				String nickName = "";
				if(tolist.size()!=0){
					toOpenId = (String)tolist.get(0).get("openid");
					if(StringUtils.isNotNull(toOpenId)){
						WechatAttention wa = wechatAttentionService.getAttentionByOpenId(toOpenId);
						if(wa!=null){
							nickName = StringUtils.isNotNull(wa.getNickname())?wa.getNickname():"";
						}
					}
				}
				String title = "恭喜您，您的好友"+nickName+"已成功加入。您既帮助了朋友，也提升了2万保障金！";
				String templateId = "b_ZMWHZ8sUa44JrAjrcjWR2yUt8yqtKtPU8NXaJEkzg";
				String keyword1 = "您已拥有"+babyUmbrellaInfo.getUmberllaMoney()/10000+"万的保障金，还需邀请"+(400000-umbrellaMoney)/20000+"位好友即可获得最高40万保障金。";
				String keyword2 = StringUtils.isNotNull(babyId)?"观察期":"待激活";
				String remark = "邀请一位好友，增加2万保额，最高可享受40万保障！";
				String url = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrellaa";
				WechatMessageUtil.templateModel(title, keyword1, keyword2, "", "", remark, token, url, fromOpenId, templateId);
			}
		}

		String title = "宝大夫送你一份见面礼";
		String description = "恭喜您已成功领取专属于宝宝的40万高额保障金";
		String url = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrellaa";
		String picUrl = "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/protectumbrella%2Fprotectumbrella";
		String message = "{\"touser\":\""+toOpenId+"\",\"msgtype\":\"news\",\"news\":{\"articles\": [{\"title\":\""+ title +"\",\"description\":\""+description+"\",\"url\":\""+ url +"\",\"picurl\":\""+picUrl+"\"}]}}";

		String jsonobj = HttpRequestUtil.httpsRequest("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" +
				token + "", "POST", message);
		System.out.println(jsonobj + "===============================");
	}


	/**
	 *检查订单的支付情况
	 * 支付完成 后服务器故障 事物无法回滚
	 * */
	@RequestMapping(value = "user/checkAppointment", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	Map<String,Object> checkAppointment(){
		Map a = new HashMap();
		Boolean result = accountService.checkAppointmentPayState("0ed32992422e4aafafd92b83b428531d");
		System.out.print(result);
		return a;
	}

}
