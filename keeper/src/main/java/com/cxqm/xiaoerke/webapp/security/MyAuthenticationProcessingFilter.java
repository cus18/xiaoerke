package com.cxqm.xiaoerke.webapp.security;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.mapper.JsonMapper;
import com.cxqm.xiaoerke.common.utils.HttpUtils;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;


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

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SystemService systemService;

	@Autowired
	private SysPropertyServiceImpl sysPropertyService;
	
	public MyAuthenticationProcessingFilter() {
		super("/test");
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,HttpServletResponse response) throws AuthenticationException {
		SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();

		String token = request.getParameter("token");
		if(token == null){//用户不存在
			throw new AuthenticationServiceException("Token cannot be null。");
		}
		
		String authCheckTokenUrl = 	sysPropertyVoWithBLOBsVo.getAuthenticationBasepath() + "/oauth/check_token?token="+token;
		String tokenInfo = HttpUtils.doGet(authCheckTokenUrl);

		JsonMapper json = JsonMapper.getInstance();
		Map<String,Object> userinfo = json.fromJson(tokenInfo,Map.class);
		String userName = null;
		try{
			userName = userinfo.get("user_name").toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(userName == null)
			throw new AuthenticationServiceException("username does not exist.");
		// 验证用户信息
		User user = systemService.getUserByLoginName(userName);
		
		if(user == null){//用户不存在
			throw new AuthenticationServiceException("User does not exist。");
		}

		// UsernamePasswordAuthenticationToken实现 Authentication
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userName, user.getPassword());
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
    
    public void doMyFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (logger.isDebugEnabled()) {
            logger.debug("Request is to process authentication");
        }

        Authentication authResult;

        try {
            authResult = attemptAuthentication(request, response);
            if (authResult == null) {
                // return immediately as subclass has indicated that it hasn't completed authentication
                return;
            }
        } catch (AuthenticationException failed) {
            // Authentication failed
            unsuccessfulAuthentication(request, response, failed);

            return;
        }

        synchronized(MyAuthenticationProcessingFilter.class) {
        	AuthenticationSuccessHandler originalSuccessHandler = this.getSuccessHandler();
	        try {
		        AuthenticationSuccessHandler appLogAuthenticationSuccessHandler = (AuthenticationSuccessHandler) SpringContextHolder.getBean("appLogAuthenticationSuccessHandler");
		    	this.setAuthenticationSuccessHandler(appLogAuthenticationSuccessHandler);
		    	
		        successfulAuthentication(request, response, chain, authResult);
	        } finally {
	        	this.setAuthenticationSuccessHandler(originalSuccessHandler);
	        }
        }
    }
    
}