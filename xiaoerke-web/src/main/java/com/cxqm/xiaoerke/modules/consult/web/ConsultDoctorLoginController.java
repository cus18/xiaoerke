
package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.service.ConsultDoctorLoginService;
import com.cxqm.xiaoerke.modules.sys.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * ConsultDoctor
 *
 * @author deliang
 * @version 2015-03-14
 */
@Controller
@RequestMapping(value = "consult/doctor/login")
public class ConsultDoctorLoginController extends BaseController {

    @Autowired
    private UtilService utilService;

    @Autowired
    private ConsultDoctorLoginService consultDoctorLoginService;


    /**
     * 获取医生的登陆状态
     *
     * @return
     */
    @RequestMapping(value = "/getDoctorLoginStatus", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map getDoctorLoginStatus(HttpSession session, HttpServletRequest request, HttpServletResponse response) {

        return consultDoctorLoginService.getDoctorLoginStatus(session, request, response);
    }


    /**
     * 医生登陆接口
     * response:
     * {
     * "status":"success"
     * }
     * //success 成功  failure 失败
     *
     * @param session
     * @param request
     * @return
     */
    @RequestMapping(value = "/doctorBinding", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map doctorBinding(@RequestBody Map<String, Object> params, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return consultDoctorLoginService.doctorBinding(params, session, request, response);
    }

    /**
     * 医生退出
     * response:
     * {
     * "status":"success"
     * }
     * //success 成功  failure 失败
     *
     * @param session
     * @param request
     * @return
     */
    @RequestMapping(value = "/signOut", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map signOut(@RequestBody Map<String, Object> params, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return consultDoctorLoginService.doctorSignOut(params, session, request, response);
    }



}
