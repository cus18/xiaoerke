package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.activity.service.OlyGamesService;
import com.cxqm.xiaoerke.modules.consult.entity.BabyCoinRecordVo;
import com.cxqm.xiaoerke.modules.consult.entity.BabyCoinVo;
import com.cxqm.xiaoerke.modules.consult.service.BabyCoinService;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    /**
     * 邀请卡生成页面
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/createInviteCard", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> createInviteCard(HttpSession session, HttpServletRequest request) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        String openId = WechatUtil.getOpenId(session, request);

        BabyCoinVo babyCoinVo = getBabyCoin(response, openId);

        String userQRCode = olyGamesService.getUserQRCode(babyCoinVo.getMarketer());//二维码
        String headImgUrl = olyGamesService.getWechatMessage(openId);//头像
        response.put("userQRCode", userQRCode);
        response.put("headImgUrl", headImgUrl);

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
     * 获取宝宝币，以及详细记录
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/test", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public void test(HttpSession session, HttpServletRequest request) {
        babyCoinInit(session, request);
//        getBabyCoinInfo(session, request);
    }

}
