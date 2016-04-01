package com.cxqm.xiaoerke.modules.sys.web;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.bankend.service.impl.IllnessServiceImpl;
import com.cxqm.xiaoerke.modules.sys.entity.IllnessVo;

/**
 * 用户Controller
 *
 * @author ThinkGem
 * @version 2013-8-29
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/illnessManage")
public class IllnessManageController extends BaseController {

    @Autowired
    IllnessServiceImpl illnessServiceImpl;


    /**
     * 获取疾病库信息列表
     *
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"illnessManage", ""})
    public String illnessManage(String level_1, String level_2, Model model) {
        HashMap<String, Object> searchMap = new HashMap<String, Object>();
        searchMap.put("level_1", level_1);
        searchMap.put("level_2", level_2);
        //获取疾病库信息
        Page<IllnessVo> page = illnessServiceImpl.findAllIllness(new Page<IllnessVo>(0, 10000), searchMap);
        model.addAttribute("page", page);
        IllnessVo Vo = new IllnessVo();
        model.addAttribute("illnessVo", Vo);
        return "modules/sys/illnessManage";
    }

    /**
     * 录入疾病或者修改疾病信息
     *
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "illnessManagerImpl")
    public String illnessManagerImpl(String id, String level_1, String level_2, Model model) {
    	IllnessVo illnessVo = new IllnessVo();
        illnessVo.setLevel_1(level_1);
        illnessVo.setLevel_2(level_2);
        //id不为空执行修改操作
        if (StringUtils.isNotNull(id)) {
            illnessVo.setId(id);
            illnessServiceImpl.updateIllness(illnessVo);
            model.addAttribute("message", "疾病信息修改成功！");
            illnessManage("", "", model);
            return "modules/sys/illnessManage";
        } else { //id为空执行保存操作
            if (StringUtils.isNotNull(level_1) && StringUtils.isNotNull(level_2)) {
                illnessVo.setId(IdGen.uuid());
                //根据一级疾病和二级疾病查询疾病是否已经存在
                boolean illnessExist = illnessServiceImpl.judgeIllnessExist(illnessVo);
                if (illnessExist) {
                    model.addAttribute("message", "该疾病已存在，请勿重复添加！");
                    IllnessVo Vo = new IllnessVo();
                    model.addAttribute("illnessVo", Vo);
                    return "modules/sys/illnessDataImp";
                }
                //将疾病信息插入到数据库
                illnessServiceImpl.insertIllnessData(illnessVo);
                model.addAttribute("message", "疾病信息录入成功！");
            }
        }
        IllnessVo Vo = new IllnessVo();
        model.addAttribute("illnessVo", Vo);
        return "modules/sys/illnessDataImpForKu";
    }

    /**
     * 疾病信息执行修改操作跳转页面
     *
     * @param isEdit no:不是修改操作  否则执行修改操作
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "illnessManagerEdit")
    public String illnessManagerEdit(String id, Model model, HttpServletRequest request, String isEdit) throws Exception {
        String level_1 = new String(request.getParameter("level_1").getBytes("ISO-8859-1"), "UTF-8");
        String level_2 = new String(request.getParameter("level_2").getBytes("ISO-8859-1"), "UTF-8");
        IllnessVo illnessVo = new IllnessVo();
        illnessVo.setId(id);
        illnessVo.setLevel_1(level_1);
        illnessVo.setLevel_2(level_2);
        model.addAttribute("illnessVo", illnessVo);
        return "modules/sys/illnessDataImpForKu";
    }

    /**
     * 疾病信息删除操作
     *
     * @param
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "illnessManagerDelete")
    public String illnessManagerDelete(String id, Model model) throws Exception {

    	IllnessVo illnessVo = new IllnessVo();
        illnessVo.setId(id);
        //根据疾病id删除疾病
        illnessServiceImpl.deleteIllnessById(illnessVo);
        model.addAttribute("message", "删除成功！");
        return illnessManage("", "", model);
    }

    /**
     * 疾病信息删除(多条)操作
     * @param
     * @return
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "illnessManagerDeleteArray")
    public String illnessManagerDeleteArray(String tArray, Model model) throws Exception {
        String[] illnessIds = tArray.split("-");
        for (int i = 0; i < illnessIds.length; i++) {
        	IllnessVo illnessVo = new IllnessVo();
            illnessVo.setId(illnessIds[i]);
            illnessServiceImpl.deleteIllnessById(illnessVo);
        }
        return "123";
    }


}
