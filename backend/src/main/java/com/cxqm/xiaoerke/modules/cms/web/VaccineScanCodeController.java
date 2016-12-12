package com.cxqm.xiaoerke.modules.cms.web;

import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.vaccine.entity.VaccineScanCodeVo;
import com.cxqm.xiaoerke.modules.vaccine.service.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 疫苗扫码Controller
 * @author sunxiao
 * @version 2016-12-12
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/vaccineScanCode")
public class VaccineScanCodeController {

	@Autowired
	private VaccineService vaccineService;

	/**
	 * 管理基础路径
	 */
	@Value("${adminPath}")
	protected String adminPath;

	/**
	 * 疫苗扫码列表
	 * sunxiao
	 * @param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "vaccineScanCodeList")
	public String vaccineScanCodeList(VaccineScanCodeVo vo, Model model) {
		List<VaccineScanCodeVo> list = vaccineService.findVaccineScanCodeList(vo);
		model.addAttribute("vo", vo);
		model.addAttribute("list", list);
		return "modules/cms/vaccineScanCodeList";
	}

	/**
	 * 疫苗扫码添加修改页面
	 * sunxiao
	 * @param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveUpdateVaccineScanCodeForm")
	public String saveUpdateVaccineScanCodeForm(VaccineScanCodeVo vo,Model model) {
		VaccineScanCodeVo rvo = new VaccineScanCodeVo();
		if(StringUtils.isNotNull(vo.getId()+"")){
			List<VaccineScanCodeVo> list = vaccineService.findVaccineScanCodeList(vo);
			if(list.size()!=0){
				rvo = list.get(0);
			}
		}
		model.addAttribute("vo", rvo);
		return "modules/cms/saveUpdateVaccineScanCodeForm";
	}

	/**
	 * 疫苗扫码添加修改
	 * sunxiao
	 * @param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveUpdateVaccineScanCode")
	public String saveUpdateVaccineScanCode(VaccineScanCodeVo vo,HttpServletRequest request,HttpServletResponse response, Model model) {
		vaccineService.updateVaccineScanCodeInfo(vo);
		model.addAttribute("vo", vo);
		return "redirect:" + adminPath + "/cms/vaccineScanCodeForm";
	}

	/**
	 * 疫苗扫码删除
	 * sunxiao
	 * @param
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "deleteVaccineScanCode")
	public String deleteVaccineScanCode(VaccineScanCodeVo vo , Model model) {
		vaccineService.deleteVaccineScanCodeById(vo.getId());
		model.addAttribute("vo", vo);
		return "redirect:" + adminPath + "/cms/vaccineScanCodeForm";
	}
}
