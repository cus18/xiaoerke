<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>预约</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			document.getElementById("otherReasonRemarks").disabled=true;
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
		});
		var reason;
		var keep="0";
		function hiddenDiv(){
			document.getElementById("withoutReserveDiv").style.display="none";
		}
		function showDiv(){
			document.getElementById("withoutReserveDiv").style.display="";
			document.getElementById("showResult").style.display="none";
		}
		function closeFalseRemarks(){
			document.getElementById("otherReasonRemarks").disabled=true;
			document.getElementById("otherReasonRemarks").value="";
		}
		function openFalseRemarks(){
			document.getElementById("otherReasonRemarks").disabled=false;
		}
		function cancelOrder(){
			document.getElementById("btnSubmit").disabled=true;
			if(document.getElementById("withoutReserve").checked==true){
				if(document.getElementById("stopService").checked==false&&document.getElementById("otherReason").checked==false){
					alert("请选择原因！");
					return;
				}
			}
			if(document.getElementById("stopService").checked==true){
				reason="医生停诊";
				keep="1";
			}else if(document.getElementById("otherReason").checked==true){
				reason=document.getElementById("otherReasonRemarks").value;
				keep="1";
			}
			$.ajax({
	             type: "post",
	             url: "${ctx}/order/cancelAppointment?",
	             data: {sysRegisterServiceId:"${registerId}",registerNo:"${registerNo}",patientRegisterId:"${patientRegisterId}",memberServiceId:"${memberServiceId}",reason:reason,keep:keep},
	             dataType: "json",
	             success: function(data){
	             	top.$.jBox.close(true);
	             }
        	});
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="patientRegisterServiceVo" action="${ctx}/order/saveAppointment?sysRegisterServiceId=${registerId}" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<div class="control-group">
			<label class="control-label">订单号:</label>
			<div class="controls">
				${registerNo}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">免费预约机会:</label>
			<div class="controls">
				<input id="reserve" name="judgeReserve" type="radio" value="0" checked="checked" onclick="hiddenDiv()"><label for="reserve">不保留</label>
				<input id="withoutReserve" name="judgeReserve" type="radio" value="1" onclick="showDiv()"><label for="withoutReserve">保留</label><br/>
				<div id="withoutReserveDiv" style="display: none;border:1px solid #DDDDDD;width: 300px;margin-top: 10px">
					<input id="stopService" name="reason" value="1" type="radio" onclick="closeFalseRemarks()" style="margin-left: 10px"><label for="stopService" style="margin-top: 5px">医生停诊</label><br/>
					<input id="otherReason" name="reason" value="2" type="radio" onclick="openFalseRemarks()" style="margin-left: 10px"><label for="otherReason" style="margin-top: 5px">其他原因</label><br/>
					<input id="otherReasonRemarks" type="text" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/><br/>
				</div>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="button" onclick="cancelOrder()" value="取消预约"/>
		</div>
	</form:form>
</body>
</html>