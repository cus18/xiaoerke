<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>文案配置</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#saveConf").click(function(){
				$("#searchForm").attr("action","${ctx}/messageContentConf/saveUpdateMessageContentConfForm");
				$("#searchForm").submit();
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
	</ul>
	<form:form id="searchForm" modelAttribute="vo" action="${ctx}/consult/messageContentConfList?" method="post" class="breadcrumb form-search ">
		<sys:message content="${message}"/>
		<ul class="ul-form">
			<li class="btns">
				<input id="saveConf" class="btn btn-primary" type="button" value="添加配置"/>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-bordered table-condensed">
		<thead>
			<tr>
				<th>id</th>
				<th>场景</th>
				<th>日期</th>
				<th>时间</th>
				<th>优先级</th>
				<th>内容</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="u">
				<tr>
					<td>${u.id}</td>
					<td>${u.scene}</td>
					<td>${u.week}</td>
					<td>${u.startTime}-${u.endTime}</td>
					<td>${u.priority}</td>
					<td>${u.content}</td>
					<td>
						<a href="${ctx}/messageContentConf/saveUpdateMessageContentConfForm?id=${u.id}">修改</a>
						<c:if test="${u.priority ne 0}">
							<a href="${ctx}/messageContentConf/deleteMessageContentConf?id=${u.id}" onclick="return confirmx('确认删除？', this.href)">删除</a>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>