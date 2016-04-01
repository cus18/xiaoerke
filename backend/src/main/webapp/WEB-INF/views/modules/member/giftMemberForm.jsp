<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>预约</title>
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
		<li class="active"><a>赠送会员</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="MemberservicerelItemservicerelRelationVo" action="${ctx}/member/giftMember?" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<div class="control-group">
			<label class="control-label">手机号码:</label>
			<div class="controls">
				<form:input id="phone" path="phone" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">会员类型:</label>
			<div class="controls">
				<form:select path="type">
					<c:forEach items="${typeMap}" var="type">
				 		<form:option value="${type.key}" label="${type.value}"/>
				 	</c:forEach>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">赠送原因:</label>
			<div class="controls">
				<form:textarea path="remark" htmlEscape="false" maxlength="50" class="input-medium"/>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit"  value="确认赠送"/>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>