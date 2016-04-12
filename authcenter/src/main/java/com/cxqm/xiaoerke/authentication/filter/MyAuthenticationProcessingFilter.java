package com.cxqm.xiaoerke.authentication.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cxqm.xiaoerke.common.utils.WechatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.cxqm.xiaoerke.authentication.common.CookieUtils;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.service.UtilService;


/**
 * 用户授权管理
 * 
 * @version 1.0
 * 
 */
public class MyAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
	
	public static final String SESSION_USER = "user";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";

	@Autowired
	private UtilService utilService;
	
	public MyAuthenticationProcessingFilter() {
		super("/test");
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,HttpServletResponse response) throws AuthenticationException {
		
		String username = obtainUsername(request);
		String password = obtainPassword(request);
		
		if(username == null || username.length() == 0)
			throw new AuthenticationServiceException("empty_username");
		if (password.length() <= 0)
			throw new AuthenticationServiceException("empty_password");

		String openId = WechatUtil.getOpenId(request.getSession(), request);
		String toUrl = request.getParameter("toUrl");
		String status = null;
		if(toUrl != null && toUrl.indexOf("/doctor/") != -1){
			status = utilService.bindUser4Doctor(username, password, openId);
		} else if(toUrl != null && toUrl.indexOf("/doctor/consultBBBBBB") != -1){
			status = utilService.bindUser4ConsultDoctor(username, password, openId);
		}else
			status = utilService.bindUser(username, password, openId);
		

		if("1".equals(status)) {
			password = "ILoveXiaoErKe";
		} else if("0".equals(status)) {
			throw new AuthenticationServiceException("invalid_verifycode");
		}
		
		// UsernamePasswordAuthenticationToken实现 Authentication
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
		// 允许子类设置详细属性
		setDetails(request, authRequest);
		// 运行UserDetailsService的loadUserByUsername 再次封装Authentication
		return this.getAuthenticationManager().authenticate(authRequest);
	}

	protected String obtainUsername(HttpServletRequest request) {
		Object obj = request.getParameter(USERNAME);
		return null == obj ? "" : obj.toString().trim();
	}

	protected String obtainPassword(HttpServletRequest request) {
		Object obj = request.getParameter(PASSWORD);
		return null == obj ? "" : obj.toString();
	}
	
	 /**
     * Provided so that subclasses may configure what is put into the authentication request's details
     * property.
     *
     * @param request that an authentication request is being created for
     * @param authRequest the authentication request object that should have its details set
     */
    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }
    
}