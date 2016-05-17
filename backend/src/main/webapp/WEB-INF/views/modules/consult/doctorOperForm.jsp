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
			$("#userType").val('${user.userType}');
		});
		
		function doctorOper(){
			if($("#name").val()==""){
				alertx("请填写姓名！");
				return;
			}
			if($("#email").val()==""){
				alertx("请填写邮箱！");
				return;
			}
			if($("#phone").val()==""){
				alertx("请填写电话！");
				return;
			}
			$.ajax({
	             type: "post",
	             url: "${ctx}/consult/doctorOper",
	             data: {id:"${user.id}",name:$("#name").val(),phone:$("#phone").val(),email:$("#email").val(),userType:$("#userType").val()},
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
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/consult/doctorOper" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<input type="hidden" value="${user.id}"/>
		<div class="control-group">
			<label class="control-label">姓名:</label>
			<div class="controls">
				<input id="name" value="${user.name}" htmlEscape="false" maxlength="50" class="input-medium"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">手机号:</label>
			<div class="controls">
				<input id="phone" value="${user.phone}" htmlEscape="false" maxlength="50" class="input-medium"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">邮箱:</label>
			<div class="controls">
				<input id="email" value="${user.email}" htmlEscape="false" maxlength="50" class="input-medium"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">用户角色:</label>
			<div class="controls">
				<select id="userType" class="txt required" style="width:100px;">
					<option value="consultDoctor">咨询医生</option>
					<option value="distributor">分诊小宝</option>
				</select>
			</div>
		</div>
		<div class="form-actions" >
			<input id="btnSubmit" class="btn btn-primary" type="button" onclick="doctorOper()" value="确认"/>
		</div>
	</form:form>
</body>
</html>