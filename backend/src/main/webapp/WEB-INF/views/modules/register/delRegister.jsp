<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>预约</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			document.getElementById("operPrice").value="${registerServiceVo.price}";
			document.getElementById("operServerType").value="${registerServiceVo.serviceType}";
			document.getElementById("time").value="${time}";
			document.getElementById("registerId").value="${registerServiceVo.id}";
			document.getElementById("operType").value="del";
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
		function modifyRegister(){
			var the_url = "${ctx}/register/operRegisterForm?sysDoctorId=${registerServiceVo.sysDoctorId}&locationId="+locationId+"&date="+date+"&time=${time}&operFlag=oper";
			registerShowWindow("操作号源", the_url, 500);
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="javascript:modifyRegister()">修改号源</a></li>
		<li class="active"><a>删除号源</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="patientRegisterServiceVo" action="${ctx}/order/saveAppointment?sysRegisterServiceId=${registerId}" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<input id="time" type="hidden" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<input id="operType" type="hidden" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<input id="registerId" type="hidden" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<div class="control-group"  style="margin-left: -100px">
			<label class="control-label">时间:</label>
			<div class="controls">
				<span class="help-inline">${date} ${time}</font> </span>
			</div>
		</div>
		<div class="control-group"  style="margin-left: -100px">
			<label class="control-label">价格:</label>
			<div class="controls">
				<input id="operPrice" name="operPrice"  htmlEscape="false" maxlength="50"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group"  style="margin-left: -100px">
			<label class="control-label">服务类型:</label>
			<div class="controls">
				<select id="operServerType" name="operServerType" htmlEscape="false" maxlength="50" onchange="serverChange()">
					<option value="1">专家门诊</option>
					<option value="2">专家门诊需等待</option>
					<option value="3">特需门诊</option>
					<option value="4">特需门诊需等待</option>
					<option value="5">私立医院</option>
				</select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group"  style="margin-left: -100px">
			<div class="controls" >
				<input id="operCopy" name="operCopy" type="checkbox" value="yes">
				<label for="operCopy" >删除所有重复的号源</label>
			</div>
		</div>
	</form:form>
</body>
</html>