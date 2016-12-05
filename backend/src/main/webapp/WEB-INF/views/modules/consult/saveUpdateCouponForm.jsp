<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>添加页面</title>
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
	<form:form id="inputForm" modelAttribute="vo" action="${ctx}/sendMindCoupon/saveSendMindCoupon?" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<form:hidden path="id" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<div class="control-group">
			<label class="control-label">优惠券名称:</label>
			<div class="controls">
				<form:input id="name" path="name" htmlEscape="false" maxlength="50"  class="required number" value="${vo.name}"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">链接地址:</label>
			<div class="controls">
				<form:input id="link" path="link" htmlEscape="false" class="required" value="${vo.link}"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">缩略图:</label>
			<div class="controls">
				<input type="hidden" id="image" name="image" value="${vo.image}" />
				<sys:ckfinder input="image" type="thumb" uploadPath="/cms/article" selectMultiple="false"/>
			</div>
		</div>
		<div class="form-actions">
			<input id="saveConf" class="btn btn-primary" type="submit" value="保存"/>
			<input id="btnCancel" class="btn" type="button" value="返回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>