package com.cxqm.xiaoerke.modules.insurance.web;

import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.insurance.entity.InsuranceHospitalVo;
import com.cxqm.xiaoerke.modules.insurance.entity.InsuranceRegisterService;
import com.cxqm.xiaoerke.modules.insurance.service.InsuranceHospitalService;
import com.cxqm.xiaoerke.modules.insurance.service.InsuranceRegisterServiceService;
import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;
import com.cxqm.xiaoerke.modules.sys.entity.SwitchConfigure;
import com.cxqm.xiaoerke.modules.sys.service.BabyBaseInfoService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.ChangzhuoMessageUtil;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "insurance")
public class InsuranceController {

    @Autowired
    InsuranceRegisterServiceService insuranceRegisterServiceService;

    @Autowired
    InsuranceHospitalService insuranceHospitalService;

    @Autowired
    BabyBaseInfoService babyBaseInfoService;

    @Autowired
    private SystemService systemService;

    @RequestMapping(value = "/saveInsuranceRegisterService", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> saveInsuranceRegisterService(@RequestBody Map<String, Object> params) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        String sex = (String) params.get("sex");
        String birthday = (String) params.get("birthday");
        if (StringUtils.isNotNull(sex) && StringUtils.isNotNull(birthday)) {
            BabyBaseInfoVo babyBaseInfoVo = new BabyBaseInfoVo();
            babyBaseInfoVo.setId(Integer.valueOf(params.get("babyId").toString()));
            babyBaseInfoVo.setSex(sex);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                babyBaseInfoVo.setBirthday(format.parse(birthday));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            babyBaseInfoService.updateByPrimaryKeySelective(babyBaseInfoVo);
        }

        Date startDate =new Date();
        Date endDate = new Date();
        Calendar c = Calendar.getInstance();
        int days = 0;
        if("3".equals(params.get("insuranceType").toString())){//肺炎宝待生效是15天
            days = 15;
        }else{
            days = 1;
        }
        c.add(Calendar.DAY_OF_YEAR, days);
        startDate = c.getTime();
        c.add(Calendar.YEAR,1);
        endDate = c.getTime();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        InsuranceRegisterService insuranceRegisterService = new InsuranceRegisterService();
        insuranceRegisterService.setId(ChangzhuoMessageUtil.createRandom(true, 10));
        insuranceRegisterService.setBabyId(params.get("babyId").toString());
        insuranceRegisterService.setIdCard(params.get("idCard").toString());
        insuranceRegisterService.setInsuranceType(params.get("insuranceType").toString());
        insuranceRegisterService.setStartTime(startDate);
        insuranceRegisterService.setEndTime(endDate);
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
    Map<String, Object> getInsuranceRegisterServiceById(@RequestBody Map<String, Object> params) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        String id = params.get("id").toString();
        InsuranceRegisterService Insurance = insuranceRegisterServiceService.getInsuranceRegisterServiceById(id);
        String idcard = Insurance.getIdCard();
        Insurance.setIdCard(idcard.replace(idcard.subSequence(5, 13), "********"));
        resultMap.put("InsuranceRegisterService", Insurance);
        return resultMap;
    }

    @RequestMapping(value = "/getInsuranceRegisterServiceListByUserid", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getInsuranceRegisterServiceListByUserid(@RequestParam(required = false) String insuranceType) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("userid", UserUtils.getUser().getId());
        dataMap.put("insuranceType", insuranceType);
        List<Map<String, Object>> insuranceViedList = insuranceRegisterServiceService.getValidInsuranceRegisterServiceListByUserid(dataMap);
        List<Map<String, Object>> insuranceInvalidList = insuranceRegisterServiceService.getInvalidInsuranceRegisterServiceListByUserid(dataMap);
        resultMap.put("insuranceInvalidList", insuranceInvalidList);
        resultMap.put("insuranceViedList", insuranceViedList);
        return resultMap;
    }

    @RequestMapping(value = "/getInsuranceRegisterServiceIfValid", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getInsuranceRegisterServiceIfValid(@RequestBody Map<String, Object> params) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        String babyId = params.get("babyId").toString();
        String insuranceType = (String) params.get("insuranceType");

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("babyId", babyId);
        dataMap.put("insuranceType", insuranceType);
        resultMap.put("valid", insuranceRegisterServiceService.getInsuranceRegisterServiceIfValid(dataMap).size());
        return resultMap;
    }

    @RequestMapping(value = "/getInsuranceRegisterServiceVisitLeadPageLogByOpenid", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getInsuranceRegisterServiceVisitLeadPageLogByOpenid(HttpServletRequest request, HttpSession session) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        String openid = WechatUtil.getOpenId(session, request);
        resultMap.put("log", insuranceRegisterServiceService.getInsuranceRegisterServiceVisitLeadPageLogByOpenid(openid).size());
        return resultMap;
    }

    @RequestMapping(value = "/getInsuranceRegisterServiceByOpenid", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getInsuranceRegisterServiceByOpenid(HttpServletRequest request, HttpSession session) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        String openid = WechatUtil.getOpenId(session, request);
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
    Map<String, Object> getInsuranceHospitalListByInfo(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpSession session) {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        InsuranceHospitalVo vo = new InsuranceHospitalVo();
        vo.setDistrict((String) params.get("district"));
        List<InsuranceHospitalVo> list = insuranceHospitalService.getInsuranceHospitalListByInfo(vo);
        List<Map<String, Object>> insuranceHospitalList = new ArrayList<Map<String, Object>>();
        for (InsuranceHospitalVo ivo : list) {
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

    @RequestMapping(value = "/getInsuranceInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getInsuranceInfo(HttpServletRequest request, HttpSession session) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map maps = new HashMap();
        maps.put("type","insurance");
        SwitchConfigure switchConfigure = systemService.getUmbrellaSwitch(maps);
        JSONObject jsonObject = JSONObject.fromObject(switchConfigure.getFlag());
        resultMap.put("insuranceInfo", jsonObject);
        return resultMap;
    }

}
