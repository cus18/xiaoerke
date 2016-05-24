<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员列表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnGiftMember").click(function(){
				$("#searchForm").attr("action","${ctx}/member/giftMemberForm");
				$("#searchForm").submit();
			});
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出用户数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/member/export");
						$("#searchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
		});
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/insurance/insuranceList?");
			$("#searchForm").submit();
	    	return false;
	    }
	    function searchSub(){
			$("#searchForm").attr("action","${ctx}/member/memberList?");
			$("#searchForm").submit();
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/insurance/insuranceList?insuranceType=1">订单列表</a></li>
		<li class="active"><a href="${ctx}/insurance/insuranceList?stateFlag=2&insuranceType=1">待审核</a></li>
		<li><a href="${ctx}/insurance/insuranceList?stateFlag=3&insuranceType=1">已补贴</a></li>
	</ul>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-bordered table-condensed">
		<thead>
			<tr>
				<th>订单号</th>
				<th>微信名</th>
				<th>手机号</th>
				<th>购买人</th>
				<th>身份证号</th>
				<th>宝宝姓名</th>
				<th>下单时间</th>
				<th>开始时间</th>
				<th>结束时间</th>
				<th>最后操作时间</th>
				<th>操作人</th>
				<th>状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="insurance">
				<tr>
					<td>${insurance.id}</td>
					<td>${insurance.parentPhone}</td>
					<td>${insurance.parentPhone}</td>
					<td>${insurance.parentName}</td>
					<td>${insurance.idCard}</td>
					<td>${insurance.babyId}</td>
					<td><fmt:formatDate value ="${insurance.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td><fmt:formatDate value ="${insurance.startTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td><fmt:formatDate value ="${insurance.endTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td><fmt:formatDate value ="${insurance.updateTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>${insurance.updateBy}</td>
					<td>
						<c:if test="${insurance.state eq '0'}">待生效</c:if>
						<c:if test="${insurance.state eq '1'}">有效</c:if>
						<c:if test="${insurance.state eq '2'}">待审核</c:if>
						<c:if test="${insurance.state eq '3'}">已赔付</c:if>
						<c:if test="${insurance.state eq '4'}">已到期</c:if>
						<c:if test="${insurance.state eq '5'}">审核失败</c:if>
					</td>
					<td><a href="${ctx}/insurance/orderDetails?id=${insurance.id}">查看详情</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>