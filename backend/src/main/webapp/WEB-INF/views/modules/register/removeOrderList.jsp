<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>加号列表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/order/removedOrderList");
			$("#searchForm").submit();
	    	return false;
	    }
		var timer;
		var removedOrderCount;
		$(document).ready(function() {
			document.getElementById("newRemovedOrder").disabled=true;
			timer = window.setInterval(temp,300000);
			$.ajax({
	             type: "post",
	             url: "${ctx}/order/getNewRemovedOrderList?",
	             data: {},
	             dataType: "json",
	             success: function(data){
	            	 removedOrderCount = data.removedOrderCount;
	             }
       		});
		});
		
		function temp(){
			$.ajax({
	             type: "post",
	             url: "${ctx}/order/getNewRemovedOrderList?",
	             data: {pageNo:$("#pageNo").val(),pageSize:$("#pageSize").val()},
	             dataType: "json",
	             success: function(data){
	            	 if(data.removedOrderCount!=removedOrderCount){
	            		 document.getElementById("newRemovedOrder").disabled=false;
	            		 document.getElementById("newRemovedOrder").style.backgroundColor="#FF6666";
	            		 clearInterval(timer);
	            	 }
	             }
        	});
		}
		function refreshNewRemovedOrder(){
			timer = window.setInterval(temp,300000);
			window.location.href = "${ctx}/order/removedOrderList?";
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/register/registerList?">号源列表</a></li>
		<li><a href="${ctx}/order/patientRegisterList?">加号列表</a></li>
		<li class="active"><a href="${ctx}/order/removedOrderList?">已删除订单列表</a></li>
		<li><a href="${ctx}/register/willNoRegisterList?">即将没有号源的医生列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="patientRegisterServiceVo" action="${ctx}/order/patientRegisterList?" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:message content="${message}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li>
				<input id="newRemovedOrder" type="button" onclick="refreshNewRemovedOrder();" value="刷新最新已删除订单" />
			</li>
		</ul>
	</form:form>
	<table id="contentTable" class="table table-bordered table-condensed">
		<thead><tr><th>是否已交保证金</th><th>预约序号</th><th>患者姓名</th><th>手机号码</th><th>症状</th><th>就诊时间</th><th>删除时间</th><th>删除人</th><th>医生姓名</th></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="removeOrder">
			<tr>
				<td>${removeOrder.isPayDeposit}</td>
				<td>${removeOrder.registerNo}</td>
				<td>${removeOrder.babyName}</td>
				<td>${removeOrder.phone}</td>
				<td>${removeOrder.illness}</td>
				<td><fmt:formatDate value ="${removeOrder.date}" pattern="yyyy-MM-dd" /> /<fmt:formatDate value ="${removeOrder.beginTime}" pattern="HH:mm" /></td>
				<td><fmt:formatDate value ="${removeOrder.createdTime}" pattern="yyyy-MM-dd HH:mm" /></td>
				<td>${removeOrder.deleteBy}</td>
				<td>${removeOrder.doctorName}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>