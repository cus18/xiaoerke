<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>退服务费</title>
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

		function consultRefund(){
			document.getElementById("btnSubmit").disabled=true;
			$.ajax({
				type: "post",
				url: "${ctx}/consult/refundFee?",
				data: {orderId:"${vo.orderId}",amount:"${vo.amount}",openid:"${vo.openid}",refundReason:document.getElementById("refundReason").value},
				dataType: "json",
				success: function(data){
					top.$.jBox.close(true);
				}
			});
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="vo" action="${ctx}/consult/consultTimeGift?" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<div class="control-group">
			<label class="control-label">会员微信号:</label>
			<div class="controls">
					${vo.nickname}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">费用合计:</label>
			<div class="controls">
					${vo.amount}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">退费原因:</label>
			<div class="controls">
				<input id="refundReason" htmlEscape="false" maxlength="50" class="input-medium"/>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="button" onclick="consultRefund()"  value="确认退费"/>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>