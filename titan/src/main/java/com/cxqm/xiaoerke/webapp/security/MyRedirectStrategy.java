package com.cxqm.xiaoerke.webapp.security;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cxqm.xiaoerke.common.utils.ConstantUtil;
import org.springframework.security.web.DefaultRedirectStrategy;

import com.cxqm.xiaoerke.common.utils.WebUtil;

public class MyRedirectStrategy extends DefaultRedirectStrategy {
	
	public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
		url = url.replaceAll("BBBBBB", "#");
		url = url.replaceAll("AAAAAA", "&");

		System.out.println(url);
		System.out.println(WebUtil.getWebPath(request));

		if(url.indexOf("http://")==-1){
			url = WebUtil.getWebPath(request) + url;
		}

		this.setContextRelative(false);

		super.sendRedirect(request, response, url);
    }
	
}
