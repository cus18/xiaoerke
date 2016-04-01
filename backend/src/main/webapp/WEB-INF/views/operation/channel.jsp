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

    </script>
</head>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/sys/Channel/ChannelMain"><font color="#006400">渠道统计分析</font></a></li>
</ul>

<form:form id="searchForm" modelAttribute="registerServiceVo" action="${ctx}/sys/Channel/ChannelMain" method="post" class="form-search">
    <sys:message content="${message}"/>
    <form:input id="startDate" path="startDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
                onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
    至
    <form:input id="endDate" path="endDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
                onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
    <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
</form:form>

<sys:message content="${message}"/>

<table id="treeTable" class="table table-striped table-bordered table-condensed hide">
    <thead>
    <tr>
        <th class="sort-column name">推广渠道</th>
        <th class="sort-column name">新关注用户</th>
        <th class="sort-column name">取消关注用户数</th>
        <th class="sort-column name">净关注用户数</th>
        <th class="sort-column name">累计用户数</th>
    </thead>
    <tbody id="treeTableList">
    <c:forEach items="${channelVo}" var="channelVo">
        <tr id="${menu.id}" pId="${menu.parent.id ne '1'?menu.parent.id:'0'}">
            <td>${channelVo.marketer}</td>
            <td>${channelVo.newAddNum}</td>
            <td>${channelVo.cancelNum}</td>
            <td>${channelVo.jinAddNum}</td>
            <td>${channelVo.leijiNum}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>