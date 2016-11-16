package com.cxqm.xiaoerke.modules.wisdom.web;

import com.cxqm.xiaoerke.common.utils.ImgUtils;
import com.cxqm.xiaoerke.modules.consult.entity.OperationPromotionTemplateVo;
import com.cxqm.xiaoerke.modules.consult.service.OperationPromotionService;
import com.cxqm.xiaoerke.modules.consult.service.OperationPromotionTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.TagExtraInfo;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by wangbaowei on 16/11/13.
 */

@Controller
public class PicSpreadController {
    @Autowired
    private OperationPromotionTemplateService operationPromotionTemplateService;

    @RequestMapping(value = "/picSpread/creaet", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String,Object> picSpreadCreat(@RequestBody Map<String, Object> params) {
        String babyName =(String) params.get("babyName");
        Map<String,Object> resultMap = new HashMap<String, Object>();
        //随机找一个样板,将名字嵌入
        OperationPromotionTemplateVo templateVo = new OperationPromotionTemplateVo();
        templateVo.setType("pictureTransmission");
        List<OperationPromotionTemplateVo> spreadList= operationPromotionTemplateService.findOperationPromotionTemplateList(templateVo);
        Integer ri = (int)(Math.random()*spreadList.size());
        String picUrl = "http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/pictureTransmission";
        String picImg = "";
        for(int i =0;i<spreadList.size();i++){
            if(i == ri){
                picUrl += spreadList.get(i).getId();
                picImg = ImgUtils.createStringMark(babyName,picUrl,spreadList.get(i).getInfo1()+(spreadList.get(i).getInfo2() == null ?"":";"+spreadList.get(i).getInfo2()));
                break;
            }
        }
        resultMap.put("picImg",picImg);
        return  resultMap;
    }
}
