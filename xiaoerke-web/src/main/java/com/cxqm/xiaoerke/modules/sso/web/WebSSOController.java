package com.cxqm.xiaoerke.modules.sso.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.mapper.JsonMapper;
import com.cxqm.xiaoerke.common.utils.HttpUtils;
import com.cxqm.xiaoerke.common.utils.WebUtil;

@Controller
@RequestMapping(value = "/sso")
public class WebSSOController {

	private static String codeCallbackUrl = "/sso/dealCode";//客户端处理认证中心CODE地址
	private static String loginCallbackUrl = "/sso/loginBack";//客户端处理认证中心登录成功地址
	private static String tokenCallbackurl = "/sso/checkToken";//客户端处理认证中心token地址

	@Autowired
	private SysPropertyServiceImpl sysPropertyService;
	@RequestMapping(value = "checkLogin")
	public @ResponseBody String checkLogin(String targeturl, HttpServletRequest request) throws Exception{

		StringBuilder sb = new StringBuilder("{\"status\":\"9\", \"redirectURL\": \"");
		if(targeturl == null) {
			sb = new StringBuilder("{\"status\":\"8\", \"redirectURL\": \"");
		}
		SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();

		sb.append(sysPropertyVoWithBLOBsVo.getAuthenticationBasepath())
		.append("/sso/checklogin?");
		
		String from = request.getParameter("from");
		if(from != null && "doctor-failure".equals(from)) {
			sb.append("from=doctor-failure&");
		}
		
		sb.append("toUrl=")
		.append(WebUtil.getWebPath(request))
		.append("/sso/loginBack");
		
		if(targeturl != null) {
			targeturl = URLEncoder.encode(targeturl, "utf-8");
			sb.append("?targeturl=")
			.append(targeturl);
		}
		sb.append("\"}");
		
		return sb.toString();
	}
	
	
	/** 处理认证中心登陆成功回调地址 */
	@RequestMapping(value = "loginBack")
	public String loginBack(String token, String targeturl, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String authcenterUrl = request.getParameter("authcenterurl");
		request.getSession().setAttribute("authcenterurl", authcenterUrl);
		
		//认证中心登录成功，回调客户端，客户端直接发起授权申请
		if(token !=null && !token.equals("")){//认证已经登陆,认证中心cookie中存在token
			//验证token
			String authCheckTokenUrl = authcenterUrl + "/oauth/check_token?token="+token;
			String tokenInfo = HttpUtils.doGet(authCheckTokenUrl);

			JsonMapper json = JsonMapper.getInstance();
			Map<String,Object> userinfo = json.fromJson(tokenInfo,Map.class);
			String userName = null;
			try{
				userName = userinfo.get("user_name").toString();//账号
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if(userName != null && !userName.equals("")){//token验证成功，证明token有效，走客户端登录流程
				if(targeturl != null) {
					targeturl = URLEncoder.encode(targeturl, "utf-8");
					return "redirect:/auth/auth_center/form?token="+token + "&targeturl=" + targeturl;
				} else
					return "redirect:/auth/auth_center/form?token="+token;
			}else{//token验证失败，走认证中心登录流程
				SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
				String authLoginUrl = sysPropertyVoWithBLOBsVo.getAuthenticationBasepath() + "/sso/login?toUrl="+WebUtil.getWebPath(request) + loginCallbackUrl;//认证中心登录地址
				return "redirect:"+authLoginUrl;
			}
		}else{//认证中心重新登陆成功，客户端发起授权请求
			SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();

			String url = "redirect:" + sysPropertyVoWithBLOBsVo.getAuthenticationBasepath() + "/oauth/authorize?response_type=code&scope=test&client_id=web&client_secret=xiaoerke123456&redirect_uri="+WebUtil.getWebPath(request)+codeCallbackUrl;
			if(targeturl != null) {
				targeturl = URLEncoder.encode(targeturl, "utf-8");
				Cookie cookie = new Cookie("targeturl",targeturl);
				cookie.setPath(request.getContextPath());
				cookie.setMaxAge(30*24*60*1000);
				response.addCookie(cookie);
			}
			return url;
		}
	}
	
	/** 处理认证中心回传CODE回调地址 */
	@RequestMapping(value = "dealCode")
	public String dealCode(@RequestParam(required=true) String code, HttpServletRequest request, HttpServletResponse response,Model model){
		//认证中心授权成功，回调客户端，客户端去认证中心获取token
		String authcenterurl = (String) request.getSession().getAttribute("authcenterurl");
		String getTokenUrl = authcenterurl + "/oauth/token?grant_type=authorization_code&scope=test&client_id=web&client_secret=xiaoerke123456&redirect_uri="+WebUtil.getWebPath(request)+codeCallbackUrl+"&code="+code;
		String resp = HttpUtils.doGet(getTokenUrl);
		JsonMapper json = JsonMapper.getInstance();
		Map<String,Object> info = json.fromJson(resp,Map.class);
		//解析json,获取token值
		String token = "";
		try{
			token = info.get("access_token").toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//将认证中心获取的token上送认证中心，认证中心保存至cookie（该步骤待定！！！）
		SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();

		String url = "redirect:" + sysPropertyVoWithBLOBsVo.getAuthenticationBasepath() + "/sso/token/sendToken?token=" + token  + "&toUrl=" + WebUtil.getWebPath(request) + tokenCallbackurl;
		return url;
	}
	
	/** 客户端验证token回调地址 
	 * @throws UnsupportedEncodingException */
	@RequestMapping(value = "checkToken")
	public String checkToken(@RequestParam(required=true) String token, HttpServletRequest request, HttpServletResponse response,Model model) throws UnsupportedEncodingException{	
		String authcenterurl = (String) request.getSession().getAttribute("authcenterurl");
		//去认证中心验证token
		String authCheckTokenUrl = authcenterurl + "/oauth/check_token?token="+token;
		String tokenInfo = HttpUtils.doGet(authCheckTokenUrl);

		JsonMapper json = JsonMapper.getInstance();
		Map<String,Object> userinfo = json.fromJson(tokenInfo,Map.class);
		String userName = null;
		try{
			userName = userinfo.get("user_name").toString();//账号
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(userName != null && !userName.equals("")){
			//走客户端登录流程
			String targeturl = null;
			Cookie[] cookies = request.getCookies();
			if(cookies != null){
				for(Cookie cookie : cookies){
					if(cookie.getName().equals("targeturl")){
						targeturl = cookie.getValue();
						//认证中心拿到token回传客户端，由客户端发起验证token请求
					}
				}
			}
			
			if(targeturl != null) {
				return "redirect:/auth/auth_center/form?token="+token + "&targeturl=" + targeturl;
			} else
				return "redirect:/auth/auth_center/form?token="+token;
		}else{//走认证中心登录流程
			SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
			String authLoginUrl = sysPropertyVoWithBLOBsVo.getAuthenticationBasepath() + "/sso/login?toUrl="+WebUtil.getWebPath(request)+loginCallbackUrl;//认证中心登录地址
			return "redirect:"+authLoginUrl;
		}
	}
	
	@RequestMapping(value="appoint/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response){
		String[] pathArray=new String[]{"/titan/","/keeper/","/authcenter/","/angel/","/doctor/","/market/","/wisdom/"};
		for(int j = 0;j < pathArray.length;  j++){
			Cookie cookie = new Cookie("JSESSIONID", null);
			cookie.setMaxAge(0);
			cookie.setPath(pathArray[j]);
			response.addCookie(cookie);
		}
		SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();

		return "redirect:" + sysPropertyVoWithBLOBsVo.getAuthenticationBasepath() + "/sso/logout?toUrl=" + WebUtil.getWebPath(request) + "/firstPage/appoint";
	}

}
