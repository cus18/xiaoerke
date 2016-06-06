package com.cxqm.xiaoerke.modules.mutualHelp.serviceImpl;

import com.cxqm.xiaoerke.modules.mutualHelp.dao.DoctorMutualHelpDao;
import com.cxqm.xiaoerke.modules.mutualHelp.service.DoctorMutualHelpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
@Service
@Transactional(readOnly = false)
public class DoctorMutualHelpServiceImpl implements DoctorMutualHelpService {

    @Autowired
    DoctorMutualHelpDao dao;

    @Override
    public Integer getCount() {
        return dao.getCount();
    }
}
