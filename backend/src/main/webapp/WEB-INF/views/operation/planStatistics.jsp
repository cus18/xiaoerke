<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/treetable.jsp" %>
<br>
<head>
    <title>健康管理数据统计</title><!--@author 张博-->
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
    <li class="active"><a href="${ctx}/sys/PlanStatistics/getPlanStatistics"><font color="#006400">基础数据统计</font></a>
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

    <form:form id="searchForm" modelAttribute="registerServiceVo" action="${ctx}/sys/PlanStatistics/getPlanStatistics" method="post" class="form-search">
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
				<th class="sort-column name">访问次数</th>
				<th class="sort-column name">访问人数</th>
				<th class="sort-column name">新增用户数</th>
				<th class="sort-column name">用户留存</th>
				<th class="sort-column name">次日</th>
				<th class="sort-column name">三日</th>
				<th class="sort-column name">一周</th>
				<th class="sort-column name">活跃用户数（连续打卡）</th>
				<th class="sort-column name">按摩</th>
				<th class="sort-column name">排便</th>
				<th class="sort-column name">饮食</th>
				<th class="sort-column name">运动</th>
				<th class="sort-column name">购买物品点击量</th>
				<th class="sort-column name">应急药</th>
				<th class="sort-column name">便盆</th>
				<th class="sort-column name">食材</th>
				<th class="sort-column name">图书</th>
				<th class="sort-column name">反馈点击量</th>
				<th class="sort-column name">好了的</th>
				<th class="sort-column name">轻</th>
				<th class="sort-column name">中</th>
				<th class="sort-column name">重</th>
				<th class="sort-column name">无好转</th>
				<th class="sort-column name">点赞</th>
                </thead>
				<tbody id="treeTableList">
                <c:forEach items="${resultlist}" var="userData">
                    <tr>
					<td>${userData.date}</td>
					<td>${userData.visitPlanNums}</td>
					<td>${userData.visitPlanPeoples}</td>
					<td>${userData.newUsers}</td>
					<td></td>
					<td>${userData.userStayNextDay}</td>
					<td>${userData.userStayThreeDay}</td>
					<td>${userData.userStayWeek}</td>
					<td></td>
					<td>${userData.continuousPunchUsersForMassage}</td>
					<td>${userData.continuousPunchUsersForDefecate}</td>
					<td>${userData.continuousPunchUsersForFood}</td>
					<td>${userData.continuousPunchUsersForSport}</td>
					<td></td>
					<td>${userData.clickDrugNums}</td>
					<td>${userData.clickFoodNums}</td>
					<td>${userData.clickBedpanNums}</td>
					<td>${userData.clickBookNums}</td>
					<td></td>
					<td>${userData.feedBackGoodNums}</td>
					<td>${userData.feedBackLightNums}</td>
					<td>${userData.feedBackMidNums}</td>
					<td>${userData.feedBackWeightNums}</td>
					<td>${userData.feedBackNothingNums}</td>
					<td>${userData.clickLikeNums}</td>
				</tr>
                </c:forEach>
				</tbody>
            </table>

</body>
</html>