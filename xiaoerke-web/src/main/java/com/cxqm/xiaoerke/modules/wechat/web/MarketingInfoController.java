package com.cxqm.xiaoerke.modules.wechat.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxqm.xiaoerke.modules.wechat.entity.UserQRCode;
import com.cxqm.xiaoerke.modules.sys.entity.WechatBean;
import com.cxqm.xiaoerke.modules.wechat.service.WeChatInfoService;

@Controller
@RequestMapping(value = "")
public class MarketingInfoController {

	@Autowired
	private WeChatInfoService wcs;

	@Autowired
	private SystemService systemService;

	@Autowired
	private SysPropertyServiceImpl sysPropertyService;

	/**
	     * 验证主入口
	     * @param request
	     * @return
	     */
	    @RequestMapping( value = "/MarketingInfo/wechat/author", method = RequestMethod.GET )
	    public String WechatAuthor(HttpServletRequest request){
			SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
	        String backUrl = request.getParameter("url");
	        String oauth2Url = WechatUtil.getOauth2Url(backUrl,sysPropertyVoWithBLOBsVo);
	        return "redirect:" + oauth2Url;
	    }

	    /**
	     * 引导页
	     * */
	    @RequestMapping(value = "/getWechatOpenid", method = {RequestMethod.POST, RequestMethod.GET})
	    public String getWechatName(HttpServletRequest request, HttpServletResponse response, HttpSession session)throws  Exception{
			SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
			String code=request.getParameter("code");
	        String get_access_token_url="https://api.weixin.qq.com/sns/oauth2/access_token?" +
	                "appid=APPID" +
	                "&secret=SECRET&" +
	                "code=CODE&grant_type=authorization_code";
	        get_access_token_url=get_access_token_url.replace("APPID", sysPropertyVoWithBLOBsVo.getUserCorpid());
	        get_access_token_url=get_access_token_url.replace("SECRET", sysPropertyVoWithBLOBsVo.getUserSectet());
	        get_access_token_url=get_access_token_url.replace("CODE", code);
	        String access_token = "";
	        String openid = "";
	        if(access_token.isEmpty()&&openid.isEmpty()){
	            String json= HttpRequestUtil.getConnectionResult(get_access_token_url, "GET", "");
	            WechatBean wechat = JsonUtil.getObjFromJsonStr(json, WechatBean.class);
	            openid=wechat.getOpenid();
	            session.setAttribute("openid",openid);
				CookieUtils.setCookie(response, "openId", openid, 60 * 60 * 24 * 30);
	        }
	            return "redirect:"+"/ap#/operateIndex";
	    }
	     /**
	      *  通过用户openid获得此用户的QRCode
	      * @param session
	      * @return
	      */
		 @RequestMapping(value = "/marketingInfo/getQRCode", method = {RequestMethod.POST, RequestMethod.GET})
		 public @ResponseBody
		    Map<String, Object>  getQRCodeImageUri(HttpServletRequest request,HttpSession session){
			 Map<String, Object> map=new HashMap<String, Object>();
			 String openId = WechatUtil.getOpenId(session,request);
			 Map<String, Object> qrcodeMap=wcs.getQRCodeFromOpenid(openId);
			 String qrcode="";
			 if(qrcodeMap==null){
				 UserQRCode uc=new UserQRCode();
				 uc.setOpenid(openId);
				 wcs.insertUserQRCode(uc);
				 qrcode=wcs.getQRCodeFromOpenid(openId).get("qrcode").toString();
			 }else{
				 qrcode=qrcodeMap.get("qrcode").toString();
			 }
			 String url= "";//"https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+std.findWeChatToken();
			 String jsonData="{\"expire_seconds\": 604800, \"action_name\": \"QR_SCENE\",\"action_info\": {\"scene\": {\"scene_id\": "+qrcode+"}}}";
			 String reJson=this.post(url, jsonData,"POST");
			 System.out.println(reJson);
			 JSONObject jb=JSONObject.fromObject(reJson);
			 String qrTicket=jb.getString("ticket");
			 String QRCodeURI="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+qrTicket;
			 map.put("QRCode", QRCodeURI);
			 map.put("Ticket",qrTicket);
			 return map;
		 }

		 /**
			 * 发送HttpPost请求
			 *
			 * @param strURL
			 *            服务地址
			 * @param params
			 *            json字符串,例如: "{ \"id\":\"12345\" }" ;其中属性名必须带双引号<br/>
			 *            type (请求方式：POST,GET)
			 * @return 成功:返回json字符串<br/>
			 */
			public  String post(String strURL, String params,String type) {
				System.out.println(strURL);
				System.out.println(params);
				try {
					URL url = new URL(strURL);// 创建连接
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setDoOutput(true);
					connection.setDoInput(true);
					connection.setUseCaches(false);
					connection.setInstanceFollowRedirects(true);
					connection.setRequestMethod(type); // 设置请求方式
					connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
					connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
					connection.connect();
					OutputStreamWriter out = new OutputStreamWriter(
							connection.getOutputStream(), "UTF-8"); // utf-8编码
					out.append(params);
					out.flush();
					out.close();
					// 读取响应
					int length = (int) connection.getContentLength();// 获取长度
					InputStream is = connection.getInputStream();
					if (length != -1) {
						byte[] data = new byte[length];
						byte[] temp = new byte[512];
						int readLen = 0;
						int destPos = 0;
						while ((readLen = is.read(temp)) > 0) {
							System.arraycopy(temp, 0, data, destPos, readLen);
							destPos += readLen;
						}
						String result = new String(data, "UTF-8"); // utf-8编码
						System.out.println(result);
						return result;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null; // 自定义错误信息
			}

		     /**
		      *  通过用户openid获得此用户推广人LIST
		      * @param session
		      * @return
		      */
			 @RequestMapping(value = "/marketingInfo/getAttentionFromMarketer", method = {RequestMethod.POST, RequestMethod.GET})
			 public @ResponseBody
			    Map<String, Object>  getAttentionFromMarketer(HttpServletRequest request,HttpSession session){
				 String openId = WechatUtil.getOpenId(session,request);
				 String marketer = openId;
				 Map<String,Object> so=wcs.getQRCodeFromOpenid(marketer);
				 Map<String, Object> map=new HashMap<String, Object>();
				 if(so==null){
					 map.put("userList", null);
					 return map;
				 }
				 marketer=so.get("qrcode").toString();
				 String time=so.get("time").toString();
				 Map<String,Object> m=new HashMap<String, Object>();
				 m.put("openid", marketer);
				 m.put("time", time);
				 if(marketer==null){
					 map.put("userList", null);
					 return map;
				 }
				 List<Map<String,Object>>  userList=wcs.getShareFromOpenid(m);
				 if(userList==null){
					 map.put("userList", null);
					 return map;
				 }
				 List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
				 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				 for (Map<String, Object> user : userList) {
					 Map<String,Object> userAlone=new HashMap<String, Object>();
					 String openid=user.get("open_id").toString();
					 String strURL="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+systemService.getWechatParameter()+"&openid="+openid+"&lang=zh_CN";
					 String param="";
					 String json=this.post(strURL, param, "GET");
					 JSONObject jo=JSONObject.fromObject(json);
					 if(jo.get("subscribe").toString().equals("0")){
						 continue;
					 }
					 String adate=sdf.format(Long.parseLong(jo.get("subscribe_time").toString())*1000);
					 Map<String,Object> getL=new HashMap<String, Object>();
					 getL.put("openid", openid);
					 getL.put("time", adate);
					 List<Map<String,Object>>  shareL=wcs.getShareFromOpenidAndAPITime(getL);
					 if(shareL.size()==0){
						 continue;
					 }
					 userAlone.put("headImage", jo.get("headimgurl"));
					 userAlone.put("name", jo.get("nickname"));
					 userAlone.put("time",sdf.format(Long.parseLong(jo.get("subscribe_time").toString())*1000) );
					 list.add(userAlone);
				}
				 map.put("userList", list);
				 return map;
			 }

}
