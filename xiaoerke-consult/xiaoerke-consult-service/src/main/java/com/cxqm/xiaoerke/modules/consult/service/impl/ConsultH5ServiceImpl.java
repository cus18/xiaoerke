package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.cxqm.xiaoerke.common.utils.OSSObjectTool;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultH5Service;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangzhongge on 2016-4-21.
 */
@Service
@Transactional(readOnly = false)
public class ConsultH5ServiceImpl implements ConsultH5Service {

    @Autowired
    ConsultRecordService consultRecordService;

    @Override
    public  HashMap<String, Object>  uploadH5MediaFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("data") String data) throws UnsupportedEncodingException {

        HashMap<String,Object> response = new HashMap<String, Object>();

        if(file !=null && ! file.isEmpty()){
            String fileName = file.getOriginalFilename();
            Map<String, Object> msgMap;
            try {
                msgMap = (Map<String, Object>) JSON.parse(URLDecoder.decode(data, "utf-8"));
                String fileType = String.valueOf(msgMap.get("fileType"));
                String senderId = String.valueOf(msgMap.get("senderId"));
                try {
                    InputStream inputStream = file.getInputStream();
                    String key = null ;
                    String aliUrl = null ;
                    if(fileType.equalsIgnoreCase("image") ||fileType.equalsIgnoreCase("1")){
                         key = OSSObjectTool.uploadFileInputStream(fileName, file.getSize(), inputStream, OSSObjectTool.BUCKET_CONSULT_PIC);
                         aliUrl = "http://"+OSSObjectTool.BUCKET_CONSULT_PIC+".oss-cn-beijing.aliyuncs.com/"+fileName;
                    }else if(fileType.equalsIgnoreCase("voice") ||fileType.equalsIgnoreCase("2")){
                         key = OSSObjectTool.uploadFileInputStream(fileName, file.getSize(), inputStream, OSSObjectTool.BUCKET_CONSULT_PIC);
                         aliUrl = "http://"+OSSObjectTool.BUCKET_CONSULT_PIC+".oss-cn-beijing.aliyuncs.com/"+fileName;
                    }else{
                         key = OSSObjectTool.uploadFileInputStream(fileName, file.getSize(), inputStream, OSSObjectTool.BUCKET_CONSULT_PIC);
                         aliUrl = "http://"+OSSObjectTool.BUCKET_CONSULT_PIC+".oss-cn-beijing.aliyuncs.com/"+fileName;
                    }
                    System.out.println("OSSObjectTool key2 ="+key.toString());
                    System.out.println("OSSObjectTool aliUrl2 =" + aliUrl);

                    if(StringUtils.isNotNull(key)){
                        response.put("fileType",fileType);
                        response.put("senderId",StringUtils.isNotNull(senderId)?senderId:"null");
                        response.put("showFile",aliUrl);
                        response.put("status","success");
                    }else{
                        response.put("fileType",fileType);
                        response.put("senderId",senderId);
                        response.put("status","failure");
                    }

                    ConsultRecordMongoVo consultRecordMongoVo = new ConsultRecordMongoVo();
                    consultRecordMongoVo.setSessionId(senderId);
                    List<ConsultRecordMongoVo> consultRecordMongoVos = consultRecordService.findUserConsultInfoBySessionId(consultRecordMongoVo);
                    response.put("status","senderId is null !!!");
                    if(consultRecordMongoVos!=null && consultRecordMongoVos.size()>0){
                        consultRecordMongoVo = consultRecordMongoVos.get(0);
                        consultRecordMongoVo.setType(fileType);
                        consultRecordMongoVo.setMessage(fileName);
                        consultRecordMongoVo.setOpercode("sender");
                        consultRecordService.saveConsultRecord(consultRecordMongoVo);
                        response.put("status","success");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    response.put("status","failure");
                }
            } catch (JSONException ex) {
                response.put("status","failure");
            }
        }
        return response;
    }
}
