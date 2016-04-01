<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/treetable.jsp" %>
<html>
<head>
    <title>综合统计查询</title>
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
    <li class="active"><a href="${ctx}/sys/OperationsComprehensive/operationsComprehensiveMain"><font color="#006400">综合统计查询</font></a></li>
</ul>
<form:form id="searchForm" modelAttribute="sysStatisticsVo" action="${ctx}/sys/OperationsComprehensive/operationsComprehensiveMain" method="post"
           class="form-search ">
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <form:input id="fromDate" path="fromDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
                onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
    &nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;
    <form:input id="toDate" path="toDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
                onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
</form:form>

<sys:message content="${message}"/>

<table id="treeTable" class="table table-striped table-bordered table-condensed hide">
    <thead>
    <tr>
        <th class="sort-column name">日期&nbsp;\&nbsp;人数</th>
        <th class="sort-column name">新关注数</th>
        <th class="sort-column name">取消关注数</th>
        <th class="sort-column name">净关注数</th>
        <th class="sort-column name">累计关注数</th>
        <th class="sort-column name">新增医生数</th>
        <th class="sort-column name">医生总数</th>
        <th class="sort-column name">新增订单数</th>
        <th class="sort-column name">取消订单数</th>
        <th class="sort-column name">成功订单数</th>
        <th class="sort-column name">成功订单总数</th>
        <th class="sort-column name">净增咨询数</th>

        <th class="sort-column name">已咨询总人数</th>
        <th class="sort-column name">新关注咨询人数</th>
        <th class="sort-column name">新关注预约人数</th>


        <th class="sort-column name">郑玉巧说（新关注）</th>
        <th class="sort-column name">老用户咨询人数</th>
        <th class="sort-column name">老用户预约人数</th>
        <th class="sort-column name">郑玉巧说（老关注）</th>
        <th class="sort-column name">当天总活跃总数</th>
        <th class="sort-column name">累计活跃总数</th>
    </thead>
    <tbody id="treeTableList">
    <c:forEach items="${SysStatistics}" var="statisticsVo">
        <tr id="${menu.id}" pId="${menu.parent.id ne '1'?menu.parent.id:'0'}">
            <td>${statisticsVo.date}</td>
            <td>${statisticsVo.newAddUsers}</td>
            <td>${statisticsVo.cancelNewUsers}</td>
            <td>${statisticsVo.actualNewUser}</td>
            <td>${statisticsVo.sumUser}</td>
            <td>${statisticsVo.newAddDoctor}</td>
            <td>${statisticsVo.totalDoctor}</td>
            <td>${statisticsVo.newOrderNumber}</td>
            <td>${statisticsVo.unsuccessfulOrder}</td>
            <td>${statisticsVo.successfulOrder}</td>
            <td>${statisticsVo.accountSuccessOrder}</td>
            <td>${statisticsVo.newAddConsultationNumber}</td>

            <td>${statisticsVo.countConsultationNumber}</td><!--已咨询人数-->
            <td>${statisticsVo.activateNewUser}</td>
            <td>${statisticsVo.activateOrder}</td>

            <td>${statisticsVo.newZhengYuQiao}</td>
            <td>${statisticsVo.consultation}</td>
            <td>${statisticsVo.activateOrderConsultationNumber}</td>
            <td>${statisticsVo.oldZhengYuQiao}</td>
            <td>${statisticsVo.alreadycon}</td>
            <td>${statisticsVo.sumAccount}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>