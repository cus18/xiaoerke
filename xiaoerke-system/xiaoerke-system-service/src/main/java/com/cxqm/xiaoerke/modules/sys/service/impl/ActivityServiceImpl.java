package com.cxqm.xiaoerke.modules.sys.service.impl;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.sys.dao.SysActivityDao;
import com.cxqm.xiaoerke.modules.sys.entity.SysActivityVo;
import com.cxqm.xiaoerke.modules.sys.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private SysActivityDao sysActivityDao;
    /**
     * 判断活动有效期  true 过期  false 没过期
     * 根据 qr_code 查询
     *
     * @return
     */
    @Override
    public Boolean judgeActivityValidity(String qrCode) {
        SysActivityVo searchVo = new SysActivityVo();
        searchVo.setQrCode(qrCode);
        SysActivityVo sysActivityVo = sysActivityDao.selectByQrcode(searchVo);
        String endDate = sysActivityVo.getEndDate().substring(0, sysActivityVo.getEndDate().length() - 2);
        if (sysActivityVo != null) {
            if (StringUtils.isNotNull(endDate)) {
                Long minutes = DateUtils.pastMinutes(DateUtils.parseDate(endDate));
                if (minutes > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public SysActivityVo selectByQrcode(SysActivityVo sysActivityVo){
        return sysActivityDao.selectByQrcode(sysActivityVo);
    }

}
