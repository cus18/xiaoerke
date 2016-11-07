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
		
		function departmentOper(){
			$.ajax({
	             type: "post",
	             url: "${ctx}/consult/departmentOper",
	             data: {id:"${vo.id}",name:$("#name").val(),sorting:$("#sorting").val(),image:$("#image").val()},
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
	<form:form id="inputForm" modelAttribute="vo" action="${ctx}/consult/departmentOper" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<input type="hidden" value="${vo.id}"/>
		<div class="control-group">
			<label class="control-label">科室名称:</label>
			<div class="controls">
				<input id="name" value="${vo.name}" htmlEscape="false" maxlength="50" class="input-medium"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">排序:</label>
			<div class="controls">
				<input id="sorting" value="${vo.sorting}" htmlEscape="false" maxlength="50" class="input-medium"/>
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
			<input id="btnSubmit" class="btn btn-primary" type="button" onclick="departmentOper()" value="确认"/>
		</div>
	</form:form>
</body>
</html>