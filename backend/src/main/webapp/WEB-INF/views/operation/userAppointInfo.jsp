<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/treetable.jsp" %>
<br>
<head>
    <title>用户预约信息统计</title><!--@author deliang-->
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
    <li class="active"><a href="${ctx}/sys/UserAppoin/userAppointInfo"><font
            color="#006400">用户预约信息统计</font></a>
    </li>
</ul>
<form id="searchForm" action="${ctx}/sys/UserAppoin/userAppointInfo" method="post"
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

    <th class="sort-column name">用户名（微信）</th>
    <th class="sort-column name">OpenId</th>
    <th class="sort-column name">来源</th>
    <th class="sort-column name">关注时间</th>
    </thead>
    <c:forEach items="${resultMap.orderData}" var="hospitalVo">
        <c:if test="${!empty hospitalVo.hospitalList}">
            <c:forEach items="${hospitalVo.hospitalList}" var="doctorVo">
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

                        <td>${patientRegisterServiceVo.nickName}</td>
                        <td>${patientRegisterServiceVo.openId}</td>
                        <td>${patientRegisterServiceVo.marketer}</td>
                        <td>${patientRegisterServiceVo.falseUserReason}</td>
                    </tr>
                </c:forEach>
            </c:forEach>
        </c:if>
    </c:forEach>
</table>
</tbody>
</body>
</html>