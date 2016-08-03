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
        //改变部门下拉
        function changeDepartment(){
            $("#searchForm").submit();
        }
    </script>
</head>
<ul class="nav nav-tabs">
    <li><a href="${ctx}/sys/Channel/ChannelMain"><font color="#006400">渠道添加</font></a></li>
    <li><a href="${ctx}/sys/Channel/userStatisticsChannel"><font color="#006400">用户统计(渠道)</font></a></li>
    <li><a href="${ctx}/sys/Channel/userStatisticsDepartment"><font color="#006400">用户统计(部门)</font></a></li>
    <li><a href="${ctx}/sys/Channel/ChannelConsultStatistics"><font color="#006400">咨询统计(渠道)</font></a></li>
    <li class="active"><a href="${ctx}/sys/Channel/DepartmentConsultStatistics"><font color="#006400">咨询统计(部门)</font></a></li>
    <li><a href="${ctx}/sys/Channel/newUserAttentionAndRemainStatistics"><font color="#006400">用户新关注与净留存统计</font></a></li>
</ul>

<form:form id="searchForm" modelAttribute="registerServiceVo" action="${ctx}/sys/Channel/DepartmentConsultStatistics" method="post" class="form-search">
    <sys:message content="${message}"/>
    <form:input id="startDate" path="startDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
                onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
    至
    <form:input id="endDate" path="endDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
                onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
    部  门 ：
    <form:select name="department" id="txtDepartment" path="department" onchange="changeDepartment();">
        <c:forEach items="${departs}" var="depart" step="1">
            <form:option value="${depart}">${depart}</form:option>
        </c:forEach>
    </form:select>
    <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
</form:form>

<sys:message content="${message}"/>

<table id="treeTable" class="table table-striped table-bordered table-condensed hide">
    <thead>
    <tr>
        <th class="sort-column name">部门</th>
        <th class="sort-column name">新咨询</th>
        <th class="sort-column name">总咨询</th>
    </thead>
    <tbody id="treeTableList">
    <c:forEach items="${departmentConsultVo}" var="departmentConsultVo">
        <tr id="${menu.id}" pId="${menu.parent.id ne '1'?menu.parent.id:'0'}">
            <td>${departmentConsultVo.department}</td>
            <td>
                <c:if test="${empty departmentConsultVo.newConsultCounts}">0</c:if>
                <c:if test="${not empty departmentConsultVo.newConsultCounts}">${departmentConsultVo.newConsultCounts}</c:if>
            </td>
            <td>
                <c:if test="${empty departmentConsultVo.totalConsultCounts}">0</c:if>
                <c:if test="${not empty departmentConsultVo.totalConsultCounts}">${departmentConsultVo.totalConsultCounts}</c:if>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>