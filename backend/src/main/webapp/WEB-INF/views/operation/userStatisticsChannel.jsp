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
    <li><a href="${ctx}/sys/Channel/ChannelMain"><font color="#006400">渠道添加</font></a></li>
    <li class="active"><a href="${ctx}/sys/Channel/userStatisticsChannel"><font color="#006400">用户统计（渠道）</font></a></li>
    <li><a href="${ctx}/sys/Channel/userStatisticsDepartment"><font color="#006400">用户统计（部门）</font></a></li>
    <li><a href="${ctx}/sys/Channel/ChannelConsultStatistics"><font color="#006400">渠道咨询统计</font></a></li>
    <li><a href="${ctx}/sys/Channel/DepartmentConsultStatistics"><font color="#006400">部门咨询统计</font></a></li>
</ul>

<form:form id="searchForm" modelAttribute="info" action="${ctx}/sys/Channel/userStatisticsChannel" method="post" class="form-search">
	<sys:message content="${message}"/>
	<form:input name="startDate" path="startDate"  type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
	至
	<form:input name="endDate" path="endDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
	部  门 ：
	<form:select name="department" path="department" id="txtDepartment">
		<c:forEach items="${departs}" var="depart" step="1">
			<option value="${depart}">${depart}</option>
		</c:forEach>
	</form:select>
	渠道：
	<form:select name="channel" path="channel" id="txtChannel">
		<c:forEach items="${channels}" var="channel" step="1">
			<option value="${channel}">${channel}</option>
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
		<th class="sort-column name">新关注</th>
		<th class="sort-column name">新取关</th>
		<th class="sort-column name">总取关</th>
		<th class="sort-column name">净留存</th>
		<th class="sort-column name">总留存</th>
	</thead>
	<tbody id="treeTableList">
	<c:forEach items="${userChannelList}" var="channelVo">
		<tr>
			<td>${channelVo.channel}</td>
			<td>${channelVo.department}</td>
			<td>
				<c:if test="${empty channelVo.attentionCount}">0</c:if>
				<c:if test="${not empty channelVo.attentionCount}">${channelVo.attentionCount}</c:if>
			</td>
			<td>
				<c:if test="${empty channelVo.cancleAttentionCount}">0</c:if>
				<c:if test="${not empty channelVo.cancleAttentionCount}">${channelVo.cancleAttentionCount}</c:if>
			</td>
			<td>${channelVo.leijiCancleAttentionCount}</td>
			<td>${channelVo.attentionCount-channelVo.leijiCancleAttentionCount}</td>
			<td>${channelVo.leijiAttentionCount}</td>
		</tr>
	</c:forEach>
	</tbody>
</table>
</body>
</html>