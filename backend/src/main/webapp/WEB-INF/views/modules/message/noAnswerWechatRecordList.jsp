<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>加号列表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/register/willNoRegisterList");
			$("#searchForm").submit();
			return false;
		}
		$(document).ready(function() {
		});
	</script>
</head>
<body>
<ul class="nav nav-tabs">

	<li class="active"><a href="${ctx}/message/noAnswerWechatRecordList?">未回复用户列表</a></li>
</ul>
<form:form id="searchForm" modelAttribute="vo" action="${ctx}/message/noAnswerWechatRecordList" method="post" class="breadcrumb form-search ">
	<ul class="ul-form">
		<li><label>就诊时间：</label>
			<form:input id="fromDate" path="fromTime" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
						onclick="WdatePicker({dateFmt:'HH:mm:ss',isShowClear:false});"/>
			至
			<form:input id="toDate" path="toTime" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
						onclick="WdatePicker({dateFmt:'HH:mm:ss',isShowClear:false});"/>
		</li>
		<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
	</ul>
</form:form>
<table id="contentTable" class="table table-bordered table-condensed">
	<thead><tr><th>微信号</th><th>openid</th><th>咨询问题</th><th>咨询时间</th><shiro:hasPermission name="sys:user:edit"><th>是否回复</th></shiro:hasPermission></tr></thead>
	<tbody>
	<c:forEach items="${lists}" var="map">
		<tr>
			<td>${map.nickname}</td>
			<td>${map.openid}</td>
			<td>${map.text}</td>
			<td><fmt:formatDate value ="${map.infoTime}" pattern="yyyy-MM-dd hh:mm:ss" /></td>
			<td>${map.opercode}</td>
		</tr>
	</c:forEach>
	</tbody>
</table>

</body>
</html>