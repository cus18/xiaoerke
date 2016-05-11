package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.OSSObjectTool;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorInfoVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultDoctorInfoService;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.UserInfoService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;

/**
 * 咨询Controller
 * @author sunxiao
 * @version 2016-4-26
 */
@Controller(value = "ConsultController")
@RequestMapping(value = "${adminPath}/consult")
public class ConsultController extends BaseController {

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private ConsultDoctorInfoService consultDoctorInfoService;

	/**
	 * 咨询医生列表
	 * sunxiao
	 * @param
	 * @param model
	 */
	@RequestMapping(value = "consultDoctorList")
	public String consultDoctorList(User user,HttpServletRequest request,HttpServletResponse response, Model model) {
		String temp = ((String)request.getParameter("pageNo"));
		Page<User> pagess = null;
		if(temp==null){
			pagess = new Page<User>();
		}else{
			Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
			Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
			pagess = new Page<User>(pageNo,pageSize);
		}
		Page<User> page = userInfoService.findUserList(pagess, user);
		model.addAttribute("vo", new User());
		model.addAttribute("page", page);
		return "modules/consult/doctorList";
	}

	/**
	 * 咨询医生操作页面
	 * sunxiao
	 * @param
	 * @param model
	 */
	@RequestMapping(value = "consultOperForm")
	public String consultOperForm(User user,HttpServletRequest request,HttpServletResponse response, Model model) {
		List<User> list = userInfoService.getUserListByInfo(user);
		model.addAttribute("user", list.get(0));
		return "modules/consult/doctorOperForm";
	}

	/**
	 * 添加修改医生信息
	 * @param
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "doctorOper")
	public String doctorOper(User user,HttpServletRequest request,HttpServletResponse response, Model model) {
		JSONObject result = new JSONObject();
		try {
			userInfoService.doctorOper(user);
			result.put("result","suc");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result","fail");
		}
		return result.toString();
	}

	/**
	 * 咨询医生更多设置
	 * sunxiao
	 * @param
	 * @param model
	 */
	@RequestMapping(value = "doctorMoreSettingForm")
	public String doctorMoreSettingForm(User user,HttpServletRequest request,HttpServletResponse response, Model model) {
		Map map = consultDoctorInfoService.getConsultDoctorInfo(user);
		model.addAttribute("user", map.get("user"));
		model.addAttribute("doctor", map.get("doctor"));
		model.addAttribute("redPacket", map.get("redPacket"));
		model.addAttribute("satisfy", map.get("satisfy"));
		model.addAttribute("unsatisfy", map.get("unsatisfy"));
		model.addAttribute("sessionCount", map.get("sessionCount"));
		model.addAttribute("sessionMap", map.get("sessionMap"));
		return "modules/consult/doctorMoreSettingForm";
	}

	/**
	 * 添加修改医生信息
	 * @param
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "doctorInfoOper")
	public String doctorInfoOper(ConsultDoctorInfoVo doctor,HttpServletRequest request,HttpServletResponse response, Model model) {
		JSONObject result = new JSONObject();
		try {
			consultDoctorInfoService.consultDoctorInfoOper(doctor);
			result.put("result","suc");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result","fail");
		}
		return result.toString();
	}

	/**
	 * 新增医生
	 * @return
	 */
	@RequestMapping(value = "fileUpload")
	public String fileUpload(@RequestParam("files") MultipartFile file,String id, Model model,RedirectAttributes redirectAttributes) {
		if (!file.isEmpty()) {
			try {
				InputStream inputStream = file.getInputStream();
				long length = file.getSize();
				//上传图片至阿里云
				OSSObjectTool.uploadFileInputStream(id, length, inputStream, OSSObjectTool.BUCKET_DOCTOR_PIC);
				inputStream.close();
				addMessage(redirectAttributes, "上传头像成功！");
			} catch (Exception e) {
				e.printStackTrace();
				addMessage(redirectAttributes, "上传头像失败！");
			}
		}
		return "redirect:" + adminPath + "/consult/doctorMoreSettingForm?id="+id;
	}

}
