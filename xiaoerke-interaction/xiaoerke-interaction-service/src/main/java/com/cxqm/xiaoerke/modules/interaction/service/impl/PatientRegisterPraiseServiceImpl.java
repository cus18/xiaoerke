package com.cxqm.xiaoerke.modules.interaction.service.impl;


import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.modules.interaction.dao.PatientRegisterPraiseDao;
import com.cxqm.xiaoerke.modules.interaction.entity.PatientRegisterPraise;
import com.cxqm.xiaoerke.modules.interaction.entity.PraiseVo;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.order.service.ConsultPhoneOrderService;
import com.cxqm.xiaoerke.modules.order.service.PatientRegisterService;
import com.cxqm.xiaoerke.modules.order.service.SysConsultPhoneService;
import com.cxqm.xiaoerke.modules.sys.entity.MongoLog;
import com.cxqm.xiaoerke.modules.sys.interceptor.SystemServiceLog;
import com.cxqm.xiaoerke.modules.sys.service.*;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.sys.utils.PatientMsgTemplate;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangbaowei on 15/11/5.
 */
@Service
@Transactional(readOnly = false)
public class PatientRegisterPraiseServiceImpl implements PatientRegisterPraiseService {

	@Autowired
	private DoctorInfoService doctorInfoService;

	@Autowired
	private PatientRegisterService patientRegisterService;

	@Autowired
	private PatientRegisterPraiseDao patientRegisterPraiseDao;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private DoctorCaseService doctorCaseService;

	@Autowired
	private DoctorLocationService doctorLocationService;

    @Autowired
    private SystemService systemService;

    @Autowired
    SysConsultPhoneService sysConsultPhoneService;

    @Autowired
    ConsultPhoneOrderService consultPhoneOrderService;

	@Autowired
	private MongoDBService<MongoLog> mongoDBServiceLog;


    //将取消原因插入到patient_register_praise表中
	@Override
    public void insertCancelReason(HashMap<String, Object> executeMap) {
    	patientRegisterPraiseDao.insertCancelReason(executeMap);

    }

    @Override
    public void getUserEvaluate(Map<String, Object> params,HashMap<String, Object> response)
    {
        response.put("userEvaluate", patientRegisterPraiseDao.getUserEvaluate(params));
    }

    @Override
    public Integer getTotalCount(HashMap<String, Object> params) {
        return patientRegisterPraiseDao.getTotalCount(params);
    }

    @Override
    public HashMap<String, Object> getDoctorEvaluateTop(HashMap<String, Object> params) {
        HashMap<String,Object> response = new HashMap<String, Object>();
        List<HashMap<String,Object>> resultList = patientRegisterPraiseDao.getDoctorEvaluateTop(params);
        if(resultList != null && resultList.size() > 0){
            for(HashMap<String,Object> map:resultList){
            Date date = (Date) map.get("date");
            String week = DateUtils.getWeekOfDate(date);
            SimpleDateFormat format = new SimpleDateFormat("MM/dd");
            SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
            map.put("date", format.format(date) + "(" + week.replaceAll("星期", "周") + ")" + format1.format(date));
			String wechat_name = map.get("wechat_name") == null?"微信用户":(String)map.get("wechat_name");
			String pic_url = map.get("pic_url") == null?"images/user_photo.png":(String)map.get("pic_url");
			map.put("wechat_name",wechat_name);
			map.put("pic_url",pic_url);
			}
        }
        response.put("evaluateList",resultList);
        return response;
    }

    @Override
    public HashMap<String, Object> getDoctorEvaluate(HashMap<String, Object> params)
    {
        HashMap<String, Object> response = new HashMap<String, Object>();

        HashMap<String, Object> dataMap = new HashMap<String, Object>();
        String doctorId = (String) params.get("doctorId");
        String evaluateType = (String) params.get("evaluateType");
        dataMap.put("doctorId",doctorId);
        dataMap.put("evaluateType",evaluateType);

        String currentPage = (String) params.get("pageNo");
        String pageSize = String.valueOf(params.get("pageSize"));
        Page<PatientRegisterPraise> page = FrontUtils.generatorPage(currentPage, pageSize);

        Page<PatientRegisterPraise> resultPage = patientRegisterPraiseDao.getDoctorEvaluate(dataMap, page);

        long tmp = FrontUtils.generatorTotalPage(resultPage);
        response.put("pageTotal", tmp + "");
        response.put("pageNo", resultPage.getPageNo());
        response.put("pageSize", resultPage.getPageSize());

        List<HashMap<String, Object>> evaluateList = new ArrayList<HashMap<String, Object>>();
        List<PatientRegisterPraise> list = resultPage.getList();
        if(list != null && !list.isEmpty()) {
            for (PatientRegisterPraise praise : list) {
                HashMap<String, Object> hmap = new HashMap<String, Object>();
                hmap.put("phone", praise.getPhone());
                hmap.put("impression", praise.getImpression());
                hmap.put("star", praise.getStar());
                hmap.put("majorStar", praise.getMajorStar());
				hmap.put("pic_url", praise.getPicUrl()==null?"images/user_photo.png":praise.getPicUrl());
				hmap.put("wechat_name", praise.getWechatName()==null?"微信用户": praise.getWechatName());
                Date date = praise.getPraiseDate();
                String week = DateUtils.getWeekOfDate(date);
                SimpleDateFormat format = new SimpleDateFormat("MM/dd");
                SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
                hmap.put("date", format.format(date)+"("+week.replaceAll("星期","周")+")"+format1.format(date));
                hmap.put("dateTime",date.getTime());
                evaluateList.add(hmap);
            }
        }
        response.put("evaluateList",evaluateList);
        return response;
    }

	@Override
    public void saveCustomerEvaluation(Map<String, Object> params) {
//		params.put("evaluateSource", "realtimeConsult");
    	patientRegisterPraiseDao.saveCustomerEvaluation(params);
    }

    public void saveQuestionnaireSurvey(List<HashMap<String, Object>> li) {
    	patientRegisterPraiseDao.saveQuestionnaireSurvey(li);
    }

    public HashMap<String, Object> findDoctorScoreInfo(String sys_doctor_id){
        return doctorInfoService.findDoctorScoreInfo(sys_doctor_id);
    }

    /**
     * 查询被评价医生的所有等待时间（根据sys_doctor_id）
     *
     * @param PraiseVo
     * @return List<PraiseVo>
     */
    @Override
    public List<HashMap<String, Object>> findAllPraiseByDoctorId(PraiseVo PraiseVo) {
        List<HashMap<String, Object>> resultList = patientRegisterPraiseDao.findAllPraiseByDoctorId(PraiseVo);
        return resultList;
    }


	@Override
    public String customerEvaluation(Map<String, Object> params,String openId)
    {
        params.put("openid", openId);
		System.out.print("----------------分享----------------");
        params.put("uuid", UUID.randomUUID().toString().replaceAll("-", ""));
		params.put("evaluateSource", "realtimeConsult");
        try {
        	patientRegisterPraiseDao.saveCustomerEvaluation(params);;
        	String st = "您已评价成功，为了让更多的宝爸宝妈体验到我们真诚的服务，您可将此次体验分享给您的朋友。【" +
                    "<a href='http://s68.baodf.com/titan/appoint#/consultShare'>我要分享</a>】";
        Map<String,Object> parameter = systemService.getWechatParameter();
        String token = (String)parameter.get("token");
        WechatUtil.sendMsgToWechat(token,openId,st);
            return "true";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "false";
    }

    @Override
	@SystemServiceLog(description = "00000012")//用户评价医生
	public Map<String, Object> orderPraiseOperation(Map<String, Object> params, HttpSession session, HttpServletRequest request)
	{
        String type = (String)params.get("type");//phone:电话咨询
		String path = request.getLocalAddr() + request.getContextPath();
		HashMap<String, Object> action = (HashMap<String, Object>) params.get("action");
		String openId = WechatUtil.getOpenId(session, request);
		params.put("openId",openId);
		Map<String,Object> parameter = systemService.getWechatParameter();
		String token = (String)parameter.get("token");
		String strURL="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+token+"&openid="+openId+"&lang=zh_CN";
		String param="";
		String json=WechatUtil.post(strURL, param, "GET");
		JSONObject jasonObject = JSONObject.fromObject(json);
		Map<String, Object> jsonMap = (Map) jasonObject;

		String patientRegisterServiceId = (String) params.get("patient_register_service_id");
		if (patientRegisterServiceId == null || "".equals(patientRegisterServiceId)) {
			patientRegisterServiceId = IdGen.uuid();
		}
		HashMap<String, Object> response = new HashMap<String, Object>();
		HashMap<String, Object> excuteMap = new HashMap<String, Object>();

		String headimgurl = null == (String) jsonMap.get("headimgurl")?"images/user_photo.png":(String) jsonMap.get("headimgurl") ;
		String nickname = null == (String) jsonMap.get("nickname")?"微信用户":(String) jsonMap.get("nickname") ;

		excuteMap.put("wechat_name", nickname);
		excuteMap.put("pic_url",headimgurl);

		//返还用户所提交的保证金
		Map<String, Object> executeMap = new HashMap<String, Object>();
		executeMap.put("patientRegisterService", patientRegisterServiceId);
		String doctorCaseId = (String)action.get("doctorCaseId");
		if (StringUtils.isNotNull(doctorCaseId)) {
			excuteMap.put("doctorCaseId", doctorCaseId);
		}
		//进行评价
		praiseHandle(type, action, patientRegisterServiceId, excuteMap);
		//分享消息推送
		HashMap<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("id", patientRegisterServiceId);

        if(type == null){
            HashMap<String, Object> resultMap  = patientRegisterService.getOrderInfoById(searchMap);
            PatientMsgTemplate.shareRemind2Wechat(openId,token,(String)resultMap.get("babyName"),(String)resultMap.get("doctorName") ,"http://s68.baodf.com/titan/appoint#/sharedDetail/"+patientRegisterServiceId+",false,");
        }

		response.put("patient_register_service_id", patientRegisterServiceId);
		response.put("status", '1');
		return response;
	}

    private void praiseHandle(String type, HashMap<String, Object> action, String patientRegisterServiceId, HashMap<String, Object> excuteMap) {
		String userId = UserUtils.getUser().getId();
		HashMap<String, Object> perInfo = new HashMap<String, Object>();
		if (userId != null && !"".equals(userId)) {
			perInfo = userInfoService.findPersonDetailInfoByUserId(userId);
		}
		excuteMap.put("id", patientRegisterServiceId);
        excuteMap.put("state","3");//待分享（已评价）

		excuteMap.put("patientRegisterPraiseId", IdGen.uuid());
        HashMap<String, Object> hashMap = null;

        //根据patientRegisterServiceId查询sys_register_service表信息
        if(type != null && "phone".equals(type)){//phone
            excuteMap.put("evaluateType","1");
            hashMap = sysConsultPhoneService.findSysConsultPhoneServiceByCRSIdExecute(excuteMap);
        }else{
            hashMap = patientRegisterService.findSysRegisterServiceByPRSIdExecute(excuteMap);
        }

		excuteMap.put("star", 1 + Integer.parseInt((String) action.get("star")));
		excuteMap.put("major_star", 1 + Integer.parseInt((String) action.get("major_star")));
		excuteMap.put("appraise", action.get("appraise"));
		excuteMap.put("symptom", action.get("symptom"));
		excuteMap.put("sys_doctor_id", hashMap.get("sys_doctor_id"));
		excuteMap.put("impression", action.get("impression"));
		excuteMap.put("zan", action.get("zan"));
		excuteMap.put("phone", perInfo.get("phone"));
		excuteMap.put("praise_date", new Date());
		excuteMap.put("patientId", UserUtils.getUser().getId());
		excuteMap.put("visit_endTime", action.get("visit_endTime"));

		excuteMap.put("sys_patient_id", hashMap.get("sys_patient_id"));
		excuteMap.put("sys_register_service_id", hashMap.get("sys_register_service_id"));

        //更新订单状态为已评价
        if(type != null && "phone".equals(type)){//phone
            consultPhoneOrderService.changeConsultPhoneRegisterServiceState(excuteMap);
        }else{
            patientRegisterService.PatientRegisterServiceIsService(excuteMap);
        }

        //添加一条评价记录
		patientRegisterPraiseDao.PatientRegisterServiceIsPraise(excuteMap);

		//---保存案例---
		String otherCase = (String)action.get("otherCase");//其他案例
		if(StringUtils.isNotNull(otherCase)){
			excuteMap.put("symptom", action.get("otherCase"));
		}

		excuteMap.put("display_status", "1");
		String doctorCaseId = (String)excuteMap.get("doctorCaseId");
		if( StringUtils.isNotNull(doctorCaseId) ){//案例不为空 原有案例数 +1
			doctorCaseService.updateDoctorCaseInfo(excuteMap);
		}else{//为空，插入新的案例
			excuteMap.put("doctorCaseNumber",null);
			doctorCaseService.saveDoctorCaseInfo(excuteMap);
		}

        if (type == null && StringUtils.isNotNull((String) action.get("visit_endTime"))) {
            //计算平均等待时间
            calculationWaitTime(patientRegisterServiceId, hashMap);
        }

	}

	private void calculationWaitTime(String patientRegisterServiceId, HashMap<String, Object> hashMap) {
		String visit_endTime = "";

		//根据patientRegisterPraiseId查询用户的就诊地址 location_id
		String location_id = doctorLocationService.findPatientLocationId(patientRegisterServiceId);
		PraiseVo PraiseVo = new PraiseVo();
		PraiseVo.setSys_doctor_id((String) hashMap.get("sys_doctor_id"));
		PraiseVo.setLocation_id(location_id);

		//查询被评价医生当前就诊地址的所有等待时间（根据sys_doctor_id，location_id）
		List<HashMap<String, Object>> voList = this.findAllPraiseByDoctorId(PraiseVo);
		int sumTime = 0;//单位分钟
		for (HashMap map : voList) {
			int waiteTime = 0;
			if(null !=map.get("wait_time")){
				BigDecimal bigDecimal = StringUtils.getBigDecimal(map.get("wait_time"));
				waiteTime = bigDecimal.intValue();
			}
			sumTime = sumTime + waiteTime;
		}
		//该医生该地点的平均等待时间=该医生在当前就诊地点的所有等待时间相加/被评价次数
		int waitTime = sumTime / voList.size();

		//更新医生界面显示的平均等待时间（x<=0 平均等待时间为0 ； 0<=x<=60 平均等待时间为x分钟  ；x>60 平均等待时间为 60 分钟）
		if (waitTime <= 0) {
			visit_endTime = "0";
		} else if (waitTime > 60) {
			visit_endTime = "60";
		} else {
			visit_endTime = String.valueOf(waitTime);
		}
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		updateMap.put("waitTime", String.valueOf(waitTime));
		updateMap.put("visit_endTime", String.valueOf(visit_endTime));
		updateMap.put("location_id", location_id);
		//更新平均等待时间数据
		doctorLocationService.updateWaiteTime(updateMap);
	}

	@Override
	public List<PraiseVo> findDoctorDetailPraiseInfo(PraiseVo praisevo) {
		return patientRegisterPraiseDao.findDoctorDetailPraiseInfo(praisevo);
	}

	@Override
	public Integer updateCustomerEvaluation(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return patientRegisterPraiseDao.updateCustomerEvaluation(params);
	}

	@Override
	public Map<String, Object> selectCustomerEvaluation(String id) {
		// TODO Auto-generated method stub
		return patientRegisterPraiseDao.selectCustomerEvaluation(id);
	}

	@Override
	public Map<String, Object> getCustomerStarInfoById(String id) {
		// TODO Auto-generated method stub
		return patientRegisterPraiseDao.getCustomerStarInfoById(id);
	}

	@Override
	public Map<String, Object> getCustomerStarSingById( String id) {
		return patientRegisterPraiseDao.getCustomerStarSingById(id);
	}

	@Override
	public Map<String, Object> getCustomerStarCountById( String id) {
		return patientRegisterPraiseDao.getCustomerStarCountById(id);
	}

	@Override
	public Map<String, Object> getDoctorHeadImageURIById(String id) {
		// TODO Auto-generated method stub
		return patientRegisterPraiseDao.getDoctorHeadImageURIById(id);
	}

	//根据条件查询评价信息sunxiao
	@Override
	public List<Map<String, Object>> getCustomerEvaluationListByInfo(Map map) {
		return patientRegisterPraiseDao.getCustomerEvaluationListByInfo(map);
	}

	//查询送心意数据， sunxiao
	@Override
	public List<Map<String, Object>> findReceiveTheMindList(Map map) {
		return patientRegisterPraiseDao.findReceiveTheMindList(map);
	}

	//查询不满意数据， sunxiao
	@Override
	public List<Map<String, Object>> findDissatisfiedList(Map map) {
		return patientRegisterPraiseDao.findDissatisfiedList(map);
	}

	@Override
	public void sendRemindMsgToUser(String userId,String sessionId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("consultSessionId",sessionId);
		String msg = "";
//		String nowDate = DateUtils.DateToStr(new Date(),"yyyy-MM");

		List<Map<String,Object>>  evaluationList = patientRegisterPraiseDao.getCustomerEvaluationListByInfo(params);
		if(null == evaluationList||evaluationList.size() == 0){
//                        发送消息+提示用户评价的信息
			msg= "医生太棒,要给好评;\n服务不好,留言吐槽. \n ----------\n【" +
					"<a href='http://120.25.161.33/keeper/wxPay/patientPay.do?serviceType=customerPay&customerId=" +
					params.get("uuid") +"&sessionId="+sessionId+ "'>点击这里去评价</a>】";
		}else{
			msg = "嗨，亲爱的,本次咨询已关闭。";
		}

//		检测是否分享宝大夫
//		Query queryInLog = new Query();
//		queryInLog.addCriteria(new Criteria("title").is("consultfirstchargefree").andOperator(
//				new Criteria("create_date").gte(nowDate)));
//		long shareBaodfNum = mongoDBServiceLog.queryCount(queryInLog);
//		queryInLog = new Query();
//		queryInLog.addCriteria(new Criteria("title").is("pushconsultfirstchargefree").andOperator(
//				new Criteria("create_date").gte(nowDate)).andOperator(new Criteria("parameters").in(userId)));
//		long pushBaodfNum = mongoDBServiceLog.queryCount(queryInLog);

//
//		queryInLog = new Query();
//		queryInLog.addCriteria(new Criteria("title").is("BHS_TXXX_LJJH"));
//		long joinUmbrella = mongoDBServiceLog.queryCount(queryInLog);
//		queryInLog = new Query();
//		queryInLog.addCriteria(new Criteria("title").is("PUSH_BHS_TXXX_LJJH").andOperator(new Criteria("parameters").in(userId)));
//		long pushJoinUmbrella = mongoDBServiceLog.queryCount(queryInLog);
//
//
//		queryInLog = new Query();
//		queryInLog.addCriteria(new Criteria("title").is("consultfirstchargefree").andOperator(
//				new Criteria("create_date").gte(nowDate)));
//		long shareUmbrella = mongoDBServiceLog.queryCount(queryInLog);
//		queryInLog = new Query();
//		queryInLog.addCriteria(new Criteria("title").is("pushconsultfirstchargefree").andOperator(
//				new Criteria("create_date").gte(nowDate)).andOperator(new Criteria("parameters").in(userId)));
//		long pushUmbrella = mongoDBServiceLog.queryCount(queryInLog);


//		if(shareBaodfNum == 0&&pushBaodfNum<=3){
//			msg =  "嗨，亲爱的,本次咨询已关闭 \n------------------\n本月4次免费咨询机会已用完，轻轻动动手指即可获得一次机会哦\n>><a href=''>分享宝大夫给朋友</a>";
//			LogUtils.saveLog("pushconsultfirstchargefree",userId);
//		}else if(joinUmbrella == 0&&pushJoinUmbrella<=3){
//			msg =  "嗨，亲爱的,本次咨询已关闭 \n------------------\n加入宝护伞不仅能获得最高40万重疾保障，还能获得一次免费咨询机会哦！\n>><a href=''>抢购宝护伞</a>";
//			LogUtils.saveLog("PUSH_BHS_TXXX_LJJH",userId);
//		}else if(shareUmbrella == 0&&pushUmbrella<=3){
//			msg =  "嗨，亲爱的,本次咨询已关闭 \n------------------\n动动手指传递爱，分享宝护伞即可免费咨询一次医生哦！\n>><a href=''>分享宝护伞给朋友</a>";
//			LogUtils.saveLog("pushconsultfirstchargefree",userId);
//		}
		Map<String,Object> parameter = systemService.getWechatParameter();
		String token = (String)parameter.get("token");
		WechatUtil.sendMsgToWechat(token,userId,msg);
	}

	@Override
	public List<Map<String, Object>> findDoctorEvaluationById(Map<String, Object> map) {
		return patientRegisterPraiseDao.findDoctorEvaluationById(map);
	}

	@Override
	public List<Map<String, Object>> findDoctorAllEvaluationByInfo(Map<String, Object> map) {
		return patientRegisterPraiseDao.findDoctorAllEvaluationById(map);
	}

	@Override
	public String getNonRealtimeCustomerId(Integer sessionid) {
		return patientRegisterPraiseDao.getNonRealtimeCustomerId(sessionid);
	}
}
