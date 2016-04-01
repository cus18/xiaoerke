package com.cxqm.xiaoerke.modules.sys.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cxqm.xiaoerke.modules.sys.entity.DoctorIllnessRelationVo;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cxqm.xiaoerke.common.utils.DateUtils;
import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.bankend.service.impl.DoctorIllnessRelationServiceImpl;
import com.cxqm.xiaoerke.modules.bankend.service.impl.DoctorServiceImpl;
import com.cxqm.xiaoerke.modules.bankend.service.impl.HospitalServiceImpl;
import com.cxqm.xiaoerke.modules.bankend.service.impl.IllnessServiceImpl;
import com.cxqm.xiaoerke.modules.operation.service.OperationHandleService;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;
import com.cxqm.xiaoerke.modules.sys.entity.IllnessVo;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;

/**
 * 用户Controller
 *
 * @author ThinkGem
 * @version 2013-8-29
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/excle")
public class ExcleDataImplController extends BaseController {

    @Autowired
    private SystemService systemService;

    @Autowired
    HospitalServiceImpl HospitalServiceImpl;

    @Autowired
    OperationHandleService OperationHandleService;

    @Autowired
    DoctorServiceImpl DoctorServiceImpl;

    @Autowired
    OperationHandleService operationHandleService;
    @Autowired
    IllnessServiceImpl illnessServceImpl;

    @Autowired
    DoctorIllnessRelationServiceImpl DoctorIllnessRelationServiceImpl;


    /**
     * excle数据导入跳转页面
     *
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "excleDataImpl")
    public String excleDataImpl() {

        return "modules/sys/excleDataImpl";
    }


//=========================================================医院excle导入================================================

    /**
     * 医院excle导入
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "hospitalExcleUpload")
    public String hospitalExcleUpload(@RequestParam("files") MultipartFile file, Model model) {
        String excleName = "hospital";
//         判断文件是否为空
        if (!file.isEmpty()) {
            try {
                //生成文件并返回文件路径
                String hospitalPath = produceFileMethod(file, excleName);

                //解析excle,倒入操作，flag判断是否导入数据成功
                String flag = operationhospitalInfo(hospitalPath,model);

                model.addAttribute("message", flag);

            } catch (Exception e) {
                model.addAttribute("message", "对不起！导入医院数据失败！请检查数据源类型和数据是否正确");
                e.printStackTrace();
            } finally {
                return "modules/sys/excleDataImpl";
            }
        }

        return "modules/sys/excleDataImpl";
    }
//=========================================================疾病excle结束================================================

    /**
     * 疾病excle导入
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "illnessExcleUpload")
    public String illnessExcleUpload(@RequestParam("files") MultipartFile file, Model model) {
        String excleName = "illness";
//       //判断文件是否为空
        if (!file.isEmpty()) {
            try {
                //生成文件并返回文件路径
                String hospitalPath = produceFileMethod(file, excleName);

                //解析excle  flag判断是否导入数据成功
                String flag = operationIllness(hospitalPath);

                model.addAttribute("message", flag);

            } catch (Exception e) {
                model.addAttribute("message", "对不起！导入疾病数据失败！请检查数据源类型和数据是否正确");
                e.printStackTrace();
            } finally {
                return "modules/sys/excleDataImpl";
            }
        }
        return "modules/sys/excleDataImpl";
    }

    //删除当前文件
//    private void fileDelete(String excleName) {
//        File deleteFile = new File(excleName);
//        deleteFile.delete();
//    }


//=========================================================医生与疾病excle导入================================================

    /**
     * 医生与疾病excle导入
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "illnessAndDoctorRelation")
    public String illnessAndDoctorRelation(@RequestParam("files") MultipartFile file, Model model) {
        String excleName = "illnessAndDoctorRelation";
        String hospitalPath ="";
//       //判断文件是否为空
        if (!file.isEmpty()) {
            try {
                //生成文件并返回文件路径
                hospitalPath = produceFileMethod(file, excleName);

                //解析excle  flag判断是否导入数据成功
                String flag = illnessAndDoctorRelation(hospitalPath);

                model.addAttribute("message", flag);

            } catch (Exception e) {
                model.addAttribute("message", "对不起！导入医生与疾病数据失败！请检查数据源类型和数据是否正确");
                e.printStackTrace();
            } finally {
                DeleteFolder(hospitalPath);
                return "modules/sys/excleDataImpl";
            }
        }
        return "modules/sys/excleDataImpl";
    }


    //=========================================================医生信息excle导入================================================

    /**
     * 医生信息excle导入
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "doctorDataImpl")
    public String doctorDataImpl(@RequestParam("files") MultipartFile file, Model model) {
        String excleName = "doctor";
//       //判断文件是否为空
        if (!file.isEmpty()) {
            try {
                //生成文件并返回文件路径
                String hospitalPath = produceFileMethod(file, excleName);

                //解析excle  flag判断是否导入数据成功
                String flag = operationDoctor(hospitalPath);

                model.addAttribute("message", flag);

            } catch (Exception e) {
                model.addAttribute("message", "对不起！导入医生数据失败！请检查数据源类型和数据是否正确");
                e.printStackTrace();
            } finally {
                return "modules/sys/excleDataImpl";
            }
        }
        return "modules/sys/excleDataImpl";
    }
    //=========================================================号源信息excle导入================================================

    /**
     * 号源信息excle导入
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "RegisterServiceDataImpl")
    public String RegisterServiceDataImpl(@RequestParam("files") MultipartFile file, Model model) {
        String excleName = "RegisterService";
//       //判断文件是否为空
        if (!file.isEmpty()) {
            try {
                //生成文件并返回文件路径
                String hospitalPath = produceFileMethod(file, excleName);

                //解析excle  flag判断是否导入数据成功
                String flag = operationRegisterService(hospitalPath);

                model.addAttribute("message", flag);


            } catch (Exception e) {
                model.addAttribute("message", "对不起！导入号源数据失败！请检查数据源类型和数据是否正确");
                e.printStackTrace();
            } finally {
                return "modules/sys/excleDataImpl";
            }
        }
        return "modules/sys/excleDataImpl";
    }
    //=========================================================================================================

    /**
     * 导入号源信息
     *
     * @return
     * @throws IOException
     * @throws BiffException
     */
    public String operationRegisterService(String path) throws IOException, BiffException {
        HashMap<String, Object> response = new HashMap<String, Object>();
        Workbook rwb;
        int count=0;
        //创建输入流
        InputStream stream = new FileInputStream(path);
        //获取Excel文件对象
        rwb = Workbook.getWorkbook(stream);
        //获取文件的指定工作表 默认的第一个
        Sheet sheet = rwb.getSheet(0);
        int rowTotalNumber = sheet.getRows();
        //行数(表头的目录不需要，从1开始)
        for (int i = 1; i < rowTotalNumber; i++) {
            int j = 0;//此表从手机号开始读取数据
            //获得医生表数据
            HashMap<String, Object> doctorInfo = new HashMap<String, Object>();
            String location_id = IdGen.uuid();
            if (!"号源信息".equals(sheet.getCell(j++, i).getContents())) {
                return "对不起，非法数据源，请按照模版进行数据导入！";
            }
            //如果手机号为空则是最后一条记录
            String isEnd = sheet.getCell(++j, i).getContents();
            if ("".equals(isEnd) || isEnd == null) {
                break;
            }
            //根据手机号查询doctorId
            String doctorId = operationHandleService.findDoctorIdByPhone(isEnd);
            if (!StringUtils.isNotNull(doctorId)) {
                return "excle文件中的第"+i+"行的医生在系统中不存在，请确认后再操作！";
            }
            doctorInfo.put("doctorId", doctorId);
            doctorInfo.put("sys_doctor_id", doctorId);
            //根据医院名称查询hospitalId
            doctorInfo.put("hospitalName", sheet.getCell(++j, i).getContents());
            String hospitalId = operationHandleService.findhospitalId((String) doctorInfo.get("hospitalName"));
            if (!StringUtils.isNotNull(hospitalId)) {
                return "excle文件中的第"+i+"行的医院在系统中不存在，请确认后再操作！";
            }
            doctorInfo.put("sys_hospital_id", hospitalId);
            doctorInfo.put("id", IdGen.uuid());
            //特别注意时间为空的插不进去，待改进
            String dates = sheet.getCell(++j, i).getContents();
            if (!StringUtils.isNotNull(dates)) {
                return "excle文件中的第"+i+"行的医生的出诊时间为空，请确认后再操作！";
            }
            doctorInfo.put("date", dates);

            doctorInfo.put("price", sheet.getCell(++j, i).getContents());
            String date = sheet.getCell(++j, i).getContents();
            if (!"".equals(date) && date != null) {
                Date begin_time = StrToDate(date);
                //结束时间自动加15分钟
                Date end_time = new Date(begin_time.getTime() + 900000);
                doctorInfo.put("begin_time", begin_time);
                doctorInfo.put("end_time", end_time);
            }
//            doctorInfo.put("end_time", StrToDate(sheet.getCell(++j, i).getContents()));
            doctorInfo.put("location", sheet.getCell(++j, i).getContents());
            doctorInfo.put("desc", sheet.getCell(++j, i).getContents());
            doctorInfo.put("status", "0");//号源状态
            doctorInfo.put("deposit", "50");//所需押金
            doctorInfo.put("service_type", sheet.getCell(++j, i).getContents());
            doctorInfo.put("root", sheet.getCell(++j, i).getContents());
            doctorInfo.put("create_date", new Date());
            //查询当前地址是否存在，如果已存在则返回sys_doctor_location的id主键,否则生成一个新地址
            String sysDoctorLocationId = operationHandleService.findDoctorExistLocation(doctorInfo);

            DoctorVo doctorVo = new DoctorVo();
            doctorVo.setId((String) doctorInfo.get("doctorId"));
            DoctorVo doctorVoinfos = DoctorServiceImpl.findDoctorDetailInfo(doctorVo).get(0);
            doctorInfo.put("doctorName", doctorVoinfos.getDoctorName());

            if (sysDoctorLocationId != null && !"".equals(sysDoctorLocationId)) {
                location_id = sysDoctorLocationId;
                doctorInfo.put("location_id", location_id);
            } else {
                //往sys_location表里插入增加数据
                doctorInfo.put("location_id", location_id);
                operationHandleService.insertSysDoctorLocation(doctorInfo);
            }
            count++;
            //执行插入操作
            operationHandleService.insertSysRegisterServiceTest(doctorInfo);

        }
        return "恭喜您已经成功导入"+count+"条数据记得核对哦~";
    }


    /**
     * 生成“医生表”和“医生与医院关联表”信息（前提是已经导入了医院表）
     *
     * @return
     * @throws IOException
     * @throws BiffException
     */
    public String operationDoctor(String path) throws IOException, BiffException {
        Workbook rwb;
        int count=0;
        //创建输入流
        InputStream stream = new FileInputStream(path);
        //获取Excel文件对象
        rwb = Workbook.getWorkbook(stream);
        //获取文件的指定工作表 默认的第一个
        Sheet sheet = rwb.getSheet(0);
        int rowTotalNumber = sheet.getRows();
        //行数(表头的目录不需要，从1开始)
        for (int i = 1; i < rowTotalNumber; i++) {
            int j = 0;
            String flag=sheet.getCell(j++, i).getContents();
            if (!"医生信息".equals(flag) && StringUtils.isNotNull(flag)) {
                return "对不起，非法数据源，请按照模版进行数据导入！";
            }
            //获得医生表数据
            HashMap<String, Object> doctorInfo = new HashMap<String, Object>();
            doctorInfo.put("doctorName", sheet.getCell(j, i).getContents());
            doctorInfo.put("hospitalName", sheet.getCell(++j, i).getContents());
            doctorInfo.put("position1", sheet.getCell(++j, i).getContents());
            doctorInfo.put("position2", sheet.getCell(++j, i).getContents());
            doctorInfo.put("career_time", sheet.getCell(++j, i).getContents());
            doctorInfo.put("experince", sheet.getCell(++j, i).getContents());
            doctorInfo.put("card_experince", sheet.getCell(++j, i).getContents());
            doctorInfo.put("personal_details", sheet.getCell(++j, i).getContents());
            //医生与医院关系表数据
            doctorInfo.put("place_detail", sheet.getCell(++j, i).getContents());
            doctorInfo.put("relation_type", sheet.getCell(++j, i).getContents());
            doctorInfo.put("department_level1", sheet.getCell(++j, i).getContents());
            doctorInfo.put("department_level2", sheet.getCell(++j, i).getContents());
            doctorInfo.put("contact_person", sheet.getCell(++j, i).getContents());
            doctorInfo.put("contact_person_phone", sheet.getCell(++j, i).getContents());
            String isEnd = sheet.getCell(++j, i).getContents();
            if ("".equals(isEnd) || isEnd == null) { //如果手机号为空则是最后一条记录
                break;
            }
            String hopitalId = operationHandleService.findhospitalId((String) doctorInfo.get("hospitalName"));
            if (!StringUtils.isNotNull(hopitalId)) {
                return "对不起，文件中有不存在的医院，请确认！大约在excle中的第"+i+"行";
            }
            doctorInfo.put("phone", sheet.getCell(j, i).getContents());

            //校验 根据手机号查询是否有已存在医生，有已存在医生，避免勿操作，提示多少行提示用户更新，同时禁止导入
            String doctorId = operationHandleService.findDoctorIdByPhone((String)doctorInfo.get("phone"));
            if(StringUtils.isNotNull(doctorId)){
                return "对不起，文件中有已存在医生，为避免误操作，请特殊处理，该医生大约在excle中第"+i+"行";
            }
            count++;
            doctorInfo.put("insertDoctorLoactinFlag","0");
            operationHandleService.CreateDoctor(doctorInfo);
        }
        return "恭喜您已经成功导入"+count+"条数据记得核对哦~";
    }

    /**
     * 读取医院excle数据
     *
     * @param path
     * @throws IOException
     * @throws BiffException
     */
    public String operationhospitalInfo(String path,Model model) {
        Workbook rwb;
        int count=0;
        Sheet sheet =null;
        List<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
        //创建输入流
        InputStream stream;
        try {
            stream = new FileInputStream(path);
            //获取Excel文件对象
            rwb = Workbook.getWorkbook(stream);
            sheet = rwb.getSheet(0);
        } catch (Exception e) {
            model.addAttribute("message","请导入excle类型的数据");
            e.printStackTrace();
        }
        //获取文件的指定工作表 默认的第一个
        int rowTotalNumber = sheet.getRows();
        //行数(表头的目录不需要，从1开始)
        for (int i = 1; i < rowTotalNumber; i++) {
            int j = 0;
            //获得单元格数据
            HashMap<String, Object> hospitalInfo = new HashMap<String, Object>();
            hospitalInfo.put("id", IdGen.uuid());
            //判断导入的确实是医院数据
            String flag =sheet.getCell(j++, i).getContents();
            if (!"医院信息".equals(flag) && flag!=null && !"".equals(flag)) {
                return "对不起，非法数据源，请按照模版进行数据导入！，请按照模版进行数据导入！";
            }
            hospitalInfo.put("name", sheet.getCell(j, i).getContents());
            String isEnd = sheet.getCell(j, i).getContents();
            if ("".equals(isEnd) || isEnd == null) { //如果医院名称为空则是最后一条记录
                break;
            }
            hospitalInfo.put("position", sheet.getCell(++j, i).getContents());
            hospitalInfo.put("details", sheet.getCell(++j, i).getContents());
            hospitalInfo.put("cityName", sheet.getCell(++j, i).getContents());
            hospitalInfo.put("medicalProcess", sheet.getCell(++j, i).getContents());
            //判断此医院是否已经存在
            if (StringUtils.isNotNull(OperationHandleService.findhospitalId((String) hospitalInfo.get("name")))) {
                //插入医院信息数据
                return "对不起，有已存在医院信息,大约在excle中的第"+i+"行,请确认！";
            }
            count++;
            arrayList.add(hospitalInfo);
        }
        operationHandleService.insertHospitalList(arrayList);
        return "恭喜您已经成功导入"+count+"条数据记得核对哦~";
    }

    /**
     * 读取疾病excle数据
     *
     * @return
     * @throws IOException
     * @throws BiffException
     */
    public String operationIllness(String path) throws IOException, BiffException {
        int count=0;
        Workbook rwb;
        List<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
        //创建输入流
        InputStream stream = new FileInputStream(path);
        //获取Excel文件对象
        rwb = Workbook.getWorkbook(stream);
        //获取文件的指定工作表 默认的第一个
        Sheet sheet = rwb.getSheet(0);
        int rowTotalNumber = sheet.getRows();
        //行数(表头的目录不需要，从1开始)
        for (int i = 1; i < rowTotalNumber; i++) {
            int j = 0;

            //判断导入的确实是疾病数据
            if (!"疾病信息".equals(sheet.getCell(j++, i).getContents())) {
                return "对不起，非法数据源，请按照模版进行数据导入！";
            }

            //获得单元格数据
            HashMap<String, Object> doctorInfo = new HashMap<String, Object>();
            doctorInfo.put("id", IdGen.uuid());
            doctorInfo.put("level_1", sheet.getCell(j, i).getContents());
            doctorInfo.put("level_2", sheet.getCell(++j, i).getContents());
            String isEnd = sheet.getCell(j, i).getContents();
            if ("".equals(isEnd) || isEnd == null) { //如果手机号为空则是最后一条记录
                break;
            }
            IllnessVo illnessVo = new IllnessVo();
            illnessVo.setLevel_1((String) doctorInfo.get("level_1"));
            illnessVo.setLevel_2((String) doctorInfo.get("level_2"));
            boolean illnessExist = illnessServceImpl.judgeIllnessExist(illnessVo);
            if (illnessExist == true) {
                return "对不起，有已存在疾病信息,大约在文件中的第"+i+"行,请确认！";
            }
            count++;
            arrayList.add(doctorInfo);
        }
        //批量插入疾病信息
        operationHandleService.insertIllness(arrayList);
        return "恭喜您已经成功导入"+count+"条数据记得核对哦~";
    }

    /**
     * 读取医生和疾病关系的excle数据
     *
     * @return
     * @throws IOException
     * @throws BiffException
     */
    public String illnessAndDoctorRelation(String path) throws IOException, BiffException {
        InputStream stream = new FileInputStream(path);
        Workbook rwb;
        int count=0;
        List<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
        //创建输入流
        //获取Excel文件对象
        rwb = Workbook.getWorkbook(stream);
        //获取文件的指定工作表 默认的第一个
        Sheet sheet = rwb.getSheet(0);
        int rowTotalNumber = sheet.getRows();
        //行数(表头的目录不需要，从1开始)
        for (int i = 1; i < rowTotalNumber; i++) {

            int j = 0;//从医生的手机号开始读取
            //获得单元格数据
            HashMap<String, Object> doctorInfo = new HashMap<String, Object>();
            doctorInfo.put("id", IdGen.uuid());
            String info = sheet.getCell(j, i).getContents();
            if ("".equals(info) || info == null) {
                break;
            }
            //判断导入的确实是疾病数据
            if (!"医生与疾病信息".equals(info)) {
                return "对不起，非法数据源，请按照模版进行数据导入！";
            }
            j++;
            //根据手机号查询doctorId
            String phone = sheet.getCell(++j, i).getContents();
            String doctorId = operationHandleService.findDoctorIdByPhone(phone);
            if(!StringUtils.isNotNull(doctorId)){
                return "医生在系统中不存在，请确认！大约在文件中的第"+(i+1)+"行";
            }
            doctorInfo.put("doctorId", doctorId);

            //根据level_1,level_2查询sys_illness主键
            String level_2 = sheet.getCell(++j, i).getContents();
            String level_1 = sheet.getCell(++j, i).getContents();
            DoctorIllnessRelationVo doctorIllnessRelationVo = new DoctorIllnessRelationVo();
            doctorIllnessRelationVo.setLevel_1(level_1);
            doctorIllnessRelationVo.setLevel_2(level_2);
            doctorIllnessRelationVo.setSys_doctor_id(doctorId);
            //校验  查询疾病在疾病库中是否已存在
            IllnessVo illnessVo = DoctorIllnessRelationServiceImpl.findSysIllnessInfo(doctorIllnessRelationVo);
            if (illnessVo!=null && StringUtils.isNotNull(illnessVo.getId())) {
                doctorInfo.put("sys_illness_id", illnessVo.getId());
            } else {
                return "对不起，有疾病在疾病库中不存在,大约在文件中的第"+i+"行,请确认！";
            }
            //校验，根据医生的主键和疾病主键查询此该医生是否已经关联了该疾病
            doctorIllnessRelationVo.setSys_doctor_id(doctorId);
            doctorIllnessRelationVo.setSys_illness_id(illnessVo.getId());
            DoctorIllnessRelationVo resultVo = DoctorIllnessRelationServiceImpl.findDoctorIllnessRelationInfo(doctorIllnessRelationVo);
            //如果已关联
            if(resultVo != null){
                return "对不起，有已存在医生与疾病关联,大约在文件中的第"+i+"行,请确认！";
            }

            String isEnd = sheet.getCell(j, i).getContents();
            //如果手机号为空则是最后一条记录
            if ("".equals(isEnd) || isEnd == null) {
                break;
            }
            count++;
            arrayList.add(doctorInfo);

        }
        //批量插入疾病与医生关联表数据
        operationHandleService.insertIllnessRelation(arrayList);
        stream.close();
        return "恭喜您已经成功导入"+count+"条数据记得核对哦~";
    }


    /**
     * 生成文件并返回文件路径
     *
     * @param file
     * @param excleName
     * @return
     * @throws IOException
     */
    private String produceFileMethod(@RequestParam("files") MultipartFile file, String excleName) throws IOException {
        Date date = new Date();
        String strDate = DateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss");
        String path = strDate.substring(0, 4) + "_" + strDate.substring(5, 7) + "_" + strDate.substring(8, 10) + "_" + strDate.substring(11, 13) + "_" + strDate.substring(14, 16) + "_" + excleName;
        File headPath = new File("./" + path);
        String hospitalPath = "./" + path;
        //判断文件夹是否创建，没有创建则创建新文件夹
        if (!headPath.exists()) {
            headPath.mkdirs();
        }
        file.transferTo(headPath);
        return hospitalPath;
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @return date
     */
    public static Date StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    //================================文件下载================================================


    @RequiresPermissions("user")
    @RequestMapping(value = "downMondle")
    public void downMondle(String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {//获取要下载的文件名
        //得到向客服端输出的输出流
        OutputStream outputStream = response.getOutputStream();
        //输出文件用的字节数组，每次向输出流发送600个字节
        byte b[] = new byte[600];
        //要下载的文件
        File fileload = new File("D:/excleTemplate", fileName);
        //客服端使用保存文件的对话框
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        //通知客服文件的MIME类型
        response.setContentType("application/msword");
        //通知客服文件的长度
        long fileLength = fileload.length();
        String length = String.valueOf(fileLength);
        response.setHeader("Content_length", length);
        //读取文件，并发送给客服端下载
        FileInputStream inputStream = new FileInputStream(fileload);
        int n = 0;
        while ((n = inputStream.read(b)) != -1) {
            outputStream.write(b, 0, n);
        }
    }

    /**
     *  根据路径删除指定的目录或文件，无论存在与否
     *@param sPath  要删除的目录或文件
     *@return 删除成功返回 true，否则返回 false。
     */
    public boolean DeleteFolder(String sPath) {
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return false;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
//                reader.close() ;
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public boolean deleteFile(String sPath) {
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     * @param   sPath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    public boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
       boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }
}


