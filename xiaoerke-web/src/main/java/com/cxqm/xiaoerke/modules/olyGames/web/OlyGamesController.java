package com.cxqm.xiaoerke.modules.olyGames.web;

import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGameDetailVo;
import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGamesVo;
import com.cxqm.xiaoerke.modules.activity.service.OlyGamesService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.umbrella.service.BabyUmbrellaInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


/**
 * 奥运 Controller
 *
 * @author deliang
 * @version 2016-08-01
 */
@Controller
@RequestMapping(value = "olympicBaby")
public class OlyGamesController extends BaseController {

    @Autowired
    private OlyGamesService olyGamesService;

    @Autowired
    private BabyUmbrellaInfoService babyUmbrellaInfoSerivce;

    private SessionRedisCache sessionRedisCache = SpringContextHolder.getBean("sessionRedisCacheImpl");

    /**
     * 获取某个游戏玩的次数
     * input:{openid:"fwefewfewf",gameLevel:3}
     * result: {gamePlayingTimes:2}
     ***/
    @RequestMapping(value = "/gameScore/GetGamePlayingTimes", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> GetGamePlayingTimes(@RequestBody Map<String, Object> params) {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        String openId = (String) params.get("openid");
        Integer gameLevel = Integer.valueOf((String) params.get("gameLevel"));
        OlyBabyGamesVo olyBabyGamesVo = new OlyBabyGamesVo();
        olyBabyGamesVo.setOpenId(openId);
        OlyBabyGamesVo resultvo = olyGamesService.selectByOlyBabyGamesVo(olyBabyGamesVo);
        if (gameLevel == 1) {
            responseMap.put("gamePlayingTimes", resultvo.getLevel1CurrentTimes());
        } else if (gameLevel == 2) {
            responseMap.put("gamePlayingTimes", resultvo.getLevel2CurrentTimes());
        } else if (gameLevel == 3) {
            responseMap.put("gamePlayingTimes", resultvo.getLevel3CurrentTimes());
        } else if (gameLevel == 4) {
            responseMap.put("gamePlayingTimes", resultvo.getLevel4CurrentTimes());
        } else if (gameLevel == 5) {
            responseMap.put("gamePlayingTimes", resultvo.getLevel5CurrentTimes());
        } else if (gameLevel == 6) {
            responseMap.put("gamePlayingTimes", resultvo.getLevel6CurrentTimes());
        }
        return responseMap;
    }

    /**
     * 根据积分，进行奖品抽奖
     * sunxiao
     * input:{openid:"fwefewfewf"}
     * result: {leftTimes:2，prizeInfo:[{name:"电饭煲"},{describe:"电饭煲"},{XXX:"电饭煲"}]}
     * leftTimes为剩余的抽奖次数，如果为-1，表示积分不够抽奖，抽奖失败
     ***/
    @RequestMapping(value = "/GetGameScorePrize",method = {RequestMethod.POST,RequestMethod.GET})
    public synchronized
    @ResponseBody
    Map<String,Object> GetGameScorePrize(@RequestBody Map<String, Object> params){
        Map<String,Object> responseMap = new HashMap<String, Object>();
        String openId = (String)params.get("openid");
        int random = new Random().nextInt(100);
        Map<String, Object> umbrellaMap=new HashMap<String, Object>();
        umbrellaMap.put("openid",openId);
        List<Map<String, Object>> umbrellaList = babyUmbrellaInfoSerivce.getBabyUmbrellaInfo(umbrellaMap);
        String today = DateUtils.DateToStr(new Date(), "date");
        Map<String, Object> prizeMap=new HashMap<String, Object>();
        prizeMap.put("prizeDate",today);
        List<Map<String, Object>> prizeList = olyGamesService.getOlyGamePrizeList(prizeMap);
        int start = 0;
        for(Map<String, Object> tempMap : prizeList){
            int end = start+(Integer)tempMap.get("probability");
            if(random>=start && random<end){
                responseMap.put("prizeOrder",tempMap.get("prizeOrder"));
                responseMap.put("prizeName",tempMap.get("prizeName"));
                break;
            }
            start = end;
        }
        return responseMap;
    }

    /**
     * 获取用户抽到的奖品列表
     * sunxiao
     * input:{openid:"fwefewfewf"}
     * result: {prizeList:[{prizeName:"电饭煲",XXX:"XXXXX"},{prizeName:"电饭煲"},{prizeName:"电饭煲"}]}
     ***/
    @RequestMapping(value = "/GetUserPrizeList",method = {RequestMethod.POST,RequestMethod.GET})
    public synchronized
    @ResponseBody
    Map<String,Object> GetUserPrizeList(@RequestBody Map<String, Object> params){
        Map<String,Object> responseMap = new HashMap<String, Object>();
        String openId = (String)params.get("openid");
        int random = new Random().nextInt(100);

        return responseMap;
    }

    /**
     * 保存用户领取奖品的地址
     * sunxiao
     * input:{openid:"fwefewfewf"}
     * result: {addressName:"海淀区",code:"100053","phone":"13601025662","userName":"赵得良"}
     ***/
    @RequestMapping(value = "/SaveUserAddress",method = {RequestMethod.POST,RequestMethod.GET})
    public synchronized
    @ResponseBody
    Map<String,Object> SaveUserAddress(@RequestBody Map<String, Object> params){
        Map<String,Object> responseMap = new HashMap<String, Object>();
        String openId = (String)params.get("openid");
        int random = new Random().nextInt(100);

        return responseMap;
    }

    /**
     * 将某关的游戏积分存入后台
     * input:{openid:"fwefewfewf",gameLevel:3,gameScore:80}
     * result: {result:"success"}
     ***/
    @RequestMapping(value = "/gameScore/SaveGameScore", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> SaveGameScore(@RequestBody Map<String, Object> params) {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        String openId = (String) params.get("openid");
        Integer gameLevel = Integer.valueOf((String) params.get("gameLevel"));
        Float gameScore = Float.valueOf((String) params.get("gameScore"));

        OlyBabyGamesVo olyBabyGamesVo = new OlyBabyGamesVo();
        olyBabyGamesVo.setOpenId(openId);
        olyBabyGamesVo.setGameScore(gameScore);
        int updateFlag = olyGamesService.updateOlyBabyGamesByOpenId(olyBabyGamesVo);

        OlyBabyGameDetailVo olyBabyGameDetailVo = new OlyBabyGameDetailVo();
        olyBabyGameDetailVo.setGameScore(gameScore);
        olyBabyGameDetailVo.setGameLevel(gameLevel);
        olyBabyGameDetailVo.setCreateBy(openId);
        olyBabyGameDetailVo.setOpenId(openId);
        olyBabyGameDetailVo.setCreateTime(new Date());
        int insertFlag = olyGamesService.insertOlyBabyGameDetailVo(olyBabyGameDetailVo);

        responseMap.put("result", updateFlag > 0 ? "success" : "failure");
        responseMap.put("result", insertFlag > 0 ? "success" : "failure");

        return responseMap;
    }


    /**
     * 获取用户的游戏积分
     * input: {openid:"fwefwefewfw"}
     * result: {gameScore:8888}
     ***/
    @RequestMapping(value = "/firstPage/GetUserGameScore", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> GetUserGameScore(@RequestBody Map<String, Object> params) {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        String openId = (String) params.get("openid");
        OlyBabyGamesVo olyBabyGamesVo = new OlyBabyGamesVo();
        olyBabyGamesVo.setOpenId(openId);
        OlyBabyGamesVo resultvo = olyGamesService.selectByOlyBabyGamesVo(olyBabyGamesVo);
        responseMap.put("gameScore",resultvo.getGameScore());
        return responseMap;
    }

    /**
     * 用户进入游戏界面初始化
     * input: {openid:"fwefwefewfw"}
     * result: {result:"success"}
     ***/
    @RequestMapping(value = "/firstPage/InitOlyGame", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> InitOlyGame(@RequestBody Map<String, Object> params) {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        String openId = (String) params.get("openid");

        OlyBabyGamesVo olyBabyGamesVo = new OlyBabyGamesVo();
        olyBabyGamesVo.setOpenId(openId);
        olyBabyGamesVo.setCreateTime(new Date());
        olyBabyGamesVo.setGameLevel(1);

        int insertFlag = olyGamesService.insertOlyBabyGamesVo(olyBabyGamesVo);

//        responseMap.put("gameScore",resultvo.getGameScore());
        return responseMap;
    }

    /**
     * 获取邀请卡
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/GetInviteCard",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Map<String,Object> GetInviteCard(@RequestBody Map<String, Object> params){
        Map<String,Object> response = new HashMap<String, Object>();
        String openId = (String)params.get("openid");
        String marketer = (String)params.get("marketer");
        if(!StringUtils.isNotNull(marketer)){
            marketer = olyGamesService.getMarketerByOpenid(openId);//根据openid获取邀请码
        }
        String userQRCode = olyGamesService.getUserQRCode(marketer);//二维码

        String headImgUrl = olyGamesService.getWechatMessage(openId);//头像

        //生成图片暂存路径
        String outPath = System.getProperty("user.dir").replace("bin", "uploadImg")+"\\image\\"+new Date().getTime()+".png";

        //生成邀请卡图片
        ImgUtils.composePic(headImgUrl, userQRCode, outPath, 71, 231,185,500);

        //上传图片
        ImgUtils.uploadImage("olympicBaby_invite_"+openId+".png", outPath);

        String path = "http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/"+"olympicBaby_invite_"+openId+".png";
        response.put("path",path);


        File wxFile=new File(outPath);
        InputStream is = null;
        try {
            is = new FileInputStream(wxFile);
            String upLoadUrl = "https://api.weixin.qq.com/cgi-bin/media/upload";
            Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
            org.json.JSONObject jsonObject = WechatUtil.uploadNoTextMsgToWX((String) userWechatParam.get("token"), upLoadUrl, "image", wxFile.getName(), is);

            WechatUtil.sendMsgToWechat((String) userWechatParam.get("token"),openId,"新游戏需要邀请从未关注过“宝大夫”的好友助力方可解锁开启，赶紧把下方图片分享出去吧，大奖在等你哦！");
            WechatUtil.sendNoTextMsgToWechat((String) userWechatParam.get("token"),openId,(String) jsonObject.get("media_id"),1);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }



}