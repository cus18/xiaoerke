package com.cxqm.xiaoerke.modules.knowledge.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cxqm.xiaoerke.common.utils.WechatUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxqm.xiaoerke.common.utils.CookieUtils;
import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.cms.entity.Article;
import com.cxqm.xiaoerke.modules.cms.entity.ArticleData;
import com.cxqm.xiaoerke.modules.cms.service.ArticleDataService;
import com.cxqm.xiaoerke.modules.cms.service.ArticleService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;

/**
 * 文章Controller
 * @author sunxiao
 * @version 2015-10-23
 */
@Controller
@RequestMapping(value = "knowledge")
public class ArticleController extends BaseController {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private WechatAttentionService wechatAttentionService;
	
	@Autowired
	private ArticleDataService articleDataService;
	
	
	@ModelAttribute
	public Article get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return articleService.get(id);
		}else{
			return new Article();
		}
	}
	
	/**
	 * 获取文章列表
	 * 2015年12月1日 上午11:18:40
	 * @return Map<String,Object>
	 * @author sunxiao
	 */
	@RequestMapping(value = "/article/articleList", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> articleList(@RequestBody Map<String, Object> params) throws Exception {
		return articleService.getArticleList(params);
	}
	
	/**
	 * 获取今日精选和今日必读文章
	 * 2015年12月1日 上午11:17:57
	 * @return Map<String,Object>
	 * @author sunxiao
	 */
	@RequestMapping(value = "/article/todaySelectAndReadArticleList", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> todaySelectAndReadArticleList(@RequestBody Map<String, Object> params,HttpServletRequest request,HttpSession session) throws Exception {
		String openId = WechatUtil.getOpenId(session, request);
        Map<String, Object> map = new HashMap<String, Object>();
        Date updateExpiredTodaySelectAndReadArticleDate =  (Date)session.getAttribute("updateExpiredTodaySelectAndReadArticleDate");
		if (updateExpiredTodaySelectAndReadArticleDate == null || (updateExpiredTodaySelectAndReadArticleDate != null 
				&& updateExpiredTodaySelectAndReadArticleDate.getTime() < new Date().getTime())){
			String now = com.cxqm.xiaoerke.common.utils.DateUtils.DateToStr(new Date(),"datetime");
			String day = now.split(" ")[0];
			String future = day+" 23:59:59";
			int diff = (int) (com.cxqm.xiaoerke.common.utils.DateUtils.StrToDate(future, "datetime").getTime()-new Date().getTime());
			session.setAttribute("updateExpiredTodaySelectAndReadArticleDate", DateUtils.addMilliseconds(new Date(), diff));
			map = articleService.getTodaySelectAndReadArticleList(params,openId);
			session.setAttribute("TodaySelectAndReadArticleList", map);
		}
		
		if(session.getAttribute("TodaySelectAndReadArticleList")!=null){
			map = (Map<String, Object>) session.getAttribute("TodaySelectAndReadArticleList");
		}else{
			map = articleService.getTodaySelectAndReadArticleList(params,openId);
		}
		
		return map;
	}
	
	@RequestMapping(value = "/article/articleDetail", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> articleDetail(@RequestBody Map<String, Object> params,HttpServletRequest request,HttpServletResponse respon) throws Exception {
		HashMap<String, Object> response = new HashMap<String, Object>();
		String openId = (String)request.getSession().getAttribute("openId");
		String id = (String) params.get("id");
		// 记录日志
		String cookieID=CookieUtils.getCookie(request, "cookieID");
		if(cookieID==null){
			cookieID=IdGen.uuid();
			CookieUtils.setCookie(respon, "cookieID",cookieID ,3600*24*365);
		}
		LogUtils.saveLog(Servlets.getRequest(), "00000052","查看某篇文章:" + id+":CookieID:"+cookieID+":generalize-detail:"+params.get("generalize"));
		Article article = get(id);
        ArticleData ad = articleDataService.get(id);
        List<Map<String, String>> list = articleService.findTitleByIds(ad.getRelation());
        String openIdFlag = "no";
        if(openId!=null){
        	SysWechatAppintInfoVo sysWechatAppintInfoVo = new SysWechatAppintInfoVo();
            sysWechatAppintInfoVo.setOpen_id(openId);
            SysWechatAppintInfoVo resultVo = wechatAttentionService.findAttentionInfoByOpenId(sysWechatAppintInfoVo);
            if(resultVo!=null){
            	openIdFlag = "yes";
            }
        }
        articleService.updateHitsAddOne(id);
        
        response.put("article", article);
        response.put("articleDetail", ad);
        response.put("articleReadCount",article.getHits());
        response.put("articleShareCount",article.getShare());
        response.put("articlePraiseCount",article.getPraise());
        response.put("relation", list);
        response.put("openIdFlag", openIdFlag);

		return response;
	}

	@RequestMapping(value = "/ArticleShareRecord", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	String ArticleShareRecord(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        String contentId = (String) params.get("contentId");
        if (contentId != null && !contentId.isEmpty()) {
            LogUtils.saveLog(request,"00000053","分享知识库文章:" + contentId);
        }
        if(contentId.contains(":")){
            articleService.updateShareAddOne(contentId.split(":")[1]);
        }else{
            articleService.updateShareAddOne(contentId);
        }

        return "";
    }

	@RequestMapping(value = "/knowledge/ArticleZanRecord", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	int ArticleZanRecord(@RequestBody Map<String, Object> params, HttpServletRequest request) {
		String articleId = (String) params.get("articleId");
		String praiseNumber = String.valueOf(params.get("PraiseNumber"));
		return articleService.updatePraiseNumber(articleId,praiseNumber);
	}
	
	/**
	 * 获取短期处理情况
	 * sunxiao
	 * @param type
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/shortTermProcess", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	HashMap<String, Object> shortTermProcess(@RequestParam String type, HttpServletRequest request) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		try{
			List<Article> list = articleService.findArticleListByType(type);
			response.put("resultCode", 0);
			response.put("resultMsg", "");
			response.put("list", list);
		}catch(Exception e){
			response.put("resultCode", 1);
			response.put("resultMsg", "获取短期处理情况失败！");
		}
		return response;
	}
	
	/**
	 * 妈妈误区
	 * sunxiao
	 * @param type
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/motherError", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	HashMap<String, Object> motherError(@RequestParam String type, HttpServletRequest request) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		try{
			List<Article> list = articleService.findArticleListByType(type);
			response.put("resultCode", 0);
			response.put("resultMsg", "");
			response.put("list", list);
		}catch(Exception e){
			response.put("resultCode", 1);
			response.put("resultMsg", "获取短期处理情况失败！");
		}
		return response;
	}
}
