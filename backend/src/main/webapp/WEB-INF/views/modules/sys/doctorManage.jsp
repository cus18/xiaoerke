<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/treetable.jsp" %>
<html>
<head>
	<title>医生管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 1}).show();
		});

		//添加医生信息
		function addDoctor(){
			location.href='${ctx}/sys/doctor/doctorEdit?id=${doctorVo.id}';
		}

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/doctor/doctorManage"><font color="#006400">医生列表</font></a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="doctorVo" action="${ctx}/sys/doctor/doctorManage" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label>医生姓名：</label><form:input path="doctorName" htmlEscape=""  class="input-medium"/></li>
			<li><label>所属医院：</label><form:input path="hospital" htmlEscape="false"  class="input-medium"/></li>
			<li class="btns" style="margin-left:20px;"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/></li>
			<%--<li class="btns" style="margin-left:20px;"><a href="${ctx}/sys/doctor/doctorEdit?id=${doctorVo.id}" onclick="">添加医生</a></li>--%>
			<li>&nbsp;&nbsp;&nbsp;</li>
			<li><input  class="btn btn-info" type="button" onclick="addDoctor();" value="添加医生" /></li>
		</ul>

	</form:form>

	<sys:message content="${message}"/>

	<table id="treeTable" class="table table-striped table-bordered table-condensed hide">
		<thead><tr><th class="sort-column name">姓名</th><th align="">所属医院</th><th>电话</th><th>操作</th></tr></thead>
		<tbody id="treeTableList">
		<c:forEach items="${page.list}" var="doctorVo">
			<tr id="${menu.id}" pId="${menu.parent.id ne '1'?menu.parent.id:'0'}">
				<td><as href="${ctx}/sys/doctor/doctorEdit?id=${doctorVo.id}">${doctorVo.doctorName}</as></td>
				<td>${doctorVo.hospital}</td>
				<td>${doctorVo.phone}</td>
				<td>
    				<%--<shiro:hasPermission name="sys:doctor:edit">--%>
    					<a href="${ctx}/sys/doctor/doctorEdit?id=${doctorVo.id}">修改</a>
						&nbsp;&nbsp;&nbsp;
					<%--</shiro:hasPermission>--%>
					<%--<shiro:hasPermission name="sys:registerForm">--%>
						<a href="${ctx}/register/registerForm?id=${doctorVo.id}&pageFlag=0" onclick="">查看号源</a>
						&nbsp;&nbsp;&nbsp;
						<c:if test="${doctorVo.phoneConsultFlag eq 'yes'}">
							<a href="${ctx}/consultPhone/registerForm?doctorId=${doctorVo.id}&doctorName=${doctorVo.doctorName}&hospital=${doctorVo.hospital}&phone=${doctorVo.phone}&pageFlag=0&phoneConsultFlag=yes" onclick="">&nbsp;&nbsp;&nbsp;详细设置&nbsp;&nbsp;&nbsp;</a>
						</c:if>
						<c:if test="${doctorVo.phoneConsultFlag eq 'no'}">
							<a href="${ctx}/consultPhone/registerForm?doctorId=${doctorVo.id}&doctorName=${doctorVo.doctorName}&hospital=${doctorVo.hospital}&phone=${doctorVo.phone}&pageFlag=0&phoneConsultFlag=no" onclick="">开通电话咨询</a>
						</c:if>
						
						&nbsp;&nbsp;&nbsp;
					<%--</shiro:hasPermission>--%>
					<%--<shiro:hasPermission name="sys:doctor:case">--%>
						<a href="${ctx}/sys/doctorCase/doctorCase?id=${doctorVo.id}&source=FromDoctorManager" onclick="">查看案例</a>
					<%--</shiro:hasPermission>--%>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>