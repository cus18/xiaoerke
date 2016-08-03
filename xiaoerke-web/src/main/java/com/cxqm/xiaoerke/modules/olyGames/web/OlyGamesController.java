package com.cxqm.xiaoerke.modules.olyGames.web;

import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGameDetailVo;
import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGamesVo;
import com.cxqm.xiaoerke.modules.activity.service.OlyGamesService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.sys.entity.WechatBean;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.umbrella.service.BabyUmbrellaInfoService;
import com.cxqm.xiaoerke.modules.wechat.entity.WechatAttention;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
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

    @Autowired
    private SystemService systemService;

    @Autowired
    private WechatAttentionService wechatAttentionService;

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
    @RequestMapping(value = "/gameScore/GetGameScorePrize",method = {RequestMethod.POST,RequestMethod.GET})
    public synchronized
    @ResponseBody
    Map<String,Object> GetGameScorePrize(@RequestBody Map<String, Object> params){
        Map<String,Object> responseMap = new HashMap<String, Object>();
        String openId = (String)params.get("openid");
        OlyBabyGamesVo olyBabyGamesVo = new OlyBabyGamesVo();
        olyBabyGamesVo.setOpenId(openId);
        OlyBabyGamesVo vo = olyGamesService.selectByOlyBabyGamesVo(olyBabyGamesVo);
        String prizes = vo.getPrize();//获取用户的抽奖信息
        int gameLevel = vo.getGameLevel();
        prizes = prizes==null?"":prizes;
        if(!prizes.contains(",")){//没有得过奖品和只得一次奖品的可以抽奖
            int random = new Random().nextInt(100);
            Map<String, Object> umbrellaMap=new HashMap<String, Object>();
            umbrellaMap.put("openid",openId);
            List<Map<String, Object>> umbrellaList = babyUmbrellaInfoSerivce.getBabyUmbrellaInfo(umbrellaMap);
            String today = DateUtils.DateToStr(new Date(), "date");
            Map<String, Object> prizeMap=new HashMap<String, Object>();
            prizeMap.put("prizeDate",today);
            List<Map<String, Object>> prizeList = olyGamesService.getOlyGamePrizeList(prizeMap);
            int start = 0;
            int prizeOrder = 3;
            for(Map<String, Object> tempMap : prizeList){
                int end = start+(Integer)tempMap.get("probability");
                if(random>=start && random<end){
                    prizeOrder = (Integer)tempMap.get("prizeOrder");
                    responseMap.put("prizeNumber",tempMap.get("prizeNumber"));
                    if((Integer)tempMap.get("prizeNumber")<=0){//如果抽到的奖品个数为0，则返回谢谢参与
                        responseMap.put("prizeOrder",3);
                        responseMap.put("prizeName","谢谢参与");
                        break;
                    }
                    if(gameLevel<(Integer)tempMap.get("levelLimit")){//等级不够，不能抽到该奖品
                        responseMap.put("prizeOrder",3);
                        responseMap.put("prizeName","谢谢参与");
                        break;
                    }
                    if(4 == prizeOrder){//抽中保护伞
                        if(umbrellaList.size()>0){//如果是保护伞用户则不能抽中保护伞
                            if("success".equals(umbrellaList.get(0).get("pay_result"))){
                                responseMap.put("prizeOrder",3);
                                responseMap.put("prizeName","谢谢参与");
                                break;
                            }
                        }
                    }
                    if((4 == prizeOrder&&prizes.contains("4"))||(4 != prizeOrder&&!prizes.contains("4")&&!"".equals(prizes))){//抽中保护伞但已经抽过保护伞，或已经抽中过b组奖品
                        responseMap.put("prizeOrder",3);
                        responseMap.put("prizeName","谢谢参与");
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
            if(responseMap.get("prizeOrder")==null){//没抽中奖品
                responseMap.put("prizeOrder",3);
                responseMap.put("prizeName","谢谢参与");
            }
            if(3 != (Integer) responseMap.get("prizeOrder")){//抽到奖品
                if("".equals(prizes)){
                    prizes = responseMap.get("prizeOrder").toString();
                }else{
                    prizes = prizes + "," + responseMap.get("prizeOrder");
                }
                Map<String,Object> param = new HashMap<String, Object>();
                param.put("prizeOrder",responseMap.get("prizeOrder"));
                param.put("prizeNumber",(Integer)responseMap.get("prizeNumber")-1);
                param.put("prizeDate",today);
                olyGamesService.updateOlyGamePrizeInfo(param);//更新奖品数量
            }else if(3 == (Integer)responseMap.get("prizeOrder")&&prizeOrder == 4&&!prizes.contains("4")){//抽到保护伞且返回谢谢参与且没有抽中过保护伞
                if("".equals(prizes)){
                    prizes = "4";
                }else{
                    prizes = prizes + ",4";
                }
            }
            OlyBabyGamesVo olyVo = new OlyBabyGamesVo();
            olyVo.setOpenId(openId);
            olyVo.setPrize(prizes);
            olyVo.setGameScore(vo.getGameScore()-80);
            olyGamesService.updateOlyBabyGamesByOpenId(olyVo);//更新用户抽奖信息
        }else{//抽中两次的只能显示谢谢参与
            responseMap.put("prizeOrder",3);
            responseMap.put("prizeName","谢谢参与");
        }
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
        Map<String,String> prizesMap = new HashMap<String, String>();//奖品列表
        String today = DateUtils.DateToStr(new Date(), "date");
        Map<String, Object> param=new HashMap<String, Object>();
        param.put("prizeDate", today);
        List<Map<String, Object>> prizeList = olyGamesService.getOlyGamePrizeList(param);
        for(Map<String, Object> temp : prizeList){
            prizesMap.put(temp.get("prizeOrder").toString(),(String)temp.get("prizeName"));
        }
        Map<String,Object> responseMap = new HashMap<String, Object>();
        List<OlyBabyGamesVo> list = olyGamesService.getUserPrizeList();//取日期最近的5个用户的奖品列表
        Map<String,Object> prizeMap = new HashMap<String, Object>();
        for(OlyBabyGamesVo vo : list){
            String[] prizes = vo.getPrize().split(",");
            String nickName = vo.getNickName();
            if(StringUtils.isNull(nickName)){
                WechatAttention wa = wechatAttentionService.getAttentionByOpenId(vo.getOpenId());
                Map parameter = systemService.getWechatParameter();
                String token = (String)parameter.get("token");
                if(wa!=null){
                    if(StringUtils.isNotNull(wa.getNickname())){
                        nickName = wa.getNickname();
                    }else{
                        WechatBean userinfo = WechatUtil.getWechatName(token, vo.getOpenId());
                        nickName = StringUtils.isNotNull(userinfo.getNickname())?userinfo.getNickname():"";
                    }
                }
            }
            StringBuffer sb = new StringBuffer("");
            for(String prize:prizes){
                sb.append(prizesMap.get(prize)).append(",");
            }
            prizeMap.put(nickName,sb.toString());
        }
        responseMap.put("prizeMap",prizeMap);
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
    Map<String,Object> GetUserPrizes(@RequestBody Map<String, Object> params){
        Map<String,Object> responseMap = new HashMap<String, Object>();
        String openId = (String)params.get("openid");
        OlyBabyGamesVo olyBabyGamesVo = new OlyBabyGamesVo();
        olyBabyGamesVo.setOpenId(openId);
        OlyBabyGamesVo vo = olyGamesService.selectByOlyBabyGamesVo(olyBabyGamesVo);
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

        //生成图片网络路径
        String path = "http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/"+"olympicBaby_invite_"+openId+".png";
        if(!ImgUtils.existHttpPath(path)){
            //生成图片暂存路径
            String outPath = System.getProperty("user.dir").replace("bin", "uploadImg")+"\\image\\"+new Date().getTime()+".png";

            if(!StringUtils.isNotNull(marketer)){
                marketer = olyGamesService.getMarketerByOpenid(openId);//根据openid获取邀请码
            }
            String userQRCode = olyGamesService.getUserQRCode(marketer);//二维码
            String headImgUrl = olyGamesService.getWechatMessage(openId);//头像

            //生成邀请卡图片
            ImgUtils.composePic(headImgUrl, userQRCode, outPath, 71, 231,185,500);

            //上传图片
            ImgUtils.uploadImage("olympicBaby_invite_"+openId+".png", outPath);

            File wxFile=new File(outPath);
            InputStream is = null;
            try {
                is = new FileInputStream(wxFile);
                String upLoadUrl = "https://api.weixin.qq.com/cgi-bin/media/upload";
                Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
                org.json.JSONObject jsonObject = WechatUtil.uploadNoTextMsgToWX((String) userWechatParam.get("token"), upLoadUrl, "image", wxFile.getName(), is);

                WechatUtil.sendMsgToWechat((String) userWechatParam.get("token"),openId,"新游戏需要邀请从未关注过“宝大夫”的好友助力方可解锁开启，赶紧把下方图片分享出去吧，大奖在等你哦！");
                WechatUtil.sendNoTextMsgToWechat((String) userWechatParam.get("token"), openId, (String) jsonObject.get("media_id"), 1);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        response.put("path",path);

        return response;
    }


}