package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultH5Service;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.DoctorInfoService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import org.json.JSONObject;
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
import java.util.Map;

/**
 * Created by jiangzhongge on 2016-4-21.
 */
@Service
@Transactional(readOnly = false)
public class ConsultH5ServiceImpl implements ConsultH5Service {

    @Autowired
    ConsultRecordService consultRecordService;

    @Autowired DoctorInfoService doctorInfoService;

    @Autowired SystemService systemService ;

    private SessionRedisCache sessionRedisCache = SpringContextHolder.getBean("sessionRedisCacheImpl");
    @Override
    public  HashMap<String, Object>  uploadH5MediaFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("data") String data) throws UnsupportedEncodingException {

        HashMap<String,Object> response = new HashMap<String, Object>();

        if(file !=null && ! file.isEmpty()){
            String fileName = file.getOriginalFilename();
            if(fileName.contains(".jpg")||fileName.contains(".png")||fileName.contains(".jpeg") || fileName.contains(".bmp") ||fileName.contains(".gif")){
                fileName = IdGen.uuid() + fileName.substring(fileName.lastIndexOf("."),fileName.length());
            }else{
                fileName = IdGen.uuid();
            }
            Map<String, Object> msgMap;
            InputStream inputStream = null;
            InputStream inputStream_ws = null;
            try {
                msgMap = (Map<String, Object>) JSON.parse(URLDecoder.decode(data, "utf-8"));
                String fileType = String.valueOf(msgMap.get("fileType"));
                String senderId = String.valueOf(msgMap.get("senderId"));
                int sessionId = Integer.valueOf(String.valueOf(msgMap.get("sessionId")));
                Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
                try {
                    inputStream = file.getInputStream();
                    inputStream_ws = file.getInputStream();
                    String key = null ;
                    String aliUrl = null ;
                    RichConsultSession consultSession = sessionRedisCache.getConsultSessionBySessionId(sessionId);
                    if(("image").equalsIgnoreCase(fileType) ||("1").equalsIgnoreCase(fileType)){
                        User user = systemService.getUserById(senderId);
                        if(user != null){
                            String userType = user.getUserType();
                            if(StringUtils.isNotNull(userType) && ("distributor".equalsIgnoreCase(userType) || "consultDoctor".equalsIgnoreCase(userType))){
                                if("wxcxqm".equalsIgnoreCase(consultSession.getSource())){
                                    String upLoadUrl = "https://api.weixin.qq.com/cgi-bin/media/upload";
                                    key = OSSObjectTool.uploadFileInputStream(fileName, file.getSize(), inputStream, OSSObjectTool.BUCKET_DOCTOR_PIC);
                                    aliUrl = "http://"+OSSObjectTool.BUCKET_DOCTOR_PIC+".oss-cn-beijing.aliyuncs.com/"+fileName;
                                    JSONObject jsonObject = WechatUtil.uploadNoTextMsgToWX((String) userWechatParam.get("token"), upLoadUrl, fileType, fileName, inputStream_ws);
                                    String  media_id= jsonObject.optString("media_id");
                                    response.put("WS_File",media_id);
                                    response.put("source",consultSession.getSource());
                                }else{
                                    key = OSSObjectTool.uploadFileInputStream(fileName, file.getSize(), inputStream, OSSObjectTool.BUCKET_DOCTOR_PIC);
                                    aliUrl = "http://"+OSSObjectTool.BUCKET_DOCTOR_PIC+".oss-cn-beijing.aliyuncs.com/"+fileName;
                                    response.put("source",consultSession.getSource());
                                }
                            }else{
                                key = OSSObjectTool.uploadFileInputStream(fileName, file.getSize(), inputStream, OSSObjectTool.BUCKET_CONSULT_PIC);
                                aliUrl = "http://"+OSSObjectTool.BUCKET_CONSULT_PIC+".oss-cn-beijing.aliyuncs.com/"+fileName;
                                response.put("source",consultSession.getSource());
                            }
                        }else{
                            key = OSSObjectTool.uploadFileInputStream(fileName, file.getSize(), inputStream, OSSObjectTool.BUCKET_CONSULT_PIC);
                            aliUrl = "http://"+OSSObjectTool.BUCKET_CONSULT_PIC+".oss-cn-beijing.aliyuncs.com/"+fileName;
                            response.put("source",consultSession.getSource());
                        }


                    }else if(("voice").equalsIgnoreCase(fileType) || ("2").equalsIgnoreCase(fileType)){
                        key = OSSObjectTool.uploadFileInputStream(fileName, file.getSize(), inputStream, OSSObjectTool.BUCKET_CONSULT_PIC);
                        aliUrl = "http://"+OSSObjectTool.BUCKET_CONSULT_PIC+".oss-cn-beijing.aliyuncs.com/"+fileName;
                        response.put("source",consultSession.getSource());
                    }else{
                        key = OSSObjectTool.uploadFileInputStream(fileName, file.getSize(), inputStream, OSSObjectTool.BUCKET_CONSULT_PIC);
                        aliUrl = "http://"+OSSObjectTool.BUCKET_CONSULT_PIC+".oss-cn-beijing.aliyuncs.com/"+fileName;
                        response.put("source",consultSession.getSource());
                    }

                    if(StringUtils.isNotNull(key)){
                        response.put("fileType",fileType);
                        response.put("senderId",StringUtils.isNotNull(senderId)?senderId:"null");
                        response.put("showFile",aliUrl);
                        response.put("status","success");
                    }else{
                        response.put("fileType", fileType);
                        response.put("senderId", senderId);
                        response.put("status","failure");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    response.put("status","failure");
                }
            } catch (JSONException ex) {
                response.put("status","failure");
            }finally{
                try {
                    if(inputStream != null){
                        inputStream.close();
                    }
                    if(inputStream_ws != null){
                        inputStream_ws.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }
}
