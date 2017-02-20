package com.cxqm.xiaoerke.modules.schedule.web;

import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.utils.ConstantUtil;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGamesVo;
import com.cxqm.xiaoerke.modules.activity.service.OlyGamesService;
import com.cxqm.xiaoerke.modules.consult.entity.*;
import com.cxqm.xiaoerke.modules.consult.service.BabyCoinService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.consult.service.util.ConsultUtil;
import com.cxqm.xiaoerke.modules.nonRealTimeConsult.service.NonRealTimeConsultService;
import com.cxqm.xiaoerke.modules.operation.service.ConsultStatisticService;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.service.LogMongoDBServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.WechatMessageUtil;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineSendMessageVo;
import com.cxqm.xiaoerke.modules.vaccine.service.VaccineService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * backend定时器
 *
 * @author deliang
 * @version 2016-06-24
 */

public class ScheduleTaskController extends BaseController {


    @Autowired
    private ConsultStatisticService consultStatisticService;


    @Autowired
    private VaccineService vaccineService;

    @Autowired
    private LogMongoDBServiceImpl logMongoDBService;

    @Autowired
    private OlyGamesService olyGamesService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private SysPropertyServiceImpl sysPropertyService;

    @Autowired
    private NonRealTimeConsultService nonRealTimeConsultService;

    @Autowired
    private BabyCoinService babyCoinService;

    @Autowired
    private SessionRedisCache sessionRedisCache;

    @Autowired
    private ConsultSessionService consultSessionService ;

    @Autowired
    private ConsultRecordService consultRecordService;

    /**
     * 咨询统计信息
     */
    public void consultStatistic() {//createDate weekBeginDate monthBeginDate
        Calendar calendar = Calendar.getInstance();

        HashMap<String, Object> searchMap = new HashMap<String, Object>();
        calendar.add(Calendar.DATE, -1);
        Date createTime = calendar.getTime();
        String createDate = DateUtils.formatDate(new Date(calendar.getTimeInMillis()), "yyyy-MM-dd");
        setDate(calendar);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date endDate = new Date();

        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -8);
        setDate(calendar);
        String weekBeginDate = DateUtils.formatDateTime(new Date(calendar.getTimeInMillis()));

        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        setDate(calendar);
        String weekEndDate = DateUtils.formatDateTime(new Date(calendar.getTimeInMillis()));

        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -31);
        setDate(calendar);
        String monthBeginDate = DateUtils.formatDateTime(new Date(calendar.getTimeInMillis()));

        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        setDate(calendar);
        String monthEndDate = DateUtils.formatDateTime(new Date(calendar.getTimeInMillis()));

        searchMap.put("createDate", createDate);
        searchMap.put("weekBeginDate", weekBeginDate);
        searchMap.put("weekEndDate", weekEndDate);
        searchMap.put("monthBeginDate", monthBeginDate);
        searchMap.put("monthEndDate", monthEndDate);

        List<Float> resultList = consultStatisticService.getConsultStatistic(searchMap);

        //查询当天咨询数
        /**
         ** select count(DISTINCT(sys_user_id)) from consult_session where create_time like '${searchDate}%'
         */
        float dayNumber = resultList.get(0);
        //查询累计咨询数
        /**
         * select count(DISTINCT(sys_user_id)) from consult_session
         */
        float titileNnumber = resultList.get(1);
        //查询评价人数
        /**
         * select count(DISTINCT(openid)) from customerEvaluation where createtime like '2016-06-24%' and (serviceAttitude != '0')
         */
        float evaluateNumber = resultList.get(2);
        //查询不评价人数
        /**
         * 查询当天咨询数 - 查询评价人数
         */
        float unevaluateNumber = dayNumber - evaluateNumber;
        //查询满意人数（天）
        /**
         * select count(DISTINCT(openid)) countNumber from customerEvaluation where createtime like '2016-06-24%' and (serviceAttitude = '3' or serviceAttitude = '5')
         */
        float satisfiedNumber = resultList.get(3);
        //查询满意人数（周）
        /**
         * select count(DISTINCT(openid)) countNumber from customerEvaluation where createtime between '' and '' and (serviceAttitude = '3' or serviceAttitude = '5')
         */
        float weekSatisfiedNumber = resultList.get(4);
        //查询满意人数（月）
        /**
         * select count(DISTINCT(openid)) countNumber from customerEvaluation where createtime between '' and '' and (serviceAttitude = '3' or serviceAttitude = '5')
         */
        float monthSatisfiedNumber = resultList.get(5);
        //不满意人数（天）
        /**
         * select count(*) from customerEvaluation where createtime between '' and '' and (serviceAttitude = '1') group by openid order by null
         */
        float dayYawpNumber = resultList.get(6);
        //不满意人数（周）
        /**
         * select count(*) from customerEvaluation where createtime between '' and '' and (serviceAttitude = '1') group by openid order by null
         */
        float weekYawpNumber = resultList.get(7);
        //不满意人数（月）
        /**
         * select count(*) from customerEvaluation where createtime between '' and '' and (serviceAttitude = '1') group by openid order by null
         */
        float monthYawpNumber = resultList.get(8);
        //日满意度
        /**
         * 每天满意人数/每天评价人数
         */
        String daySatisfiedDegree = "";
        if (evaluateNumber != 0) {
            daySatisfiedDegree = String.valueOf(satisfiedNumber / evaluateNumber * 100) + "%";
        }

        //打赏人数
        /**
         * select count(*) from customerEvaluation where createtime like '%' and (payStatus = 'success') group by openid order by null
         */
        float rewardNumber = resultList.get(9);

        //打赏比例
        /**
         * 打赏人数/评价人数
         */
        String rewardDegree = "";
        if (evaluateNumber != 0) {
            rewardDegree = String.valueOf(rewardNumber / evaluateNumber * 100) + "%";
        }
        //首次咨询取消关注
        /**
         SELECT count(*) from sys_attention where date LIKE '2016-06-24%' and STATUS='1' and openid in (
         select sys_user_id from consult_session where consult_number = 1 and create_time like '2016-06-24%' GROUP BY sys_user_id ORDER BY null
         )
         */
        float firstConsultCancleAttentionNumber = resultList.get(10);
        //多次咨询后取消关注
        /**
         SELECT count(*) from sys_attention where date LIKE '2016-06-24%' and STATUS='1' and openid in (
         select sys_user_id from consult_session where consult_number > 1 and create_time like '2016-06-24%' GROUP BY sys_user_id ORDER BY null
         )
         */
        float moreConsultCancleAttentionNumber = resultList.get(11);
        //首次咨询人数
        /**
         *select count(*) from consult_session where consult_number = 1 and create_time like '%%'
         */
        float firstConsultNumber = resultList.get(12);
        //首次咨询比例
        /**
         * 首次咨询人数/当天咨询数
         */
        String firstConsultDegree = "";
        if (dayNumber != 0) {
            firstConsultDegree = String.valueOf(firstConsultNumber / dayNumber * 100) + "%";
        }
        //多次咨询人数
        /**
         * select count(*) from consult_session where consult_number > 0 and create_time like '%%'
         */
        float moreConusltNumber = resultList.get(13);
        //多次咨询比例
        /**
         * 多次咨询人数 / 当天咨询数
         */
        String moreConsultDegree = "";
        if (dayNumber != 0) {
            moreConsultDegree = String.valueOf(moreConusltNumber / dayNumber * 100) + "%";
        }

        //查询每周评价人数
        /**
         * select count(DISTINCT(openid)) from customerEvaluation where createtime between '' and '' and (serviceAttitude != '0')
         */
        float weekEvaluateNumber = resultList.get(14);
        //查询每月评价人数
        /**
         *select count(DISTINCT(openid)) from customerEvaluation where createtime between '' and '' and (serviceAttitude != '0')
         */
        float monthEvaluateNumber = resultList.get(15);

        //周满意度
        /**
         * 每周满意人数/每周评价人数
         */
        String weedSatisfiedDegree = "";
        if (weekEvaluateNumber != 0) {
            weedSatisfiedDegree = String.valueOf((float) weekSatisfiedNumber / (float) weekEvaluateNumber * 100) + "%";
        }
        //月满意度
        /**
         * 每月满意人数/月评价人数
         */
        String monthDatisfiedDegree = "";
        if (monthEvaluateNumber != 0) {
            monthDatisfiedDegree = String.valueOf((float) monthSatisfiedNumber / (float) monthEvaluateNumber * 100) + "%";
        }
        //最小金额
        /**
         *select MIN(redpacket) countNumber from customerEvaluation where createtime like '2016-06-30%' and redPacket is not null and redPacket !=''
         */
        float minMoney = 0;
        float maxMoney = 0;
        float sumMoney = 0;
        try {
            minMoney = resultList.get(16);
            //最高金额
            /**
             * select MAX(redpacket) countNumber from customerEvaluation where createtime like '2016-06-30%' and redPacket is not null and redPacket !=''
             */
            maxMoney = resultList.get(17);
            //打赏总额
            /**
             *select sum(redpacket) countNumber from customerEvaluation where createtime like '2016-06-30%' and redPacket is not null and redPacket !=''
             */
            sumMoney = resultList.get(18);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //评价点击量
        Query queryClick = new Query().addCriteria(Criteria.where("title").regex("ZXPJSXY_JE")).addCriteria(Criteria.where("create_date").gte(startDate).andOperator(Criteria.where("create_date").lte(endDate)));

        int evaluateClickNumber = (int) logMongoDBService.queryCount(queryClick);
        //评价占比
        /**
         *评价点击量/当天咨询数
         */
        String evaluateClickDegree = "";
        if (dayNumber != 0) {
            evaluateClickDegree = String.valueOf((float) evaluateClickNumber / (float) dayNumber * 100) + "%";
        }
        System.out.println("开始查询时间===================================="+System.currentTimeMillis());
        //分享点击量
        Query queryShare = new Query().addCriteria(Criteria.where("title").is("ZXFX")).addCriteria(Criteria.where("create_date").gte(startDate).andOperator(Criteria.where("create_date").lte(endDate)));

        int shareClickNumber = (int) logMongoDBService.queryCount(queryShare);
        System.out.println("结束查询时间===================================="+System.currentTimeMillis());
        //分享占比
        /**
         *分享数/当天咨询数
         */
        String shareClickDegree = "";
        if (dayNumber != 0) {
            shareClickDegree = String.valueOf((float) shareClickNumber / (float) dayNumber * 100) + "%";
        }

        /**
         * 付费推送推了多少人需要去重
         */
        Query query = new Query().addCriteria(Criteria.where("title").is("consult_charge_twice_information")).addCriteria(Criteria.where("create_date").gte(startDate).andOperator(Criteria.where("create_date").lte(endDate)));
        int chargeSendMessageNumber = logMongoDBService.queryListDistinct(query, "parameters").size();

        /**
         * 付费推送多少人点击了   需要去重  时间改为当天
         */
        query = new Query().addCriteria(Criteria.where("title").regex("consult_charge_twice_information_payclick")).addCriteria(Criteria.where("create_date").gte(startDate).andOperator(Criteria.where("create_date").lte(endDate)));
        int chargeMessageClickNumber = logMongoDBService.queryListDistinct(query, "open_id").size();
        /**
         * 多少人付费    加当天的时间
         */
        float chargeSuccessNumber = resultList.get(19);

        ConsultStatisticVo consultStatisticVo = new ConsultStatisticVo();
        consultStatisticVo.setDayNumber((int) dayNumber);
        consultStatisticVo.setDaySatisfiedDegree(daySatisfiedDegree);
        consultStatisticVo.setDayYawpNumber((int) dayYawpNumber);
        consultStatisticVo.setEvaluateNumber((int) evaluateNumber);
        consultStatisticVo.setSatisfiedNumber((int) satisfiedNumber);
        consultStatisticVo.setFirstConsultCancleAttentionNumber((int) firstConsultCancleAttentionNumber);
        consultStatisticVo.setFirstConsultDegree(firstConsultDegree);
        consultStatisticVo.setMonthSatisfiedDegree(monthDatisfiedDegree);
        consultStatisticVo.setMonthYawpNumber((int) monthYawpNumber);
        consultStatisticVo.setMoreConsultCancleAttentionNumber((int) moreConsultCancleAttentionNumber);
        consultStatisticVo.setMoreConsultDegree(moreConsultDegree);
        consultStatisticVo.setRewardDegree(rewardDegree);
        consultStatisticVo.setFirstConsultNumber((int) firstConsultNumber);
        consultStatisticVo.setFirstConsultCancleAttentionNumber((int) firstConsultCancleAttentionNumber);
        consultStatisticVo.setWeekYawpNumber((int) weekYawpNumber);
        consultStatisticVo.setWeedSatisfiedDegree(weedSatisfiedDegree);
        consultStatisticVo.setUnevaluateNumber((int) unevaluateNumber);
        consultStatisticVo.setTitileNumber((int) titileNnumber);
        consultStatisticVo.setMoreConusltNumber((int) moreConusltNumber);
        consultStatisticVo.setRewardNumber((int) rewardNumber);
        consultStatisticVo.setMinMoney(String.valueOf(minMoney));
        consultStatisticVo.setMaxMoney(String.valueOf(maxMoney));
        consultStatisticVo.setSumMoney(String.valueOf(sumMoney));
        consultStatisticVo.setEvaluateClickNumber(evaluateClickNumber);
        consultStatisticVo.setEvaluateClickDegree(evaluateClickDegree);
        consultStatisticVo.setShareClickNumber(shareClickNumber);
        consultStatisticVo.setShareClickDegree(shareClickDegree);
        consultStatisticVo.setCreateDate(createTime);

        consultStatisticVo.setChargeMessageClickNumber(chargeMessageClickNumber);
        consultStatisticVo.setChargeSendMessageNumber(chargeSendMessageNumber);
        consultStatisticVo.setChargeSuccessNumber((int) chargeSuccessNumber);
        consultStatisticService.insertSelective(consultStatisticVo);

    }

    private void setDate(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
    }

    /**
     * 疫苗提醒
     */
    public void babyVaccineRemind() {
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        Map parameter = systemService.getWechatParameter();
        String token = (String) parameter.get("token");
        VaccineSendMessageVo searchVaccineSendMessageVo = new VaccineSendMessageVo();
        searchVaccineSendMessageVo.setSearchTime(DateUtils.DateToStr(new Date(), "date"));
        searchVaccineSendMessageVo.setValidFlag(ConstantUtil.VACCINEVALID.getVariable());

        //今天要发消息的人
        List<VaccineSendMessageVo> vaccineSendMessageVos = vaccineService.selectSendMessageInfo(searchVaccineSendMessageVo);

        for (VaccineSendMessageVo sendMessageVo : vaccineSendMessageVos) {
            searchVaccineSendMessageVo.setSysUserId(sendMessageVo.getSysUserId());
            List<VaccineSendMessageVo> messageVos = vaccineService.selectByVaccineSendMessageInfo(searchVaccineSendMessageVo);
            if (messageVos != null && messageVos.size() > 0) {
                if (messageVos.size() == 1) {
                    VaccineSendMessageVo vo = messageVos.get(0);
                    String[] sendContent = vo.getContent().split("\\|\\|");

                    WechatMessageUtil.templateModel(sendContent[0], sendContent[1].replace("0000-00-00", DateUtils.DateToStr(vo.getInoculationTime(), "date")), sendContent[2], "", "", sendContent[3], token, "", vo.getSysUserId(), sysPropertyVoWithBLOBsVo.getTemplateIdDBRWTX());
                    //将疫苗的发送时间往后延迟三十天，消息失效按照用户扫描为准，扫码后与当前码有关的消息失效
                    vo.setId(vo.getId());
                    Calendar sendTimeAdd30 = Calendar.getInstance();
                    sendTimeAdd30.setTime(vo.getSendTime());
                    sendTimeAdd30.add(Calendar.DAY_OF_MONTH, 30);
                    vo.setSendTime(sendTimeAdd30.getTime());

                    Calendar inoculationTimeAdd30 = Calendar.getInstance();
                    inoculationTimeAdd30.setTime(vo.getInoculationTime());
                    inoculationTimeAdd30.add(Calendar.DAY_OF_MONTH, 30);
                    vo.setInoculationTime(inoculationTimeAdd30.getTime());

                    vaccineService.updateByPrimaryKeyWithBLOBs(vo);
                } else {
                    String symbol = "和";
                    VaccineSendMessageVo vo = messageVos.get(0);
                    String[] sendContent = vo.getContent().split("\\|\\|");
                    String title = sendContent[0];
                    String keyword1 = sendContent[1];
                    String keyword2 = sendContent[2];
                    String keyword3 = sendContent[3];
                    if (messageVos.size() == 2) {
                        for (int i = 1; i < messageVos.size(); i++) {
                            String temp = messageVos.get(i).getContent().split("\\|\\|")[1];
                            keyword1 = keyword1 + symbol + temp.substring(temp.indexOf("种")+1, temp.length());
                            keyword1 = keyword1.replace("0000-00-00", DateUtils.DateToStr(messageVos.get(i).getInoculationTime(), "date"));
                        }
                    }else{
                        for (int i = 1; i < messageVos.size(); i++) {

                            String temp = messageVos.get(i).getContent().split("\\|\\|")[1];
                            if(i != messageVos.size()-1){
                                symbol = "、";
                            }
                            keyword1 = keyword1 + symbol + temp.substring(temp.indexOf("种")+1, temp.length());
                            keyword1 = keyword1.replace("0000-00-00", DateUtils.DateToStr(messageVos.get(i).getInoculationTime(), "date"));
                        }
                    }


                    WechatMessageUtil.templateModel(title, keyword1, keyword2, "", "", keyword3, token, "", vo.getSysUserId(), sysPropertyVoWithBLOBsVo.getTemplateIdDBRWTX());

                }
            }

        }
    }

    public void updateOlyGames() {
        olyGamesService.updateLevelCurrentTimes(new OlyBabyGamesVo());
    }

    public void updateConsultDoctorInfo() {
        nonRealTimeConsultService.updateConsultDoctorInfo();
    }

    public void updateBabyCoinInviteNumber(){
        BabyCoinVo babyCoinVo = new BabyCoinVo();
        babyCoinVo.setInviteNumberMonth(0);
        babyCoinService.updateBabyCoinInviteNumber(babyCoinVo);
    }


    /**
     * 清理redis、mongo、mysql垃圾数据
     * 2016-10-12 15:51:46 jiangzg add
     */
    public void clearRedisData() {
        //o3_NPwmmMGpZxC5eS491_JzFRHtU
        Map<String, Object> result = new HashMap<String, Object>();
        List dataList = new ArrayList();          //删除的数据
        List<Object> consultSessions = sessionRedisCache.getConsultSessionsByKey();
        List<Object> sessionIds = sessionRedisCache.getSessionIdByKey();
        List<Integer> sessionIdList = null;            //redis 所有sessionID
        if(sessionIds != null && sessionIds.size() >0){
            sessionIdList = new ArrayList<Integer>();
            for (Object o : sessionIds) {
                Integer sessionId = Integer.valueOf(String.valueOf(o));
                sessionIdList.add(sessionId);
            }
        }
        Date nowDate = new Date();
        HashMap<Object,Object> dataMap = new HashMap<Object,Object>();
        /**
         * mysql
         */
        ConsultSession consultSession = new ConsultSession();
        consultSession.setStatus(ConsultSession.STATUS_ONGOING);
        List<ConsultSession> consultSessionList = consultSessionService.selectBySelective(consultSession);
        if(consultSessionList !=null && consultSessionList.size() >0){
            for(ConsultSession ongoingConsultSession : consultSessionList){
                if(nowDate.getTime() - ongoingConsultSession.getCreateTime().getTime() > 6*60*60*1000){
                    ongoingConsultSession.setStatus(ConsultSession.STATUS_COMPLETED);
                    consultSessionService.updateSessionInfo(ongoingConsultSession);
                    if(!dataMap.containsKey(ongoingConsultSession.getId())){
                        dataMap.put(ongoingConsultSession.getId(), ongoingConsultSession.getUserId());
                    }
                }
            }
        }
        /**
         * mongo
         */
        Query queryAgain = new Query(where("status").is("ongoing"));
        List<ConsultSessionStatusVo> consultSessionStatusAgainVos = consultRecordService.querySessionStatusList(queryAgain);
        if(consultSessionStatusAgainVos != null && consultSessionStatusAgainVos.size()>0){
            for(int i=0 ; i<consultSessionStatusAgainVos.size();i++){
                if(consultSessionStatusAgainVos.get(i).getLastMessageTime() != null && consultSessionStatusAgainVos.get(i).getLastMessageTime().getTime()-nowDate.getTime() > 6*60*60*1000){
                    //清除mongo内的残留数据
                    Query removeQuery = new Query().addCriteria(where("_id").is(consultSessionStatusAgainVos.get(i).getId()));
                    consultRecordService.updateConsultSessionStatusVo(removeQuery,"complete");
                    if(!dataMap.containsKey(consultSessionStatusAgainVos.get(i).getSessionId())){
                        dataMap.put(consultSessionStatusAgainVos.get(i).getSessionId(), consultSessionStatusAgainVos.get(i).getUserId());
                    }
                    /*if(!removelist.contains(consultSessionStatusAgainVos.get(i).getSessionId())) {
                         removelist.add(consultSessionStatusAgainVos.get(i).getSessionId());
                    }*/
                }
            }
        }
        /**
         * monog sessionId is null
         */
        List matchList = new ArrayList();
        matchList.add("null");
        matchList.add("");
        Query clearQuery = new Query(where("sessionId").in(matchList));
        consultRecordService.deleteMongoRecordBySelective(clearQuery, ConsultSessionStatusVo.class);

        /**
         * redis
         */
        Map noRemoveMap = new HashMap();
        if(consultSessions !=null && consultSessions.size()>0){
            for(Object consultSessionObject:consultSessions){
                if(consultSessionObject != null) {
                    RichConsultSession consultSessionValue = ConsultUtil.transferMapToRichConsultSession((Map<Object, Object>) consultSessionObject);
                    if(consultSessionValue != null && consultSessionValue.getCreateTime() != null && (nowDate.getTime() - consultSessionValue.getCreateTime().getTime() > 6*60*60*1000)){
                        if("ongoing".equalsIgnoreCase(consultSessionValue.getStatus())){
                            sessionRedisCache.removeConsultSessionBySessionId(consultSessionValue.getId());
                            if(!dataMap.containsKey(consultSessionValue.getId())) {
                                dataMap.put(consultSessionValue.getId(), consultSessionValue.getUserId());
                            }
                       /* if(!removelist.contains(consultSessionValue.getId())){
                            removelist.add(consultSessionValue.getId());
                        }*/
                            if(sessionIdList != null && sessionIdList.size() >0){
                                if(sessionIdList.contains(consultSessionValue.getId())){
                                    Integer sessionId = sessionRedisCache.getSessionIdByUserId(consultSessionValue.getUserId());
                                    if((sessionId !=null) && String.valueOf(sessionId).equalsIgnoreCase(String.valueOf(consultSessionValue.getId()))){
                                        sessionRedisCache.removeUserIdSessionIdPair(consultSessionValue.getUserId());
                                    }else{
                                        HashMap userIdSessionIdPair = new HashMap();
                                        userIdSessionIdPair.put(sessionId,consultSessionValue.getUserId());
                                        noRemoveMap.put(consultSessionValue.getId(),userIdSessionIdPair);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        dataList.add(dataMap);
        result.put("removeSessionList",dataList);
        result.put("noRemoveSessionList",noRemoveMap);
    }

    /**
     * 蒙太奇聊天记录导出 每天抓取
     * jiangzg 2017-1-13 16:02:42
     */
    public void exportConsultDataByThird() {
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        String source = "COOP_BTQ";

        String outExportPath = "";
        String osName = System.getProperty("os.name").toLowerCase();
        if("windows".equalsIgnoreCase(osName)){
            outExportPath = "D:/test" ;
        }else{
            outExportPath = "/mnt/tomcat/data" ;
        }
        StringBuilder startTime = new StringBuilder();
        StringBuilder endTime = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int currentYear = calendar.get(calendar.YEAR);
        int currentMonth = calendar.get(calendar.MONTH) + 1;
        int currentDate = calendar.get(calendar.DAY_OF_MONTH);
        if(currentDate == 1){
            if(currentMonth == 1){
                currentYear = currentYear - 1 ;
                currentMonth = 12;
                currentDate = 31 ;
            }else{
                if(calendar.YEAR % 4 == 0){
                   if(currentMonth == 3){
                       currentMonth = 2 ;
                       currentDate = 29 ;
                   }else{
                      if("1-3-5-7-8-10-12".contains(""+currentMonth)){
                          currentMonth = currentMonth -1 ;
                          currentDate = 30 ;
                      }else{
                          currentMonth = currentMonth -1 ;
                          currentDate = 31 ;
                      }
                   }
                }else{
                    if(currentMonth == 3){
                        currentMonth = 2 ;
                        currentDate = 28 ;
                    }else{
                        if("1-3-5-7-8-10-12".contains(""+currentMonth)){
                            currentMonth = currentMonth -1 ;
                            currentDate = 30 ;
                        }else{
                            currentMonth = currentMonth -1 ;
                            currentDate = 31 ;
                        }
                    }
                }
            }
        }
        int currentHour = calendar.get(calendar.HOUR_OF_DAY);

        if (StringUtils.isNotNull(source)) {
            if (currentMonth < 10) {
                if(currentDate < 10){
                    startTime.append(""+currentYear+ "-0" + currentMonth + "-0"+currentDate).append("").append("00:00:00");
                    endTime.append(""+currentYear+ "-0" + currentMonth + "-0"+currentDate).append("").append("23:59:59");
                }else{
                    startTime.append(""+currentYear + "-0" + currentMonth +"-"+ currentDate).append("").append("00:00:00");
                    endTime.append(""+currentYear + "-0" + currentMonth +"-"+ currentDate).append("").append("23:59:59");
                }
            } else {
                if(currentDate < 10){
                    startTime.append(""+currentYear + currentMonth + "-0" + currentDate).append("").append("00:00:00");
                    endTime.append(""+currentYear + currentMonth + "-0" + currentDate).append("").append("23:59:59");
                }else {
                    startTime.append(""+currentYear + currentMonth + currentDate).append("").append("00:00:00");
                    endTime.append(""+currentYear + currentMonth + currentDate).append("").append("23:59:59");
                }
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long differTime = 0;
        long startDate = 0l ;
        long endDate = 0l ;
        try {
            Date start = sdf.parse(startTime.toString());
            Date end = sdf.parse(endTime.toString());
            startDate = start.getTime();
            endDate = end.getTime();
            differTime = endDate - startDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (StringUtils.isNotNull(source)) {
            if("COOP_BHQ".equalsIgnoreCase(source)){
                source = "h5bhq";
            }else if("COOP_YKDL".equalsIgnoreCase(source)){
                source = "h5ykdl";
            }else if("COOP_BTQ".equalsIgnoreCase(source)){
                source = "h5mtq";
            }
            JSONArray jsonArray = new JSONArray();
            try {
                /**
                 * 加入聊天记录提取代码
                 */
                Query query = new Query(Criteria.where("createDate").exists(true).andOperator(Criteria.where("createDate").gte(sdf.parse(startTime.toString())), Criteria.where("createDate").lt(sdf.parse(endTime.toString()))).and("source").is(source));
                query.with(new Sort(Sort.Direction.DESC, "createDate"));
                List<ConsultRecordMongoVo> consultRecordMongoVos = consultRecordService.getCurrentUserHistoryRecord(query);
                if (consultRecordMongoVos != null && consultRecordMongoVos.size() > 0) {
                    for (ConsultRecordMongoVo consultRecordMongoVo : consultRecordMongoVos) {
                        JSONObject data = new JSONObject();
                        data.put("id", consultRecordMongoVo.getSessionId());
                        data.put("userName", consultRecordMongoVo.getNickName());
                        data.put("userId", consultRecordMongoVo.getUserId());
                        data.put("csUserId", consultRecordMongoVo.getCsUserId());
                        data.put("senderId", consultRecordMongoVo.getSenderId());
                        if(consultRecordMongoVo.getMessage().contains("：")){
                            data.put("message", consultRecordMongoVo.getMessage().substring(consultRecordMongoVo.getMessage().indexOf("：")+1));
                        }else{
                            data.put("message", consultRecordMongoVo.getMessage());
                        }
                        data.put("type", consultRecordMongoVo.getType());
                        data.put("createDate", consultRecordMongoVo.getCreateDate());
                        jsonArray.add(data);
                    }
                }
            } catch (Exception ex) {
                ex.getStackTrace();
            }
            Map titleMap = new HashMap();
            titleMap.put("1","会话ID");
            titleMap.put("2","用户ID");
            titleMap.put("3","用户昵称");
            titleMap.put("4","医生ID");
            titleMap.put("5","发送者ID");
            titleMap.put("6","发送消息");
            titleMap.put("7","消息类型");
            titleMap.put("8","发送时间");
            exportConsultDataForMTQ(outExportPath,source,startDate,endDate,titleMap,jsonArray);
        }
    }

    public void exportConsultDataForMTQ(String outExportPath , String source ,long startDate ,long endDate,Map titleMap,JSONArray jsonArray){
        File csvFile = null;
        BufferedWriter csvFileOutputStream = null;
        String fileName = source +"-"+new SimpleDateFormat("yyMMdd").format(new Date());
        try {
            csvFile = new File(outExportPath + fileName + ".csv");
            File parent = csvFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            if(!csvFile.exists()){
                csvFile.createNewFile();
            }
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(csvFile), "GB2312"), 1024);
            // 写入文件头部
            for (Iterator propertyIterator = titleMap.entrySet().iterator(); propertyIterator.hasNext();) {
                java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator
                        .next();
                csvFileOutputStream.write("\""
                        + propertyEntry.getValue().toString() + "\"");
                if (propertyIterator.hasNext()) {
                    csvFileOutputStream.write(",");
                }
            }
            csvFileOutputStream.newLine();
            if(jsonArray != null && jsonArray.size() > 0){
               for(Iterator iterator = jsonArray.iterator(); iterator.hasNext();){
                   LinkedHashMap row = (LinkedHashMap) iterator.next();
                   for (Iterator propertyIterator = row.entrySet().iterator(); propertyIterator.hasNext();) {
                       java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
                       // System.out.println( BeanUtils.getProperty(row, propertyEntry.getKey().toString()));
                       csvFileOutputStream.write("\""
                               +  propertyEntry.getValue().toString() + "\"");
                       if (propertyIterator.hasNext()) {
                           csvFileOutputStream.write(",");
                       }
                   }
                   if (iterator.hasNext()) {
                       csvFileOutputStream.newLine();
                   }
               }
               csvFileOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                csvFileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
