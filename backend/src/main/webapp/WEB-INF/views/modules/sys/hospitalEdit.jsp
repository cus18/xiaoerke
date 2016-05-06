<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
	<title>医院信息</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function () {
			$("#inputForm").validate({
				submitHandler: function (form) {
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function (error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			$("#cityName").val("${hospitalVo.cityName}");
		});

	</script>

</head>
<body>
<ul class="nav nav-tabs">
	<li class="active"><a href="${ctx}/sys/hospital/hospitalManage">医院列表</a></li>
</ul>
<br/>

<form:form id="inputForm" modelAttribute="contactVo" action="${ctx}/sys/hospital/hospitalSave" method="post"
		   class="form-horizontal">
	<input id="sysHospitalId" name="sysHospitalId" type="hidden" value="${contactVo.sysHospitalId}"  class="input-xlarge"/>
	<sys:message content="${message}"/>
	<div class="control-group">
		<label class="control-label">医院名称:</label>

		<div class="controls">
			<input id="name" name="name" type="text" value="${hospitalVo.name}"  class="input-xlarge"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>

	<div class="control-group">
		<label class="control-label">医院类型:</label>

		<div class="controls">
			<input id="hospitalType" name="hospitalType" type="hidden" value="${hospitalVo.hospitalType}"  class="input-xlarge"/>
				${hospitalVo.hospitalType eq "2" ? "合作机构" : "公立医院"}
		</div>
	</div>
	<c:if test="${hospitalVo.hospitalType eq '2'}">

		<div class="control-group">
			<label class="control-label">合作机构联系人:</label>

			<div class="controls">
				<input id="contactName" name="contactName" type="text" value="${hospitalVo.businessContactName}"
					   class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合作机构联系人电话:</label>

			<div class="controls">
				<input id="contactPhone" name="contactPhone" type="text" value="${hospitalVo.businessContactPhone}"
					   class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">医院图片:</label>

			<div class="controls">
				<input type="hidden" id="hospitalPic" name="hospitalPic" value="http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/${contactVo.sysHospitalId}" />
				<sys:ckfinder input="hospitalPic" type="thumb" uploadPath="/sys/hospital" selectMultiple="false"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">费用减免:</label>

			<div class="controls">
				<input id="costReduction" name="costReduction" type="text" value="${contactVo.costReduction}"
					   class="input-xlarge"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">绿色通道:</label>

			<div class="controls">
				<input id="greenChannel" name="greenChannel" type="text" value="${contactVo.greenChannel}"
					   class="input-xlarge"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">限价标准:</label>

			<div class="controls">
				<input id="limitStandard" name="limitStandard" type="text" value="${contactVo.limitStandard}"
					   class="input-xlarge"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">1）限价范围:</label>

			<div class="controls">
				<input id="limitRange" name="limitRange" type="text" value="${contactVo.limitRange}"
					   class="input-xlarge"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">2）限价疾病:</label>

			<div class="controls">
				<input id="limitDisease" name="limitDisease" type="text" value="${contactVo.limitDisease}"
					   class="input-xlarge"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">中药:</label>

			<div class="controls">
				<input id="chineseMedicine" name="chineseMedicine" type="text" value="${contactVo.chineseMedicine}"
					   class="required"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">西药:</label>

			<div class="controls">
				<input id="westernMedicine" name="westernMedicine" type="text" value="${contactVo.westernMedicine}"
					   class="required"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">检查项目:</label>

			<div class="controls">
				<input id="inspectionItems" name="inspectionItems" type="text" value="${contactVo.inspectionItems}"
					   class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">药费:</label>

			<div class="controls">
				<input id="medicineFee" name="medicineFee" type="text" value="${contactVo.medicineFee}"
					   class="input-xlarge"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">检查费:</label>

			<div class="controls">
				<input id="inspectionFee" name="inspectionFee" type="text" value="${contactVo.inspectionFee}"
					   class="input-xlarge"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">诊疗项目:</label>

			<div class="controls">
				<input type="hidden" id="clinicItemsPic" name="clinicItemsPic" value="http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/${contactVo.sysHospitalId}clinicItems" />
				<sys:ckfinder input="clinicItemsPic" type="thumb" uploadPath="/sys/hospital" selectMultiple="false"/><br>
				<input id="clinicItems" name="clinicItems" type="text" value="${contactVo.clinicItems}"
					   class="input-xlarge"/>
			</div>
		</div>
	</c:if>

	<div class="control-group">
		<label class="control-label">医院地理位置:</label>

		<div class="controls">
			<input id="position" name="position" type="text" value="${hospitalVo.position}"
				   class="required"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">医院的详细信息描述:</label>

		<div class="controls">
			<textarea name="details" rows="4" maxlength="200" class="required" style="width:270px;">${hospitalVo.details}</textarea>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>

	<div class="control-group">
		<label class="control-label">所在城市:</label>

		<div class="controls">
			<select id="cityName" name="cityName" class="input-medium">
				<c:forEach items="${fns:getDictList('sys_city')}" var="city">
					<option value="${city.value}">${city.label}</option>
				</c:forEach>
			</select>
		</div>
	</div>

	<div class="control-group">
		<label class="control-label">是否在客户端显示:</label>
		<div class="controls">
			<input id="isDisplay" name="isDisplay" type="text" value="${hospitalVo.isDisplay}" class="required"/>
			<span class="help-inline"><font color="red">*</font>显示为 display，隐藏请填写 hidden </span>
		</div>
	</div>


	<div class="control-group">
		<label class="control-label">排序:</label>

		<div class="controls">
			<input id="sort" name="sort" type="text" value="${hospitalVo.sort}"
				   class="required"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>


	<div class="control-group">
		<label class="control-label">就诊流程:</label>

		<div class="controls">
			<textarea name="medicalProcess" rows="4" maxlength="200" class="required" style="width:270px;">${hospitalVo.medicalProcess}</textarea>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>

	<div class="form-actions">
		<input id="btnSubmit" class="btn btn-primary" onclick="" type="submit" value="保 存"/>
	</div>
</form:form>
</body>
</html>