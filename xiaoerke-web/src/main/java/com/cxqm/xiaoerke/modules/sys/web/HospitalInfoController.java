package com.cxqm.xiaoerke.modules.sys.web;

import com.cxqm.xiaoerke.common.dataSource.DataSourceInstances;
import com.cxqm.xiaoerke.common.dataSource.DataSourceSwitch;
import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.FrontUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.common.web.Servlets;
import com.cxqm.xiaoerke.modules.order.service.RegisterService;
import com.cxqm.xiaoerke.modules.sys.service.DoctorInfoService;
import com.cxqm.xiaoerke.modules.sys.service.HospitalInfoService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 医院信息获取和管理的控制层
 *
 * @author chenjiake
 * @version 2015-11-04
 */
@Controller
@RequestMapping(value = "sys/hospital")
public class HospitalInfoController extends BaseController {

    @Autowired
    private HospitalInfoService hospitalInfoService;
    
    @Autowired
    private DoctorInfoService doctorInfoService;
    
    @Autowired
    private RegisterService registerService;

    /**
     * 获取系统内所有医院
     * params:{"pageNo":"2","pageSize":"10","orderBy":"1"}
     * //1按照医院医生人数最多的排序
     *
     * response:
     * {"pageNo":"2","pageSize":"10","pageTotal":"20",
     * "hospitalData":[
     * {"hospitalId":"787xeieo234","hospitalName":"北京儿童医院"，"hospitalLocation":"北京市西便门内大街53号"},
     * {"hospitalId":"7xdwerd234","hospitalName":"首都儿科研究所"，"hospitalLocation":"北京市西便门内大街53号"}
     * ]
     * }
     */
    @RequestMapping(value = "", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> listHospital(@RequestBody Map<String, Object> params) {
		//DataSourceSwitch.setDataSourceType(DataSourceInstances.WRITE);
        return hospitalInfoService.listAllHospital(params);
	}

	@RequestMapping(value = "/cooperation", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	HashMap<String, Object> getCooperationHospitalInfo(@RequestParam(required=true) String hospitalId) {
		HashMap<String,Object> result = new HashMap<String,Object>();
		HashMap<String,Object> response = new HashMap<String,Object>();
		HashMap<String, Object> HospitalInfo = hospitalInfoService.getHospitalDetailInfo(hospitalId);
		Page<HashMap<String, Object>> page = FrontUtils.generatorPage("1", "3");
		HashMap<String, Object> doctorMap = new HashMap<String, Object>();
		doctorMap.put("sysHospitalId", hospitalId);
		doctorMap.put("orderBy", "0");
		Page<HashMap<String, Object>> resultPage = doctorInfoService.findDoctorByHospital(doctorMap, page);
		response.put("pageNo", resultPage.getPageNo());
		response.put("pageSize", resultPage.getPageSize());
		long tmp = FrontUtils.generatorTotalPage(resultPage);
		response.put("pageTotal", tmp + "");
		List<HashMap<String, Object>> list = resultPage.getList();
		List<HashMap<String, Object>> doctorDataVoList = new ArrayList<HashMap<String, Object>>();
		registerService.generateDoctorDataVoList(list, doctorDataVoList);
		result.put("doctorDataVo", doctorDataVoList);
		result.put("hospitalInfo",HospitalInfo);
		return result;
	}

    /**
     * 获取医院所有科室
     * params:{"pageNo":"2","pageSize":"10","orderBy":"1"，"hospitalId":"324xdksg234"}
     * //1按照此医院科室中医生人数最多排序，科室为一级科室
     *
     * response:
     * {"pageNo":"2","pageSize":"10","pageTotal":"20",
     * "departmentData":[
     * {"hospitalId":"787xeieo234","hospitalName":"北京儿童医院"，"departmentLevel1Name":"儿内","},
     * {"hospitalId":"7xdwerd234","hospitalName":"首都儿科研究所"，"departmentLevel1Name":"儿内"}
     * ]
     * }
     */
    @RequestMapping(value = "/department", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> listHospitalDepartment(@RequestBody Map<String, Object> params) {
        return hospitalInfoService.listHospitalDepartment(params);
    }

    /**
     * 获取医院医生，需要考虑排序
     * <p/>
     * params:{"pageNo":"2","pageSize":"10","orderBy":"1"，"hospitalId":"324xdksg234","department_level1":"2312314313"}
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
     * orderBy 0 按照时间最近排序 1 按照粉丝最多排序
     */
    @RequestMapping(value = "/doctor", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    Map<String, Object> listHospitalDoctor(@RequestBody Map<String, Object> params) {
		HashMap<String, Object> response = new HashMap<String, Object>();

		String hospitalId = (String) params.get("hospitalId");
		String currentPage = (String) params.get("pageNo");
		String pageSize = (String) params.get("pageSize");
		String orderBy = (String) params.get("orderBy");
		String department_level1 = (String) params.get("department_level1");

		Page<HashMap<String, Object>> page = FrontUtils.generatorPage(currentPage,
				pageSize);

		HashMap<String, Object> doctorMap = new HashMap<String, Object>();
		doctorMap.put("sysHospitalId", hospitalId);
		doctorMap.put("orderBy", orderBy);
		doctorMap.put("department_level1", department_level1);

		Page<HashMap<String, Object>> resultPage = doctorInfoService.findDoctorByHospital(doctorMap, page);

		// 记录日志
		String sort = "";
		if (orderBy.equals("0")) {
			sort = "时间最近排序";
		} else if (orderBy.equals("1")) {
			sort = "粉丝最多排序";
		} else if (orderBy.equals("2")) {
			sort = "从业时间排序";
		}
		LogUtils.saveLog(Servlets.getRequest(), "00000076","获取医院医生:" + hospitalId + " 用户id" + UserUtils.getUser().getId()+"sort:"+sort);

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

    /**
     * 获取医院的某个科室的医生
     * <p/>
     * params:{"pageNo":"2","pageSize":"10","orderBy":"0"，"hospitalId":"324xdksg234","departmentLevel1Name":"儿内"}
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
    @RequestMapping(value = "/department/doctor", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> listHospitalDepartmentDoctor(@RequestBody Map<String, Object> params) {
		HashMap<String, Object> response = new HashMap<String, Object>();

		String hospitalId = (String) params.get("hospitalId");
		String departmentName = (String) params.get("departmentLevel1Name");
		String currentPage = ((String) params.get("pageNo"));
		String pageSize = ((String) params.get("pageSize"));
		String orderBy = (String) params.get("orderBy");

		Page<HashMap<String, Object>> page = FrontUtils.generatorPage(currentPage,
				pageSize);
		HashMap<String, Object> hospitalInfo = new HashMap<String, Object>();
		hospitalInfo.put("hospitalId", hospitalId);
		hospitalInfo.put("departmentName", departmentName);
		hospitalInfo.put("orderBy", orderBy);

		Page<HashMap<String, Object>> resultPage = doctorInfoService.findPageDoctorByDepartment(hospitalInfo, page);

		// 记录日志
		LogUtils.saveLog(Servlets.getRequest(), "00000075" + departmentName);//获取医院的某个科室的医生

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
	
	/**
	 *根据医院名称获取医院信息
	 * */
	@RequestMapping(value = "/info", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	Map<String, Object> getHospicalInfo(@RequestBody Map<String, Object> params){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		HashMap<String,Object> result = hospitalInfoService.getHospitalDetailInfo((String) params.get("id"));
		String medicalProcess = (String)result.get("medicalProcess");
		resultMap.put("medicalProcess",medicalProcess);
		return resultMap;
	}

	@RequestMapping(value = "/listDepartmentHospital", method = {RequestMethod.POST, RequestMethod.GET})
	public
	@ResponseBody
	Map<String, Object> listDepartmentHospital(@RequestBody Map<String, Object> params) {
		return hospitalInfoService.listDepartmentHospital(params);
	}


}
