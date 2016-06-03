package com.cxqm.xiaoerke.modules.umbrella.service;

import com.cxqm.xiaoerke.modules.sys.entity.BabyBaseInfoVo;
import com.cxqm.xiaoerke.modules.umbrella.entity.BabyUmbrellaInfo;
import com.cxqm.xiaoerke.modules.umbrella.entity.UmbrellaFamilyInfo;

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
}
