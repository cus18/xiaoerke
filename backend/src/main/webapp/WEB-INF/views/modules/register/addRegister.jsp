<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>预约</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			document.getElementById("time").value="${time}";
			document.getElementById("operType").value="add";
			addChoiceInterval();
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
		function addChoiceInterval(){
		    if(document.getElementById("operCopy").checked==true){
			    document.getElementById("operInterval1").disabled=false;
			    document.getElementById("operInterval2").disabled=false;
		    }else{
			    document.getElementById("operInterval1").disabled=true;
			    document.getElementById("operInterval2").disabled=true;
		    }
	    }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a>添加号源</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="patientRegisterServiceVo" action="${ctx}/order/saveAppointment?sysRegisterServiceId=${registerId}" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<input id="time" type="hidden" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<input id="operType" type="hidden" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<div class="control-group"  style="margin-left: -100px">
			<label class="control-label">时间:</label>
			<div class="controls">
				<span class="help-inline">${date} ${time}</font> </span>
			</div>
		</div>
		<div class="control-group"  style="margin-left: -100px">
			<label class="control-label">价格:</label>
			<div class="controls">
				<input id="operPrice" name="operPrice" htmlEscape="false" maxlength="50"/>
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
				<input id="operCopy" name="operCopy" type="checkbox" onclick="addChoiceInterval()" value="yes">
				<label for="operCopy" >自动按周重复该设置</label>
			</div>
			<div class="controls" >
				<input id="operInterval1" name="operInterval" checked="checked" type="radio" value="0">
				<label for="operInterval1" >每周重复</label>
				<input id="operInterval2" name="operInterval" type="radio" value="1">
				<label for="operInterval2" >隔周重复</label>
			</div>
		</div>
	</form:form>
</body>
</html>