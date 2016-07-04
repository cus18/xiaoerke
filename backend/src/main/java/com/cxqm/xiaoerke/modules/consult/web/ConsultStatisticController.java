package com.cxqm.xiaoerke.modules.consult.web;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.consult.entity.ConsultStatisticVo;
import com.cxqm.xiaoerke.modules.operation.service.ConsultStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 咨询统计Controller
 *
 * @author deliang
 * @version 2016-06-30
 */
@Controller(value = "consultStatisticController")
@RequestMapping(value = "${adminPath}/consultStatistic")
public class ConsultStatisticController extends BaseController {

    @Autowired
    private ConsultStatisticService consultStatisticService;

    /**
     * 咨询医生列表
     * sunxiao
     *
     * @param
     * @param model
     */
    @RequestMapping(value = "consultStatisticBaseData")
    public String consultStatisticBaseData(Model model, HttpServletRequest request) {

        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        //用户行为统计分析
        if (StringUtils.isNull(startDate)) {//默认查看最近5天的
            Calendar ca = Calendar.getInstance();
            ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE) - 5);
            Date start = ca.getTime();
            startDate = DateUtils.formatDate(start, "yyyy-MM-dd");
            ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DATE) + 5);
            Date end = ca.getTime();
            endDate = DateUtils.formatDate(end, "yyyy-MM-dd");
        }
        List<ConsultStatisticVo> consultStatisticVos = consultStatisticService.getConsultStatisticList(startDate, endDate);
        model.addAttribute("consultStatisticVos", consultStatisticVos);
        ConsultStatisticVo consultStatisticVo = new ConsultStatisticVo();
        consultStatisticVo.setStartDate(startDate);
        consultStatisticVo.setEndDate(endDate);
        model.addAttribute("consultStatisticVo", consultStatisticVo);


        return "operation/consultStatistic";
    }


}
