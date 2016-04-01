package com.cxqm.xiaoerke.modules.sys.web;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.bankend.service.HospitalService;
import com.cxqm.xiaoerke.modules.bankend.service.impl.DoctorServiceImpl;
import com.cxqm.xiaoerke.modules.operation.service.OperationHandleService;
import com.cxqm.xiaoerke.modules.order.entity.ConsulPhonetDoctorRelationVo;
import com.cxqm.xiaoerke.modules.order.service.PhoneConsultDoctorRelationService;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorHospitalRelationVo;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorLocationVo;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;
import com.cxqm.xiaoerke.modules.sys.entity.HospitalVo;
import com.cxqm.xiaoerke.modules.sys.service.DoctorInfoService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.ChangzhuoMessageUtil;
import com.google.common.collect.Lists;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户Controller
 * @author ThinkGem
 * @version 2013-8-29
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/doctor")
public class DoctorManageController extends BaseController {

	@Autowired
	OperationHandleService OperationHandleService;

	@Autowired
	private DoctorServiceImpl DoctorServiceImpl;

	@Autowired
	private OperationHandleService operationHandleService;

	@Autowired
	HospitalService hospitalService;

	@Autowired
	private DoctorInfoService doctorInfoService;

	@Autowired
	private SystemService systemService;
	
	@Autowired
	private PhoneConsultDoctorRelationService phoneConsultDoctorRelationService;

	/**
	 * 获取所有医生
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = {"doctorManage", ""})
	public String doctorManage(DoctorVo doctorVo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DoctorVo> page = DoctorServiceImpl.findDoctor(new Page<DoctorVo>(), doctorVo);
		List<DoctorVo> list = page.getList();
		for (DoctorVo vo : list){
			vo.setRelationType("");
			vo.setHospital(DoctorServiceImpl.findAllHospitalByDoctorId(vo));
			ConsulPhonetDoctorRelationVo param = new ConsulPhonetDoctorRelationVo();
			param.setDoctorId(vo.getId());
			List<ConsulPhonetDoctorRelationVo> consulPhonetDoctorRelationList = phoneConsultDoctorRelationService.getPhoneConsultDoctorRelationByInfo(param);
			if(consulPhonetDoctorRelationList.size()==0){
				vo.setPhoneConsultFlag("no");
			}else{
				vo.setPhoneConsultFlag("yes");
			}
		}
		model.addAttribute("page", page);
		return "modules/sys/doctorManage";
	}
	/**
	 * 跳转到医生添加或修改界面
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "doctorEdit")
	public String doctorEdit(DoctorVo doctorVo, Model model) {
		if(doctorVo.getId() == null || "".equals(doctorVo.getId())){//添加医生操作
			return "modules/sys/doctorAdd";
		}else{//获取数据跳转的医生修改页面

			//查询医生详细信息，界面显示用（单条，并且里面的医院不一定是归属医院left join，vo肯定不为空）
			DoctorVo Vo=DoctorServiceImpl.findDoctorDetailInfo(doctorVo).get(0);
			//查询医生的归属医院
			doctorVo.setRelationType("1");
			List<DoctorVo> tempVo=DoctorServiceImpl.findDoctorDetailInfo(doctorVo);
//			if(tempVo == null){
//				doctorVo.setRelationType("2");
//				tempVo=DoctorServiceImpl.findDoctorDetailInfo(doctorVo);
//			}
			if(tempVo.size()==0){
				Vo.setHospital("");
			}else{
				Vo.setHospital(tempVo.get(0).getHospital());
			}
			Vo.setRelationType("0");
			//获取医生出诊医院
			List<String> arrayList = DoctorServiceImpl.findAllHospitalListByDoctorId(Vo);
			Vo.setHospitalList(arrayList);
			String date=DateUtils.formatDate(Vo.getCareerTime(), "yyyy-MM-dd");
			Vo.setCareerTimeForDisplay(date);

			model.addAttribute("doctorVo", Vo);
			return "modules/sys/doctorEdit";
		}
	}
	/**
	 * 删除所选医生
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "doctorDelete")
	public String doctorDelete(DoctorVo doctorVo, RedirectAttributes redirectAttributes) {
		//删除doctor信息,包括，删除doctor_hospital_relation表信息，删除sys_doctor_location表信息，删除sys_register_service表信息
		DoctorServiceImpl.deleteDoctorByDoctorId(doctorVo.getId());

		addMessage(redirectAttributes, "删除医生成功");
		return "redirect:" + adminPath + "/sys/doctor/doctorManage?repage";
	}


	/**
	 * 医生信息修改后保存操作
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "Save")
	public String Save(DoctorVo doctorVo,RedirectAttributes redirectAttributes, Model model) {
		DoctorServiceImpl.saveEditDoctor(doctorVo);
		addMessage(redirectAttributes, "恭喜您，" + doctorVo.getDoctorName() + "医生信息修改成功！");
		return "redirect:" + adminPath + "/sys/doctor/doctorManage?repage";
	}


	/**
	 * 查询当前医生与当前医院的关联信息
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "doctorHospitalRelation")
	public String doctorHospitalRelation(String doctorId, HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException {
		String hospitalName = new String(request.getParameter("hospitalName").getBytes("ISO-8859-1"),"UTF-8");
		DoctorHospitalRelationVo doctorHospitalRelationVo = DoctorServiceImpl.findDoctorHospitalRelation(hospitalName, doctorId);
		model.addAttribute("DoctorHospitalRelationVo", doctorHospitalRelationVo);
		return "modules/sys/doctorHospitalRelation";
	}


	/**
	 * “当前医生与当前医院的关联信息”修改后保存操作
	 * @return  123
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "doctorHospitalRelationSave")
	public String doctorHospitalRelationSave(DoctorHospitalRelationVo doctorHospitalRelationVo, HttpServletRequest request, HttpServletResponse response, Model model)  {
		String[] newLocation = request.getParameterValues("newloc");
		String[] idInput = request.getParameterValues("idInput");
		String[] stop = request.getParameterValues("stop");
		String[] train = request.getParameterValues("train");
		String[] bus = request.getParameterValues("bus");
		String[] inroute = request.getParameterValues("inroute");
		String[] messageroute = request.getParameterValues("messageroute");
		String[] price = request.getParameterValues("price");
		String[] kindlyReminder = request.getParameterValues("kindlyReminder");
		Map<String, Object> map = new HashMap<String, Object>();//new了三个map都是啥东西啊。。。郁闷，这样的代码真不想看
		Map<String, Object> oldmap = new HashMap<String, Object>();
		Map<String, Object> tempmap = new HashMap<String, Object>();

		//判断医生的归属医院，如果已经有归属医院则返回，提示不能再添加归属医院（医生的归属医院只能有一个）

		if("1".equals(doctorHospitalRelationVo.getRelationType())){
			HashMap<String,Object> searchMap = new HashMap<String, Object>();
			searchMap.put("doctorId",doctorHospitalRelationVo.getSysDoctorId());
			searchMap.put("relationType",doctorHospitalRelationVo.getRelationType());
			List<DoctorHospitalRelationVo> listVo = doctorInfoService.getDoctorHospitalRelationVo(searchMap);
			if(listVo.size()>1){
				DoctorVo doctorVo= new DoctorVo();
				doctorVo.setId(doctorHospitalRelationVo.getSysDoctorId());
				model.addAttribute("message","对不起，修改失败！医生只能有一个归属医院！现在有"+listVo.size()+"  ");
				return doctorEdit(doctorVo,model);
			}
		}

		if(newLocation!=null){
			for(int i=0;i<newLocation.length;i++){
				String s = "1、停车"+stop[i]+"。"+"2、地铁"+train[i]+"。"+"3、公交"+bus[i]+"；"+inroute[i]+"；"+messageroute[i];
				map.put("".equals(idInput[i])?i+"":idInput[i], newLocation[i]+"S|S"+s+"S|S"+price[i]+"S|S"+kindlyReminder[i]+"S|S");
			}
		}
		if(doctorHospitalRelationVo.getLocation().length()!=0){
			String[] oldLocation = doctorHospitalRelationVo.getLocation().split("S\\|X");
			String[] oldRoute = doctorHospitalRelationVo.getRoute().split("S\\|X");
			for(int i=0;i<oldLocation.length;i++){
				oldmap.put(oldLocation[i].split("S\\|S")[0], oldLocation[i].split("S\\|S")[1]+"S|X"+oldRoute[i]);
			}
		}
		for(String s : map.keySet()){
			if(oldmap.keySet().contains(s)){
				tempmap.put(s, map.get(s));
			}
		}
		for(String s : tempmap.keySet()){
			map.remove(s);
			oldmap.remove(s);
		}
		DoctorServiceImpl.saveDoctorHospitalRelation(doctorHospitalRelationVo,map,oldmap,tempmap);
		DoctorVo doctorVo= new DoctorVo();
		doctorVo.setId(doctorHospitalRelationVo.getSysDoctorId());
		model.addAttribute("message","恭喜您，医生与医院关联修改成功！");
		return doctorEdit(doctorVo,model);
	}

	/**
	 * 删除医生与医院的关联关系
	 * Parameter 关联表主键
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "doctorHospitalRelationDelete")
	public String doctorHospitalRelationDelete(String doctorHospitalRelationId, RedirectAttributes redirectAttributes) {
		//根据doctorHospitalRelationId查询关联信息
		DoctorHospitalRelationVo doctorHospitalRelationVo = DoctorServiceImpl.findDoctorHospitalRelationById(doctorHospitalRelationId);

		//删除医生与医院的关联关系
		DoctorServiceImpl.deleteDoctorHospitalRelation(doctorHospitalRelationId,doctorHospitalRelationVo);
		addMessage(redirectAttributes, "删除关联关系成功");
		return "redirect:" + adminPath + "/sys/doctor/doctorManage?repage";
	}

	/**
	 * 新增医生
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "doctorAdd")
	public String doctorAdd(DoctorVo doctorVo, Model model) {
		String phone = doctorVo.getPhone();
		if(StringUtils.isNull(phone)){//如果医生的手机号为空，按照合作机构医生处理
			phone = "doc" + ChangzhuoMessageUtil.createRandom(false, 8);
		}
		String doctorId = operationHandleService.findDoctorIdByPhone(phone);
		if(StringUtils.isNotNull(doctorId)){
			model.addAttribute("message", "对不起，" + doctorVo.getDoctorName() +"医生已经添加过了！");
			return "modules/sys/doctorAdd";
		}
		HashMap<String, Object> doctorInfo = new HashMap<String, Object>();
		doctorInfo.put("doctorName", doctorVo.getDoctorName());
		String[] strings = doctorVo.getHospital().split(",");
		doctorInfo.put("sys_hospital_id",strings[0]);
		//根据hospitalId查询hospitalType
		HospitalVo hospitalVo = hospitalService.getHospital(strings[0]);
		if("2".equals(hospitalVo.getHospitalType())){
			doctorInfo.put("relation_type", "2");
		}else{
			doctorInfo.put("relation_type", "1");
		}
		doctorInfo.put("hospitalName", strings[1]);
		doctorInfo.put("position1", doctorVo.getPosition1());
		doctorInfo.put("position2", doctorVo.getPosition2());
		doctorInfo.put("career_time", DateUtils.formatDate(doctorVo.getCareerTime(), "yyyy-MM-dd"));
		doctorInfo.put("experince", doctorVo.getExperience());
		doctorInfo.put("card_experince", doctorVo.getCardExperience());
		doctorInfo.put("personal_details", doctorVo.getPersonDetails());
		//医生与医院关系表数据
		doctorInfo.put("location", doctorVo.getPlaceDetail());

		doctorInfo.put("department_level1", doctorVo.getDepartmentLevel1());
		doctorInfo.put("department_level2", doctorVo.getDepartmentLevel2());
		doctorInfo.put("contact_person", doctorVo.getContactPerson());
		doctorInfo.put("contact_person_phone", doctorVo.getContactPersonPhone());
		doctorInfo.put("phone", phone);
		doctorInfo.put("subsidy",doctorVo.getSubsidy());

		doctorInfo.put("insertDoctorLoactinFlag","1");

		//获取微信所需token的参数值
		Map<String,Object> prameter = systemService.getWechatParameter();
		doctorInfo.put("token", prameter.get("token"));
		int doctorFlag=operationHandleService.CreateDoctor(doctorInfo);
		//doctorFlag为2代表此用户为患者并且已经注册过了
		if(doctorFlag == 2){
			model.addAttribute("message", "手机号已经注册过了，请确认");
		}else{
			model.addAttribute("message", "恭喜您，" + doctorVo.getDoctorName() +"医生添加成功！");
		}
		DoctorVo vo = new DoctorVo();
		vo.setDoctorName(doctorVo.getDoctorName());
		vo.setHospital(doctorVo.getHospital());
		vo.setPosition1(doctorVo.getPosition1());
		vo.setPosition2(doctorVo.getPosition2());
		vo.setExperience(doctorVo.getExperience());
		vo.setCardExperience(doctorVo.getCardExperience());
		vo.setPersonDetails(doctorVo.getPersonDetails());
		vo.setPlaceDetail(doctorVo.getPlaceDetail());
		vo.setDepartmentLevel1(doctorVo.getDepartmentLevel1());
		vo.setDepartmentLevel2(doctorVo.getDepartmentLevel2());
		vo.setContactPerson(doctorVo.getContactPerson());
		vo.setContactPersonPhone(doctorVo.getContactPersonPhone());
		vo.setPhone(phone);
		model.addAttribute("doctorVo", vo);
		return "modules/sys/doctorAdd";
	}

	/**
	 * 新增医生与医院关系
	 * @return 456
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "doctorHospitalRelationDataImp")
	public String doctorHospitalRelationDataImp(DoctorHospitalRelationVo doctorHospitalRelationVo, Model model,String flag,String phone, HttpServletRequest request) throws Exception{

		//判断医生的归属医院，如果已经有归属医院则返回，提示不能再添加归属医院（医生的归属医院只能有一个）

		if("1".equals(doctorHospitalRelationVo.getRelationType())){
			HashMap<String,Object> searchMap = new HashMap<String, Object>();
			//根据医生的手机号查询医生的主键
			String doctorId = operationHandleService.findDoctorIdByPhone(doctorHospitalRelationVo.getPhone());
			searchMap.put("doctorId",doctorId);
			searchMap.put("relationType",doctorHospitalRelationVo.getRelationType());
			List<DoctorHospitalRelationVo> listVo = doctorInfoService.getDoctorHospitalRelationVo(searchMap);
			if(listVo.size()>0){
				DoctorVo doctorVo= new DoctorVo();
				doctorVo.setId(doctorId);
				model.addAttribute("message","对不起，修改失败！医生只能有一个归属医院！");
				return doctorEdit(doctorVo,model);
			}
		}



		if("1".equals(flag) || "".equals(phone) || phone==null){
			String rawDoctorName = request.getParameter("doctorName");
			String doctorName = rawDoctorName == null ? null : new String(rawDoctorName.getBytes("ISO-8859-1"),"UTF-8");
			doctorHospitalRelationVo.setDoctorName(doctorName);
			model.addAttribute("doctorHospitalRelationVo",doctorHospitalRelationVo);
			return "modules/sys/doctorHospitalRelationDataImp";
		}else{
			HashMap<String,Object> hashMap = new HashMap<String, Object>();
			//根据医生的手机号查询医生的主键
			String doctorId = operationHandleService.findDoctorIdByPhone(doctorHospitalRelationVo.getPhone());
			if(!StringUtils.isNotNull(doctorId)){
				model.addAttribute("message", "对不起，医生不存在！");
				return "modules/sys/doctorHospitalRelationDataImp";
			}
			String[] strings=doctorHospitalRelationVo.getHospitalName().split(",");
			if(strings == null || strings.length <2){
				model.addAttribute("message", "hospitalName 错误！");
				return "modules/sys/doctorHospitalRelationDataImp";
			}

			String hospitalId = strings[0];
			doctorHospitalRelationVo.setHospitalName(strings[1]);

			DoctorHospitalRelationVo doctorHospitalRel = DoctorServiceImpl.findDoctorHospitalRelationByIds(hospitalId, doctorId);
			if(doctorHospitalRel != null) {
				model.addAttribute("message", "所选医生与医院关系已存在");
				return "modules/sys/doctorHospitalRelationDataImp";
			}
			hashMap.put("hospitalId",hospitalId);
			hashMap.put("sys_doctor_id",doctorId);
			hashMap.put("place_detail",doctorHospitalRelationVo.getPlaceDetail());
			hashMap.put("relation_type",doctorHospitalRelationVo.getRelationType());
			hashMap.put("department_level1",doctorHospitalRelationVo.getDepartmentLevel1());
			hashMap.put("department_level2",doctorHospitalRelationVo.getDepartmentLevel2());
			hashMap.put("contact_person",doctorHospitalRelationVo.getContactPerson());
			hashMap.put("contact_person_phone",doctorHospitalRelationVo.getContactPersonPhone());
			hashMap.put("phone",doctorHospitalRelationVo.getPhone());
			hashMap.put("id", IdGen.uuid());
			hashMap.put("hospitalName",doctorHospitalRelationVo.getHospitalName());

			String[] newLocation = request.getParameterValues("newloc");
			String[] stop = request.getParameterValues("stop");
			String[] train = request.getParameterValues("train");
			String[] bus = request.getParameterValues("bus");
			String[] inroute = request.getParameterValues("inroute");
			String[] messageroute = request.getParameterValues("messageroute");
			String[] price = request.getParameterValues("price");
			String[] kindlyReminder = request.getParameterValues("kindlyReminder");
			List<DoctorLocationVo> list = new ArrayList<DoctorLocationVo>();
			if(newLocation!=null){
				for(int i=0;i<newLocation.length;i++){
					String s = "1、停车"+stop[i]+"。"+"2、地铁"+train[i]+"。"+"3、公交"+bus[i]+"；"+inroute[i]+"；"+messageroute[i];
					DoctorLocationVo vo = new DoctorLocationVo();
					vo.setId(IdGen.uuid());
					vo.setSysDoctorId(doctorId);
					vo.setDoctorName(doctorHospitalRelationVo.getDoctorName());
					vo.setSysHospitalId(hospitalId);
					vo.setHospitalName(doctorHospitalRelationVo.getHospitalName());
					vo.setLocation(newLocation[i]);
					vo.setRoute(s);
					vo.setPrice(Float.valueOf(price[i]));
					if ((kindlyReminder != null) && (kindlyReminder[i] != null)) {
						vo.setKindlyReminder(kindlyReminder[i]);
					}
					list.add(vo);
				}
			}

			DoctorServiceImpl.saveDoctorHospitalRelation(hashMap,list);
			model.addAttribute("message", "恭喜您，添加成功！");
			DoctorVo doctorVo= new DoctorVo();
			doctorVo.setId(doctorId);
			return doctorEdit(doctorVo,model);
		}
	}

	/**
	 * 医生头像上传功能
	 */
//	@ResponseBody
//	@RequestMapping(value = "handleFileUpload", method = RequestMethod.POST)
//	public String handleFileUpload(@RequestParam("files") MultipartFile file,String id) {
//		if (!file.isEmpty()) {
//			try {
//				InputStream inputStream = file.getInputStream();
//				long length = file.getSize();
//				//上传图片至阿里云
//				DoctorServiceImpl.uploadDoctorPic(id,length,inputStream);
//
//				inputStream.close();
//				return "恭喜你！上传头像成功！";
//
//			} catch (Exception e) {
//				e.printStackTrace();
//				return "对不起，上传头像失败" + e.getMessage();
//			}
//		} else {
//			return "You failed to upload " + " because the file was empty.";
//		}
//	}


	/**
	 * 新增医生
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "handleFileUpload")
	public String handleFileUpload(@RequestParam("files") MultipartFile file,String id, Model model) {
		if (!file.isEmpty()) {
			try {
				InputStream inputStream = file.getInputStream();
				long length = file.getSize();
				//上传图片至阿里云
				DoctorServiceImpl.uploadDoctorPic(id, length, inputStream);
				inputStream.close();
				model.addAttribute("message", "恭喜你！上传头像成功！");
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("message", "对不起，上传头像失败");
			}
		} else {
			model.addAttribute("message", "You failed to upload " + " because the file was empty.");
		}
		DoctorVo doctorVo = new DoctorVo();
		doctorVo.setId(id);
//		Page<doctorVo> page = DoctorServiceImpl.findDoctor(new Page<doctorVo>(), doctorVo);
//		List<doctorVo> list = page.getList();
//		for (doctorVo vo : list){
//			vo.setHospital(DoctorServiceImpl.findAllHospitalByDoctorId(vo));
//		}
//		model.addAttribute("page", page);
//		model.addAttribute("doctorVo", doctorVo);
		return  doctorEdit(doctorVo,model);
//		return "modules/sys/doctorManage";

	}

	/**
	 * @param type 类型
	 * @return
	 * @author zdl
	 * 获取医院JSON数据。
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treedoctorData")
	public List<Map<String, Object>> treedoctorData(@RequestParam(required = false) String type) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Page<HashMap<String, Object>> page = doctorInfoService.findPageAllDoctor(new Page<HashMap<String, Object>>(1,1000));
		List<HashMap<String, Object>> doctorList = page.getList();
		for (int i = 0; i < doctorList.size(); i++) {
			Map<String, Object> map = doctorList.get(i);
			map.put("phone", map.get("phone"));
//			map.put("id", map.get("id"));
			map.put("name", map.get("doctorName"));
			if (type != null && "3".equals(type)) {
				map.put("isParent", true);
			}
			mapList.add(map);
		}
		return mapList;
	}

}
