<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>医院信息</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				//			$("#btnSubmit").click(function () {
				//
				//			});
				$("#inputForm").validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("输入有误，请先更正。");
										 if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent()); 
										} else {
											error.insertAfter(element);
										}
									}
								} 
						); 
			});
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/account/withdrawRecord">提现详情</a></li>
	</ul>
	<br />

	<form:form id="inputForm" modelAttribute="withdrawDetail"
		action="${ctx}/sys/account/withdrawSave" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		
		<div class="control-group">
			<label class="control-label">姓名:</label>
			<label class="control-label" style="text-align:left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${withdrawDetail.name}</label>
		</div>
		
		<div class="control-group">
			<label class="control-label">用户类型:</label>
			<label class="control-label" style="text-align:left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${withdrawDetail.userType}</label>
		</div>

		<div class="control-group">
			<label class="control-label">手机号:</label>
			<label class="control-label" style="text-align:left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${withdrawDetail.phone}</label>
		</div>
		<div class="control-group">
			<label class="control-label">openId:</label>
			<label class="control-label" style="text-align:left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${withdrawDetail.openId}</label>
		</div>
		
		<div class="control-group">
			<label class="control-label">绑定账户:</label>

			<div class="controls">
				<input id="accountBindingId" name="accountBindingId" type="text" value="${withdrawDetail.accountBindingId}"
					class="required" /> 
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">提现金额:</label>

			<div class="controls">
				<input id="moneyAmount" name="moneyAmount" type="text" value="${withdrawDetail.moneyAmount}"
					class="required" />
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">提现时间:</label>

			<div class="controls">
				<input id="createdDate" name="createdDate" type="text" value="${withdrawDetail.createdDate}"
					class="required" /> 
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">到账时间:</label>

			<div class="controls">
				<input id="receivedDate" name="receivedDate" type="text" value="${withdrawDetail.receivedDate}"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">状态:</label>
			<div class="controls">
			<form:select path="status">
					<c:forEach items="${statusMap}" var="sta">
				 		<form:option value="${sta.key}" label="${sta.value}"/>
				 	</c:forEach>
			</form:select>
			</div>
		</div>

		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit"
				value="保 存" />
		</div>
	</form:form>
</body>
</html>