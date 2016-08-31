<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>赠送咨询机会</title>
	<meta name="decorator" content="default"/>
	<meta http-equiv="content-type"  content="text/html; charset=UTF-8" />.
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

		function consultGiftTime(){
			document.getElementById("btnSubmit").disabled=true;
			$.ajax({
				type: "post",
				url: "${ctx}/consult/consultTimeGift?",
				data: {id:"${vo.id}",permTimes:document.getElementById("permTimes").value,sysUserId:"${vo.sysUserId}",nickname:"${vo.nickname}"},
				dataType: "json",
				success: function(data){
					top.$.jBox.close(true);
				}
			});
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="vo" action="${ctx}/consult/consultTimeGift?id=${vo.id}" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<div class="control-group">
			<label class="control-label">openid:</label>
			<div class="controls">
					${vo.sysUserId}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">赠送次数:</label>
			<div class="controls">
				<form:input path="permTimes" id="permTimes" htmlEscape="false" maxlength="50" class="input-medium"/>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="button" onclick="consultGiftTime()" value="确认赠送"/>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>