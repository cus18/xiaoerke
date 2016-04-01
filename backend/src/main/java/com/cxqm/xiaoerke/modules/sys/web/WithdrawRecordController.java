package com.cxqm.xiaoerke.modules.sys.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.account.entity.WithdrawRecord;
import com.cxqm.xiaoerke.modules.account.service.AccountService;


@Controller
@RequestMapping(value = "${adminPath}/sys/account")
public class WithdrawRecordController extends BaseController{

	@Autowired
	AccountService accountService;
	
	/**
	 * 获取提现纪录 
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = {"withdrawRecord"})
	public String withdrawManage(WithdrawRecord withdrawRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		String temp = ((String)request.getParameter("pageNo"));
		Page<WithdrawRecord> pagess = null;
		if(temp==null){
			pagess = new Page<WithdrawRecord>();
			}else{
				Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
				Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
				System.out.println("pageNo:"+pageNo);
				System.out.println("pageSize:"+pageSize);
				pagess = new Page<WithdrawRecord>(pageNo,pageSize);
			}
		String orderBy = request.getParameter("orderBy");
		if(null!=orderBy&&!"".equals(orderBy)){
			String[] orderBys = request.getParameter("orderBy").split(" ");
			pagess.setOrderBy("CONVERT("+orderBys[0]+" USING gbk)"+orderBys[1]);
		}
		Page<WithdrawRecord> page = accountService.getAllWithdrawRecords( withdrawRecord,pagess);
		page.setOrderBy(orderBy);
		Map<String, Object> priceMap = new HashMap<String, Object>();
		priceMap.put("", "全部");
		priceMap.put("50", "少于50");
		priceMap.put("50-100", "50-100");
		priceMap.put("100", "多于100");
		Map<String, Object> statusMap = new HashMap<String, Object>();
		statusMap.put("", "全部");
		statusMap.put("成功", "成功");
		statusMap.put("等待", "等待");
		statusMap.put("失败", "失败");
        model.addAttribute("page", page);
        model.addAttribute("priceMap", priceMap);
        model.addAttribute("statusMap", statusMap);
        model.addAttribute("withdrawRecord", withdrawRecord);
		return "modules/sys/withdrawRecord";
	}
	
	/**
	 * 提现详情
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = {"withdrawDetail"})
	public String withdrawDetail(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		WithdrawRecord withdrawDetail = accountService.getWithdrawRecordDetail(id);
		Map<String, Object> statusMap = new HashMap<String, Object>();
		statusMap.put("成功", "成功");
		statusMap.put("等待", "等待");
		statusMap.put("失败", "失败");
        model.addAttribute("withdrawDetail", withdrawDetail);
        model.addAttribute("statusMap", statusMap);
		return "modules/sys/withdrawDetail";
	}
	
	/**
     * 保存提现记录
     * @return
     */
	@RequiresPermissions("user")
    @RequestMapping(value = "withdrawSave")
    public String withdrawSave(WithdrawRecord withdrawRecord,RedirectAttributes redirectAttributes, Model model) {
        accountService.withdrawSave(withdrawRecord);
        addMessage(redirectAttributes,"记录修改成功！");
        return "redirect:" + adminPath + "/sys/account/withdrawRecord";
    }
    
    /**
     * 删除提现记录
     * @return
     */
	@RequiresPermissions("user")
    @RequestMapping(value = "withdrawDelete")
    public String withdrawDelete(WithdrawRecord withdrawRecord,RedirectAttributes redirectAttributes, Model model) {
        accountService.withdrawDelete(withdrawRecord.getId());
        addMessage(redirectAttributes, "记录删除成功！");
        return "redirect:" + adminPath + "/sys/account/withdrawRecord";
    }

}
