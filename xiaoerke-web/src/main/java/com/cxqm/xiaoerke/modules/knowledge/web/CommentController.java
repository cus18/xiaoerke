package com.cxqm.xiaoerke.modules.knowledge.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cxqm.xiaoerke.common.utils.WechatUtil;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.CookieUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.cms.entity.Comment;
import com.cxqm.xiaoerke.modules.cms.service.CommentService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;

/**
 * 知识库评论Controller
 * @author sunxiao
 * 2015-11-26
 */
@Controller(value = "WebCommentController")
@RequestMapping(value = "${xiaoerkePath}")
public class CommentController extends BaseController {
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private SystemService systemService;
	/**
	 * 根据id查询评论
	 * 2015年11月26日 下午8:24:02
	 * @return Comment
	 * @author sunxiao
	 */
	@ModelAttribute
	public Comment get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return commentService.get(id);
		}else{
			return new Comment();
		}
	}
	
	/**
	 * 保存知识库评论
	 * 2015年11月26日 下午8:22:15
	 * @return Map<String,Object>
	 * @author sunxiao
	 */
	@RequestMapping(value = "/knowledge/comment/save", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> save(@RequestBody Map<String, Object> params,HttpServletRequest request, HttpServletResponse response,HttpSession session) throws Exception {
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		String openId = WechatUtil.getOpenId(session,request);
		Map<String,Object> parameter = systemService.getWechatParameter();
		String token = (String)parameter.get("token");
		String nickName = (String)session.getAttribute("wechatName");
        
        String strURL="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+token+"&openid="+openId+"&lang=zh_CN";
		String param="";
		String json=WechatUtil.post(strURL, param, "GET");
		JSONObject jasonObject = JSONObject.fromObject(json);
        Map<String, Object> jsonMap = (Map) jasonObject;
        
		Comment comment = new Comment();
		comment.setContentId((String)params.get("articleId"));
		comment.setContent((String)params.get("commentContent"));
		comment.setName(nickName);
		comment.setIp(request.getRemoteAddr());
		comment.setCreateDate(new Date());
		comment.setOpenid(openId);
		comment.setHeadimgurl((String) jsonMap.get("headimgurl"));
		comment.setDelFlag("0");
		commentService.saveComment(comment);
		LogUtils.saveLog(Servlets.getRequest(), "00000059","保存知识库登陆用户信息openid:" + openId+"contentId(articleId):"+(String)params.get("articleId"));
		return returnMap;
	}
	
	/**
	 * 知识库评论列表
	 * 2015年11月26日 下午8:23:20
	 * @return Map<String,Object>
	 * @author sunxiao
	 */
	@RequestMapping(value = "/knowledge/comment/list", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> commentList(@RequestParam String articleId,@RequestParam Integer pageNo,@RequestParam Integer pageSize,HttpServletRequest request,HttpSession session) throws Exception {
		HashMap<String, Object> response = new HashMap<String, Object>();
		Comment comment = new Comment();
		comment.setContentId(articleId);
		comment.setDelFlag("0");
		Page<Comment> page = commentService.findPage(new Page<Comment>(pageNo,pageSize), comment);
		
        String openid = WechatUtil.getOpenId(session, request);
		 
		List<HashMap<String,Object>> commentList = new ArrayList();
		//根据文章的ID，获取文章的阅读次数，和分享的次数
		for(Comment c : page.getList())
		{
			HashMap<String,Object> articleMap = new HashMap<String,Object>();
			articleMap.put("articleId", (c.getContentId()));
			articleMap.put("commentContent", (c.getContent()));
			articleMap.put("nickName", (c.getName()));
			articleMap.put("createDate",(c.getCreateDate()));
			articleMap.put("picUrl",c.getHeadimgurl());
			commentList.add(articleMap);
		}
        response.put("commentList", commentList);
        LogUtils.saveLog(Servlets.getRequest(), "00000058","获取评论列表，文章id:" + articleId);
		return response;
	}
	
}
