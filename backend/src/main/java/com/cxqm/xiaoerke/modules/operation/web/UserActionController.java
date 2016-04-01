package com.cxqm.xiaoerke.modules.operation.web;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.bankend.service.impl.HospitalServiceImpl;
import com.cxqm.xiaoerke.modules.sys.entity.HospitalVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
