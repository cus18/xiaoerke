package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.HttpRequestUtil;
import com.cxqm.xiaoerke.common.utils.OSSObjectTool;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.account.service.AccountService;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorInfoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorTimeGiftVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionPropertyVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultDoctorInfoService;
import com.cxqm.xiaoerke.modules.entity.DissatisfiedVo;
import com.cxqm.xiaoerke.modules.entity.ReceiveTheMindVo;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.member.entity.MemberservicerelItemservicerelRelationVo;
import com.cxqm.xiaoerke.modules.sys.dao.UtilDao;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.entity.ValidateBean;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.service.UserInfoService;
import com.cxqm.xiaoerke.modules.sys.service.UtilService;
import com.cxqm.xiaoerke.modules.sys.utils.WechatMessageUtil;
import com.cxqm.xiaoerke.modules.umbrella.entity.UmbrellaMongoDBVo;
import com.cxqm.xiaoerke.modules.umbrella.service.BabyUmbrellaInfoService;
import com.cxqm.xiaoerke.modules.umbrella.serviceimpl.UmbrellaMongoDBServiceImpl;
import com.cxqm.xiaoerke.modules.utils.excel.ExportExcel;
import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

	@Autowired
	private PatientRegisterPraiseService patientRegisterPraiseService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private UtilDao utilDao;

	@Autowired
	private UtilService utilService;

	/**
	 * 咨询的医生 列表
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
	public String consultOperForm(User user, Model model) {
		List<User> list = userInfoService.getUserListByInfo(user);
		model.addAttribute("user", list.get(0));
		return "modules/consult/doctorOperForm";
	}

	/**
	 * 验证码查询界面
	 * sunxiao
	 * @param
	 * @param model
	 */
	@RequestMapping(value = "searchVerificationCode")
	public String searchVerificationCode(User user, Model model) {
		user = userInfoService.getUserListByInfo(user).get(0);
		ValidateBean validateBean = utilDao.getIdentifying(user.getPhone());

//		if(validateBean == null || StringUtils.isBlank(validateBean.getName())){
//			ValidateBean bean = new ValidateBean();
//			bean.setId(UUID.randomUUID().toString().replaceAll("-", ""));
//			bean.setUserPhone(num);
//			bean.setCreateTime(new Date());
//			bean.setCode(identify);
//			bean.setStatus("1");
//			int status = utilDao.saveOrUpdateIdentify(bean);
//		}else{
			if(StringUtils.isNotBlank(validateBean.getName())){
				validateBean.setName(user.getName());
			}else{
				validateBean.setName(user.getEmail());
			}
//		}

		model.addAttribute("validateBean", validateBean);
		return "modules/consult/editUserVerificationCode";
	}


	/**
	 * 验证码更新界面
	 * sunxiao
	 * @param
	 */
	@ResponseBody
	@RequestMapping(value = "updateVerificationCode")
	public String updateVerificationCode(ValidateBean validateBean) {
		JSONObject result = new JSONObject();
		try {
			utilService.updateValidateCode(validateBean);
			result.put("result","suc");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result","fail");
		}
		return result.toString();
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
		model.addAttribute("redPacketPerson", map.get("redPacketPerson"));
		model.addAttribute("satisfy", map.get("satisfy"));
		model.addAttribute("verysatisfy", map.get("verysatisfy"));
		model.addAttribute("unsatisfy", map.get("unsatisfy"));
		model.addAttribute("sessionCount", map.get("sessionCount"));
		model.addAttribute("sessionMap", map.get("sessionMap"));
		model.addAttribute("lectureList", map.get("lectureList"));
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
	 * 添加修改医生信息
	 * @param
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveLecture")
	public String saveLecture(ConsultDoctorInfoVo doctor,HttpServletRequest request,HttpServletResponse response, Model model) {
		JSONObject result = new JSONObject();
		try {
			consultDoctorInfoService.saveLecture(doctor);
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

	/**
	 * 退会员费表单
	 * @param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "exportForm")
	public String exportForm(HttpServletRequest request,HttpServletResponse response, Model model) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -1);
		model.addAttribute("yesterday",DateUtils.DateToStr(calendar.getTime(),"date"));
		model.addAttribute("type",request.getParameter("type"));
		return "modules/consult/exportForm";
	}

	/**
	 * 导出会员数据
	 * sunxiao
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "dataExport", method= RequestMethod.POST)
	public String dataExport(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			String type = request.getParameter("type");
			String fileName = "订单数据"+ DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
			Map param = new HashMap();
			param.put("fromDate",fromDate);
			param.put("toDate",toDate);
			if("receiveTheMindExport".equals(type)){
				List<Map<String,Object>> list = patientRegisterPraiseService.findReceiveTheMindList(param);
				List<ReceiveTheMindVo> voList = new ArrayList<ReceiveTheMindVo>();
				for(Map map:list){
					ReceiveTheMindVo vo = new ReceiveTheMindVo();
					vo.setContent((String) map.get("content"));
					vo.setCreatetime(DateUtils.DateToStr((Date) map.get("createtime")));
					vo.setDoctorName((String) map.get("doctorName"));
					vo.setNickname((String)map.get("nickname"));
					vo.setServiceAttitude("3".equals((String)map.get("serviceAttitude"))?"满意":"非常满意");
					vo.setRedPacket((String)map.get("redpacket"));
					vo.setPhone((String)map.get("phone"));
					vo.setOpenid((String)map.get("openid"));
					voList.add(vo);
				}
				new ExportExcel("送心意数据", ReceiveTheMindVo.class).setDataList(voList).write(response, fileName).dispose();
			}else if("dissatisfiedExport".equals(type)){
				List<Map<String,Object>> list = patientRegisterPraiseService.findDissatisfiedList(param);
				List<DissatisfiedVo> voList = new ArrayList<DissatisfiedVo>();
				for(Map map:list){
					DissatisfiedVo vo = new DissatisfiedVo();
					vo.setOpenid((String) map.get("openid"));
					vo.setPhone((String) map.get("phone"));
					vo.setNickname((String) map.get("nickname"));
					vo.setCreatetime(DateUtils.DateToStr((Date) map.get("createtime")));
					vo.setContent((String) map.get("content"));
					vo.setDoctorName((String) map.get("doctorName"));
					if(map.get("dissatisfied")!=null){
						String dissatis = (String)map.get("dissatisfied");
						if(StringUtils.isNotNull(dissatis)){
							if(dissatis.contains("0")){
								vo.setAttitude("是");
							}
							if(dissatis.contains("1")){
								vo.setAbility("是");
							}
							if(dissatis.contains("2")){
								vo.setResponse("是");
							}
							if(dissatis.contains("3")){
								vo.setOther("是");
							}
						}
					}
					voList.add(vo);
				}
				new ExportExcel("不满意数据", DissatisfiedVo.class).setDataList(voList).write(response, fileName).dispose();
			}
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "导出用户失败！失败信息：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 咨询订单列表
	 * sunxiao
	 * @param
	 * @param model
	 */
	@RequestMapping(value = "consultOrderList")
	public String consultOrderList(ConsultDoctorTimeGiftVo vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		String temp = ((String)request.getParameter("pageNo"));
		Page<ConsultDoctorTimeGiftVo> pagess = null;
		if(temp==null){
			pagess = new Page<ConsultDoctorTimeGiftVo>();
		}else{
			Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
			Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
			pagess = new Page<ConsultDoctorTimeGiftVo>(pageNo,pageSize);
		}
		Page<ConsultDoctorTimeGiftVo> page = consultDoctorInfoService.findConsultDoctorOrderListByInfo(pagess, vo);
		Map<String,Object> tradeWayMap = new LinkedHashMap<String, Object>();
		tradeWayMap.put("", "全部");
		tradeWayMap.put("doctorConsultPay", "支付");
		tradeWayMap.put("doctorConsultGift", "赠送");
		model.addAttribute("vo", vo);
		model.addAttribute("page", page);
		model.addAttribute("tradeWayMap", tradeWayMap);
		return "modules/consult/consultOrderList";
	}

	/**
	 * 咨询订单列表
	 * sunxiao
	 * @param
	 * @param model
	 */
	@RequestMapping(value = "consultUserInfoList")
	public String consultUserInfoList(ConsultSessionPropertyVo vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		String temp = ((String)request.getParameter("pageNo"));
		Page<ConsultSessionPropertyVo> pagess = null;
		if(temp==null){
			pagess = new Page<ConsultSessionPropertyVo>();
		}else{
			Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
			Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
			pagess = new Page<ConsultSessionPropertyVo>(pageNo,pageSize);
		}
		Page<ConsultSessionPropertyVo> page = consultDoctorInfoService.findConsultUserInfoListByInfo(pagess, vo);
		model.addAttribute("vo", new ConsultSessionPropertyVo());
		model.addAttribute("page", page);
		return "modules/consult/consultUserInfoList";
	}

	/**
	 * 赠送咨询机会弹框
	 * sunxiao
	 * @param
	 * @param model
	 */
	@RequestMapping(value = "consultTimeGiftForm")
	public String consultTimeGiftForm(ConsultSessionPropertyVo vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		try {
			vo.setNickname(URLDecoder.decode(request.getParameter("nickname"), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		model.addAttribute("vo", vo);
		return "modules/consult/giftConsultTimeForm";
	}

	/**
	 * 赠送咨询机会
	 * @param
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "consultTimeGift")
	public String consultTimeGift(ConsultSessionPropertyVo vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		JSONObject result = new JSONObject();
		consultDoctorInfoService.consultTimeGift(vo);
		result.put("suc", "suc");
		return result.toString();
	}

	/**
	 * 退款页面
	 * sunxiao
	 * @param
	 * @param model
	 */
	@RequestMapping(value = "refundFeeForm")
	public String refundFeeForm(ConsultDoctorTimeGiftVo vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		try {
			vo.setNickname(URLDecoder.decode(vo.getNickname(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		model.addAttribute("vo", vo);
		return "modules/consult/consultRefundForm";
	}

	/**
	 * 退款
	 * @param
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "refundFee")
	public String refundFee(ConsultDoctorTimeGiftVo vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		JSONObject result = new JSONObject();
		String reason = request.getParameter("refundReason");
		reason = StringUtils.isNull(reason)?"yunweituikuan":reason;
		accountService.updateAccount(vo.getAmount() * 100, vo.getOrderId(), new HashMap<String, Object>(), false, vo.getOpenid(), reason);
		result.put("suc", "suc");
		return result.toString();
	}
}
