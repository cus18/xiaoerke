<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/treetable.jsp" %>
<html>
<head>
    <title>渠道统计分析</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#treeTable").treeTable({expandLevel: 1}).show();
        });

        function changeDepartment(){
            $("#searchForm").submit();
        };

        function changeChannel(){
            $("#searchForm").submit();
        };
    </script>
</head>
<ul class="nav nav-tabs">
    <li><a href="${ctx}/sys/Channel/ChannelMain"><font color="#006400">渠道添加</font></a></li>
    <li class="active"><a href="${ctx}/sys/Channel/ChannelConsultStatistics"><font color="#006400">渠道咨询统计</font></a></li>
    <li><a href="${ctx}/sys/Channel/DepartmentConsultStatistics"><font color="#006400">部门咨询统计</font></a></li>
</ul>

<form:form id="searchForm" modelAttribute="registerServiceVo" action="${ctx}/sys/Channel/ChannelConsultStatistics" method="post" class="form-search">
    <sys:message content="${message}"/>
    <form:input id="startDate" path="startDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
                onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
    至
    <form:input id="endDate" path="endDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
                onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
    部  门 ：
    <form:select name="department" id="txtDepartment" path="department" onchange="changeDepartment();">
        <c:forEach items="${departs}" var="depart" step="1">
            <form:option value="${depart}">${depart}</form:option>
        </c:forEach>
    </form:select>
    渠道细分：
    <form:select name="channel" id="txtChannel" path="channel" onchange="changeChannel();">
        <c:forEach items="${channels}" var="channel" step="1">
            <form:option value="${channel}">${channel}</form:option>
        </c:forEach>
    </form:select>
    <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
</form:form>

<sys:message content="${message}"/>

<table id="treeTable" class="table table-striped table-bordered table-condensed hide">
    <thead>
    <tr>
        <th class="sort-column name">二维码</th>
        <th class="sort-column name">渠道细分名称</th>
        <th class="sort-column name">新咨询</th>
        <th class="sort-column name">总咨询</th>
    </thead>
    <tbody id="treeTableList">
    <c:forEach items="${channelConsultVo}" var="channelConsultVo">
        <tr id="${menu.id}" pId="${menu.parent.id ne '1'?menu.parent.id:'0'}">
            <td>${channelConsultVo.marketer}</td>
            <td>${channelConsultVo.channel}</td>
            <td>
                <c:if test="${empty channelConsultVo.newConsultCounts}">0</c:if>
                <c:if test="${not empty channelConsultVo.newConsultCounts}">${channelConsultVo.newConsultCounts}</c:if>
            </td>
            <td>
                <c:if test="${empty channelConsultVo.totalConsultCounts}">0</c:if>
                <c:if test="${not empty channelConsultVo.totalConsultCounts}">${channelConsultVo.totalConsultCounts}</c:if>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>