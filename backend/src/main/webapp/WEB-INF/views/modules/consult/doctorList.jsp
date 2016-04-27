<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>医生列表</title>
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

		function doctorOperForm(href,title){
			href=encodeURI(encodeURI(href));
			top.$.jBox.open('iframe:'+href,title,$(top.document).width()-860,$(top.document).height()-250,{
				buttons:{"关闭":true},
				closed: function () {
					$("#searchForm").attr("action","${ctx}/consult/consultDoctorList");
					$("#searchForm").submit();
				}
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/member/memberList?">会员列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="user" action="${ctx}/member/memberList?" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:message content="${message}"/>
		<ul class="ul-form">
			<li><label>医生姓名：</label>
				<form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li class="btns">
				<input class="btn btn-primary" type="button" onclick="searchSub()" value="查询" />
				<input class="btn btn-primary" type="button" onclick="doctorOperForm('${ctx}/consult/consultOperForm?','新增医生')" value="新增医生" />
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-bordered table-condensed">
		<thead>
			<tr>
				<th>id</th>
				<th>医生姓名</th>
				<th>手机号</th>
				<th>邮箱</th>
				<th>角色</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="u">
				<tr>
					<td>${u.id}</td>
					<td>${u.name}</td>
					<td>${u.phone}</td>
					<td>${u.email}</td>
					<td>
						<c:if test="${u.userType eq 'distributor'}">分诊小宝</c:if>
						<c:if test="${u.userType eq 'consultDoctor'}">咨询医生</c:if>
					</td>
					<td>
						<a href="${ctx}/consult/doctorMoreSettingForm?id=${u.id}">更多设置</a>
						<a href="#" onclick="doctorOperForm('${ctx}/consult/consultOperForm?id=${u.id}','账号修改')">账号修改</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>