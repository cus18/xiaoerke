package com.cxqm.xiaoerke.modules.sys.web;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.bankend.service.impl.DoctorIllnessRelationServiceImpl;
import com.cxqm.xiaoerke.modules.bankend.service.impl.IllnessServiceImpl;
import com.cxqm.xiaoerke.modules.operation.service.OperationHandleService;
import com.cxqm.xiaoerke.modules.search.service.util.RdsDataSourceJDBC;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorIllnessRelationVo;
import com.cxqm.xiaoerke.modules.sys.entity.IllnessVo;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.google.common.collect.Lists;
import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户Controller
 *
 * @author ThinkGem
 * @version 2013-8-29
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/doctorIllnessRelation")
public class DoctorIllnessRelationController extends BaseController {

    @Autowired
    private SystemService systemService;

    @Autowired
    DoctorIllnessRelationServiceImpl DoctorIllnessRelationServiceImpl;

    @Autowired
    OperationHandleService operationHandleService;

    @Autowired
    IllnessServiceImpl illnessServiceImpl;

    @Autowired
    RdsDataSourceJDBC RdsDataSourceJDBC;
//	@ModelAttribute
//	public hospitalVo get() {
//		if (StringUtils.isNotBlank(id)){
//			return null;
//			return HospitalServiceImpl.getHospital(id);
//		}else{
//			return new hospitalVo();
//
//		}
//		return null;
//	}

    /**
     * 录入医生与疾病基本信息
     *
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "doctorIllnessRelationDataImp")
    public String doctorIllnessRelationDataImp(HttpServletRequest request, String doctorName, String level_1, String level_2, String seachByLevel_1, Model model) throws Exception {
        DoctorIllnessRelationVo doctorIllnessRelationVo = new DoctorIllnessRelationVo();

        //需求：界面上的二级疾病要根据一级疾病过滤。因为没法向 <sys:treeselect  uri=""  中传递过滤参数，界面上的modelAttribute="doctorIllnessRelationVo"为空，
        // 所以在这里做个转换，将界面上的level_1放到doctorIllnessRelationVO里，进而获取参数
        if (StringUtils.isNotNull(seachByLevel_1)) {
            seachByLevel_1 = new String(request.getParameter("seachByLevel_1").getBytes("ISO-8859-1"), "UTF-8");
            doctorIllnessRelationVo.setLevel_1(seachByLevel_1);
            doctorIllnessRelationVo.setDoctorName(doctorName);
            model.addAttribute("doctorIllnessRelationVO", doctorIllnessRelationVo);
            return "modules/sys/doctorIllnessRelationDataImp";
        } else {
            if (StringUtils.isNotNull(doctorName)) {
                String[] strings = doctorName.split(",");
                String[] level_2s = level_2.split(",");
                doctorIllnessRelationVo.setLevel_1(level_1);
                doctorIllnessRelationVo.setLevel_2(level_2s[1]);

                //根据一类疾病和二类疾病查询疾病信息表主键
                IllnessVo illnessVo = DoctorIllnessRelationServiceImpl.findSysIllnessInfo(doctorIllnessRelationVo);
                if (illnessVo == null) {
                    DoctorIllnessRelationVo doctorIllnessRelationVOs = new DoctorIllnessRelationVo();
                    model.addAttribute("doctorIllnessRelationVO", doctorIllnessRelationVOs);
                    model.addAttribute("message", "录入失败，当前疾病组合在疾病库不存在，请确认！");
                    return "modules/sys/doctorIllnessRelationDataImp";
                }

                //将组合的数据插入到doctor_illness_relation中
                doctorIllnessRelationVo.setSys_doctor_id(strings[0]);
                doctorIllnessRelationVo.setId(IdGen.uuid());
                doctorIllnessRelationVo.setSys_illness_id(illnessVo.getId());

                //校验，根据医生的主键和疾病主键查询此该医生是否已经关联了该疾病
                DoctorIllnessRelationVo resultVo = DoctorIllnessRelationServiceImpl.findDoctorIllnessRelationInfo(doctorIllnessRelationVo);
                //如果已关联
                if (resultVo != null) {
                    model.addAttribute("message", "医生与疾病已关联，请勿重复操作！");
                } else {
                    DoctorIllnessRelationServiceImpl.insertDoctorIllnessRelationData(doctorIllnessRelationVo);
                    model.addAttribute("message", "医生与疾病关联录入成功！");
                }

            }
            model.addAttribute("doctorIllnessRelationVO", doctorIllnessRelationVo);
            return "modules/sys/doctorIllnessRelationDataImp";
        }

    }


    /**
     * 获取系统所有的二级疾病
     * @param type 类型
     * @return
     * @author zdl
     * 获取医院JSON数据。
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeLevel_2Data")
    public List<Map<String, Object>> treeLevel_2Data(@RequestParam(required = false) String type, String level_1, HttpServletRequest request) throws Exception {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        //如果level_1不为空，说明此查询的二级疾病需根据一级疾病过滤
        if (StringUtils.isNotNull(level_1)) {
            //得转码两次，我也是醉了
            String seachLevel = new String(request.getParameter("level_1").getBytes("ISO-8859-1"), "UTF-8");
            String seachLeve2 = new String(seachLevel.getBytes("ISO-8859-1"), "UTF-8");
            hashMap.put("illnessName", seachLeve2);
        }

        Page<HashMap<String, Object>> page = illnessServiceImpl.findPageAllLevel_2(new Page<HashMap<String, Object>>(1, 10000), hashMap);

        List<HashMap<String, Object>> doctorList = page.getList();
        for (int i = 0; i < doctorList.size(); i++) {
            Map<String, Object> map = doctorList.get(i);
            map.put("name", map.get("level_2"));
            if (type != null && "3".equals(type)) {
                map.put("isParent", true);
            }
            mapList.add(map);
        }
        return mapList;
    }

    /**
     * 入口：医生修改界面。显示所有一级疾病和二级疾病
     *
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"DoctorIllnessRelation", ""})
    public String DoctorIllnessRelation(IllnessVo illnessVo, Model model, String doctorId) {
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
            DoctorIllnessRelationVo doctorIllnessRelationVo = new DoctorIllnessRelationVo();
            doctorIllnessRelationVo.setSys_doctor_id(doctorId);
            model.addAttribute("doctorIllnessRelationVo", doctorIllnessRelationVo);
        }
        return "modules/sys/doctorAndIllnessRelation";
    }

    /**
     * 界面初始化（加载当前医生与疾病关联数据）
     *
     * @return
     */
    @RequestMapping(value = "DoctorIllnessRelationData")
    @ResponseBody
    public String DoctorIllnessRelationData(Model model, HttpServletRequest request, String tArray, String doctorId) {
        //用户修改之后保存操作
        if (tArray != null) {
            DoctorIllnessRelationVo temVo = new DoctorIllnessRelationVo();
            temVo.setSys_doctor_id(doctorId);
            //根据doctorId删除医生与疾病的所有关联信息（先删除，后保存）
            DoctorIllnessRelationServiceImpl.deleteDoctorAndIllnessRelation(temVo);
            if(!tArray.equals("")){
            String[] str = tArray.split(",");
            StringBuffer stringBuffer = new StringBuffer();//用于存放疾病表主键

                for (int i = 0; i < str.length; i++) {
                    String[] save_str = str[i].split("-");
                    DoctorIllnessRelationVo doctorIllnessRelationVo = new DoctorIllnessRelationVo();
                    doctorIllnessRelationVo.setSys_doctor_id(doctorId);
                    //根据一级疾病和二级疾病查询疾病主键
                    doctorIllnessRelationVo.setLevel_1(save_str[0]);
                    doctorIllnessRelationVo.setLevel_2(save_str[1]);
                    IllnessVo illnessVo = DoctorIllnessRelationServiceImpl.findSysIllnessInfo(doctorIllnessRelationVo);
                    if (illnessVo != null) {
                        doctorIllnessRelationVo.setSys_illness_id(illnessVo.getId());

                        //医生与疾病关系保存操作
                        doctorIllnessRelationVo.setId(IdGen.uuid());
                        DoctorIllnessRelationServiceImpl.insertDoctorIllnessRelationData(doctorIllnessRelationVo);
                    }
                    stringBuffer.append("'");
                    stringBuffer.append(illnessVo.getId());
                    stringBuffer.append("',");
                }


            stringBuffer.deleteCharAt(stringBuffer.length()-1);//去掉最后一个,
            //生成并保存医生与热词关系
//            RdsDataSourceJDBC.insertDoctorAndHotWordsRelationToRds(stringBuffer.toString(),doctorId);
            }
        } else {//界面初始化（加载当前医生与疾病关联数据）
            JSONObject resultMap = new JSONObject();
            List<String> arrayList = new ArrayList<String>();
            if (StringUtils.isNotNull(doctorId)) {
                DoctorIllnessRelationVo doctorIllnessRelationVo = new DoctorIllnessRelationVo();
                doctorIllnessRelationVo.setSys_doctor_id(doctorId);
                List<IllnessVo> result_list = illnessServiceImpl.findSysIllnessBySysDoctorId(doctorIllnessRelationVo);
                for (IllnessVo illnessVo : result_list) {
                    arrayList.add(illnessVo.getId());
                }
                resultMap.put("list", arrayList);
                return resultMap.toString();
            }
        }
        return null;
    }

}
