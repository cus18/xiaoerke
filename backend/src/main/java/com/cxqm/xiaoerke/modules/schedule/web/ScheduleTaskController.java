package com.cxqm.xiaoerke.modules.schedule.web;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultStatisticVo;
import com.cxqm.xiaoerke.modules.operation.service.ConsultStatisticService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * backend定时器
 *
 * @author deliang
 * @version 2016-06-24
 */

public class ScheduleTaskController extends BaseController {


    @Autowired
    private ConsultStatisticService consultStatisticService;

    /**
     * 咨询统计信息
     */
    public void consultStatistic() {//createDate weekBeginDate monthBeginDate
        Calendar calendar = Calendar.getInstance();

        HashMap<String, Object> searchMap = new HashMap<String, Object>();
        calendar.add(Calendar.DATE, -1);
        String createDate = DateUtils.formatDate(new Date(calendar.getTimeInMillis()), 1);

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

        List<Integer> resultList = consultStatisticService.getConsultStatistic(searchMap);

        //查询当天咨询数
        /**
         ** select count(DISTINCT(sys_user_id)) from consult_session where create_time like '${searchDate}%'
         */
        Integer dayNumber = resultList.get(0);
        //查询累计咨询数
        /**
         * select count(DISTINCT(sys_user_id)) from consult_session
         */
        Integer titileNnumber = resultList.get(1);
        //查询评价人数
        /**
         * select count(DISTINCT(openid)) from customerEvaluation where createtime like '2016-06-24%' and (serviceAttitude != '0')
         */
        Integer evaluateNumber = resultList.get(2);
        //查询不评价人数
        /**
         * 查询当天咨询数 - 查询评价人数
         */
        Integer unevaluateNumber = dayNumber - evaluateNumber;
        //查询满意人数（天）
        /**
         * select count(DISTINCT(openid)) countNumber from customerEvaluation where createtime like '2016-06-24%' and (serviceAttitude = '3' or serviceAttitude = '5')
         */
        Integer satisfiedNumber = resultList.get(3);
        //查询满意人数（周）
        /**
         * select count(DISTINCT(openid)) countNumber from customerEvaluation where createtime between '' and '' and (serviceAttitude = '3' or serviceAttitude = '5')
         */
        Integer weekSatisfiedNumber = resultList.get(4);
        //查询满意人数（月）
        /**
         * select count(DISTINCT(openid)) countNumber from customerEvaluation where createtime between '' and '' and (serviceAttitude = '3' or serviceAttitude = '5')
         */
        Integer monthSatisfiedNumber = resultList.get(5);
        //不满意人数（天）
        /**
         * select count(*) from customerEvaluation where createtime between '' and '' and (serviceAttitude = '1') group by openid order by null
         */
        Integer dayYawpNumber = resultList.get(6);
        //不满意人数（周）
        /**
         * select count(*) from customerEvaluation where createtime between '' and '' and (serviceAttitude = '1') group by openid order by null
         */
        Integer weekYawpNumber = resultList.get(7);
        //不满意人数（月）
        /**
         * select count(*) from customerEvaluation where createtime between '' and '' and (serviceAttitude = '1') group by openid order by null
         */
        Integer monthYawpNumber = resultList.get(8);
        //日满意度
        /**
         * 每天满意人数/每天评价人数
         */
        String daySatisfiedDegree = String.valueOf(satisfiedNumber / evaluateNumber * 100) + "%";

        //打赏人数
        /**
         * select count(*) from customerEvaluation where createtime like '%' and (payStatus = 'success') group by openid order by null
         */
        Integer rewardNumber = resultList.get(9);

        //打赏比例
        /**
         * 打赏人数/评价人数
         */
        String rewardDegree = String.valueOf(rewardNumber / evaluateNumber * 100) + "%";
        //首次咨询取消关注
        /**
         SELECT count(*) from sys_attention where date LIKE '2016-06-24%' and STATUS='1' and openid in (
         select sys_user_id from consult_session where consult_number = 1 and create_time like '2016-06-24%' GROUP BY sys_user_id ORDER BY null
         )
         */
        Integer firstConsultCancleAttentionNumber = resultList.get(10);
        //多次咨询后取消关注
        /**
         SELECT count(*) from sys_attention where date LIKE '2016-06-24%' and STATUS='1' and openid in (
         select sys_user_id from consult_session where consult_number > 1 and create_time like '2016-06-24%' GROUP BY sys_user_id ORDER BY null
         )
         */
        Integer moreConsultCancleAttentionNumber = resultList.get(11);
        //首次咨询人数
        /**
         *select count(*) from consult_session where consult_number = 1 and create_time like '%%'
         */
        Integer firstConsultNumber = resultList.get(12);
        //首次咨询比例
        /**
         * 首次咨询人数/当天咨询数
         */
        String firstConsultDegree = String.valueOf(firstConsultNumber / dayNumber * 100) + "%";
        //多次咨询人数
        /**
         * select count(*) from consult_session where consult_number > 0 and create_time like '%%'
         */
        Integer moreConusltNumber = resultList.get(13);
        //多次咨询比例
        /**
         * 多次咨询人数 / 当天咨询数
         */
        String moreConsultDegree = String.valueOf(moreConusltNumber / dayNumber * 100) + "%";

        //查询每周评价人数
        /**
         * select count(DISTINCT(openid)) from customerEvaluation where createtime between '' and '' and (serviceAttitude != '0')
         */
        Integer weekEvaluateNumber = resultList.get(14);
        //查询每月评价人数
        /**
         *select count(DISTINCT(openid)) from customerEvaluation where createtime between '' and '' and (serviceAttitude != '0')
         */
        Integer monthEvaluateNumber = resultList.get(15);

        //周满意度
        /**
         * 每周满意人数/每周评价人数
         */
        String weedSatisfiedDegree = String.valueOf(weekYawpNumber / weekEvaluateNumber * 100) + "%";
        //月满意度
        /**
         * 每月满意人数/月评价人数
         */
        String monthDatisfiedDegree = String.valueOf(monthYawpNumber / monthEvaluateNumber * 100) + "%";

        ConsultStatisticVo consultStatisticVo = new ConsultStatisticVo();

        consultStatisticVo.setDayNumber(dayNumber);
        consultStatisticVo.setDaySatisfiedDegree(daySatisfiedDegree);
        consultStatisticVo.setDayYawpNumber(dayYawpNumber);
        consultStatisticVo.setEvaluateNumber(evaluateNumber);
        consultStatisticVo.setFirstConsultCancleAttentionNumber(firstConsultCancleAttentionNumber);
        consultStatisticVo.setFirstConsultDegree(firstConsultDegree);
        consultStatisticVo.setMonthSatisfiedDegree(monthDatisfiedDegree);
        consultStatisticVo.setMonthYawpNumber(monthYawpNumber);
        consultStatisticVo.setMoreConsultCancleAttentionNumber(moreConsultCancleAttentionNumber);
        consultStatisticVo.setMoreConsultDegree(moreConsultDegree);
        consultStatisticVo.setRewardDegree(rewardDegree);
        consultStatisticVo.setFirstConsultNumber(firstConsultNumber);
        consultStatisticVo.setFirstConsultCancleAttentionNumber(firstConsultCancleAttentionNumber);
        consultStatisticVo.setWeekYawpNumber(weekYawpNumber);
        consultStatisticVo.setWeedSatisfiedDegree(weedSatisfiedDegree);
        consultStatisticVo.setUnevaluateNumber(unevaluateNumber);
        consultStatisticVo.setTitileNumber(titileNnumber);
        consultStatisticVo.setMoreConusltNumber(moreConusltNumber);
        consultStatisticVo.setRewardNumber(rewardNumber);
        consultStatisticService.insertSelective(consultStatisticVo);

    }

    private void setDate(Calendar calendar) {
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
    }

}
