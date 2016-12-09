<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>导出统计表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
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
		function exportData(){
			$("#btnSubmit").attr("disabled", true);
			$("#inputForm").submit();
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="" action="${ctx}/consult/dataExport" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<input type="hidden" name="type" value="${type}"/>
		<div class="control-group">
			<label class="control-label">选择时间:</label>
			<div class="controls">
				<input name="fromDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" value="${yesterday}"/>
				至
				<input name="toDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" value="${yesterday}"/>
			</div>
		</div>
		<div class="form-actions" >
			<input id="btnSubmit" onclick="exportData()" class="btn btn-primary" type="button" value="确认"/>
		</div>
	</form:form>
</body>
</html>