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
		<li><a href="${ctx}/register/registerList?">号源列表</a></li>
		<li><a href="${ctx}/order/patientRegisterList?">加号列表</a></li>
		<li><a href="${ctx}/order/removedOrderList?">已删除订单列表</a></li>
		<li class="active"><a href="${ctx}/register/willNoRegisterList?">即将没有号源的医生列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="registerServiceVo" action="${ctx}/register/willNoRegisterList" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<table id="contentTable" class="table table-bordered table-condensed">
		<thead><tr><th>医生姓名</th><th>所在医院</th><th>所在部门</th><th>日期</th><shiro:hasPermission name="sys:user:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="register">
			<tr>
				<td>${register.doctorName}</td>
				<td>${register.hospitalName}</td>
				<td>${register.department}</td>
				<td><fmt:formatDate value ="${register.date}" pattern="yyyy-MM-dd" /></td>
				<shiro:hasPermission name="register:registerForm">
					<td>
						<a href="${ctx}/register/registerForm?id=${register.sysDoctorId}&pageFlag=0" onclick="">查看号源</a>
					</td>
				</shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>