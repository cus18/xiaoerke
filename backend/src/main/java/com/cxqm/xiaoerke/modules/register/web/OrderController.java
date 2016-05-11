package com.cxqm.xiaoerke.modules.register.web;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cxqm.xiaoerke.modules.account.service.AccountService;
import com.cxqm.xiaoerke.modules.account.service.PayRecordService;

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
import com.cxqm.xiaoerke.modules.utils.excel.ExportExcel;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.order.entity.OrderPropertyVo;
import com.cxqm.xiaoerke.modules.order.entity.PatientRegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.entity.RegisterServiceVo;
import com.cxqm.xiaoerke.modules.order.entity.UserReturnVisitVo;
import com.cxqm.xiaoerke.modules.order.service.PatientRegisterService;
import com.cxqm.xiaoerke.modules.order.service.RegisterService;
import com.cxqm.xiaoerke.modules.sys.entity.RemovedOrderNotification;
import com.cxqm.xiaoerke.modules.sys.service.NotificationService;
import com.cxqm.xiaoerke.modules.wechat.dao.WechatAttentionDao;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;

/**
 * 订单Controller
 * @author frank
 * @version 2015-11-09
 */
@Controller(value = "BackendOrderController")
@RequestMapping(value = "${adminPath}/order")
public class OrderController extends BaseController {

	@Autowired
	private RegisterService registerService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private PatientRegisterService patientRegisterService;

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private PayRecordService payRecordService;
	
	@Autowired
	private WechatAttentionDao wechatAttentionDao;

	/**
	 * 判断是否有新订单
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "getNewOrderList")
	public String getNewOrderList(PatientRegisterServiceVo patientRegisterServiceVo,HttpServletRequest request,HttpServletResponse response, Model model) {
		JSONObject result = new JSONObject();
		patientRegisterServiceVo.setStatus("1");
		Page<PatientRegisterServiceVo> page = patientRegisterService.findRegisterPatientList(new Page<PatientRegisterServiceVo>(), patientRegisterServiceVo, "");
		result.put("orderCount", page.getCount());
		return result.toString();
	}
	
	/**
	 * 预约患者列表
	 * @param status为0表示待支付，1表示待就诊，2表示待评价，3表示待分享，4表示待建档,6表示已取消，显示不同状态的预订
	 * @param model
	 * @return 
	 */
	@RequiresPermissions("order:patientRegisterList")
	@RequestMapping(value = "patientRegisterList")
	public String patientRegisterList(PatientRegisterServiceVo patientRegisterServiceVo,HttpServletRequest request,HttpServletResponse response, Model model) {
		String getvo = request.getParameter("getvo");
		String pg = request.getParameter("flag");
		String warn = request.getParameter("warn");
		if("yes".equals(getvo)){
			model.addAttribute("patientRegisterServiceVo",request.getSession().getAttribute("prsvs"));
			patientRegisterServiceVo=(PatientRegisterServiceVo) request.getSession().getAttribute("prsvs");
			model.addAttribute("warn", patientRegisterServiceVo.getWarnFlag());
		}else{
			if("1".equals(warn)){
				patientRegisterServiceVo.setWarnFlag("1");
				model.addAttribute("warn", "1");
			}
			request.getSession().setAttribute("prsvs", patientRegisterServiceVo);
		}
		
		Page<PatientRegisterServiceVo> pagess = null;
		if("yes".equals(pg)){
			Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
			Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
			pagess = new Page<PatientRegisterServiceVo>(pageNo,pageSize);
		}else{
			pagess = new Page<PatientRegisterServiceVo>();
		}
		patientRegisterServiceVo.setStatus(patientRegisterServiceVo.getStatus()==null?"1":patientRegisterServiceVo.getStatus());
		Page<PatientRegisterServiceVo> page = patientRegisterService.findRegisterPatientList(pagess, patientRegisterServiceVo, warn);
		Map<String, Object> statusMap = new HashMap<String, Object>();
		statusMap.put("1", "待就诊");
		statusMap.put("2", "待评价");
		statusMap.put("3", "待分享");
		statusMap.put("6", "已取消");
		statusMap.put("", "全部");
		Map<String, Object> relationTypeMap = new HashMap<String, Object>();
		relationTypeMap.put("1", "公立医院");
		relationTypeMap.put("2", "合作机构");
		relationTypeMap.put("", "全部");
		model.addAttribute("statusList", statusMap);
		model.addAttribute("relationTypeMap", relationTypeMap);
		model.addAttribute("page", page);
		return "modules/register/alreadyRegisterList";
	}
	
	/**
	 * 已删除订单列表
	 * @param 
	 * @param model
	 * @return 
	 */
	@RequiresPermissions("order:removedOrderList")
	@RequestMapping(value = "removedOrderList")
	public String removedOrderList(PatientRegisterServiceVo patientRegisterServiceVo,HttpServletRequest request,HttpServletResponse response, Model model) {
		String flag = request.getParameter("pageNo");
		Page<RemovedOrderNotification> pagess = null;
		if(flag!=null){
			Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
			Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
			pagess = new Page<RemovedOrderNotification>(pageNo,pageSize);
		}else{
			pagess = new Page<RemovedOrderNotification>();
		}
		Page<RemovedOrderNotification> pageList = notificationService.findRemovedOrderNotifications(pagess);
		model.addAttribute("page", pageList);
		return "modules/register/removeOrderList";
	}
	
	/**
	 * 得到新的已删除订单列表
	 * @param 
	 * @param model
	 * @return 
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "getNewRemovedOrderList")
	public String getNewRemovedOrderList(PatientRegisterServiceVo patientRegisterServiceVo,HttpServletRequest request,HttpServletResponse response, Model model) {
		JSONObject result = new JSONObject();
		Page<RemovedOrderNotification> page = new Page<RemovedOrderNotification>();
		
		Page<RemovedOrderNotification> pageList = notificationService.findRemovedOrderNotifications(page);
		result.put("removedOrderCount", pageList.getCount());
		return result.toString();
	}
	
	/**
	 * 导出订单数据
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("user")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(PatientRegisterServiceVo patientRegisterServiceVo, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "订单数据"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            List<PatientRegisterServiceVo> list = patientRegisterService.getPatientRegisterList(patientRegisterServiceVo);
    		new ExportExcel("订单数据", PatientRegisterServiceVo.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/register/patientRegisterList";
    }
	
	/**
	 * 查看患者预约详细信息
	 * @param model
	 * @return
	 */
	@RequiresPermissions("order:returnVisitDetail")
	@RequestMapping(value = "patientReturnVisitDetail")
	public String patientReturnVisitDetail(PatientRegisterServiceVo patientRegisterServiceVo,HttpServletRequest request,HttpServletResponse response, Model model) {
		String patientId = request.getParameter("id");
		String sysRegisterId = request.getParameter("sysRegisterId");
		String pageNo = request.getParameter("pageNo");
		String pageSize = request.getParameter("pageSize");
    	HashMap<String,Object> retMap = patientRegisterService.patientReturnVisitDetail(patientId ,sysRegisterId);
		model.addAttribute("patientMap", retMap.get("patientMap"));
		model.addAttribute("registerMap", retMap.get("registerMap"));
		model.addAttribute("UserReturnVisitVo", retMap.get("UserReturnVisitVo"));
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("pageSize", pageSize);
		return "modules/register/patientRegisterInfo";
	}
	
	/**
	 * 查看患者预约详细信息
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "saveUserReturnVisit")
	public String saveUserReturnVisit(UserReturnVisitVo userReturnVisitVo,HttpServletRequest request,HttpServletResponse response, RedirectAttributes redirectAttributes){
		String pageNo = request.getParameter("pageNo");
		String pageSize = request.getParameter("pageSize");

		userReturnVisitVo.setFalseUserReasonRemarks(userReturnVisitVo.getFalseUserReasonRemarks()==null?"": userReturnVisitVo.getFalseUserReasonRemarks());
		patientRegisterService.saveUserReturnVisit(userReturnVisitVo);

		addMessage(redirectAttributes, "保存回访信息成功！");
		return "redirect:" + adminPath + "/order/patientRegisterList?getvo=yes&flag=yes&pageNo="+pageNo+"&pageSize="+pageSize;
	}
	
	/**
	 * 进入预约页
	 * @param model
	 * @return
	 */
	@RequiresPermissions("order:appointment")
	@RequestMapping(value = "appointmentForm")
	public String appointmentForm(HttpServletRequest request,HttpServletResponse response, Model model) {
		String registerId = request.getParameter("id");
		model.addAttribute("registerId", registerId);
		PatientRegisterServiceVo patientRegisterServiceVo = new PatientRegisterServiceVo();
		model.addAttribute("patientRegisterServiceVo", patientRegisterServiceVo);
		return "modules/register/appointmentForm";
	}
	
	/**
	 * 预约
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "saveAppointment")
	public String saveAppointment(HttpServletRequest request,PatientRegisterServiceVo patientRegisterServiceVo,HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
		RegisterServiceVo vo = registerService.getRegisterByInfo(patientRegisterServiceVo.getSysRegisterServiceId());
		if(vo!=null){
			
			//判断此订单是不是扫码预约用户（根据openid查marketer）
            SysWechatAppintInfoVo sysWechatAppintInfoVo = new SysWechatAppintInfoVo();
            OrderPropertyVo orderPropertyVo = new OrderPropertyVo();
            sysWechatAppintInfoVo.setOpen_id(orderPropertyVo.getOpenid());
            sysWechatAppintInfoVo.setPatient_register_service_id(orderPropertyVo.getPatientRegisterServiceId());
            List<SysWechatAppintInfoVo> attendVos = wechatAttentionDao.findAttentionInfoByOpenIdLists(sysWechatAppintInfoVo);
            System.out.println("判断是不是扫码用户？"+attendVos.size());

            if (attendVos.size() > 0) {
            	orderPropertyVo.setScanCode("yes");//是扫码关注的
                System.out.println("是扫码用户"+attendVos.size());
            } else {
            	orderPropertyVo.setScanCode("no");
                System.out.println("不是扫码用户"+attendVos.size());

            }
            
			patientRegisterService.savePatientRegisterService(patientRegisterServiceVo, orderPropertyVo);
			
			addMessage(redirectAttributes, "保存成功！");
		}else{
			addMessage(redirectAttributes, "该号源已被占用！");
		}
		return "redirect:" + adminPath + "/register/registerList?";
	}

	
	/**
	 * 取消预约
	 * @return
	 */
	@RequiresPermissions("order:cancelOrder")
	@ResponseBody
	@RequestMapping(value = "cancelAppointment")
	public String cancelAppointment(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		JSONObject result = new JSONObject();
		String registerId = request.getParameter("sysRegisterServiceId");
		String patientRegisterId = request.getParameter("patientRegisterId");
		String memberServiceId = request.getParameter("memberServiceId");
		String reason = request.getParameter("reason");
		String keep = request.getParameter("keep");
		patientRegisterService.cancelAppointment(registerId,patientRegisterId,memberServiceId,keep,reason,"kefu");
		addMessage(redirectAttributes, "取消预约成功！");
		result.put("suc", "suc");
		return result.toString();
	}

	/**
	 * sunxiao
	 * 取消预约
	 * @return
	 */
	@RequiresPermissions("order:cancelOrder")
	@RequestMapping(value = "cancelAppointmentForm")
	public String cancelAppointmentForm(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
		String registerId = request.getParameter("sysRegisterId");
		String registerNo = request.getParameter("registerNo");
		String patientRegisterId = request.getParameter("patientId");
		String memberServiceId = request.getParameter("memberServiceId");
		model.addAttribute("registerId", registerId);
		model.addAttribute("registerNo", registerNo);
		model.addAttribute("patientRegisterId", patientRegisterId);
		model.addAttribute("memberServiceId", memberServiceId);
		return "modules/register/cancelAppointmentForm";
	}
}
