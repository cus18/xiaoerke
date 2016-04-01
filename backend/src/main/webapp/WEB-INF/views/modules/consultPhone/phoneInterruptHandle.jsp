<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>电话中断处理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		//审核通过不通过弹窗
		function insuranceAudit(href,title){
			href=encodeURI(encodeURI(href));
			top.$.jBox.open('iframe:'+href,title,$(top.document).width()-860,$(top.document).height()-330,{
				buttons:{"关闭":true},
				closed: function () { 
					//$("#searchForm").attr("action","${ctx}/member/memberList?");
					//$("#searchForm").submit();
				}
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a><font color="#b8860b">电话中断处理</font></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="irsvo" action="" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<form:input id="urvid" path="id" type="hidden" value="${vo.id}"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">订单状态:</label>
			<div class="controls">
				<td>
					<c:if test="${irsvo.state eq '0'}">待生效</c:if>
					<c:if test="${irsvo.state eq '1'}">有效</c:if>
					<c:if test="${irsvo.state eq '2'}">待审核</c:if>
					<c:if test="${irsvo.state eq '3'}">已赔付</c:if>
					<c:if test="${irsvo.state eq '4'}">已到期</c:if>
					<c:if test="${irsvo.state eq '5'}">审核失败</c:if>
					<c:if test="${irsvo.state eq '6'}">待付款</c:if>
				</td>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">手机号码:</label>
			<div class="controls">
				${irsvo.parentPhone}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">购买人:</label>
			<div class="controls">
				${irsvo.parentName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">身份证号:</label>
			<div class="controls">
				${irsvo.idCard}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">宝宝姓名:</label>
			<div class="controls">
				${irsvo.babyName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">下单时间:</label>
			<div class="controls">
				<fmt:formatDate value ="${irsvo.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开始时间:</label>
			<div class="controls">
				<fmt:formatDate value ="${irsvo.startTime}" pattern="yyyy-MM-dd HH:mm:ss" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">结束时间:</label>
			<div class="controls">
				<fmt:formatDate value ="${irsvo.endTime}" pattern="yyyy-MM-dd HH:mm:ss" />
			</div>
		</div>
		<div class="form-actions">
			<c:if test="${irsvo.state eq '2'}">
				<input id="btnSubmit" class="btn btn-primary" type="button" onclick="insuranceAudit('${ctx}/insurance/auditForm?id=${irsvo.id}&nickName=${irsvo.nickName}&parentId=${irsvo.parentId}&state=3','审核通过')" value="审核通过"></input>
				<input id="btnSubmit" class="btn btn-primary" type="button" onclick="insuranceAudit('${ctx}/insurance/auditForm?id=${irsvo.id}&nickName=${irsvo.nickName}&parentId=${irsvo.parentId}&state=5','审核不通过')" value="审核不通过"/>
			</c:if>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>