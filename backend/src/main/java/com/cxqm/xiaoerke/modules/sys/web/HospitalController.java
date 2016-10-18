package com.cxqm.xiaoerke.modules.sys.web;

import com.cxqm.xiaoerke.common.persistence.Page;
import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.utils.OSSObjectTool;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.bankend.service.impl.HospitalServiceImpl;
import com.cxqm.xiaoerke.modules.operation.service.OperationHandleService;
import com.cxqm.xiaoerke.modules.search.service.util.RdsDataSourceJDBC;
import com.cxqm.xiaoerke.modules.sys.entity.HospitalVo;
import com.cxqm.xiaoerke.modules.sys.entity.SysHospitalContactVo;
import com.cxqm.xiaoerke.modules.sys.service.HospitalInfoService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import com.cxqm.xiaoerke.modules.sys.utils.UserUtils;
import com.google.common.collect.Lists;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.util.Date;
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
@RequestMapping(value = "${adminPath}/sys/hospital")
public class HospitalController extends BaseController {

    @Autowired
    private SystemService systemService;

    @Autowired
    HospitalServiceImpl HospitalServiceImpl;

    @Autowired
    OperationHandleService OperationHandleService;

    @Autowired
    RdsDataSourceJDBC rdsDataSourceJDBC;

    @Autowired
    HospitalInfoService hospitalInfoService;


    /**
     * 获取系统所有医院
     *
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = {"hospitalManage", ""})
    public String hospitalManage(HospitalVo hospitalVo, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<HospitalVo> page = HospitalServiceImpl.findAllHospital(new Page<HospitalVo>(), hospitalVo);
        model.addAttribute("page", page);
        return "modules/sys/hospitalManage";
    }

    /**
     * 录入医院基本信息
     *
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "hospitalDataImp")
    public String hospitalDataImp(HospitalVo hospitalVo, Model model,
                                  SysHospitalContactVo contactVo) {
        hospitalVo.setId(IdGen.uuid());
        hospitalVo.setCreateDate(new Date());
        hospitalVo.setCreateBy(UserUtils.getUser().getId());

        if(StringUtils.isNotNull(contactVo.getContactPhone())){
            hospitalVo.setHospitalType("2");
            //将联系人信息插入到sys_hospital_contact表中
            contactVo.setSysHospitalName(hospitalVo.getName());
            contactVo.setSysHospitalId(hospitalVo.getId());

            hospitalInfoService.insertSysHospitalContactVo(contactVo);

            //上传医院图片
            if(StringUtils.isNotNull(contactVo.getHospitalPic())){
                try {
                    uploadHospitalImage(contactVo.getSysHospitalId(),contactVo.getHospitalPic());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //上传诊疗项目图片
            if(StringUtils.isNotNull(contactVo.getClinicItemsPic())){
                try {
                    uploadHospitalImage(contactVo.getSysHospitalId()+"clinicItems",contactVo.getClinicItemsPic());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (StringUtils.isNotNull(hospitalVo.getName()) && StringUtils.isNotNull(hospitalVo.getMedicalProcess()) && StringUtils.isNotNull(hospitalVo.getPosition()) && StringUtils.isNotNull(hospitalVo.getDetails()) && StringUtils.isNotNull(hospitalVo.getCityName())) {

            if (!StringUtils.isNotNull(OperationHandleService.findhospitalId(hospitalVo.getName()))) {
                //插入医院信息数据
                HospitalServiceImpl.insertHospitalData(hospitalVo);
                //掉用rds接口，同步数据到rds上
//                rdsDataSourceJDBC.insertHospitalToRds(hospitalVo);
                model.addAttribute("message", "医院录入成功！");
            } else {
                model.addAttribute("message", "对不起，当前医院已存在！");
            }
        }

        model.addAttribute("contactVo", new SysHospitalContactVo());
        return "modules/sys/hospitalDataImp";
    }

    private void uploadHospitalImage(String id , String src) throws Exception{
        File file = new File(System.getProperty("user.dir").replace("bin", "webapps")+ URLDecoder.decode(src, "utf-8"));
        FileInputStream inputStream = new FileInputStream(file);
        long length = file.length();
        //上传图片至阿里云
        OSSObjectTool.uploadFileInputStream(id, length, inputStream, OSSObjectTool.BUCKET_DOCTOR_PIC);
    }

    /**
     * @param type 类型
     * @return
     * @author zdl
     * 获取医院JSON数据。
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeHospitalData")
    public List<Map<String, Object>> treeHospitalData(@RequestParam(required = false) String type) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        Page<HashMap<String, Object>> page = hospitalInfoService.findPageAllHospital(new Page<HashMap<String, Object>>(1, 10000));
        List<HashMap<String, Object>> hospitalList = page.getList();
        for (int i = 0; i < hospitalList.size(); i++) {
            Map<String, Object> map = hospitalList.get(i);
            map.put("id", map.get("id"));
            map.put("name", map.get("name"));
            if (type != null && "3".equals(type)) {
                map.put("isParent", true);
            }
            mapList.add(map);
        }
        return mapList;
    }


    /**
     * 跳转到医院添加或修改界面
     *
     * @return
     */
    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = "hospitalEdit")
    public String hospitalEdit(HospitalVo hospitalVo, Model model) {
        if (hospitalVo.getId() == null || "".equals(hospitalVo.getId())) {
            return "modules/sys/hospitalEdit";
        } else {
            //根据医院id查询医院信息
            HospitalVo resultVo = HospitalServiceImpl.getHospital(hospitalVo.getId());

            if("2".equals(resultVo.getHospitalType())){
                Map map = new HashMap();
                map.put("hospitalId", resultVo.getId());
                List<SysHospitalContactVo> list = hospitalInfoService.getHospitalContact(map);
                model.addAttribute("contactVo", list.get(0));
            }else{
                model.addAttribute("contactVo", new SysHospitalContactVo());
            }
            model.addAttribute("hospitalVo", resultVo);
            return "modules/sys/hospitalEdit";
        }
    }

    /**
     * 医院信息修改后保存操作
     *
     * @return
     */

    @RequestMapping(value = "hospitalSave")
    public String hospitalSave(HospitalVo hospitalVo, Model model,
                               SysHospitalContactVo contactVo, RedirectAttributes redirectAttributes) {
        //医院修改操作
        hospitalVo.setId(contactVo.getSysHospitalId());
        HospitalServiceImpl.saveEditHospital(hospitalVo,contactVo);
        addMessage(redirectAttributes, "恭喜您，" + "信息修改成功！");
        return "redirect:" + adminPath + "/sys/hospital/hospitalManage?repage";
    }


    /**
     * 删除所选医生
     *
     * @return
     */
    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = "hospitalDelete")
    public String hospitalDelete(HospitalVo hospitalVo, RedirectAttributes redirectAttributes) {
        HospitalServiceImpl.deleteHospitalByHospitalId(hospitalVo.getId());
        addMessage(redirectAttributes, "删除医院成功");
        return "redirect:" + adminPath + "/sys/hospital/hospitalManage?repage";
    }


}
