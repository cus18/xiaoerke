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
			$("#searchForm").attr("action","${ctx}/order/patientRegisterList?flag=yes");
			$("#searchForm").submit();
	    	return false;
	    }
		var orderCount;
		var timer;
		$(document).ready(function() {
			var warn = "${warn}";
			if(warn=="1"){
				document.getElementById("warn").checked=true;
			}
			document.getElementById("newOrder").disabled=true;
			timer = window.setInterval(temp,300000);
			$.ajax({
	             type: "post",
	             url: "${ctx}/order/getNewOrderList?",
	             data: {},
	             dataType: "json",
	             success: function(data){
	            	 orderCount = data.orderCount;
	             }
       		});
			
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出用户数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/order/export");
						$("#searchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
		});
		function temp(){
			$.ajax({
	             type: "post",
	             url: "${ctx}/order/getNewOrderList?",
	             data: {pageNo:$("#pageNo").val(),pageSize:$("#pageSize").val()},
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
			timer = window.setInterval(temp,300000);
			window.location.href = "${ctx}/order/patientRegisterList?";
		}
		function searchSub(){
			$("#searchForm").attr("action","${ctx}/order/patientRegisterList");
			$("#searchForm").submit();
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/register/registerList?">号源列表</a></li>
		<li class="active"><a href="${ctx}/order/patientRegisterList?">加号列表</a></li>
		<li><a href="${ctx}/order/removedOrderList?">已删除订单列表</a></li>
		<li><a href="${ctx}/register/willNoRegisterList?">即将没有号源的医生列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="patientRegisterServiceVo" action="${ctx}/order/patientRegisterList?" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:message content="${message}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li>
				<input id="newOrder" type="button" onclick="refreshNewOrder();" value="刷新最新订单" />
			</li>
			<li class="clearfix"></li>
			<li>
				<span>
					<input id="warn" name="warn" type="checkbox" value="1">
					<label for="warn" >就诊前2小时提醒</label>
				</span>
			</li>
			<li><label>患者姓名：</label>
				<form:input path="babyName" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>医生姓名：</label>
				<form:input path="doctorName" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>手机号：</label>
				<form:input path="phone" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>订单状态：</label>
			<form:select path="status">
				<c:forEach items="${statusList}" var="sta">
			 		<form:option value="${sta.key}" label="${sta.value}"/>
			 	</c:forEach>
			</form:select>
			</li>
			<li><label>订单类型：</label>
			<form:select path="relationType">
				<c:forEach items="${relationTypeMap}" var="relation">
			 		<form:option value="${relation.key}" label="${relation.value}"/>
			 	</c:forEach>
			</form:select>
			</li>
			<li class="clearfix"></li>
			<li><label>下单时间：</label>
				<form:input id="OrderCreateDateFrom" name="OrderCreateDateFrom" path="OrderCreateDateFrom" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				至
				<form:input id="OrderCreateDateTo" name="OrderCreateDateTo" path="OrderCreateDateTo" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li><label>就诊时间：</label>
				<form:input id="visitDateFrom" path="visitDateFrom" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				至
				<form:input id="visitDateTo" path="visitDateTo" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="button" onclick="searchSub()" value="查询" />
				<shiro:hasPermission name="order:exportOrderForm">
					<input id="btnExport" class="btn btn-primary" type="button" value="导出数据表"/>
				</shiro:hasPermission>
			</li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-bordered table-condensed">
		<thead>
			<tr>
				<th>预约序号</th>
				<th>微信名</th>
				<th>患者姓名</th>
				<th>手机号码</th>
				<th>下单时间</th>
				<th>订单状态</th>
				<th>就诊时间</th>
				<th>订单真伪</th>
				<th>医生姓名</th>
				<th>是否黄牛</th>
				<th>订单类型</th>
				<th>支付状态</th>
				<th>最后操作时间</th>
				<th>删除人</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="patient">
			<c:if test="${patient.warnFlag=='1'}">
				<tr style='background-color:LightPink'>
			</c:if>
			<c:if test="${patient.warnFlag!='1'}">
				<tr>
			</c:if>
				<td>${patient.registerNo}</td>
				<td>${patient.nickName}</td>
				<td>${patient.babyName}</td>
				<td>${patient.phone}</td>
				<td>${patient.showCreateDate}</td>
				<td>${patient.status}</td>
				<td>${patient.showTime}</td>
				<td>${patient.falseUserReason}</td>
				<td>${patient.doctorName}</td>
				<td>${patient.doctorName}</td>
				<td>${patient.relationType}</td>
				<td>${patient.payStatus}</td>
				<td>${patient.showUpdateDate}</td>
				<td>${patient.deleteBy}</td>
				<td>
				<c:choose>
					<c:when test="${patient.status=='已取消'}">
						已取消&nbsp;&nbsp;&nbsp;
					</c:when>
					<c:otherwise>
						<shiro:hasPermission name="order:cancelOrder">
							<a href="#"  onclick="viewCancelAppointment('${ctx}/order/cancelAppointmentForm?registerNo=${patient.registerNo}&patientId=${patient.id}&sysRegisterId=${patient.sysRegisterServiceId}&memberServiceId=${patient.memberServiceId}')">取消预约</a>
							<script type="text/javascript">
								function viewCancelAppointment(href){
									top.$.jBox.open('iframe:'+href,'取消预约',$(top.document).width()-820,$(top.document).height()-280,{
										buttons:{"关闭":true},
										closed: function () { 
											$("#searchForm").attr("action","${ctx}/order/patientRegisterList");
											$("#searchForm").submit();
										}
									});
								}
							</script>
						</shiro:hasPermission>
					</c:otherwise>
				</c:choose>
    				<shiro:hasPermission name="order:returnVisitDetail">
    					<a href="${ctx}/order/patientReturnVisitDetail?id=${patient.id}&sysRegisterId=${patient.sysRegisterServiceId}&pageNo=${page.pageNo}&pageSize=${page.pageSize}">回访信息</a>
    				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>