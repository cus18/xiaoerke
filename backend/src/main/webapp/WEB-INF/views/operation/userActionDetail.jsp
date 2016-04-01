<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/treetable.jsp" %>
<html>
<head>
    <title>用户行为详细记录</title>
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
    <li class="active"><a href="${ctx}/sys/UserActionDetail/UserActionDetail"><font color="#006400">用户行为详细记录</font></a></li>
</ul>
<form id="searchForm" action="${ctx}/sys/UserActionDetail/UserActionDetail" method="post"
      class="form-search ">
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input name="begin_time" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
           onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
    &nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;
    <input name="end_time" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
           onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    openID ：<input name="openid" type="text">
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
</form>

<sys:message content="${message}"/>

<table id="treeTable" class="table table-striped table-bordered table-condensed hide">
    <thead>
    <tr>
        <th class="sort-column name">步骤</th>
        <th class="sort-column name">操作时间</th>
        <th class="sort-column name">点击链接</th>
        <th class="sort-column name">点击连接名称</th>
    </thead>
    <tbody id="treeTableList">
    <c:forEach items="${resultMap}" var="resultMap">
        <tr id="${menu.id}" pId="${menu.parent.id ne '1'?menu.parent.id:'0'}">
            <td>${index}</td>
            <td>${create_date}</td>
            <td>${request_uri}</td>
            <td>${title}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>