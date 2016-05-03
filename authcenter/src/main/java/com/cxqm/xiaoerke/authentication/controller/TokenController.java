package com.cxqm.xiaoerke.authentication.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cxqm.xiaoerke.common.utils.ConstantUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cxqm.xiaoerke.authentication.common.BaseController;

@Controller
@RequestMapping(value = "/sso/token")
public class TokenController extends BaseController {
	
	@RequestMapping(value = "sendToken")
	public String sendToken(@RequestParam String toUrl, @RequestParam String token, HttpServletRequest request, HttpServletResponse response, Model model){
		Cookie cookie = new Cookie("ssoToken",token);
		cookie.setPath("/");
		cookie.setDomain(ConstantUtil.DOMAIN_VALUE);
		cookie.setMaxAge(60*60*24*90);
		response.addCookie(cookie);
		
		return "redirect:"+toUrl+"?token="+token;
	}

}
