<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>医生列表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function () {

			$("#imgSubmit").click(function () {
				var file = $("#file").val();
				if (file == "") {
					alertx("请添加医生头像！");
					return false;
				}
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
		<li class="active"><a href="${ctx}/sys/doctor/doctorManage"><font color="#006400">医生列表</font></a></li>
		<li class="active"><a href="${ctx}/sys/doctor/doctorEdit?id=${doctorVo.id}"><font color="#8b0000">医生<shiro:hasPermission name="sys:user:edit">${not empty doctorVo.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:user:edit">查看</font></shiro:lacksPermission></a></li>
	</ul><br/>

	<form:form id="imgForm" modelAttribute="doctorVo"  class="form-horizontal">
		<div class="control-group">
			<label class="control-label">头像:</label>
			<div class="controls">
				<img src="http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/${doctorVo.id}" class="img-rounded" />
			</div>
		</div>
	</form:form>

	<div style="width: 600px;">
		<div class="accordion-group">
				<div >
				  <a  data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<div align="right">上传头像</div>
				  </a>
				</div>
				<div id="collapseTwo" class="accordion-body collapse">
				  <div class="accordion-inner">
					  <form:form id="fileForm" modelAttribute="doctorVo" enctype="multipart/form-data" action="${ctx}/sys/doctor/handleFileUpload" method="post" class="form-horizontal">
						  <div class="control-group">
							  <div class="controls">
								  <input id="file" type="file" name="files" class="btn btn-small" style="" value=" ">&nbsp;&nbsp;&nbsp;
								  <input type="submit" id="imgSubmit" class="btn btn-success" value="上传">
							  </div>
						  </div>
						  <form:hidden path="id"/>
					  </form:form>
				  </div>
				</div>
		</div>
    </div>


	<form:form id="inputForm" modelAttribute="doctorVo" action="${ctx}/sys/doctor/Save" method="post" class="form-horizontal">
		<form:hidden path="sysUserId"/>
		<form:hidden path="id"/>
		<form:hidden path="hospital"/>
		<sys:message content="${message}"/>

		<div class="control-group">
			<label class="control-label">姓名:</label>
			<div class="controls">
				<form:input path="doctorName" htmlEscape="false"   class="input-small" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">从业时间:</label>
				<div class="controls">
					<form:input path="careerTimeForDisplay" htmlEscape="false" class="required"/>
				</div>
				<%--<input id="careerTime" name="careerTime" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"--%>
					   <%--value="<fmt:formatDate value="${link.weightDate}" pattern="yyyy-MM-dd"/>"--%>
					   <%--onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>--%>
				<%--<span class="help-inline">数值越大排序越靠前，过期时间可为空，过期后取消置顶。</span>--%>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">所属医院:</label>
			<div class="controls">
				&nbsp;
				<a href="${ctx}/sys/doctor/doctorHospitalRelation?doctorId=${doctorVo.id}&hospitalName=${doctorVo.hospital}">${doctorVo.hospital}</a>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<c:if test="${empty doctorVo.hospital}">
					<a href="${ctx}/sys/doctor/doctorHospitalRelationDataImp?phone=${doctorVo.phone}&flag=1&doctorName=${doctorVo.doctorName}">添加</a>
				</c:if>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">其他出诊医院:</label>
			<%--<div class="controls">--%>
                <%--<sys:treeselect  id="hospital" name="doctorVo.hospital" value="${doctorVo.hospital}" labelName="hospital" labelValue="${doctorVo.hospital}"--%>
					<%--title="医院" url="/sys/hospital/treeHospitalData" cssClass="required"/>--%>
			<%--</div>--%>
			<c:forEach items="${doctorVo.hospitalList}" var="hospitalList">
				<tr >
					<td style="font-size:20px">
						&nbsp;&nbsp;&nbsp;
						<a href="${ctx}/sys/doctor/doctorHospitalRelation?doctorId=${doctorVo.id}&hospitalName=${hospitalList} ">&nbsp;&nbsp;&nbsp;<c:out value="${hospitalList}"/></a>
					</td>
					<%--hospitalList=hospitalList&doctorId=--%>
				</tr>
			</c:forEach>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="${ctx}/sys/doctor/doctorHospitalRelationDataImp?phone=${doctorVo.phone}&flag=1&doctorName=${doctorVo.doctorName}">添加</a>
		</div>

		<div class="control-group">
			<label class="control-label">关联疾病 : </label>
			<a href="${ctx}/sys/doctorIllnessRelation/DoctorIllnessRelation?doctorId=${doctorVo.id}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;查看关联的疾病</a>
		</div>

		<div class="control-group">
			<label class="control-label">一级职务:</label>
			<div class="controls">
				<form:input path="position1" htmlEscape="false"  />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">二级职务:</label>
			<div class="controls">
				<form:input path="position2" htmlEscape="false"  />

			</div>
		</div>

		<div class="control-group">
			<label class="control-label">医生电话:</label>
			<div class="controls">
				<form:input path="phone" htmlEscape="false" />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">补贴金额:</label>
			<div class="controls">
				<form:input path="subsidy" htmlEscape="false" />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">擅长疾病:</label>
			<div class="controls">
				<form:textarea path="experience" htmlEscape="false" rows="1"  class="input-xxlarge"/>
			</div>
		</div>

		</div>
		<div class="control-group">
			<label class="control-label">个人简介:</label>
			<div class="controls">
				<form:textarea path="personDetails" htmlEscape="false" rows="3"  class="input-xxlarge"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">是否在客户端显示:</label>
			<div class="controls">
				<form:input path="isDisplay" htmlEscape="false" /><span class="help-inline">显示为 display，隐藏请填写 hidden </span>
			</div>
		</div>


		<div class="form-actions">
			<shiro:hasPermission name="sys:user:edit">
			<div  style="width: 600px;">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="取 消" onclick="history.go(-1)"/>
				<input id="btnDelete" class="btn btn-danger" type="button" value="删 除" onclick="return confirmx('确认要删除该医生吗？','${ctx}/sys/doctor/doctorDelete?id=${doctorVo.id} ')"/>
			</div>
		</div>
	</form:form>
</body>
</html>