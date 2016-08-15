package com.cxqm.xiaoerke.modules.olyGames.web;

import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGameDetailVo;
import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGamesVo;
import com.cxqm.xiaoerke.modules.activity.service.OlyGamesService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.sys.entity.WechatBean;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.ChangzhuoMessageUtil;
import com.cxqm.xiaoerke.modules.umbrella.service.BabyUmbrellaInfoService;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import jxl.Sheet;
import jxl.Workbook;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


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

    @Autowired
    private SystemService systemService;

    @Autowired
    private WechatAttentionService wechatAttentionService;

    private static ExecutorService threadExecutor = Executors.newSingleThreadExecutor();

    /**
     * 获取某个游戏玩的次数
     * input:{openid:"fwefewfewf",gameLevel:3}
     * result: {gamePlayingTimes:2}
     ***/
    @RequestMapping(value = "/test", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    void test() {

    }



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
        int gameLevel = Integer.parseInt((String)params.get("gameLevel")) ;
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
        }else {
            responseMap.put("gamePlayingTimes", 0);
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
    @RequestMapping(value = "/gameScore/GetGameScorePrize", method = {RequestMethod.POST, RequestMethod.GET})
    public synchronized
    @ResponseBody
    Map<String, Object> GetGameScorePrize(@RequestBody Map<String, Object> params) {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        String openId = (String) params.get("openid");
        OlyBabyGamesVo olyBabyGamesVo = new OlyBabyGamesVo();
        olyBabyGamesVo.setOpenId(openId);
        OlyBabyGamesVo vo = olyGamesService.selectByOlyBabyGamesVo(olyBabyGamesVo);
        Map<String, Object> umbrellaMap = new HashMap<String, Object>();
        umbrellaMap.put("openid", openId);
        List<Map<String, Object>> umbrellaList = babyUmbrellaInfoSerivce.getBabyUmbrellaInfo(umbrellaMap);
        boolean umbrellaUser = false;
        if(umbrellaList.size()>0){//如果是保护伞用户则不能抽中保护伞
            if("success".equals(umbrellaList.get(0).get("pay_result"))||umbrellaList.get(0).get("pay_result")==null){
                umbrellaUser = true;
            }
        }
        String prizes = vo.getPrize();//获取用户的抽奖信息
        int gameLevel = vo.getGameLevel();
        prizes = prizes==null?"":prizes;
        OlyBabyGamesVo olyVo = new OlyBabyGamesVo();
        olyVo.setOpenId(openId);
        boolean thanks = false;
        if((umbrellaUser&&("".equals(prizes)||"4".equals(prizes)))||(!umbrellaUser&&!prizes.contains(","))){//是保护伞用户
            int random = new Random().nextInt(100);
            String today = DateUtils.DateToStr(new Date(), "date");
            Map<String, Object> prizeMap = new HashMap<String, Object>();
            prizeMap.put("prizeDate", today);
            List<Map<String, Object>> prizeList = olyGamesService.getOlyGamePrizeList(prizeMap);
            int start = 0;
            int prizeOrder = 3;
            for (Map<String, Object> tempMap : prizeList) {
                int end = start + (Integer) tempMap.get("probability");
                if (random >= start && random < end) {
                    prizeOrder = (Integer) tempMap.get("prizeOrder");
                    responseMap.put("prizeNumber", tempMap.get("prizeNumber"));
                    if ((Integer) tempMap.get("prizeNumber") <= 0) {//如果抽到的奖品个数为0，则返回谢谢参与
                        thanks = true;
                        break;
                    }
                    if (gameLevel < (Integer) tempMap.get("levelLimit")) {//等级不够，不能抽到该奖品
                        thanks = true;
                        break;
                    }
                    if(4 == prizeOrder && umbrellaUser){//加入保护伞的用户抽中保护伞
                        thanks = true;
                        break;
                    }
                    if ((4 == prizeOrder && "4".equals(prizes)) || (4 != prizeOrder && !"4".equals(prizes) && !"".equals(prizes))) {//抽中保护伞但已经抽过保护伞，或已经抽中过b组奖品
                        thanks = true;
                        break;
                    }
                    responseMap.put("prizeOrder",tempMap.get("prizeOrder"));
                    responseMap.put("prizeName",tempMap.get("prizeName"));
                    responseMap.put("prizeLink",tempMap.get("prizeLink"));
                    responseMap.put("postage",tempMap.get("postage"));
                    break;
                }
                start = end;
            }
            if (!thanks && responseMap.get("prizeOrder") == null) {//没抽中奖品
                thanks = true;
            }
            if(!thanks){//抽到奖品
                if("".equals(prizes)){
                    prizes = responseMap.get("prizeOrder").toString();
                }else{
                    prizes = prizes + "," + responseMap.get("prizeOrder");
                }
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("prizeOrder", responseMap.get("prizeOrder"));
                param.put("prizeNumber", (Integer) responseMap.get("prizeNumber") - 1);
                param.put("prizeDate", today);
                olyGamesService.updateOlyGamePrizeInfo(param);//更新奖品数量
            }
            olyVo.setPrize(prizes);
        }else{//抽中两次的只能显示谢谢参与
            thanks = true;
        }
        if(thanks){
            responseMap.put("prizeOrder",3);
            responseMap.put("prizeName","谢谢参与");
        }
        olyVo.setGameScore((float) -80);
        olyGamesService.updateOlyBabyGamesByOpenId(olyVo);//更新用户抽奖信息
        return responseMap;
    }

    /**
     * 获取最近日期前5名用户抽到的奖品列表
     * sunxiao
     * input:{openid:"fwefewfewf"}
     * result: {prizeList:[{prizeName:"电饭煲",XXX:"XXXXX"},{prizeName:"电饭煲"},{prizeName:"电饭煲"}]}
     ***/
    @RequestMapping(value = "/gameScore/GetUserPrizeList",method = {RequestMethod.POST,RequestMethod.GET})
    public
    @ResponseBody
    Map<String,Object> GetUserPrizeList(@RequestBody Map<String, Object> params){
        Map<String,String> allPrizesMap = new HashMap<String, String>();//奖品列表
        String today = DateUtils.DateToStr(new Date(), "date");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("prizeDate", today);
        List<Map<String, Object>> allPrizeList = olyGamesService.getOlyGamePrizeList(param);
        for(Map<String, Object> temp : allPrizeList){
            allPrizesMap.put(temp.get("prizeOrder").toString(),(String)temp.get("prizeName"));
        }
        Map<String,Object> responseMap = new HashMap<String, Object>();
        List<OlyBabyGamesVo> list = olyGamesService.getUserPrizeList();//取日期最近的5个用户的奖品列表
        List<Map<String, Object>> prizeList = new ArrayList<Map<String, Object>>();
        for(OlyBabyGamesVo vo : list){
            String[] prizes = vo.getPrize().split(",");
            String nickName = vo.getNickName();
            StringBuffer sb = new StringBuffer("");
            for(String prize:prizes){
                if("4".equals(prize)){
                    continue;
                }
                sb.append(allPrizesMap.get(prize)+"、");
            }
            Map<String, Object> prizeMap = new HashMap<String, Object>();
            String headImg = null;
            try{
                headImg = olyGamesService.getWechatMessage(vo.getOpenId());
            }catch (Exception e){
                System.out.println(e);
            }

            prizeMap.put("nickName", nickName);
            prizeMap.put("headImg", headImg==null?"http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fpic%2Fa_04.png":headImg);
            prizeMap.put("prizeName", sb.toString().substring(0,sb.toString().length()-1));
            prizeList.add(prizeMap);
        }
        responseMap.put("prizeList", prizeList);
        return responseMap;
    }

    /**
     * 保存用户领取奖品的地址
     * sunxiao
     * input:{openid:"fwefewfewf"}
     * result: {addressName:"海淀区",code:"100053","phone":"13601025662","userName":"赵得良"}
     ***/
    @RequestMapping(value = "/gameScore/SaveUserAddress",method = {RequestMethod.POST,RequestMethod.GET})
    public
    @ResponseBody
    Map<String,Object> SaveUserAddress(@RequestBody Map<String, Object> params){
        Map<String,Object> responseMap = new HashMap<String, Object>();
        String openId = (String)params.get("openid");
        String address = (String)params.get("address");
        OlyBabyGamesVo param = new OlyBabyGamesVo();
        param.setOpenId(openId);
        param.setAddress(address);
        olyGamesService.updateOlyBabyGamesByOpenId(param);
        return responseMap;
    }

    /**
     * 获取用户抽到的奖品
     * sunxiao
     * input:{openid:"fwefewfewf"}
     * result: {prizeList:[{prizeName:"电饭煲",XXX:"XXXXX"},{prizeName:"电饭煲"},{prizeName:"电饭煲"}]}
     ***/
    @RequestMapping(value = "/gameScore/GetUserPrizes",method = {RequestMethod.POST,RequestMethod.GET})
    public
    @ResponseBody
    Map<String,Object> GetUserPrizes(@RequestBody  Map<String, Object> params){
        Map<String,Object> responseMap = new HashMap<String, Object>();
        String openId = (String)params.get("openid");
        OlyBabyGamesVo olyBabyGamesVo = new OlyBabyGamesVo();
        olyBabyGamesVo.setOpenId(openId);
        OlyBabyGamesVo vo = olyGamesService.selectByOlyBabyGamesVo(olyBabyGamesVo);
        if(vo!=null){
            if(StringUtils.isNotNull(vo.getPrize())){
                String[] prizes = vo.getPrize().split(",");
                List<Map<String,Object>> prizeList = new ArrayList<Map<String, Object>>();
                for(String temp : prizes){
                    Map<String,Object> prizeMap = new HashMap<String, Object>();
                    Map<String,Object> param = new HashMap<String, Object>();
                    param.put("prizeDate",DateUtils.DateToStr(new Date(),"date"));
                    param.put("prizeOrder",temp);
                    List<Map<String,Object>> pList = olyGamesService.getOlyGamePrizeList(param);
                    prizeMap.put("prizeName",pList.get(0).get("prizeName"));
                    prizeMap.put("prizeLink",pList.get(0).get("prizeLink"));
                    prizeMap.put("prizeOrder",temp);
                    prizeList.add(prizeMap);
                }
                responseMap.put("prizeList", prizeList);
            }
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
    Map<String, Object> SaveGameScore(@RequestBody  Map<String, Object> params) {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        String openId = (String) params.get("openid");
        int gameLevel = Integer.parseInt((String)params.get("gameLevel")) ;
        double gameScore = Double.parseDouble((String) params.get("gameScore"));

        OlyBabyGamesVo olyBabyGamesVo = new OlyBabyGamesVo();
        olyBabyGamesVo.setOpenId(openId);
        olyBabyGamesVo.setGameScore((float)gameScore);
        if (gameLevel == 1) {
            olyBabyGamesVo.setLevel1CurrentTimes(1);
        } else if (gameLevel == 2) {
            olyBabyGamesVo.setLevel2CurrentTimes(1);
        } else if (gameLevel == 3) {
            olyBabyGamesVo.setLevel3CurrentTimes(1);
        } else if (gameLevel == 4) {
            olyBabyGamesVo.setLevel4CurrentTimes(1);
        } else if (gameLevel == 5) {
            olyBabyGamesVo.setLevel5CurrentTimes(1);
        } else if (gameLevel == 6) {
            olyBabyGamesVo.setLevel6CurrentTimes(1);
        }
        int updateFlag = olyGamesService.updateOlyBabyGamesByOpenId(olyBabyGamesVo);

        OlyBabyGameDetailVo olyBabyGameDetailVo = new OlyBabyGameDetailVo();
        olyBabyGameDetailVo.setGameScore((float) gameScore);
        olyBabyGameDetailVo.setGameLevel(gameLevel);
        olyBabyGameDetailVo.setCreateBy(openId);
        olyBabyGameDetailVo.setOpenId(openId);
        olyBabyGameDetailVo.setCreateTime(new Date());
        int insertFlag = olyGamesService.insertOlyBabyGameDetailVo(olyBabyGameDetailVo);

        responseMap.put("result", updateFlag > 0 ? "success" : "failure");
        responseMap.put("result", insertFlag > 0 ? "success" : "failure");
        Map<String, Object> playTimes = GetGamePlayingTimes(params);
        responseMap.put("gamePlayingTimes",playTimes.get("gamePlayingTimes"));
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
    Map<String, Object> GetUserGameScore( @RequestBody Map<String, Object> params) {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        String openId = (String) params.get("openid");
        OlyBabyGamesVo olyBabyGamesVo = new OlyBabyGamesVo();
        olyBabyGamesVo.setOpenId(openId);
        OlyBabyGamesVo resultvo = olyGamesService.selectByOlyBabyGamesVo(olyBabyGamesVo);
        responseMap.put("gameScore", resultvo.getGameScore());
        return responseMap;
    }

    /**
     * 获取参加奥运宝宝游戏的总人数
     * inputParam: {}
     * resultParam: {gameMemberNum:8888}
     *
     * @return
     */
    @RequestMapping(value = "/firstPage/GetGameMemberNum", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    HashMap<String, Object> getGameMemberNum() {
        HashMap<String, Object> response = new HashMap<String, Object>();
        int result = olyGamesService.getGameMemberNum();
        if (result != 0) {
            response.put("gameMemberNum", result);
        } else {
            response.put("gameMemberNum", 0);
        }
        return response;
    }

    /**
     * 获取参加用户目前所处的游戏关数,已经要开启下一关，所要进行的操作
     * input: {openid:"fwefwefewfw"}
     * result: {gameLevel:2,gameAction:1，needInviteFriendNum:3,}
     * gameLevel 表示当前所处的管卡,0表示用户处于通关的状态
     * gameAction 1表示需要关注，2表示需要邀请好友
     * needInviteFriendNum 表示还需要邀请加入的好友数
     ***/
    @RequestMapping(value = "/firstPage/GetGameMemberStatus", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    HashMap<String, Object> getGameMemberStatus(@RequestBody Map<String,Object> param) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        int needInviteFriendNum = 0;
        String openid = (String)param.get("openid");
        int gameLevel = 0;
        Object level = param.get("gameLevel");
        if(level !=null && level !=""){
            gameLevel = Integer.valueOf((String)level);
        }
        OlyBabyGamesVo olyBabyGamesVo = new OlyBabyGamesVo();
        olyBabyGamesVo.setOpenId(openid);
        OlyBabyGamesVo olyBabyGamesVo1 = olyGamesService.selectByOlyBabyGamesVo(olyBabyGamesVo);
        if (olyBabyGamesVo1 != null) {
            response.put("status", "success");
            needInviteFriendNum = olyBabyGamesVo1.getInviteFriendNumber();
            if (olyBabyGamesVo1.getGameLevel() == 1) {
                Map userStatus = wechatAttentionService.findLastAttentionStatusByOpenId(openid);
                if(userStatus != null && StringUtils.isNotNull((String)userStatus.get("status")) && "0".equals(userStatus.get("status"))){
                    String nickname = (String)userStatus.get("nickname");
                    if (StringUtils.isNotNull(nickname)) {
                        olyBabyGamesVo1.setNickName(nickname);
                    } else {
                        olyBabyGamesVo1.setNickName("");
                    }
                    olyBabyGamesVo1.setGameScore(null);
                    olyBabyGamesVo1.setLevel1CurrentTimes(null);
                    olyBabyGamesVo1.setLevel2CurrentTimes(null);
                    olyBabyGamesVo1.setLevel3CurrentTimes(null);
                    olyBabyGamesVo1.setLevel4CurrentTimes(null);
                    olyBabyGamesVo1.setLevel5CurrentTimes(null);
                    olyBabyGamesVo1.setLevel6CurrentTimes(null);
                    olyGamesService.updateOlyBabyGamesByOpenId(olyBabyGamesVo1);
                    if(needInviteFriendNum < 1){
                        response.put("gameAction", 2);        //需要关注才能玩下一关
                        response.put("gameLevel", olyBabyGamesVo1.getGameLevel());
                        response.put("needInviteFriendNum", needInviteFriendNum);
                    }
                }else{
                    response.put("gameAction", 1);        //需要关注才能玩下一关
                    response.put("gameLevel", olyBabyGamesVo1.getGameLevel());
                    response.put("needInviteFriendNum", needInviteFriendNum);
                }
            } else {
                int currentLevel = olyBabyGamesVo1.getGameLevel();
                switch (needInviteFriendNum) {
                    case 0:
                        if (gameLevel == 1) {
                            response.put("gameLevel", currentLevel);
                            response.put("gameAction", 0);
                            response.put("needInviteFriendNum", needInviteFriendNum);
                        } else {
                            response.put("gameLevel", currentLevel);
                            response.put("gameAction", 2);
                            response.put("needInviteFriendNum", needInviteFriendNum);
                        }
                        break;
                    case 1:
                        if (gameLevel <= 2) {
                            response.put("gameLevel", currentLevel);
                            response.put("gameAction", 0);
                            response.put("needInviteFriendNum", needInviteFriendNum);
                        } else {
                            response.put("gameLevel", currentLevel);
                            response.put("gameAction", 2);
                            response.put("needInviteFriendNum", needInviteFriendNum);
                        }
                        break;
                    case 2:
                        if (gameLevel <= 2) {
                            response.put("gameLevel", currentLevel);
                            response.put("gameAction", 0);
                            response.put("needInviteFriendNum", needInviteFriendNum);
                        } else {
                            response.put("gameLevel", currentLevel);
                            response.put("gameAction", 2);
                            response.put("needInviteFriendNum", needInviteFriendNum);
                        }
                        break;
                    default:
                        response.put("gameLevel", 0);
                        response.put("gameAction", 0);
                        response.put("needInviteFriendNum", needInviteFriendNum);
                        break;
                }
            }
        } else {
            olyBabyGamesVo.setCreateTime(new Date());
            olyBabyGamesVo.setGameLevel(1);
            olyBabyGamesVo.setGameScore(0.00f);
            olyBabyGamesVo.setInviteFriendNumber(0);
            olyBabyGamesVo.setLevel1CurrentTimes(0);
            olyBabyGamesVo.setLevel2CurrentTimes(0);
            olyBabyGamesVo.setLevel3CurrentTimes(0);
            olyBabyGamesVo.setLevel4CurrentTimes(0);
            olyBabyGamesVo.setLevel5CurrentTimes(0);
            olyBabyGamesVo.setLevel6CurrentTimes(0);
            olyBabyGamesVo.setAddress("");
            olyBabyGamesVo.setNickName("");
            olyBabyGamesVo.setPrize("");
            int result = 0;
            synchronized (this) {
                String num = olyGamesService.getLastNewMarkter();
                String marketer = "150000001";
                int number = 0 ;
                if(StringUtils.isNotNull(num)){
                    number = Integer.valueOf(num) + 1;
                    marketer = String.valueOf(number);
                }
                olyBabyGamesVo.setMarketer(marketer);
                result = olyGamesService.addGamePlayerInfo(olyBabyGamesVo);
            }
            if (result > 0) {
                response.put("status", "addSuccess");
                response.put("gameAction", 0);
                response.put("gameLevel", 1);
                response.put("needInviteFriendNum", 0);
            } else {
                response.put("status", "addFailure");
            }
        }
        return response;
    }

    /**
     * 获取邀请卡
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/GetInviteCard", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> GetInviteCard(@RequestBody Map<String, Object> params) {
        Map<String, Object> response = new HashMap<String, Object>();
        String openId = (String) params.get("openid");
        String marketer = (String) params.get("marketer");

        //生成图片网络路径
        String path = "http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/"+"olympicBaby_invite_"+openId+".png";
        String fileName = new Date().getTime()+"";
        if(!ImgUtils.existHttpPath(path)){
            //生成图片暂存路径
            String outPath = System.getProperty("user.dir").replace("bin", "uploadImg")+"/"+fileName+".png";

            if(!StringUtils.isNotNull(marketer)){
                marketer = olyGamesService.getMarketerByOpenid(openId);//根据openid获取邀请码
            }
            String userQRCode = olyGamesService.getUserQRCode(marketer);//二维码
            String headImgUrl = olyGamesService.getWechatMessage(openId);//头像

            if(headImgUrl==null){//还是没有图片的设为默认图片
                headImgUrl = "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common/baodf_logo.jpg";
            }
            //生成邀请卡图片
            ImgUtils.composePic(headImgUrl, userQRCode, outPath, 71, 231,185,500);

            //上传图片
            ImgUtils.uploadImage("olympicBaby_invite_"+openId+".png", outPath);
        }


        Runnable thread = new inviteCardMessageThread(path,openId);
        threadExecutor.execute(thread);
        response.put("path",path);
        return response;
    }


    public class inviteCardMessageThread extends Thread {
        private String imgPath;
        private String openid;

        public inviteCardMessageThread(String imgPath,String openid) {
          this.imgPath = imgPath;
          this.openid = openid;
        }

        public void run() {
            InputStream is = null;
            InputStream iso = null;
            try {
                URL url = new URL(imgPath);
                HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
                is = httpUrl.getInputStream();
                String localPath = olyGamesService.uploadMedia(is);
                File wxFile=new File(localPath);
                iso = new FileInputStream(wxFile);
                String upLoadUrl = "https://api.weixin.qq.com/cgi-bin/media/upload";
                Map userWechatParam = systemService.getWechatParameter();;
                org.json.JSONObject jsonObject = WechatUtil.uploadNoTextMsgToWX((String) userWechatParam.get("token"), upLoadUrl, "image", wxFile.getName(), iso);
                wxFile.delete();
                WechatUtil.sendMsgToWechat((String) userWechatParam.get("token"),openid,"新游戏需要邀请从未关注过“宝大夫”的好友助力方可解锁开启，赶紧把下方图片分享出去吧，大奖在等你哦！");
                WechatUtil.sendNoTextMsgToWechat((String) userWechatParam.get("token"), openid, (String) jsonObject.get("media_id"), 1);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try {
                    is.close();
                    iso.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}