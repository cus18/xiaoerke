<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>疫苗信息</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#saveVaccineForm").click(function(){
				$("#searchForm").attr("action","${ctx}/vaccine/saveUpdateVaccineForm");
				$("#searchForm").submit();
			});
		});
	</script>
</head>
<body>
<ul class="nav nav-tabs">
	<li><a href="${ctx}/vaccine/vaccineStationList?">预防站列表</a></li>
	<li class="active"><a href="${ctx}/vaccine/vaccineList?">疫苗列表</a></li>
</ul>
<form:form id="searchForm" modelAttribute="vo" action="${ctx}/vaccine/vaccineList?" method="post" class="breadcrumb form-search ">
	<sys:message content="${message}"/>
	<ul class="ul-form">
		<li><label>疫苗名称：</label>
			<form:input name="doctorName" path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
		</li>
		<li><label>疫苗编号：</label>
			<form:input name="doctorName" path="number" htmlEscape="false" maxlength="50" class="input-medium"/>
		</li>
		<li class="btns">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</li>
		<li class="btns">
			<input id="saveVaccineForm" class="btn btn-primary" type="button" value="增加疫苗"/>
		</li>
		<li class="clearfix"></li>
	</ul>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-bordered table-condensed">
	<thead>
	<tr>
		<th>疫苗名称</th>
		<th>疫苗编号</th>
		<th>简介</th>
		<th>注意事项</th>
		<th>操作</th>
	</tr>
	</thead>
	<tbody>
	<c:forEach items="${list}" var="u">
		<tr>
			<td>${u.name}</td>
			<td>PD_YMTX_${u.id}</td>
			<td>${u.introduce}</td>
			<td>${u.attention}</td>
			<td>
				<a href="${ctx}/vaccine/saveUpdateVaccineForm?id=${u.id}">修改</a>
				<a href="${ctx}/vaccine/deleteVaccine?id=${u.id}" onclick="return confirmx('确认删除？', this.href)">删除</a>
			</td>
		</tr>
	</c:forEach>
	</tbody>
</table>
</body>
</html>