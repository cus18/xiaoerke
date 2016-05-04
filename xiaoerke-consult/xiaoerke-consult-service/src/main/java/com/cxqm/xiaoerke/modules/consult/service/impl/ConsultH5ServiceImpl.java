package com.cxqm.xiaoerke.modules.consult.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultH5Service;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.sys.service.DoctorInfoService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by jiangzhongge on 2016-4-21.
 */
@Service
@Transactional(readOnly = false)
public class ConsultH5ServiceImpl implements ConsultH5Service {

    @Autowired
    ConsultRecordService consultRecordService;

    @Autowired
    DoctorInfoService doctorInfoService;

    private SessionRedisCache sessionRedisCache = SpringContextHolder.getBean("sessionRedisCacheImpl");
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
                Map userWechatParam = sessionRedisCache.getWeChatParamFromRedis("user");
                try {
                    InputStream inputStream = file.getInputStream();
                    String key = null ;
                    String aliUrl = null ;
                    String csName = null;
                    if(fileType.equalsIgnoreCase("image") ||fileType.equalsIgnoreCase("1")){
                        csName = doctorInfoService.getDoctorNameByDoctorId(senderId);
                        if(StringUtils.isNotNull(csName)){
                            Integer sessionId = sessionRedisCache.getSessionIdByUserId(senderId);
                            RichConsultSession consultSession = sessionRedisCache.getConsultSessionBySessionId(sessionId);
                            if("wxcxqm".equalsIgnoreCase(consultSession.getSource())){
                                String upLoadUrl = "https://api.weixin.qq.com/cgi-bin/media/upload";
                                ExecutorService executorService = Executors.newCachedThreadPool();
                                try {
                                    Future ali = executorService.submit(new MyThread("ali",fileName,file.getSize(),inputStream,OSSObjectTool.BUCKET_DOCTOR_PIC));
                                    Future ws = executorService.submit(new MyThread("ws", (String) userWechatParam.get("token"), upLoadUrl, fileType, fileName, inputStream));
                                    response.put("showFile",ali.get());
                                    response.put("WS_File",ws.get());
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }finally {
                                    if(!executorService.isShutdown()){
                                        executorService.shutdown();
                                    }
                                }
                            }else{
                                key = OSSObjectTool.uploadFileInputStream(fileName, file.getSize(), inputStream, OSSObjectTool.BUCKET_DOCTOR_PIC);
                                aliUrl = "http://"+OSSObjectTool.BUCKET_DOCTOR_PIC+".oss-cn-beijing.aliyuncs.com/"+fileName;
                                response.put("showFile",aliUrl);
                            }
                        }else{
                            key = OSSObjectTool.uploadFileInputStream(fileName, file.getSize(), inputStream, OSSObjectTool.BUCKET_CONSULT_PIC);
                            aliUrl = "http://"+OSSObjectTool.BUCKET_CONSULT_PIC+".oss-cn-beijing.aliyuncs.com/"+fileName;
                            response.put("showFile",aliUrl);
                        }

                    }else if(fileType.equalsIgnoreCase("voice") ||fileType.equalsIgnoreCase("2")){
                         key = OSSObjectTool.uploadFileInputStream(fileName, file.getSize(), inputStream, OSSObjectTool.BUCKET_CONSULT_PIC);
                         aliUrl = "http://"+OSSObjectTool.BUCKET_CONSULT_PIC+".oss-cn-beijing.aliyuncs.com/"+fileName;
                         response.put("showFile",aliUrl);
                    }else{
                         key = OSSObjectTool.uploadFileInputStream(fileName, file.getSize(), inputStream, OSSObjectTool.BUCKET_CONSULT_PIC);
                         aliUrl = "http://"+OSSObjectTool.BUCKET_CONSULT_PIC+".oss-cn-beijing.aliyuncs.com/"+fileName;
                         response.put("showFile",aliUrl);
                    }
                    System.out.println("OSSObjectTool key2 ="+key.toString());
                    System.out.println("OSSObjectTool aliUrl2 =" + aliUrl);

                    if(StringUtils.isNotNull(key)){
                        response.put("fileType",fileType);
                        response.put("senderId",StringUtils.isNotNull(senderId)?senderId:"null");
                        response.put("status","success");
                    }else{
                        response.put("fileType", fileType);
                        response.put("senderId", senderId);
                        response.put("status","failure");
                    }

                    /*ConsultRecordMongoVo consultRecordMongoVo = new ConsultRecordMongoVo();
                    consultRecordMongoVo.setSessionId(senderId);
                    List<ConsultRecordMongoVo> consultRecordMongoVos = consultRecordService.findUserConsultInfoBySessionId(consultRecordMongoVo);
                //  response.put("status","senderId is null !!!");
                    if(consultRecordMongoVos!=null && consultRecordMongoVos.size()>0){
                        consultRecordMongoVo = consultRecordMongoVos.get(0);
                        consultRecordMongoVo.setType(fileType);
                        consultRecordMongoVo.setMessage(fileName);
                        consultRecordMongoVo.setOpercode("sender");
                        consultRecordService.saveConsultRecord(consultRecordMongoVo);
                        response.put("status","success");
                    }*/

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

    public static class MyThread implements Callable<String> {
        private String urlStr;
        private long fileSize;
        private String fileName;
        private InputStream inputStream;
        private String filePath;
        private String tokenNum;
        private String upLoadUrl;
        private String fileType;
        public MyThread(String urlStr,String fileName,long fileSize,InputStream inputStream,String filePath){
            this.urlStr = urlStr;
            this.fileName = fileName;
            this.fileSize = fileSize;
            this.inputStream = inputStream;
            this.filePath = filePath;
        }
        public MyThread(String urlStr,String tokenNum,String upLoadUrl,String fileType,String fileName,InputStream inputStream){
            this.urlStr = urlStr;
            this.tokenNum = tokenNum;
            this.upLoadUrl = upLoadUrl;
            this.fileType = fileType;
            this.fileName = fileName;
            this.inputStream = inputStream;
        }
        public MyThread(){

        }
        public MyThread(String urlStr){
            this.urlStr = urlStr;
        }
        @Override
        public String call() throws Exception {
            if("ali".equalsIgnoreCase(urlStr)){
                OSSObjectTool.uploadFileInputStream(fileName, fileSize, inputStream, filePath);
                String aliUrl = "http://"+OSSObjectTool.BUCKET_DOCTOR_PIC+".oss-cn-beijing.aliyuncs.com/"+fileName;
                return aliUrl;
            }else if("ws".equalsIgnoreCase(urlStr)){
                JSONObject jsonObject = WechatUtil.uploadNoTextMsgToWX(tokenNum, upLoadUrl, fileType, fileName, inputStream);
                String  media_id= jsonObject.optString("media_id");
                return media_id;
            }
            return null;
        }
    }
    public static void main(String[] args){
        ExecutorService exec = Executors.newCachedThreadPool();
//       ArrayList<Future<String>> results = new ArrayList<Future<String>>();
        //Future 相当于是用来存放Executor执行的结果的一种容器
        try {
            String[] words = {"ali","ws"};
            ArrayList<Future<String>> results = new ArrayList<Future<String>>();

            for(String word : words){
                Callable callable = new MyThread(word);
                Future future = exec.submit(callable);
                results.add(future);
            }
            System.out.println(results.get(0).get());
            System.out.println(results.get(1).get());
            System.out.println("========================================");
            for(int i=0;i<100;i++){
                Future ali = exec.submit(new MyThread("ali"));
                Future ws = exec.submit(new MyThread("ws"));
                System.out.println(ali.get());
                System.out.println(ws.get());
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        exec.shutdown();
    }
}
