package com.cxqm.xiaoerke.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

/**
 * Created by Administrator on 2014/11/1.
 */
public class WebUtil {
	/**
	 * 获得i18n字符串
	 */
	public static String getMessage(String code, Object[] args) {
		LocaleResolver localLocaleResolver = (LocaleResolver) SpringContextHolder.getBean(LocaleResolver.class);
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		Locale localLocale = localLocaleResolver.resolveLocale(request);
		return SpringContextHolder.getApplicationContext().getMessage(code, args, localLocale);
	}

	/**
	 * 获得用户远程地址
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		String remoteAddr = request.getHeader("X-Real-IP");
		if (StringUtils.isNotBlank(remoteAddr)) {
			remoteAddr = request.getHeader("X-Forwarded-For");
		} else if (StringUtils.isNotBlank(remoteAddr)) {
			remoteAddr = request.getHeader("Proxy-Client-IP");
		} else if (StringUtils.isNotBlank(remoteAddr)) {
			remoteAddr = request.getHeader("WL-Proxy-Client-IP");
		}
		return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
	}
	
	/** 获取域名 */
	
	public static String getWebPath(HttpServletRequest request){
		String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		if(request.getServerPort() == 80) {
		    path = request.getScheme() + "://" + request.getServerName()  + request.getContextPath();
		}
		
		return path;
	}
	public static String getDrwenwoRedirectUrl(HttpServletRequest request,String toUrl) throws UnsupportedEncodingException{
	        StringBuffer requestURL = request.getRequestURL();
	        String contextPath =  request.getContextPath()+"/";
	        int indexOf = requestURL.indexOf(contextPath);
	        String url= requestURL.substring(0,indexOf)+contextPath+"public/api/v2/drwenwo/redirect?toUrl=";
	        url+=URLEncoder.encode(toUrl, "UTF-8");
	        return url;
	        
	}
}
