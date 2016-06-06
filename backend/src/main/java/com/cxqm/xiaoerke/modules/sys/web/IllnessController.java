package com.cxqm.xiaoerke.modules.sys.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.bankend.service.impl.IllnessServiceImpl;
import com.cxqm.xiaoerke.modules.sys.entity.IllnessVo;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;

/**
 * 用户Controller
 * @author ThinkGem
 * @version 2013-8-29
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/illness")
public class IllnessController extends BaseController {

	@Autowired
	private SystemService systemService;

	@Autowired
	IllnessServiceImpl illnessServceImpl;


	/**
	 * 录入疾病信息
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "illnessDataImp")
	public String hospitalDataImp(String level_1,String level_2,String isDisplay,Model model) {
		if(StringUtils.isNotNull(level_1) && StringUtils.isNotNull(level_2)){
			IllnessVo illnessVo = new IllnessVo();
			illnessVo.setId(IdGen.uuid());
			illnessVo.setLevel_1(level_1);
			illnessVo.setLevel_2(level_2);
			illnessVo.setIsDisplay(isDisplay);
			//根据一级疾病和二级疾病查询疾病是否已经存在
			boolean illnessExist = illnessServceImpl.judgeIllnessExist(illnessVo);
			if(illnessExist){
				model.addAttribute("message", "该疾病已存在，请勿重复添加！");
				IllnessVo Vo = new IllnessVo();
				model.addAttribute("illnessVo", Vo);
				return "modules/sys/illnessDataImp";
			}
			
			//将疾病信息插入到数据库
			illnessServceImpl.insertIllnessData(illnessVo);
			model.addAttribute("message", "疾病信息录入成功！");
		}


		IllnessVo Vo = new IllnessVo();
		model.addAttribute("illnessVo", Vo);
		return "modules/sys/illnessDataImp";
	}

	/**
	 * 验证疾病是否存在
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "checkIllness")
	public String checkIllness(HttpServletRequest request) throws  Exception{
		String level_1 = new String(request.getParameter("level_1").getBytes("ISO-8859-1"),"UTF-8");
		String level_2 = new String(request.getParameter("level_2").getBytes("ISO-8859-1"),"UTF-8");
		IllnessVo illnessVo = new IllnessVo();
		illnessVo.setId(IdGen.uuid());
		illnessVo.setLevel_1(level_1);
		illnessVo.setLevel_2(level_2);
		//根据一级疾病和二级疾病查询疾病是否已经存在
		boolean illnessExist = illnessServceImpl.judgeIllnessExist(illnessVo);
		//如果已经存在了
		if(illnessExist){
			return  "true";
		}
		return "false";
	}

}
