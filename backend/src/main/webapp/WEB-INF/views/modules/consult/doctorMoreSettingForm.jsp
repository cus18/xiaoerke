<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>预约</title>
	<style type="text/css">
		.datesun LI {
			BORDER-RIGHT: #FFD1A4 1px solid;
			DISPLAY: block;
			FLOAT: left;
			HEIGHT: 25px;
			font-family:Arial, Helvetica, sans-serif;
			font-size:12px;
		}
		.datesun LI A {
			PADDING:1px 15px 0;
			DISPLAY: block;
			FONT-WEIGHT: none;
			COLOR: #562505;
			LINE-HEIGHT: 25px;
			TEXT-DECORATION: none;
		}
		.datesun LI A:hover {
			COLOR:#562505;
			BACKGROUND-COLOR: #FFD1A4;
			TEXT-DECORATION: none;
		}

		.locationsun LI {
			BORDER-RIGHT: #FFD1A4 1px solid;
			DISPLAY: block;
			FLOAT: left;
			HEIGHT: 25px;
			font-family:Arial, Helvetica, sans-serif;
			font-size:12px;
		}
		.locationsun LI A {
			PADDING:1px 15px 0;
			DISPLAY: block;
			FONT-WEIGHT: none;
			COLOR: #562505;
			LINE-HEIGHT: 25px;
			TEXT-DECORATION: none;
		}
		.locationsun LI A:hover {
			COLOR:#562505;
			BACKGROUND-COLOR: #FFD1A4;
			TEXT-DECORATION: none;
		}
		.current{
			BACKGROUND-COLOR: #FFD1A4;
		}
		.locationsun li#date{
			color:#FFD1A4;
			PADDING:2px 15px 0;
		}
	</style>
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
			$("#gender").val('${doctor.gender}');
			$("input[type='radio'][name=grabSession][value='${doctor.grabSession}']").attr("checked",true);
			$("input[type='radio'][name=sendMessage][value='${doctor.sendMessage}']").attr("checked",true);
			$("input[type='radio'][name=receiveDifferentialNotification][value='${doctor.receiveDifferentialNotification}']").attr("checked",true);
			document.getElementsByClassName("datesun")[0].getElementsByTagName("A")[0].className="current";
		});
		
		function savePersonalData(){
			if($("#hospital").val()==""){
				alertx("请填写医院！");
				return;
			}
			if($("#department").val()==""){
				alertx("请填写科室！");
				return;
			}
			$.ajax({
	             type: "post",
	             url: "${ctx}/consult/doctorInfoOper",
	             data: {userId:"${user.id}",gender:$("#gender").val(),title:$("#title").val(),hospital:$("#hospital").val(),department:$("#department").val(),skill:$("#skill").val(),description:$("#description").val()},
	             dataType: "json",
	             success: function(data){
	             	if("suc"==data.result){
						alertx("操作成功！");
             		}else{
	             		alertx("操作失败！");
	             	}
	             }
        	});
		}
		function changeDiv(obj,flag){
			var a=document.getElementsByClassName("datesun")[0].getElementsByTagName("A");
			for(var i=0;i<a.length;i++){
				a[i].className="";
			}
			obj.className="current";
			if(flag=='consultRecords'){
				$("#consultRecords").show();
				$("#personalData").hide();
				$("#permissionSettings").hide();
			}
			if(flag=='personalData'){
				$("#consultRecords").hide();
				$("#personalData").show();
				$("#permissionSettings").hide();
			}
			if(flag=='permissionSettings'){
				$("#consultRecords").hide();
				$("#personalData").hide();
				$("#permissionSettings").show();
			}
		}
		function savePermissionSettings(){
			$.ajax({
				type: "post",
				url: "${ctx}/consult/doctorInfoOper",
				data: {userId:"${user.id}",grabSession:$('input:radio[name="grabSession"]:checked').val(),sendMessage:$('input:radio[name="sendMessage"]:checked').val(),receiveDifferentialNotification:$('input:radio[name="receiveDifferentialNotification"]:checked').val()},
				dataType: "json",
				success: function(data){
					if("suc"==data.result){
						alertx("操作成功！");
					}else{
						alertx("操作失败！");
					}
				}
			});
		}
		function deleteDoctor(){
			confirmx("确认删除吗？",function(){
				$.ajax({
					type: "post",
					url: "${ctx}/consult/doctorOper",
					data: {id:"${user.id}",delFlag:'1'},
					dataType: "json",
					success: function(data){
						if("suc"==data.result){
							location.href="${ctx}/consult/consultDoctorList";
							alertx("删除成功！");
						}else{
							alertx("删除失败！");
						}
					}
				});
			})
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/consult/doctorOper" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<input type="text" value="${user.id}"/>
		<div class="control-group">
			<label class="control-label">姓名:</label>
			<div class="controls">
					${user.name}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"></label>
			<div class="controls">
				<ul class="datesun" id="datesun">
					<LI><A href="#" onclick="changeDiv(this,'consultRecords')">咨询记录</A></LI>
					<LI><A href="#" onclick="changeDiv(this,'personalData')">个人资料</A></LI>
					<LI><A href="#" onclick="changeDiv(this,'permissionSettings')">权限设置</A></LI>
				</ul>
			</div>
		</div>
	</form:form>
	<div id="consultRecords">
		<form:form id="inputForm" modelAttribute="user" action="${ctx}/consult/doctorOper" method="post" class="form-horizontal"><%--
			<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
			<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
			<input type="text" value="${user.id}"/>
			<div class="control-group">
				<label class="control-label">姓名:</label>
				<div class="controls">
						${user.name}
				</div>
			</div>
			<div class="form-actions" >
				<input class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
				<input class="btn btn-primary" type="button" onclick="deleteDoctor()" value="删除该医生"/>
			</div>
		</form:form>
	</div>
	<div id="personalData" style="display: none">
		<form:form id="inputForm" modelAttribute="doctor" action="${ctx}/consult/doctorOper" method="post" class="form-horizontal"><%--
			<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
			<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
			<input type="text" value="${doctor.id}"/>
			<div class="control-group">
				<label class="control-label">性别:</label>
				<div class="controls">
					<select id="gender" class="txt required" style="width:100px;">
						<option value="1">男</option>
						<option value="0">女</option>
					</select>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">医生职称:</label>
				<div class="controls">
					<input id="title" value="${doctor.title}" htmlEscape="false" maxlength="50" class="input-medium"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">医院:</label>
				<div class="controls">
					<input id="hospital" value="${doctor.hospital}" htmlEscape="false" maxlength="50" class="input-medium"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">科室:</label>
				<div class="controls">
					<input id="department" value="${doctor.department}" htmlEscape="false" maxlength="50" class="input-medium"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">擅长:</label>
				<div class="controls">
					<input id="skill" value="${doctor.skill}" htmlEscape="false" maxlength="50" class="input-medium"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">医生介绍:</label>
				<div class="controls">
					<input id="description" value="${doctor.description}" htmlEscape="false" maxlength="50" class="input-medium"/>
				</div>
			</div>
			<div class="form-actions" >
				<input class="btn btn-primary" type="button" onclick="savePersonalData()" value="确认"/>
				<input class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
				<input class="btn btn-primary" type="button" onclick="deleteDoctor()" value="删除该医生"/>
			</div>
		</form:form>
	</div>
	<div id="permissionSettings" style="display: none">
		<form:form id="inputForm" modelAttribute="doctor" action="${ctx}/consult/doctorOper" method="post" class="form-horizontal"><%--
			<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
			<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
			<input type="text" value="${doctor.id}"/>
			<div class="control-group">
				<label class="control-label">是否允许抢接会话:</label>
				<div class="controls">
					<input id="grabSessionyes" name="grabSession" value="1" type="radio" checked="checked"><label for="grabSessionyes">是</label>
					<input id="grabSessionno" name="grabSession" value="0" type="radio"><label for="grabSessionno">否</label>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">是否向用户发送评价消息:</label>
				<div class="controls">
					<input id="sendMessageyes" name="sendMessage" value="1" type="radio" checked="checked"><label for="sendMessageyes">是</label>
					<input id="sendMessageno" name="sendMessage" value="0" type="radio"><label for="sendMessageno">否</label>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">接收差评监控通知:</label>
				<div class="controls">
					<input id="receiveDifferentialNotificationyes" name="receiveDifferentialNotification" value="1" type="radio" checked="checked"><label for="receiveDifferentialNotificationyes">是</label>
					<input id="receiveDifferentialNotificationno" name="receiveDifferentialNotification" value="0" type="radio"><label for="receiveDifferentialNotificationno">否</label>
				</div>
			</div>
			<div class="form-actions" >
				<input class="btn btn-primary" type="button" onclick="savePermissionSettings()" value="保存"/>
				<input class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
				<input class="btn btn-primary" type="button" onclick="deleteDoctor()" value="删除该医生"/>
			</div>
		</form:form>
	</div>
</body>
</html>