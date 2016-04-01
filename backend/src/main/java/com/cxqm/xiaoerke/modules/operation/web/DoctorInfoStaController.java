package com.cxqm.xiaoerke.modules.operation.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.operation.service.DoctorOperationService;
import com.cxqm.xiaoerke.modules.wechat.entity.DoctorAttentionVo;

/**
 * 医生信息统计 Controller
 *
 * @author deliang
 * @version 2015-11-05
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/DoctorInfoSta")
public class DoctorInfoStaController extends BaseController {

    @Autowired
    DoctorOperationService doctorOperationService;

    /**
     * 获取医生统计信息
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"doctorInfo", ""})
    public String doctorInfo(DoctorAttentionVo doctorAttentionVo, HttpServletRequest request, HttpServletResponse response, Model model) {
        List<DoctorAttentionVo> doctorAttentionVoList = new ArrayList<DoctorAttentionVo>();
        if(doctorAttentionVo.getStartDate() != null){
            doctorAttentionVoList = doctorOperationService.findDoctorInfo(doctorAttentionVo);
        }
        model.addAttribute("doctorAttentionVoList",doctorAttentionVoList);
        model.addAttribute("doctorAttentionVo",doctorAttentionVo);
        return "operation/doctorInfoSta";
    }


}
