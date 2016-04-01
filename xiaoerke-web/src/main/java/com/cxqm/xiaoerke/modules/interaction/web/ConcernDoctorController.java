/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.interaction.web;

import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.interaction.service.ConcernService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.*;

/**
 * 测试Controller
 *
 * @author ThinkGem
 * @version 2013-10-17
 */
@Controller
@RequestMapping(value = "${xiaoerkePath}")
public class ConcernDoctorController extends BaseController {

    @Autowired
    private ConcernService concernService;

    /**
     * 查询我的关注医生信息 000_zdl
     * response:
     * {"pageNo":"2","pageSize":"10","pageTotal":"20",
     * "doctorDataVo":[
     * {"doctorId":"787xeieo234","doctorName":"甘晓庄","hospitalName":"北京儿童医院"，"departmentFullName":"儿内-重症医学科",
     * "expertise":"急慢性咳嗽","keyValueNum":"29","career_time":"32","fans_number":"40","available_time":"0"},
     * {"doctorId":"7xdwerd234","doctorName":"董庆华","hospitalName":"首都儿科研究所"，"departmentFullName":"儿内-呼吸内科",
     * "expertise":"急慢性咳嗽","keyValueNum":"29","career_time":"20"，"fans_number":"40","available_time":"0"}
     * ]
     * }
     * //available_time为0表示今日可约，为1代表明日可约，为2，代表2天后可约，依次类推7天以内的即可
     */
    @RequestMapping(value = "/interaction/user/myselfConcern", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> listSearchConcern(@RequestBody Map<String, Object> params) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        concernService.getMyConcernedDoctorList(params,response);
        return response;
    }

    /**
     * 关注医生
     * params:{"doctorId":"1"}
     */
    @RequestMapping(value = "/interaction/user/doctorConcern", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> doctorConcern(@RequestBody Map<String, Object> params) {
        concernService.userConcernDoctor(params);
        return null;
    }

    /**
     * 判断该用户是否已经关注医生  @author zdl
     * params:{"doctorId":"1"}
     * response：{"isConcerned"：true}
     */
    @RequestMapping(value = "/interaction/user/isConcerned", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> doctorConcernYesOrFalse(@RequestBody Map<String, Object> params) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        concernService.judgeIfUserConcernDoctor(params,response);
        return response;
    }
}
