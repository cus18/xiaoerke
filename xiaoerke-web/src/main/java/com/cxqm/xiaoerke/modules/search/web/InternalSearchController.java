package com.cxqm.xiaoerke.modules.search.web;

import java.util.*;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.Cookiebean;
import com.cxqm.xiaoerke.common.utils.FrontUtils;
import com.cxqm.xiaoerke.common.utils.SearchCook;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.Servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.cxqm.xiaoerke.modules.order.service.RegisterService;
import com.cxqm.xiaoerke.modules.search.service.InternalSearchService;
import com.cxqm.xiaoerke.modules.sys.entity.HospitalVo;
import com.cxqm.xiaoerke.modules.sys.service.DoctorInfoService;
import com.cxqm.xiaoerke.modules.sys.service.HospitalInfoService;
import com.cxqm.xiaoerke.modules.sys.utils.LogUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "${xiaoerkePath}")
public class InternalSearchController {
	
	@Autowired
	private InternalSearchService internalSearchService;

	@Autowired
	private RegisterService registerService;
	
	@Autowired
	private HospitalInfoService hospitalInfoService;
	
	@Autowired
	private DoctorInfoService doctorInfoService;
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 搜索医生接口
	 * @param params 搜索的字符串,包括医生姓名、医院和症状 页面大小 页面索引
	 * @param
	 * @param
	 * @return
    {
        "doctorData":
        [
            {
                "hospitalName": "北京儿童医院",
                "title": "副教授",
                "cardExperience": "感冒，发烧，咳嗽",
                "doctorName": "常丽",
                "doctorId": "2",
                "registerInfos":
                [
                    {
                        "latestRegister": 1438221600000,
                        "location": "儿童"
                    },
                    {
                        "latestRegister": 1438239600000,
                        "location": "中日"
                    }
                ]
            }, 
            {
                "hospitalName": "北京协和医院",
                "title": "教授",
                "cardExperience": "多动症",
                "doctorName": "高淑芬",
                "doctorId": "4",
                "registerInfos":
                [
                    {
                        "latestRegister": 1438221600000,
                        "location": "中日"
                    }
                ]
            }
        ],
        "status": "OK",
        "pageTotal": "1",
        "pageNo": 0,
        "pageSize": 10
    }
	 */
    @RequestMapping(value = "/search/searchDoctors", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> listSearchDoctor(@RequestBody Map<String, Object> params,HttpServletRequest request,HttpServletResponse httpResponse) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		String queryStr = (String)params.get("queryStr");
		Integer pageNo = (Integer)params.get("pageNo");
		Integer pageSize = (Integer)params.get("pageSize");
		String orderBy = (String)params.get("orderBy");
		String hospitalId = (String)params.get("hospitalId");
		SearchCook.writecookie(queryStr, "", queryStr, request, httpResponse, 6);
		// 记录日志
		LogUtils.saveLog(Servlets.getRequest(), "00000072","搜索医生或者疾病:" + queryStr);

		if(StringUtils.isEmpty(orderBy))
			orderBy = "0";

		if(pageNo == null)
			pageNo = 0;

		if(pageSize == null)
			pageSize = 10;

		try {
			long now = new Date().getTime();
			String[] doctorIds = internalSearchService.searchDoctors(queryStr, 500, 0,"doctorId");
			logger.info("1=========================== " + (new Date().getTime() - now) );
			List<HashMap<String, Object>> doctorDataList = new ArrayList<HashMap<String, Object>>();
			List<HospitalVo> hospitals = null;
			long pageTotal = 0;
			if(doctorIds != null && doctorIds.length > 0) {
				Page<HashMap<String,Object>> page = new Page<HashMap<String,Object>>(pageNo, pageSize);
				now = new Date().getTime();
				Page<HashMap<String, Object>> doctorsPage = doctorInfoService.findPageAllDoctorByDoctorIds(doctorIds, hospitalId, orderBy, page);
				logger.info("2=========================== " + (new Date().getTime() - now) );
				pageTotal = FrontUtils.generatorTotalPage(doctorsPage);
				List<HashMap<String, Object>> list = doctorsPage.getList();
				now = new Date().getTime();
				registerService.generateDoctorDataVoList(list, doctorDataList);
				logger.info("3=========================== " + (new Date().getTime() - now) );
				hospitals = hospitalInfoService.findHospitalsByDoctorIds(doctorIds);
			}

			response.put("pageNo", pageNo);
			response.put("pageSize", pageSize);
			response.put("pageTotal", pageTotal + "");
			response.put("status", "OK");
			response.put("doctorData", doctorDataList);
			response.put("hospitals", hospitals);
		} catch (Exception e) {
			response.put("status", "FAIL");
			logger.info(e.getMessage());
			e.printStackTrace();
		}

		return response;
    }
    
    @RequestMapping(value = "/search/import", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> keywordsIllnessRelImportt() {
    	HashMap<String, Object> response = new HashMap<String, Object>();
    	internalSearchService.keywordsIllnessRelImport();
        return response;
    }

    @RequestMapping(value = "/search/user/PatientSearchList", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> getPatientSearchList(HttpServletRequest request) {
        ArrayList<Cookiebean> li = SearchCook.getSearchPatientS(request);
        Collections.reverse(li);
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("patientSearchList", li);
        return response;
    }

    @RequestMapping(value = "/search/user/RemoveAllSearchHistory", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    String removeAllSearchHistory(HttpServletRequest request, HttpServletResponse response) {
        SearchCook.removeAllSearchHistory(request, response);
        return "";
    }

    @RequestMapping(value = "/search/user/autoPrompting", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object>  autoPrompting(@RequestBody Map<String, Object> params) throws Exception{
        Map<String,Object> resultMap = new HashMap<String, Object>();
        String queryStr = (String)params.get("queryStr");
        if(StringUtils.isNotNull(queryStr)){
            String[] doctorIds = internalSearchService.searchDoctors(queryStr, 500, 0,"autoPromp");
            resultMap.put("result", Arrays.asList(doctorIds));
            return resultMap;
        }
        return null;
    }
}
