package com.cxqm.xiaoerke.modules.member.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cxqm.xiaoerke.modules.consult.entity.ConsultMemberVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultMemberRedsiCacheService;
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
import com.cxqm.xiaoerke.modules.member.entity.MemberservicerelItemservicerelRelationVo;
import com.cxqm.xiaoerke.modules.member.service.MemberService;

/**
 * 订单Controller
 * @author sunxiao
 * @version 2015-12-15
 */
@Controller(value = "BackendMemberController")
@RequestMapping(value = "${adminPath}/member")
public class MemberController extends BaseController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private ConsultMemberRedsiCacheService consultMemberRedsiCacheService;
	
	/**
	 * 会员列表
	 * @param 
	 * @param model
	 * @return 
	 */
	@RequiresPermissions("member:memberList")
	@RequestMapping(value = "memberList")
	public String memberList(MemberservicerelItemservicerelRelationVo vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		String temp = ((String)request.getParameter("pageNo"));
		Page<MemberservicerelItemservicerelRelationVo> pagess = null;
		if(temp==null){
			pagess = new Page<MemberservicerelItemservicerelRelationVo>();
		}else{
			Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
			Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
			pagess = new Page<MemberservicerelItemservicerelRelationVo>(pageNo,pageSize);
		}
		Page<MemberservicerelItemservicerelRelationVo> page = memberService.findMemberServiceList(pagess,vo);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("youxiao", "有效");
		map.put("yituikuan", "已退款");
		map.put("shixiao", "已过期");
		map.put("", "全部");
		model.addAttribute("statusList", map);
		model.addAttribute("page", page);
		model.addAttribute("vo", vo);
		return "modules/member/memberList";
	}
	
	/**
	 * 导出会员数据
	 * sunxiao
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("user")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(MemberservicerelItemservicerelRelationVo vo, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "订单数据"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            List<MemberservicerelItemservicerelRelationVo> list = memberService.getAllMemberServiceList(vo,"exportData");
    		new ExportExcel("订单数据", com.cxqm.xiaoerke.modules.entity.MemberservicerelItemservicerelRelationVo.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/register/patientRegisterList";
    }
	
	/**
	 * 退会员费表单
	 * @param 
	 * @param model
	 * @return 
	 */
	@RequiresPermissions("member:memberList")
	@RequestMapping(value = "refundMembershipFeeForm")
	public String refundMembershipFeeForm(MemberservicerelItemservicerelRelationVo vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		try {
			vo.setNickName(URLDecoder.decode(request.getParameter("nickName"), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("vo", vo);
		return "modules/member/refundMembershipFeeForm";
	}
	
	/**
	 * 退会员费
	 * @param 
	 * @param model
	 * @return 
	 */
	@RequiresPermissions("member:refundMembershipFee")
	@ResponseBody
	@RequestMapping(value = "refundMembershipFee")
	public String refundMembershipFee(MemberservicerelItemservicerelRelationVo vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		JSONObject result = new JSONObject();
		memberService.customerWithdrawMember(vo.getId()+"", Float.valueOf(request.getParameter("price"))*100, vo.getRemark());
		result.put("suc", "suc");
		return result.toString();
	}

	/**
	 * 会员使用情况
	 * @param 
	 * @param model
	 * @return 
	 */
	@RequiresPermissions("member:memberUsageDetail")
	@RequestMapping(value = "memberUsageDetail")
	public String memberUsageDetail(MemberservicerelItemservicerelRelationVo vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		List<Map<String, String>> list = memberService.memberUsageDetail(vo.getId());
		if(list.size()==0){
			model.addAttribute("result", "noDetail");
		}
		model.addAttribute("list", list);
		return "modules/member/memberUsageDetail";
	}
	
	/**
	 * 赠送会员
	 * @param 
	 * @param model
	 * @return 
	 */
	@RequiresPermissions("member:memberList")
	@RequestMapping(value = "giftMemberForm")
	public String giftMemberForm(MemberservicerelItemservicerelRelationVo vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		Map<String, Object> typeMap = new LinkedHashMap<String, Object>();
		typeMap.put("week", "周会员");
		typeMap.put("month", "月会员");
		typeMap.put("quarter", "季会员");
		model.addAttribute("typeMap", typeMap);
		model.addAttribute("MemberservicerelItemservicerelRelationVo", new MemberservicerelItemservicerelRelationVo());
		return "modules/member/giftMemberForm";
	}
	
	/**
	 * 赠送会员
	 * @param 
	 * @param model
	 * @return 
	 */
	@RequiresPermissions("member:giftMember")
	@RequestMapping(value = "giftMember")
	public String giftMember(MemberservicerelItemservicerelRelationVo vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		vo.setSource("bankend");
		memberService.giftMember(vo);
		return "redirect:" + adminPath + "/member/memberList?repage";
	}

	/**
	 * 赠送会员
	 * @param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "consultMemberList")
	public String consultMemberList(ConsultMemberVo vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		String temp = ((String)request.getParameter("pageNo"));
		Page<ConsultMemberVo> pagess = null;
		if(temp==null){
			pagess = new Page<ConsultMemberVo>();
		}else{
			Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
			Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
			pagess = new Page<ConsultMemberVo>(pageNo,pageSize);
		}
		Page<ConsultMemberVo> page = consultMemberRedsiCacheService.findConsultMemberList(vo, pagess);
		model.addAttribute("page", page);
		model.addAttribute("ConsultMemberVo", vo);
		return "modules/member/consultMemberList";
	}


	/**
	 * 更新会员
	 * @param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "updateconsultMember")
	public String updateconsultMemberList(ConsultMemberVo vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		JSONObject result = new JSONObject();
		consultMemberRedsiCacheService.updateRedisConsultInfo(vo.getOpenid());
		return result.toString();
	}

}
