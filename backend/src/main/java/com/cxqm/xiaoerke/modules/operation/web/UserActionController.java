package com.cxqm.xiaoerke.modules.operation.web;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.bankend.service.impl.HospitalServiceImpl;
import com.cxqm.xiaoerke.modules.interaction.entity.UserFeedbackVo;
import com.cxqm.xiaoerke.modules.interaction.service.FeedbackService;
import com.cxqm.xiaoerke.modules.sys.entity.HospitalVo;
import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户行为统计 Controller
 *
 * @author deliang
 * @version 2015-11-05
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/UserAction")
public class UserActionController extends BaseController {

    @Autowired
    HospitalServiceImpl HospitalServiceImpl;

    @Autowired
    FeedbackService feedbackService;

    /**
     * 获取系统所有医院
     *
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"hospitalManage", ""})
    public String hospitalManage(HospitalVo hospitalVo,Model model) {
        Page<HospitalVo> page = HospitalServiceImpl.findAllHospital(new Page<HospitalVo>(), hospitalVo);
        model.addAttribute("page", page);
        return "modules/sys/hospitalManage";
    }

    /**
     * 用户反馈列表
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"userFeedbackList", ""})
    public String userFeedbackList(UserFeedbackVo vo,Model model) {
        Page<UserFeedbackVo> page = feedbackService.findUserFeedbackList(new Page<UserFeedbackVo>(), vo);
        model.addAttribute("page", page);
        model.addAttribute("vo", vo);
        return "operation/userFeedbackList";
    }

    @RequestMapping(value = "changeSolve")
    public
    @ResponseBody
    String changeSolve(UserFeedbackVo vo) {
        JSONObject result = new JSONObject();
        feedbackService.changeSolve(vo);
        result.put("result","suc");
        return result.toString();
    }
}
