package com.cxqm.xiaoerke.authentication.controller;

import com.cxqm.xiaoerke.authentication.common.BaseController;
import com.cxqm.xiaoerke.authentication.common.CookieUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;


@Controller
public class LoginController extends BaseController{

	@Autowired
	private UtilService utilService;


	@Autowired
	private SysPropertyServiceImpl sysPropertyService;


	/** 认证中心登陆页 */
	@RequestMapping(value = "${ssoPath}/login", method = RequestMethod.GET)
	public String login(@RequestParam String toUrl, HttpServletRequest request,
						HttpServletResponse response, Model model) {
		
		String authcenterUrl = request.getParameter("authcenterurl");
		if(toUrl.indexOf("?") == -1)
			toUrl += ("?authcenterurl=" +authcenterUrl);
		else
			toUrl += ("&authcenterurl=" +authcenterUrl);
		
        model.addAttribute("toUrl", toUrl);
        String appName = getAppNameFromUrl(toUrl);
		return "modules/sso/" + appName;
	}
	
	/** 检查认证中心是否已登录  */
	@RequestMapping(value = "${ssoPath}/checklogin")
	public String checkLogin(String toUrl, HttpServletRequest request){
		//认证中心在cookie中获取token，未取到则跳转登陆页登陆
		String ip = request.getLocalAddr();
		int port = request.getLocalPort();
		String contextPath = request.getContextPath();
		StringBuilder authcenterUrl = new StringBuilder("http://")
		.append(ip.contains(":") ? "localhost" : ip)
		.append(":")
		.append(port)
		.append(contextPath);
		
		if(toUrl.indexOf("?") == -1)
			toUrl += ("?authcenterurl=" +authcenterUrl);
		else
			toUrl += ("&authcenterurl=" +authcenterUrl);
		
		String url = "redirect:/sso/login?toUrl="+toUrl;
		
		String from = request.getParameter("from");
		if(from != null && "doctor-failure".equals(from)) {
			return url;
		}
		
		Cookie[] cookies = request.getCookies();
		if(cookies==null){
			return url;
		}else{
			for(Cookie cookie : cookies){
				if(cookie.getName().equals("ssoToken")){
					String token = cookie.getValue();//认证中心拿到token回传客户端，由客户端发起验证token请求
					if(toUrl.indexOf("?") == -1)
						return "redirect:"+toUrl+"?token="+token;
					else
						return "redirect:"+toUrl+"&token="+token;
				}
			}
		}
		return url;
	}
	
	@RequestMapping(value="${ssoPath}/logout")
	public String logout(String toUrl,HttpServletResponse response,HttpServletRequest request){
		SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
		Cookie[] cookies = request.getCookies();
		String[] pathArray=new String[]{"/titan/","/keeper/","/authcenter/","/angel/","/doctor/","/market/","/wisdom/","/"};
		for(int i = 0,len = cookies.length; i < len; i++) {
			for(int j = 0;j < pathArray.length;  j++){
				Cookie cookie = new Cookie(cookies[i].getName(), null);
				cookie.setDomain(sysPropertyVoWithBLOBsVo.getBaodfDomainValue());
				cookie.setMaxAge(0);
				cookie.setPath(pathArray[j]);
				response.addCookie(cookie);
			}
		}
		return "redirect:"+toUrl;
	}
	
	/**
	 * 获取主题方案
	 */
	@RequestMapping(value = "/theme/{theme}")
	public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request,
								   HttpServletResponse response){
		if (StringUtils.isNotBlank(theme)){
			CookieUtils.setCookie(response, "theme", theme);
		}else{
			theme = CookieUtils.getCookie(request, "theme");
		}
		return "redirect:"+request.getParameter("url");
	}

	/** 授权确认页面
	 * 
	 *  目前流程：授权页面自动跳转，默认为“确认授权”
	 *  
	 *  正常流程：显示授权确认页面，由用户操作是否授权
	 *  */
	@RequestMapping(value="oauthconfirm")
	public String oauthconfirm(HttpServletRequest request, HttpServletResponse response){
		return "modules/sso/ssoConfirm";
	}
	
	public static String getAppNameFromUrl(String url){
		String[] urlSplits = url.split("/");
		if(urlSplits.length > 2)
			return urlSplits[3];
		else
			return null;
	}

	/**
	 * 根据手机号，获取验证码，验证码将根据手机号，下推至用户手机
	 * <p/>
	 * params:{"userPhone":"13601025662"}
	 * <p/>
	 * response:
	 * {
	 * "code":"35678",
	 * "status":"1"
	 * }
	 * //status为1表示获取验证码成功，为0表示获取验证码失败
	 */
	@RequestMapping(value = "/util/user/getCode", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	Map<String, Object> getUserCode(@RequestBody Map<String, Object> params) {
		System.out.print("getCode()...."+new Date());
		SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
		String userPhone = (String) params.get("userPhone");
		return utilService.sendIdentifying(userPhone,sysPropertyVoWithBLOBsVo);
	}
    
}
