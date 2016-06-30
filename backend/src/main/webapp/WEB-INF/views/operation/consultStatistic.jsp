<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/treetable.jsp" %>
<br>
<head>
    <title>咨询数据统计</title><!--@author 得良-->
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
    <li class="active"><a href="${ctx}/consultStatistic/consultStatisticBaseData"><font color="#006400">咨询数据统计</font></a>
    </li>
</ul>
    <form:form id="searchForm" modelAttribute="consultStatisticVo" action="${ctx}/consultStatistic/consultStatisticBaseData" method="post" class="form-search">
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
				<th class="sort-column name">日期\项目</th>
				<th class="sort-column name">日均</th>
				<th class="sort-column name">累计</th>
				<th class="sort-column name">日满意度</th>
				<th class="sort-column name">周满意度</th>
				<th class="sort-column name">月满意度</th>
				<th class="sort-column name">日打赏比例</th>
				<th class="sort-column name">日打赏人数</th>
				<th class="sort-column name">日不满意</th>
				<th class="sort-column name">周不满意</th>
				<th class="sort-column name">月不满意</th>
				<th class="sort-column name">不评价</th>
				<th class="sort-column name">首次咨询取消关注</th>
				<th class="sort-column name">多次咨询取消关注</th>
				<th class="sort-column name">首次咨询</th>
                <th class="sort-column name">首次咨询比例</th>
                <th class="sort-column name">多次咨询</th>
                <th class="sort-column name">多次咨询比例</th>
				<th class="sort-column name">最高金额</th>
				<th class="sort-column name">最小金额</th>
				<th class="sort-column name">打赏总数</th>
				<th class="sort-column name">评价点击量</th>
				<th class="sort-column name">评价占比</th>
				<th class="sort-column name">分享点击量</th>
				<th class="sort-column name">分享占比</th>
                </thead>
				<tbody id="treeTableList">
                <c:forEach items="${consultStatisticVos}" var="vo">
                <tr>
					<td>${vo.displayDate}</td>
					<td>${vo.dayNumber}</td>
					<td>${vo.titileNumber}</td>
					<td>${vo.daySatisfiedDegree}</td>
					<td>${vo.weedSatisfiedDegree}</td>
					<td>${vo.monthSatisfiedDegree}</td>
					<td>${vo.rewardDegree}</td>
					<td>${vo.rewardNumber}</td>
					<td>${vo.dayYawpNumber}</td>
					<td>${vo.weekYawpNumber}</td>
					<td>${vo.monthYawpNumber}</td>
					<td>${vo.unevaluateNumber}</td>
					<td>${vo.firstConsultCancleAttentionNumber}</td>
					<td>${vo.moreConsultCancleAttentionNumber}</td>
					<td>${vo.firstConsultNumber}</td>
					<td>${vo.firstConsultDegree}</td>
					<td>${vo.moreConusltNumber}</td>
					<td>${vo.moreConsultDegree}</td>
					<td>${vo.maxMoney}</td>
					<td>${vo.minMoney}</td>
					<td>${vo.sumMoney}</td>
					<td>${vo.evaluateClickNumber}</td>
					<td>${vo.evaluateClickDegree}</td>
					<td>${vo.shareClickNumber}</td>
					<td>${vo.shareClickDegree}</td>
				</tr>
                </c:forEach>
				</tbody>
            </table>

</body>
</html>