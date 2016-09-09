<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>用户渠道查询</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        function page(n, s) {
            if($("#nickname").val() == ''&&$("#openid").val() == ''){
                alertx("请输入昵称或openid");
                return false;
            }
            if (n) $("#pageNo").val(n);
            if (s) $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
        $(document).ready(function () {
            if('${todayAttention}' == '1'){
                $("#todayAttention").attr("checked", true);
            }
            if('${todayConsult}' == '1'){
                $("#todayConsult").attr("checked", true);
            }
            $("#searchForm").validate({
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
    <li class="active"><a href="${ctx}/sys/Channel/userChannelSearch?">用户渠道查询</a></li>
</ul>
<form:form id="searchForm" modelAttribute="vo" action="${ctx}/sys/Channel/userChannelSearch" method="post"
           class="breadcrumb form-search ">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <sys:message content="${message}"/>
    <sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
    <ul class="ul-form">
        <li><label>昵称：</label>
            <form:input name="nickname" path="nickname" htmlEscape="false" maxlength="50" class="input-medium"/>
        </li>
        <li><label>openid：</label>
            <form:input name="openid" path="openid" htmlEscape="false" maxlength="50" class="input-medium"/>
        </li>
        <li>
            <span>
                <input id="todayAttention" name="todayAttention" type="checkbox" value="1">
                <label for="todayAttention">当天关注</label>
                <input id="todayConsult" name="todayConsult" type="checkbox" value="1">
                <label for="todayConsult">当天咨询</label>
            </span>
        </li>
        <li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"
                                onclick="return page();"/>
    </ul>
</form:form>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th class="sort-column doctorName">昵称</th>
        <th>openid</th>
        <th>渠道</th>
        <th>状态</th>
        <th>日期</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="attention">
        <tr>
            <td>${attention.nickname}</td>
            <td>${attention.openid}</td>
            <td>${attention.marketer}</td>
            <td>
                <c:if test="${attention.status eq '0'}">已关注</c:if>
                <c:if test="${attention.status eq '1'}">未关注</c:if>
            </td>
            <td><fmt:formatDate value="${attention.date}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>