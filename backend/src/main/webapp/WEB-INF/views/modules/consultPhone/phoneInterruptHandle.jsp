<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>电话中断处理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		//审核通过不通过弹窗
		function insuranceAudit(href,title){
			href=encodeURI(encodeURI(href));
			top.$.jBox.open('iframe:'+href,title,$(top.document).width()-860,$(top.document).height()-330,{
				buttons:{"关闭":true},
				closed: function () {
					//$("#searchForm").attr("action","${ctx}/member/memberList?");
					//$("#searchForm").submit();
				}
			});
		}
		function dialing(){
			if($("#surplusTime").val().trim()==''){
				alertx("请输入剩余时间！");
				return;
			}
			if($("#loginPhone").val().trim()==''){
				alertx("请输入用户电话！");
				return;
			}
			if($("#doctorAnswerPhone").val().trim()==''){
				alertx("请输入医生电话！");
				return;
			}
			alert($("#timingDial").attr("checked"));
			if($("#inputDailTime").val()==''&&$("#timingDial").attr("checked")){
				alertx("请输入拨打时间！");
				return;
			}

			$.ajax({
				type: "post",
				url: "${ctx}/consultPhone/timingDial",
				data: {dialType:$('input[name="dial"]:checked').val(),surplusTime:$("#surplusTime").val(),userPhone:$("#loginPhone").val(),doctorPhone:$("#doctorAnswerPhone").val(),dialDate:$("#inputDailTime").val()},
				dataType: "json",
				success: function(data){

				}
			});
		}
		function showTime(){
			$("#inputTime").show();
			$("#btnCancel").val("保存接通申请");
		}
		function hideTime(){
			$("#inputTime").hide();
			$("#btnCancel").val("确认点击拨打");
		}
	</script>
</head>
<body>
<ul class="nav nav-tabs">
	<li class="active"><a><font color="#b8860b">电话中断处理</font></a></li>
</ul><br/>
<form:form id="inputForm" modelAttribute="consultPhone" action="${ctx}/consultPhone/timingDial" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
	<form:input id="urvid" path="id" type="hidden" value="${vo.id}"/>
	<sys:message content="${message}"/>
	<div class="control-group">
		<label class="control-label">订单号:</label>
		<div class="controls">
				${map.register_no}
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">宝宝姓名:</label>
		<div class="controls">
				${map.babyName}
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">剩余时长:</label>
		<div class="controls">
				${map.surplusTime}
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">咨询时刻:</label>
		<div class="controls">
				${map.date}&nbsp;${map.beginTime}<br/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">预约类型:</label>
		<div class="controls">
				${map.price}元/${map.type}min
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">${map.doctorName}最近被预约详情</label>
		<div class="controls">
			<c:forEach items="${map.orderTimeMap}" var="orderTime">
				${fn:substring(orderTime.key, 5, 10)}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ${orderTime.value}<br/>
			</c:forEach>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label"></label>
		<div class="controls">
			<font color="blue">手动接通设置</font><br/>
			设置时长&nbsp;&nbsp;&nbsp;&nbsp;用户手机号码&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${map.doctorName}电话<br/>
			<input id="surplusTime" htmlEscape="false" maxlength="50" style="width: 50px" class="required" value="${map.surplusTime}"/>
			<input id="loginPhone" htmlEscape="false" maxlength="50" style="width: 150px" class="required" value="${map.loginPhone}"/>
			<input id="doctorAnswerPhone" htmlEscape="false" maxlength="50" style="width: 150px" class="required" value="${map.doctor_answer_phone}"/><br/>
			<hr>设定接通时间
			<input id="immediatelyDial" name="dial" value="immediatelyDial" type="radio" checked="checked" onclick="hideTime()"><label for="immediatelyDial">立即拨打</label>
			<input id="timingDial" name="dial" value="timingDial" type="radio" onclick="showTime()"><label for="timingDial">定时拨打</label><hr>
			<div id="inputTime" style="display: none">拨打时间:<input id="inputDailTime" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false});"/><br/>
			注意：医生被预约的时间段不能设置定时拨打<br/>
			</div>
			<input id="btnCancel" class="btn" type="button" value="确认点击拨打" onclick="dialing()"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label"></label>
		<div class="controls">
			<font color="blue">手动接通记录</font><br/>
			设置时长&nbsp;&nbsp;&nbsp;&nbsp;用户手机号码&nbsp;&nbsp;&nbsp;&nbsp;医生电话&nbsp;&nbsp;&nbsp;&nbsp;拨打时间&nbsp;&nbsp;&nbsp;&nbsp;接通方式&nbsp;&nbsp;&nbsp;&nbsp;操作用户<br/>
		</div>
	</div>
	<div class="form-actions">
		<c:if test="${irsvo.state eq '2'}">
			<input id="btnSubmit" class="btn btn-primary" type="button" onclick="insuranceAudit('${ctx}/insurance/auditForm?id=${irsvo.id}&nickName=${irsvo.nickName}&parentId=${irsvo.parentId}&state=3','审核通过')" value="审核通过"></input>
			<input id="btnSubmit" class="btn btn-primary" type="button" onclick="insuranceAudit('${ctx}/insurance/auditForm?id=${irsvo.id}&nickName=${irsvo.nickName}&parentId=${irsvo.parentId}&state=5','审核不通过')" value="审核不通过"/>
		</c:if>
		<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
	</div>
</form:form>
</body>
</html>