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
		});

		//取消已选中的号源 zdl
		function cancleSelect(){
			var tArray = "";
			for(var i=0;i<document.getElementsByName("selectCheckBox").length;i++){
				if(document.getElementsByName("selectCheckBox")[i].getElementsByTagName("input")[0].checked){
					tArray = tArray + document.getElementsByName("selectCheckBox")[i].getElementsByTagName("input")[0].value +"-";
				}
			}
			$.ajax({
				type: "post",
				url: "${ctx}/register/deleteRegisterArray",
				data: {tArray: tArray},
				dataType: "json",
				success: function(){
					location.href = '${ctx}/register/registerList';
				}

			});
		}

		// 确认对话框 zdl
		function confirmxCancel(mess){
			top.$.jBox.confirm(mess,'系统提示',function(v){
				if(v=='ok'){
					cancleSelect();
				}
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/register/registerList?">号源列表</a></li>
		<li><a href="${ctx}/order/patientRegisterList?">加号列表</a></li>
		<li><a href="${ctx}/order/removedOrderList?">已删除订单列表</a></li>
		<li><a href="${ctx}/register/willNoRegisterList?">即将没有号源的医生列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="registerServiceVo" action="${ctx}/register/registerList" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:message content="${message}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label>医生科室：</label>
				<form:select path="department" class="input-medium">
					<form:option value="" label=""/>
					<c:forEach items="${illLevel1List}" var="ill">
				 		<form:option value="${ill}" label="${ill}"/>
				 	</c:forEach>
				</form:select>
			</li>
			<li><label>医生医院：</label>
				<form:select path="sysHospitalId" class="input-medium">
					<form:option value="" label=""/>
					<c:forEach items="${hospitalList}" var="hos">
				 		<form:option value="${hos.id}" label="${hos.name}"/>
				 	</c:forEach>
				</form:select>
			</li>
			<li><label>医生姓名：</label>
				<form:input name="doctorName" path="doctorName" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>订单类型：</label>
			<form:select path="relationType">
				<c:forEach items="${relationTypeMap}" var="relation">
			 		<form:option value="${relation.key}" label="${relation.value}"/>
			 	</c:forEach>
			</form:select>
			</li>
			<li class="clearfix"></li>
			<li><label>就诊时间：</label>
				<form:input id="fromDate" path="fromDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				至
				<form:input id="toDate" path="toDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li><label>挂号费：</label>
				<form:select path="priceRange">
					<c:forEach items="${priceMap}" var="sta">
				 		<form:option value="${sta.key}" label="${sta.value}"/>
				 	</c:forEach>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
		</ul>
	</form:form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th class="sort-column doctorName">医生姓名</th><th>就诊时间</th><th>价格</th><th>就医地址</th><th>号源类型</th><th>科室</th><th>手机</th><shiro:hasPermission name="user"><th>操作</th></shiro:hasPermission>
			<th><span onclick="confirmxCancel('确认要取消选中的号源吗？')"><font color="red" onmouseover="this.style.cursor='pointer'">取消已选中</font></span></th>
		</tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="register">
			<tr>
				<td>${register.doctorName}</td>
				<td><fmt:formatDate value ="${register.date}" pattern="yyyy-MM-dd" /> /<fmt:formatDate value ="${register.beginTime}" pattern="HH:mm" /></td>
				<td>${register.price}</td>
				<td>${register.hospitalName}${register.location}</td>
				<td>${register.relationType}</td>
				<td>${register.department}</td>
				<td>${register.phone}</td>
				<td>
    				<shiro:hasPermission name="order:appointment">
    					<a href="${ctx}/order/appointmentForm?id=${register.id}">预约</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="register:deleteRegister">
    					<a href="${ctx}/register/deleteRegister?id=${register.id}" onclick="return confirmx('确认要取消号源吗？', this.href)">取消号源</a>
    				</shiro:hasPermission>
				</td>
				<td><div name="selectCheckBox"><input id="${register.id}" value="${register.id}" type="checkbox"></div></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>