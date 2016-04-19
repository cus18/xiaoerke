<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>退费</title>
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
		
		function cancelOrder(){
			var auditReason = "";
			if($("#price").val()==""){
				alertx("请填写补贴金额！");
				return;
			}else{
				if(isNaN($("#price").val())||$("#price").val()<=0||$("#price").val()>400){
				   alertx("请填写0-400的数字！");
				   return;
				}
			}
			if($("#cancelReason").val()==""){
				alertx("请填写退费原因！");
				return;
			}
			$.ajax({
	             type: "post",
	             url: "${ctx}/consultPhone/cancelOrder?",
	             data: {id:"${vo.id}",price:$("#price").val(),cancelReason:$("#cancelReason").val()},
	             dataType: "json",
	             success: function(data){
	             	if("suc"==data.suc){
	             		top.$.jBox.close(true);
             		}else{
	             		alertx("操作失败！");
	             	}
	             }
        	});
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="vo" action="${ctx}/insurance/insuranceAudit?state=3" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<div class="control-group">
			<label class="control-label">订单号:</label>
			<div class="controls">
				${vo.id}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">退费:</label>
			<div class="controls">
				<form:input path="price" id="price" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">原因:</label>
			<div class="controls">
				<input id="cancelReason" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions" >
			<input id="btnSubmit" class="btn btn-primary" type="button" onclick="cancelOrder()" value="确认退费"/>
		</div>
	</form:form>
</body>
</html>