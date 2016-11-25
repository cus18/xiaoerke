package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.common.utils.OSSObjectTool;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.consult.dao.SendMindCouponDao;
import com.cxqm.xiaoerke.modules.consult.entity.SendMindCouponVo;
import com.cxqm.xiaoerke.modules.consult.service.SendMindCouponService;
import com.cxqm.xiaoerke.modules.consult.service.SendMindCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.util.List;

/**
 * 文案配置实现类
 * @author sunxiao
 * 2016-10-31
 */

@Service
public class SendMindCouponServiceImpl implements SendMindCouponService {

    @Autowired
    SendMindCouponDao sendMindCouponDao;

    @Override
    public List<SendMindCouponVo> findSendMindCouponByInfo(SendMindCouponVo vo) {
        return sendMindCouponDao.findSendMindCouponByInfo(vo);
    }

    @Override
    public String saveSendMindCoupon(SendMindCouponVo vo) {
        if(StringUtils.isNotNull(vo.getImage())){
            uploadArticleImage("sendMindCoupon" + vo.getId(), vo.getImage());
        }
        if(StringUtils.isNotNull(vo.getId()+"")){
            sendMindCouponDao.updateSendMindCoupon(vo);
            return "修改成功";

        }else{
            sendMindCouponDao.saveSendMindCoupon(vo);
            return "保存成功";
        }
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

    @Override
    public void deleteSendMindCoupon(SendMindCouponVo vo) {
        sendMindCouponDao.deleteSendMindCoupon(vo);
    }
}
