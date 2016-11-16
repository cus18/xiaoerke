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
		
		function templateOper(){
			if($("#info11").val()==''||$("#info12").val()==''){
				alertx("请填写坐标！");
				return;
			}
			$.ajax({
	             type: "post",
	             url: "${ctx}/operationPromotion/operationPromotionTemplateOper",
	             data: {id:"${vo.id}",info11:$("#info11").val(),info12:$("#info12").val(),info21:$("#info21").val(),info22:$("#info22").val(),image:$("#image").val(),type:"pictureTransmission"},
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
	<form:form id="inputForm" modelAttribute="vo" action="${ctx}/consult/templateOper" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<input type="hidden" value="${vo.id}"/>
		<div class="control-group">
			<label class="control-label">名字坐标1:</label>
			<div class="controls">
				<input id="info11" value="${fn:split(vo.info1,',')[0]}" htmlEscape="false" maxlength="50" style="width: 60px;" class="input-medium"/>，
				<input id="info12" value="${fn:split(vo.info1,',')[1]}" htmlEscape="false" maxlength="50" style="width: 60px;" class="input-medium"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">名字坐标2:</label>
			<div class="controls">
				<input id="info21" value="${fn:split(vo.info2,',')[0]}" htmlEscape="false" maxlength="50" style="width: 60px;" class="input-medium"/>，
				<input id="info22" value="${fn:split(vo.info2,',')[1]}" htmlEscape="false" maxlength="50" style="width: 60px;" class="input-medium"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">缩略图:</label>
			<div class="controls">
				<input type="hidden" id="image" name="image" value="${vo.image}" />
				<sys:ckfinder input="image" type="thumb" uploadPath="/cms/article" selectMultiple="false"/>
			</div>
		</div>
		<div class="form-actions" >
			<input id="btnSubmit" class="btn btn-primary" type="button" onclick="templateOper()" value="确认"/>
		</div>
	</form:form>
</body>
</html>