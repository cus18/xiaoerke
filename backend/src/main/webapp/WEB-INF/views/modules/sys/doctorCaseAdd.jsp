<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>添加案例</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function () {
			location.reload;
			$("#btnSubmit").click(function () {
				var doctor_case_name = $("#doctor_case_name").val();
				var doctor_case_number = $("#doctor_case_number").val();
				var display_status = $("#display_status").val();

				if (doctor_case_name == "") {
					alertx("案例名称不能为空！");
					return false;
				}
				if (doctor_case_number == "") {
					alertx("案例数不能为空！");
					return false;
				}
				if (display_status == "") {
					alertx("是否显示不能为空！");
					return false;
				}
				if (display_status != "1" && display_status != "0") {
					alertx("请填写0或1");
					return false;
				}
			});
			$("#inputForm").validate({
				submitHandler: function (form) {
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function (error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
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
		<li class="active"><a href="${ctx}/sys/doctor/doctorManage"><font color="#8a2be2">医生列表</font></a></li>
		<li class="active"><a href="${ctx}/sys/doctorCase/doctorCaseManage"><font color="#006400">医生案例列表</font></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="doctorCaseVo" action="${ctx}/sys/doctorCase/insertDoctorCase?source=doctor_case_add" method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		<form:hidden path="sys_doctor_id"/>
		<div class="control-group">
			<label class="control-label">医生名称:</label>
			<div class="controls">
					${doctorCaseVo.doctorName}
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">案例名称:</label>
			<div class="controls">
				<form:input path="doctor_case_name" htmlEscape="false"  class="input-small" />
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">案例数:</label>
			<div class="controls">
				<form:input path="doctor_case_number" htmlEscape="false"  class="input-small" />
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">是否显示:</label>
			<div class="controls">
				<form:input path="display_status" htmlEscape="false"  class="input-small" />
				<span class="help-inline"><font color="red">0 显示，1 不显示</font></span>
			</div>
		</div>

		<div class="form-actions">
			<shiro:hasPermission name="sys:user:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>