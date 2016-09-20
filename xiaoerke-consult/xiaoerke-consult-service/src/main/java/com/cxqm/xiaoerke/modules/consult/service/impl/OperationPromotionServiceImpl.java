package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultDoctorInfoDao;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultDoctorTimeGiftDao;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultSessionPropertyDao;
import com.cxqm.xiaoerke.modules.consult.dao.OperationPromotionDao;
import com.cxqm.xiaoerke.modules.consult.entity.*;
import com.cxqm.xiaoerke.modules.consult.service.ConsultDoctorInfoService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.consult.service.OperationPromotionService;
import com.cxqm.xiaoerke.modules.interaction.service.PatientRegisterPraiseService;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.service.UserInfoService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

/**
 * 咨询医生信息实现类
 * @author sunxiao
 * 2016-04-26
 */

@Service
public class OperationPromotionServiceImpl implements OperationPromotionService {

    @Autowired
    OperationPromotionDao operationPromotionDao;

    @Override
    public List<OperationPromotionVo> findKeywordRoleList(OperationPromotionVo vo) {
        return operationPromotionDao.findKeywordRoleList(vo);
    }

    @Override
    public void saveKeywordRole(OperationPromotionVo vo) {
        operationPromotionDao.saveKeywordRole(vo);
    }

    @Override
    public Map getAllRoleListByKeyword() {
        Map<String,OperationPromotionVo> resultMap = new HashMap<String, OperationPromotionVo>();
        List<OperationPromotionVo> mapList = operationPromotionDao.getAllRoleListByKeyword();
        for (OperationPromotionVo vo : mapList) {
            resultMap.put(vo.getKeyword(),vo);
        }
        return resultMap;
    }
}
