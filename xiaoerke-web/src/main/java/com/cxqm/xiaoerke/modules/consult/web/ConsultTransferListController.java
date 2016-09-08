package com.cxqm.xiaoerke.modules.consult.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.SpringContextHolder;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultDoctorInfoVo;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultTransferListVo;
import com.cxqm.xiaoerke.modules.consult.entity.RichConsultSession;
import com.cxqm.xiaoerke.modules.consult.service.ConsultDoctorInfoService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultSessionService;
import com.cxqm.xiaoerke.modules.consult.service.ConsultTransferListVoService;
import com.cxqm.xiaoerke.modules.consult.service.SessionRedisCache;
import com.cxqm.xiaoerke.modules.consult.service.core.ConsultSessionManager;
import com.cxqm.xiaoerke.modules.sys.entity.Dict;
import com.cxqm.xiaoerke.modules.sys.entity.User;
import com.cxqm.xiaoerke.modules.sys.service.DictService;
import com.cxqm.xiaoerke.modules.sys.service.UserInfoService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jiangzhongge on 2016-5-13.
 * 咨询列表接口
 */
@Controller
@RequestMapping(value = "consult/transfer")
public class ConsultTransferListController {

    @Autowired
    private ConsultTransferListVoService consultTransferListVoService;

    @Autowired
    private ConsultDoctorInfoService consultDoctorInfoService ;

    @Autowired
    private SessionRedisCache sessionRedisCache = SpringContextHolder.getBean("sessionRedisCacheImpl");

    @RequestMapping(value="/findConsultTransferList",method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody
    HashMap<String,Object> findAllConsultTransferListVo(@RequestBody HashMap<String,Object> params){
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        HashMap<String,Object> response = new HashMap<String, Object>();
        JSONObject jsonObject;
        JSONArray jsonArray = new JSONArray();
        ConsultTransferListVo consultTransferListVo = new ConsultTransferListVo();
        String sortBy = (String)params.get("sortBy");
        if(StringUtils.isNotNull(sortBy ) && "order".equalsIgnoreCase(sortBy)){
            consultTransferListVo.setOrderBy("department");
        }
        consultTransferListVo.setStatus("ongoing");
        consultTransferListVo.setDelFlag("0");
        List<ConsultTransferListVo> consultTransferListVos= consultTransferListVoService.findAllConsultTransferListVo(consultTransferListVo);
        if(consultTransferListVos!= null && consultTransferListVos.size()>0){
            for(ConsultTransferListVo consultTransfer:consultTransferListVos){
                jsonObject = new JSONObject();
                jsonObject.put("currentDoctor",StringUtils.isNotNull(consultTransfer.getSysUserNameCs()) ? consultTransfer.getSysUserNameCs() :"无");
                jsonObject.put("userName",consultTransfer.getSysUserName());
                jsonObject.put("createDate",DateUtils.formatDateTime(consultTransfer.getCreateDate()));
                jsonObject.put("department",consultTransfer.getDepartment());
                jsonObject.put("userId",consultTransfer.getSysUserId());
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

    /**
     * 将用户添加进转诊
     * @param params
     * @return
     */
    @RequestMapping(value="/saveConsultTransfer",method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody
    HashMap<String,Object> saveConsultTransferListVo(@RequestBody HashMap<String,Object> params){
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        HashMap<String,Object> responseResult = new HashMap<String, Object>();
        ConsultTransferListVo consultTransferListVo = new ConsultTransferListVo();
        ConsultSessionManager consultSessionManager = ConsultSessionManager.INSTANCE;
        Date date = new Date();
        User user = UserUtils.getUser();
        consultTransferListVo.setCreateBy(user.getId());
        consultTransferListVo.setCreateDate(date);
        consultTransferListVo.setDelFlag("0");
        HashMap<String,Object> requestData = (HashMap<String,Object>)params.get("consultData");
        consultTransferListVo.setDepartment((String)requestData.get("department"));
        consultTransferListVo.setSessionId((Integer) requestData.get("sessionId"));
        RichConsultSession richConsultSession = sessionRedisCache.getConsultSessionBySessionId((Integer)requestData.get("sessionId"));
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
                consultSessionManager.refreshConsultTransferList(UserUtils.getUser().getId());
            }else{
                responseResult.put("status","failure");
            }
        }
        return responseResult;
    }

    @RequestMapping(value="/deleteConsultTransfer",method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody
    String deleteConsultTransfer(@RequestParam(value = "id",required=true) String id){
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        int count = consultTransferListVoService.deleteConsultTransferListVo(Integer.valueOf(id));
        if(count > 0){
            return "success";
        }else{
            return "failure" ;
        }
    }

    /**
     * 列出所有科室
     * @return
     */
    @RequestMapping(value="/findDoctorDepartment",method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody
    HashMap<String,Object> findDoctorDepartment(){
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

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

    /**
     * 更新转诊列表用户状态
     * @param params
     * @return
     */
    @RequestMapping(value ="/updateConsultTransferByPrimaryKey",method = {RequestMethod.POST,RequestMethod.GET})
    public @ResponseBody
    HashMap<String,Object> updateConsultTransferByPrimaryKeys(@RequestBody HashMap<String,Object> params){
        DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);

        HashMap<String,Object> response = new HashMap<String,Object>();
        response.put("status","failure");
        List<HashMap<String,Object>> reqeustData =  (List<HashMap<String,Object>>)params.get("content");
        ConsultSessionManager consultSessionManager=ConsultSessionManager.INSTANCE;
        ConsultTransferListVo consultTransferListVo = new ConsultTransferListVo();
        List allId = new ArrayList();
        HashMap<String,Object> data ;
        if(reqeustData != null && reqeustData.size()>0){
            for(int i=0;i<reqeustData.size();i++){
                data = reqeustData.get(i);
                allId.add(data.get("id"));
            }
        }
        if(StringUtils.isNotNull((String)params.get("status")) && !"ongoing".equalsIgnoreCase((String)params.get("status"))){
            consultTransferListVo.setStatus("complete");
        }
        if(StringUtils.isNotNull((String)params.get("delFlag")) && !"0".equalsIgnoreCase((String)params.get("delFlag"))){
            consultTransferListVo.setDelFlag("1");
        }
        int count =0;
        if(allId != null && allId.size() >0){
            for(int i =0 ; i<allId.size();i++){
                consultTransferListVo.setId((Integer)allId.get(i));
                int result = consultTransferListVoService.updateConsultTransferByPrimaryKey(consultTransferListVo);
                if(result >0){
                    count ++;
                }
            }
            if(count == allId.size()){
                response.put("status","success");
                consultSessionManager.refreshConsultTransferList(UserUtils.getUser().getId());
            }
        }
        return response;
    }


    /**
     * 通过医生ID查询医生信息
     * @author jiangzg 2016年5月25日12:23:17
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/getCurrentDoctorDepartment", method= RequestMethod.POST)
    public @ResponseBody HashMap<String,Object> getConsultDoctorInfoByUserId(@RequestBody String userId){
        DataSourceSwitch.setDataSourceType(DataSourceInstances.READ);

        HashMap<String,Object> response = new HashMap<String, Object>();
        response.put("status","failure");
        if(StringUtils.isNotNull(userId)){
            ConsultDoctorInfoVo consultDoctorInfoVo = consultDoctorInfoService.getConsultDoctorInfoByUserId(userId);
            if(consultDoctorInfoVo != null){
                response.put("department",consultDoctorInfoVo.getDepartment());
                response.put("status","success");
            }
        }
        return response;
    }
}
