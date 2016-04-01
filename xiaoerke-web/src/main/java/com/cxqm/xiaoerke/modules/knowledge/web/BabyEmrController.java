package com.cxqm.xiaoerke.modules.knowledge.web;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cxqm.xiaoerke.common.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.cms.entity.BabyEmrVo;
import com.cxqm.xiaoerke.modules.cms.service.BabyEmrService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;

/**
 * 知识库宝宝账号Controller
 * @author sunxiao
 * 2015-11-26
 */
@Controller
@RequestMapping(value = "${xiaoerkePath}")
public class BabyEmrController extends BaseController {

	@Autowired
	private BabyEmrService babyEmrService;
	
	@Autowired
    private SystemService systemService;
	
	private static long picLen;
	
	/**
	 * 根据id查询评论
	 * 2015年11月26日 下午8:24:02
	 * @return BabyEmrVo
	 * @author sunxiao
	 *//*
	@ModelAttribute
	public BabyEmrVo get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return babyEmrService.get(id);
		}else{
			return new BabyEmrVo();
		}
	}*/
	
	/**
	 * 保存宝宝信息
	 * 2015年11月26日 下午8:22:15
	 * @return Map<String,Object>
	 * @author sunxiao
	 */
	@RequestMapping(value = "/knowledge/babyEmr/save", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> save(@RequestBody Map<String, Object> params,
            HttpServletRequest request,HttpSession session) throws Exception {
		HashMap<String, Object> response = new HashMap<String, Object>();
		String openId = WechatUtil.getOpenId(session,request);
		String mediaId = (String)params.get("mediaId");
		List<BabyEmrVo> list = babyEmrService.getBabyEmrList(openId);
		if(list.size()>0){
			BabyEmrVo bev = new BabyEmrVo();
		    bev.setId(list.get(0).getId());
		    bev.setBirthday(DateUtils.StrToDate((String) params.get("babyBirthday"), "date"));
		     bev.setGender((String) params.get("gender"));
		    bev.setBabyName((String) params.get("babyName"));
		    if(mediaId == null){
		       bev.setStatus("0");
		    }else{
		       uploadArticleImage(bev.getId(),mediaId);
		       bev.setStatus("1");
		    }
		    bev.setUpdateDate(new Date());
		    babyEmrService.update(bev);
		    LogUtils.saveLog(Servlets.getRequest(), "00000054" + bev.getId());//修改知识库登陆用户信息
		}else{
			BabyEmrVo bev = new BabyEmrVo();
			bev.setId(IdGen.uuid());
			bev.setOpenid(openId);
			bev.setBirthday(DateUtils.StrToDate((String) params.get("babyBirthday"), "date"));
			bev.setGender((String) params.get("gender"));
			bev.setBabyName((String) params.get("babyName"));
			if(mediaId == null){
				bev.setStatus("0");
			}else{
				uploadArticleImage(bev.getId(),mediaId);
				bev.setStatus("1");
			}
			bev.setCreateDate(new Date());
			babyEmrService.save(bev);
			LogUtils.saveLog(Servlets.getRequest(), "00000055" + bev.getId());//保存知识库登陆用户信息
		}
		
		return response;
	}
	
	/**
	 * 修改宝宝信息
	 * 2015年12月1日 下午8:07:15
	 * @return Map<String,Object>
	 * @author sunxiao
	 */
	@RequestMapping(value = "/knowledge/babyEmr/updateBabyEmr", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> updateBabyEmr(@RequestBody Map<String, Object> params,HttpServletRequest request,HttpSession session) throws Exception {
		HashMap<String, Object> response = new HashMap<String, Object>();
		String openId = WechatUtil.getOpenId(session,request);
        List<BabyEmrVo> list = babyEmrService.getBabyEmrList(openId);
		BabyEmrVo bev = new BabyEmrVo();
		bev.setId(list.get(0).getId());
		bev.setBirthday(DateUtils.StrToDate((String) params.get("birthday"), "date"));
		bev.setUpdateDate(new Date());
		babyEmrService.update(bev);
		session.setAttribute("updateExpiredTodaySelectAndReadArticleDate",null);
		session.setAttribute("TodaySelectAndReadArticleList",null);
		LogUtils.saveLog(Servlets.getRequest(), "00000056" + bev.getId());//更改知识库登陆用户信息
		return response;
	}
	
	/**
	 * 修改宝宝头像
	 * 2015年11月26日 下午8:22:15
	 * @return Map<String,Object>
	 * @author sunxiao
	 */
	@RequestMapping(value = "/knowledge/babyEmr/updatePic", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> updatePic(@RequestParam String mediaId,HttpServletRequest request,HttpSession session) throws Exception {
		HashMap<String, Object> response = new HashMap<String, Object>();
		String openId = WechatUtil.getOpenId(session,request);
        List<BabyEmrVo> list = babyEmrService.getBabyEmrList(openId);
        uploadArticleImage(list.get(0).getId(),mediaId);
        //上传头像成功，修改状态
        BabyEmrVo bev = new BabyEmrVo();
        bev.setId(list.get(0).getId());
        bev.setStatus("1");
        babyEmrService.update(bev);
		return response;
	}
	
	private void uploadArticleImage(String id , String mediaId) throws Exception{
		//上传图片至阿里云
		OSSObjectTool.uploadFileInputStream(id, picLen, getInputStream(mediaId), OSSObjectTool.BUCKET_ARTICLE_PIC);
	}
	
	/**
	 * 保存宝宝信息
	 * 2015年11月26日 下午8:22:15
	 * @return Map<String,Object>
	 * @author sunxiao
	 */
	@RequestMapping(value = "/knowledge/babyEmr/getBabyEmrList", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getBabyEmrList(HttpServletRequest request,HttpSession session) throws Exception {
		HashMap<String, Object> response = new HashMap<String, Object>();
		String openId = WechatUtil.getOpenId(session, request);
		List<BabyEmrVo> list = babyEmrService.getBabyEmrList(openId);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if(list.size()!=0){
			response.put("name", list.get(0).getBabyName());
			response.put("birthday", df.format(list.get(0).getBirthday()));
			response.put("id", list.get(0).getId());
			response.put("status", list.get(0).getStatus());
		}
		return response;
	}
	
	/**
	    * 根据文件id下载文件
	    * 
	    * @param mediaId
	    *            媒体id
	    * @throws Exception
	    */
	   public  InputStream getInputStream(String mediaId) {
	       InputStream is = null;
	       Map<String,Object> parameter = systemService.getWechatParameter();
	       String token = (String)parameter.get("token");
	       String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="
	               + token + "&media_id=" + mediaId;
	       try {
	           URL urlGet = new URL(url);
	           HttpURLConnection http = (HttpURLConnection) urlGet
	                   .openConnection();
	           http.setRequestMethod("GET"); // 必须是get方式请求
	           http.setRequestProperty("Content-Type",
	                   "application/x-www-form-urlencoded");
	           http.setDoOutput(true);
	           http.setDoInput(true);
	           System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
	           System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
	           http.connect();
	           // 获取文件转化为byte流
	           is = http.getInputStream();
	           picLen = http.getContentLengthLong();
	       } catch (Exception e) {
	           e.printStackTrace();
	       }
	       return is;
	   }
	
}
