package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.cxqm.xiaoerke.common.utils.OSSObjectTool;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.utils.WechatUtil;
import com.cxqm.xiaoerke.modules.consult.dao.OperationPromotionDao;
import com.cxqm.xiaoerke.modules.consult.entity.*;
import com.cxqm.xiaoerke.modules.consult.service.OperationPromotionService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.UUIDUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 咨询医生信息实现类
 * @author sunxiao
 * 2016-04-26
 */

@Service
public class OperationPromotionServiceImpl implements OperationPromotionService {

    @Autowired
    OperationPromotionDao operationPromotionDao;

    @Autowired
    SystemService systemService;

    @Override
    public List<OperationPromotionVo> findKeywordRoleList(OperationPromotionVo vo) {
        List<OperationPromotionVo> list = operationPromotionDao.findKeywordRoleList(vo);
        return list;
    }

    @Override
    public List<OperationPromotionVo> findKeywordRoleByInfo(OperationPromotionVo vo) {
        return operationPromotionDao.findKeywordRoleByInfo(vo);
    }

    @Override
    public void saveKeywordRole(OperationPromotionVo vo) {
        String roleid = vo.getRoleId();
        if(StringUtils.isNotNull(vo.getImgPath())){
            vo.setImgPath(StringEscapeUtils.unescapeHtml4(vo.getImgPath()));
            String id = uploadPic(vo);//保存图片
            if(id==null){
                //return null;
            }
        }
        String[] keywords = vo.getKeyword().split(" ");//保存关键字
        vo.setRoleId(UUIDUtil.getUUID());
        for(String keyword : keywords){
            if(StringUtils.isNotNull(keyword)){
                vo.setKeyword(keyword);
                operationPromotionDao.saveKeywordRole(vo);
            }
        }
        if(StringUtils.isNotNull(roleid)){
            vo.setRoleId(roleid);
            List<OperationPromotionVo> list = operationPromotionDao.findKeywordRoleList(vo);
            if(list.size()!=0){
                operationPromotionDao.deleteKeywordRole(list.get(0));
            }
        }
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

    private String uploadPic(OperationPromotionVo vo){//id为mediaId
        String content = vo.getImgPath();
        Matcher m = Pattern.compile("src.*?/>").matcher(content);
        String replyPicId = "";
        while(m.find()){
            String[] imgpath = m.group().split("\"");
            if(!imgpath[1].contains("aliyuncs")){
                try {
                    String id = uploadImageToWxAndAliyun(imgpath[1]);
                    if(id == null){
                        return null;
                    }
                    content = content.replace(imgpath[1].substring(0,imgpath[1].lastIndexOf("/")+1),
                            "http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/");
                    content = content.replace(imgpath[1].substring(imgpath[1].lastIndexOf("/")+1,imgpath[1].length()), id);
                    replyPicId = replyPicId + id + ",";
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                replyPicId = replyPicId + imgpath[1].split("/")[imgpath[1].split("/").length-1] + ",";
            }
        }
        vo.setImgPath(content);
        vo.setReplyPicId(replyPicId);
        return content;
    }

    private String uploadImageToWxAndAliyun(String src) throws Exception{
        File file = new File(System.getProperty("user.dir").replace("bin", "webapps")+URLDecoder.decode(src, "utf-8"));
        FileInputStream inputStream = new FileInputStream(file);
        FileInputStream fis = new FileInputStream(file);
        long length = file.length();
        Map<String, Object> wechatMap = systemService.getWechatParameter();
        String media_id = WechatUtil.uploadPermanentMedia((String) wechatMap.get("token"), file, "image", "");//上传图片到微信服务器
        if(media_id == null){
            return null;
        }
        //上传图片至阿里云
        OSSObjectTool.uploadFileInputStream(media_id, length, fis, OSSObjectTool.BUCKET_ARTICLE_PIC);
        return media_id;
    }

    @Override
    public void deleteKeywordRole(OperationPromotionVo vo) {
        operationPromotionDao.deleteKeywordRole(vo);
    }
}
