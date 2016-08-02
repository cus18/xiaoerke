package com.cxqm.xiaoerke.modules.olyGames.web;

import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGameDetailVo;
import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGamesVo;
import com.cxqm.xiaoerke.modules.activity.service.OlyGamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


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

        String marketer = "";//根据openid获取邀请码
        String userQRCode = olyGamesService.getUserQRCode(marketer);//二维码

        return response;
    }


}