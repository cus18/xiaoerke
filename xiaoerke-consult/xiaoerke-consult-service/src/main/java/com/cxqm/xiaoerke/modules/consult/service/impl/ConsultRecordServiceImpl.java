package com.cxqm.xiaoerke.modules.consult.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.cxqm.xiaoerke.common.utils.ConstantUtil;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.OSSObjectTool;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultRecordDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultSessionStatusVo;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.sys.entity.PaginationVo;
import com.cxqm.xiaoerke.modules.sys.service.MongoDBService;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 咨询服务
 *
 * @author deliang
 * @version 2015-12-09
 */
@Service
@Transactional(readOnly = false)
public class ConsultRecordServiceImpl implements ConsultRecordService{


    @Autowired
    private ConsultRecordDao consultRecordDao;

    @Autowired
    private ConsultRecordMongoDBServiceImpl consultRecordMongoDBService;

    private static ExecutorService threadExecutor = Executors.newSingleThreadExecutor();

    @Override
    public int deleteByPrimaryKey(Long id) {
        return consultRecordDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(ConsultRecordVo consultRecordVo) {
        return consultRecordDao.insert(consultRecordVo);
    }

    @Override
    public int insertConsultRecordBatch(List<ConsultRecordVo> consultRecordVos) {
        return consultRecordDao.insertConsultRecordBatch(consultRecordVos);
    }

    @Override
    public int insertSelective(ConsultRecordVo consultRecordVo) {
        return consultRecordDao.insert(consultRecordVo);
    }

    @Override
    public ConsultRecordVo selectByPrimaryKey(Long id) {
        return consultRecordDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(ConsultRecordVo consultRecordVo) {
        return consultRecordDao.updateByPrimaryKeySelective(consultRecordVo);
    }

    @Override
    public int updateByPrimaryKey(ConsultRecordVo consultRecordVo) {
        return consultRecordDao.updateByPrimaryKey(consultRecordVo);
    }

    @Override
    public PaginationVo<ConsultRecordMongoVo> getRecordDetailInfo(int pageNo, int pageSize, Query query,String recordType) {
        return consultRecordMongoDBService.getRecordDetailInfo(pageNo, pageSize, query, recordType);
    }

    @Override
    public PaginationVo<ConsultSessionStatusVo> getUserMessageList(int pageNo, int pageSize, Query query) {
        return consultRecordMongoDBService.getUserMessageList(pageNo, pageSize, query);
    }

    @Override
    public List<ConsultSessionStatusVo> queryUserMessageList(Query query){
        return consultRecordMongoDBService.queryUserMessageList(query);
    }

    @Override
    public int saveConsultRecord(ConsultRecordMongoVo consultRecordMongoVo) {
        return consultRecordMongoDBService.saveConsultRecord(consultRecordMongoVo);
    }

    public int saveConsultRecordTemporary(ConsultRecordMongoVo consultRecordMongoVo){
        /**1、通过consultRecordMongoVo中的sessionId判断，在ConsultRecordTemporary中是否有此sessionId的记录，如果有sessionId的记录则将
        consultRecordMongoVo的数据，插入到ConsultRecordTemporary中***/
        /**2、如果RichConsultSession中没有sessionId的数据，证明这是一个新来的sessionId会话，做如下判断，
         * （1），consultRecordMongoVo中的userId和csUserId组合查询在RichConsultSession中有记录， 不管多少条，全部删除，将consultRecordMongoVo
         * 数据重新插入；（2）consultRecordMongoVo中的userId和csUserId组合查询在RichConsultSession中没有记录，则直接插入，不需要做任何删除***/

        if(consultRecordMongoVo != null && consultRecordMongoVo.getSessionId()!=null){
            ConsultRecordMongoVo recordMongoVo = consultRecordMongoDBService.findOneConsultRecordTemporary(new Query(Criteria.where("sessionId").
                    is(consultRecordMongoVo.getSessionId())));
            if(recordMongoVo !=null ){
                consultRecordMongoDBService.insertTempRecord(consultRecordMongoVo);
            }else {
                Query query = new Query(Criteria.where("userId").is(consultRecordMongoVo.getUserId()).andOperator(Criteria.where("csUserId").
                        is(consultRecordMongoVo.getCsUserId())));
                ConsultRecordMongoVo resultVo = consultRecordMongoDBService.findOneConsultRecordTemporary(query);
                if(resultVo!=null){
                    consultRecordMongoDBService.deleteConsultRecordTemporary(query);
                }
                consultRecordMongoDBService.insertTempRecord(consultRecordMongoVo);
            }
        }
        return 0;
    }


    @Override
    public ConsultRecordMongoVo findOneConsultRecord(Query query) {
        return consultRecordMongoDBService.findOneConsult(query);
    }


    @Override
    public ConsultSessionStatusVo findOneConsultSessionStatusVo(Query query){
        return consultRecordMongoDBService.findOneConsultSessionStatusVo(query);
    }

    @Override
    public List<ConsultRecordMongoVo> findUserConsultInfoBySessionId(ConsultRecordMongoVo consultRecordMongoVo) {
        return consultRecordDao.findUserConsultInfoBySessionId(consultRecordMongoVo);
    }

    @Override
    public HashMap<String, Object> uploadMediaFile(@RequestParam("file") MultipartFile file, @RequestParam("data") String data) throws UnsupportedEncodingException {
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
                    OSSObjectTool.uploadFileInputStream(fileName, file.getSize(), inputStream, OSSObjectTool.BUCKET_CONSULT_PIC);
                    //根据sessionId查询consult用户信息
                    ConsultRecordMongoVo consultRecordMongoVo = new ConsultRecordMongoVo();
                    consultRecordMongoVo.setSessionId(senderId);
                    List<ConsultRecordMongoVo> consultRecordMongoVos = this.findUserConsultInfoBySessionId(consultRecordMongoVo);
                    response.put("status","senderId is null !!!");
                    if(consultRecordMongoVos!=null && consultRecordMongoVos.size()>0){
                        consultRecordMongoVo = consultRecordMongoVos.get(0);
                        consultRecordMongoVo.setType(fileType);
                        consultRecordMongoVo.setMessage(fileName);
                        consultRecordMongoVo.setOpercode("sender");
                        this.saveConsultRecord(consultRecordMongoVo);
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

    //异步保存聊天信息
    @Override
    public void buildRecordMongoVo(@RequestParam(required = true) String senderId, @RequestParam(required = true) String type,
                                   @RequestParam(required = false) String messageContent, RichConsultSession consultSession) {
        Runnable thread = new SaveRecordThread(senderId,type,messageContent,consultSession);
        threadExecutor.execute(thread);
    }

    public class SaveRecordThread extends Thread {
        private String senderId;
        private String type;
        private String messageContent;
        private RichConsultSession consultSession;

        public SaveRecordThread(String senderId, String type,String messageContent,RichConsultSession consultSession) {
            super(SaveRecordThread.class.getSimpleName());
            this.senderId = senderId;
            this.type = type;
            this.messageContent = messageContent;
            this.consultSession = consultSession;
        }

        @Override
        public void run() {
            ConsultRecordMongoVo consultRecordMongoVo = new ConsultRecordMongoVo();
            Integer sessionId = consultSession.getId();
            consultRecordMongoVo.setSessionId(sessionId.toString());
            consultRecordMongoVo.setType(type);
            consultRecordMongoVo.setMessage(messageContent);
            consultRecordMongoVo.setSenderId(senderId);
            consultRecordMongoVo.setSenderName(consultSession.getUserName());
            consultRecordMongoVo.setUserId(consultSession.getUserId());
            consultRecordMongoVo.setCsUserId(consultSession.getCsUserId());
            consultRecordMongoVo.setDoctorName(consultSession.getCsUserName());
            consultRecordMongoVo.setCreateDate(new Date());
            saveConsultRecord(consultRecordMongoVo);
            saveConsultRecordTemporary(consultRecordMongoVo);
        }
    }

    @Override
    public void saveConsultSessionStatus(RichConsultSession consultSession) {
        ConsultSessionStatusVo consultSessionStatusVo = new ConsultSessionStatusVo();
        consultSessionStatusVo.setSessionId(String.valueOf(consultSession.getId()));
        consultSessionStatusVo.setLastMessageTime(new Date());
        consultSessionStatusVo.setUserId(consultSession.getUserId());
        consultSessionStatusVo.setStatus("ongoing");
        consultSessionStatusVo.setUserId(consultSession.getUserId());
        consultSessionStatusVo.setUserName(consultSession.getUserName());
        consultSessionStatusVo.setCsUserName(consultSession.getCsUserName());
        consultSessionStatusVo.setCsUserId(consultSession.getCsUserId());
        consultSessionStatusVo.setSource(consultSession.getSource());
        consultSessionStatusVo.setCreateDate(new Date());
        consultSessionStatusVo.setPayStatus(ConstantUtil.NO_PAY);
        consultSessionStatusVo.setFirstTransTime(null);
        consultRecordMongoDBService.upsertConsultSessionStatusVo(consultSessionStatusVo);
    }

    @Override
    public void modifyConsultSessionStatusVo(RichConsultSession consultSession) {
        ConsultSessionStatusVo consultSessionStatusVo = new ConsultSessionStatusVo();
        consultSessionStatusVo.setSessionId(String.valueOf(consultSession.getId()));
        consultSessionStatusVo.setCsUserId(""+consultSession.getCsUserId());
        consultRecordMongoDBService.modifyConsultSessionStatusVo(consultSessionStatusVo);
    }

    @Override
    public List<ConsultSessionStatusVo> querySessionStatusList(Query query){
        return consultRecordMongoDBService.querySessionStatusList(query);
    }

    @Override
    public void  updateConsultSessionStatusVo(Query query,String status) {
        consultRecordMongoDBService.updateConsultSessionStatusVo(query,status);
    }

    @Override
    public void  deleteConsultTempRecordVo(Query query) {
        consultRecordMongoDBService.deleteConsultRecordTemporary(query);
    }

    @Override
    public List<ConsultRecordMongoVo> getCurrentUserHistoryRecord(Query query) {
        return consultRecordMongoDBService.queryList(query);
    }

    @Override
    public List<ConsultSessionStatusVo> getConsultSessionStatusVo(Query query) {
        return consultRecordMongoDBService.getConsultSessionStatusList(query);
    }


    @Override
    public WriteResult removeConsultRankRecord(Query query) {
        return consultRecordMongoDBService.removeConsultRankRecord(query);
    }

    //jiangzg add 2016-8-11 18:32:02 更新mongo集合中字段
    @Override
    public int updateConsultSessionFirstTransferDate(Query query, Update update, Class t) {
       return  consultRecordMongoDBService.updateConsultSessionFirstTransferDate(query,update,t);
    }
}
