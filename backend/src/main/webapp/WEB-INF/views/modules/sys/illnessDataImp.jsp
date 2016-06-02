<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>疾病信息</title>
	<meta name="decorator" content="default"/>
		<script type="text/javascript">

			$("#level_2").focus();
			$(document).ready(function() {

				$("#btnSubmit").click(function () {
					var level_2 = $("#level_2").val();
					if (level_2 == "") {
						alertx("二类疾病不能为空！");
						return false;
					}

				})

				$("#inputForm").validate({
					<%--rules: {--%>
						<%--level_1: {remote:{--%>
							<%--url: "${ctx}/sys/illness/checkIllness",     //后台处理程序--%>
							<%--type: "get",               //数据发送方式--%>
							<%--dataType: "json",           //接受数据格式--%>
							<%--data: {                     //要传递的数据--%>
								<%--level_2: function() {--%>
									<%--return $("#level_2").val();--%>
								<%--}--%>
							<%--} }--%>
						<%--&lt;%&ndash;level_1: {remote: "${ctx}/sys/illness/checkIllness?level_1="+encodeURIComponent('${illnessVo.level_1}')}&ndash;%&gt;--%>

						<%--}},--%>
					<%--messages: {--%>
						<%--level_1: {remote: "该疾病已存在！请勿重复添加-------"}--%>

					<%--},--%>
					submitHandler: function(form){
						loading('正在提交，请稍等...');
						form.submit();
					},
					errorContainer: "#messageBox",
					errorPlacement: function (error, element) {
//						$("#messageBox").text("输入有误，请先更正。");
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
		<li class="active"><a href="${ctx}/sys/illness/illnessDataImp">疾病信息录入</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="illnessVo" action="${ctx}/sys/illness/illnessDataImp" method="post" class="form-horizontal">
		<%--<form:hidden path="id"/>--%>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">一类疾病:</label>
			<div class="controls">
				<form:select path="level_1" class="input-medium">
					<form:options items="${fns:getDictList('illness_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline">没有？请联系管理员</span>
			</div>
		</div>

		<%--<div class="control-group">--%>
			<%--<label class="control-label">一类疾病:</label>--%>
			<%--<div class="controls">--%>
				<%--<input id="level_1" name="level_1" type="text" value="${illnessVo.level_1}" maxlength="50" minlength="3" class="required" equalTo="#newPassword"/>--%>
				<%--<span class="help-inline"><font color="red">找不到？联系管理员哦~</font> </span>--%>
			<%--</div>--%>
		<%--</div>--%>

		<%--<div class="control-group">--%>
			<%--<label class="control-label">一类疾病:</label>--%>
			<%--<div class="controls">--%>
				<%--<sys:treeselect  id="level_1" name="level_1" value="${illnessVo.level_1}" labelName="level_1" labelValue="${illnessVo.level_1}"--%>
								 <%--title="一类疾病" url="/sys/hospital/treeHospitalData" cssClass="required"/>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%----%>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">二类疾病</label>--%>
			<%--<div class="controls">--%>
				<%--<sys:treeselect  id="level_2" name="level_2" value="${doctorIllnessRelationVo.level_2}" labelName="level_2" labelValue="${doctorIllnessRelationVo.level_2}"--%>
								 <%--title="二级疾病" url="/sys/doctorIllnessRelation/treeLevel_2Data" cssClass="required"/>--%>
			<%--</div>--%>
		<%--</div>--%>
		<div class="control-group">
			<label class="control-label">二类疾病:</label>
			<div class="controls">
				<input id="level_2" name="level_2" type="text" value="${illnessVo.level_2}" class="required" />
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">是否在客户端显示:</label>
			<div class="controls">
				<input id="isDisplay" name="isDisplay" type="text" value="${illnessVo.isDisplay}" class="required"/>
				<span class="help-inline"><font color="red">*</font>显示为 1，隐藏请填写 0 </span>
			</div>
		</div>

		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
		</div>
	</form:form>
</body>
</html>