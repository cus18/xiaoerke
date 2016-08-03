package com.cxqm.xiaoerke.modules.activity.service;

import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGameDetailVo;
import com.cxqm.xiaoerke.modules.activity.entity.OlyBabyGamesVo;

import java.util.List;
import java.util.Map;

/**
 * Created by deliang on 16/8/01.
 */
public interface OlyGamesService {

    OlyBabyGamesVo selectByOlyBabyGamesVo(OlyBabyGamesVo olyBabyGamesVo);

    /**
     * 获取所有奖品列表
     * sunxiao
     * @return
     */
    List<Map<String,Object>> getOlyGamePrizeList(Map<String, Object> prizeMap);

    /**
     * 更新奖品信息
     * sunxiao
     * @return
     */
    void updateOlyGamePrizeInfo(Map<String, Object> prizeMap);

    /**
     * 获取日期最近的5个用户的奖品列表
     * sunxiao
     * @return
     */
    List<OlyBabyGamesVo> getUserPrizeList();

    int insertOlyBabyGamesVo(OlyBabyGamesVo record);

    int insertOlyBabyGameDetailVo(OlyBabyGameDetailVo record);

    int updateOlyBabyGamesByOpenId(OlyBabyGamesVo record);

    String getUserQRCode(String id);

    String getWechatMessage(String openId);

    String getMarketerByOpenid(String openId);


    int getGameMemberNum();        //查询参与游戏总人数

    int addGamePlayerInfo(OlyBabyGamesVo olyBabyGamesVo);    //新玩家第一次进入游戏页面，添加他的信息

    int getNewAttentionByOpenId(String userId);
}
