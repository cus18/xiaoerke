<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/treetable.jsp" %>
<html>
<head>
    <title>医生案例管理</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#treeTable").treeTable({expandLevel: 1}).show();
            debugger;
            //计算案例总数
            var sum = 0;
            var allinput=document.getElementsByName("doctorCaseNumbers").length;
            for(var i=0;i<allinput;i++){
                sum+=new Number(document.getElementsByName("doctorCaseNumbers")[i].value);
            }
//            alert(sum);
//            $("sum").text(sum);
            $("#sum").html(sum);

        });
        //一键保存操作
        function updateDoctorCase() {
            loading();
            $("#listForm").attr("action", "${ctx}/sys/doctorCase/updateDoctorCase");
            $("#listForm").submit();
        }
        //添加医生案例
        function addDoctorCase(){
            location.href='${ctx}/sys/doctorCase/insertDoctorCase?sys_doctor_id=${doctorCaseVo.sys_doctor_id}&doctorCaseId=${doctorCaseVo.id}&doctorName=${doctorCaseVo.doctorName}&source=caseManager';
        }


    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/sys/doctor/doctorManage"><font color="#8a2be2">医生列表</font></a></li>
    <li class="active"><a href="${ctx}/sys/doctorCase/doctorCaseManage"><font color="#006400">医生案例列表</font></a></li>
</ul>
<form:form id="searchForm" modelAttribute="doctorCaseVo"
           action="${ctx}/sys/doctorCase/doctorCase?source=doctorCaseManager" method="post"
           class="breadcrumb form-search ">
    <form:hidden path="sys_doctor_id"></form:hidden>
    <ul class="ul-form">
        <li><label>案例名称：</label><form:input path="doctor_case_name" htmlEscape="false" class="input-medium"/></li>
        <li class="btns" style="margin-left:20px;"><input id="btnSubmit" class="btn btn-primary" type="submit"
                                                          value="查询"/></li>
        <li>&nbsp;&nbsp;&nbsp;</li>
        <li><input class="btn btn-info" type="button" onclick="addDoctorCase();" value="添加医生案例"/></li>
        <li>&nbsp;&nbsp;&nbsp;</li>
        <li>总案例为：<span id="sum"> </span></li>
    </ul>
</form:form>

<sys:message content="${message}"/>
<form id="listForm" method="post">
    <table id="treeTable" class="table table-striped table-bordered table-condensed hide">
        <thead>
        <tr>
            <th class="sort-column name">医生姓名</th>
            <th>案例名称</th>
            <th>案例数</th>
            <th>是否显示(0 显示，1 不显示)</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${voList}" var="doctorCaseVo">
            <tr>
                <td>${doctorCaseVo.doctorName}</td>
                <td><input name="doctorCaseNames" type="text"
                           value=${doctorCaseVo.doctor_case_name} style="width:200px;margin:0;padding:0;text-align:center;">
                </td>
                <td>
                    <input type="hidden" name="ids" value=${doctorCaseVo.id}>
                    <input name="doctorCaseNumbers" type="text"
                           value=${doctorCaseVo.doctor_case_number} style="width:50px;margin:0;padding:0;text-align:center;">
                </td>
                <td><input name="displays" type="text"
                           value=${doctorCaseVo.display_status} style="width:50px;margin:0;padding:0;text-align:center;">
                </td>
                <td>
                    <a href="${ctx}/order/patientReturnVisitDetail?id=${doctorCaseVo.patient_register_service_id}&sysRegisterId=${doctorCaseVo.sys_register_service_id}">查看回访记录</a>
                    &nbsp;&nbsp;&nbsp;
                    <a href="${ctx}/sys/doctorCase/doctorCaseDelete?doctorCaseId=${doctorCaseVo.id}&sys_doctor_id=${doctorCaseVo.sys_doctor_id}" onclick="return confirmx('确定要删除这个案例吗？', this.href)">删除</a>
                <td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div class="form-actions pagination-left">
        <input id="btnSaveSubmit" class="btn btn-primary" type="button" value="一键保存" onclick="updateDoctorCase();"/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
    </div>
</form>
</body>
</html>