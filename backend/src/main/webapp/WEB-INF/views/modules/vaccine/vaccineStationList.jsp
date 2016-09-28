<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>疫苗信息</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#saveVaccine").click(function(){
				$("#searchForm").attr("action","${ctx}/vaccine/saveUpdateVaccineStationForm");
				$("#searchForm").submit();
			});
		});
	</script>
</head>
<body>
<ul class="nav nav-tabs">
	<li class="active"><a href="${ctx}/vaccine/vaccineStationList?">预防站列表</a></li>
	<li><a href="${ctx}/vaccine/vaccineList?">疫苗列表</a></li>
</ul>
<form:form id="searchForm" modelAttribute="vo" action="${ctx}/vaccine/vaccineStationList?" method="post" class="breadcrumb form-search ">
	<sys:message content="${message}"/>
	<ul class="ul-form">
		<li><label>疫苗站名称：</label>
			<form:input name="name" path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
		</li>
		<li><label>疫苗站编号：</label>
			<form:input name="id" path="id" htmlEscape="false" maxlength="50" class="input-medium"/>
		</li>
		<li class="btns">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</li>
		<li class="btns">
			<input id="saveVaccine" class="btn btn-primary" type="button" value="增加疫苗站"/>
		</li>
		<li class="clearfix"></li>
	</ul>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-bordered table-condensed">
	<thead>
	<tr>
		<th>疫苗站名称</th>
		<th>疫苗站编号</th>
		<th>地址</th>
		<th>电话</th>
		<th>联系人</th>
		<th>接种工作日</th>
		<th>操作</th>
	</tr>
	</thead>
	<tbody>
	<c:forEach items="${list}" var="u">
		<tr>
			<td>${u.name}</td>
			<td>${u.id}</td>
			<td>${u.address}</td>
			<td>${u.contactPhone}</td>
			<td>${u.contactName}</td>
			<td>${u.workDate}</td>
			<td>
				<a href="${ctx}/vaccine/saveUpdateVaccineStationForm?id=${u.id}">修改</a>
				<a href="${ctx}/vaccine/deleteKeywordRole?roleId=${u.id}" onclick="return confirmx('确认删除？', this.href)">删除</a>
			</td>
		</tr>
	</c:forEach>
	</tbody>
</table>
</body>
</html>