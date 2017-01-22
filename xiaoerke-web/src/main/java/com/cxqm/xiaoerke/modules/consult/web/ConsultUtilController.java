package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.account.entity.PayRecord;
import com.cxqm.xiaoerke.modules.account.service.impl.PayRecordServiceImpl;
import com.cxqm.xiaoerke.modules.activity.service.OlyGamesService;
import com.cxqm.xiaoerke.modules.consult.entity.BabyCoinVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSession;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultStatisticVo;
import com.cxqm.xiaoerke.modules.consult.service.*;
import com.cxqm.xiaoerke.modules.consult.service.impl.ConsultRecordMongoDBServiceImpl;
import com.cxqm.xiaoerke.modules.operation.service.ConsultStatisticService;
import com.cxqm.xiaoerke.modules.sys.entity.MongoLog;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.LogMongoDBServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import com.cxqm.xiaoerke.modules.sys.utils.WechatMessageUtil;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import org.springframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/09/05 0024.
 */
@Controller
@RequestMapping(value = "consult/util")
public class ConsultUtilController {
    @Autowired
    private OlyGamesService olyGamesService;

    @Autowired
    private BabyCoinService babyCoinService;

    @Autowired
    private WechatAttentionService wechatAttentionService;

    @Autowired
    private SysPropertyServiceImpl sysPropertyService;

    @Autowired
    private SessionRedisCache sessionRedisCache;

    @Autowired
    private ConsultSessionPropertyService consultSessionPropertyService;

    @Autowired
    private PayRecordServiceImpl payRecordService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private ConsultStatisticService consultStatisticService;

    @Autowired
    SendMindCouponService sendMindCouponService;

    @Autowired
    private ConsultMemberRedsiCacheService consultMemberRedsiCacheService;

    @Autowired
    private LogMongoDBServiceImpl logMongoDBService;

    @Autowired
    private ConsultSessionService consultSessionService;

    /**
     * 给用户推消息
     *
     * @param request
     * @param
     * @return
     */
    /**
     * 业务服务提醒
     * <p>
     * xuOOz6ifH6PYJlktzn6Qbj6OODMCNoQt-iD3trAqnmA
     * <p>
     * 亲爱的，你的宝宝币即将到期啦！
     * <p>
     * 主题：宝宝币到期服务提醒
     * <p>
     * 提醒内容：2017年11月30日前获得的宝宝币还有4天到期
     * <p>
     * 截止日期：2017年1月15日
     * <p>
     * 还没用完的抓紧咨询医生， 或者点击前往兑换优惠券哦~
     */
    @RequestMapping(value = "/sendMessage", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> sendMessage(Map<String, Object> params, HttpSession session, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<String, Object>();
        Map parameter = systemService.getWechatParameter();
        String token = (String) parameter.get("token");
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        BabyCoinVo babyCoinVo = new BabyCoinVo();
        List<BabyCoinVo> babyCoinVos = babyCoinService.selectSubBabyCoin(babyCoinVo);
        String title = "亲爱的，你的宝宝币即将到期啦！ ";
        String keyword1 = "宝宝币到期提醒";
        String keyword2 = "2017年11月30日前获得的宝宝币还有4天到期";
        String keyword3 = "2017年1月15日";
        String remark = "年末送亲戚孩子的礼品你准备好了吗？宝宝币点击兑换优惠券更省钱哦~";
        String url = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=48";
        if (babyCoinVos != null && babyCoinVos.size() > 0) {
            for (int i = 0; i <= babyCoinVos.size(); i++) {
                if (babyCoinVos.get(i) != null) {
                    BabyCoinVo vo = babyCoinVos.get(i);
                    if (StringUtils.isNotNull(vo.getOpenId())) {
//                        if(vo.getOpenId().equals("o3_NPwqranZIs-hNjl-B2LjV39oQ"))
                        WechatMessageUtil.templateModel(title, keyword1, keyword2, keyword3, "", remark, token, url, vo.getOpenId(), sysPropertyVoWithBLOBsVo.getTemplateIdYWFWTX());
//                        babyCoinService.updateBabyCoinByOpenId(vo);
                    }
                }
            }
        }
        return response;
    }

    @RequestMapping(value = "/test", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public void test(HttpSession session, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<String, Object>();
        response = getstatistic(response,session,request);
        int a=1;
    }

    /**
     * 统计 1.12---1.21 符合条件的，21：00-----09：00：00 没有付费，第二天用次数进行咨询的次数
     *
     * @param params
     * @param session
     * @param request
     * @return
     */
    @RequestMapping(value = "/getstatistic", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getstatistic(Map<String, Object> params, HttpSession session, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<String, Object>();
        Date beforeDate = new Date(117, 0, 11);
        Date afterDate = new Date(117, 1, 11);
        boolean boolean1 = true;
        String boolean2 = "";
        //查询符合条件的openId
        Query query = new Query().addCriteria(Criteria.where("title").is("ZXTS_YJSD").and("create_date").gte(beforeDate).andOperator(Criteria.where("create_date").lte(afterDate)));
        List<MongoLog> mongoLogs = logMongoDBService.queryList(query);
        Assert.notNull(mongoLogs, "consultRecordMongoVos 不能为空！！！！");
        for (MongoLog mongoLog : mongoLogs) {
            //遍历：判断有没有付费
            PayRecord record = new PayRecord();
            record.setOpenId(mongoLog.getOpen_id());
            Date payDate = mongoLog.getCreate_date();
            Date date1 = new Date(payDate.getYear(),payDate.getMonth(),payDate.getDate(),21,0,0);
            Date date2 = new Date(payDate.getYear(),payDate.getMonth(),payDate.getDate()+1,7,0,0);
            record.setPayDate(date1);
            record.setReceiveDate(date2);
            record.setStatus("success");
            boolean1 = payRecordService.selectUserPayInfo(record);
            //判断第二天有没有用咨询次数进行咨询
            ConsultSession consultSession = new ConsultSession();
            Date date3 = new Date(payDate.getYear(),payDate.getMonth(),payDate.getDate()+1,21,0,0);
            consultSession.setCreateTime(date2);
            consultSession.setUpdateTime(date3);
            consultSession.setChargeType("mt");
            consultSession.setUserId(mongoLog.getUserId());
            List<ConsultSession> consultSessions = consultSessionService.selectBySelectiveOrder(consultSession);
            if(consultSessions!=null && consultSessions.size()>0){
                boolean2 ="来咨询";
            }else{
                boolean2 ="没来咨询";
            }
            //插入statistic表中
            ConsultStatisticVo consultStatisticVo = new ConsultStatisticVo();
            consultStatisticVo.setMaxMoney(mongoLog.getOpen_id());
            consultStatisticVo.setMonthSatisfiedDegree(boolean1==true?"支付": "未支付");
            consultStatisticVo.setMinMoney(boolean2);
            consultStatisticVo.setCreateDate(mongoLog.getCreate_date());
            consultStatisticService.insertSelective(consultStatisticVo);
        }

        return response;
    }


}
