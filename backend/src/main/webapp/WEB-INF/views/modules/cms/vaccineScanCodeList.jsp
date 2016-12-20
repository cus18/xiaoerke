<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>疫苗扫码信息</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#saveVaccineScanCodeForm").click(function(){
				$("#searchForm").attr("action","${ctx}/cms/vaccineScanCode/saveUpdateVaccineScanCodeForm");
				$("#searchForm").submit();
			});
		});
	</script>
</head>
<body>
<ul class="nav nav-tabs">
	<li><a>疫苗扫码列表</a></li>
</ul>
<form:form id="searchForm" modelAttribute="vo" action="${ctx}/cms/vaccineScanCode/vaccineScanCodeList?" method="post" class="breadcrumb form-search ">
	<sys:message content="${message}"/>
	<ul class="ul-form">
		<li><label>疫苗名称：</label>
			<form:input name="name" path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
		</li>
		<li><label>疫苗编号：</label>
			<form:input name="id" path="id" htmlEscape="false" maxlength="50" class="input-medium"/>
		</li>
		<li class="btns">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</li>
		<li class="btns">
			<input id="saveVaccineScanCodeForm" class="btn btn-primary" type="button" value="增加"/>
		</li>
		<li class="clearfix"></li>
	</ul>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-bordered table-condensed">
	<thead>
	<tr>
		<th>ID</th>
		<th>疫苗</th>
		<th>月龄</th>
		<th>费用</th>
		<th>操作</th>
	</tr>
	</thead>
	<tbody>
	<c:forEach items="${list}" var="u">
		<tr>
			<td>${u.id}</td>
			<td>${u.name}</td>
			<td>
				${u.age}
			</td>
			<td>
				${u.isfree}
			</td>
			<td>
				<a href="${ctx}/cms/vaccineScanCode/saveUpdateVaccineScanCodeForm?id=${u.id}">修改</a>
				<a href="${ctx}/cms/vaccineScanCode/deleteVaccineScanCode?id=${u.id}" onclick="return confirmx('确认删除？', this.href)">删除</a>
			</td>
		</tr>
	</c:forEach>
	</tbody>
</table>
</body>
</html>