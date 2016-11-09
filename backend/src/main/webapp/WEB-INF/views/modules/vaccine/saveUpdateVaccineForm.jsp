<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>疫苗信息录入</title>
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
			$("#deleteVaccine").click(function(){
				confirmx("确认删除吗？",function(){
					$("#inputForm").attr("action","${ctx}/vaccine/deleteVaccine?id=${vo.id}");
					$("#inputForm").submit();
				})
			});
		});
		function saveVaccine(){
			if($("#miniumAge").val()%30!=0){
				alertx("最小月龄必须是30的倍数！");
				return;
			}
			if($("#lastTimeInterval").val()%30!=0){
				alertx("周期必须是30的倍数！");
				return;
			}
			$("#inputForm").attr("action","${ctx}/vaccine/saveUpdateVaccineInfo");
			$("#inputForm").submit();
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="vo" action="${ctx}/vaccine/saveUpdateVaccineInfo" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<form:hidden path="id" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<div class="control-group">
			<label class="control-label">疫苗名称:</label>
			<div class="controls">
				<form:input id="name" path="name" htmlEscape="false" maxlength="50"  class="required" value="${vo.name}"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<c:if test="${not empty vo.id}">
			<div class="control-group">
				<label class="control-label">疫苗编号:</label>
				<div class="controls">
					PD_YMTX_${vo.id}
				</div>
			</div>
		</c:if>
		<div class="control-group">
			<label class="control-label">接种最小月龄:</label>
			<div class="controls">
				<form:input id="miniumAge" path="miniumAge" htmlEscape="false" maxlength="50"  class="required number" value="${vo.miniumAge}"/>
				<span class="help-inline"><font color="red">*单位是天，必须为30的倍数</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">周期:</label>
			<div class="controls">
				<form:input id="lastTimeInterval" path="lastTimeInterval" htmlEscape="false" maxlength="50"  class="required number" value="${vo.lastTimeInterval}"/>
				<span class="help-inline"><font color="red">*单位是天，必须为30的倍数</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">简介:</label>
			<div class="controls">
				<form:textarea path="introduce" id="saytext" name="saytext" style="width: 400px ;height: 80px" value="${vo.introduce}"></form:textarea>
			</div>
			<div id="show"></div>
		</div>

		<div class="control-group">
			<label class="control-label">注意事项:</label>
			<div class="controls">
				<form:textarea id="attention" htmlEscape="true" path="attention" rows="4" maxlength="200" class="input-xxlarge" value="${vo.attention}"/>
			</div>
		</div>
		<div class="form-actions">
			<input id="saveRole" class="btn btn-primary" type="button" onclick="saveVaccine()" value="保存"/>
			<input id="deleteVaccine" class="btn btn-primary" type="button" value="删除"/>
			<input id="btnCancel" class="btn" type="button" value="返回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>