<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>预约</title>
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
		
		function submitAudit(){
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
			if($("#auditReason").val()==""){
				alertx("请填写通过原因！");
				return;
			}
			$.ajax({
	             type: "post",
	             url: "${ctx}/insurance/insuranceAudit?",
	             data: {id:"${irsvo.id}",state:3,price:$("#price").val(),auditReason:$("#auditReason").val(),parentId:"${irsvo.parentId}"},
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
	<form:form id="inputForm" modelAttribute="irsvo" action="${ctx}/insurance/insuranceAudit?state=3" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<div class="control-group">
			<label class="control-label">微信号:</label>
			<div class="controls">
				${irsvo.nickName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">补贴金额:</label>
			<div class="controls">
				<form:input path="price" id="price" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">原因:</label>
			<div class="controls">
				<form:input path="auditReason" id="auditReason" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions" >
			<input id="btnSubmit" class="btn btn-primary" type="button" onclick="submitAudit()" value="确认补贴"/>
		</div>
	</form:form>
</body>
</html>