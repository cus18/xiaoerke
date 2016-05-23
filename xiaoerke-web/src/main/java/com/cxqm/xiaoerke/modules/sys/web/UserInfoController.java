/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.web;

import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.CookieUtils;
import com.cxqm.xiaoerke.common.utils.FrontUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.account.entity.Record;
import com.cxqm.xiaoerke.modules.account.service.AccountService;
import com.cxqm.xiaoerke.modules.member.entity.MemberservicerelItemservicerelRelationVo;
import com.cxqm.xiaoerke.modules.member.service.MemberService;
import com.cxqm.xiaoerke.modules.order.service.PatientRegisterService;
import com.cxqm.xiaoerke.modules.sys.entity.MessageVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.interceptor.SystemControllerLog;
import com.cxqm.xiaoerke.modules.sys.service.MessageService;
import com.cxqm.xiaoerke.modules.sys.service.UserInfoService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.*;

/**
 * 测试Controller
 *
 * @author ThinkGem
 * @version 2013-10-17
 */
@Controller
@RequestMapping(value = "")
public class UserInfoController extends BaseController {

    @Autowired
    private  UserInfoService userInfoService;

    @Autowired
	private PatientRegisterService patientRegisterService;
    
    @Autowired
	private AccountService accountService;

	@Autowired
	private MemberService memberService;
    
    /**
     * 获取个人中心主页的信息 @ author 13_zdl_a
     * <p/>
     * params:{"patientId":"few3fe3232"}
     * <p/>
     * response:
     * {
     * "waitPay":"3","waitTreat":"2","waitAppraise":"2","waitShare":"3","waitEmr":"2","account":"30"
     * <p/>
     * }
     */
	@SystemControllerLog(description = "00000080")//获取个人中心主页的信息
	@RequestMapping(value = "/info/user", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> myselfInfo(@RequestBody Map<String, Object> params,HttpSession session,HttpServletRequest request,HttpServletResponse httpResponse) {

		HashMap<String, Object> response = new HashMap<String, Object>();
		String unBindUserPhoneNum = (String)params.get("unBindUserPhoneNum");
		if(unBindUserPhoneNum!=""){
			CookieUtils.setCookie(httpResponse, "unBindUserPhoneNum", unBindUserPhoneNum);
		}else{
			unBindUserPhoneNum = CookieUtils.getCookie(request,"unBindUserPhoneNum");
		}
		User user = UserUtils.getUser();
		String userId = user.getId();
		String userPhone = user.getPhone();
		if(userId!=null){
			String patientId = userInfoService.getPatientIdByUserId(userId);
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("patientId", patientId);
			HashMap<String, Object> resultMap = patientRegisterService.getPerCenterPageInfo(hashMap);//获取个人中心主页的信息
			resultMap.put("userPhone",userPhone);
			resultMap.put("patientId", patientId);
			resultMap.put("userName", session.getAttribute("wechatName"));
			Float account = accountService.accountFund(userId);
			resultMap.put("accountFund",account/100);
			List<Record> recordList = accountService.getAccountDetail(userId);
			String BondSwitch = Global.getConfig("webapp.BondSwitch");
			Boolean Bl = new Boolean(BondSwitch);
			resultMap.put("switchStatus",Bl);
			List<MemberservicerelItemservicerelRelationVo> memberList = memberService.findMemberPropertyAppAvailable();
			int memberNum = 0;
			if(null !=memberList && memberList.size()>0){
				for(MemberservicerelItemservicerelRelationVo vo:memberList){
					memberNum += vo.getLeftTimes();
				}
			}
			resultMap.put("memberNum", memberNum);
			if(recordList.size()>0){
				resultMap.put("bondSwitch",true);
			}else{
				if("false".equals(BondSwitch)){
					resultMap.put("bondSwitch",false);
				}else{
					resultMap.put("bondSwitch",true);
				}
			}
			if (resultMap != null && !resultMap.isEmpty()) {
				return resultMap;
			}


		}else{
			//根据手机号查询订单
			String BondSwitch = Global.getConfig("webapp.BondSwitch");
			Boolean Bl = new Boolean(BondSwitch);
			response.put("bondSwitch", Bl);
			response.put("switchStatus", Bl);
			response.put("memberNum", "0");
			response.put("accountFund","0");
			response.put("userPhone",userPhone);

			List<HashMap<Integer, Object>> appointmentInfo = patientRegisterService.getUnBindUserOrder(unBindUserPhoneNum);//myselfService.getUnBindUserOrder(unBindUserPhoneNum);//获取个人中心主页的信息
			for(HashMap<Integer, Object> map:appointmentInfo){

				Integer appointNum = Integer.parseInt((String)map.get("status"));
				switch (appointNum){
					case 0: response.put("waitPay",1); break;
					case 1 : response.put("waitTreat",1); break;
					case 2: response.put("waitAppraise",1); break;
					default:break;
				}
			}

			return response;
		}
		// 需要根据userId去获取patientId
		return null;
    }


	//获取用户登陆状态
	@RequestMapping(value = "auth/info/loginStatus", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	HashMap<String,Object> loginStatus(@RequestBody Map<String, Object> params,HttpSession session,HttpServletRequest request) {
		HashMap<String,Object> result = new HashMap<String,Object>();
		result.put("status","20");
		User user = UserUtils.getUser();
		result.put("userPhone",user.getPhone());
		result.put("userId",user.getId());
		result.put("userName",user.getName());
		result.put("userType",user.getUserType());
//		if(StringUtils.isNotNull(user.getOpenid())){
//			result.put("openId",user.getOpenid());
//		}else{
			String openId = WechatUtil.getOpenId(session,request);
			if(StringUtils.isNotNull(openId)){
				result.put("openId",openId);
			}else{
				result.put("openId","noOpenId");
			}
//		}
		return result;
	}

}
