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
        return operationPromotionTemplateDao.findTemplateListByInfo(vo);
    }

    @Override
    public void operationPromotionTemplateOper(OperationPromotionTemplateVo vo) {
        if(StringUtils.isNotNull(vo.getId()+"")){
            operationPromotionTemplateDao.updateByPrimaryKeySelective(vo);
        }else{
            vo.setType("pictureTransmission");
            operationPromotionTemplateDao.insertSelective(vo);
        }
        if(StringUtils.isNotNull(vo.getImage())){
            uploadArticleImage("pictureTransmission"+vo.getId(),vo.getImage());
        }
    }

    @Override
    public void deleteOperationPromotionTemplate(OperationPromotionTemplateVo vo) {
        operationPromotionTemplateDao.deleteByPrimaryKey(vo.getId()+"");
    }

    private void uploadArticleImage(String id , String src) {
        try {
            File file = new File(System.getProperty("user.dir").replace("bin", "webapps") + URLDecoder.decode(src, "utf-8"));
            FileInputStream inputStream = new FileInputStream(file);
            long length = file.length();
            //上传图片至阿里云
            OSSObjectTool.uploadFileInputStream(id, length, inputStream, OSSObjectTool.BUCKET_ARTICLE_PIC);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
