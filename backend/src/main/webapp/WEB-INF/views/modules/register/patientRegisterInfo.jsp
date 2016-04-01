<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>预约详情</title>
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
			if(${UserReturnVisitVo.userFeedback}==1){
				document.getElementById("notSatisfy").checked=true;
			}else{
				document.getElementById("satisfy").checked=true;
			}
			if(${UserReturnVisitVo.isUser}==1){
				document.getElementById("isNotUser").checked=true;
				document.getElementById("isNotUserDiv").style.display="";
			}else{
				document.getElementById("isUser").checked=true;
			}
			var reasons = document.getElementsByName("reason");
			for(var i=0;i<reasons.length;i++){
				if(${UserReturnVisitVo.falseUserReason}!=null){
					if(reasons[i].value==${UserReturnVisitVo.falseUserReason}){
						reasons[i].checked=true;
					}
				}
			}
			document.getElementById("falseUserReasonRemarks").disabled=true;
		});
		
		function save(){
			var userRet = document.getElementsByName("userRet");
			var uret;
			for(var i=0;i<userRet.length;i++){
				if(userRet[i].checked){
					uret = userRet[i].value;
				}
			}
			
			var reasons = document.getElementsByName("reason");
			var result="";
			for(var i=0;i<reasons.length;i++){
				if(reasons[i].checked){
					result = reasons[i].value;
				}
			}
			
			var judgeUser = document.getElementsByName("judgeUser");
			var ju;
			for(var i=0;i<judgeUser.length;i++){
				if(judgeUser[i].checked){
					ju = judgeUser[i].value;
					if(ju==0){
						result="0";
						document.getElementById("falseUserReasonRemarks").value="";
					}
				}
			}
			var pageNo = document.getElementById("pageNo").value;
			var pageSize = document.getElementById("pageSize").value;
			$("#inputForm").attr("action","${ctx}/order/saveUserReturnVisit?patientRegisterId=${patientMap.id}&userFeedback="+uret+"&isUser="+ju+"&falseUserReason="+result+"&pageNo="+pageNo+"&pageSize="+pageSize);
			$("#inputForm").submit();
		}
		
		function hiddenDiv(){
			document.getElementById("isNotUserDiv").style.display="none";
		}
		function showDiv(){
			document.getElementById("isNotUserDiv").style.display="";
			document.getElementById("showResult").style.display="none";
		}
		function showResult(){
			var reasons = document.getElementsByName("reason");
			var result;
			for(var i=0;i<reasons.length;i++){
				if(reasons[i].checked){
					result = reasons[i].value;
				}
			}
			document.getElementById("isNotUserDiv").style.display="none";
			document.getElementById("showResult").style.display="";
			if(result!=null){
				if(result=="1"){
					result="未接电话";
				}else if(result=="2"){
					result="黄牛";
				}else if(result=="3"){
					result="怀疑占号";
				}else if(result=="4"){
					result=document.getElementById("falseUserReasonRemarks").value;
				}
				document.getElementById("showResult").innerText="原因说明："+result;
			}
		}
		function cancleAppointment(){
			confirmx("确认要取消预约吗？",function(){
				window.location.href="${ctx}/order/cancelAppointment?sysRegisterId=${registerMap.id}&patientId=${patientMap.id}";
			})
		}
		function closeFalseRemarks(){
			document.getElementById("falseUserReasonRemarks").disabled=true;
			document.getElementById("falseUserReasonRemarks").value="";
		}
		function openFalseRemarks(){
			document.getElementById("falseUserReasonRemarks").disabled=false;
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a><font color="#b8860b">回访信息</font></a></li>
		<li class="active"><a href="${ctx}/sys/doctor/doctorManage"><font color="#8a2be2">医生列表</font></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="UserReturnVisitVo" action="" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<input id="pageNo" name="pageNo" type="hidden" value="${pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${pageSize}"/>
		<form:input id="urvid" path="id" type="hidden" value="${UserReturnVisitVo.id}"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">手机号码:</label>
			<div class="controls">
				${patientMap.phone}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">宝贝姓名:</label>
			<div class="controls">
				${patientMap.babyName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">病情描述:</label>
			<div class="controls">
				${patientMap.illness}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">出生日期:</label>
			<div class="controls">
				${patientMap.birthday}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">下单时间:</label>
			<div class="controls">
				${patientMap.create_date}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">医生姓名:</label>
			<div class="controls">
				${registerMap.name}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">医生微信号:</label>
			<div class="controls">
				${registerMap.nickname}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开始时间:</label>
			<div class="controls">
				${registerMap.date}&nbsp;&nbsp;${registerMap.begin_time}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">价格:</label>
			<div class="controls">
				${registerMap.price}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">就医流程:</label>
			<div class="controls">
				${registerMap.medical_process}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">就医地址:</label>
			<div class="controls">
				${registerMap.hospitalName}${registerMap.location}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">路线:</label>
			<div class="controls">
				${registerMap.root}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">候诊时长:</label>
			<div class="controls">
				<form:input id="waitTime" path="waitTime" htmlEscape="false" maxlength="100"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">整体满意度:</label>
			<div class="controls">
				<form:select path="overallSatisfy" class="input-mini">
					<form:option value="0" label="是"/>
					<form:option value="1" label="否"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">用户反馈:</label>
			<div class="controls">
				<input id="satisfy" name="userRet" value="0" type="radio"><label for="satisfy">满意</label>
				<input id="notSatisfy" name="userRet" value="1" type="radio"><label for="notSatisfy">不满意</label><br/>
				<form:textarea path="userFeedbackRemarks" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">用户真假:</label>
			<div class="controls">
				<input id="isUser" name="judgeUser" type="radio" value="0" checked="checked" onclick="hiddenDiv()"><label for="isUser">真</label>
				<input id="isNotUser" name="judgeUser" type="radio" value="1" onclick="showDiv()"><label for="isNotUser">假</label><br/>
				<div id="isNotUserDiv" style="display: none;border:1px solid #DDDDDD;width: 300px;margin-top: 10px">
					<input id="noAnswer" name="reason" value="1" type="radio" onclick="closeFalseRemarks()" style="margin-left: 10px"><label for="noAnswer" style="margin-top: 5px">未接电话</label><br/>
					<input id="yellowCow" name="reason" value="2" type="radio" onclick="closeFalseRemarks()" style="margin-left: 10px"><label for="yellowCow" style="margin-top: 5px">黄牛</label><br/>
					<input id="occupy" name="reason" value="3" type="radio" onclick="closeFalseRemarks()" style="margin-left: 10px"><label for="occupy" style="margin-top: 5px">怀疑占号</label><br/>
					<input id="other" name="reason" value="4" type="radio" onclick="openFalseRemarks()" style="margin-left: 10px"><label for="other" style="margin-top: 5px">以上都不包括，可自行添加其他描述</label><br/>
					<form:textarea path="falseUserReasonRemarks" id="falseUserReasonRemarks" style="margin-left: 10px" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/><br/>
					<input id="btnSubmit" style="margin-left: 20px" class="btn btn-primary" type="button" onclick="showResult()" value="确定"/>
					<input id="btnSubmit" style="margin-left: 20px" class="btn btn-primary" type="button" onclick="hiddenDiv()" value="取消"/>
				</div>
				<label id="showResult" style="display: none"></label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">取消原因及方式:</label>
			<div class="controls">
				<form:input id="cancelReason" path="cancelReason" htmlEscape="false" maxlength="100"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="button" onclick="save()" value="保 存"/>
			<input id="btnSubmit" class="btn btn-primary" type="button" onclick="cancleAppointment()" value="取消预约"/>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>