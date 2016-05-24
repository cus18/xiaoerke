package com.cxqm.xiaoerke.modules.umbrella.serviceimpl;

import com.cxqm.xiaoerke.modules.umbrella.dao.BabyUmbrellaInfoDao;
import com.cxqm.xiaoerke.modules.umbrella.entity.BabyUmbrellaInfo;
import com.cxqm.xiaoerke.modules.umbrella.service.BabyUmbrellaInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by feibendechayedan on 16/5/20.
 */

@Service
@Transactional(readOnly = false)
public class BabyUmbrellaInfoServiceImpl implements BabyUmbrellaInfoService {

    @Autowired
    private BabyUmbrellaInfoDao babyUmbrellaInfoDao;

    @Override
    public int saveBabyUmberllaInfo(BabyUmbrellaInfo babyUmbrellaInfo) {
        return babyUmbrellaInfoDao.saveBabyUmberllaInfo(babyUmbrellaInfo);
    }

    @Override
    public int updateBabyUmberllaInfo(BabyUmbrellaInfo babyUmbrellaInfo) {
        return babyUmbrellaInfoDao.updateBabyUmberllaInfo(babyUmbrellaInfo);
    }

    @Override
    public List<Map<String, Object>> getBabyUmbrellaInfo(Map<String, Object> map) {
        return babyUmbrellaInfoDao.getBabyUmbrellaInfo(map);
    }
}
