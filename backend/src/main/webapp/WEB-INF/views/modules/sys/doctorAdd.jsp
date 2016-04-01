<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function () {
			location.reload;
			$("#btnSubmit").click(function () {
				var careerTime = $("#careerTime").val();
				var hospitalId = $("#hospitalId").val();
				var phone = $("#phone").val();
				var subsidy = $("#subsidy").val();
//				var isPhone=/^(?:13\d|15\d|18\d)\d{5}(\d{3}|\*{3})$/;
//				if(!isPhone.test(phone)){
//					alertx("请正确填写电话号码，例如:13415764179");
//					return false;
//				}

				var placeDetail = $("#placeDetail").val();

				var experience = $("#experience").val();
				var cardExperience = $("#cardExperience").val();
				var personDetails = $("#personDetails").val();

				if (careerTime == "") {
					alertx("从业时间不能为空！");
					return false;
				}
				if (hospitalId == "" || hospitalId==null) {
					alertx("归属医院不能为空！");
					return false;
				}
//				if (phone == "") {
//					alertx("医生手机号不能为空！");
//					return false;
//				}
				if (placeDetail == "") {
					alertx("就诊地点不能为空！");
					return false;
				}
				if (experience == "") {
					alertx("擅长疾病不能为空！");
					return false;
				}
//				if (cardExperience == "") {
//					alertx("专业擅长不能为空！");
//					return false;
//				}
				if (personDetails == "") {
					alertx("医生简介不能为空！");
					return false;
				}
//				if (subsidy == 0) {
//					alertx("补贴金额不能为0！");
//					return false;
//				}
			});
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
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/doctor/doctorManage">医生列表</a></li>
		<li class="active"><a href="${ctx}/sys/doctor/doctorEdit?id=${doctorVo.id}">医生<shiro:hasPermission name="sys:user:edit">${not empty doctorVo.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:user:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="doctorVo" action="${ctx}/sys/doctor/doctorAdd" method="post" class="form-horizontal">
		<form:hidden path="sysUserId"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">姓名:</label>
			<div class="controls">
				<form:input path="doctorName" htmlEscape="false"  class="input-small" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">从业时间:</label>&nbsp;&nbsp;&nbsp;&nbsp;
				<input id="careerTime" name="careerTime" type="text" readonly="readonly"  class="input-small Wdate"
					   value="<fmt:formatDate value="${link.weightDate}" pattern="yyyy-MM-dd"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
			<span class="help-inline"><font color="red">*</font> </span>
				<span class="help-inline">数值越大排序越靠前，过期时间可为空，过期后取消置顶。</span>

			</div>
		</div>
		<div class="control-group">
			<label class="control-label">归属医院:</label>
			<div class="controls">
                <sys:treeselect  id="hospital" name="hospital" value="${doctorVo.hospital}" labelName="hospital" labelValue="${doctorVo.hospital}"
					title="医院" url="/sys/hospital/treeHospitalData" cssClass="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>

		</div>

		<div class="control-group">
			<label class="control-label">一级职务:</label>
			<div class="controls">
				<form:input path="position1" htmlEscape="false" />
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">二级职务:</label>
			<div class="controls">
				<%--<form:select path="position2" class="input-medium">--%>
					<%--<form:options items="${fns:getDictList('position2_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>--%>
				<%--</form:select>--%>
				<form:input path="position2" htmlEscape="false" />
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">医生电话:</label>
			<div class="controls">
				<form:input path="phone" htmlEscape="false"/>
				<span class="help-inline"><font color="red">* 如果该医生只在合作医院出诊，手机号可以为空，否则为必填项哈~</font> </span>
			</div>
		</div>
		<%-- <div class="control-group">
			<label class="control-label">就诊地点:</label>
			<div class="controls">
				<form:input path="placeDetail" htmlEscape="false" />
				<span class="help-inline"><font color="red">*</font> </span>
				<span class="help-inline">在当前医院的就诊地点</span>
			</div>
		</div> --%>

		<%-- <div class="control-group">
			<label class="control-label">与医院所属关系:</label>
			<div class="controls">
				<select id="relationType" style="width: 100px"  name="relationType" htmlEscape="false">
					<option value="0">0</option>
					<option value="1">1</option>
				</select>
				<span class="help-inline"><font color="red">*</font> </span>
				<form:input path="relationType" htmlEscape="false" maxlength="100"/>
				<span class="help-inline">医生和此医院的关系（1表示归属关系，0表示仅在此坐诊执业）</span>
			</div>
		</div> --%>

		<div class="control-group">
			<label class="control-label">一级科室:</label>
			<div class="controls">
				<form:input path="departmentLevel1" htmlEscape="false" />

			</div>
		</div>

		<div class="control-group">
			<label class="control-label">二级科室:</label>
			<div class="controls">
				<form:input path="departmentLevel2" htmlEscape="false" />

			</div>
		</div>

		<div class="control-group">
			<label class="control-label">商务部负责人:</label>
			<div class="controls">
				<form:input path="contactPerson" htmlEscape="false" />
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">负责人电话:</label>
			<div class="controls">
				<form:input path="contactPersonPhone" htmlEscape="false" />
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">补贴金额:</label>
			<div class="controls">
				<form:input path="subsidy" htmlEscape="false" />
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">擅长疾病:</label>
			<div class="controls">
				<form:textarea path="experience" htmlEscape="false" rows="1"  class="input-xxlarge"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">专业擅长:</label>--%>
			<%--<div class="controls">--%>
				<%--<form:textarea path="cardExperience" htmlEscape="false" rows="1"  class="input-xxlarge"/>--%>
				<%--<span class="help-inline"><font color="red">*</font> </span>--%>
			<%--</div>--%>

		<%--</div>--%>

		</div>
		<div class="control-group">
			<label class="control-label">个人简介:</label>
			<div class="controls">
				<form:textarea path="personDetails" htmlEscape="false" rows="3" class="input-xxlarge"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>

		</div>

		<div class="form-actions">
			<shiro:hasPermission name="sys:user:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>