package com.cxqm.xiaoerke.modules.sys.web;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.bankend.service.impl.DoctorIllnessRelationServiceImpl;
import com.cxqm.xiaoerke.modules.bankend.service.impl.IllnessServiceImpl;
import com.cxqm.xiaoerke.modules.operation.service.OperationHandleService;
import com.cxqm.xiaoerke.modules.search.entity.SearchKeyword;
import com.cxqm.xiaoerke.modules.search.service.util.RdsDataSourceJDBC;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorIllnessRelationVo;
import com.cxqm.xiaoerke.modules.sys.entity.IllnessVo;
import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户Controller
 *
 * @author ThinkGem
 * @version 2013-8-29
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/keyword")
public class keyWordController extends BaseController {


    @Autowired
    OperationHandleService operationHandleService;

    @Autowired
    RdsDataSourceJDBC rdsDataSourceJDBC;

    @Autowired
    IllnessServiceImpl illnessServiceImpl;

    @Autowired
    DoctorIllnessRelationServiceImpl doctorIllnessRelationServiceImpl;


    /**
     * 获取所有的热词
     *
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"findAllkeyword", ""})
    public String findAllkeyword(String flag,SearchKeyword SearchKeyword,Model model, HttpServletRequest request, HttpServletResponse response,  String keyword) {
        List<SearchKeyword> lists;
        if (StringUtils.isNotNull(keyword)) {
            lists = rdsDataSourceJDBC.findAllKeyWord(keyword, "like");//根据keyword模糊查询
        } else {
            lists = rdsDataSourceJDBC.findAllKeyWord(keyword, "all");//查询所有热词
        }
        if("1".equals(flag)){
            model.addAttribute("message", "恭喜您，保存成功！");
        }
        model.addAttribute("list", lists);
        model.addAttribute("SearchKeyword", SearchKeyword);
        return "modules/sys/keyWordManage";
    }

    /**
     * 获取所有的热词
     *
     * @return
     */
    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = "addKeyword")
    public String addKeyword(SearchKeyword SearchKeyword, Model model, String keyword) {
        //keyword不为空保存操作
        if (StringUtils.isNotNull(keyword)) {
            //校验，查看热词是否已经存在
            List<SearchKeyword> lists = rdsDataSourceJDBC.findAllKeyWord(keyword, "notlike");
            if (lists.size() == 0) {
                //保存热词
//                rdsDataSourceJDBC.saveKeyWord(keyword);
                model.addAttribute("message", "热词保存成功！");
            } else {
                model.addAttribute("message", "热词已存在，请勿重复添加！");
            }
        }else {
            return "modules/sys/addKeyword";
        }

        model.addAttribute("SearchKeyword", SearchKeyword);
        List<SearchKeyword> lists = rdsDataSourceJDBC.findAllKeyWord(keyword, "all");//查询所有热词
        model.addAttribute("list", lists);
        return "modules/sys/keyWordManage";
    }

    /**
     * 删除热词
     * @param id
     */
    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = "deletekeyword")
    public String deletekeyword(String id,Model model,RedirectAttributes redirectAttributes){
//        rdsDataSourceJDBC.deletekeyword(id);
        List<SearchKeyword> lists = rdsDataSourceJDBC.findAllKeyWord("", "all");//查询所有热词
        model.addAttribute("list", lists);
        model.addAttribute("SearchKeyword", new SearchKeyword());
        addMessage(redirectAttributes, "删除热词成功");
        return  "redirect:" + adminPath + "/sys/keyword/findAllkeyword?repage";
    }

    /**
     * 跳转到热词与疾病关联页面
     * @param id
     */
    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = "keywordAndillnessRelation")
    public String keywordAndillnessRelation(String id,Model model,IllnessVo illnessVo){
        //查询所有的一类疾病
        Page<IllnessVo> page = illnessServiceImpl.findIllnessVoList(new Page<IllnessVo>(0, 1000), illnessVo);
        //便于界面显示，每个illnessVo中再放一个list<illnessVo> 用来存放当level_1对应的level_2
        if (page != null) {
            List<IllnessVo> list = page.getList();
            List<IllnessVo> voList = new ArrayList<IllnessVo>();
            for (IllnessVo Vo : list) {
                //根据level_1查询对应的level_2信息
                List<IllnessVo> illnessVoList = illnessServiceImpl.findIllnessLevel_2List(Vo);
                Vo.setList(illnessVoList);
                //将带有level_2的list放入新的list里
                voList.add(Vo);
            }
            page.setList(voList);
            model.addAttribute("page", page);
            SearchKeyword searchKeyword = new SearchKeyword();
            searchKeyword.setId(Integer.parseInt(id));
            model.addAttribute("SearchKeyword", searchKeyword);
        }
        return "modules/sys/keyWordAndIllnessRelation";
    }



    /**
     * 界面初始化 和 保存疾病与热词关系
     * @return
     */
    @RequestMapping(value = "keywordAndillnessRelationData")
    @ResponseBody
    public String keywordAndillnessRelationData(Model model, HttpServletRequest request, String tArray, String keyWordId) {
        //热词与疾病的关系修改之后保存操作
        if (StringUtils.isNotNull(tArray)) {
            String[] str = tArray.split(",");
            StringBuffer stringBuffer = new StringBuffer();//用于存放疾病表主键
            for (int i = 0; i < str.length; i++) {
                String[] save_str = str[i].split("-");
                DoctorIllnessRelationVo doctorIllnessRelationVo = new DoctorIllnessRelationVo();
                //根据一级疾病和二级疾病查询疾病主键
                doctorIllnessRelationVo.setLevel_1(save_str[0]);
                doctorIllnessRelationVo.setLevel_2(save_str[1]);
                IllnessVo illnessVo = doctorIllnessRelationServiceImpl.findSysIllnessInfo(doctorIllnessRelationVo);
                stringBuffer.append(illnessVo.getId());
                stringBuffer.append(",");
            }
            //保存疾病与热词关系
//            rdsDataSourceJDBC.deleteAndSaveKeyWordAndIllnessRelation(stringBuffer.toString(),keyWordId);


        } else {//界面初始化（加载当前医生与疾病关联数据）
            JSONObject resultMap = new JSONObject();
            //查询与该热词有关联的疾病主键
//            List<String> arrayList = rdsDataSourceJDBC.findAllillnessKeyByKeyword(keyWordId);
//            resultMap.put("list", arrayList);
            return resultMap.toString();
        }
        return null;
    }
}
