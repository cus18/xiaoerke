package com.cxqm.xiaoerke.modules.vaccine.web;

import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineInfoVo;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineInfoWithBLOBsVo;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineStationRelVo;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineStationVo;
import com.cxqm.xiaoerke.modules.vaccine.service.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保险Controller
 * @author sunxiao
 * @version 2016-9-26
 */
@Controller
@RequestMapping(value = "${adminPath}/vaccine")
public class VaccineController {

	@Autowired
	VaccineService vaccineService;

	/**
	 * 疫苗列表
	 * sunxiao
	 * @param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "vaccineList")
	public String vaccineList(VaccineInfoWithBLOBsVo vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		List<VaccineInfoWithBLOBsVo> list = vaccineService.findVaccineList(vo);
		model.addAttribute("vo", vo);
		model.addAttribute("list", list);
		return "modules/vaccine/vaccineList";
	}

	/**
	 * 疫苗列表
	 * sunxiao
	 * @param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveUpdateVaccineForm")
	public String saveUpdateVaccineForm(VaccineInfoWithBLOBsVo vo, Model model) {
		if(StringUtils.isNotNull(vo.getId()+"")){
			List<VaccineInfoWithBLOBsVo> list = vaccineService.findVaccineList(vo);
			model.addAttribute("vo", list.get(0));
		}else{
			model.addAttribute("vo", new VaccineInfoWithBLOBsVo());
		}

		return "modules/vaccine/saveUpdateVaccineForm";
	}

	/**
	 * 疫苗列表
	 * sunxiao
	 * @param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveUpdateVaccineInfo")
	public String saveUpdateVaccineInfo(VaccineInfoWithBLOBsVo vo , Model model) {
		vaccineService.saveVaccineInfo(vo);
		model.addAttribute("vo", vo);
		return "modules/vaccine/vaccineList";
	}

	/**
	 * 疫苗列表
	 * sunxiao
	 * @param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "vaccineStationList")
	public String vaccineStationList(VaccineStationVo vo , Model model) {
		List<VaccineStationVo> list = vaccineService.findVaccineStationList(vo);
		model.addAttribute("vo", vo);
		model.addAttribute("list", list);
		return "modules/vaccine/vaccineStationList";
	}

	/**
	 * 疫苗列表
	 * sunxiao
	 * @param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveUpdateVaccineStationForm")
	public String saveUpdateVaccineStationForm(VaccineStationVo vo, Model model) {
		if(StringUtils.isNotNull(vo.getId()+"")){
			List<VaccineStationVo> list = vaccineService.selectByVaccineStationVo(vo);
			VaccineStationRelVo relVo = new VaccineStationRelVo();
			relVo.setVaccineStationId(vo.getId());
			List<VaccineStationRelVo> relList = vaccineService.findVaccineStationRelList(relVo);
			model.addAttribute("relList", relList);
			model.addAttribute("vo", list.get(0));
		}else{
			model.addAttribute("vo", new VaccineStationVo());
		}

		VaccineInfoWithBLOBsVo vvo = new VaccineInfoWithBLOBsVo();
		List<VaccineInfoWithBLOBsVo> vlist = vaccineService.findVaccineList(vvo);
		Map<String,String> map = new HashMap<String, String>();
		for(VaccineInfoWithBLOBsVo temp : vlist){
			map.put(temp.getId()+","+temp.getName()+","+temp.getMiniumAge()+","+temp.getLastTimeInterval(),temp.getName());
		}
		map.put("","");
		model.addAttribute("vaccineMap", map);
		return "modules/vaccine/saveUpdateVaccineStationForm";
	}

	/**
	 * 疫苗列表
	 * sunxiao
	 * @param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveVaccineStationInfo")
	public String saveVaccineStationInfo(VaccineStationVo vo,HttpServletRequest request,Model model) {
		String[] vaccines = request.getParameterValues("vaccine");
		String[] nextvaccines = request.getParameterValues("nextvaccine");
		String relid = request.getParameter("relid");
		List<VaccineStationRelVo> relList = new ArrayList<VaccineStationRelVo>();
		if(vaccines!=null){
			for(int i=0;i<vaccines.length;i++){
				VaccineStationRelVo relVo = new VaccineStationRelVo();
				String[] vaccine = vaccines[i].split(",");
				String[] nextvaccine = nextvaccines[i].split(",");
				relVo.setVaccineId(Integer.parseInt(vaccine[0]));
				relVo.setVaccineName(vaccine[1]);
				relVo.setMiniumAge(Integer.parseInt(vaccine[2]));
				relVo.setNextVaccineId(Integer.parseInt(nextvaccine[0]));
				relVo.setWillVaccineName(nextvaccine[1]);
				relVo.setNextLastTimeInterval(Integer.parseInt(nextvaccine[3]));
				relList.add(relVo);
			}
		}
		vaccineService.saveVaccineStationInfo(vo,relList,relid);
		model.addAttribute("vo", vo);
		return "modules/register/vaccineList";
	}

}
