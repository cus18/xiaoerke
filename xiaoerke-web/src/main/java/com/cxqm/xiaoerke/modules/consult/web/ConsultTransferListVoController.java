package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultTransferListVo;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultTransferListVoService;
import com.cxqm.xiaoerke.modules.sys.entity.Dict;
import com.cxqm.xiaoerke.modules.sys.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jiangzhongge on 2016-5-13.
 */
@Controller
@RequestMapping(value = "consult/transfer")
public class ConsultTransferListVoController {

    @Autowired
    ConsultTransferListVoService consultTransferListVoService;

    @Autowired
    DictService dictService ;

    @Autowired
    ConsultSessionService consultSessionService ;

    @RequestMapping(value="/findConsultTransferList",method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody
    HashMap<String,Object> findAllConsultTransferListVo(@RequestParam(required=false) String orderOrNot){
        HashMap<String,Object> response = new HashMap<String, Object>();
        ConsultTransferListVo consultTransferListVo = new ConsultTransferListVo();
        if(StringUtils.isNotNull(orderOrNot ) && "order".equalsIgnoreCase(orderOrNot)){
            consultTransferListVo.setOrder("department");
        }
        List<ConsultTransferListVo> list= consultTransferListVoService.findAllConsultTransferListVo(consultTransferListVo);
        if(list!= null && list.size()>0){
            for(int i=0;i<list.size();i++){
                ConsultTransferListVo consultTransfer = list.get(i);
                RichConsultSession richConsultSession = new RichConsultSession();
                richConsultSession.setUserId(consultTransfer.getSysUserId());
                richConsultSession.setStatus("ongoing");
                List<RichConsultSession> richConsultSessionlist = consultSessionService.selectRichConsultSessions(richConsultSession);
                if(richConsultSessionlist != null && richConsultSessionlist.size()>0){
                    response.put("currentDoctor", richConsultSessionlist.get(0).getCsUserName());
                }else{
                    response.put("currentDoctor", "无");
                }
                response.put("userName",consultTransfer.getSysUserName());
                response.put("createDate",consultTransfer.getCreateDate());
                response.put("department",consultTransfer.getDepartment());
                response.put("id",consultTransfer.getId());
                response.put("status","success");
            }
        }else{
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
        consultTransferListVo.setCreateBy((String) params.get(""));
        consultTransferListVo.setCreateDate(date);
        consultTransferListVo.setDelFlag((String) params.get(""));
        consultTransferListVo.setDepartment((String) params.get(""));
        consultTransferListVo.setDepartmentId((String) params.get(""));
        consultTransferListVo.setSessionId((Integer) params.get(""));
        consultTransferListVo.setSysUserId((String) params.get(""));
        consultTransferListVo.setSysUserIdCs((String) params.get(""));
        consultTransferListVo.setSysUserName((String) params.get(""));
        consultTransferListVo.setSysUserNameCs((String) params.get(""));
        consultTransferListVo.setUpdateDate(date);
        consultTransferListVo.setStatus((String) params.get(""));

        int count = consultTransferListVoService.addConsultTransferListVo(consultTransferListVo);
        if(count != 0){
            responseResult.put("status","success");
        }else{
            responseResult.put("status","failure");
        }
        return responseResult;
    }

    @RequestMapping(value="/deleteConsultTransfer",method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody
    String deleteConsultTransfer(@RequestParam(required=true) String id){
        int count = consultTransferListVoService.deleteConsultTransferListVo(id);
        if(count != 0){
            return "success";
        }else{
            return "failure" ;
        }
    }

    @RequestMapping(value="/findDoctorDepartment",method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody
    List<HashMap<String,Object>> findDoctorDepartment(){
        HashMap<String,Object> response = new HashMap<String, Object>();
        List<HashMap<String,Object>> listmap = new ArrayList<HashMap<String, Object>>();
        Dict dict = new Dict();
        dict.setType("department_type");
        List list = dictService.findList(dict);
        if(list !=null && list.size()>0){
            for(int i=0 ; i<list.size() ;i++){
                Dict dict1 = (Dict)list.get(i);
                String dictId = dict1.getId();
                String dictValue = dict1.getDescription();
                response.put("dictId",dictId);
                response.put("dictValue", dictValue);
                listmap.add(response);
            }
        }
        return listmap ;
    }
}
