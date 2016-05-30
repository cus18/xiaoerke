package com.cxqm.xiaoerke.modules.healthRecords.web;

import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.healthRecords.entity.BabyIllnessInfoVo;
import com.cxqm.xiaoerke.modules.healthRecords.service.HealthRecordsService;
import com.cxqm.xiaoerke.modules.order.service.PatientRegisterService;
import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;
import com.cxqm.xiaoerke.modules.sys.entity.PerAppDetInfoVo;
import com.cxqm.xiaoerke.modules.sys.service.CustomerService;
import com.cxqm.xiaoerke.modules.sys.service.DoctorCaseService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangbaowei on 16/1/18.
 */

@Controller
@RequestMapping(value = "healthRecord")
public class HealthRecordsController {

  @Autowired
  private PatientRegisterService patientRegisterService;

  @Autowired
  private CustomerService customerService;

  @Autowired
  private HealthRecordsService healthRecordsService;

  @Autowired
  private DoctorCaseService doctorCaseService;

  @Autowired
  private ConsultSessionService consultConversationService;

  /**
   * 获取上次预约时的宝宝信息
   *
   * */
   @RequestMapping(value = "/getLastOrderBabyInfo", method = {RequestMethod.POST, RequestMethod.GET})
   public
   @ResponseBody
   Map<String,Object> getLastOrderBabyInfo(){
	   Map<String, Object> resultMap = new HashMap<String, Object>();
	   resultMap.put("lastOrderBabyInfo",patientRegisterService.getlastPatientRegisterInfo(UserUtils.getUser().getPhone()));
     return resultMap;
   }


  /**
   *获取用户下宝宝信息
   * @return Map 宝宝信息集合
   * */
  @RequestMapping(value = "/getBabyinfoList", method = {RequestMethod.POST, RequestMethod.GET})
  public
  @ResponseBody
  Map<String, Object> getUserBabyInfolist(@RequestBody Map<String, Object> params){
    Map<String, Object> resultMap = new HashMap<String, Object>();
    List<BabyBaseInfoVo> babyInfoList = new ArrayList<BabyBaseInfoVo>();
    String openId = (String)params.get("openId");
    if(StringUtils.isNotNull(openId)){
      babyInfoList = healthRecordsService.getUserBabyInfolistByOpenId(openId);
    }else{
      String userId = UserUtils.getUser().getId();
      babyInfoList = healthRecordsService.getUserBabyInfolist(userId);
    }

    resultMap.put("babyInfoList",babyInfoList);
    resultMap.put("userPhone", UserUtils.getUser().getPhone());
    return resultMap;
  }

  /**
   *获取宝宝订单详情
   * @param babyId 宝宝id主见
   * @return Map map 预约详情的map集合
   * * @throws UnsupportedEncodingException
   * */
  @RequestMapping(value = "/getAppointmentInfo", method = {RequestMethod.POST, RequestMethod.GET})
  public
  @ResponseBody
  Map<String, Object> getAppointmentInfo(@RequestParam String babyId) throws UnsupportedEncodingException{

    Map<String, Object> map=new HashMap<String, Object>();
    //根据上述两个参数查询预约详情记录
    List<Map<String, Object>> result = patientRegisterService.getHealthRecordsAppointmentInfo( babyId, UserUtils.getUser().getPhone());
    map.put("orderInfoList", result);
    return map;
  }


  /**
   *添加宝宝信息
   * @return Map 宝宝信息集合
   * @throws UnsupportedEncodingException
   * */
  @RequestMapping(value = "/saveBabyInfo", method = {RequestMethod.POST, RequestMethod.GET})
  public
  @ResponseBody
  Map<String, Object> saveBabyInfo(@RequestParam String sex, @RequestParam String name, @RequestParam String birthDay, HttpServletRequest request, HttpSession session) throws UnsupportedEncodingException{
    Map<String, Object> resultMap = new HashMap<String, Object>();

    BabyBaseInfoVo vo = new BabyBaseInfoVo();
    vo.setSex(sex);
    vo.setBirthday(this.toDate(birthDay));
    vo.setName(URLDecoder.decode(name, "utf-8"));
    String id=UserUtils.getUser().getId();
    String openid=UserUtils.getUser().getOpenid();
    if(openid.equals("")||openid==null){
      openid= WechatUtil.getOpenId(session, request);
    }
    vo.setUserid(id);
    vo.setOpenid(openid);
    int result = healthRecordsService.insertBabyInfo(vo);
    resultMap.put("resultCode",result);
    resultMap.put("autoId", vo.getId());
    return resultMap;
  }

  /**
   *修改宝宝信息
   * @return Map 宝宝信息集合
   * @throws UnsupportedEncodingException
   * */
  @RequestMapping(value = "/updateBabyInfo", method = {RequestMethod.POST, RequestMethod.GET})
  public
  @ResponseBody
  Map<String, Object> updateBabyInfo(@RequestParam String id,@RequestParam String sex,@RequestParam String name,@RequestParam String birthDay,HttpServletRequest request, HttpSession session) throws UnsupportedEncodingException{
    Map<String, Object> resultMap = new HashMap<String, Object>();

    BabyBaseInfoVo vo = new BabyBaseInfoVo();
    vo.setId(Integer.parseInt(id));
    vo.setSex(sex);
    vo.setBirthday(this.toDate(birthDay));
    vo.setName(URLDecoder.decode(name, "utf-8"));
    vo.setUserid(UserUtils.getUser().getId());
    String openid=UserUtils.getUser().getOpenid();
    if(openid.equals("")||openid==null){
      openid= WechatUtil.getOpenId(session, request);
    }
    vo.setOpenid(openid);
    int result = healthRecordsService.updateBabyInfo(vo);
    resultMap.put("resultCode",result);
    return resultMap;
  }

  /**
   *删除宝宝信息
   * @return Map 宝宝信息集合
   * */
  @RequestMapping(value = "/deleteBabyInfo", method = {RequestMethod.POST, RequestMethod.GET})
  public
  @ResponseBody
  Map<String, Object> deleteBabyInfo(@RequestParam String id){
    Map<String, Object> resultMap = new HashMap<String, Object>();
    //对删除宝宝行为不做物理删除
    //张博  2016年3月24日
    BabyBaseInfoVo vo=new BabyBaseInfoVo();
    vo.setId(Integer.parseInt(id));
    vo.setState("6");
    int result =healthRecordsService.updateBabyInfo(vo);
    resultMap.put("resultCode",result);
    return resultMap;
  }



  /**
   *获取宝宝咨询详情记录
   * @param babyId 宝宝id主见
   * @return Map map 咨询详情的map集合
   * */
  @RequestMapping(value = "/getConsultationInfo", method = {RequestMethod.POST, RequestMethod.GET})
  public
  @ResponseBody
  Map<String, Object> getConsultationInfo(@RequestParam Integer babyId){
    return customerService.getCustomerLogByBabyId(babyId);
  }

  /**
   *添加病情描述页面
   *
   * @param params  病情描述
   * @return Map map 修改结果
   * * @throws UnsupportedEncodingException
   * */
  @RequestMapping(value = "/modifyBabyIndfo", method = {RequestMethod.POST, RequestMethod.GET})
  public
  @ResponseBody
  Map<String, Object> modifyBabyIndfo(@RequestBody Map<String, Object> params) throws UnsupportedEncodingException{

    BabyIllnessInfoVo bean = new BabyIllnessInfoVo();
    if(StringUtils.isNotNull((String)params.get("illnessDescribe"))){
      bean.setDesc((String) params.get("illnessDescribe"));
    }

    bean.setCreateTime(new Date());
    bean.setBabyinfoId(String.valueOf((Integer) params.get("babyId")));
    String caseImg = (String)params.get("caseImg");
    if(StringUtils.isNotNull((String)params.get("caseImg"))){
      String[] imgStr = caseImg.split(",");
      for(String str:imgStr){
        if(StringUtils.isNotNull(str)){
          healthRecordsService.uploadPic(str,str);
        }
      }

      bean.setCaseimg(caseImg);
    }
    String resultImg = (String)params.get("resultImg");
    if(StringUtils.isNotNull(resultImg)){
      String[] imgStr = resultImg.split(",");
      for(String str:imgStr){
        if(StringUtils.isNotNull(str)){
          healthRecordsService.uploadPic(str,str);
        }
      }

      bean.setResultimg(resultImg);
    }

    String positionImg = (String)params.get("positionImg");
    if(StringUtils.isNotNull(positionImg)){
      String[] imgStr = caseImg.split(",");
      for(String str:imgStr){
        if(StringUtils.isNotNull(str)){
          healthRecordsService.uploadPic(str,str);
        }
      }

      bean.setPositionimg(positionImg);
    }

    String otherImg = (String)params.get("otherImg");
    if(StringUtils.isNotNull(otherImg)){
      String[] imgStr = caseImg.split(",");
      for(String str:imgStr){
        if(StringUtils.isNotNull(str)){
          healthRecordsService.uploadPic(str,str);
        }
      }

      bean.setOtherimg(otherImg);
    }
    bean.setConversationId((String)params.get(("conversation_id")));
    int state = healthRecordsService.insertBabyIllnessInfo(bean);
    Map<String,Object> resultMap = new HashMap<String, Object>();
    resultMap.put("state",state);
    return resultMap;
  }




  /**
   *填写病情诊断
   * @param PatientRegisterId
   *        订单主见
   * @param caseName
   *        案例名称
   * @throws UnsupportedEncodingException
   * */
  @RequestMapping(value = "/addDoctorCase", method = {RequestMethod.POST, RequestMethod.GET})
  public
  @ResponseBody
  Map<String, Object>  addDoctorCase(@RequestParam String PatientRegisterId,@RequestParam String caseName) throws UnsupportedEncodingException{
    PerAppDetInfoVo vo = new PerAppDetInfoVo();
    vo.setId(PatientRegisterId);
    Map result = patientRegisterService.findPersonAppointDetailInfo(vo);
    int caseNum = doctorCaseService.findDoctorCaseNumberByName((String) result.get("doctorId"),URLDecoder.decode(caseName, "utf-8"));
    HashMap<String, Object> insertMap = new HashMap<String, Object>();
    insertMap.put("patientRegisterPraiseId", IdGen.uuid());
    insertMap.put("sys_doctor_id", (String)result.get("doctorId"));
    insertMap.put("id", PatientRegisterId);
    insertMap.put("symptom", URLDecoder.decode(caseName, "utf-8"));
    insertMap.put("doctorCaseNumber", caseNum==0?"1":caseNum+1);
    insertMap.put("display_status", "0");
    insertMap.put("update_date", new Date());
    doctorCaseService.saveDoctorCaseInfo(insertMap);
    Map<String,Object> resultMap = new HashMap<String, Object>();
    resultMap.put("state", "true");
    return resultMap;
  }

  /**
   * 宝宝病例
   * @return 病例,病例图片
   * */
  @RequestMapping(value = "/getConsultInfo",method = {RequestMethod.GET,RequestMethod.POST})
  public
  @ResponseBody
  Map<String,Object> getConsultInfo(@RequestBody Map<String,Object> map){
      String openId=map.get("openId").toString();
	  Map<String,Object> result = new HashMap<String, Object>();
    List<Map<String,Object>> resultList = consultConversationService.getConsultInfo(openId);
	  result.put("getConsultInfo", 0 < resultList.size() ? resultList.get(0) : null);
    return result;
  }


  /**
   * 历史咨询
   * */
  @RequestMapping(value = "/consultHistory",method = {RequestMethod.POST,RequestMethod.GET})
    public
    @ResponseBody
    Map<String,Object> consultHistory(@RequestParam String openId){
      Map<String,Object> result = new HashMap<String, Object>();
      result.put("consultHistoryList",consultConversationService.getConsultInfo(openId));
      return result;
    }

  /**
   * 点击历史咨询更新update时间
   * */
  @RequestMapping(value = "/updateConversationInfo", method = {RequestMethod.POST, RequestMethod.GET})
  public
  @ResponseBody
  Map<String,Object> updateConversationInfo(@RequestParam Integer id){
    Map<String,Object> result = new HashMap<String, Object>();
    ConsultSession consultSession = new ConsultSession();
    consultSession.setId(id);
    consultSession.setUpdateTime(new Date());
    result.put("state",consultConversationService.updateSessionInfo(consultSession));
    return result;
  }


  public Date toDate(String date){
	  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	  try {
		return sdf.parse(date);
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
  }

}
