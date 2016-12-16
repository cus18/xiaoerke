package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.modules.account.entity.PayRecord;
import com.cxqm.xiaoerke.modules.account.service.impl.PayRecordServiceImpl;
import com.cxqm.xiaoerke.modules.activity.service.OlyGamesService;
import com.cxqm.xiaoerke.modules.consult.entity.BabyCoinRecordVo;
import com.cxqm.xiaoerke.modules.consult.entity.BabyCoinVo;
import com.cxqm.xiaoerke.modules.consult.entity.SendMindCouponVo;
import com.cxqm.xiaoerke.modules.consult.service.*;
import com.cxqm.xiaoerke.modules.sys.entity.SysPropertyVoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.sys.service.SysPropertyServiceImpl;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.sys.utils.WechatMessageUtil;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@RequestMapping(value = "babyCoin")
public class BabyCoinController {

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
     * 邀请卡生成页面
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/createInviteCard", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> createInviteCard(@RequestBody Map<String, Object> params,HttpSession session, HttpServletRequest request) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        String openId = WechatUtil.getOpenId(session, request);//"oogbDwD_2BTQpftPu9QClr-mCw7U"
        String oldOpenId = String.valueOf(params.get("oldOpenId"));
        String marketer = String.valueOf(params.get("marketer"));
        BabyCoinVo babyCoinVo = getBabyCoin(response, oldOpenId);
        String userQRCode = olyGamesService.getUserQRCode(marketer);//二维码
        String headImgUrl = olyGamesService.getWechatMessage(oldOpenId);//头像
        response.put("userQRCode", userQRCode);
        if(StringUtils.isNotNull(headImgUrl)){
            response.put("headImgUrl", headImgUrl);
        }else {
            response.put("headImgUrl", "http://img5.imgtn.bdimg.com/it/u=1846948884,880298315&fm=21&gp=0.jpg");
        }
        response.put("babyCoinVo", babyCoinVo);

        return response;
    }

    /**
     * 初始化生成宝宝币接口，selfCenter.html页面初始化就调用
     *
     * @param request
     * @param
     * @return response {"userStatus":"oldUser"} oldUser 老用户  newUser 新用户
     */
    @RequestMapping(value = "/babyCoinInit", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> babyCoinInit(HttpSession session, HttpServletRequest request) {
        HashMap<String, Object> response = new HashMap<String, Object>();

        String openId = WechatUtil.getOpenId(session, request);
//        String openId = "oogbDwD_2BTQpftPu9QClr-mCs7U";
        BabyCoinVo babyCoinVo = getBabyCoin(response, openId);
        BabyCoinRecordVo babyCoinRecordVo = new BabyCoinRecordVo();
        babyCoinRecordVo.setOpenId(openId);
        List<BabyCoinRecordVo> babyCoinRecordVos = babyCoinService.selectByBabyCoinRecordVo(babyCoinRecordVo);
        for (BabyCoinRecordVo vo : babyCoinRecordVos) {
            vo.setDate(DateUtils.DateToStr(vo.getCreateTime(), "monthDate"));
            if(vo.getBalance() > 0){
                vo.setStrBalance("+"+vo.getBalance());
            }else{
                vo.setStrBalance(String.valueOf(vo.getBalance()));
            }
        }
        response.put("babyCoinRecordVos", babyCoinRecordVos);
        response.put("babyCoinVo", babyCoinVo);

        return response;
    }

    private BabyCoinVo getBabyCoin(HashMap<String, Object> response, String openId) {
        BabyCoinVo babyCoinVo = new BabyCoinVo();
        babyCoinVo.setOpenId(openId);
        babyCoinVo = babyCoinService.selectByBabyCoinVo(babyCoinVo);
        SysWechatAppintInfoVo sysWechatAppintInfoVo = new SysWechatAppintInfoVo();
        sysWechatAppintInfoVo.setOpen_id(openId);
        SysWechatAppintInfoVo wechatAttentionVo = wechatAttentionService.findAttentionInfoByOpenId(sysWechatAppintInfoVo);
        if (babyCoinVo == null || babyCoinVo.getCash() == null) {//新用户，初始化宝宝币
            synchronized (this) {
                babyCoinVo = new BabyCoinVo();
                babyCoinVo.setCash(0l);
                babyCoinVo.setCreateBy(openId);
                babyCoinVo.setCreateTime(new Date());
                babyCoinVo.setOpenId(openId);
                if(wechatAttentionVo != null && wechatAttentionVo.getWechat_name()!=null){
                    babyCoinVo.setNickName(wechatAttentionVo.getWechat_name());
                }
                BabyCoinVo lastBabyCoinUser = new BabyCoinVo();
                lastBabyCoinUser.setCreateTime(new Date());
                lastBabyCoinUser = babyCoinService.selectByBabyCoinVo(lastBabyCoinUser);
                if (lastBabyCoinUser == null || lastBabyCoinUser.getMarketer() == null) {
                    babyCoinVo.setMarketer("110000000");//初始值
                } else {
                    babyCoinVo.setMarketer(String.valueOf(Integer.valueOf(lastBabyCoinUser.getMarketer()) + 1));
                }
                babyCoinService.insertBabyCoinSelective(babyCoinVo);
            }
            response.put("userStatus", "newBabyCoinUser");
        } else {
            response.put("userStatus", "oldBabyCoinUser");
        }
        return babyCoinVo;
    }


    /**
     * 个人中心，获取宝宝币信息
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/getBabyCoinInfo", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getBabyCoinInfo(HttpSession session, HttpServletRequest request) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        String openId = WechatUtil.getOpenId(session,request);//"oogbDwD_2BTQpftPu9QClr-mCw7U";
        //宝宝币余额
        BabyCoinVo babyCoinVo = new BabyCoinVo();
        babyCoinVo.setOpenId(openId);
        babyCoinVo = babyCoinService.selectByBabyCoinVo(babyCoinVo);
        response.put("babyCoinCash", babyCoinVo.getCash());
        return response;
    }

    /**
     * 扣除宝宝币操作
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/minusOrAddBabyCoin", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> minusOrAddBabyCoin(HttpSession session, HttpServletRequest request) {
        LogUtils.saveLog("minusOrAddBabyCoin"+WechatUtil.getOpenId(session,request));
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        HashMap<String, Object> response = new HashMap<String, Object>();
        String openId = WechatUtil.getOpenId(session,request);//"oogbDwD_2BTQpftPu9QClr-mCw7U";
        //宝宝币余额
        int flag = 0;
        BabyCoinVo babyCoinVo = new BabyCoinVo();
        babyCoinVo.setOpenId(openId);
        babyCoinVo = babyCoinService.selectByBabyCoinVo(babyCoinVo);
        if(babyCoinVo.getCash() > Long.valueOf(sysPropertyVoWithBLOBsVo.getOnceConsultNeedBabyCoin())){
            babyCoinVo.setCash(babyCoinVo.getCash() - Long.valueOf(sysPropertyVoWithBLOBsVo.getOnceConsultNeedBabyCoin()));
            flag = babyCoinService.updateBabyCoinByOpenId(babyCoinVo);
        }
        if(flag > 0){
            response.put("status", "success");
            LogUtils.saveLog("insertBabyCoinRecord",openId);
            //支付记录
            Integer sessionId = sessionRedisCache.getSessionIdByUserId(WechatUtil.getOpenId(session, request));
            BabyCoinRecordVo babyCoinRecordVo = new BabyCoinRecordVo();
            babyCoinRecordVo.setBalance(-99);
            babyCoinRecordVo.setCreateBy(openId);
            babyCoinRecordVo.setCreateTime(new Date());
            babyCoinRecordVo.setSource("consultPay");
            babyCoinRecordVo.setSessionId(sessionId);
            babyCoinRecordVo.setOpenId(openId);
            babyCoinService.insertBabyCoinRecord(babyCoinRecordVo);
            LogUtils.saveLog("insertBabyCoinRecord sessionId=" + sessionId);

            consultSessionPropertyService.addPermTimes(openId);
            Map parameter = systemService.getWechatParameter();
            String token = (String) parameter.get("token");
//            WechatUtil.sendMsgToWechat(token, openId, "【支付成功通知】你已在宝大夫成功支付24小时咨询服务费，感谢你的信任和支持！\n----------------\n把您的问题发送给医生，立即开始咨询吧");


            PayRecord payRecord = new PayRecord();
            payRecord.setUserId(openId);
            String payRecordId = IdGen.uuid();
            payRecord.setId(payRecordId);
            payRecord.setOpenId(openId);
            payRecord.setOrderId(String.valueOf(sessionId));
            payRecord.setAmount(990f);
            payRecord.setPayType("selfAccount");
            payRecord.setStatus("success");
            payRecord.setFeeType("doctorConsultPay_babyCoin");
            payRecord.setPayDate(new Date());
            payRecord.setReceiveDate(new Date());
            payRecord.setCreatedBy(openId);
            LogUtils.saveLog("babyCoinPay:" + payRecordId);//用户自身余额支付
            payRecordService.insertPayInfo(payRecord);

//            限时咨询增加会员时长
//                   mysql 增加会员记录,延长redis的时间
            consultMemberRedsiCacheService.payConsultMember(openId,sysPropertyVoWithBLOBsVo.getConsultMemberTime(),"0",token);

            //更改支付状态
            LogUtils.saveLog("宝宝币支付 sessionId = " + sessionId, openId);
            HttpRequestUtil.wechatpost("http://s132.baodf.com/angel/consult/wechat/notifyPayInfo2Distributor?openId=" + openId,
                    "openId=" + openId);
        }else{
            response.put("status", "failure");
        }

        return response;
    }


    /**
     * 获取宝宝币，以及详细记录
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/test", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public void test(HttpSession session, HttpServletRequest request) {
//        babyCoinInit(session, request);
//        getBabyCoinInfo(session, request);
        sendMindBabyCoinUser();
    }




    /**
     * 用宝宝币兑换优惠券
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/exchangeCoupon", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> exchangeCoupon(@RequestBody Map<String, Object> params,HttpSession session, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<String, Object>();
        String openId = WechatUtil.getOpenId(session, request);
        if(null == openId){
            response.put("status", "openidLoss");
        }
        Integer id = (Integer)params.get("id");
        SendMindCouponVo sendMindInfo = sendMindCouponService.getSendMindCouponInof(id);
        Integer couponCoin = Integer.parseInt(sendMindInfo.getName())*10;
        //宝宝币余额
        int flag = 0;
        BabyCoinVo babyCoinVo = new BabyCoinVo();
        babyCoinVo.setOpenId(openId);
        babyCoinVo = babyCoinService.selectByBabyCoinVo(babyCoinVo);
        if (babyCoinVo.getCash() > couponCoin) {
            babyCoinVo.setCash(babyCoinVo.getCash() - couponCoin);
            flag = babyCoinService.updateBabyCoinByOpenId(babyCoinVo);
        }
        if (flag > 0) {
            response.put("link",  sendMindInfo.getLink());
            response.put("status", "success");
        } else {
            response.put("status", "failure");
        }
        //支付记录
        LogUtils.saveLog("exchangeCoupon" + WechatUtil.getOpenId(session, request));
        BabyCoinRecordVo babyCoinRecordVo = new BabyCoinRecordVo();
        babyCoinRecordVo.setBalance(couponCoin);
        babyCoinRecordVo.setCreateBy(openId);
        babyCoinRecordVo.setCreateTime(new Date());
        babyCoinRecordVo.setSource("exchangeCoupon");
        babyCoinRecordVo.setOpenId(openId);
        babyCoinService.insertBabyCoinRecord(babyCoinRecordVo);
        return  response;
    }

    @RequestMapping(value = "sendMindCouponList")
    public
    @ResponseBody
    Map<String,Object> sendMindCouponList() {
        Map<String,Object> resultMap = new HashMap<String, Object>();
        List<SendMindCouponVo> list = sendMindCouponService.findSendMindCouponByInfo(null);
        resultMap.put("mindCoupon",list);
        return resultMap;
    }

    @RequestMapping(value = "sendMindBabyCoinUser")
    public
    @ResponseBody
    void sendMindBabyCoinUser() {
        Map parameter = systemService.getWechatParameter();
        String token = (String) parameter.get("token");
        SysPropertyVoWithBLOBsVo sysPropertyVoWithBLOBsVo = sysPropertyService.querySysProperty();
        BabyCoinVo babyCoinVo = new BabyCoinVo();
        babyCoinVo.setCash(100l);
        babyCoinVo.setCreateTime(new Date());
        List<BabyCoinVo> babyCoinVos = babyCoinService.selectListByBabyCoinVo(babyCoinVo);
        String title = "天降红包啦！！ 快进门抢！";
        String keyword1 = "双12宝宝币限量兑换优惠券";
        String keyword2 = "亲爱的，感谢您对宝大夫的支持，奉上4张价值不等的优惠券，点击即可兑换。\n";
        String keyword3 = "2016年12月25日";
        String remark = "数量有限，快来点击兑换吧！";
        String url = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com//keeper/wechatInfo/getUserWechatMenId?url=48";
        if(babyCoinVos!=null && babyCoinVos.size()>0){
            for(int i = 100;i<=200;i++){
                if(babyCoinVos.get(i)!=null){
                    BabyCoinVo vo = babyCoinVos.get(i);
                    if(StringUtils.isNotNull(vo.getOpenId())){
                        WechatMessageUtil.templateModel(title, keyword1, keyword2, keyword3, "", remark, token, url, vo.getOpenId(), sysPropertyVoWithBLOBsVo.getTemplateIdYWFWTX());
                    }
                }
            }
        }

    }

}
