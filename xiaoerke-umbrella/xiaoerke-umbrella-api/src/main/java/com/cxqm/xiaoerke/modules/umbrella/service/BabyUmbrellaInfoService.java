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
     * 查询激活人数
     * @param map
     * @return
     */
    int getUmbrellaActivationCount(Map<String, Object> map);

    /**
     * 查询未激活人数
     * @param map
     * @return
     */
    int getUmbrellaNotActivationCount(Map<String, Object> map);

    /**
     * 查询激活家庭人数
     * @param map
     * @return
     */
    int getUmbrellaActivationFamilyPeopleCount(Map<String, Object> map);

    int saveOpenidToMongoDB(UmbrellaMongoDBVo entity);

    List<UmbrellaMongoDBVo> getUmbrellaMongoDBVoList(Query query);




    /**
     * 查询免费激活
     * @param map
     * @return
     */
    int getUmbrellaFreeActivationCount(Map<String, Object> map);

    /**
     * 查询免费未激活
     * @param map
     * @return
     */
    int getUmbrellaFreeNotActivationCount(Map<String, Object> map);
}
