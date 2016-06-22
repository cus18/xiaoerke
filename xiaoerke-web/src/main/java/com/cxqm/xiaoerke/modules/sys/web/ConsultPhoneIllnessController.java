package com.cxqm.xiaoerke.modules.sys.web;

import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.modules.sys.entity.IllnessVo;
import com.cxqm.xiaoerke.modules.sys.service.IllnessInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by wangbaowei on 16/3/16.
 */

@Controller
public class ConsultPhoneIllnessController {

    @Autowired
    private IllnessInfoService illnessInfoService;

    /**
     * 电话咨询首页-获取一级疾病列表
     * @return Map
     * */
    @RequestMapping(value = "getFirstIllnessList",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    List<IllnessVo> getFirstIllnessList(){
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);
        return illnessInfoService.getFirstIllnessList();
    }

    /**
     * 电话咨询首页-获取二级疾病列表
     * @return Map
     * */
    @RequestMapping(value = "getSecondIllnessList",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    List<IllnessVo> getSecondIllnessList(@RequestParam String illnessName){
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);
        return illnessInfoService.findSecondIllnessByName(illnessName);
    }
}
