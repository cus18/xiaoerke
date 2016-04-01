package com.cxqm.xiaoerke.modules.sys.web;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.sys.dao.DoctorCaseDao;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorCaseVo;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;

/**
 * 用户Controller
 *
 * @author deliang
 * @version 2013-8-29
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/doctorCase")
public class DoctorCaseManageController extends BaseController {

    @Autowired
    private SystemService systemService;

    @Autowired
    private DoctorCaseDao doctorCaseDao;


    /**
     * 获取医生的案例列表
     *
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"doctorCase", ""})
    public String doctorCase(DoctorCaseVo doctorCaseVo, String source, String id, String doctorName, String doctor_case_name, HttpServletRequest request, HttpServletResponse response, Model model) {
        HashMap<String, Object> executeMap = new HashMap<String, Object>();
        executeMap.put("doctorId", id);
        //查询医生的案例info
        if (source.equals("doctorCaseManager") && StringUtils.isNotNull(doctor_case_name)) {
            executeMap.put("doctor_case_name", doctor_case_name);
            executeMap.put("doctorId", doctorCaseVo.getSys_doctor_id());
        }
        executeMap.put("display_status", null);
        List<DoctorCaseVo> voList = doctorCaseDao.findDoctorCase(executeMap);
        if (voList != null && voList.size() > 0) {
            model.addAttribute("doctorCaseVo", voList.get(0));
        } else {
            DoctorCaseVo vo = new DoctorCaseVo();
            vo.setSys_doctor_id(id);
            model.addAttribute("doctorCaseVo", vo);
        }
        model.addAttribute("voList", voList);
        return "modules/sys/doctorCaseManage";
    }


    /**
     * 获取医生的案例列表
     *
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"doctorCaseDelete", ""})
    public String doctorCaseDelete(String sys_doctor_id, String doctorCaseId, Model model) {
        HashMap<String, Object> executeMap = new HashMap<String, Object>();
        executeMap.put("doctorCaseId", doctorCaseId);
        //删除医生案例
        doctorCaseDao.deleteDoctorCaseById(executeMap);
        //跳转
        executeMap.put("doctorId", sys_doctor_id);
        List<DoctorCaseVo> voList = doctorCaseDao.findDoctorCase(executeMap);
        if(voList!=null && voList.size()>0){
            model.addAttribute("doctorCaseVo", voList.get(0));
        }else {
            DoctorCaseVo doctorCaseVo = new DoctorCaseVo();
            doctorCaseVo.setSys_doctor_id(sys_doctor_id);
            model.addAttribute("doctorCaseVo", doctorCaseVo);
        }
        model.addAttribute("voList", voList);
        return "modules/sys/doctorCaseManage";
    }

    /**
     * 批量修改案例列表信息
     *
     * @return
     */
    @RequestMapping(value = "updateDoctorCase")
    public String updateDoctorCase(String[] ids, String[] displays, String[] doctorCaseNames, Integer[] doctorCaseNumbers, RedirectAttributes redirectAttributes) {
        int len = ids.length;
        for (int i = 0; i < len; i++) {
            DoctorCaseVo doctorCaseVos = new DoctorCaseVo();
            doctorCaseVos.setId(ids[i]);
            doctorCaseVos.setDoctor_case_name(doctorCaseNames[i]);
            doctorCaseVos.setDisplay_status(displays[i]);
            doctorCaseVos.setDoctor_case_number(doctorCaseNumbers[i]);
            doctorCaseDao.updateDoctorCase(doctorCaseVos);
        }
        addMessage(redirectAttributes, "保存成功!");
        return "redirect:" + adminPath + "/sys/doctor/doctorManage";
    }

    /**
     * 添加医生案例
     *
     * @return
     */
    @RequestMapping(value = "insertDoctorCase")
    public String insertDoctorCase(DoctorCaseVo doctorCaseVo, String source, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        //跳转到添加医生案例页面
        if (source.equals("caseManager")) {
            String doctorName = new String(request.getParameter("doctorName").getBytes("ISO-8859-1"), "UTF-8");
            doctorCaseVo.setDoctorName(doctorName);
            model.addAttribute("doctorCaseVo", doctorCaseVo);
            return "modules/sys/doctorCaseAdd";
        } else {
            //保存医生案例
            HashMap<String, Object> insertMap = new HashMap<String, Object>();
            insertMap.put("patientRegisterPraiseId", IdGen.uuid());
            insertMap.put("sys_doctor_id", doctorCaseVo.getSys_doctor_id());
            insertMap.put("id", "无");
            insertMap.put("symptom", doctorCaseVo.getDoctor_case_name());
            insertMap.put("sys_patient_id", "无");
            insertMap.put("sys_register_service_id", "无");
            insertMap.put("doctorCaseNumber", doctorCaseVo.getDoctor_case_number());
            insertMap.put("display_status", doctorCaseVo.getDisplay_status());
            doctorCaseDao.saveDoctorCaseInfo(insertMap);
            insertMap.put("doctorId", doctorCaseVo.getSys_doctor_id());
            model.addAttribute("message", "保存成功");
            insertMap.put("display_status", null);
            List<DoctorCaseVo> voList = doctorCaseDao.findDoctorCase(insertMap);
            if (voList != null && voList.size() > 0) {

                model.addAttribute("doctorCaseVo", voList.get(0));
            }
            model.addAttribute("voList", voList);
            return "modules/sys/doctorCaseManage";
        }
    }

}
