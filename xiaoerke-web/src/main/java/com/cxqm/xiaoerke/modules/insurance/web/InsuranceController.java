package com.cxqm.xiaoerke.modules.insurance.web;

import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.insurance.entity.InsuranceHospitalVo;
import com.cxqm.xiaoerke.modules.insurance.entity.InsuranceRegisterService;
import com.cxqm.xiaoerke.modules.insurance.service.InsuranceHospitalService;
import com.cxqm.xiaoerke.modules.insurance.service.InsuranceRegisterServiceService;
import com.cxqm.xiaoerke.modules.sys.utils.ChangzhuoMessageUtil;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "insurance")
public class InsuranceController {

	@Autowired
	InsuranceRegisterServiceService insuranceRegisterServiceService;
	
	@Autowired
	InsuranceHospitalService insuranceHospitalService;

	   @RequestMapping(value = "/saveInsuranceRegisterService", method = {RequestMethod.POST, RequestMethod.GET})
	   public
	   @ResponseBody
	   Map<String,Object> saveInsuranceRegisterService(@RequestBody Map<String, Object> params){
		   Map<String, Object> resultMap = new HashMap<String, Object>();
		   InsuranceRegisterService insuranceRegisterService=new InsuranceRegisterService();
		   insuranceRegisterService.setId(ChangzhuoMessageUtil.createRandom(true, 10));
		   insuranceRegisterService.setBabyId(params.get("babyId").toString());
		   insuranceRegisterService.setIdCard(params.get("idCard").toString());
		   insuranceRegisterService.setInsuranceType(params.get("insuranceType").toString());
		   insuranceRegisterService.setParentName(params.get("parentName").toString());
		   insuranceRegisterService.setParentType(params.get("parentType").toString());
		   insuranceRegisterService.setParentPhone(params.get("parentPhone").toString());
		   insuranceRegisterService.setState("6");
		   insuranceRegisterService.setSource("buy");//订单来源，buy是用户端购买，give是运维赠送，sunxiao
		   resultMap.put("resultCode", insuranceRegisterServiceService.saveInsuranceRegisterService(insuranceRegisterService));
		   resultMap.put("id", insuranceRegisterService.getId());
	     return resultMap;
	   }
	   
	   @RequestMapping(value = "/getInsuranceRegisterServiceById", method = {RequestMethod.POST, RequestMethod.GET})
	   public
	   @ResponseBody
	   Map<String,Object> getInsuranceRegisterServiceById(@RequestBody Map<String, Object> params){
		   Map<String, Object> resultMap = new HashMap<String, Object>();
		   String id=params.get("id").toString();
		   InsuranceRegisterService  Insurance=insuranceRegisterServiceService.getInsuranceRegisterServiceById(id);
		   String idcard = Insurance.getIdCard();
		   Insurance.setIdCard(idcard.replace(idcard.subSequence(5, 13), "********"));
		   resultMap.put("InsuranceRegisterService",Insurance);
	     return resultMap;
	   }
	   
	   @RequestMapping(value = "/getInsuranceRegisterServiceListByUserid", method = {RequestMethod.POST, RequestMethod.GET})
	   public
	   @ResponseBody
	   Map<String,Object> getInsuranceRegisterServiceListByUserid(String insuranceType){
		   Map<String, Object> resultMap = new HashMap<String, Object>();
           Map<String, Object> dataMap = new HashMap<String, Object>();
           dataMap.put("userid",UserUtils.getUser().getId());
           dataMap.put("insuranceType",insuranceType);
		   List<Map<String,Object>> insuranceViedList=insuranceRegisterServiceService.getValidInsuranceRegisterServiceListByUserid(dataMap);
		   List<Map<String,Object>> insuranceInvalidList=insuranceRegisterServiceService.getInvalidInsuranceRegisterServiceListByUserid(dataMap);
		   resultMap.put("insuranceInvalidList", insuranceInvalidList);
		   resultMap.put("insuranceViedList", insuranceViedList);
	     return resultMap;
	   }
	
	   @RequestMapping(value = "/getInsuranceRegisterServiceIfValid", method = {RequestMethod.POST, RequestMethod.GET})
	   public
	   @ResponseBody
	   Map<String,Object> getInsuranceRegisterServiceIfValid(@RequestBody Map<String, Object> params){
		   Map<String, Object> resultMap = new HashMap<String, Object>();
		   String babyId=params.get("babyId").toString();
		   resultMap.put("valid", insuranceRegisterServiceService.getInsuranceRegisterServiceIfValid(babyId).size());
	     return resultMap;
	   }
	   
	   @RequestMapping(value = "/getInsuranceRegisterServiceVisitLeadPageLogByOpenid", method = {RequestMethod.POST, RequestMethod.GET})
	   public
	   @ResponseBody
	   Map<String,Object> getInsuranceRegisterServiceVisitLeadPageLogByOpenid(HttpServletRequest request,HttpSession session){
		   Map<String, Object> resultMap = new HashMap<String, Object>();
		   String openid=WechatUtil.getOpenId(session, request);
		   resultMap.put("log", insuranceRegisterServiceService.getInsuranceRegisterServiceVisitLeadPageLogByOpenid(openid).size());
		   return resultMap;
	   }
	   
	   @RequestMapping(value = "/getInsuranceRegisterServiceByOpenid", method = {RequestMethod.POST, RequestMethod.GET})
	   public
	   @ResponseBody
	   Map<String,Object> getInsuranceRegisterServiceByOpenid(HttpServletRequest request,HttpSession session){
		   Map<String, Object> resultMap = new HashMap<String, Object>();
		   String openid=WechatUtil.getOpenId(session, request);
		   resultMap.put("insurance", insuranceRegisterServiceService.getInsuranceRegisterServiceByOpenid(openid).size());
		   return resultMap;
	   }
	   
	   /**
		 * sunxiao
		 * 根据条件查询保险相关医院
		 */
	   @RequestMapping(value = "/getInsuranceHospitalListByInfo", method = {RequestMethod.POST, RequestMethod.GET})
	   public
	   @ResponseBody
	   Map<String,Object> getInsuranceHospitalListByInfo(@RequestBody Map<String, Object> params,HttpServletRequest request,HttpSession session){
		   Map<String, Object> resultMap = new HashMap<String, Object>();
		   InsuranceHospitalVo vo = new InsuranceHospitalVo();
		   vo.setDistrict((String)params.get("district"));
		   List<InsuranceHospitalVo> list = insuranceHospitalService.getInsuranceHospitalListByInfo(vo);
		   List<Map<String,Object>> insuranceHospitalList = new ArrayList<Map<String,Object>>();
		   for(InsuranceHospitalVo ivo : list){
			   Map<String, Object> map = new HashMap<String, Object>();
			   map.put("hospitalName", ivo.getHospitalName());
			   map.put("district", ivo.getDistrict());
			   map.put("location", ivo.getLocation());
			   map.put("phone", ivo.getPhone());
			   map.put("id", ivo.getId());
			   insuranceHospitalList.add(map);
		   }
		   resultMap.put("insurance", insuranceHospitalList);
		   return resultMap;
	   }
}
