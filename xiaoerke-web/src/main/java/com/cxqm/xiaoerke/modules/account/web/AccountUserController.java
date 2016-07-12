package com.cxqm.xiaoerke.modules.account.web;

import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.account.service.AccountService;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhonePatientService;
import com.cxqm.xiaoerke.modules.order.service.PatientRegisterService;
import com.cxqm.xiaoerke.modules.sys.entity.PerAppDetInfoVo;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
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
	private ConsultPhonePatientService consultPhonePatientService;

	private static Lock lock = new ReentrantLock();

	/**
	 *用户的账户信息
	 */
	@RequestMapping(value = "/account/user/accountInfo", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	Map<String, Object> accountInfo() {
		DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

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
		DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

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
		DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

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
		DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

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
		DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

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
		DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

		String consultPhoneServiceId = request.getParameter("patientRegisterId");
		Map<String,Object> consultMap = consultPhonePatientService.getPatientRegisterInfo(Integer.parseInt(consultPhoneServiceId));
		if("待支付".equals(consultMap.get("state"))){
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
		SortedMap<Object,Object> params = new TreeMap<Object,Object>();
		params.put("agent","7");
		return JSONObject.fromObject(params).toString();
	}


	/**
	 * js支付
	 *
	 * */
	@RequestMapping(value = "/account/user/umbrellaPay", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	String umbrellaPay(HttpServletRequest request,HttpSession session) throws Exception {
		DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

		//获取统一支付接口参数
		Map prepayInfo = accountService.getPrepayInfo(request, session, "umbrellaService");
		prepayInfo.put("feeType", "umbrella");
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
	@RequestMapping(value = "/account/user/lovePlanPay", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	String lovePlanPay(HttpServletRequest request,HttpSession session) throws Exception {
		String leaveNote = request.getParameter("leaveNote");
//		if(leaveNote != null) {
//			leaveNote = new String(leaveNote.getBytes("ISO-8859-1"), "utf-8");
//		}
		Integer donationType = null;
		if(StringUtils.isNotNull(request.getParameter("donationType"))){
			donationType = Integer.valueOf(request.getParameter("donationType"));
		}

		DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
		//获取统一支付接口参数
		request.setAttribute("feeType", "lovePlan");
		request.setAttribute("leaveNote", leaveNote);
		request.setAttribute("donationType", donationType);
		Map prepayInfo = accountService.getPrepayInfo(request, session, "lovePlanService");
		prepayInfo.put("feeType", "lovePlan");
		System.out.println("feeType:" + prepayInfo.get("feeType").toString());
		//拼装jsPay所需参数,如果prepay_id生成成功则将信息放入account_pay_record表
		String userId = UserUtils.getUser().getId();
		String payParameter = accountService.assemblyPayParameter(request,prepayInfo,session,userId, null);
		return payParameter;
	}

	/**
	 * js支付
	 *
	 * */
	@RequestMapping(value = "/account/user/doctorConsultPay", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	String doctorConsultPay(HttpServletRequest request,HttpSession session) throws Exception {
		DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
		//获取统一支付接口参数
		String payType = (String)request.getAttribute("payType");
		request.setAttribute("feeType", payType);
		Map prepayInfo = accountService.getPrepayInfo(request, session, "doctorConsultPay");
		prepayInfo.put("feeType",payType);
//		System.out.println("feeType:" + prepayInfo.get("feeType").toString());
		//拼装jsPay所需参数,如果prepay_id生成成功则将信息放入account_pay_record表
		String userId = UserUtils.getUser().getId();
		String payParameter = accountService.assemblyPayParameter(request,prepayInfo,session,userId, null);
		return payParameter;
	}

}
