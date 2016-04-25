<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员列表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/insurance/insuranceHospitalList?");
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="">保险关联医院列表</a></li>
		<li><a href="${ctx}/insurance/saveInsuranceHospitalForm?">添加保险关联医院</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="vo" action="${ctx}/insurance/insuranceHospitalList?" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:message content="${message}"/>
		<ul class="ul-form">
			<li><label>区：</label>
				<form:select path="district" class="input-mini">
					<form:option value="" label="全部"/>
					<form:option value="0" label="海淀区"/>
					<form:option value="1" label="朝阳区"/>
					<form:option value="2" label="东城区"/>
					<form:option value="3" label="西城区"/>
					<form:option value="4" label="石景山区"/>
					<form:option value="5" label="丰台区"/>
					<form:option value="6" label="门头沟区"/>
					<form:option value="7" label="房山区"/>
					<form:option value="8" label="通州区"/>
					<form:option value="9" label="顺义区"/>
					<form:option value="10" label="大兴区"/>
					<form:option value="11" label="昌平区"/>
					<form:option value="12" label="平谷区"/>
					<form:option value="13" label="怀柔区"/>
					<form:option value="14" label="密云县"/>
					<form:option value="15" label="延庆县"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-bordered table-condensed">
		<thead>
			<tr>
				<th>id</th>
				<th>医院</th>
				<th>地址</th>
				<th>区</th>
				<th>电话</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="insuranceHospital">
				<tr>
					<td>${insuranceHospital.id}</td>
					<td>${insuranceHospital.hospitalName}</td>
					<td>${insuranceHospital.location}</td>
					<td>
						<c:if test="${insuranceHospital.district eq '0'}">海淀区</c:if>
						<c:if test="${insuranceHospital.district eq '1'}">朝阳区</c:if>
						<c:if test="${insuranceHospital.district eq '2'}">东城区</c:if>
						<c:if test="${insuranceHospital.district eq '3'}">西城区</c:if>
						<c:if test="${insuranceHospital.district eq '4'}">石景山区</c:if>
						<c:if test="${insuranceHospital.district eq '5'}">丰台区</c:if>
						<c:if test="${insuranceHospital.district eq '6'}">门头沟区</c:if>
						<c:if test="${insuranceHospital.district eq '7'}">房山区</c:if>
						<c:if test="${insuranceHospital.district eq '8'}">通州区</c:if>
						<c:if test="${insuranceHospital.district eq '9'}">顺义区</c:if>
						<c:if test="${insuranceHospital.district eq '10'}">大兴区</c:if>
						<c:if test="${insuranceHospital.district eq '11'}">昌平区</c:if>
						<c:if test="${insuranceHospital.district eq '12'}">平谷区</c:if>
						<c:if test="${insuranceHospital.district eq '13'}">怀柔区</c:if>
						<c:if test="${insuranceHospital.district eq '14'}">密云县</c:if>
						<c:if test="${insuranceHospital.district eq '15'}">延庆县</c:if>
					</td>
					<td>${insuranceHospital.phone}</td>
					<td>
						<a href="${ctx}/insurance/saveInsuranceHospitalForm?id=${insuranceHospital.id}">修改</a>
						<a href="${ctx}/insurance/delInsuranceHospital?id=${insuranceHospital.id}">删除</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>