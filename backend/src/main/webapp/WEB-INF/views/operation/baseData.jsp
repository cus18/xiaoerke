<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/treetable.jsp" %>
<br>
<head>
    <title>基础数据统计</title><!--@author 张博-->
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#treeTable").treeTable({expandLevel: 1}).show();
        });
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/sys/BaseData/getBaseDataStatistics"><font color="#006400">基础数据统计</font></a>
    </li>
</ul>
<%--<form id="searchForm" action="${ctx}/sys/BaseData/getBaseDataStatistics" method="post" class="form-search ">--%>
    <%--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>
    <%--<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"--%>
           <%--onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>--%>
    <%--&nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;--%>
    <%--<input name="endDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"--%>
           <%--onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>--%>
    <%--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>--%>
<%--</form>--%>

    <form:form id="searchForm" modelAttribute="registerServiceVo" action="${ctx}/sys/BaseData/getBaseDataStatistics" method="post" class="form-search">
	    <sys:message content="${message}"/>
        <form:input id="startDate" path="startDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			至
			<form:input id="endDate" path="endDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
    </form:form>

            <table class="table table-striped table-bordered table-condensed">

                <thead>

				<th class="sort-column name">时间\指标</th>
				<th class="sort-column name">预约大夫</th>
				<th class="sort-column name">访问次数</th>
				<th class="sort-column name">新增订单</th>
				<th class="sort-column name">取消订单</th>
				<th class="sort-column name">净增订单</th>
				<th class="sort-column name">累计有效订单</th>
				<th class="sort-column name">当天就诊订单</th>
				<th class="sort-column name">当天真实用户</th>
				<th class="sort-column name">当天付费订单</th>
				<!-- <th class="sort-column name">累计已就诊订单</th> -->
				<th class="sort-column name">咨询大夫</th>
				<th class="sort-column name">有效咨询人数</th>
				<th class="sort-column name">累计有效咨询人数</th>
				<%--<th class="sort-column name">郑玉巧说</th>--%>
				<th class="sort-column name">新注册宝宝人数</th>
				<th class="sort-column name">累计注册宝宝人数</th>
				<%--<th class="sort-column name">新增阅读人数</th>--%>
				<%--<th class="sort-column name">累计阅读人数</th>--%>
				<%--<th class="sort-column name">新增阅读人次</th>--%>
				<%--<th class="sort-column name">累计阅读人次</th>--%>
				<%--<th class="sort-column name">转发分享人次</th>--%>
				<%--<th class="sort-column name">按Cookie统计人数</th>--%>
<!-- 				<th class="sort-column name">用户数</th> -->
<!-- 				<th class="sort-column name">新增</th> -->
<!-- 				<th class="sort-column name">取消</th> -->
<!-- 				<th class="sort-column name">净增</th> -->
<!-- 				<th class="sort-column name">累计用户</th> -->
				<th class="sort-column name">医生数</th>
				<th class="sort-column name">新增</th>
				<th class="sort-column name">累计</th>
                </thead>
				<tbody id="treeTableList">
                <c:forEach items="${resultlist}" var="userData">
                    <tr>
					<td>${userData.CREATED}</td>
					<td></td>
					<td>${userData.visiteNum}</td>
					<td>${userData.addOrder}</td>
					<td>${userData.cancelOrder}</td>
					<td>${userData.netOrder}</td>
					<td>${userData.totalAddOrder}</td>
					<td>${userData.thatdayOrder}</td>
					<td>${userData.truthorder}</td>
					<td>${userData.payorder}</td>
					<!-- <td>${userData.totalVictoryOrder}</td> -->
					<td></td>
					<td>${userData.victorynums}</td>
					<td>${userData.totalvictorynums}</td>
					<%--<td></td>--%>
					<td>${userData.addZYQPbe}</td>
					<td>${userData.totalZYQPbe}</td>
					<%--<td>${userData.addreadnums}</td>--%>
					<%--<td>${userData.totalreadnums}</td>--%>
					<%--<td>${userData.addsecreadnums}</td>--%>
					<%--<td>${userData.totalsecreadnums}</td>--%>
					<%--<td>${userData.sharenums}</td>--%>
					<%--<td>${userData.cookieTotal}</td>--%>
<!-- 					<td></td> -->
<%-- 					<td>${userData.addusernum}</td> --%>
<%-- 					<td>${userData.cancelnum}</td> --%>
<%-- 					<td>${userData.netusernum}</td> --%>
<%-- 					<td>${userData.totalusernum}</td> --%>
					<td></td>
					<td>${userData.adddoctornum}</td>
					<td>${userData.netdoctornum}</td>
				</tr>
                </c:forEach>
				</tbody>
            </table>

</body>
</html>