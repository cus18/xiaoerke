<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>审核不通过</title>
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
		
		function notPassReasonChange(){
			if($("#notPassReason").val()=="other"){
				$("#otherReason").show();//$("#otherReason").css('display','block'); $("#otherReason")[0].style.display = 'none';
			}else{
				$("#otherReason").hide();//$("#id").show()
			}
		}
		function submitAudit(){
			var auditReason = "";
			if($("#notPassReason").val()=="other"){
				if($("#otherReason").val()==""){
					alertx("请填入不通过原因！");
					return;
				}else{
					auditReason = $("#otherReason").val();
				}
			}else{
				auditReason = $("#notPassReason").val();
			}
			
			$.ajax({
	             type: "post",
	             url: "${ctx}/insurance/insuranceAudit?",
	             data: {id:"${irsvo.id}",state:5,auditReason:auditReason},
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
	<form:form id="inputForm" modelAttribute="irsvo" action="${ctx}/insurance/insuranceAudit?state=5" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<form:hidden path="id" htmlEscape="false" maxlength="50" class="input-xlarge"/>
		<div class="control-group">
			<label class="control-label">微信号:</label>
			<div class="controls">
				${irsvo.nickName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">不通过原因:</label>
			<div class="controls">
				<form:select path="auditReason" name="notPassReason" id="notPassReason" onchange="notPassReasonChange()">
				    <option value="所上传的图片不符合要求">所上传的图片不符合要求</option>
				    <option value="所上传的身份证图片与投保人不一致">所上传的身份证图片与投保人不一致</option>
				    <option value="所上传的就诊证明图片姓名与投保人不一致">所上传的就诊证明图片姓名与投保人不一致</option>
				    <option value="other">其他</option>
			    </form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"></label>
			<div class="controls">
				<input id="otherReason" style="display: none;" htmlEscape="false" maxlength="50" class="required" value=""/>
			</div>
		</div>
		<div class="form-actions" >
			<input id="btnSubmit" class="btn btn-primary" type="button" onclick="submitAudit()" value="确认"/>
		</div>
	</form:form>
</body>
</html>