package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.account.service.impl.PayRecordServiceImpl;
import com.cxqm.xiaoerke.modules.activity.service.OlyGamesService;
import com.cxqm.xiaoerke.modules.consult.entity.BabyCoinVo;
import com.cxqm.xiaoerke.modules.consult.service.*;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.WechatMessageUtil;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "util")
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
    SendMindCouponService sendMindCouponService;

    @Autowired
    private ConsultMemberRedsiCacheService consultMemberRedsiCacheService;

    /**
     * 给用户推消息
     *
     * @param request
     * @param
     * @return
     */
    /**
     *业务服务提醒

     xuOOz6ifH6PYJlktzn6Qbj6OODMCNoQt-iD3trAqnmA

     亲爱的，你的宝宝币即将到期啦！

     主题：宝宝币到期服务提醒

     提醒内容：2017年11月30日前获得的宝宝币还有4天到期

     截止日期：2017年1月15日

     还没用完的抓紧咨询医生， 或者点击前往兑换优惠券哦~
     */
    @RequestMapping(value = "/sendMessage", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> sendMessage(@RequestBody Map<String, Object> params, HttpSession session, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<String, Object>();
        Map parameter = systemService.getWechatParameter();
        String token = (String) parameter.get("token");
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        BabyCoinVo babyCoinVo = new BabyCoinVo();
        List<BabyCoinVo> babyCoinVos = babyCoinService.selectSubBabyCoin(babyCoinVo);
        String title = "亲爱的，你的宝宝币即将到期啦！";
        String keyword1 = "宝宝币到期服务提醒";
        String keyword2 = "2017年11月30日前获得的宝宝币还有4天到期";
        String keyword3 = "2017年1月15日";
        String remark = "还没用完的抓紧咨询医生， 或者点击前往兑换优惠券哦~";
        String url = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=48";
        if(babyCoinVos!=null && babyCoinVos.size()>0){
            for(int i = 100;i<=babyCoinVos.size();i++){
                if(babyCoinVos.get(i)!=null){
                    BabyCoinVo vo = babyCoinVos.get(i);
                    if(StringUtils.isNotNull(vo.getOpenId())){
                        if(vo.getOpenId().equals("oogbDwD_2BTQpftPu9QClr-mCw7U"))
                        WechatMessageUtil.templateModel(title, keyword1, keyword2, keyword3, "", remark, token, url, vo.getOpenId(), sysPropertyVoWithBLOBsVo.getTemplateIdYWFWTX());
//                        babyCoinService.updateBabyCoinByOpenId(vo);
                    }
                }
            }
        }
        return response;
    }

}
