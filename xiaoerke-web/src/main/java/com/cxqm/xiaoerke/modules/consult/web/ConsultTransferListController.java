package com.cxqm.xiaoerke.modules.consult.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultTransferListVo;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultDoctorInfoService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultTransferListVoService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.sys.entity.Dict;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.DictService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jiangzhongge on 2016-5-13.
 */
@Controller
@RequestMapping(value = "consult/transfer")
public class ConsultTransferListController {

    @Autowired
    private ConsultTransferListVoService consultTransferListVoService;

    @Autowired
    private DictService dictService ;

    @Autowired
    private ConsultSessionService consultSessionService ;

    @Autowired
    private ConsultDoctorInfoService consultDoctorInfoService ;

    private SessionRedisCache sessionRedisCache = SpringContextHolder.getBean("sessionRedisCacheImpl");

    @RequestMapping(value="/findConsultTransferList",method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody
    HashMap<String,Object> findAllConsultTransferListVo(@RequestParam(value ="sortBy",required=false) String sortBy){
        HashMap<String,Object> response = new HashMap<String, Object>();
        JSONObject jsonObject;
        JSONArray jsonArray = new JSONArray();
        ConsultTransferListVo consultTransferListVo = new ConsultTransferListVo();
        if(StringUtils.isNotNull(sortBy ) && "order".equalsIgnoreCase(sortBy)){
            consultTransferListVo.setOrderBy("department");
        }
        consultTransferListVo.setStatus("ongoing");
        consultTransferListVo.setDelFlag("0");
        List<ConsultTransferListVo> consultTransferListVos= consultTransferListVoService.findAllConsultTransferListVo(consultTransferListVo);
        if(consultTransferListVos!= null && consultTransferListVos.size()>0){
            for(ConsultTransferListVo consultTransfer:consultTransferListVos){
                jsonObject = new JSONObject();
                RichConsultSession richConsultSession = new RichConsultSession();
                richConsultSession.setUserId(consultTransfer.getSysUserId());
                richConsultSession.setStatus("ongoing");
                List<RichConsultSession> richConsultSessionlist = consultSessionService.selectRichConsultSessions(richConsultSession);
                if(richConsultSessionlist != null && richConsultSessionlist.size()>0){
                    jsonObject.put("currentDoctor", richConsultSessionlist.get(0).getCsUserName());
                }else{
                    jsonObject.put("currentDoctor", "无");
                }
                jsonObject.put("userName",consultTransfer.getSysUserName());
                jsonObject.put("createDate",consultTransfer.getCreateDate());
                jsonObject.put("department",consultTransfer.getDepartment());
                jsonObject.put("id", consultTransfer.getId());
                jsonArray.add(jsonObject);
            }
            response.put("data",jsonArray);
            response.put("status","success");
        }else{
            response.put("data",jsonArray);
            response.put("status","failure");
        }
        return response;
    }

    @RequestMapping(value="/saveConsultTransfer",method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody
    HashMap<String,Object> saveConsultTransferListVo(@RequestBody HashMap<String,Object> params){
        HashMap<String,Object> responseResult = new HashMap<String, Object>();
        ConsultTransferListVo consultTransferListVo = new ConsultTransferListVo();
        Date date = new Date();
        User user = UserUtils.getUser();
        consultTransferListVo.setCreateBy(user.getId());
        consultTransferListVo.setCreateDate(date);
        consultTransferListVo.setDelFlag("0");
        consultTransferListVo.setDepartment((String) params.get("department"));
        consultTransferListVo.setSessionId((Integer) params.get("sessionId"));
        RichConsultSession richConsultSession = sessionRedisCache.getConsultSessionBySessionId((Integer) params.get("sessionId"));
        if(richConsultSession !=null) {
            consultTransferListVo.setSysUserId(richConsultSession.getUserId());
            consultTransferListVo.setSysUserName(richConsultSession.getUserName());
        }
        List<ConsultTransferListVo> consultTransferListVos = consultTransferListVoService.findAllConsultTransferListVo(consultTransferListVo);
        if(consultTransferListVos !=null && consultTransferListVos.size()>0){
            responseResult.put("status","exist");
        }else{
            consultTransferListVo.setSysUserIdCs(user.getId());
            consultTransferListVo.setSysUserNameCs(user.getName());
            consultTransferListVo.setStatus("ongoing");
            int count = consultTransferListVoService.addConsultTransferListVo(consultTransferListVo);
            if(count > 0){
                responseResult.put("status","success");
            }else{
                responseResult.put("status","failure");
            }
        }
        return responseResult;
    }

    @RequestMapping(value="/deleteConsultTransfer",method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody
    String deleteConsultTransfer(@RequestParam(value = "id",required=true) String id){
        int count = consultTransferListVoService.deleteConsultTransferListVo(Integer.valueOf(id));
        if(count > 0){
            return "success";
        }else{
            return "failure" ;
        }
    }

    @RequestMapping(value="/findDoctorDepartment",method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody
    HashMap<String,Object> findDoctorDepartment(){
        HashMap<String,Object> response = new HashMap<String, Object>();
        List<String> departmentList = consultDoctorInfoService.getConsultDoctorDepartment();
        JSONObject jsonObject;
        JSONArray jsonArray = new JSONArray();
        if(departmentList != null && departmentList.size()>0){
            for(int i=0; i<departmentList.size(); i++){
                String departmentName = departmentList.get(i);
                jsonObject = new JSONObject();
                jsonObject.put("departmentName",departmentName);
                jsonArray.add(jsonObject);
            }
            response.put("data",jsonArray);
            response.put("status","success");
        }else{
            response.put("status","failure");
        }
        return response ;
    }

    @RequestMapping(value ="/updateConsultTransferByPrimaryKey",method = {RequestMethod.POST,RequestMethod.GET})
    public @ResponseBody
    String updateConsultTransferByPrimaryKeys(@RequestBody HashMap<String,Object> params){
        String response = "failure";
        ConsultTransferListVo consultTransferListVo = new ConsultTransferListVo();
        if(StringUtils.isNotNull((String)params.get("status")) && !"ongoing".equalsIgnoreCase((String)params.get("status"))){
            consultTransferListVo.setStatus("complete");
        }
        if(StringUtils.isNotNull((String)params.get("delFlag")) && !"0".equalsIgnoreCase((String)params.get("delFlag"))){
            consultTransferListVo.setDelFlag("1");
        }
        String[] allId =(String[])params.get("updateIds");
        int count =0;
        if(allId != null && allId.length >0){
            for(int i =0 ; i<allId.length;i++){
                consultTransferListVo.setId(Integer.valueOf(allId[i]));
                int result = consultTransferListVoService.updateConsultTransferByPrimaryKey(consultTransferListVo);
                if(result >0){
                    count ++;
                }else{
                    break ;
                }
            }
            if(count == allId.length){
                response = "success";
            }
        }
        return response;
    }
}
