<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>送心意优惠券</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#save").click(function(){
				$("#searchForm").attr("action","${ctx}/sendMindCoupon/saveUpdateSendMindCouponForm");
				$("#searchForm").submit();
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
	</ul>
	<form:form id="searchForm" modelAttribute="vo" action="${ctx}/sendMindCoupon/sendMindCouponList?" method="post" class="breadcrumb form-search ">
		<sys:message content="${message}"/>
		<ul class="ul-form">
			<li><label>名称：</label>
				<form:input name="name" path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li class="btns">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
				<input id="save" class="btn btn-primary" type="button" value="添加"/>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-bordered table-condensed">
		<thead>
			<tr>
				<th>id</th>
				<th>优惠券名称</th>
				<th>链接</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="u">
				<tr>
					<td>${u.id}</td>
					<td>${u.name}</td>
					<td>${u.link}</td>
					<td>
						<a href="${ctx}/sendMindCoupon/saveUpdateSendMindCouponForm?id=${u.id}">修改</a>
						<a href="${ctx}/sendMindCoupon/deleteSendMindCoupon?id=${u.id}" onclick="return confirmx('确认删除？', this.href)">删除</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>