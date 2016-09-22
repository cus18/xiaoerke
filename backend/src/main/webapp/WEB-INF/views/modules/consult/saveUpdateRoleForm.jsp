<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>添加修改规则页面</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/qqemotion/jquery.qqFace.js" type="text/javascript"></script>
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
			$("#deleteRole").click(function(){
				$("#inputForm").attr("action","${ctx}/operationPromotion/deleteKeywordRole");
				$("#inputForm").submit();
			});
		});

		$(function(){
			$('#emotion').qqFace({
				id : 'facebox',
				assign:'saytext',
				path:'${ctxStatic}/qqemotion/arclist/'	//表情存放的路径
			});
		});
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="vo" action="${ctx}/operationPromotion/saveKeywordRole?" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<form:hidden path="roleId" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<div class="control-group">
			<label class="control-label">规则名:</label>
			<div class="controls">
				<form:input id="roleName" path="roleName" htmlEscape="false" maxlength="50"  class="required" value="${vo.roleName}"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">关键字:</label>
			<div class="controls">
				<form:textarea id="keyword" path="keyword" htmlEscape="false" maxlength="50"  class="required" value="${vo.keyword}"/>
				<span class="help-inline"><font color="red" size="6px">*多个关键字以空格分隔</font> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">回复:</label>
			<div class="controls">
				<form:textarea class="input" path="replyText" id="saytext" name="saytext" style="width: 400px ;height: 80px" value="${vo.replyText}"></form:textarea>
				<img id="emotion" src="http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fqqface%2F1.gif"/>
			</div>
			<div id="show"></div>
		</div>

		<div class="control-group">
			<div class="controls">
				<form:textarea id="replyText" htmlEscape="true" path="imgPath" rows="4" maxlength="200" class="input-xxlarge" value="${vo.imgPath}"/>
				<sys:ckeditor replace="replyText" uploadPath="/cms/article" />
			</div>
		</div>
		<div class="form-actions">
			<input id="saveRole" class="btn btn-primary" type="submit" value="保存"/>
			<input id="deleteRole" class="btn btn-primary" type="button" value="删除"/>
			<input id="btnCancel" class="btn" type="button" value="返回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>