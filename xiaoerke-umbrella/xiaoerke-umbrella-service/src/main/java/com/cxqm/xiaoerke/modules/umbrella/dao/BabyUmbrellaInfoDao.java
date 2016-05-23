package com.cxqm.xiaoerke.modules.umbrella.dao;

import com.cxqm.xiaoerke.common.persistence.annotation.MyBatisDao;
import com.cxqm.xiaoerke.modules.umbrella.entity.BabyUmbrellaInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by feibendechayedan on 16/5/20.
 */
@MyBatisDao
public interface BabyUmbrellaInfoDao {

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
    List<Map<String,Object>>  getBabyUmbrellaInfo();

}
