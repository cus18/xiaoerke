<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>关键字</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#saveRole").click(function(){
				$("#searchForm").attr("action","${ctx}/operationPromotion/saveUpdateRoleForm");
				$("#searchForm").submit();
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
	</ul>
	<form:form id="searchForm" modelAttribute="user" action="${ctx}/consult/consultDoctorList?" method="post" class="breadcrumb form-search ">
		<sys:message content="${message}"/>
		<ul class="ul-form">
			<li class="btns">
				<input id="saveRole" class="btn btn-primary" type="button" value="添加规则"/>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-bordered table-condensed">
		<thead>
			<tr>
				<th>id</th>
				<th>规则</th>
				<th>关键字</th>
				<th>回复</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="u">
				<tr>
					<td>${u.roleId}</td>
					<td>${u.roleName}</td>
					<td>${u.keyword}</td>
					<td>${u.replyText}</td>
					<td>
						<a href="${ctx}/operationPromotion/saveUpdateRoleForm?roleId=${u.roleId}">修改</a>
						<a href="${ctx}/operationPromotion/deleteKeywordRole?roleId=${u.roleId}" onclick="return confirmx('确认删除？', this.href)">删除</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>