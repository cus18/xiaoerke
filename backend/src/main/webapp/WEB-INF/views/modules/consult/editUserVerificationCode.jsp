<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>验证码</title>
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
			$("#userType").val('${user.userType}');
		});

		function doctorOper(){
			if($("#name").val()==""){
				alertx("请填写姓名！");
				return;
			}
			if($("#userPhone").val()==""){
				alertx("请填写手机号！");
				return;
			}
			if($("#code").val()==""){
				alertx("请填写验证码！");
				return;
			}
			if($("#createTime").val()==""){
				alertx("请填写有效期！");
				return;
			}
			$.ajax({
				type: "post",
				url: "${ctx}/consult/updateVerificationCode",
				data: {id:"${validateBean.id}",name:$("#name").val(),userPhone:$("#userPhone").val(),code:$("#code").val(),createTime:$("#createTime").val()},
				dataType: "json",
				success: function(data){
					if("suc"==data.result){
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
	<form:form id="inputForm" modelAttribute="validateBean" action="${ctx}/consult/updateVerificationCode" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<input type="hidden" value="${validateBean.id}"/>
		<div class="control-group">
			<label class="control-label">姓名:</label>
			<div class="controls">
				<input id="name" value="${validateBean.name}" htmlEscape="false" maxlength="50" class="input-medium"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">手机号:</label>
			<div class="controls">
				<input id="userPhone" value="${validateBean.userPhone}" htmlEscape="false" maxlength="50" class="input-medium"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">出生日期后6位:</label>
			<div class="controls">
				<input id="code" value="${validateBean.code}" htmlEscape="false" maxlength="50" class="input-medium"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">有效期:</label>
			<form:input name="createTime" path="createTime" type="text" readonly="readonly" maxlength="100" class="input-small Wdate"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
		</div>


		<div class="form-actions" >
			<input id="btnSubmit" class="btn btn-primary" type="button" value="确认"/>
		</div>
	</form:form>
</body>
</html>