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
	<form:form id="inputForm" modelAttribute="messagevo" action="${ctx}/message/sendWechatMessage" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">first:</label>
			<div class="controls">
				<form:input name="first" path="first" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">咨询内容(keyword1):</label>
			<div class="controls">
				<form:textarea path="keyword1" htmlEscape="false" maxlength="1150" rows="4" cols="63" class="required input-xxlarge"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">回复内容(keyword2):</label>
			<div class="controls">
				<form:textarea path="keyword2" name="keyword2" htmlEscape="false" maxlength="1150" rows="4" cols="63" class="required input-xxlarge"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">医生名(keyword3):</label>
			<div class="controls">
				<form:input name="keyword3" path="keyword3" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">keyword4:</label>
			<div class="controls">
				<form:input name="keyword4" path="keyword4" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">url:</label>
			<div class="controls">
				<form:input name="url" path="url" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">openId:</label>
			<div class="controls">
				<form:input name="openId" path="openId" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions" >
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="确认发送"/>
		</div>
	</form:form>
</body>
</html>