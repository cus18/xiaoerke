package com.cxqm.xiaoerke.modules.umbrella.serviceimpl;

import com.cxqm.xiaoerke.modules.sys.dao.UtilDao;
import com.cxqm.xiaoerke.modules.sys.entity.ValidateBean;
import com.cxqm.xiaoerke.modules.umbrella.dao.BabyUmbrellaInfoDao;
import com.cxqm.xiaoerke.modules.umbrella.service.BabyUmbrellaInfoThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by guozengguang on 16/7/6.
 */

@Service
@Transactional(readOnly = false)
public class BabyUmbrellaInfoThirdPartyServiceImpl implements BabyUmbrellaInfoThirdPartyService {

    @Autowired
    private BabyUmbrellaInfoDao babyUmbrellaInfoDao;

    @Autowired
    private UtilDao utilDao;

    /**
     *根据手机号查询该用户是否购买宝护伞
     */
    public boolean ifBuyUmbrella(Map<String,Object> map){
        List<Map<String,Object>> list = babyUmbrellaInfoDao.getifBuyUmbrellaInfo(map);
        if(list!=null && list.size()>0){
            return true;
        }
        return false;
    }

    /**
     *根据手机号查询该用户是否已经关注平台
     */
    public Map<String,Object> getStatusByPhone(Map<String,Object> map){
        Map<String,Object> statusMap = babyUmbrellaInfoDao.getStatusByPhone(map);
        return statusMap;
    }

    /**
     *
     */
    public ValidateBean getIdentifying(String phoneNum){
        return utilDao.getIdentifying(phoneNum);
    }
}
