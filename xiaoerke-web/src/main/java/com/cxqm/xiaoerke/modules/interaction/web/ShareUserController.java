/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.interaction.web;

import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.interaction.service.FeedbackService;
import com.cxqm.xiaoerke.modules.interaction.service.ShareService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 测试Controller
 *
 * @author ThinkGem
 * @version 2013-10-17
 */
@Controller
@RequestMapping(value = "interaction")
public class ShareUserController extends BaseController {

    @Autowired
    private ShareService shareService;

    /**
     * 获取个人分享信息详情 @author 0_zdl
     * <p/>
     * params:{"patient_register_service_id":"fewi323odw"}
     * <p/>
     * response:
     * {
     * "hospitalName":"4214cdsifn","doctorName":"甘晓庄","hospitalName":"北京儿童医院"，"position1":"主任医师",
     * "position2":"教授","date":"2015-07-03", "praise":"很好",
     * }
     */
    @RequestMapping(value = "/user/share", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> myselfInfoShareDetail(@RequestBody Map<String, Object> params) {
        // 记录日志
        LogUtils.saveLog(Servlets.getRequest(), "00000050","获取个人分享信息详情:" + params.get("patientRegisterServiceId"));
        Map<String, Object> response = shareService.getMyShareDetail(params);
        return response;
    }
}
