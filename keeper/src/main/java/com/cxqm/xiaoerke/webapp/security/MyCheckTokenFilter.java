package com.cxqm.xiaoerke.webapp.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;

public class MyCheckTokenFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;

		BearerTokenExtractor tokenExtractor = new BearerTokenExtractor();

		Authentication auth = tokenExtractor.extract(req);
		if (auth == null) {// 未找到token，返回错误信息
			//String resp = "{resultCode:\"0903\",resultMsg:\"token不存在\"}";
			//response.getWriter().print(resp);
		} else {
			//chain.doFilter(request, response);
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
