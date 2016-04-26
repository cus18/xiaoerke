<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>赠送保险</title>
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
		<li><a href="${ctx}/insurance/insuranceHospitalList?">保险关联医院列表</a></li>
		<li class="active"><a href="">添加保险关联医院</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="insuranceHospitalVo" action="${ctx}/insurance/saveInsuranceHospital?" method="post" class="form-horizontal">
		<form:hidden path="id" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<%--<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">医院:</label>
			<div class="controls">
				<form:input id="hospitalName" path="hospitalName" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">地址:</label>
			<div class="controls">
				<form:input id="location" path="location" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">区:</label>
			<div class="controls">
				<form:select path="district" class="input-mini">
					<form:option value="0" label="海淀区"/>
					<form:option value="1" label="朝阳区"/>
					<form:option value="2" label="东城区"/>
					<form:option value="3" label="西城区"/>
					<form:option value="4" label="石景山区"/>
					<form:option value="5" label="丰台区"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">手机号码:</label>
			<div class="controls">
				<form:input id="phone" path="phone" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit"  value="确认添加"/>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>