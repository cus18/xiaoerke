package com.cxqm.xiaoerke.modules.wechat.service.impl;

import com.cxqm.xiaoerke.common.config.Global;
import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.healthRecords.service.HealthRecordsService;
import com.cxqm.xiaoerke.modules.interaction.dao.PatientRegisterPraiseDao;
import com.cxqm.xiaoerke.modules.marketing.service.LoveMarketingService;
import com.cxqm.xiaoerke.modules.member.service.MemberService;
import com.cxqm.xiaoerke.modules.sys.entity.*;
import com.cxqm.xiaoerke.modules.sys.service.ActivityService;
import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import com.cxqm.xiaoerke.modules.sys.utils.WechatMessageUtil;
//import com.cxqm.xiaoerke.modules.umbrella.entity.BabyUmbrellaInfo;
//import com.cxqm.xiaoerke.modules.umbrella.service.BabyUmbrellaInfoService;
import com.cxqm.xiaoerke.modules.umbrella.entity.BabyUmbrellaInfo;
import com.cxqm.xiaoerke.modules.umbrella.service.BabyUmbrellaInfoService;
import com.cxqm.xiaoerke.modules.wechat.dao.WechatAttentionDao;
import com.cxqm.xiaoerke.modules.wechat.dao.WechatInfoDao;
import com.cxqm.xiaoerke.modules.wechat.entity.HealthRecordMsgVo;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import com.cxqm.xiaoerke.modules.wechat.service.WechatPatientCoreService;
import com.cxqm.xiaoerke.modules.wechat.service.util.MessageUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Transactional(readOnly = false)
public class WechatPatientCoreServiceImpl implements WechatPatientCoreService {

	@Autowired
	private WechatInfoDao wechatInfoDao;

	@Autowired
	private WechatAttentionDao wechatAttentionDao;

	@Autowired
	private SystemService systemService;

	@Autowired
	private ConsultSessionService consultConversationService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private MongoDBService<WechatAttention> mongoDBService;

	@Autowired
	private MongoDBService<MongoLog> mongoLogService;

    @Autowired
    private MongoDBService<HealthRecordMsgVo> healthRecordMsgVoMongoDBService;

    @Autowired
	private MemberService memberService;

	@Autowired
	private SessionRedisCache sessionRedisCache;

	private String mongoEnabled = Global.getConfig("mongo.enabled");

	@Autowired
	private PatientRegisterPraiseDao patientRegisterPraiseDao;

	@Autowired
	LoveMarketingService loveMarketingService;

	@Autowired
	private BabyUmbrellaInfoService babyUmbrellaInfoService;

	@Autowired
	private WechatAttentionService wechatAttentionService;

	private static ExecutorService threadExecutor = Executors.newSingleThreadExecutor();

	/**
	 * 处理微信发来的请求
	 *
	 * @param request
	 * @return
	 */
	@Override
	public String processPatientRequest(HttpServletRequest request,HttpServletResponse response) throws IOException {
		System.out.println("processPatientRequest===================================");
		String respMessage = null;

		/** 解析xml数据 */
		ReceiveXmlEntity xmlEntity = new ReceiveXmlProcess().getMsgEntity(getXmlDataFromWechat(request));
		String msgType = xmlEntity.getMsgType();

		// xml请求解析
		if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
			// 事件类型
			String eventType = xmlEntity.getEvent();
			if(eventType.equals(MessageUtil.SCAN)){
				//已关注公众号的情况下扫描
				this.updateAttentionInfo(xmlEntity);
				respMessage = processScanEvent(xmlEntity,"oldUser",request,response);
			}else if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)){
				//扫描关注公众号或者搜索关注公众号都在其中
				respMessage = processSubscribeEvent(xmlEntity, request,response);
			}
			// 取消订阅
			else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)){
				processUnSubscribeEvent(xmlEntity, request);
			}
			// 自定义菜单点击事件
			else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)){
				respMessage = processClickMenuEvent(xmlEntity,request,response);
			}
			// 结束咨询对话
			else if(eventType.equals(MessageUtil.KF_CLOSE)){
				respMessage = processCloseEvent(xmlEntity,request,response);
			}
			//获取用户位置
			else if (eventType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)){
				processGetLocationEvent(xmlEntity,request);
			}
			//点击菜单中的链接
			else if (eventType.equals(MessageUtil.EVENT_TYPE_VIEW)){
				//respMessage = processClickMenulinkEvent(xmlEntity,request,response);
			}
		}
		else {
			String customerService = Global.getConfig("wechat.customservice");
			if("false".equals(customerService)){
				Runnable thread = new processConsultMessageThread(xmlEntity);
				threadExecutor.execute(thread);
				return "";
			}else if("true".equals(customerService)){
			  respMessage = transferToCustomer(xmlEntity);
			}
		}
		return respMessage;
	}

	public class processConsultMessageThread extends Thread {
		private ReceiveXmlEntity xmlEntity;

		public processConsultMessageThread(ReceiveXmlEntity xmlEntity) {
			this.xmlEntity = xmlEntity;
		}

		public void run() {
			try {
				System.out.println(xmlEntity.getContent());
				if(xmlEntity.getMsgType().equals("text")){
					this.sendPost(ConstantUtil.ANGEL_WEB_URL + "angel/consult/wechat/conversation",
							"openId=" + xmlEntity.getFromUserName() +
							"&messageType=" + xmlEntity.getMsgType() +
							"&messageContent=" + URLEncoder.encode(xmlEntity.getContent(), "UTF-8"));
				}else{
					this.sendPost(ConstantUtil.ANGEL_WEB_URL + "angel/consult/wechat/conversation",
							"openId=" + xmlEntity.getFromUserName() +
							"&messageType=" + xmlEntity.getMsgType() +
							"&mediaId=" + xmlEntity.getMediaId());
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		/**
		 * 向指定 URL 发送POST方法的请求
		 *
		 * @param url
		 *            发送请求的 URL
		 * @param param
		 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
		 * @return 所代表远程资源的响应结果
		 */
		public String sendPost(String url, String param) {
			PrintWriter out = null;
			BufferedReader in = null;
			String result = "";
			try {
				URL realUrl = new URL(url);
				// 打开和URL之间的连接
				URLConnection conn = realUrl.openConnection();
				// 设置通用的请求属性
				conn.setRequestProperty("accept", "*/*");
				conn.setRequestProperty("connection", "Keep-Alive");
				conn.setRequestProperty("user-agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
				// 发送POST请求必须设置如下两行
				conn.setDoOutput(true);
				conn.setDoInput(true);
				// 获取URLConnection对象对应的输出流
				out = new PrintWriter(conn.getOutputStream());
				// 发送请求参数
				out.print(param);
				// flush输出流的缓冲
				out.flush();
				// 定义BufferedReader输入流来读取URL的响应
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
			} catch (Exception e) {
				System.out.println("发送 POST 请求出现异常！"+e);
				e.printStackTrace();
			}
			//使用finally块来关闭输出流、输入流
			finally{
				try{
					if(out!=null){
						out.close();
					}
					if(in!=null){
						in.close();
					}
				}
				catch(IOException ex){
					ex.printStackTrace();
				}
			}
			return result;
		}
	}

	private String getXmlDataFromWechat(HttpServletRequest request)
	{
		/** 读取接收到的xml消息 */
		StringBuffer sb = new StringBuffer();
		InputStream is = null;
		try {
			is = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String s = "";
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	private String processScanEvent(ReceiveXmlEntity xmlEntity,String userType,HttpServletRequest request,HttpServletResponse response)
	{
		String EventKey = xmlEntity.getEventKey();
		System.out.println(EventKey + "EventKey=========================================");
		Article article = new Article();
		List<Article> articleList = new ArrayList<Article>();
		NewsMessage newsMessage = new NewsMessage();
		newsMessage.setToUserName(xmlEntity.getFromUserName());
		newsMessage.setFromUserName(xmlEntity.getToUserName());
		newsMessage.setCreateTime(new Date().getTime());
		newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
		newsMessage.setFuncFlag(0);
		if(EventKey.indexOf("baoxian_000001")>-1&&xmlEntity.getEvent().equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)){
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(xmlEntity.getFromUserName());
			textMessage.setFromUserName(xmlEntity.getToUserName());
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);
			textMessage.setContent("尊敬的诺安康VIP客户，您好！欢迎加入宝大夫，让您从此育儿不用愁！\n\n【咨询大夫】直接咨询北京三甲医院儿科专家，一分钟内极速回复！\n\n【妈妈活动】添加宝大夫客服微信：bdfdxb，加入宝大夫家长群，与众多宝爸宝妈一起交流分享，参与更多好玩的活动！\n\n如需人工协助，请您拨打：400-623-7120。\n");
			return MessageUtil.textMessageToXml(textMessage);

		}else
		if(EventKey.indexOf("doc")>-1)
		{
			Map<String,Object> map = wechatInfoDao.getDoctorInfo(EventKey.replace("doc",""));
			article.setTitle("您已经成功关注" + map.get("hospitalName") + map.get("name") + "医生，点击即可预约");
			article.setDescription("");
			article.setPicUrl(ConstantUtil.TITAN_WEB_URL + "/titan/images/attentionDoc.jpg");
			article.setUrl(ConstantUtil.TITAN_WEB_URL + "/titan/appoint#/doctorAppointment/" + map.get("id") + ",,,,,doctorShare,,");
			articleList.add(article);
		}else if(EventKey.indexOf("267")>-1){
			article.setTitle("恭喜您,通过‘糖盒儿(tanghe2)’关注宝大夫,不仅可以随时免费咨询儿科专家,还可获赠一次预约名医的机会。");
			article.setDescription("");
			articleList.add(article);
		}else if(EventKey.indexOf("263")>-1){
			article.setTitle("【郑玉巧育儿经】--宝大夫");
			article.setDescription("智能匹配月龄，获取针对一对一育儿指导，建立宝宝专属健康档案，一路呵护，茁壮成长！");
			article.setPicUrl(ConstantUtil.TITAN_WEB_URL + "/titan/images/Follow.jpg");
			article.setUrl(ConstantUtil.TITAN_WEB_URL + "/titan/appoint#/knowledgeIndex");
			articleList.add(article);
		}else if(EventKey.indexOf("521")>-1){
			article.setTitle("宝大夫关爱儿童公益活动");
			article.setDescription("赶快去邀请更多的人传递爱吧！");
			article.setPicUrl("http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/MessageImage/10.pic_hd.jpg");
			article.setUrl(ConstantUtil.TITAN_WEB_URL+"/titan/appoint#/knowledgeIndex");
			articleList.add(article);
			//更新二维码拥有者善款
			loveMarketingService.updateInviteMan(EventKey,xmlEntity.getFromUserName());
		}
		else if(EventKey.indexOf("month")>-1)
		{
			if(userType.equals("newUser"))
			{
				Boolean value = activityService.judgeActivityValidity(EventKey.replace("qrscene_", ""));
				if(value==false)
				{
					//推送赠送月会员URL消息
					article.setTitle("月卡");
					article.setDescription("感谢您关注宝大夫综合育儿服务平台，宝大夫现对新用户推出月卡服务。");
					article.setPicUrl(ConstantUtil.TITAN_WEB_URL + "/titan/images/Follow.jpg");
					article.setUrl(ConstantUtil.TITAN_WEB_URL + "/titan/wechatInfo/fieldwork/wechat/author?url="+
							ConstantUtil.TITAN_WEB_URL + "/titan/wechatInfo/getUserWechatMenId?url=21");
					articleList.add(article);
					memberService.insertMemberSendMessage(xmlEntity.getFromUserName(), "1");
				}
				else if(value == true)
				{
					article.setTitle("活动已过期，赠送周会员");
					article.setDescription("您好，此活动已过期，不过别担心，您仍可参加免费体验宝大夫短期会员服务");
					article.setPicUrl(ConstantUtil.TITAN_WEB_URL + "/titan/images/Follow.jpg");
					article.setUrl(ConstantUtil.TITAN_WEB_URL + "/titan/wechatInfo/fieldwork/wechat/author?url=" +
							ConstantUtil.TITAN_WEB_URL + "/titan/wechatInfo/getUserWechatMenId?url=20");
					articleList.add(article);
				}
			}
			else if(userType.equals("oldUser"))
			{
				article.setDescription("感谢您关注宝大夫综合育儿服务平台，本次活动只针对新关注用户，" +
						"请把这份幸运分享给您身边的宝宝吧！再次感谢您对宝大夫的支持与信任！" +
						"\n\n点击进入，全新郑玉巧玉儿经免费查阅！");
				articleList.add(article);
				article.setUrl("");
			}
		}
		else if(EventKey.indexOf("quarter")>-1)
		{
			if(userType.equals("newUser"))
			{
				Boolean value = activityService.judgeActivityValidity(EventKey.replace("qrscene_", ""));
				if (value == false) {
					//推送赠送季会员URL消息
					article.setTitle("季卡");
					article.setDescription("感谢您关注宝大夫综合育儿服务平台，宝大夫现对新用户推出季卡服务。");
					article.setPicUrl(ConstantUtil.TITAN_WEB_URL+"/titan/images/Follow.jpg");
					article.setUrl(ConstantUtil.TITAN_WEB_URL+"/titan/wechatInfo/fieldwork/wechat/author?url="+
							ConstantUtil.TITAN_WEB_URL+"/titan/wechatInfo/getUserWechatMenId?url=22");
					articleList.add(article);
					memberService.insertMemberSendMessage(xmlEntity.getFromUserName(), "1");
				}
				else if(value == true)
				{
					article.setTitle("活动已过期，赠送周会员");
					article.setDescription("您好，此活动已过期，不过别担心，您仍可参加免费体验宝大夫短期会员服务");
					article.setPicUrl(ConstantUtil.TITAN_WEB_URL + "/titan/images/Follow.jpg");
					article.setUrl(ConstantUtil.TITAN_WEB_URL + "/titan/wechatInfo/fieldwork/wechat/author?url=" +
							ConstantUtil.TITAN_WEB_URL + "/titan/wechatInfo/getUserWechatMenId?url=20");
					articleList.add(article);
				}
			}
			else if(userType.equals("oldUser"))
			{
				article.setDescription("感谢您关注宝大夫综合育儿服务平台，本次活动只针对新关注用户，" +
						"请把这份幸运分享给您身边的宝宝吧！再次感谢您对宝大夫的支持与信任！" +
						"\n\n点击进入，全新郑玉巧玉儿经免费查阅");
				article.setUrl("http://baodf.com/titan/wechatInfo/fieldwork/wechat/author?" +
						"url=http://baodf.com/titan/wechatInfo/getUserWechatMenId?url=4");
				articleList.add(article);
			}
		}
		else if(EventKey.indexOf("xuanjianghuodong_zhengyuqiao_saoma")>-1)
		{
			article.setDescription("您好，欢迎关注！" +
					"\n\n点击进入宝大夫-郑玉巧育儿经，一起交流学习育儿健康管理知识！");
			article.setUrl("http://baodf.com/titan/wechatInfo/fieldwork/wechat/author?" +
					"url=http://baodf.com/titan/wechatInfo/getUserWechatMenId?url=4");
			articleList.add(article);
		}else if(EventKey.indexOf("FQBTG")>-1){
			article.setTitle("防犬宝,一份温馨的安全保障");
			article.setDescription("只要19.8元，打狂犬疫苗最高可获得互助补贴1000元。不幸患狂犬病可获得互助补贴5万元！");
			article.setPicUrl("http://oss-cn-beijing.aliyuncs.com/xiaoerke-article-pic/FQBTGXX.png");
			article.setUrl(ConstantUtil.KEEPER_WEB_URL + "/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=26");
			articleList.add(article);
		}else if(EventKey.indexOf("homepage_qualityservices_kuaizixun")>-1){//官网快咨询
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(xmlEntity.getFromUserName());
			textMessage.setFromUserName(xmlEntity.getToUserName());
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);
			textMessage.setContent("1、点击左下角“小键盘”输入文字或语音,即可咨询疾病或保健问题\t\t\n 2、免费在线咨询时间:\n小儿内科:   24小时全天\n小儿皮肤科:   9:00~22:00\n营养保健科:   9:00~22:00\n小儿其他专科:(外科、眼科、耳鼻喉科、口腔科、预防保健科、中医科)   19:00~21:00 \n妇产科   19:00~22:00");
			return MessageUtil.textMessageToXml(textMessage);
		}else if(EventKey.indexOf("homepage_qualityservices_mingyimianzhen")>-1){//官网名医面诊
			article.setTitle("名医面诊");
			article.setDescription("三甲医院儿科专家，线上准时预约，线下准时就诊。");
			article.setPicUrl("http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/gw%2Fmingyimianzhen");
			article.setUrl(ConstantUtil.TITAN_WEB_URL+"/titan/firstPage/appoint");
			articleList.add(article);
		}else if(EventKey.indexOf("homepage_qualityservices_mingyidianhua")>-1){//官网名医电话
			article.setTitle("名医电话");
			article.setDescription("权威儿科专家，10分钟通话，个性化就诊和康复指导。");
			article.setPicUrl("http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/gw%2Fmingyidianhua");
			article.setUrl(ConstantUtil.TITAN_WEB_URL + "/titan/firstPage/phoneConsult");
			articleList.add(article);
		}else if(EventKey.indexOf("qrscene_12")>-1){//扫码分享
			String toOpenId = xmlEntity.getFromUserName();//扫码者openid
			Map<String, Object> param1 = new HashMap<String, Object>();
			param1.put("openid",toOpenId);
			List<Map<String,Object>> list1 = babyUmbrellaInfoService.getBabyUmbrellaInfo(param1);
			Map<String, Object> param = new HashMap<String, Object>();
			String id = EventKey.split("_")[1];
			param.put("id",id);
			List<Map<String,Object>> list = babyUmbrellaInfoService.getBabyUmbrellaInfo(param);
			String tourl = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrellab";
			BabyUmbrellaInfo newBabyUmbrellaInfo = new BabyUmbrellaInfo();
			System.out.println(list1.size()+"list1.size()++++++++++++++++++++++++++++++++++++++++++++++");
			boolean sendsucmes = false;
			if(list1.size()==0){//用户第一次加入保护伞
				tourl = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrellaa";
				Map maps = new HashMap();
				maps.put("type","umbrella");
				SwitchConfigure switchConfigure = systemService.getUmbrellaSwitch(maps);
				String flag = switchConfigure.getFlag();
				//        flag为1是打开，0是关闭
				double ram=0;
				if(flag.equals("1")) {
					ram = Math.random() * 5;
//        while (ram < 1){
//            ram=Math.random() * 5;
//        }

					do {
						ram = Math.random() * 5;
					} while (ram < 1);
				}
				String res = String.format("%.0f", ram);
				newBabyUmbrellaInfo.setTruePayMoneys(res);
				newBabyUmbrellaInfo.setVersion("a");
				if(res.equals("0")){
					newBabyUmbrellaInfo.setPayResult("success");
					sendsucmes = true;
//            babyUmbrellaInfo.setActivationTime(new Date());
				}else {
					newBabyUmbrellaInfo.setPayResult("fail");
				}
				if(list.size()!=0){//有分享者时，修改分享者信息并发送给分享者信息
					String fromOpenId = (String)list.get(0).get("openid");//分享者openid
					String babyId = (String)list.get(0).get("baby_id");
					Map parameter = systemService.getWechatParameter();
					String token = (String)parameter.get("token");
					int oldUmbrellaMoney = (Integer) list.get(0).get("umbrella_money");
					int newUmbrellaMoney = (Integer) list.get(0).get("umbrella_money")+20000;
					int friendJoinNum = (Integer) list.get(0).get("friendJoinNum");
					WechatAttention wa = wechatAttentionService.getAttentionByOpenId(toOpenId);
					String nickName = "";
					if(wa!=null){
						nickName = StringUtils.isNotNull(wa.getNickname())?wa.getNickname():"";
					}
					String title = "恭喜您，您的好友"+nickName+"已成功加入。您既帮助了朋友，也提升了2万保障金！";
					String templateId = "b_ZMWHZ8sUa44JrAjrcjWR2yUt8yqtKtPU8NXaJEkzg";
					String keyword1 = "您已拥有"+newUmbrellaMoney/10000+"万的保障金，还需邀请"+(400000-newUmbrellaMoney)/20000+"位好友即可获得最高40万保障金。";
					String remark = "邀请一位好友，增加2万保额，最高可享受40万保障！";
					if(oldUmbrellaMoney<400000&&!EventKey.contains("qrscene_120000000")){
						BabyUmbrellaInfo babyUmbrellaInfo = new BabyUmbrellaInfo();
						babyUmbrellaInfo.setId(Integer.parseInt(id));
						babyUmbrellaInfo.setUmberllaMoney(newUmbrellaMoney);
						babyUmbrellaInfoService.updateBabyUmbrellaInfoById(babyUmbrellaInfo);
					}
					if (newUmbrellaMoney>=400000){
						title = "感谢您的爱心，第"+(friendJoinNum+1)+"位好友"+nickName+"已成功加入，一次分享，一份关爱，汇聚微小力量，传递大爱精神！";
						templateId = "b_ZMWHZ8sUa44JrAjrcjWR2yUt8yqtKtPU8NXaJEkzg";
						keyword1 = "您已成功拥有40万的最高保障金。";
						remark = "您还可以继续邀请好友，传递关爱精神，让更多的家庭拥有爱的保障！";
					}
					BabyUmbrellaInfo babyUmbrellaInfo = new BabyUmbrellaInfo();
					babyUmbrellaInfo.setId(Integer.parseInt(id));
					babyUmbrellaInfo.setFriendJoinNum(friendJoinNum+1);
					babyUmbrellaInfoService.updateBabyUmbrellaInfoById(babyUmbrellaInfo);

					String keyword2 = StringUtils.isNotNull(babyId)?"观察期":"待激活";
					String url = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=31";
					WechatMessageUtil.templateModel(title, keyword1, keyword2, "", "", remark, token, url, fromOpenId, templateId);
				}
				newBabyUmbrellaInfo.setOpenid(toOpenId);
				newBabyUmbrellaInfo.setUmberllaMoney(200000);
				babyUmbrellaInfoService.saveBabyUmbrellaInfo(newBabyUmbrellaInfo);
			}else{
				/*if(list.size()!=0){
					if("a".equals(list.get(0).get("version"))){
						tourl = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrellaa";
					}
				}*/
				if("success".equals(list1.get(0).get("pay_result"))){
					sendsucmes = true;
				}
			}
			System.out.println(sendsucmes+"sendsucmes=============sendsucmes============================");
			if(sendsucmes){
				article.setTitle("宝大夫送你一份见面礼");
				article.setDescription("恭喜您已成功领取专属于宝宝的20万高额保障金");
				article.setPicUrl("http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/protectumbrella%2Fprotectumbrella");
				//article.setUrl(tourl);
				article.setUrl("http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=31");
				articleList.add(article);
			}

			if("oldUser".equals(userType)&&!sendsucmes){//老用户扫码发送保护伞信息
				if(!"umbrellaSendWechatMessageOldUserScan".equals(CookieUtils.getCookie(request, "umbrellaSendWechatMessageOldUserScan"))){
					CookieUtils.setCookie(response, "umbrellaSendWechatMessageOldUserScan", "umbrellaSendWechatMessageOldUserScan", 3600 * 24 * 365);
					int count = babyUmbrellaInfoService.getUmbrellaCount();
					article.setTitle("宝大夫送你一份见面礼");
					article.setDescription("专属于宝宝的40万高额保障金5元即送，目前已有" + count + "位妈妈们领取，你也赶紧加入吧，运气好还能免单哦！");
					article.setPicUrl("http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/protectumbrella%2Fprotectumbrella");
					article.setUrl("http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/130000000/a");
					articleList.add(article);
				}
			}
		}else if(EventKey.indexOf("qrscene_13")>-1){
			String toOpenId = xmlEntity.getFromUserName();//扫码者openid
			Map<String, Object> param1 = new HashMap<String, Object>();
			param1.put("openid",toOpenId);
			List<Map<String,Object>> list1 = babyUmbrellaInfoService.getBabyUmbrellaInfo(param1);
			Map<String, Object> param = new HashMap<String, Object>();
			String id = EventKey.split("_")[1];
			param.put("id",id);
			List<Map<String,Object>> list = babyUmbrellaInfoService.getBabyUmbrellaInfo(param);
			boolean sendsucmes = false;
			if(list1.size()==0){//用户第一次加入保护伞
				if(list.size()!=0&&!EventKey.contains("qrscene_130000000")) {
					String fromOpenId = (String) list.get(0).get("openid");//分享者openid
					String babyId = (String) list.get(0).get("baby_id");
					Map parameter = systemService.getWechatParameter();
					String token = (String) parameter.get("token");
					int oldUmbrellaMoney = (Integer) list.get(0).get("umbrella_money");
					int newUmbrellaMoney = (Integer) list.get(0).get("umbrella_money")+20000;
					int friendJoinNum = (Integer) list.get(0).get("friendJoinNum");
					WechatAttention wa = wechatAttentionService.getAttentionByOpenId(toOpenId);
					String nickName = "";
					if (wa != null) {
						nickName = StringUtils.isNotNull(wa.getNickname()) ? wa.getNickname() : "";
					}
					String title = "恭喜您，您的好友" + nickName + "已成功加入。您既帮助了朋友，也提升了2万保障金！";
					String templateId = "b_ZMWHZ8sUa44JrAjrcjWR2yUt8yqtKtPU8NXaJEkzg";
					String keyword1 = "您已拥有" + newUmbrellaMoney / 10000 + "万的保障金，还需邀请" + (400000 - newUmbrellaMoney) / 20000 + "位好友即可获得最高40万保障金。";
					String remark = "邀请一位好友，增加2万保额，最高可享受40万保障！";
					if (oldUmbrellaMoney < 400000) {
						BabyUmbrellaInfo babyUmbrellaInfo = new BabyUmbrellaInfo();
						babyUmbrellaInfo.setId(Integer.parseInt(id));
						babyUmbrellaInfo.setUmberllaMoney(newUmbrellaMoney);
						babyUmbrellaInfoService.updateBabyUmbrellaInfoById(babyUmbrellaInfo);
					}
					if(newUmbrellaMoney>=400000){
						title = "感谢您的爱心，第"+(friendJoinNum+1)+"位好友"+nickName+"已成功加入，一次分享，一份关爱，汇聚微小力量，传递大爱精神！";
						templateId = "b_ZMWHZ8sUa44JrAjrcjWR2yUt8yqtKtPU8NXaJEkzg";
						keyword1 = "您已成功拥有40万的最高保障金。";
						remark = "您还可以继续邀请好友，传递关爱精神，让更多的家庭拥有爱的保障！";
					}
					BabyUmbrellaInfo babyUmbrellaInfo = new BabyUmbrellaInfo();
					babyUmbrellaInfo.setId(Integer.parseInt(id));
					babyUmbrellaInfo.setFriendJoinNum(friendJoinNum+1);
					babyUmbrellaInfoService.updateBabyUmbrellaInfoById(babyUmbrellaInfo);

					String keyword2 = StringUtils.isNotNull(babyId) ? "观察期" : "待激活";
					String url = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=31";
					WechatMessageUtil.templateModel(title, keyword1, keyword2, "", "", remark, token, url, fromOpenId, templateId);
				}
			}else{
				if("success".equals(list1.get(0).get("pay_result"))){
					sendsucmes = true;
				}
			}
			System.out.println(sendsucmes+"sendsucmes=============sendsucmes============================");
			if(sendsucmes){
				article.setTitle("宝大夫送你一份见面礼");
				article.setDescription("恭喜您已成功领取专属于宝宝的20万高额保障金");
				article.setPicUrl("http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/protectumbrella%2Fprotectumbrella");
				//article.setUrl("http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrellaa");
				article.setUrl("http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=31");
				articleList.add(article);
			}
			if("oldUser".equals(userType)&&!sendsucmes){//老用户扫码发送保护伞信息
				if(!"umbrellaSendWechatMessageOldUserScan".equals(CookieUtils.getCookie(request, "umbrellaSendWechatMessageOldUserScan"))){//新用户关注，推送保护伞消息
					CookieUtils.setCookie(response, "umbrellaSendWechatMessageOldUserScan", "umbrellaSendWechatMessageOldUserScan", 3600 * 24 * 365);
					int count = babyUmbrellaInfoService.getUmbrellaCount();
					article.setTitle("宝大夫送你一份见面礼");
					article.setDescription("专属于宝宝的40万高额保障金5元即送，目前已有" + count + "位妈妈们领取，你也赶紧加入吧，运气好还能免单哦！");
					article.setPicUrl("http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/protectumbrella%2Fprotectumbrella");
					article.setUrl("http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/130000000/a");
					articleList.add(article);
				}
			}
		}

		if(articleList.size() == 0){
			return "";
		}
		// 设置图文消息个数
		newsMessage.setArticleCount(articleList.size());
		// 设置图文消息包含的图文集合
		newsMessage.setArticles(articleList);
		// 将图文消息对象转换成xml字符串
		String respMessage = MessageUtil.newsMessageToXml(newsMessage);
		return respMessage;
	}

	private String processSubscribeEvent(ReceiveXmlEntity xmlEntity,HttpServletRequest request,HttpServletResponse response)
	{
		Map parameter = systemService.getWechatParameter();
//		Map userWechatParam = parameter.getWeChatParamFromRedis("user");
		String token = (String) parameter.get("token");
		String EventKey = xmlEntity.getEventKey();
		String marketer = "";
		if(StringUtils.isNotNull(EventKey)){
			marketer = EventKey.replace("qrscene_", "");
		}
		this.insertAttentionInfo(xmlEntity, token, marketer);
		return sendSubScribeMessage(xmlEntity, request,response, marketer, token);
	}

	private void insertAttentionInfo(ReceiveXmlEntity xmlEntity,String token,String marketer)
	{
		HashMap<String,Object> map = new HashMap<String, Object>();
		String openId = xmlEntity.getFromUserName();
		String id = UUID.randomUUID().toString().replaceAll("-", "");
		WechatBean wechatBean = WechatUtil.getWechatName(token, openId);

		map.put("id", id);
		map.put("status", "0");
		map.put("openId", openId);
		map.put("marketer", marketer);
		map.put("doctorMarketer", marketer);
		map.put("updateTime", new Date());
		map.put("nickname", EmojiFilter.coverEmoji(wechatBean.getNickname()));
		int attentionNum = wechatInfoDao.checkAttention(map);
		if(attentionNum > 0) {
			map.put("ispay",0);
		}
		else {
			map.put("ispay",1);
		}
		wechatInfoDao.insertAttentionInfo(map);

		if("true".equalsIgnoreCase(mongoEnabled))
		{
			WechatAttention attention = new WechatAttention();
			attention.setId(id);
			attention.setDate(new Date());
			attention.setMarketer(marketer);
			attention.setNickname(wechatBean.getNickname());
			attention.setOpenid(openId);
			attention.setStatus("0");
			mongoDBService.insert(attention);

			MongoLog mongoLog = new MongoLog();
			mongoLog.setId(IdGen.uuid());
			mongoLog.setCreate_by(UserUtils.getUser().getId());
			mongoLog.setCreate_date(new Date());
			mongoLog.setTitle("00000001");//微信宝大夫用户版公众平台关注
			mongoLog.setIsPay(map.get("ispay").toString());
			mongoLog.setNickname((String) map.get("nickname"));
			mongoLog.setOpen_id(openId);
			mongoLog.setMarketer(marketer);
			mongoLog.setStatus((String) map.get("status"));
			mongoLogService.insert(mongoLog);
		}
	}

	private void updateAttentionInfo(ReceiveXmlEntity xmlEntity)
	{
		String EventKey = xmlEntity.getEventKey();
		Date updateDate = new Date();
		String openId = xmlEntity.getFromUserName();
		String marketer = EventKey.replace("qrscene_", "");
		HashMap<String,Object> updateTimeMap = new HashMap<String, Object>();
		updateTimeMap.put("openId",openId);
		updateTimeMap.put("updateTime", updateDate);
		updateTimeMap.put("doctorMarketer", marketer);
		//new zdl
		wechatInfoDao.updateAttentionInfo(updateTimeMap);
	}

	private String sendSubScribeMessage(ReceiveXmlEntity xmlEntity,HttpServletRequest request,HttpServletResponse response,String marketer,String token)
	{
		HttpSession session = request.getSession();
		session.setAttribute("openId", xmlEntity.getFromUserName());
		LogUtils.saveLog(request, "00000001");//注：参数含义请参照sys_log_mapping表，如00000001表示“微信宝大夫用户版公众平台关注”
		String EventKey = xmlEntity.getEventKey();
		if(EventKey.indexOf("xuanjianghuodong_zhengyuqiao_saoma")<=-1&&EventKey.indexOf("baoxian_000001")<=-1)
		{
			List<Article> articleList = new ArrayList<Article>();
			Article article = new Article();
			article.setTitle("宝大夫送你一份见面礼");
			article.setDescription("");
			article.setPicUrl("http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/menu/%E5%AE%9D%E6%8A%A4%E4%BC%9Ebanner2%20-%20%E5%89%AF%E6%9C%AC%20%E6%8B%B7%E8%B4%9D.png");
			article.setUrl("http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/130000000/a");
			articleList.add(article);

			article = new Article();
			article.setTitle("咨询大夫\n一分钟极速回复，7×24全年无休");
			article.setDescription("三甲医院医生7X24全年无休   一分钟极速回复");
			article.setPicUrl("http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/menu/%E5%92%A8%E8%AF%A2%E5%A4%A7%E5%A4%AB.png");
			article.setUrl("https://mp.weixin.qq.com/s?__biz=MzI2MDAxOTY3OQ==&mid=504236660&idx=1&sn=10d923526047a5276dd9452b7ed1e302&scene=1&srcid=0612OCo7d5ASBoGRr2TDgjfR&key=f5c31ae61525f82ed83c573369e70b8f9b853c238066190fb5eb7b8640946e0a090bbdb47e79b6d2e57b615c44bd82c5&ascene=0&uin=MzM2NjEyMzM1&devicetype=iMac+MacBookPro11%2C4+OSX+OSX+10.11.4+build(15E65)&version=11020201&pass_ticket=dG5W6eOP3JU1%2Fo3JXw19SFBAh1DgpSlQrAXTyirZuj970HMU7TYojM4D%2B2LdJI9n");
			articleList.add(article);

			article = new Article();
			article.setTitle("名医面诊\n线上轻松预约，线下准时专家面诊");
			article.setDescription("三甲医院儿科专家，线上准时预约，线下准时就诊");
			article.setPicUrl("http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/menu/%E5%90%8D%E5%8C%BB%E9%9D%A2%E8%AF%8A.png");
			article.setUrl("http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=2");
			articleList.add(article);

//			article = new Article();
//			article.setTitle("名医电话|通话10分钟，宝宝康复指导全Get");
//			article.setDescription("与权威儿科专家通话10分钟，个性化就诊和康复指导");
//			article.setPicUrl("http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/menu/%E5%90%8D%E5%8C%BB%E7%94%B5%E8%AF%9D.png");
//			article.setUrl("http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=28");
//			articleList.add(article);

			article = new Article();
			article.setTitle("妈妈社群\n育儿交流找组织，客服微信：bdfdxb");
			article.setDescription("添加宝大夫客服微信：bdfdxb，加入宝大夫家长群，与众多宝妈一起交流分享，参与更多好玩儿的活动");
			article.setPicUrl("http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/menu/%E5%A6%88%E5%A6%88%E6%B4%BB%E5%8A%A8.png");
			article.setUrl("https://mp.weixin.qq.com/s?__biz=MzI2MDAxOTY3OQ==&mid=504236661&idx=3&sn=4c1fd3ee4eb99e6aca415f60dceb6834&scene=1&srcid=0616uPcrUKz7FVGgrmOcZqqq&from=singlemessage&isappinstalled=0&key=18e81ac7415f67c44d3973b3eb8e53f264f47c1109eceefa8d6be994349fa7f152bb8cfdfab15b36bd16a4400cd1bd87&ascene=0&uin=MzM2NjEyMzM1&devicetype=iMac+MacBookPro11%2C4+OSX+OSX+10.11.4+build(15E65)&version=11020201&pass_ticket=ZgGIH5%2B8%2FkhHiHeeRG9v6qbPZmK5qPlBL02k0Qo%2FHCK7eLMOZexAypBy0dzPjzaZ");
			articleList.add(article);

			WechatUtil.senImgMsgToWechat(token,xmlEntity.getFromUserName(),articleList);

//			String st = "欢迎加入宝大夫，让您从此育儿不再愁！"+WechatUtil.emoji(0x1f339)+"\n\n"
//					+"【免费咨询】直接咨询北京三甲医院儿科专家，一分钟内回复！\n" +
//					"【预约专家】急速预约北京儿科专家出诊时间，不用排队挂号！\n"+
//					"【妈妈活动】添加宝大夫客服微信：bdfdxb，加入宝大夫家长群，与众多宝妈一起交流分享，参与更多好玩儿的活动！\n\n"+
//					"如需人工协助预约儿科专家,请您拨打：400-623-7120。";
//			WechatUtil.sendMsgToWechat(token, xmlEntity.getFromUserName(), st);
		}

		return processScanEvent(xmlEntity,"newUser",request,response);
	}

	private void processUnSubscribeEvent(ReceiveXmlEntity xmlEntity,HttpServletRequest request)
	{
		// TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
		HttpSession session = request.getSession();
		session.setAttribute("openId", xmlEntity.getFromUserName());
		LogUtils.saveLog(request, "00000002");//注：参数含义请参照sys_log_mapping表，如00000002表示“微信宝大夫用户版公众平台取消关注”
		HashMap<String,Object> map = new HashMap<String, Object>();
		String openId = xmlEntity.getFromUserName();
		map.put("openId", openId);
		//根据openid查询最近关注的marketer，防止取消关注的时候marketer总是为空
		WechatAttention wechatAttention = new WechatAttention();
		wechatAttention.setOpenid(openId);
		wechatAttention = wechatAttentionDao.findMarketerByOpeinid(wechatAttention);
		map.put("marketer", wechatAttention.getMarketer());
		String id = UUID.randomUUID().toString().replaceAll("-", "");
		map.put("id", id);
		map.put("status", "1");
		wechatInfoDao.insertAttentionInfo(map);

		if("true".equalsIgnoreCase(mongoEnabled))
		{
			WechatAttention attention = new WechatAttention();
			attention.setId(id);
			attention.setDate(new Date());
			attention.setMarketer(wechatAttention.getMarketer());
			attention.setOpenid(openId);
			attention.setStatus("1");
			mongoDBService.insert(attention);

			MongoLog mongoLog = new MongoLog();
			mongoLog.setId(IdGen.uuid());
			mongoLog.setCreate_by(UserUtils.getUser().getId());
			mongoLog.setTitle("00000002");//微信宝大夫用户版公众平台取消关注
			mongoLog.setOpen_id((String) map.get("openId"));
			mongoLog.setStatus((String) map.get("status"));
			mongoLogService.insert(mongoLog);
		}
	}

	private String processClickMenuEvent(ReceiveXmlEntity xmlEntity,HttpServletRequest request,HttpServletResponse response)
	{
		// TODO 自定义菜单
		String respMessage = "";
		if("39".equals(xmlEntity.getEventKey())){
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(xmlEntity.getFromUserName());
			textMessage.setFromUserName(xmlEntity.getToUserName());
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);
			String st = "您好，我是宝大夫专职医护，愿为您提供医学服务，协助预约儿科专家，请您拨打：400-623-7120。";
			textMessage.setContent(st);
			respMessage = MessageUtil.textMessageToXml(textMessage);
		}
		else if("38".equals(xmlEntity.getEventKey()))
		{
			HttpSession session = request.getSession();
			session .setAttribute("openId",xmlEntity.getFromUserName());
			LogUtils.saveLog(request,"00000003");//注：参数含义请参照sys_log_mapping表，如00000003表示“咨询医生消息推送”
//			TextMessage textMessage = new TextMessage();
//			textMessage.setToUserName(xmlEntity.getFromUserName());
//			textMessage.setFromUserName(xmlEntity.getToUserName());
//			textMessage.setCreateTime(new Date().getTime());
//			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
//			textMessage.setFuncFlag(0);
//			textMessage.setContent("1、点击左下角“小键盘”输入文字或语音,即可咨询疾病或保健问题\t\t\n 2、免费在线咨询时间:\n小儿内科:   24小时全天\n小儿皮肤科:   9:00~22:00\n营养保健科:   9:00~22:00\n小儿其他专科:(外科、眼科、耳鼻喉科、口腔科、预防保健科、中医科)   19:00~21:00 \n妇产科   19:00~22:00");

//			respMessage = MessageUtil.textMessageToXml(textMessage);
			Map parameter = systemService.getWechatParameter();
			String token = (String) parameter.get("token");
			List<Article> articleList = new ArrayList<Article>();
			Article article = new Article();
			article.setTitle("咨询大夫 | 三甲医院儿科专家  1分钟极速回复");
			article.setDescription("小儿内科:       24小时全天 \n\n小儿皮肤科:   9:00 ~ 22:00\n\n小儿营养科:   9:00 ~ 22:00\n\n(外科、眼科、耳鼻喉科、口腔科、预防保健科、中医科)\n\n点击查看更多哦");
			article.setPicUrl("http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/menu/%E6%8E%A8%E9%80%81%E6%B6%88%E6%81%AF2.png");
			article.setUrl("https://mp.weixin.qq.com/s?__biz=MzI2MDAxOTY3OQ==&mid=504236660&idx=1&sn=10d923526047a5276dd9452b7ed1e302&scene=1&srcid=0612OCo7d5ASBoGRr2TDgjfR&key=f5c31ae61525f82ed83c573369e70b8f9b853c238066190fb5eb7b8640946e0a090bbdb47e79b6d2e57b615c44bd82c5&ascene=0&uin=MzM2NjEyMzM1&devicetype=iMac+MacBookPro11%2C4+OSX+OSX+10.11.4+build(15E65)&version=11020201&pass_ticket=dG5W6eOP3JU1%2Fo3JXw19SFBAh1DgpSlQrAXTyirZuj970HMU7TYojM4D%2B2LdJI9n");
			articleList.add(article);
			WechatUtil.senImgMsgToWechat(token,xmlEntity.getFromUserName(),articleList);
			memberService.sendExtendOldMemberWechatMessage(xmlEntity.getFromUserName());
		}else if("36".equals(xmlEntity.getEventKey()))
		{
			List<Article> articleList = new ArrayList<Article>();
			// 创建图文消息
			NewsMessage newsMessage = new NewsMessage();
			newsMessage.setToUserName(xmlEntity.getFromUserName());
			newsMessage.setFromUserName(xmlEntity.getToUserName());
			newsMessage.setCreateTime(new Date().getTime());
			newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
			newsMessage.setFuncFlag(0);
			Article article = new Article();
			article.setTitle("咨询大夫，请关注最新公众账号!");
			article.setDescription("公告：本公众号将于2015年8月9日起停止服务。需要咨询大夫，" +
					"请妈妈们识别二维码关注最新的公众号（微信号：BaodfWX）。专业的大夫在线等您来咨询哦！");
			article.setPicUrl("http://mmbiz.qpic.cn/mmbiz/dGa0GvlZMicotZvyd4ZkHKjYe3ERsP5OD0spQbFz1CPTd" +
					"qqWjrP1s5pr4BpDxvM97NgYNm4PiazfHv37t6kbP9dw/640?wx_fmt=jpeg&wxfrom=5");
			article.setUrl("http://mp.weixin.qq.com/s?__biz=MzI0MjAwNjY0Nw==&mid=208340985&idx=1&sn=2beb0d78fc9097f10d073e182f1cb6c1&scene=0#rd");
			articleList.add(article);
			// 设置图文消息个数
			newsMessage.setArticleCount(articleList.size());
			// 设置图文消息包含的图文集合
			newsMessage.setArticles(articleList);
			// 将图文消息对象转换成xml字符串
			respMessage = MessageUtil.newsMessageToXml(newsMessage);
		}

		return respMessage;
	}

	private String processCloseEvent(ReceiveXmlEntity xmlEntity,HttpServletRequest request,HttpServletResponse response)
	{
		String respMessage = null;
		String openId=xmlEntity.getFromUserName();
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("openid", openId);
		 params.put("uuid", UUID.randomUUID().toString().replaceAll("-", ""));
		 params.put("starNum1", 0);
		 params.put("starNum2", 0);
		 params.put("starNum3", 0);
		 params.put("doctorId", xmlEntity.getKfAccount());
		 params.put("content", "");
		 params.put("dissatisfied", null);
		 params.put("redPacket", null);
		 patientRegisterPraiseDao.saveCustomerEvaluation(params);
		 String st = "感谢您对我们的信任与支持，为了以后能更好的为您服务，请对本次服务做出评价！【" +
			"<a href='http://s68.baodf.com/titan/appoint#/userEvaluate/"+params.get("uuid")+"'>我要评价</a>】";
		 Map parameter = systemService.getWechatParameter();
		 String token = (String)parameter.get("token");
		 WechatUtil.sendMsgToWechat(token, openId, st);
		 LogUtils.saveLog(request, "00000004");//注：00000004表示“客服评价”

		/*if(!"umbrellaSendWechatMessageCloseConsult".equals(CookieUtils.getCookie(request, "umbrellaSendWechatMessageCloseConsult"))){//关闭咨询，推送保护伞消息
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("openid", "zixunguanbi");
			List<Map<String,Object>> list = babyUmbrellaInfoService.getBabyUmbrellaInfo(param);
			if(list.size()==0||!"success".equals(list.get(0).get("pay_result"))){
				int count = babyUmbrellaInfoService.getUmbrellaCount();
				String title = "小病问医生，大病有互助";
				String description = "感谢您对宝大夫的信任，现在宝大夫联合中国儿童少年基金会，共同推出家庭重疾40万高额保障互助计划，目前已有" + count + "位妈妈加入，现在就等你了，赶紧加入吧！";
				//String url = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrellab";
				String url = "http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/130000000/a";
				String picUrl = "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/protectumbrella%2Fprotectumbrella";
				String message = "{\"touser\":\""+openId+"\",\"msgtype\":\"news\",\"news\":{\"articles\": [{\"title\":\""+ title +"\",\"description\":\""+description+"\",\"url\":\""+ url +"\",\"picurl\":\""+picUrl+"\"}]}}";

				String jsonobj = HttpRequestUtil.httpsRequest("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" +
						token + "", "POST", message);
				System.out.println(jsonobj+"===============================");
			}
		}*/
		return respMessage;
	}

	private void processGetLocationEvent(ReceiveXmlEntity xmlEntity,HttpServletRequest request)
	{
		LogUtils.saveLog(request, "00000005");//00000005：获取地理位置信息
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("id",UUID.randomUUID().toString().replaceAll("-", ""));
		map.put("latitude",xmlEntity.getLatitude());
		map.put("precision",xmlEntity.getPrecision());
		map.put("createTime",xmlEntity.getCreateTime());
		map.put("openid",xmlEntity.getFromUserName());
		map.put("longitude", xmlEntity.getLongitude());
		wechatInfoDao.insertCustomerLocation(map);
	}

	private String transferToCustomer(ReceiveXmlEntity xmlEntity)
	{
		ConsultSession vo = new ConsultSession();
		vo.setUpdateTime(new Date());
		vo.setStatus("0");
		vo.setUserId(xmlEntity.getFromUserName());
		vo.setCreateTime(new Date());
		String title = UUID.randomUUID().toString().replaceAll("-", "");
		vo.setTitle(title);
		vo.setCsUserId("110@BaodfWX");
		consultConversationService.saveConsultInfo(vo);
//
//		if(xmlEntity.getContent().equals("营养")){
//			String st = "加入宝大夫营养管理（仅限1-3岁），科学喂养让宝宝成长更加健康！\n\n" +
//					"点击直接进入：<a href='http://s22.baodf.com/xiaoerke-wxapp/wechatInfo/fieldwork/wechat/" +
//					"author?url=http://s22.baodf.com/xiaoerke-wxapp/wechatInfo/getUserWechatMenId?url=10'>营养管理(戳此链接)</a>" +
//					"\n" +
//					"微信入口：宝大夫—宝粉之家—健康管理—营养管理";
//			Map parameter = systemService.getWechatParameter();
//			String token = (String)parameter.get("token");
//			WechatUtil.senImgMsgToWechat(token, xmlEntity.getFromUserName(), "营养报告 | 科学喂养的重要性", "这里面的不仅仅是答案。",
//					"http://mp.weixin.qq.com/s?__biz=MzI2MDAxOTY3OQ==&mid=403036004&idx=1&sn=db524245950080a097b0574927c97001" +
//							"&scene=0&previewkey=lQ2vTkDCu67xf5wIiSxoGJ1iJUUG%2F7eLf7OA%2FVEtaJE%3D#wechat_redirect",
//					"http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/MessageImage%2F7.jpg");
//			WechatUtil.sendMsgToWechat(token, xmlEntity.getFromUserName(), st);
//			Log log=new Log();
//			log.setTitle("YYHD-YY");
//			log.setCreateDate(new Date());
//			log.setUpdateDate(new Date());
//			log.setOpenId(xmlEntity.getFromUserName());
//			LogUtils.saveLogVo(log);
//			return "";
//		}
//		if(xmlEntity.getContent().equals("安全")){
//			String st = "恭喜您，参与抽奖成功。抽奖结果请关注4月5号微信文章。" +
//					"\n\n" +
//					"更重要的是：" +
//					"\n" +
//					"关注宝大夫即可直接免费咨询北京三甲医院的儿科专家，1分钟内极速回复。就像微信聊天一样简单！\n" +
//					"\n" +
//					"点击下方小键盘，赶紧发送您的健康疑问吧！";
//			Map parameter = systemService.getWechatParameter();
//			String token = (String)parameter.get("token");
//			WechatUtil.sendMsgToWechat(token, xmlEntity.getFromUserName(), st);
//			Log log=new Log();
//			log.setTitle("YYHD-AQ");
//			log.setCreateDate(new Date());
//			log.setUpdateDate(new Date());
//			log.setOpenId(xmlEntity.getFromUserName());
//			LogUtils.saveLogVo(log);
//			return "";
//		}
		//增加一个判断用户宝宝信息的接口,如果没有宝宝信息则推送消息
//		List<BabyBaseInfoVo> babyList = healthRecordsService.selectUserBabyInfo(xmlEntity.getFromUserName());
//		boolean healthRecordMsgAlreadySend = false;
//		try{
//		  healthRecordMsgAlreadySend = this.findHealthRecordMsgByOpenid(xmlEntity.getFromUserName(),"BB_MSG");
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//
//		if(babyList.size()==0 && !healthRecordMsgAlreadySend){
//			String st = "在正式开始咨询前，医生需要了解一些宝宝的简单信息，以便更快速精准和细致地为宝宝服务！\n" +
//					"---------------------\n"+
//					"<a href='http://s22.baodf.com/xiaoerke-wxapp/wechatInfo/fieldwork/wechat/author?url" +
//					"=http://s22.baodf.com/xiaoerke-wxapp/wechatInfo/getUserWechatMenId?url=24'>点击这里补充宝宝信息</a>";
//			Map parameter = systemService.getWechatParameter();
//			String token = (String)parameter.get("token");
//			WechatUtil.sendMsgToWechat(token, xmlEntity.getFromUserName(), st);
//			HealthRecordMsgVo healthRecordMsgVo = new HealthRecordMsgVo();
//			healthRecordMsgVo.setOpenId(xmlEntity.getFromUserName());
//			healthRecordMsgVo.setType("BB_MSG");
//			insertHealthRecordMsg(healthRecordMsgVo);
//		}


			return "<xml><ToUserName><![CDATA[" + xmlEntity.getFromUserName()  +
					"]]></ToUserName><FromUserName><![CDATA[" + xmlEntity.getToUserName() +
					"]]></FromUserName><CreateTime><![CDATA[" + new Date().getTime() +
					"]]></CreateTime><MsgType><![CDATA[transfer_customer_service]]></MsgType>" +
					"</xml>";

	}

    /***
     * 根据openid查询消息发送记录
     * @param openid type
     * @return 有记录 true 无记录 false
     */
	@Override
    public boolean findHealthRecordMsgByOpenid(String openid, String type) {
        HealthRecordMsgVo healthRecordMsgVo = new HealthRecordMsgVo();
        healthRecordMsgVo.setOpenId(openid);
        Query queryRecord = new Query();
        Date date = new Date();
        date.setHours(0);
        queryRecord.addCriteria(Criteria.where("open_id").is(openid).and("create_date").gte(date));
        Long RecordCount = healthRecordMsgVoMongoDBService.queryCount(queryRecord);
        return RecordCount != 0l ? true : false;
    }

    /***
     * 插入消息发送记录
     *
     * @param
     * @return
     */
	@Override
    public int insertHealthRecordMsg(HealthRecordMsgVo healthRecordMsgVo) {
		healthRecordMsgVo.setId(IdGen.uuid());
        healthRecordMsgVo.setCreate_date(new Date());
        healthRecordMsgVo.setOpen_id(healthRecordMsgVo.getOpenId());
		healthRecordMsgVo.setOpenId(null);
        return  healthRecordMsgVoMongoDBService.insert(healthRecordMsgVo);
    }
}
