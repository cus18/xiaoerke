<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>excle数据导入</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function () {
			$("#hospitalSubmit").click(function () {
				var hospitalFile = $("#hospitalFile").val();
				if (hospitalFile == "") {
					alertx("请添加医院数据！");
					return false;
				}
			});
			$("#illnessSubmit").click(function () {
				var hospitalFile = $("#illnessFile").val();
				if (hospitalFile == "") {
					alertx("请添加疾病数据！");
					return false;
				}
			});
			$("#doctorIllnessSubmit").click(function () {
				var hospitalFile = $("#doctorIllnessFile").val();
				if (hospitalFile == "") {
					alertx("请添加医生与疾病关联数据！");
					return false;
				}
			});
			$("#doctorSubmit").click(function () {
				var hospitalFile = $("#doctorFile").val();
				if (hospitalFile == "") {
					alertx("请添加医生数据！");
					return false;
				}
			});
			$("#sourceSubmit").click(function () {
				var sourceFile = $("#sourceFile").val();
				if (sourceFile == "") {
					alertx("请添加号源数据！");
					return false;
				}
			});


			$("#hospitalForm").validate({
				submitHandler: function (form) {
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox"
			});
			$("#illnessForm").validate({
				submitHandler: function (form) {
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox"
			});
			$("#doctorIllnessForm").validate({
				submitHandler: function (form) {
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox"
			});
			$("#doctorForm").validate({
				submitHandler: function (form) {
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox"
			});
			$("#sourceForm").validate({
				submitHandler: function (form) {
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox"
			});

		});
	</script>

</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/hospital/hospitalDataImp">医院信息录入</a></li>
	</ul><br/>

	<%--1、医院信息导入--%>
	<form:form id="hospitalForm"  enctype="multipart/form-data" action="${ctx}/sys/excle/hospitalExcleUpload" method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		<div  class="control-group">
			<label class="control-label">医院信息导入:</label>
			<div id="msg" class="controls">
				<input id="hospitalFile" type="file" name="files" class="btn btn-small">&nbsp;&nbsp;&nbsp;
				<input id="hospitalSubmit" type="submit" class="btn btn-success" value="导入">
				<span class="help-inline">生成医院信息</span>
				<a href="http://101.200.194.68:8081/backend/excleTemplate/hospital.xls">模版下载</a>
			</div>
		</div>
	</form:form>

	<%--2、疾病信息导入--%>
	<form:form id="illnessForm"  enctype="multipart/form-data" action="${ctx}/sys/excle/illnessExcleUpload" method="post" class="form-horizontal">

		<div class="control-group">
			<label class="control-label">疾病信息导入:</label>
			<div class="controls">
				<input id="illnessFile" type="file" name="files" class="btn btn-small">&nbsp;&nbsp;&nbsp;
				<input id="illnessSubmit" type="submit" class="btn btn-success" value="导入">
				<span class="help-inline">往疾病库里添加数据</span>
				<a href="http://101.200.194.68:8081/backend/excleTemplate/illness.xls">模版下载</a>
			</div>
		</div>
	</form:form>

	<%--3、医生与疾病关联信息导入--%>
	<form:form id="doctorIllnessForm"  enctype="multipart/form-data" action="${ctx}/sys/excle/illnessAndDoctorRelation" method="post" class="form-horizontal">

		<div class="control-group">
			<label class="control-label">医生与疾病关联信息导入:</label>
			<div class="controls">
				<input id="doctorIllnessFile" type="file" name="files" class="btn btn-small">&nbsp;&nbsp;&nbsp;
				<input id="doctorIllnessSubmit" type="submit" class="btn btn-success" value="导入">
				<span class="help-inline">注意：1、医生手机号必须已存在 2、一级疾病和二级疾病在疾病库里已存在 。否则导入失败。</span>
				<a href="http://101.200.194.68:8081/backend/excleTemplate/doctor_illness_relation.xls">模版下载</a>
			</div>
		</div>
	</form:form>


	<%--4、医生信息导入--%>
	<form:form id="doctorForm"  enctype="multipart/form-data" action="${ctx}/sys/excle/doctorDataImpl" method="post" class="form-horizontal">

		<div class="control-group">
			<label class="control-label">医生信息导入:</label>
			<div class="controls">
				<input id="doctorFile" type="file" name="files" class="btn btn-small">&nbsp;&nbsp;&nbsp;
				<input id="doctorSubmit" type="submit" class="btn btn-success" value="导入">
				<span class="help-inline">生成user信息、生成医生信息、医生与医院的关系信息</span>
				<a href="http://101.200.194.68:8081/backend/excleTemplate/doctor.xls">模版下载</a>
			</div>
		</div>
	</form:form>


	<%--5、号源信息导入--%>
	<form:form id="sourceForm"  enctype="multipart/form-data" action="${ctx}/sys/excle/RegisterServiceDataImpl" method="post" class="form-horizontal">

		<div class="control-group">
			<label class="control-label">号源信息导入:</label>
			<div class="controls">
				<input id="sourceFile" type="file" name="files" class="btn btn-small">&nbsp;&nbsp;&nbsp;
				<input id="sourceSubmit" type="submit" class="btn btn-success" value="导入">
				<span class="help-inline">生成号源信息</span>
				<a href="http://101.200.194.68:8081/backend/excleTemplate/service.xls">模版下载</a>
			</div>
		</div>
	</form:form>



</body>
</html>