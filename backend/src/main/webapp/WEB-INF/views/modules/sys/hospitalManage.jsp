<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/treetable.jsp" %>
<html>
<head>
    <title>医院管理</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#treeTable").treeTable({expandLevel: 1}).show();

            $("#inputForm").validate({
                submitHandler: function (form) {
                    loading('正在提交，请稍等...');
                    form.submit();
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
        });
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/sys/hospital/hospitalManage"><font color="#006400">医院列表</font></a></li>
</ul>
<form:form id="searchForm" modelAttribute="hospitalVo" action="${ctx}/sys/hospital/hospitalManage" method="post"
           class="breadcrumb form-search ">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
    <ul class="ul-form">
        <li><label>医院名称：</label><form:input path="name" htmlEscape="" class="input-medium"/></li>
        <li class="btns" style="margin-left:20px;"><input id="btnSubmit" class="btn btn-primary" type="submit"
                                                          value="查询" onclick="return page();"/></li>
    </ul>
</form:form>
<sys:message content="${message}"/>
<table id="treeTable" class="table table-striped table-bordered table-condensed hide">
    <thead>
    <tr>
        <th class="sort-column name">医院名称</th>
        <th>地址</th>
        <th>机构联系人</th>
        <th>机构联系人电话</th>
        <th>所属城市</th>
        <th>排序</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody id="treeTableList">
    <c:forEach items="${page.list}" var="hospitalVo">
        <tr id="${menu.id}" pId="${menu.parent.id ne '1'?menu.parent.id:'0'}">
            <td><a href="${ctx}/sys/hospital/hospitalEdit?id=${hospitalVo.id}">${hospitalVo.name}</a></td>
            <td>${hospitalVo.position}</td>
            <td>${hospitalVo.contactName}</td>
            <td>${hospitalVo.contactPhone}</td>
            <td>${hospitalVo.cityName}</td>
            <td>${hospitalVo.sort}</td>
            <td>
                    <%--<shiro:hasPermission name="sys:hospital:edit"><td>--%>
                <a href="${ctx}/sys/hospital/hospitalEdit?id=${hospitalVo.id}">修改</a>
                    <%--</shiro:hasPermission>--%>
                    <%--<shiro:hasPermission name="sys:hospital:edit">--%>
                <a href="${ctx}/sys/hospital/hospitalDelete?id=${hospitalVo.id}"
                   onclick="return confirmx('确定要把这个医院和它的关联全部删除吗？', this.href)">删除</a>
                    <%--</shiro:hasPermission>--%>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>