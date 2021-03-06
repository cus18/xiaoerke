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
			if($("#inputDailTime").val()==''&&$("#timingDial").attr("checked")){
				alertx("请输入拨打时间！");
				return;
			}
			$.ajax({
				type: "post",
				url: "${ctx}/consultPhone/timingDial",
				data: {dialType:$('input[name="dial"]:checked').val(),surplusTimeStr:$("#surplusTime").val(),userPhone:$("#loginPhone").val(),doctorPhone:$("#doctorAnswerPhone").val(),dialDate:$("#inputDailTime").val(),orderId:${map.id}},
				dataType: "json",
				success: function(data){
					if(data.result==undefined){
						alertx("操作成功！");
					}else{
						alertx(data.result);
					}
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
	<input id="orderId" value="${map.id}"/>
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
			<fmt:formatDate value ="${map.surplusTime}" pattern="mm:ss" />
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
			<table>
				<tr>
					<th>设置时长</th>
					<th>用户手机号码</th>
					<th>${map.doctorName}电话</th>
				</tr>
				<tr>
					<td>
						<input id="surplusTime" type="text" readonly="readonly" maxlength="10" class="input-small Wdate"
									onclick="WdatePicker({dateFmt:'mm:ss',isShowClear:false});" value="<fmt:formatDate value ="${map.surplusTime}" pattern="mm:ss" />"/>
					</td>
					<td>
						<input id="loginPhone" htmlEscape="false" maxlength="50" style="width: 150px" class="required" value="${map.phoneNum}"/>
					</td>
					<td>
						<input id="doctorAnswerPhone" htmlEscape="false" maxlength="50" style="width: 150px" class="required" value="${map.doctor_answer_phone}"/>
					</td>
				</tr>
			</table>
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
			<table border="1px">
				<tr>
					<th>设置时长</th>
					<th>用户手机号码</th>
					<th>医生电话</th>
					<th>拨打时间</th>
					<th>接通方式</th>
					<th>操作用户</th>
				</tr>
				<c:forEach items="${map.recordList}" var="record">
					<tr>
						<td><fmt:formatDate value ="${record.surplusDate}" pattern="mm:ss" /></td>
						<td>${record.userPhone}</td>
						<td>${record.doctorPhone}</td>
						<td><fmt:formatDate value ="${record.dialDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<td>
							<c:if test="${record.dialType eq 'immediatelyDial'}">立即拨打</c:if>
							<c:if test="${record.dialType eq 'timingDial'}">定时拨打</c:if>
						</td>
						<td>${record.operBy}</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
	<div class="form-actions">
		<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
	</div>
</form:form>
</body>
</html>