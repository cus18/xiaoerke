package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.HttpRequestUtil;
import com.cxqm.xiaoerke.common.utils.OSSObjectTool;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorInfoVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultDoctorInfoService;
import com.cxqm.xiaoerke.modules.entity.DissatisfiedVo;
import com.cxqm.xiaoerke.modules.entity.ReceiveTheMindVo;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.member.entity.MemberservicerelItemservicerelRelationVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.service.UserInfoService;
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
	private BabyUmbrellaInfoService babyUmbrellaInfoService;

	@Autowired
	private SystemService systemService;

	/**
	 * 咨询医生列表
	 * sunxiao
	 * @param
	 * @param model
	 */
	@RequestMapping(value = "consultDoctorList")
	public String consultDoctorList(User user,HttpServletRequest request,HttpServletResponse response, Model model) {
		/*Query queryDate = new Query();
		List<UmbrellaMongoDBVo> openidlist = babyUmbrellaInfoService.getUmbrellaMongoDBVoList(queryDate);
		int count = 0;
		StringBuffer sb = new StringBuffer("");
		for(UmbrellaMongoDBVo vo : openidlist){
			Map map = new HashMap();
			map.put("openid",vo.getOpenid());
			List<Map<String,Object>> list = babyUmbrellaInfoService.getBabyUmbrellaInfo(map);
			if(list.size()!=0){
				if("success".equals(list.get(0).get("pay_result"))){
					count++;
					sb.append("'"+list.get(0).get("openid")+"',");
				}
			}
		}
		System.out.print(count);
		System.out.print(sb.toString());*/

		sendWechatMessage();

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

	private void sendWechatMessage(){
		try{
			FileReader reader = new FileReader("d://openid.txt");
			BufferedReader br = new BufferedReader(reader);
			String openid = null;
			Map tokenMap = systemService.getWechatParameter();
			String token = (String)tokenMap.get("token");
			String title = "您刚领取的20万保障金还未激活";
			String templateId = "lJIuV_O_zRMav4Fcv32e9cD7YG7cb0WVOPXNjhg_UpU";
			String keyword2 = "保护伞——宝大夫儿童重疾互助计划";
			String remark = "马上点击，完善信息即可激活保障金! ";
			String url = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=31";
			while((openid = br.readLine()) != null) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("openid", openid);
				List<Map<String,Object>> list = babyUmbrellaInfoService.getBabyUmbrellaInfo(param);

				String keyword1 = list.get(0).get("id") + "";
				WechatMessageUtil.templateModel(title, keyword1, keyword2, "", "", remark, token, url, openid, templateId);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
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
		model.addAttribute("redPacketPerson", map.get("redPacketPerson"));
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

}
