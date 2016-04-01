package com.cxqm.xiaoerke.modules.sys.web;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.FrontUtils;
import com.cxqm.xiaoerke.modules.sys.service.HospitalInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangbaowei on 16/3/16.
 * 电话咨询医院相关接口
 *
 */

@Controller
@RequestMapping(value = "${xiaoerkePath}/consultPhone/")
public class ConsultPhoneHospitalController {

    @Autowired
    private HospitalInfoService hospitalInfoService;

    /**
     * 电话咨询首页-获取所有医院列表
     * @return Map
     * */
    @RequestMapping(value = "getAllHospitalList",method = {RequestMethod.GET,RequestMethod.POST})
    public
    @ResponseBody
    Map<String,Object> getAllHospitalList(@RequestParam(value="pageNo", required=true) String pageNo,
                                                     @RequestParam(value="pageSize", required=false) String pageSize,
                                                     @RequestParam(value="orderBy", required=false) String orderBy){
        HashMap<String, Object> response = new HashMap<String, Object>();
        Page<HashMap<String, Object>> page = FrontUtils.generatorPage(pageNo, pageSize);
        Page<HashMap<String, Object>> resultPage = hospitalInfoService.getHospitalListByConsulta(page);
        long tmp = FrontUtils.generatorTotalPage(resultPage);
        response.put("pageTotal", tmp + "");
        response.put("pageNo", resultPage.getPageNo());
        response.put("pageSize", resultPage.getPageSize());

        List<HashMap<String, Object>> list = resultPage.getList();
        List<HashMap<String, Object>> hospitalVoList = new ArrayList<HashMap<String, Object>>();
        if (list != null && !list.isEmpty()) {
            for (Map hospitalMap : list) {
                HashMap<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put("hospitalId", hospitalMap.get("id"));
                dataMap.put("hospitalName", hospitalMap.get("name"));
                dataMap.put("hospitalLocation", hospitalMap.get("position"));
                if(hospitalMap.containsKey("hospital_type"))
                {
                    dataMap.put("hospitalType", hospitalMap.get("hospital_type"));
                }
                hospitalVoList.add(dataMap);
            }
        }
        response.put("hospitalData", hospitalVoList);
        return response;
    }


}
