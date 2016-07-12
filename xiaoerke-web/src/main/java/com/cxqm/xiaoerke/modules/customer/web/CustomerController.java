package com.cxqm.xiaoerke.modules.customer.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxqm.xiaoerke.modules.member.service.MemberService;
import com.cxqm.xiaoerke.modules.search.service.InternalSearchService;
import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;
import com.cxqm.xiaoerke.modules.sys.entity.CustomerBabyInfo;
import com.cxqm.xiaoerke.modules.sys.entity.CustomerLog;
import com.cxqm.xiaoerke.modules.sys.entity.CustomerReturn;
import com.cxqm.xiaoerke.modules.sys.service.CustomerService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;

import freemarker.template.utility.StringUtil;

@Controller
@RequestMapping(value = "customer")
public class CustomerController {

	@Autowired
	private CustomerService cs;
	
	@Autowired
	private InternalSearchService internalSearchService;
	
	@Autowired
	private MemberService memberService;
	
	
	/**
	 * 添加咨询宝宝信息
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/saveBaby", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  saveCustomerBaby(@RequestBody Map<String, Object> params){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			BabyBaseInfoVo babyBaseInfoVo=new BabyBaseInfoVo();
			babyBaseInfoVo.setName(params.get("babyName").toString());
			babyBaseInfoVo.setBirthday(sdf.parse(params.get("babyBirthday").toString()));
			babyBaseInfoVo.setOpenid(params.get("openid").toString());
			babyBaseInfoVo.setSex(params.get("sex").toString());
			babyBaseInfoVo.setState("1");
			if(params.get("userid")!=null){
				babyBaseInfoVo.setUserid(params.get("userid").toString());
				babyBaseInfoVo.setState("0");
				}
			if(params.get("id")!=null&&!params.get("id").equals("")){
				babyBaseInfoVo.setId(Integer.parseInt(params.get("id").toString()));
				int result=cs.updateBabyInfo(babyBaseInfoVo);
				map.put("type",result);
				return map;
			}
			int result=cs.saveBabyInfo(babyBaseInfoVo);
			map.put("type",result);
			return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("type", 0);
			return map;
		}
	}

	/**
	 * 根据咨询者Openid查询历史宝宝信息
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/searchBabyInfo", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  searchBabyInfo(@RequestBody Map<String, Object> params){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			String openid=params.get("openid").toString();
			List<Map<String, Object>> babyList=cs.getBabyInfoListNew(openid);
			map.put("babyList", babyList);
			map.put("nickName", cs.getNickName(openid));
			return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("babyList", "");
			return map;
		}
	}
	
	/**
	 * 查询历史咨询科室
	 * @return
	 */
	@RequestMapping(value = "/searchIllnessList", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  searchIllnessList(){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			List<Map<String, Object>> babyList=cs.getIllnessList();
			map.put("illnessList", babyList);
			return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("illnessList", "");
			return map;
		}
	}

	/**
	 *  存储历史咨询科室
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/saveIllness", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  searchCustomer(@RequestBody Map<String, Object> params){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			String illness=params.get("illness").toString();
			cs.saveIllness(illness);
			map.put("type", 1);
			return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("type", 0);
			return map;
		}
	}


	/**
	 * 添加咨询历史
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/saveCustomerLog", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  saveCustomerLog(@RequestBody Map<String, Object> params){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			CustomerLog cl=new CustomerLog();
			cl.setCreateDate(sdf.parse(params.get("create_date").toString()));
			cl.setCustomerID(params.get("customerID").toString());
			cl.setIllnessID(params.get("illness").toString());
			cl.setSections(params.get("sections").toString());
			cl.setBabyInfoID(params.get("id").toString());
			cl.setShow(params.get("show").toString());
			cl.setResult(params.get("result").toString());
			cl.setOpenid(params.get("openid").toString());
			cs.saveCustomerLog(cl);
			map.put("type", 1);
			return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("type", 0);
			return map;
		}
	}

	/**
	 * 重定向（缓存）
	 * @return
	 */
	@RequestMapping(value = "/responseCustomer", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  responseCustomer(HttpServletResponse response){
		try {
			response.sendRedirect("/xiaoerke-wxapp/statistic/index#/customer");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/getOrderInfoByOpenid", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  getOrderInfoByOpenid(@RequestBody Map<String, Object> params){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

		Map<String ,Object> map=new HashMap<String, Object>();
		String openid;
		if(params.get("openid")==null||params.get("openid").equals("")){
			openid=UserUtils.getUser().getOpenid();
			if(openid==null||openid.equals("")){
				return map;
			}
		}else{
			openid=params.get("openid").toString();
		}
		map.put("orderList", cs.getOrderInfoByOpenid(openid,null));
		return map;
	}

	@RequestMapping(value = "/getCustomerLogByOpenID", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  getCustomerLogByOpenID(@RequestBody Map<String, Object> params){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

		Map<String ,Object> map=new HashMap<String, Object>();
		String openid;
		/*if(params.get("openid")==null||params.get("openid").equals("")){
			openid=UserUtils.getUser().getOpenid();
			if(openid==null||openid.equals("")){
				return map;
			}
		}else{
			openid=params.get("openid").toString();
		}*/
//		List list=cs.getCustomerLogByBabyOpenID(openid);
		List list = new ArrayList();
		HashMap hashMap = new HashMap();
		hashMap.put("customerId","123456");
		hashMap.put("show","头晕/发烧");
		hashMap.put("zhenduan","感冒");
		hashMap.put("result","吃药");
		HashMap hashMap2 = new HashMap();
		hashMap2.put("customerId","aaaa");
		hashMap2.put("show","头晕/发烧");
		hashMap2.put("zhenduan","dfdf");
		hashMap2.put("result","吃药");
		list.add(hashMap);
		list.add(hashMap2);
		map.put("logList", list);
		return map;
	}
	
	@RequestMapping(value = "/onIllnessKeydown", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  onIllnessKeydown(@RequestBody Map<String, Object> params){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

		Map<String ,Object> map=new HashMap<String, Object>();
		String illness=params.get("illness").toString();
		if(illness==null||illness.equals("")){
			return map;
		}
		map.put("ZYQURI", cs.onIllnessKeydown(illness));
		return map;
	}
	
	/**
	 *  存储Openid关键字历史
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/saveOpenidAndKeywords", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  saveOpenidAndKeywords(@RequestBody Map<String, Object> params){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			String openid=params.get("openid").toString();
			String keywords=params.get("keywords").toString();
			cs.saveOpenidKeywords(openid, keywords);
			map.put("type", 1);
			return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("type", 0);
			return map;
		}
	}

	/**
	 * 根据疾病查询医生列表
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/getDocotrByIllness", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  getDocotrByIllness(@RequestBody Map<String, Object> params){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

		Map<String ,Object> map=new HashMap<String, Object>();
		String illness=params.get("illness").toString();
		try {
			String[] doctorids=internalSearchService.searchDoctors(illness,500,0,"doctorId");
			if(doctorids.length>0){
				map.put("values", "yes");
			}else{
				map.put("values", "null");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping(value = "/getKeywordsByOpenID", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  getKeywordsByOpenID(@RequestBody Map<String, Object> params){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

		Map<String ,Object> map=new HashMap<String, Object>();
		String openid=params.get("openid").toString();
		if(openid==null||openid.equals("")){
			return map;
		}
		String keywords=params.get("keywords").toString();
		if(keywords==null||keywords.equals("")){
			return map;
		}
		map.put("logList", cs.getKeywordsByOpenID(openid,keywords));
		return map;
	}
	
	@RequestMapping(value = "/getLastOrderDate", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  getLastOrderDate(@RequestBody Map<String, Object> params){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

		Map<String ,Object> map=new HashMap<String, Object>();
		String openid=params.get("openid").toString();
		if(openid==null||openid.equals("")){
			openid=UserUtils.getUser().getOpenid();
			if(openid==null||openid.equals("")){
				return map;
			}
		}
		map.put("memberEndDate", cs.getVIPEndDate(openid));
		map.put("orderDate", cs.getLastOrderDate(openid,""));
		map.put("memberEndDate", cs.getVIPEndDate(openid));
		return map;
	}
	
	@RequestMapping(value = "/getMemberInfo", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  getMemberInfo(@RequestBody Map<String, Object> params){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

		Map<String ,Object> map=new HashMap<String, Object>();
		String openid=params.get("openid").toString();
		map.put("memberInfo", memberService.getMemberServiceStatus(openid));
		return map;
	}
	
	@RequestMapping(value = "/saveReturnService", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  saveReturnService(@RequestBody Map<String, Object> params){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

		Map<String ,Object> map=new HashMap<String, Object>();
		CustomerReturn customerReturn=new CustomerReturn();
		customerReturn.setOpenID(params.get("openid").toString());
		customerReturn.setCustomer(params.get("customerID").toString());
		map.put("type", cs.saveCustomerReturn(customerReturn));
		return map;
	}
	
	@RequestMapping(value = "/SendMessage", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Object>  SendMessage(){
		Map<String ,Object> map=new HashMap<String, Object>();
		cs.SendCustomerReturn();
		map.put("type", "OK");
		return map;
	}
	
}
