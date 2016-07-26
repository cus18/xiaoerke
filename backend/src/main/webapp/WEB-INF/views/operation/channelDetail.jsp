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
        //改变渠道下拉
        function changeChannel(){
            $("#searchForm").submit();
        }
    </script>
</head>
<ul class="nav nav-tabs">
    <li><a href="${ctx}/sys/Channel/ChannelMain"><font color="#006400">渠道添加</font></a></li>
    <li><a href="${ctx}/sys/Channel/ChannelCategory"><font color="#006400">渠道分类统计</font></a></li>
    <li class="active"><a href="${ctx}/sys/Channel/ChannelDetail"><font color="#006400">渠道细分</font></a></li>
    <li><a href="${ctx}/sys/Channel/userStatisticsChannel"><font color="#006400">用户统计（渠道）</font></a></li>
    <li><a href="${ctx}/sys/Channel/userStatisticsDepartment"><font color="#006400">用户统计（部门）</font></a></li>
</ul>

<form:form id="searchForm" modelAttribute="registerServiceVo" action="${ctx}/sys/Channel/ChannelDetail" method="post" class="form-search">
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
        <th class="sort-column name">二维码号</th>
        <th class="sort-column name">关注</th>
        <th class="sort-column name">取消</th>
        <th class="sort-column name">累计关注</th>
        <th class="sort-column name">累计取消</th>
        <th class="sort-column name">咨询人数</th>
        <th class="sort-column name">无效咨询</th>
        <th class="sort-column name">咨询占比</th>
        <th class="sort-column name">渠道细分</th>
    </thead>
    <tbody id="treeTableList">
    <c:forEach items="${channelDetailVo}" var="channelVo">
        <tr id="${menu.id}" pId="${menu.parent.id ne '1'?menu.parent.id:'0'}">
            <td>${channelVo.marketer}</td>
            <td>
                <c:if test="${empty channelVo.attentionCount}">0</c:if>
                <c:if test="${not empty channelVo.attentionCount}">${channelVo.attentionCount}</c:if>
            </td>
            <td>
                <c:if test="${empty channelVo.cancleAttentionCount}">0</c:if>
                <c:if test="${not empty channelVo.cancleAttentionCount}">${channelVo.cancleAttentionCount}</c:if>
            </td>
            <td>${channelVo.leijiAttentionCount}</td>
            <td>${channelVo.leijiCancleAttentionCount}</td>
            <td>${channelVo.chatCount}</td>
            <td>${channelVo.invalidChatCount}</td>
            <td>${channelVo.chatScale}</td>
            <td>${channelVo.channel}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>