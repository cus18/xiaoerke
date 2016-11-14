<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>图片传播列表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});

	    function searchSub(){
			$("#searchForm").attr("action","${ctx}/operationPromotion/operationPromotionTemplateList?");
			$("#searchForm").submit();
		}

		function operationPromotionTemplateOperForm(href,title){
			href=encodeURI(encodeURI(href));
			top.$.jBox.open('iframe:'+href,title,630,470,{
				buttons:{"关闭":true},
				closed: function () {
					$("#searchForm").attr("action","${ctx}/operationPromotion/operationPromotionTemplateList");
					$("#searchForm").submit();
				}
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/operationPromotion/operationPromotionTemplateList?">图片传播列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="vo" action="${ctx}/operationPromotion/consultDoctorDepartmentList?" method="post" class="breadcrumb form-search ">
		<sys:message content="${message}"/>
		<ul class="ul-form">
			<li><label>ID：</label>
				<form:input path="id" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li class="btns">
				<input class="btn btn-primary" type="button" onclick="searchSub()" value="查询" />
				<input class="btn btn-primary" type="button" onclick="operationPromotionTemplateOperForm('${ctx}/operationPromotion/operationPromotionTemplateOperForm?','新增')" value="新增" />
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-bordered table-condensed">
		<thead>
			<tr>
				<th>id</th>
				<th>名字坐标1（px）</th>
				<th>名字坐标2（px）</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${templateList}" var="u">
				<tr>
					<td>${u.id}</td>
					<td>${u.info1}</td>
					<td>${u.info2}</td>
					<td>
						<a href="#" onclick="operationPromotionTemplateOperForm('${ctx}/operationPromotion/operationPromotionTemplateOperForm?id=${u.id}','账号修改')">账号修改</a>
						<a href="${ctx}/operationPromotion/deleteOperationPromotionTemplate?id=${u.id}" onclick="return confirmx('确认删除？', this.href)">删除</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>