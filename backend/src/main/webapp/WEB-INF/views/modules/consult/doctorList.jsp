<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>医生列表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/consult/consultDoctorList?");
			$("#searchForm").submit();
	    	return false;
	    }
	    function searchSub(){
			$("#searchForm").attr("action","${ctx}/consult/consultDoctorList?");
			$("#searchForm").submit();
		}

		function doctorOperForm(href,title){
			href=encodeURI(encodeURI(href));
			top.$.jBox.open('iframe:'+href,title,430,370,{
				buttons:{"关闭":true},
				closed: function () {
					$("#searchForm").attr("action","${ctx}/consult/consultDoctorList");
					$("#searchForm").submit();
				}
			});
		}
		function doctorVerificationForm(href,title){
			href=encodeURI(encodeURI(href));
			top.$.jBox.open('iframe:'+href,title,430,370,{
				buttons:{"关闭":true},
				closed: function () {
					$("#searchForm").attr("action","${ctx}/consult/consultDoctorList");
					$("#searchForm").submit();
				}
			});
		}

		function dataExport(type){
			href="${ctx}/consult/exportForm?type="+type;
			top.$.jBox.open('iframe:'+href,'导出统计表',$(top.document).width()-860,$(top.document).height()-330,{
				buttons:{"关闭":true},
				closed: function () {
					$("#searchForm").attr("action","${ctx}/consult/consultDoctorList?");
					$("#searchForm").submit();
				}
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/consult/consultDoctorList?">咨询医生列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="user" action="${ctx}/consult/consultDoctorList?" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:message content="${message}"/>
		<ul class="ul-form">
			<li class="btns">
				<input class="btn btn-primary" type="button" onclick="dataExport('receiveTheMindExport')" value="导出收到心意"/>
				<input class="btn btn-primary" type="button" onclick="dataExport('dissatisfiedExport')" value="导出不满意"/>
			</li>
			<li class="clearfix"></li>
			<li><label>医生姓名：</label>
				<form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>手机号：</label>
				<form:input path="phone" htmlEscape="false" maxlength="50" class="input-medium"/>
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
						<a href="#" onclick="doctorVerificationForm('${ctx}/consult/searchVerificationCode?id=${u.id}','验证码修改')">验证码修改</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>