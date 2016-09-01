<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>咨询订单列表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/consult/consultOrderList?");
			$("#searchForm").submit();
	    	return false;
	    }
	    function searchSub(){
			$("#searchForm").attr("action","${ctx}/consult/consultOrderList?");
			$("#searchForm").submit();
		}

		function doctorOperForm(href,title){
			href=encodeURI(encodeURI(href));
			top.$.jBox.open('iframe:'+href,title,430,370,{
				buttons:{"关闭":true},
				closed: function () {
					$("#searchForm").attr("action","${ctx}/consult/consultOrderList");
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
		<li class="active"><a href="${ctx}/consult/consultOrderList?">交易订单</a></li>
		<li><a href="${ctx}/consult/consultUserInfoList?">账户状态</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="vo" action="${ctx}/consult/consultOrderList?" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:message content="${message}"/>
		<ul class="ul-form">
			<li class="clearfix"></li>
			<li><label>openid：</label>
				<form:input path="openid" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>微信名：</label>
				<form:input path="nickname" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>日期范围：</label>
				<form:input id="fromDate" path="fromDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				至
				<form:input id="toDate" path="toDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li><label>交易方式：</label>
				<form:select path="feeType">
					<c:forEach items="${tradeWayMap}" var="tradeWay">
						<form:option value="${tradeWay.key}" label="${tradeWay.value}"/>
					</c:forEach>
				</form:select>
			</li>
			<li class="btns">
				<input class="btn btn-primary" type="button" onclick="return page(1,10);" value="搜索" />
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
				<th>交易日期</th>
				<th>交易金额</th>
				<th>交易方式</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="u">
				<tr>
					<td>${u.openid}</td>
					<td>${u.nickname}</td>
					<td>${u.receiveDate}</td>
					<td>
						<c:if test="${u.feeType eq 'doctorConsultPay'}">${u.amount/100}元</c:if>
						<c:if test="${u.feeType eq 'doctorConsultGift'}"><fmt:formatNumber var="c" value="${u.amount}" pattern="#"/>${c}次机会</c:if>
					</td>
					<td>
						<c:if test="${u.status eq 'success' and u.reason eq null}">支付</c:if>
						<c:if test="${u.status eq 'success' and u.reason ne null}">已退款</c:if>
						<c:if test="${u.status eq 'doctorConsultGift'}">赠送</c:if>
					</td>
					<td>
						<c:if test="${u.status eq 'success' and u.reason eq null}">
							<a href="#" onclick="doctorOperForm('${ctx}/consult/refundFeeForm?orderId=${u.orderId}&nickname=${u.nickname}&amount=${u.amount/100}&openid=${u.openid}','退款')">退款</a>
						</c:if>
						<c:if test="${u.status eq 'success' and u.reason ne null}">不可退</c:if>
						<c:if test="${u.status eq 'doctorConsultGift'}">不可退</c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>