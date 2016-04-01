<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
	<title>医院信息</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function () {

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
	<li class="active"><a href="${ctx}/sys/hospital/hospitalManage">医院列表</a></li>
</ul>
<br/>

<form:form id="inputForm" modelAttribute="hospitalVo" action="${ctx}/sys/hospital/hospitalSave" method="post"
		   class="form-horizontal">
	<form:hidden path="id"/>
	<sys:message content="${message}"/>
	<div class="control-group">
		<label class="control-label">医院名称:</label>

		<div class="controls">
			<input id="name" name="name" type="text" value="${hospitalVo.name}"  class="input-xlarge"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>

	<c:if test="${not empty hospitalVo.businessContactName}">
		<div class="control-group">
			<label class="control-label">合作机构联系人:</label>

			<div class="controls">
				<input id="businessContactName" name="businessContactName" type="text" value="${hospitalVo.businessContactName}"
					   class="input-xlarge"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合作机构联系人电话:</label>

			<div class="controls">
				<input id="businessContactPhone" name="businessContactPhone" type="text" value="${hospitalVo.businessContactPhone}"
					   class="input-xlarge"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
	</c:if>

	<div class="control-group">
		<label class="control-label">医院地理位置:</label>

		<div class="controls">
			<input id="position" name="position" type="text" value="${hospitalVo.position}"
				   class="input-xxlarge"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">医院的详细信息描述:</label>

		<div class="controls">

			<form:textarea path="details" htmlEscape="false" rows="2"  class="input-xxlarge"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>

	<div class="control-group">
		<label class="control-label">所在城市:</label>

		<div class="controls">
			<form:select path="cityName" class="input-medium">
				<form:options items="${fns:getDictList('sys_city')}" itemLabel="label" itemValue="value"
							  htmlEscape="false"/>
			</form:select>
		</div>
	</div>

	<div class="control-group">
		<label class="control-label">是否在客户端显示:</label>
		<div class="controls">
			<form:input path="isDisplay" htmlEscape="false" /><span class="help-inline">显示为 display，隐藏请填写 hidden </span>
		</div>
	</div>


	<div class="control-group">
		<label class="control-label">排序:</label>

		<div class="controls">
			<input id="sort" name="sort" type="text" value="${hospitalVo.sort}"
				   class="required"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>


	<div class="control-group">
		<label class="control-label">就诊流程:</label>

		<div class="controls">
			<form:textarea path="medicalProcess" htmlEscape="false" rows="5"  class="input-xxlarge"/>
		</div>
	</div>

	<div class="form-actions">
		<input id="btnSubmit" class="btn btn-primary" onclick="" type="submit" value="保 存"/>
	</div>
</form:form>
</body>
</html>