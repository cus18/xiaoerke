package com.cxqm.xiaoerke.modules.sys.web;

import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.FrontUtils;
import com.cxqm.xiaoerke.modules.sys.service.DoctorGroupInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangbaowei on 16/3/16.
 */
@Controller
@RequestMapping(value = "consultPhone/")
public class ConsultPhoneDoctorGroupController {


    @Autowired
    private DoctorGroupInfoService doctorGroupInfoService;

    /**
     * 电话咨询首页-获取专家团列表
     * @return Map
     * */
    @RequestMapping(value = "getAllDoctorGroupList",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    Map<String,Object> getAllDoctorGroupList(@RequestParam(value="pageNo", required=true) String pageNo,
                                             @RequestParam(value="pageSize", required=false) String pageSize,
                                             @RequestParam(value="orderBy", required=false) String orderBy){
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);
        Page<HashMap<String, Object>> page = FrontUtils.generatorPage(pageNo, pageSize);
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("is_consultPhone","1");
        return doctorGroupInfoService.findPageAllDoctorGroup(page, param);
    }
}
