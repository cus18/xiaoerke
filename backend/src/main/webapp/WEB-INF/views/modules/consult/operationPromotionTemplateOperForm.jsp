<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>预约</title>
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
			$("#userType").val('${user.userType}');
			$("#info1 option[value='${vo.info1}']").attr("selected", true);
		});
		
		function templateOper(type){
			$("#btnSubmit").attr("disabled",true);
			alert($("#confContent").val());
			if('consultPay'==type){
				$.ajax({
					type: "post",
					url: "${ctx}/operationPromotion/operationPromotionTemplateOper",
					data: {id:"${vo.id}",info1:$("#info1").val(),info2:$("#confContent").val(),info3:$("#info3").val(),info4:$("#info4").val(),info5:$("#info5").val(),info6:$("#info6").val(),type:"consultPay"},
					dataType: "json",
					success: function(data){
						if("suc"==data.result){
							top.$.jBox.close(true);
						}else{
							alertx("操作失败！");
						}
					}
				});
			}else{
				if($("#info11").val()==''||$("#info12").val()==''){
					alertx("请填写坐标！");
					return;
				}
				$.ajax({
					type: "post",
					url: "${ctx}/operationPromotion/operationPromotionTemplateOper",
					data: {id:"${vo.id}",info11:$("#info11").val(),info12:$("#info12").val(),info21:$("#info21").val(),info22:$("#info22").val(),image:$("#image").val(),type:"pictureTransmission"},
					dataType: "json",
					success: function(data){
						if("suc"==data.result){
							top.$.jBox.close(true);
						}else{
							alertx("操作失败！");
						}
					}
				});
			}
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="vo" action="${ctx}/operationPromotion/operationPromotionTemplateOper1" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<form:input type="hidden" path="id" value="${vo.id}"/>
		<form:input type="hidden" path="type" value="${vo.type}"/>
		<c:if test="${vo.type eq 'pictureTransmission'}">
			<div class="control-group">
				<label class="control-label">名字坐标1:</label>
				<div class="controls">
					<input id="info11" value="${fn:split(vo.info1,',')[0]}" htmlEscape="false" maxlength="50" style="width: 60px;" class="input-medium"/>，
					<input id="info12" value="${fn:split(vo.info1,',')[1]}" htmlEscape="false" maxlength="50" style="width: 60px;" class="input-medium"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">名字坐标2:</label>
				<div class="controls">
					<input id="info21" value="${fn:split(vo.info2,',')[0]}" htmlEscape="false" maxlength="50" style="width: 60px;" class="input-medium"/>，
					<input id="info22" value="${fn:split(vo.info2,',')[1]}" htmlEscape="false" maxlength="50" style="width: 60px;" class="input-medium"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">缩略图:</label>
				<div class="controls">
					<input type="hidden" id="image" name="image" value="${vo.image}" />
					<sys:ckfinder input="image" type="thumb" uploadPath="/cms/article" selectMultiple="false"/>
				</div>
			</div>
		</c:if>
		<c:if test="${vo.type eq 'consultPay'}">
			<div class="control-group">
				<label class="control-label">时间:</label>
				<div class="controls">
					<form:input id="info3" path="info3s" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
					至
					<form:input id="info4" path="info4s" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">时段场景:</label>
				<div class="controls">
					<form:select path="info1" name="info1" id="info1">
						<option value="收费">收费</option>
						<option value="不收费">不收费</option>
					</form:select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">开始时间:</label>
				<div class="controls">
					<form:input id="info5" path="info5s" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
								onclick="WdatePicker({dateFmt:'HH:mm:ss',isShowClear:false});"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">结束时间:</label>
				<div class="controls">
					<form:input id="info6" path="info6s" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
								onclick="WdatePicker({dateFmt:'HH:mm:ss',isShowClear:false});" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">文案:</label>
				<div class="controls">
					<form:textarea path="info2" id="confContent" name="confContent" style="width: 200px ;height: 80px" value="${vo.info2}"></form:textarea>
					<sys:ckeditor replace="confContent"/>
				</div>
			</div>
		</c:if>
		<div class="form-actions" >
			<c:if test="${vo.type eq 'consultPay'}">
				<input id="btnSubmit" class="btn btn-primary" type="submit"  value="确认"/>
			</c:if>
			<c:if test="${vo.type eq 'pictureTransmission'}">
				<input id="btnSubmit" class="btn btn-primary" type="button" onclick="templateOper('${vo.type}')" value="确认"/>
			</c:if>
		</div>
	</form:form>
</body>
</html>