package com.cxqm.xiaoerke.modules.insurance.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.excel.ExportExcel;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.account.service.AccountService;
import com.cxqm.xiaoerke.modules.insurance.entity.InsuranceHospitalVo;
import com.cxqm.xiaoerke.modules.insurance.entity.InsuranceRegisterService;
import com.cxqm.xiaoerke.modules.insurance.service.InsuranceHospitalService;
import com.cxqm.xiaoerke.modules.insurance.service.InsuranceRegisterServiceService;
import com.cxqm.xiaoerke.modules.sys.service.BabyBaseInfoService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;

/**
 * 保险Controller
 * @author sunxiao
 * @version 2016-3-15
 */
@Controller(value = "InsuranceController")
@RequestMapping(value = "${adminPath}/insurance")
public class InsuranceController extends BaseController {

	@Autowired
	private InsuranceRegisterServiceService insuranceRegisterService;
	
	@Autowired
	BabyBaseInfoService babyBaseInfoService;
	
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private InsuranceHospitalService insuranceHospitalService;
	
	/**
	 * 防犬宝的订单列表
	 * sunxiao
	 * @param 
	 * @param model
	 * @return 
	 */
	@RequiresPermissions("insurance:insuranceList")
	@RequestMapping(value = "insuranceList")
	public String insuranceList(InsuranceRegisterService vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		String temp = ((String)request.getParameter("pageNo"));
		Page<InsuranceRegisterService> pagess = null;
		if(temp==null){
			pagess = new Page<InsuranceRegisterService>();
		}else{
			Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
			Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
			pagess = new Page<InsuranceRegisterService>(pageNo,pageSize);
		}
		String returnUrl = "";
		if("2".equals(request.getParameter("stateFlag"))){//待审核
			vo.setState("2");
			returnUrl = "modules/insurance/pendingAuditList";
		}else if("3".equals(request.getParameter("stateFlag"))){//已补贴
			vo.setState("3");
			returnUrl = "modules/insurance/alreadySubsidizedList";
		}else{
			returnUrl = "modules/insurance/insuranceList";
		}
		Page<InsuranceRegisterService> page = insuranceRegisterService.findInsuranceServiceList(pagess,vo);
		Map<String, Object> statusMap = new LinkedHashMap<String, Object>();
		statusMap.put("0", "待生效");
		statusMap.put("1", "有效");
		statusMap.put("2", "待审核");
		statusMap.put("3", "已赔付");
		statusMap.put("4", "已到期");
		statusMap.put("5", "审核失败");
		statusMap.put("6", "待支付");
		statusMap.put("", "全部");
		model.addAttribute("statusList", statusMap);
		model.addAttribute("page", page);
		model.addAttribute("vo", vo);
		return returnUrl;
	}
	
	/**
	 * 导出订单数据
	 * sunxiao
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("user")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(InsuranceRegisterService insuranceRegisterVo, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "订单数据"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            List<InsuranceRegisterService> list = insuranceRegisterService.getInsuranceServiceList(insuranceRegisterVo);
    		new ExportExcel("订单数据", InsuranceRegisterService.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/register/patientRegisterList";
    }
	
	/**
	 * 防犬宝的订单详情
	 * sunxiao
	 * @param 
	 * @param model
	 * @return 
	 */
	@RequiresPermissions("insurance:orderDetails")
	@RequestMapping(value = "orderDetails")
	public String orderDetails(InsuranceRegisterService vo,HttpServletRequest request,HttpServletResponse response, Model model) {
			InsuranceRegisterService irsvo = insuranceRegisterService.getInsuranceRegisterServiceById(vo.getId()+"");
			model.addAttribute("irsvo", irsvo);
		return "modules/insurance/orderDetails";
	}
	
	/**
	 * 弹出是否审批通过页面
	 * sunxiao
	 * @param 
	 * @param model
	 * @return 
	 */
	@RequiresPermissions("insurance:orderDetails")
	@RequestMapping(value = "auditForm")
	public String auditForm(InsuranceRegisterService vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		String returnUrl = "";
		try {
			vo.setNickName(URLDecoder.decode(request.getParameter("nickName") , "utf-8"));
			if("3".equals(vo.getState())){
				returnUrl = "modules/insurance/auditPassForm";
			}else if("5".equals(vo.getState())){
				returnUrl = "modules/insurance/auditNotPassForm";
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("irsvo", vo);
		return returnUrl;
	}
	
	/**
	 * 审批
	 * sunxiao
	 * @param 
	 * @param model
	 * @return 
	 */
	@RequiresPermissions("insurance:orderDetails")
	@ResponseBody
	@RequestMapping(value = "insuranceAudit")
	public String insuranceAudit(InsuranceRegisterService vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		JSONObject result = new JSONObject();
		try{
			vo.setUpdateBy(UserUtils.getUser().getName());
			vo.setUpdateTime(new Date());
			if("3".equals(vo.getState())){//审核通过，已赔付
				accountService.updateAccount(vo.getPrice()*100, vo.getId(), new HashMap<String, Object>(), false, vo.getParentId(),vo.getAuditReason());//补贴钱给用户
			}
			insuranceRegisterService.updateInsuranceRegisterService(vo);
			result.put("suc", "suc");
		}catch(Exception e){
			result.put("suc", "fail");
		}
		return result.toString();
	}
	
	/**
	 * 赠送保险form
	 * sunxiao
	 * @param 
	 * @param model
	 * @return 
	 */
	@RequiresPermissions("insurance:insuranceList")
	@RequestMapping(value = "giveInsuranceForm")
	public String giveInsuranceForm(InsuranceRegisterService vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		model.addAttribute("InsuranceRegisterService", new InsuranceRegisterService());
		return "modules/insurance/giveInsuranceForm";
	}
	
	/**
	 * 赠送保险
	 * sunxiao
	 * @param 
	 * @param model
	 * @return 
	 */
	@RequiresPermissions("insurance:insuranceList")
	@RequestMapping(value = "giveInsurance")
	public String giveInsurance(InsuranceRegisterService vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		String retStr = insuranceRegisterService.giveInsuranceRegisterService(vo);
		if("yes".equals(retStr)){
			return "redirect:" + adminPath + "/insurance/insuranceList?repage";
		}else{
			vo.setBirthday(null);
			model.addAttribute("message", "该用户已买保险！");
			model.addAttribute("InsuranceRegisterService", vo);
			return "modules/insurance/giveInsuranceForm";
		}
	}
	
	/**
	 * 保险关联医院列表
	 * sunxiao
	 * @param 
	 * @param model
	 * @return 
	 */
	@RequiresPermissions("insurance:insuranceList")
	@RequestMapping(value = "insuranceHospitalList")
	public String insuranceHospitalList(InsuranceHospitalVo vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		String temp = ((String)request.getParameter("pageNo"));
		Page<InsuranceHospitalVo> pagess = null;
		if(temp==null){
			pagess = new Page<InsuranceHospitalVo>();
		}else{
			Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
			Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
			pagess = new Page<InsuranceHospitalVo>(pageNo,pageSize);
		}
		Page<InsuranceHospitalVo> pageList = insuranceHospitalService.findInsuranceHospitalListByInfo(pagess, vo);
		model.addAttribute("page", pageList);
		model.addAttribute("vo", vo);
		return "modules/insurance/insuranceHospitalList";
	}
	
	/**
	 * 保存保险关联医院
	 * sunxiao
	 * @param 
	 * @param model
	 * @return 
	 */
	@RequiresPermissions("insurance:insuranceList")
	@RequestMapping(value = "saveInsuranceHospitalForm")
	public String saveInsuranceHospitalForm(InsuranceHospitalVo vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		model.addAttribute("insuranceHospitalVo", vo);
		return "modules/insurance/addInsuranceHospitalForm";
	}
	
	/**
	 * 保存保险关联医院
	 * sunxiao
	 * @param 
	 * @param model
	 * @return 
	 */
	@RequiresPermissions("insurance:insuranceList")
	@RequestMapping(value = "saveInsuranceHospital")
	public String saveInsuranceHospital(InsuranceHospitalVo vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		try{
			int add = insuranceHospitalService.saveInsuranceHospital(vo);
			if(add==1){
				model.addAttribute("message", "保存医院成功！");
			}else{
				model.addAttribute("message", "保存医院失败！");
			}
		}catch(Exception e){
			model.addAttribute("message", "保存医院异常！");
		}
		return "modules/insurance/addInsuranceHospitalForm";
	}
}
