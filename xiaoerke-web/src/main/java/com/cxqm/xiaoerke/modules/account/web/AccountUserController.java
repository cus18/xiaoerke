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

}
