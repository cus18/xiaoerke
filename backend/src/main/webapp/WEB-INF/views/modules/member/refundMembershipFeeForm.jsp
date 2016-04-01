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
		
		function refundMembershipFee(){
			var price = document.getElementById("price").value;
			var remark = document.getElementById("remark").value;
			if(price>150||price<=0){
				alert("不能为负数或大于购买金额！");
				return;
			}
			document.getElementById("btnSubmit").disabled=true;
			$.ajax({
	             type: "post",
	             url: "${ctx}/member/refundMembershipFee?",
	             data: {nickName:"${vo.nickName}",id:"${vo.id}",price:price,remark:remark},
	             dataType: "json",
	             success: function(data){
	             	top.$.jBox.close(true);
	             }
        	});
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="vo" action="${ctx}/member/refundMembershipFee?" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<form:hidden path="id" htmlEscape="false" maxlength="50" class="input-xlarge"/>
		<div class="control-group">
			<label class="control-label">会员微信号:</label>
			<div class="controls">
				${vo.nickName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">会员费:</label>
			<div class="controls">
				<input id="price" htmlEscape="false" maxlength="50" class="required" value="50"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">退费原因:</label>
			<div class="controls">
				<input id="remark" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions" >
			<input id="btnSubmit" class="btn btn-primary" type="button" onclick="refundMembershipFee()" value="确认退费"/>
		</div>
	</form:form>
</body>
</html>