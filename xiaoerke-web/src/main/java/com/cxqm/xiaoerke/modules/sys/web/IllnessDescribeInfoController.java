package com.cxqm.xiaoerke.modules.sys.web;

import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.modules.healthRecords.service.BabyIllnessInfoService;
import com.cxqm.xiaoerke.modules.order.entity.ConsulPhonetDoctorRelationVo;
import com.cxqm.xiaoerke.modules.order.service.PhoneConsultDoctorRelationService;
import com.cxqm.xiaoerke.modules.order.service.SysConsultPhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 病情描述Controller
 * @author chenxiaoqiong
 * @version 2016/4/14
 */
@Controller
@RequestMapping(value="illness/")
public class IllnessDescribeInfoController {

    @Autowired
    PhoneConsultDoctorRelationService phoneConsultDoctorRelationService;

    @Autowired
    SysConsultPhoneService sysConsultPhoneService;

    @Autowired
    BabyIllnessInfoService babyIllnessInfoService;

    @RequestMapping(value="illnessDescribe/detail")
    @ResponseBody
    public Map<String,Object> getIllnessDetail(@RequestBody Map<String,Object> params){
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        Map<String,Object> response = new HashMap<String, Object>();
        Integer sys_phoneConsult_id = Integer.parseInt((String)params.get("sys_phoneConsult_id"));
        String doctorId = (String)params.get("doctorId");

        //订单时间
        Map<String,Object> serviceMap = sysConsultPhoneService.getSysPhoneConsultInfo(sys_phoneConsult_id);
        if(serviceMap != null){
            response.put("consultDate",serviceMap.get("consultDate"));
        }
        //服务类型
        ConsulPhonetDoctorRelationVo relationVo = phoneConsultDoctorRelationService.getPhoneConsultRigister(doctorId);
        if(relationVo != null) {
            float price = relationVo.getPrice();
            response.put("price",price);
            response.put("serverLength", relationVo.getServerLength());
        }

        //illnessDetail
        Map<String,Object> illnessInfoMap = babyIllnessInfoService.getIllnessInfoBySysPhoneConsultId(sys_phoneConsult_id);
        if(illnessInfoMap != null){
            response.put("illnessInfo",illnessInfoMap);
        }

        return response;
    }
}
