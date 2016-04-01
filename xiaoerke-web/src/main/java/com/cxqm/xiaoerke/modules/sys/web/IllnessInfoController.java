/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.sys.web;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.*;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.order.service.RegisterService;
import com.cxqm.xiaoerke.modules.sys.service.DoctorInfoService;
import com.cxqm.xiaoerke.modules.sys.service.HospitalInfoService;
import com.cxqm.xiaoerke.modules.sys.service.IllnessInfoService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 测试Controller
 *
 * @author ThinkGem
 * @version 2013-10-17
 */
@Controller
@RequestMapping(value = "${xiaoerkePath}")
public class IllnessInfoController extends BaseController {

    @Autowired
    private IllnessInfoService illnessInfoService;

    @Autowired
    private DoctorInfoService doctorInfoService;

    @Autowired
    private HospitalInfoService hospitalInfoService;
    
    @Autowired
	private RegisterService registerService;
	
    /**
     * 获取疾病一级分类
     * <p/>
     * params:{"pageNo":"2","pageSize":"10","orderBy":"1"}
     * //1为此疾病的医生人数最多的排序
     * <p/>
     * response:
     * {"pageNo":"2","pageSize":"10","pageTotal":"20",
     * "illnessData":[
     * {"illnessName":"小儿呼吸"},
     * {"illnessName":"小儿皮肤"}
     * ]
     * }
     */
    @RequestMapping(value = "/sys/illness/first", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> listFirstIllness(@RequestBody Map<String, Object> params) {
        return illnessInfoService.listFirstIllness(params);
    }

    /**
     * 获取一级疾病下的二级分类
     * params:{"illnessName":"小儿呼吸"}
     * <p/>
     * response:
     * {
     * "illnessListData":[
     * {"illnessSecondId":"fewx323ddw","illnessSecondName":"支气管炎"},
     * {"illnessSecondId":"fewx323ddw","illnessSecondName":"肺炎"}
     * ]
     * }
     */
    @RequestMapping(value = "/sys/illness/second", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> listSecondIllness(@RequestBody Map<String, Object> params) {
        return illnessInfoService.listSecondIllness(params);
    }

    /**
     * 获取二级分类疾病下的所有关联医院
     * <p/>
     * params:{"pageNo":"2","pageSize":"10","orderBy":"0","illnessSecondId":"fewx323ddw"}
     * //0按照最近的出诊时间排序，1按照匹配最佳排序，2按照粉丝最多排序
     * <p/>
     * response:
     * {"pageNo":"2","pageSize":"10","pageTotal":"20",
     * "hospitalData":[
     * {"hospitalId":"787xeieo234","hospitalName":"北京儿童医院"，"hospitalLocation":"北京市西便门内大街53号"},
     * {"hospitalId":"7xdwerd234","hospitalName":"首都儿科研究所"，"hospitalLocation":"北京市西便门内大街53号"}
     * ]
     * }
     */
    @RequestMapping(value = "/sys/illness/second/hospital", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> listSecondIllnessHospital(@RequestBody Map<String, Object> params) {
        return hospitalInfoService.listSecondIllnessHospital(params);
    }

    /**
     * 获取二级分类疾病下的专业医生
     * <p/>
     * params:{"pageNo":"2","pageSize":"10","orderBy":"0","illnessSecondId":"fewx323ddw"}
     * //0按照最近的出诊时间排序，1按照匹配最佳排序，2按照粉丝最多排序
     * <p/>
     * response:
     * {"pageNo":"2","pageSize":"10","pageTotal":"20",
     * "doctorDataVo":[
     * {"doctorId":"787xeieo234","doctorName":"甘晓庄","hospitalName":"北京儿童医院"，"departmentFullName":"儿内-重症医学科",
     * "expertise":"小儿呼吸","career_time":"32","fans_number":"40","available_time":"0"},
     * {"doctorId":"7xdwerd234","doctorName":"董庆华","hospitalName":"首都儿科研究所"，"departmentFullName":"儿内-呼吸内科",
     * "expertise":"小儿呼吸","career_time":"20"，"fans_number":"40","available_time":"0"}
     * ]
     * }
     * //available_time为0表示今日可约，为1代表明日可约，为2，代表2天后可约，依次类推7天以内的即可
     */
    @RequestMapping(value = "/sys/illness/second/doctor", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> listSecondIllnessDoctor(@RequestBody Map<String, Object> params) {
    	
    	HashMap<String, Object> response = new HashMap<String, Object>();

		String illnessSecondId = (String) params.get("illnessSecondId");
		String hospitalId = (String) params.get("hospitalId");
		String currentPage = ((String) params.get("pageNo"));
		String pageSize = ((String) params.get("pageSize"));
		String orderBy = (String) params.get("orderBy");
		String department_level1 = (String) params.get("department_level1");
		Page<HashMap<String, Object>> page = FrontUtils.generatorPage(currentPage,
				pageSize);
		HashMap<String, Object> secondIllnessInfo = new HashMap<String, Object>();
		secondIllnessInfo.put("orderBy", orderBy);
		secondIllnessInfo.put("illnessSecondId", illnessSecondId);
		secondIllnessInfo.put("hospitalId", hospitalId);
		secondIllnessInfo.put("department_level1", department_level1);
		Page<HashMap<String, Object>> resultPage = doctorInfoService.listSecondIllnessDoctor(secondIllnessInfo, page);

		// 记录日志
		LogUtils.saveLog(Servlets.getRequest(),"00000077" ,"获取二级分类疾病下的专业医生:" + illnessSecondId + ":" + hospitalId + ":" + department_level1);
		String sort = "";
		if (orderBy == "0") {
			sort = "时间最近排序";
		} else if (orderBy == "1") {
			sort = "粉丝最多排序";
		} else if (orderBy == "1") {
			sort = "从业时间排序";
		}
		LogUtils.saveLog(Servlets.getRequest(), "00000078","根据" + sort + "获取二级分类疾病下的专业医生");//排序获取二级分类疾病下的专业医生

		response.put("pageNo", resultPage.getPageNo());
		response.put("pageSize", resultPage.getPageSize());
		long tmp = FrontUtils.generatorTotalPage(resultPage);
		response.put("pageTotal", tmp + "");
		List<HashMap<String, Object>> list = resultPage.getList();
		List<HashMap<String, Object>> doctorDataVoList = new ArrayList<HashMap<String, Object>>();
		registerService.generateDoctorDataVoList(list, doctorDataVoList);
		response.put("doctorDataVo", doctorDataVoList);

		return response;
    }
    
}
