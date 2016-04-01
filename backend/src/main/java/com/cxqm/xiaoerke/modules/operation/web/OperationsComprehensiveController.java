package com.cxqm.xiaoerke.modules.operation.web;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.operation.entity.SysStatistics;
import com.cxqm.xiaoerke.modules.operation.service.OperationsComprehensiveService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 业务综合统计 Controller
 *
 * @author deliang
 * @version 2015-11-05
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/OperationsComprehensive")
public class OperationsComprehensiveController extends BaseController {

    @Autowired
    OperationsComprehensiveService operationsComprehensiveService;


    /**
     * 获取业务综合统计数据表
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"operationsComprehensiveMain", ""})
    public String operationsComprehensiveMain(SysStatistics sysStatisticsVo,String search,SysStatistics SysStatistics, HttpServletRequest request, HttpServletResponse response, Model model) {

        if (StringUtils.isNull(sysStatisticsVo.getFromDate())) {//默认查看最近5天的
            Calendar ca = Calendar.getInstance();
            ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE) - 5);
            Date start = ca.getTime();
            String fromDate = DateUtils.formatDate(start, "yyyy-MM-dd");
            ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE)+5);
            Date end = ca.getTime();
            String toDate = DateUtils.formatDate(end, "yyyy-MM-dd");
            sysStatisticsVo.setFromDate(fromDate);
            sysStatisticsVo.setToDate(toDate);
        }

        //获取业务综合统计数据表
        List<SysStatistics> resultlist = operationsComprehensiveService.getOperationsComprehensiveList(sysStatisticsVo);
        model.addAttribute("SysStatistics", resultlist);
        model.addAttribute("sysStatisticsVo",sysStatisticsVo);

        return "operation/operationComprehensive";
    }
}
