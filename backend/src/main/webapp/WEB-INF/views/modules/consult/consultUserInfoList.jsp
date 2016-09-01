<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>咨询用户状态列表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/consult/consultUserInfoList?");
			$("#searchForm").submit();
	    	return false;
	    }
	    function searchSub(){
			$("#searchForm").attr("action","${ctx}/consult/consultUserInfoList?");
			$("#searchForm").submit();
		}

		function giftConsultTime(href,title){
			href=encodeURI(encodeURI(href));
			top.$.jBox.open('iframe:'+href,title,430,370,{
				buttons:{"关闭":true},
				closed: function () {
					$("#searchForm").attr("action","${ctx}/consult/consultUserInfoList");
					$("#searchForm").submit();
				}
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/consult/consultOrderList?">交易订单</a></li>
		<li class="active"><a href="${ctx}/consult/consultUserInfoList?">账户状态</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="vo" action="${ctx}/consult/consultUserInfoList?" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:message content="${message}"/>
		<ul class="ul-form">
			<li class="clearfix"></li>
			<li><label>openid：</label>
				<form:input path="sysUserId" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>微信名：</label>
				<form:input path="nickname" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li class="btns">
				<input class="btn btn-primary" type="button" onclick="searchSub()" value="搜索" />
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
				<th>当月有效机会</th>
				<th>长期有效机会</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="u">
				<tr>
					<td>${u.sysUserId}</td>
					<td>${u.nickname}</td>
					<td>${u.monthTimes}</td>
					<td>${u.permTimes}</td>
					<td>
						<a href="#" onclick="giftConsultTime('${ctx}/consult/consultTimeGiftForm?id=${u.id}&sysUserId=${u.sysUserId}&nickname=${u.nickname}','赠送机会')">赠送</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>