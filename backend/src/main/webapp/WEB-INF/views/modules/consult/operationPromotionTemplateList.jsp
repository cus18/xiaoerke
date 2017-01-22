<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>图片传播列表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#consultPay").click(function(){
				$("#searchForm").attr("action","${ctx}/operationPromotion/operationPromotionTemplateOperForm?");
				$("#searchForm").submit();
			});
		});

		function searchSub(){
			$("#searchForm").attr("action","${ctx}/operationPromotion/operationPromotionTemplateList?");
			$("#searchForm").submit();
		}

		function operationPromotionTemplateOperForm(href,title,type){
			href=encodeURI(encodeURI(href+"type="+type));
			top.$.jBox.open('iframe:'+href,title,630,470,{
				buttons:{"关闭":true},
				closed: function () {
					$("#searchForm").attr("action","${ctx}/operationPromotion/operationPromotionTemplateList?type="+type);
					$("#searchForm").submit();
				}
			});
		}
	</script>
</head>
<body>
<ul class="nav nav-tabs">
	<c:if test="${vo.type eq 'pictureTransmission'}">
		<li class="active"><a href="${ctx}/operationPromotion/operationPromotionTemplateList?">图片传播列表</a></li>
	</c:if>
</ul>
<form:form id="searchForm" modelAttribute="vo" action="${ctx}/operationPromotion/consultDoctorDepartmentList?" method="post" class="breadcrumb form-search ">
	<sys:message content="${message}"/>
	<form:input type="hidden" path="type" value="${vo.type}"/>
	<ul class="ul-form">
		<li><label>ID：</label>
			<form:input path="id" htmlEscape="false" maxlength="50" class="input-medium"/>
		</li>
		<li class="btns">
			<input class="btn btn-primary" type="button" onclick="searchSub()" value="查询" />
			<c:if test="${vo.type eq 'consultPay'}">
				<input class="btn btn-primary" type="button" id="consultPay" value="新增" />
			</c:if>
			<c:if test="${vo.type eq 'pictureTransmission'}">
				<input class="btn btn-primary" type="button" onclick="operationPromotionTemplateOperForm('${ctx}/operationPromotion/operationPromotionTemplateOperForm?','新增','${vo.type}')" value="新增" />
			</c:if>
		</li>
		<li class="clearfix"></li>
	</ul>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-bordered table-condensed">
	<thead>
	<tr>
		<th>id</th>
		<c:if test="${vo.type eq 'pictureTransmission'}">
			<th>名字坐标1（px）</th>
			<th>名字坐标2（px）</th>
		</c:if>
		<c:if test="${vo.type eq 'consultPay'}">
			<th>日期</th>
			<th>时间段</th>
			<th>时段场景</th>
			<th>内容</th>
		</c:if>
		<th>操作</th>
	</tr>
	</thead>
	<tbody>
	<c:forEach items="${templateList}" var="u">
		<tr>
			<td>${u.id}</td>
			<c:if test="${u.type eq 'pictureTransmission'}">
				<td>${u.info1}</td>
				<td>${u.info2}</td>
			</c:if>
			<c:if test="${u.type eq 'consultPay'}">
				<td><fmt:formatDate value="${u.info3}" pattern="yyyy.MM.dd"/>-<fmt:formatDate value="${u.info4}" pattern="yyyy.MM.dd"/></td>
				<td><fmt:formatDate value="${u.info5}" pattern="HH:mm:ss"/>-<fmt:formatDate value="${u.info6}" pattern="HH:mm:ss"/></td>
				<td>${u.info1}</td>
				<td>${u.info2}</td>
			</c:if>
			<td>
				<c:if test="${u.type eq 'pictureTransmission'}">
					<a href="#" onclick="operationPromotionTemplateOperForm('${ctx}/operationPromotion/operationPromotionTemplateOperForm?id=${u.id}&','账号修改','${vo.type}')">账号修改</a>
				</c:if>
				<c:if test="${u.type eq 'consultPay'}">
					<a href="${ctx}/operationPromotion/operationPromotionTemplateOperForm?id=${u.id}&type=consultPay">账号修改</a>
				</c:if>
				<a href="${ctx}/operationPromotion/deleteOperationPromotionTemplate?id=${u.id}" onclick="return confirmx('确认删除？', this.href)">删除</a>
			</td>
		</tr>
	</c:forEach>
	</tbody>
</table>
</body>
</html>