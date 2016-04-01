<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/treetable.jsp" %>
<br>
<head>
    <title>号源结算统计</title><!--@author deliang-->
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#treeTable").treeTable({expandLevel: 1}).show();
        });

    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/sys/AppointRegisterAccount/appointRegisterAccount"><font color="#006400">号源结算统计</font></a>
    </li>
</ul>
<form id="searchForm" action="${ctx}/sys/AppointRegisterAccount/appointRegisterAccount" method="post"
      class="form-search ">
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input name="begin_time" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
           onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
    &nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;
    <input name="end_time" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
           onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
</form>

<sys:message content="${message}"/>
<tbody id="treeTableList">
<c:forEach items="${resultMap.orderData}" var="hospitalVo">
    <c:if test="${!empty hospitalVo.hospitalList}">
        <c:forEach items="${hospitalVo.hospitalList}" var="doctorVo">
            <span class="icon-map-marker" style="border: inherit"></span><font color="red"
                                                                               size="2">${doctorVo.hospitalName}——${doctorVo.name}——${doctorVo.location}</font><br>
            <table class="table table-striped">
                <thead>
                <th class="sort-column name">所属科室</th>
                <th class="sort-column name">医生手机号</th>
                <th class="sort-column name">预约时间</th>
                <th class="sort-column name">就诊时间</th>
                <th class="sort-column name">宝宝姓名</th>
                <th class="sort-column name">家长手机号</th>
                <th class="sort-column name">疾病描述</th>
                <th class="sort-column name">预约状态</th>
                </thead>

                <c:forEach items="${doctorVo.doctorList}" var="patientRegisterServiceVo">
                    <tr>
                        <td>${patientRegisterServiceVo.departmentLevel1}</td>

                        <td>${patientRegisterServiceVo.doctorPhone}</td>

                        <td>${patientRegisterServiceVo.orderCreateDate}</td>

                        <td>${patientRegisterServiceVo.visitDate}</td>

                        <td>${patientRegisterServiceVo.babyName}</td>

                        <td>${patientRegisterServiceVo.phone}</td>

                        <td>${patientRegisterServiceVo.illness}</td>

                        <td>${patientRegisterServiceVo.status}</td>
                    </tr>
                </c:forEach>

            </table>
        </c:forEach>
    </c:if>
</c:forEach>
</tbody>
</body>
</html>