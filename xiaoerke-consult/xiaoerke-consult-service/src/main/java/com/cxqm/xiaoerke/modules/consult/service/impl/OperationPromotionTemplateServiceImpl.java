package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.common.utils.OSSObjectTool;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.consult.dao.OperationPromotionDao;
import com.cxqm.xiaoerke.modules.consult.dao.OperationPromotionTemplateDao;
import com.cxqm.xiaoerke.modules.consult.entity.OperationPromotionTemplateVo;
import com.cxqm.xiaoerke.modules.consult.entity.OperationPromotionVo;
import com.cxqm.xiaoerke.modules.consult.service.OperationPromotionService;
import com.cxqm.xiaoerke.modules.consult.service.OperationPromotionTemplateService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.UUIDUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 运营推广实现类
 * @author sunxiao
 * 2016-11-10
 */

@Service
public class OperationPromotionTemplateServiceImpl implements OperationPromotionTemplateService {

    @Autowired
    OperationPromotionTemplateDao operationPromotionTemplateDao;


    @Override
    public List<OperationPromotionTemplateVo> findOperationPromotionTemplateList(OperationPromotionTemplateVo vo) {
        return null;
    }

    @Override
    public void operationPromotionTemplateOper(OperationPromotionTemplateVo vo) {

    }

    @Override
    public void deleteOperationPromotionTemplate(OperationPromotionTemplateVo vo) {

    }
}
