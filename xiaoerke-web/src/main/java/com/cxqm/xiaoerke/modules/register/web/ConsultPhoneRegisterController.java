package com.cxqm.xiaoerke.modules.register.web;

import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.modules.order.entity.SysConsultPhoneServiceVo;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhoneOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by wangbaowei on 16/3/16.
 */
@Controller
@RequestMapping(value = "consultPhone/phoneRegister/")
public class ConsultPhoneRegisterController {

  @Autowired
  private ConsultPhoneOrderService consultPhoneOrderService;

    /**
     * 根据主见查询号源详情
     * @return Map
     * */
    @RequestMapping(value = "getRegisterInfo",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    SysConsultPhoneServiceVo getConsultPhoneList(@RequestParam(value="consultPhoneServiceId", required=true) Integer consultPhoneServiceId){
      DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);
      return consultPhoneOrderService.getConsultServiceInfo(consultPhoneServiceId);
    }

}
