<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/treetable.jsp" %>
<html>
<head>
	<title>提现记录</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		$(document).ready(function() {
			$("#searchForm").validate({
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
			$("#treeTable").treeTable({expandLevel : 1}).show();
		}); 
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/account/withdrawRecord"><font color="#006400">提现记录</font></a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="withdrawRecord" action="${ctx}/sys/account/withdrawRecord" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="1"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label>手机号：</label>
				<form:input name="phone" path="phone" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>金额：</label>
				<form:select path="priceRange">
					<c:forEach items="${priceMap}" var="pri">
				 		<form:option value="${pri.key}" label="${pri.value}"/>
				 	</c:forEach>
				</form:select>
			</li>
			<li><label>状态：</label>
				<form:select path="status">
					<c:forEach items="${statusMap}" var="sta">
				 		<form:option value="${sta.key}" label="${sta.value}"/>
				 	</c:forEach>
				</form:select>
			</li>
			<li class="clearfix"></li>
			<li><label>提现时间：</label>
				<form:input id="fromCreatedDate" path="fromCreatedDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				至
				<form:input id="toCreatedDate" path="toCreatedDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li><label>到账时间：</label>
				<form:input id="fromReceivedDate" path="fromReceivedDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				至
				<form:input id="toReceivedDate" path="toReceivedDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
		</ul>
	</form:form>

	<sys:message content="${message}"/>

	<table id="treeTable" class="table table-striped table-bordered table-condensed hide">
		<thead><tr><th>姓名</th><th>用户类型</th><th>手机号</th><th>提现金额</th><th>提现时间</th><th>到账时间</th><th>状态</th><shiro:hasPermission name="sys:user:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody id="treeTableList">
		<c:forEach items="${page.list}" var="withdrawRecord">
			<tr id="${menu.id}" pId="${menu.parent.id ne '1'?menu.parent.id:'0'}">
				<td>${withdrawRecord.name}</td>
				<td>${withdrawRecord.userType}</td>
				<td>${withdrawRecord.phone}</td>
				<td>${withdrawRecord.moneyAmount}</td>
				<td>${withdrawRecord.createdDate}</td>
				<td>${withdrawRecord.receivedDate}</td>
				<td>${withdrawRecord.status}</td>
				<shiro:hasPermission name="sys:user:edit"><td>
    				<a href="${ctx}/sys/account/withdrawDetail?id=${withdrawRecord.id}">详情</a>
					&nbsp;&nbsp;&nbsp;
					<a href="${ctx}/sys/account/withdrawDelete?id=${withdrawRecord.id}" onclick="return confirmx('确认要删除记录吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>