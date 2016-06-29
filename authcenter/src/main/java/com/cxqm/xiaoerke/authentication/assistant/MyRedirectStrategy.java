package com.cxqm.xiaoerke.authentication.assistant;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.DefaultRedirectStrategy;

public class MyRedirectStrategy extends DefaultRedirectStrategy {

	public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
		String toUrl = request.getParameter("toUrl");//客户端回调地址
		String redirectUrl = "";
		if(url.equals("/")){//登录成功,回调客户端
			redirectUrl = toUrl;	
		}else{//登录失败，回调认证中心地址
			redirectUrl = request.getContextPath() + url + "?toUrl=" + toUrl;
		}

        redirectUrl = response.encodeRedirectURL(redirectUrl);
        
        if (logger.isDebugEnabled()) {
            logger.debug("Redirecting to '" + redirectUrl + "'");
        }

        response.sendRedirect(redirectUrl);
    }
	
	
}
