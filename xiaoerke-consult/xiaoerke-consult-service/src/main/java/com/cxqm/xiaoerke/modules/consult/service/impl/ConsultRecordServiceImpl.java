package com.cxqm.xiaoerke.modules.consult.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.cxqm.xiaoerke.common.utils.OSSObjectTool;
import com.cxqm.xiaoerke.modules.consult.dao.ConsultRecordDao;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordMongoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultRecordVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultRecordService;
import com.cxqm.xiaoerke.modules.sys.entity.PaginationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
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
 * 咨询服务
 *
 * @author deliang
 * @version 2015-12-09
 */
@Service
@Transactional(readOnly = false)
public class ConsultRecordServiceImpl implements ConsultRecordService {


    @Autowired
    private ConsultRecordDao consultRecordDao;

    @Autowired
    ConsultRecordMongoDBServiceImpl consultRecordMongoDBService;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return consultRecordDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(ConsultRecordVo consultRecordVo) {
        return consultRecordDao.insert(consultRecordVo);
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
    public PaginationVo<ConsultRecordMongoVo> getPage(int pageNo, int pageSize, Query query) {
        return consultRecordMongoDBService.getPage(pageNo, pageSize, query);
    }

    @Override
    public int saveConsultRecord(ConsultRecordMongoVo consultRecordMongoVo) {
        return consultRecordMongoDBService.saveConsultRecord(consultRecordMongoVo);
    }


    @Override
    public ConsultRecordMongoVo findOneConsultRecord(Query query) {
        return consultRecordMongoDBService.findOneConsult(query);
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



}
