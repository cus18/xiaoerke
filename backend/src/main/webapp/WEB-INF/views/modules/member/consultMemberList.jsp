<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>咨询会员列表</title>
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
			$("#searchForm").attr("action","${ctx}/member/memberList?");
			$("#searchForm").submit();
	    	return false;
	    }
	    function searchSub(){
			$("#searchForm").attr("action","${ctx}/member/consultMemberList?");
			$("#searchForm").submit();
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="#">咨询会员列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="ConsultMemberVo" action="${ctx}/member/memberList?" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:message content="${message}"/>
		<ul class="ul-form">
			<li><label>openid：</label>
				<form:input path="openid" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>微信名：</label>
				<form:input path="nickname" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="button" onclick="searchSub()" value="查询" />
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-bordered table-condensed">
		<thead>
			<tr>
				<th>openid</th>
				<th>微信名</th>
				<th>购买日期</th>
				<th>交易金额</th>
				<th>截止日期</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="member">
				<tr>
					<td>${member.openid}</td>
					<td>${member.nickname}</td>
					<td><fmt:formatDate value ="${member.createDate}" pattern="yyyy-MM-dd HH:mm" /></td>
					<td>${member.payAcount}</td>
					<td><fmt:formatDate value ="${member.endTime}" pattern="yyyy-MM-dd HH:mm" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>