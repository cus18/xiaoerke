package com.cxqm.xiaoerke.modules.umbrella.service;

import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;
import com.cxqm.xiaoerke.modules.umbrella.entity.BabyUmbrellaInfo;
import com.cxqm.xiaoerke.modules.umbrella.entity.UmbrellaFamilyInfo;
import com.cxqm.xiaoerke.modules.umbrella.entity.UmbrellaMongoDBVo;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;

/**
 * Created by feibendechayedan on 16/5/20.
 */
public interface BabyUmbrellaInfoService {

    /**
     * 添加保障金信息
     * @param babyUmbrellaInfo
     * @return
     */
    int saveBabyUmbrellaInfo(BabyUmbrellaInfo babyUmbrellaInfo);

    /**
     * 更新保障金信息
     * @param babyUmbrellaInfo
     * @return
     */
    int updateBabyUmbrellaInfo(BabyUmbrellaInfo babyUmbrellaInfo);

    /**
     * 更新保障金信息
     * @param babyUmbrellaInfo
     * @return
     */
    int updateBabyUmbrellaInfoById(BabyUmbrellaInfo babyUmbrellaInfo);

    /**
     * 查询全部
     * @return
     */
    List<Map<String,Object>> getBabyUmbrellaInfo(Map<String, Object> map);

    Integer getUserShareNums(String id);


    String getUserQRCode(String id);

    /**
     * 查询参加数量
     * @return
     */
    Integer  getBabyUmbrellaInfoTotal(Map<String, Object> map);


    Map<String,Object> getOpenidStatus(String openid);

    Integer getTotalBabyUmbrellaInfoMoney(Map<String, Object> map);

    int getUmbrellaCount();

    void umbrellaSendWechatMessage();

    int saveFamilyUmbrellaInfo(UmbrellaFamilyInfo vo);

    List<UmbrellaFamilyInfo> getFamilyUmbrellaList(Integer umbrella_id);

    BabyBaseInfoVo getBabyBaseInfo(Integer umbrella_id);

    Map getUmbrellaNum(Map<String, Object> map);


    /**
     * 更新保障金分享信息
     * @param babyUmbrellaInfo
     * @return
     */
    int updateBabyUmbrellaInfoIfShare(BabyUmbrellaInfo babyUmbrellaInfo);


    /**
     * 新版添加保障金信息
     * @param babyUmbrellaInfo
     * @return
     */
    int newSaveBabyUmbrellaInfo(BabyUmbrellaInfo babyUmbrellaInfo);


    /**
     * 根据Openid 删除一个宝护伞信息
     * @param openid
     * @return
     */
    int deleteUmbrellaByOpenid(String openid);


    /**
     * 更新支付信息
     * @param babyUmbrellaInfo
     * @return
     */
    int updateBabyUmbrellaInfoStatus(BabyUmbrellaInfo babyUmbrellaInfo);


    /**
     * 根据宝护伞ID 删除成员信息
     * @param id
     * @return
     */
    int deleteByUmbrellaId(Integer id);


    /**
     * 获取用户第几位加入
     * @param map
     * @return
     */
    int getUmbrellaRank(Map<String, Object> map);

    int saveOpenidToMongoDB(UmbrellaMongoDBVo entity);

    List<UmbrellaMongoDBVo> getUmbrellaMongoDBVoList(Query query);
}
