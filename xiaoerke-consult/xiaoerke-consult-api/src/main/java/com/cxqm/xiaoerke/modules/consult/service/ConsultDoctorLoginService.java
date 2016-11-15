package com.cxqm.xiaoerke.modules.consult.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by zhaodeliang on 16/6/27.
 */
public interface ConsultDoctorLoginService {


    Map getDoctorLoginStatus(HttpSession session, HttpServletRequest request,HttpServletResponse response);

    Map doctorBinding(Map<String, Object> params, HttpSession session, HttpServletRequest request, HttpServletResponse response);

    Map doctorSignOut(Map<String, Object> params, HttpSession session, HttpServletRequest request, HttpServletResponse response);
}
