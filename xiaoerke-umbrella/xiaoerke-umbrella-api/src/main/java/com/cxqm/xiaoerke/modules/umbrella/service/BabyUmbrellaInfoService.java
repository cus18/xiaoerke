package com.cxqm.xiaoerke.modules.umbrella.service;

import com.cxqm.xiaoerke.modules.umbrella.entity.BabyUmbrellaInfo;

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
    int saveBabyUmberllaInfo(BabyUmbrellaInfo babyUmbrellaInfo);

    /**
     * 更新保障金信息
     * @param babyUmbrellaInfo
     * @return
     */
    int updateBabyUmberllaInfo(BabyUmbrellaInfo babyUmbrellaInfo);

    /**
     * 查询全部
     * @return
     */
    List<Map<String,Object>> getBabyUmbrellaInfo(Map<String, Object> map);

}
