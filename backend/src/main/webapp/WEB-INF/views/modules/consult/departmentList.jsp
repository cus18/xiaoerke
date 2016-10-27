<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>医生列表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});

	    function searchSub(){
			$("#searchForm").attr("action","${ctx}/consult/consultDoctorDepartmentList?");
			$("#searchForm").submit();
		}

		function departmentOperForm(href,title){
			href=encodeURI(encodeURI(href));
			top.$.jBox.open('iframe:'+href,title,630,470,{
				buttons:{"关闭":true},
				closed: function () {
					$("#searchForm").attr("action","${ctx}/consult/consultDoctorDepartmentList");
					$("#searchForm").submit();
				}
			});
		}

		function changeShow(id){
			var isShow = 1;
			if($('#'+id).attr('checked') == false){
				isShow = 0;
			}
			$.ajax({
				type: "post",
				url: "${ctx}/consult/departmentIsShow",
				data: {id:id,isShow:isShow},
				dataType: "json",
				success: function(data){

				}
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/consult/consultDoctorList?">咨询医生部门列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="vo" action="${ctx}/consult/consultDoctorDepartmentList?" method="post" class="breadcrumb form-search ">
		<sys:message content="${message}"/>
		<ul class="ul-form">
			<li><label>科室名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li class="btns">
				<input class="btn btn-primary" type="button" onclick="searchSub()" value="查询" />
				<input class="btn btn-primary" type="button" onclick="departmentOperForm('${ctx}/consult/departmentOperForm?','新增科室')" value="新增科室" />
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-bordered table-condensed">
		<thead>
			<tr>
				<th>id</th>
				<th>名称</th>
				<th>排序</th>
				<th>显示</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${departmentList}" var="u">
				<tr>
					<td>${u.id}</td>
					<td>${u.name}</td>
					<td>${u.sorting}</td>
					<td>
						<input id="${u.id}" name="isShow" type="checkbox" value="${u.isShow}" <c:if test="${u.isShow eq '1'}">checked</c:if> onclick="changeShow('${u.id}')">
					</td>
					<td>
						<a href="#" onclick="departmentOperForm('${ctx}/consult/departmentOperForm?id=${u.id}','账号修改')">账号修改</a>
						<a href="${ctx}/consult/deleteDepartment?id=${u.id}" onclick="return confirmx('确认删除？', this.href)">删除</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>