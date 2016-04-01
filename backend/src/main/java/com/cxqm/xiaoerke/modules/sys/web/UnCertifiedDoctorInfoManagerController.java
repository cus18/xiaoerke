package com.cxqm.xiaoerke.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.modules.sys.entity.UnCertifiedDoctorInfo;
import com.cxqm.xiaoerke.modules.sys.service.UnCertifiedDoctorInfoService;

/**
 * 未审核医生
 * @author 张博
 * @version 2015年9月23日22:22:04
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/doctor")
public class UnCertifiedDoctorInfoManagerController {

	@Autowired
	private UnCertifiedDoctorInfoService unCertifiedDoctorInfoService;
	
	@RequiresPermissions("user")
	@RequestMapping(value = "uncertifiedDoctor")
	 public String list(UnCertifiedDoctorInfo doctorInfo,HttpServletRequest request, HttpServletResponse response, Model model) {
			String temp = ((String)request.getParameter("pageNo"));
			Page<UnCertifiedDoctorInfo> pagess = null;
			if(temp==null){
				pagess = new Page<UnCertifiedDoctorInfo>();
			}else{
				Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
				Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
				pagess = new Page<UnCertifiedDoctorInfo>(pageNo,pageSize);
			}
			pagess=unCertifiedDoctorInfoService.findPageUnCertifiedDoctorInfo(pagess, doctorInfo);
	        model.addAttribute("page", pagess);
	        return "modules/sys/undertifiedDoctorInfo";
	}
	
	
}
