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
			$("#searchForm").attr("action","${ctx}/member/memberList?");
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
		<li class="active"><a href="${ctx}/member/memberList?">会员列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="vo" action="${ctx}/member/memberList?" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:message content="${message}"/>
		<ul class="ul-form">
			<li><label>会员微信名：</label>
				<form:input path="nickName" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>手机号：</label>
				<form:input path="phone" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>会员状态：</label>
			<form:select path="status">
				<c:forEach items="${statusList}" var="sta">
			 		<form:option value="${sta.key}" label="${sta.value}"/>
			 	</c:forEach>
			</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="button" onclick="searchSub()" value="查询" />
			<input id="btnExport" class="btn btn-primary" type="button" value="导出数据表"/>
			</li>
			<li class="clearfix"></li>
			<shiro:hasPermission name="member:giftMember">
				<input id="btnGiftMember" class="btn btn-primary" type="button" value="赠送会员通道"/>
			</shiro:hasPermission>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-bordered table-condensed">
		<thead>
			<tr>
				<th>id</th>
				<th>来源渠道</th>
				<th>微信名</th>
				<th>手机号</th>
				<th>服务类型</th>
				<th>购买时间</th>
				<th>服务有效期</th>
				<th>剩余预约次数</th>
				<th>收费</th>
				<th>会员状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="member">
				<tr>
					<td>${member.id}</td>
					<td>${member.name}</td>
					<td>${member.nickName}</td>
					<td>${member.phone}</td>
					<td>${member.type}</td>
					<td>${member.activateDateStr}</td>
					<td>${member.serviceValidityPeriod}天</td>
					<td>${member.leftTimes}</td>
					<td>${member.isPay}</td>
					<td>${member.status}</td>
					<td>
					<shiro:hasPermission name="member:refundMembershipFee">
						<c:choose>
							<c:when test="${member.isPay=='否'}">
								不可退&nbsp;&nbsp;&nbsp;
							</c:when>
							<c:when test="${member.status=='已退款'}">
								已退款&nbsp;&nbsp;&nbsp;
							</c:when>
							<c:otherwise>
								<a href="#"  onclick="refundMembershipFee('${ctx}/member/refundMembershipFeeForm?id=${member.id}&nickName=${member.nickName}')">退会员费</a>
							</c:otherwise>
						</c:choose>
					</shiro:hasPermission>
						<script type="text/javascript">
							function refundMembershipFee(href){
							href=encodeURI(encodeURI(href));
								top.$.jBox.open('iframe:'+href,'退会员费',$(top.document).width()-860,$(top.document).height()-330,{
									buttons:{"关闭":true},
									closed: function () { 
										$("#searchForm").attr("action","${ctx}/member/memberList?");
										$("#searchForm").submit();
									}
								});
							}
						</script>
						<shiro:hasPermission name="member:memberUsageDetail">
					 		<a href="${ctx}/member/memberUsageDetail?id=${member.id}">使用情况</a>
					 	</shiro:hasPermission>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>