<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订单列表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var orderCount;
		var timer;
		$(document).ready(function() {
			document.getElementById("newOrder").disabled=true;
			timer = window.setInterval(getNewOrderCount,300000);
			$.ajax({
				type: "post",
				url: "${ctx}/consultPhone/getNewOrderCount?",
				data: {state:4},
				dataType: "json",
				success: function(data){
					orderCount = data.orderCount;
				}
			});
		});
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/consultPhone/consultPhoneOrderList?stateFlag=remove");
			$("#searchForm").submit();
			return false;
		}
		function getNewOrderCount(){
			$.ajax({
				type: "post",
				url: "${ctx}/consultPhone/getNewOrderCount?",
				data: {state:4},
				dataType: "json",
				success: function(data){
					if(data.orderCount!=orderCount){
						document.getElementById("newOrder").disabled=false;
						document.getElementById("newOrder").style.backgroundColor="#FF6666";
						clearInterval(timer);
					}
				}
			});
		}
		function refreshNewOrder(){
			timer = window.setInterval(getNewOrderCount,300000);
			window.location.href = "${ctx}/consultPhone/consultPhoneOrderList?stateFlag=remove";
		}
	</script>
</head>
<body>
<ul class="nav nav-tabs">
	<li><a href="${ctx}/consultPhone/consultPhoneOrderList?">订单列表</a></li>
	<li class="active"><a href="">已删除订单列表</a></li>
</ul>
<form:form id="searchForm" modelAttribute="consultPhone" action="${ctx}/consultPhone/orderList?" method="post" class="breadcrumb form-search ">
	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
	<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	<sys:message content="${message}"/>
	<ul class="ul-form">
		<li>
			<input id="newOrder" type="button" onclick="refreshNewOrder();" value="刷新最新订单" />
		</li>
	</ul>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-bordered table-condensed">
	<thead>
	<tr>
		<th>订单号</th>
		<th>微信名</th>
		<th>下单时间</th>
		<th>登录手机号</th>
		<th>接听手机号</th>
		<th>宝宝姓名</th>
		<th>预约医生</th>
		<th>咨询时刻</th>
		<th>预约类型</th>
		<th>剩余时长</th>
		<th>订单状态</th>
		<th>支付状态</th>
		<th>最后操作时间</th>
		<th>删除人</th>
	</tr>
	</thead>
	<tbody>
	<c:forEach items="${page.list}" var="consultPhone">
		<tr>
			<td>${consultPhone.registerNo}</td>
			<td>${consultPhone.nickName}</td>
			<td><fmt:formatDate value ="${consultPhone.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
			<td>${consultPhone.loginPhone}</td>
			<td>${consultPhone.phoneNum}</td>
			<td>${consultPhone.babyName}</td>
			<td>${consultPhone.doctorName}</td>
			<td><fmt:formatDate value ="${consultPhone.date}" pattern="yyyy-MM-dd" /> <fmt:formatDate value ="${consultPhone.beginTime}" pattern="HH:mm" /></td>
			<td>${consultPhone.price}元/${consultPhone.type}min</td>
			<td><fmt:formatDate value ="${consultPhone.surplusDate}" pattern="mm:ss" /></td>
			<td>
				<c:if test="${consultPhone.state eq '0'}">待支付</c:if>
				<c:if test="${consultPhone.state eq '1'}">待接通</c:if>
				<c:if test="${consultPhone.state eq '2'}">待评价</c:if>
				<c:if test="${consultPhone.state eq '3'}">待分享</c:if>
				<c:if test="${consultPhone.state eq '4'}">已取消</c:if>
				<c:if test="${consultPhone.state eq '5'}">已完成</c:if>
				<c:if test="${consultPhone.state eq 'daichonglian'}">等待重连</c:if>
			</td>
			<td>
				<c:if test="${consultPhone.state eq '0'}">待支付</c:if>
				<c:if test="${consultPhone.state eq '1' || consultPhone.state eq '2' || consultPhone.state eq '3' || consultPhone.state eq 'daichonglian'}">已支付</c:if>
				<c:if test="${consultPhone.state eq '4'}">已退款</c:if>
			</td>
			<td><fmt:formatDate value ="${consultPhone.updateTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
			<td>${consultPhone.deleteBy}</td>

		</tr>
	</c:forEach>
	</tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>