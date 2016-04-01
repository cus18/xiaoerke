<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>疾病信息</title>
	<meta name="decorator" content="default"/>
		<script type="text/javascript">


			$(document).ready(function() {

				$("#btnSubmit").click(function () {
					var level_2 = $("#keyword").val();
					if (level_2 == "") {
						alertx("热词不能为空！");
						return false;
					}

				})

				$("#inputForm").validate({
					submitHandler: function(form){
						loading('正在提交，请稍等...');
						form.submit();
					},
					errorContainer: "#messageBox",
					errorPlacement: function (error, element) {
						if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
							error.appendTo(element.parent().parent());
						} else {
							error.insertAfter(element);
						}

					}
				});

			});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/keyword/addKeyword">新增热词</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="SearchKeyword" action="${ctx}/sys/keyword/addKeyword" method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">关键词:</label>
			<div class="controls">
				<input id="keyword" name="keyword" type="text" value="${SearchKeyword.keyword}" class="required" />
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
		</div>
	</form:form>
</body>
</html>