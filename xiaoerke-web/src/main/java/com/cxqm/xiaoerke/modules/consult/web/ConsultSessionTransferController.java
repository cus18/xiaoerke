package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.modules.consult.entity.ConsultTransferListVo;
import com.cxqm.xiaoerke.modules.consult.service.ConsultTransferListVoService;
import com.cxqm.xiaoerke.modules.consult.service.core.ConsultSessionManager;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangzhongge on 2016-5-18.
 * 转接列表发起会话
 */
@Controller
@RequestMapping(value = "consultSession/transfer")
public class ConsultSessionTransferController {

    @Autowired
    private ConsultTransferListVoService consultTransferListVoService;

    @RequestMapping(value = "/createMoreUserConsultSession", method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody
    HashMap<String, Object> createMoreWXUserConsultSession(@RequestBody Map<String, Object> params){

        HashMap<String ,Object> response = new HashMap<String, Object>();
        List<HashMap<String,Object>> requestData = (List<HashMap<String,Object>>) params.get("content");
        ConsultSessionManager consultSessionManager=ConsultSessionManager.getSessionManager();
        List<HashMap<String,Object>> transferList = new ArrayList<HashMap<String, Object>>();
        if(requestData !=null && requestData.size() >0){
            for(int i=0 ; i<requestData.size();i++){
                HashMap<String,Object> data = new HashMap<String, Object>();
                data.put("id", requestData.get(i).get("id"));
                data.put("userId", requestData.get(i).get("userId"));
                transferList.add(data);
            }
            HashMap failureMap ;
            if(transferList != null && transferList.size()>0){
                for(HashMap<String,Object> hashMap : transferList){
                    try{
                        response = consultSessionManager.createConsultSession((String)hashMap.get("userId"));
                        if(response!= null && "success".equalsIgnoreCase((String) response.get("status"))){
                            ConsultTransferListVo consultTransferListVo;
                            String status = "complete";
                            String delFlag = "0";
                            consultTransferListVo = new ConsultTransferListVo();
                            consultTransferListVo.setStatus(status);
                            consultTransferListVo.setDelFlag(delFlag);
                            consultTransferListVo.setId((Integer)hashMap.get("id"));
                            int count = consultTransferListVoService.updateConsultTransferByPrimaryKey(consultTransferListVo);
                            if(count > 0){
                                response.put("status","success");
                                consultSessionManager.refreshConsultTransferList(UserUtils.getUser().getId());
                            }
                        }else{
                             failureMap= new HashMap();
                             failureMap.put("id", hashMap.get("id"));
                             response.put("failureMap",failureMap);
                        }
                    }catch(Exception exception){
                        exception.getStackTrace();
                    }

                }
            }
        }
        return response;
    }
}
