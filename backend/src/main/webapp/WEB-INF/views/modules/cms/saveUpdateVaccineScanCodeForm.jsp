<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>疫苗信息录入</title>
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
			$("input[type='radio'][name=isfree][value='${vo.isfree}']").attr("checked",true);
		});
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="vo" action="${ctx}/cms/vaccineScanCode/saveUpdateVaccineScanCode" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<form:hidden path="id" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<div class="control-group">
			<label class="control-label">疫苗:</label>
			<div class="controls">
				<form:input id="name" path="name" htmlEscape="false" maxlength="50"  class="required" value="${vo.name}"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">月龄:</label>
			<div class="controls">
				<form:input id="age" path="age" htmlEscape="false" maxlength="50"  class="required" value="${vo.age}"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">费用:</label>
			<div class="controls">
				<input type="radio" name="isfree" value="自费" id="notfree" checked><label for="notfree">自费</label>
				<input type="radio" name="isfree" value="免费" id="free"><label for="free">免费</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">预防疾病:</label>
			<div class="controls">
				<form:input id="diseasePrevention" path="diseasePrevention" htmlEscape="false" maxlength="50"  class="required" value="${vo.diseasePrevention}"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">注意事项:</label>
			<div class="controls">
				<form:textarea path="attention" id="attention" name="saytext" style="width: 400px ;height: 80px" value="${vo.attention}"></form:textarea>
			</div>
			<div id="show"></div>
		</div>

		<div class="control-group">
			<label class="control-label">告知单:</label>
			<div class="controls">
				<form:textarea id="informedForm" htmlEscape="true" path="informedForm" rows="4" maxlength="200" class="input-xxlarge" value="${vo.informedForm}"/>
			</div>
		</div>
		<div class="form-actions">
			<input class="btn btn-primary" type="submit" value="保存"/>
			<input id="btnCancel" class="btn" type="button" value="返回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>