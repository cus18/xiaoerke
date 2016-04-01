package com.cxqm.xiaoerke.modules.sys.service;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.FrontUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * Created by wangbaowei on 16/3/16.
 */
public class ConsultPhoneHospitalService {

    @Autowired
    private HospitalInfoService hospitalInfoService;

    /**
     * 电话咨询首页-获取所有医院列表
     * @return Map
     * */
    @RequestMapping(value = "getAllHospitalList",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    Page<HashMap<String, Object>> getAllHospitalList(@RequestParam String pageSize,@RequestParam String pageNo){
        Page<HashMap<String, Object>> page = FrontUtils.generatorPage(pageNo, pageSize);
        Page<HashMap<String, Object>> hospitalPage = hospitalInfoService.getHospitalListByConsulta(page);
        return hospitalPage;
    }
}
