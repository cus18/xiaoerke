<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/treetable.jsp" %>
<html>
<head>
    <title>医生信息统计</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#treeTable").treeTable({expandLevel: 1}).show();
        });
    </script>
</head>
<>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/sys/DoctorInfoSta/doctorInfo"><font color="#006400">医生信息统计</font></a></li>
</ul>
<form:form id="searchForm" modelAttribute="doctorAttentionVo" action="${ctx}/sys/DoctorInfoSta/doctorInfo" method="post"
           class="form-search ">
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <form:input id="startDate" path="startDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
                onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
    &nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;
    <form:input id="endDate" path="endDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
                onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
</form:form>

<sys:message content="${message}"/>

<table id="treeTable" class="table table-striped table-bordered table-condensed hide">
    <thead>
    <tr>
        <th class="sort-column name">微信名</th>
        <th class="sort-column name">OpenID</th>
        <th class="sort-column name">医生电话</th>
        <th class="sort-column name">来源（BD）</th>
        <th class="sort-column name">关注时间</th>
        <th class="sort-column name">医生姓名</th>
        <th class="sort-column name">医院</th>
        <th class="sort-column name">科室</th>
        <th class="sort-column name">累计号源总数</th>
        <th class="sort-column name">已预约号源数</th>
        <th class="sort-column name">剩余预约号源数</th>
    </thead>

    <tbody id="treeTableList">
    <c:forEach items="${doctorAttentionVoList}" var="statisticsVo">
        <tr id="${menu.id}" pId="${menu.parent.id ne '1'?menu.parent.id:'0'}">
            <td>${statisticsVo.nickname}</td>
            <td>${statisticsVo.openid}</td>
            <td>${statisticsVo.phone}</td>
            <td>${statisticsVo.marketer}</td>
            <td>${statisticsVo.date}</td>
            <td>${statisticsVo.doctorName}</td>
            <td>${statisticsVo.hospitalName}</td>
            <td>${statisticsVo.department}</td>
            <td>${statisticsVo.registerServiceCount}</td>
            <td>${statisticsVo.appointNumberAlready}</td>
            <td>${statisticsVo.appointNumber}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>