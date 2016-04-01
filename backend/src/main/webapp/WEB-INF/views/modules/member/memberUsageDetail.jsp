<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>使用情况</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a>使用情况</a></li>
	</ul>
	<form:form id="inputForm" modelAttribute="UserReturnVisitVo" action="" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<sys:message content="${message}"/>
		<c:if test="${result=='noDetail'}">
			该用户暂时没有使用会员功能下订单！
		</c:if>
		<c:forEach items="${list}" var="vo" varStatus="status">
			<div class="control-group">
				<label class="control-label"><font size="3" style="font-weight: bold;">第${status.index+1}次预约</font></label>
			</div>
			<div class="control-group">
				<label class="control-label">订单号:</label>
				<div class="controls">
					${vo.register_no}
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">患者姓名:</label>
				<div class="controls">
					${vo.babyName}
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">预约时间:</label>
				<div class="controls">
					${vo.date} ${vo.begin_time}
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">预约医生:</label>
				<div class="controls">
					${vo.name}
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">订单状态:</label>
				<div class="controls">
					${vo.status}
				</div>
			</div>
			<c:if test="${vo.status=='已取消'}">
				<div class="control-group">
					<label class="control-label">是否保留机会:</label>
					<div class="controls">
						${vo.keepChance}
					</div>
				</div>
				<c:if test="${vo.keepChance=='是'}">
					<div class="control-group">
						<label class="control-label">保留机会原因:</label>
						<div class="controls">
							${vo.cancelReason}
						</div>
					</div>
				</c:if>
			</c:if>
		</c:forEach>
		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>