<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>添加修改配置页面</title>
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
			if('${vo.priority}' == '0' || '${vo.priority}' == ''){
				$("#startTime").attr("value",'00:00');
				$("#endTime").attr("value",'24:00');
				$("#startTime").attr("disabled",true);
				$("#endTime").attr("disabled",true);
				$('#week').attr('checked',true).attr('disabled','disabled');
				for(var i=1;i<8;i++){
					$('#week'+i).attr('disabled','disabled');
				}
			}else{
				var week = '${vo.week}';
				var weeks = week.split(",");
				for(var temp in weeks){
					alert(weeks[temp]);
					$('#week'+weeks[temp]).attr('checked',true);
				}
			}

		});

		function priorityChange(){
			if($("#priority").val()==0){
				$("#startTime").attr("value",'00:00');
				$("#endTime").attr("value",'24:00');
				$("#startTime").attr("disabled",true);
				$("#endTime").attr("disabled",true);
				$('#week').attr('checked',true).attr('disabled','disabled');
				for(var i=1;i<8;i++){
					$('#week'+i).attr('disabled','disabled');
					$('#week'+i).attr('checked','');
				}
			}else{
				$("#startTime").attr("value",'');
				$("#endTime").attr("value",'');
				$("#startTime").attr("disabled",false);
				$("#endTime").attr("disabled",false);
				$('#week').attr('checked',false).attr('disabled','');
				for(var i=1;i<8;i++){
					$('#week'+i).attr('disabled','');
				}
			}
		}
		function kick(){
			if($("#week").attr("checked")){
				for(var i=1;i<8;i++){
					$('#week'+i).attr('disabled','disabled');
					$('#week'+i).attr('checked','');
				}
			}else{
				for(var i=1;i<8;i++){
					$('#week'+i).attr('disabled','');
				}
			}
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="vo" action="${ctx}/messageContentConf/saveMessageContentConf?" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<form:hidden path="id" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<div class="control-group">
			<label class="control-label">应用场景:</label>
			<div class="controls">
				<c:if test="${not empty vo.id}">
					${vo.scene}
				</c:if>
				<c:if test="${empty vo.id}">
					<form:select path="scene" name="scene" id="scene" onchange="priorityChange()">
						<option value="咨询关闭评价">咨询关闭评价</option>
						<option value="送心意成功">送心意成功</option>
					</form:select>
					<span class="help-inline"><font color="red">*</font> </span>
				</c:if>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">优先级:</label>
			<div class="controls">
				<c:if test="${not empty vo.priority}">
					${vo.priority}
				</c:if>
				<c:if test="${empty vo.priority}">
					<form:select path="priority" name="priority" id="priority" onchange="priorityChange()">
						<option value="0">0</option>
						<option value="1">1</option>
						<option value="2">2</option>
						<option value="3">3</option>
					</form:select>
					<span class="help-inline"><font color="red" size="3px">*每个场景没有0优先级就不能添加其他优先级，0优先级只能修改内容，不能删除</font> </span>
				</c:if>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">日期:</label>
			<div class="controls">
				<input id="week" name="weekList" class="" type="checkbox" value="0" onclick="kick()">
				<label for="week">每天</label>
				<input id="week1" name="weekList" class="" type="checkbox" value="1" onclick="kick()">
				<label for="week1">周一</label>
				<input id="week2" name="weekList" class="" type="checkbox" value="2" onclick="kick()">
				<label for="week2">周二</label>
				<input id="week3" name="weekList" class="" type="checkbox" value="3" onclick="kick()">
				<label for="week3">周三</label>
				<input id="week4" name="weekList" class="" type="checkbox" value="4" onclick="kick()">
				<label for="week4">周四</label>
				<input id="week5" name="weekList" class="" type="checkbox" value="5" onclick="kick()">
				<label for="week5">周五</label>
				<input id="week6" name="weekList" class="" type="checkbox" value="6" onclick="kick()">
				<label for="week6">周六</label>
				<input id="week7" name="weekList" class="" type="checkbox" value="7" onclick="kick()">
				<label for="week7">周日</label>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开始时间:</label>
			<div class="controls">
				<form:input id="startTime" path="startTime" type="text" readonly="readonly" maxlength="20" class="input-small Wdate required"
							onclick="WdatePicker({dateFmt:'HH:mm:ss',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开始时间:</label>
			<div class="controls">
				<form:input id="endTime" path="endTime" type="text" readonly="readonly" maxlength="20" class="input-small Wdate required"
							onclick="WdatePicker({dateFmt:'HH:mm:ss',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">内容:</label>
			<div class="controls">
				<form:textarea id="confContent" htmlEscape="true" path="content" rows="4" maxlength="200" class="input-xxlarge"/>
				<sys:ckeditor replace="confContent"/>
			</div>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>

		<div class="form-actions">
			<input id="saveConf" class="btn btn-primary" type="submit" value="保存"/>
			<input id="btnCancel" class="btn" type="button" value="返回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>