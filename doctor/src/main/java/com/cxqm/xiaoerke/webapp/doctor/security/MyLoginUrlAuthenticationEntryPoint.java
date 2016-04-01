package com.cxqm.xiaoerke.webapp.doctor.security;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class MyLoginUrlAuthenticationEntryPoint extends
		LoginUrlAuthenticationEntryPoint {

	public MyLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
		super(loginFormUrl);
	}
	
	@Override
	protected String buildRedirectUrlToLoginPage(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException) {
		String url = super.buildRedirectUrlToLoginPage(request, response, authException);
		
		String routePath = request.getParameter("routePath");
		if(routePath == null) {
			BufferedReader br = null;
			try {
				br = request.getReader();
				String body = "";
				String inputLine;
				while ((inputLine = br.readLine()) != null) {
					body += inputLine;
			    }
				
				if(body.length() > 1) {
					JSONObject json = JSONObject.fromObject(body);
					if(json.containsKey("routePath")) 
						routePath = json.getString("routePath");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				if(br != null)
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
		
		if(routePath != null) {
			StringBuilder urlSB = new StringBuilder(url);
			urlSB.append("?targeturl=");
			urlSB.append(routePath);
			url = urlSB.toString();
		}

		url = response.encodeRedirectURL(url);
		return url;
	}

}
