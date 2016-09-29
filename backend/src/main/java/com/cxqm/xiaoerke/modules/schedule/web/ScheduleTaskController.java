package com.cxqm.xiaoerke.modules.schedule.web;

import com.cxqm.xiaoerke.common.utils.ConstantUtil;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGamesVo;
import com.cxqm.xiaoerke.modules.activity.service.OlyGamesService;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultStatisticVo;
import com.cxqm.xiaoerke.modules.operation.service.ConsultStatisticService;
import com.cxqm.xiaoerke.modules.sys.service.LogMongoDBServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineSendMessageVo;
import com.cxqm.xiaoerke.modules.vaccine.service.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;

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
        //分享点击量
        Query queryShare = new Query().addCriteria(Criteria.where("title").is("ZXFX")).addCriteria(Criteria.where("create_date").gte(startDate).andOperator(Criteria.where("create_date").lte(endDate)));

        int shareClickNumber = (int) logMongoDBService.queryCount(queryShare);
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
        Query query = new Query().addCriteria(Criteria.where("title").regex("consult_charge_twice_information")).addCriteria(Criteria.where("create_date").gte(startDate).andOperator(Criteria.where("create_date").lte(endDate)));
        int chargeSendMessageNumber = logMongoDBService.queryListDistinct(query, "open_id").size();

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

    public void babyVaccineRemind() {
        Map parameter = systemService.getWechatParameter();
        String token = (String) parameter.get("token");
        VaccineSendMessageVo vaccineSendMessageVo = new VaccineSendMessageVo();
        vaccineSendMessageVo.setSendTime(new Date());
        vaccineSendMessageVo.setValidFlag(ConstantUtil.VACCINEVALID.getVariable());
        List<VaccineSendMessageVo> vaccineSendMessageVos = vaccineService.selectByVaccineSendMessageInfo(vaccineSendMessageVo);
        for (VaccineSendMessageVo vo :vaccineSendMessageVos){
            String resultStatus = WechatUtil.sendMsgToWechat(token, vo.getSysUserId(), vo.getContent());
            if (resultStatus.equals("messageOk")){
                vo.setId(vo.getId());
                vo.setValidFlag(ConstantUtil.VACCINEINVALID.getVariable());
                vaccineService.updateByPrimaryKeyWithBLOBs(vo);
            }

        }
    }

    public void updateOlyGames() {
        olyGamesService.updateLevelCurrentTimes(new OlyBabyGamesVo());
    }

}
