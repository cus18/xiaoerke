/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cxqm.xiaoerke.modules.operation.web;

import com.cxqm.xiaoerke.common.utils.IdGen;
import com.cxqm.xiaoerke.common.web.BaseController;
import com.cxqm.xiaoerke.modules.bankend.service.impl.DoctorServiceImpl;
import com.cxqm.xiaoerke.modules.bankend.service.impl.HospitalServiceImpl;
import com.cxqm.xiaoerke.modules.operation.service.OperationHandleService;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorCaseVo;
import com.cxqm.xiaoerke.modules.sys.entity.DoctorVo;
import com.cxqm.xiaoerke.modules.sys.entity.HospitalVo;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 测试Controller
 *
 * @author ThinkGem
 * @version 2013-10-17
 */
@Controller
@RequestMapping(value = "${xiaoerkePath}")
public class operationController extends BaseController {

    @Autowired
    private DoctorServiceImpl DoctorServiceImpl;

    @Autowired
    private HospitalServiceImpl HospitalServiceImpl;

    @Autowired
    private OperationHandleService operationHandleService;


    /**
     * 到数据流程 ： 1、导入医院表信息  2、导入医生表信息（其中医生表中的医院必须来自医院表，并且也一个字也不能差，能够同时生成医生与医院的关联表）
     * 3、导入号源表(医院名称同上)
     */
    //生成医院信息
    @RequestMapping(value = "/appointment/operation/hospitalInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> operationhospitalInfo(Map<String, Object> params) throws IOException, BiffException {
        HashMap<String, Object> response = new HashMap<String, Object>();
        Workbook rwb;//@RequestBody
        //创建输入流
        InputStream stream = new FileInputStream("c:\\xiaoErKeData\\hospital.xls");

        List<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
        //获取Excel文件对象
        rwb = Workbook.getWorkbook(stream);
        //获取文件的指定工作表 默认的第一个
        Sheet sheet = rwb.getSheet(0);
        int rowTotalNumber = sheet.getRows();
        //行数(表头的目录不需要，从1开始)
        for (int i = 1; i < rowTotalNumber; i++) {
            int j = 0;
            //获得单元格数据
            HashMap<String, Object> hospitalInfo = new HashMap<String, Object>();
            hospitalInfo.put("id", IdGen.uuid());
            hospitalInfo.put("name", sheet.getCell(j, i).getContents());
            String isEnd = sheet.getCell(j, i).getContents();
            if ("".equals(isEnd) || isEnd == null) { //如果医院名称为空则是最后一条记录
                break;
            }
            hospitalInfo.put("position", sheet.getCell(++j, i).getContents());
            hospitalInfo.put("details", sheet.getCell(++j, i).getContents());
            hospitalInfo.put("cityName", sheet.getCell(++j, i).getContents());
            arrayList.add(hospitalInfo);
        }
        operationHandleService.insertHospitalList(arrayList);
        return response;
    }

    //生成“医生表”和“医生与医院关联表”信息（前提是已经导入了医院表）
    @RequestMapping(value = "/appointment/operation/doctor", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> operationDoctor(Map<String, Object> params) throws IOException, BiffException {//@RequestBody
        HashMap<String, Object> response = new HashMap<String, Object>();
        Workbook rwb;
        //创建输入流
        InputStream stream = new FileInputStream("c:\\xiaoErKeData\\doctor.xls");
        //获取Excel文件对象
        rwb = Workbook.getWorkbook(stream);
        //获取文件的指定工作表 默认的第一个
        Sheet sheet = rwb.getSheet(0);
        int rowTotalNumber = sheet.getRows();
        //行数(表头的目录不需要，从1开始)
        for (int i = 1; i < rowTotalNumber; i++) {
            int j = 0;
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
            doctorInfo.put("phone", sheet.getCell(j, i).getContents());
            operationHandleService.CreateDoctor(doctorInfo);
        }
        return response;
    }

    //导入号源的信息
    @RequestMapping(value = "/appointment/operation/registerService", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> operationRegisterService(Map<String, Object> params) throws IOException, BiffException {
        HashMap<String, Object> response = new HashMap<String, Object>();
        Workbook rwb;
        //创建输入流
        InputStream stream = new FileInputStream("c:\\xiaoErKeData\\service.xls");
        //获取Excel文件对象
        rwb = Workbook.getWorkbook(stream);
        //获取文件的指定工作表 默认的第一个
        Sheet sheet = rwb.getSheet(0);
        int rowTotalNumber = sheet.getRows();
        //行数(表头的目录不需要，从1开始)
        for (int i = 1; i < rowTotalNumber; i++) {
            int j = 1;//此表从手机号开始读取数据
            //获得医生表数据
            HashMap<String, Object> doctorInfo = new HashMap<String, Object>();

            //如果手机号为空则是最后一条记录
            String isEnd = sheet.getCell(j, i).getContents();
            if ("".equals(isEnd) || isEnd == null) {
                break;
            }
            String location_id = IdGen.uuid();
            //根据手机号查询doctorId
            String doctorId=operationHandleService.findDoctorIdByPhone(isEnd);
            doctorInfo.put("doctorId", doctorId);
            doctorInfo.put("sys_doctor_id", doctorId);
            //根据医院名称查询hospitalId
            doctorInfo.put("hospitalName", sheet.getCell(++j, i).getContents());
            String hospitalId=operationHandleService.findhospitalId((String)doctorInfo.get("hospitalName"));
            doctorInfo.put("sys_hospital_id",hospitalId);
            doctorInfo.put("id",IdGen.uuid());
            //特别注意时间为空的插不进去，待改进
            doctorInfo.put("date", sheet.getCell(++j, i).getContents());
            doctorInfo.put("price", sheet.getCell(++j, i).getContents());
            String date= sheet.getCell(++j, i).getContents();
            if(!"".equals(date) && date!=null){
                Date begin_time= StrToDate(date);
                //结束时间自动加15分钟
                Date end_time = new Date(begin_time.getTime() + 900000);
                doctorInfo.put("begin_time", begin_time);
                doctorInfo.put("end_time",end_time);
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

            DoctorVo doctorVo =new DoctorVo();
            doctorVo.setId((String) doctorInfo.get("doctorId"));
            DoctorVo doctorVoinfos =DoctorServiceImpl.findDoctorDetailInfo(doctorVo).get(0);
            doctorInfo.put("doctorName", doctorVoinfos.getDoctorName());

            if(sysDoctorLocationId !=null && !"".equals(sysDoctorLocationId)){
                location_id = sysDoctorLocationId;
                doctorInfo.put("location_id", location_id);
            }else{
                //往sys_location表里插入增加数据
                doctorInfo.put("location_id", location_id);
                operationHandleService.insertSysDoctorLocation(doctorInfo);
            }
            //执行插入操作
            operationHandleService.insertSysRegisterServiceTest(doctorInfo);
        }
        return response;
    }


    //导入号源的信息
    @RequestMapping(value = "/appointment/operation/registerServiceTest", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> operationRegisterServiceTest(Map<String, Object> params) throws IOException, BiffException {
        HashMap<String, Object> response = new HashMap<String, Object>();
        Workbook rwb;
        //创建输入流
        InputStream stream = new FileInputStream("c:\\xiaoErKeData\\service.xls");
        //获取Excel文件对象
        rwb = Workbook.getWorkbook(stream);
        //获取文件的指定工作表 默认的第一个
        Sheet sheet = rwb.getSheet(0);
        int rowTotalNumber = sheet.getRows();
        //行数(表头的目录不需要，从1开始)
        for (int i = 1; i < rowTotalNumber; i++) {
            int j = 1;//此表从手机号开始读取数据
            //获得医生表数据
            HashMap<String, Object> doctorInfo = new HashMap<String, Object>();

            //如果手机号为空则是最后一条记录
            String isEnd = sheet.getCell(j, i).getContents();
            if ("".equals(isEnd) || isEnd == null) {
                break;
            }
            String location_id = IdGen.uuid();
            doctorInfo.put("id", sheet.getCell(j, i).getContents());
            doctorInfo.put("sys_doctor_id", sheet.getCell(++j, i).getContents());

            doctorInfo.put("sys_patient_id", sheet.getCell(++j, i).getContents());
            doctorInfo.put("sys_hospital_id", sheet.getCell(++j, i).getContents());
            doctorInfo.put("price", sheet.getCell(++j, i).getContents());//号源状态
            String date = sheet.getCell(++j, i).getContents();

            doctorInfo.put("date", date);//所需押金
            doctorInfo.put("begin_time", sheet.getCell(++j, i).getContents());
            doctorInfo.put("end_time", sheet.getCell(++j, i).getContents());
            doctorInfo.put("location", sheet.getCell(++j, i).getContents());

            doctorInfo.put("desc_info", sheet.getCell(++j, i).getContents());
            doctorInfo.put("status", sheet.getCell(++j, i).getContents());

            doctorInfo.put("deposit", sheet.getCell(++j, i).getContents());
            doctorInfo.put("service_type", sheet.getCell(++j, i).getContents());
            doctorInfo.put("create_date", sheet.getCell(++j, i).getContents());
//            doctorInfo.put("update_date", new Date());
//            doctorInfo.put("trafficInfo", sheet.getCell(++j, i).getContents());
            doctorInfo.put("root", sheet.getCell(++j, i).getContents());
            doctorInfo.put("appointmentNo", sheet.getCell(++j, i).getContents());

            DoctorVo doctorVo =new DoctorVo();
            doctorVo.setId((String) doctorInfo.get("sys_doctor_id"));
            DoctorVo doctorVoinfos =DoctorServiceImpl.findDoctorDetailInfo(doctorVo).get(0);
            doctorInfo.put("doctorName", doctorVoinfos.getDoctorName());
            HospitalVo hospitalVo= HospitalServiceImpl.getHospital((String)doctorInfo.get("sys_hospital_id"));
            doctorInfo.put("hospitalName", hospitalVo.getName());
            //查询当前地址是否存在，如果已存在则返回sys_doctor_location的id主键,否则生成一个新地址
            String sysDoctorLocationId = operationHandleService.findDoctorExistLocation(doctorInfo);
            if(sysDoctorLocationId !=null && !"".equals(sysDoctorLocationId)){
                location_id = sysDoctorLocationId;
                doctorInfo.put("location_id", location_id);
            }else{
                //往sys_location表里插入增加数据
                doctorInfo.put("location_id", location_id);
                operationHandleService.insertSysDoctorLocation(doctorInfo);
            }
            //执行插入操作
            operationHandleService.insertSysRegisterServiceTest(doctorInfo);

        }
        return response;
    }

    //导入疾病信息表sys_illness
    @RequestMapping(value = "/appointment/operation/illness", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> operationIllness( Map<String, Object> params) throws IOException, BiffException {
        HashMap<String, Object> response = new HashMap<String, Object>();

        Workbook rwb;//@RequestBody
        List<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
        //创建输入流
        InputStream stream = new FileInputStream("c:\\xiaoErKeData\\sys_illness_2.xls");
        //获取Excel文件对象
        rwb = Workbook.getWorkbook(stream);
        //获取文件的指定工作表 默认的第一个
        Sheet sheet = rwb.getSheet(0);
        int rowTotalNumber = sheet.getRows();
        //行数(表头的目录不需要，从1开始)
        for (int i = 1; i < rowTotalNumber; i++) {
            int j = 0;
            //获得单元格数据
            HashMap<String, Object> doctorInfo = new HashMap<String, Object>();
            doctorInfo.put("id",IdGen.uuid());
            doctorInfo.put("level_1", sheet.getCell(j, i).getContents());
            doctorInfo.put("level_2", sheet.getCell(++j, i).getContents());
            String isEnd = sheet.getCell(j, i).getContents();
            if ("".equals(isEnd) || isEnd == null) { //如果手机号为空则是最后一条记录
                break;
            }
            arrayList.add(doctorInfo);
        }
        //批量插入疾病信息
        operationHandleService.insertIllness(arrayList);
        return response;
    }

    //导入医生与二级疾病关联表doctor_illness_relation
    @RequestMapping(value = "/appointment/operation/illnessAndDoctorRelation", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    Map<String, Object> illnessAndDoctorRelation( Map<String, Object> params) throws IOException, BiffException {
        InputStream stream = new FileInputStream("c:\\xiaoErKeData\\doctorIllness.xls");
        HashMap<String, Object> response = new HashMap<String, Object>();
        Workbook rwb;//@RequestBody
        List<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
        //创建输入流
        //获取Excel文件对象
        rwb = Workbook.getWorkbook(stream);
        //获取文件的指定工作表 默认的第一个
        Sheet sheet = rwb.getSheet(0);
        int rowTotalNumber = sheet.getRows();
        //行数(表头的目录不需要，从1开始)
        for (int i = 1; i < rowTotalNumber; i++) {
            int j = 1;//从医生的手机号开始读取
            //获得单元格数据
            HashMap<String, Object> doctorInfo = new HashMap<String, Object>();
            doctorInfo.put("id",IdGen.uuid());

            //根据手机号查询doctorId
            String phone=sheet.getCell(j, i).getContents();
            String doctorId=operationHandleService.findDoctorIdByPhone(phone);
            doctorInfo.put("doctorId", doctorId);

            //根据level_2查询sys_illness主键
            String level_2 = sheet.getCell(++j, i).getContents();
            String illnessId = operationHandleService.findIllnessIdBylevel2(level_2);
            doctorInfo.put("sys_illness_id", illnessId);

            String isEnd = sheet.getCell(j, i).getContents();
            //如果手机号为空则是最后一条记录
            if ("".equals(isEnd) || isEnd == null) {
                break;
            }
            arrayList.add(doctorInfo);
        }
        //批量插入疾病与医生关联表数据
        operationHandleService.insertIllnessRelation(arrayList);
        return response;
    }

    //导入医生案例
    @RequestMapping(value = "/appointment/operation/illnessAndDoctorCase", method = {RequestMethod.POST, RequestMethod.GET})
    public
    @ResponseBody
    void illnessAndDoctorCase(Map<String, Object> params) throws IOException, BiffException {
        InputStream stream = new FileInputStream("c:\\xiaoErKeData\\doctorCase.xls");

        Workbook rwb = Workbook.getWorkbook(stream);

        Sheet sheet = rwb.getSheet(0);

        int rowTotalNumber = sheet.getRows();

        for (int i = 1; i < rowTotalNumber; i++) {
            int j = 0;

            DoctorCaseVo doctorCaseVo = new DoctorCaseVo();

            //根据医生姓名查找sys_doctor_id
            String doctorName = sheet.getCell(j, i).getContents();
            String doctorId = operationHandleService.findDoctorIdByname(doctorName);
            String caseName = sheet.getCell(++j, i).getContents();
            String caseNumber = sheet.getCell(++j, i).getContents().trim();
            String isDisplay = sheet.getCell(++j, i).getContents();

            //组合数据
            doctorCaseVo.setId(IdGen.uuid());
            doctorCaseVo.setSys_doctor_id(doctorId);
            doctorCaseVo.setDoctor_case_name(caseName);
            Double d = Double.parseDouble(caseNumber);
            doctorCaseVo.setDoctor_case_number((int)Math.round(d));
            doctorCaseVo.setDisplay_status(isDisplay);
            doctorCaseVo.setCreate_date(new Date());
            System.out.println("-----------------------"+i+"---------------------------");
            operationHandleService.saveDoctorCase(doctorCaseVo);

        }
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


}
