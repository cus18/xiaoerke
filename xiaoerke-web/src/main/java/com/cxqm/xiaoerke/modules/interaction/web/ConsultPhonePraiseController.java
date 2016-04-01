package com.cxqm.xiaoerke.modules.interaction.web;

import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangbaowei on 16/3/16.
 */

@Controller
@RequestMapping(value = "interaction")
public class ConsultPhonePraiseController {

    @Autowired
    private PatientRegisterPraiseService patientRegisterPraiseService;

    /**
     * 医生评价
     * @return Map
     * */
    @RequestMapping(value = "/user/evaluateDoctor",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    Map<String,Object> evaluateDoctor(@RequestBody HashMap<String, Object> params){
        HashMap<String, Object> response = new HashMap<String, Object>();
        response = patientRegisterPraiseService.getConsultEvaluate(params);
        return response;
    }
}
