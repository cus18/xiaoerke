<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/treetable.jsp" %>
<html>
<head>
    <title>用户咨询统计</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#treeTable").treeTable({expandLevel: 1}).show();
        });
    </script>
</head>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/sys/Channel/ChannelMain"><font color="#006400">用户咨询统计</font></a></li>
</ul>
<form id="searchForm" action="${ctx}/sys/Channel/ChannelMain" method="post"
      class="form-search ">
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
           onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
    &nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;
    <input name="endDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
           onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

    <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
    <font color="blue">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数据量较大，请耐心等待！</font></a>
</form>

<table class="table table-striped">
    <thead>
    <tr>
        <th class="sort-column name">微信名</th>
        <th class="sort-column name">OpenID</th>
        <th class="sort-column name">关注时间</th>
        <th class="sort-column name">咨询开始时间</th>
        <th class="sort-column name">是否有效咨询</th>
        <th class="sort-column name">咨询关闭时间</th>
        <th class="sort-column name">提问次数</th>
        <th class="sort-column name">使用预约频道次数</th>
        <th class="sort-column name">成功预约次数</th>
        <th class="sort-column name">约单次数</th>
        <th class="sort-column name">访问页面数</th>
        <th class="sort-column name">来源</th>
    </thead>
    <tbody id="treeTableList">
    <c:forEach items="${resultMap.consult}" var="consultVo">
        <tr>
            <td>${consultVo.nickname}</td>
            <td>${consultVo.openid}</td>
            <td>${consultVo.date}</td>
            <td>${consultVo.openWindow}</td>
            <td>${consultVo.nullity}</td>
            <td>${consultVo.closeWindow}</td>
            <td>${consultVo.consult_count}</td>
            <td>${consultVo.validRNum}</td>
            <td>${consultVo.victoryRNum}</td>
            <td>${consultVo.validNum}</td>
            <td>${consultVo.visitRNum}</td>
            <td>${consultVo.marketer}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>