package com.cxqm.xiaoerke.authentication.common;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionHandlerForAPI {

	public static final String DEFAULT_ERROR_VIEW = "error";
	private static final Logger logger = LoggerFactory.getLogger(FormAuthenticationFilter.class);
	
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Map<String, Object> defaultErrorHandler(HttpServletRequest request, Exception ex) throws Exception {
        if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null)
            throw ex;
         
        ModelAndView mav = new ModelAndView();
 		mav.addObject("resultMsg", ex.getMessage() == null ? "" : ex.getMessage());
 		
 		Map<String, String[]> paramMap = request.getParameterMap();
 		logger.error("api error message:<" + ex.getMessage() + "> 请求地址:<"
 				+ request.getRequestURI() + ">  请求参数:<" + JSONObject.fromObject(paramMap) + ">");
 		ex.printStackTrace();
 		if (ex instanceof MissingServletRequestParameterException)
 			mav.addObject("resultCode", "0002");
 		else if (ex instanceof TypeMismatchException)
 			mav.addObject("resultCode", "0001");
 		else {
 			mav.addObject("resultMsg", "An internal Exception happened.");
 			mav.addObject("resultCode", "9999");
 		}
 		Map<String, Object> map = mav.getModel();
 		return map;
    }
	
}
