package com.cxqm.xiaoerke.modules.account.web;

import com.cxqm.xiaoerke.common.utils.XMLUtil;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.account.entity.PayRecord;
import com.cxqm.xiaoerke.modules.account.service.AccountService;
import com.cxqm.xiaoerke.modules.account.service.PayRecordService;
import com.cxqm.xiaoerke.modules.insurance.entity.InsuranceRegisterService;
import com.cxqm.xiaoerke.modules.insurance.service.InsuranceRegisterServiceService;
import com.cxqm.xiaoerke.modules.interaction.entity.PatientRegisterPraise;
import com.cxqm.xiaoerke.modules.member.service.MemberService;
import com.cxqm.xiaoerke.modules.order.entity.ConsultPhoneRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.entity.OrderPropertyVo;
import com.cxqm.xiaoerke.modules.order.entity.PatientRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhonePatientService;
import com.cxqm.xiaoerke.modules.order.service.PatientRegisterService;
import com.cxqm.xiaoerke.modules.sys.entity.PerAppDetInfoVo;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 关于账户管理
 * @author frank
 * @date 2015-10-14
 */
@Controller
@RequestMapping(value = "")
public class AccountUserController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private PatientRegisterService patientRegisterService;

	@Autowired
	private SystemService systemService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private InsuranceRegisterServiceService insuranceService;

	@Autowired
	private PayRecordService payRecordService;

	@Autowired
	private ConsultPhonePatientService consultPhonePatientService;

	private static Lock lock = new ReentrantLock();

	/**
	 *用户的账户信息
	 */
	@RequestMapping(value = "/account/user/accountInfo", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	Map<String, Object> accountInfo() {
		HashMap<String, Object> response = new HashMap<String, Object>();
		accountService.getUserAccountInfo(response);
		return response;
	}

	/**
	 * 调用企业支付接口用户用户退款
	 * */
	@RequestMapping(value = "/account/user/returnPay",  method = {RequestMethod.POST, RequestMethod.GET})
	public synchronized
	@ResponseBody
	Map<String,Object>  returnPay(@RequestBody Map<String, Object> params,HttpServletRequest request,HttpSession session){
		lock.lock();
		Map<String,Object> response = new HashMap<String, Object>();
		try {
			int withDrawNum = accountService.checkUserWithDraw(UserUtils.getUser().getId());
			if(withDrawNum<4){
				accountService.returnUserMoney(params,response,request,session);
				return response;
			}else{
				response.put("return_msg", "每天提现不能超过3次");
			}
		}catch (Exception e){
			e.printStackTrace();
			response.put("return_msg", "账户异常,请重试或联系客服");
		}finally {
			lock.unlock();
		}
		return response;
	}

	/**
	 * js支付
	 *
	 * */
	@RequestMapping(value = "/account/user/userPay", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	String userPay(HttpServletRequest request,HttpSession session) throws Exception {
		//查询当前订单状态是否是待支付
		String patientRegisterId =request.getParameter("patientRegisterId");

		//获取统一支付接口参数
		Map prepayInfo = accountService.getPrepayInfo(request,session,"appointService");

		//拼装jsPay所需参数,如果prepay_id生成成功则将信息放入account_pay_record表
		String userId = UserUtils.getUser().getId();//patientRegisterService.getUserIdByPatientRegisterId(patientRegisterId);

		PerAppDetInfoVo Info = new PerAppDetInfoVo();
		Info.setId(patientRegisterId);
		Map<String, Object> responseMap = patientRegisterService.findPersonAppointDetailInfo(Info);
		String payParameter = accountService.assemblyPayParameter(request,prepayInfo,session,userId,
				responseMap!=null?(String)responseMap.get("doctorId"):"");
		return payParameter;
	}

	/**
	 * js支付
	 *
	 * */
	@RequestMapping(value = "/account/user/antiDogPay", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	String antiDogPay(HttpServletRequest request,HttpSession session) throws Exception {

		//获取统一支付接口参数
		Map prepayInfo = accountService.getPrepayInfo(request, session, "insuranceService");
		prepayInfo.put("feeType", "insurance");
		System.out.println("feeType:" + prepayInfo.get("feeType").toString());
		//拼装jsPay所需参数,如果prepay_id生成成功则将信息放入account_pay_record表
		String userId = UserUtils.getUser().getId();//patientRegisterService.getUserIdByPatientRegisterId(patientRegisterId);

		String payParameter = accountService.assemblyPayParameter(request,prepayInfo,session,userId, null);
		return payParameter;
	}


	/**
	 * js支付
	 *
	 * */
	@RequestMapping(value = "/account/user/customerPay", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	String customerPay(HttpServletRequest request,HttpSession session) throws Exception {

		//获取统一支付接口参数
		Map prepayInfo = accountService.getPrepayInfo(request, session, "customerService");
		prepayInfo.put("feeType", "customer");
		System.out.println("feeType:"+prepayInfo.get("feeType").toString());
		//拼装jsPay所需参数,如果prepay_id生成成功则将信息放入account_pay_record表
		String userId = UserUtils.getUser().getId();//patientRegisterService.getUserIdByPatientRegisterId(patientRegisterId);

		String payParameter = accountService.assemblyPayParameter(request,prepayInfo,session,userId, null);
		return payParameter;
	}

	/**
	 * js支付
	 *
	 * */
	@RequestMapping(value = "/account/user/consultPhonePay", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	String consultPhonePay(HttpServletRequest request,HttpSession session) throws Exception {
		String consultPhoneServiceId = request.getParameter("patientRegisterId");
		Map<String,Object> consultMap = consultPhonePatientService.getPatientRegisterInfo(Integer.parseInt(consultPhoneServiceId));
		request.setAttribute("payPrice", consultMap.get("price"));
		//获取统一支付接口参数
		Map prepayInfo = accountService.getPrepayInfo(request, session, "consultPhone");
		prepayInfo.put("feeType", "consultPhone");
		System.out.println("feeType:"+prepayInfo.get("feeType").toString());
		//拼装jsPay所需参数,如果prepay_id生成成功则将信息放入account_pay_record表
		String userId = UserUtils.getUser().getId();//patientRegisterService.getUserIdByPatientRegisterId(patientRegisterId);
		String payParameter = accountService.assemblyPayParameter(request,prepayInfo,session,userId, null);
		return payParameter;
	}

	/**
	 * 接收支付成后微信notify_url参数中传来的参数
	 * 支付完成 后服务器故障 事物无法回滚
	 * */
	@RequestMapping(value = "/user/getPayNotifyInfo", method = {RequestMethod.POST, RequestMethod.GET})
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
				patientRegisterService.handleWxMemberServiceNotifyInfo(accountNeedPay,payRecord,
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
	@RequestMapping(value = "/user/getInsurancePayNotifyInfo", method = {RequestMethod.POST, RequestMethod.GET})
	public synchronized
	@ResponseBody
	String getInsurancePayNotifyInfo(HttpServletRequest request) {
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
//				InsuranceRegisterService insurance = insuranceService.getInsuranceRegisterServiceById(orderId);
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
			if("SUCCESS".equals(map.get("return_code"))){
				LogUtils.saveLog(Servlets.getRequest(), "00000048","用户微信支付完成:" + map.get("out_trade_no"));
				PayRecord payRecord = new PayRecord();
				payRecord.setId((String) map.get("out_trade_no"));
				payRecord.setStatus("success");
				payRecord.setReceiveDate(new Date());

				Map<String,Object> consultPhoneMap= insuranceService.getPayRecordById(payRecord.getId());
				String consultPhoneId= consultPhoneMap.get("order_id").toString();
				System.out.println("orderId:"+consultPhoneId);
//				InsuranceRegisterService insurance = insuranceService.getInsuranceRegisterServiceById(orderId);
				if(consultPhoneMap.get("fee_type").toString().equals("consultPhone")){
					ConsultPhoneRegisterServiceVo consultPhoneVo = new ConsultPhoneRegisterServiceVo();
					consultPhoneVo.setId(Integer.parseInt(consultPhoneId));
					consultPhoneVo.setState("1");
					consultPhoneVo.setUpdateTime(new Date());

					consultPhonePatientService.updateOrderInfoBySelect(consultPhoneVo);
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
	 *检查订单的支付情况
	 * 支付完成 后服务器故障 事物无法回滚
	 * */
	@RequestMapping(value = "/user/checkAppointment", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	Map<String,Object> checkAppointment(){
		Map a = new HashMap();
		Boolean result = accountService.checkAppointmentPayState("0ed32992422e4aafafd92b83b428531d");
		System.out.print(result);
		return a;
	}
}
