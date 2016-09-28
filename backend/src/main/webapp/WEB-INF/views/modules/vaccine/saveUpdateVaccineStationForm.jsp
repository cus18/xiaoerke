<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>疫苗站信息录入</title>
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
			$("#saveVaccineStation").click(function(){
				$("#inputForm").attr("action","${ctx}/vaccine/saveVaccineStationInfo?relid="+relid);
				$("#inputForm").submit();
			});
			$("#deleteVaccineStation").click(function(){
				confirmx("确认删除吗？",function(){
					$("#inputForm").attr("action","${ctx}/vaccine/deleteVaccineStation?id=${vo.id}");
					$("#inputForm").submit();
				})
			});
			$("#addVaccine").click(function(){
				$("#contentTable").append("<tr><td><select name='vaccine' onchange='fillblank()'><c:forEach items='${vaccineMap}' var='sta'><option value='${sta.key}' label='${sta.value}'/></c:forEach>	</select></td><td id='vaccineId'></td><td></td>	<td><select name='nextvaccine'><c:forEach items='${vaccineMap}' var='sta'><option value='${sta.key}' label='${sta.value}'/></c:forEach>	</select></td>	<td></td><td></td>	</tr>");
			});
			var workDate = '${vo.workDate}';
			$(workDate.split(",")).each(function (i,day){
				$(":checkbox[id='"+day+"']").attr("checked",true);
			});
		});
		var relid = "";
		function deleteVaccine(obj,id){
			var tr=obj.parentNode.parentNode;
			var tbody=tr.parentNode;
			tbody.removeChild(tr);
			relid = relid+id+",";
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="vo" action="${ctx}/vaccine/saveVaccineStationInfo?" method="post" class="form-horizontal">
		<form:hidden path="id" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<div class="control-group">
			<label class="control-label">疫苗站名称:</label>
			<div class="controls">
				<form:input id="name" path="name" htmlEscape="false" maxlength="50"  class="required" value="${vo.name}"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<c:if test="${not empty vo.id}">
			<div class="control-group">
				<label class="control-label">疫苗站编号:</label>
				<div class="controls">
					${vo.id}
				</div>
			</div>
		</c:if>
		<div class="control-group">
			<label class="control-label">联系人:</label>
			<div class="controls">
				<form:input id="contactName" path="contactName" htmlEscape="false" maxlength="50"  class="required" value="${vo.contactName}"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">联系电话:</label>
			<div class="controls">
				<form:input id="contactPhone" path="contactPhone" htmlEscape="false" maxlength="50"  class="required" value="${vo.contactPhone}"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">地址:</label>
			<div class="controls">
				<form:input id="address" path="address" htmlEscape="false" maxlength="50"  class="required" value="${vo.address}"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">接种工作日:</label>
			<div class="controls">
				<input id="1" name="workDateList" type="checkbox" value="1">
				<label for="1"><span>星期一</span></label>
				<input id="2" name="workDateList" type="checkbox" value="2">
				<label for="2"><span>星期二</span></label>
				<input id="3" name="workDateList" type="checkbox" value="3">
				<label for="3"><span>星期三</span></label>
				<input id="4" name="workDateList" type="checkbox" value="4">
				<label for="4"><span>星期四</span></label>
				<input id="5" name="workDateList" type="checkbox" value="5">
				<label for="5"><span>星期五</span></label>
				<input id="6" name="workDateList" type="checkbox" value="6">
				<label for="6"><span>星期六</span></label>
				<input id="7" name="workDateList" type="checkbox" value="7">
				<label for="7"><span>星期天</span></label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">接种程序:</label>
			<div class="controls">
				<input type="button" id="addVaccine" value="添加疫苗"/>
				<table id="contentTable" class="table table-bordered table-condensed">
					<thead>
					<tr>
						<th>疫苗名称</th>
						<th>疫苗编号</th>
						<th>接种最小年龄</th>
						<th>后置疫苗</th>
						<th>周期</th>
						<th>操作</th>
					</tr>
					</thead>
					<tbody>
						<c:forEach items="${relList}" var="rel">
							<tr>
								<td>
									${rel.vaccineName}
								</td>
								<td>
									${rel.vaccineId}
								</td>
								<td>
									${rel.miniumAge}
								</td>
								<td>
									${rel.willVaccineName}
								</td>
								<td>
									${rel.vaccineId}
								</td>
								<td>
									<span onclick="deleteVaccine(this,'${rel.id}')">删除</span>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>

		<div class="form-actions">
			<input id="saveVaccineStation" class="btn btn-primary" type="button" value="保存"/>
			<input id="deleteVaccineStation" class="btn btn-primary" type="button" value="删除"/>
			<input id="btnCancel" class="btn" type="button" value="返回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>