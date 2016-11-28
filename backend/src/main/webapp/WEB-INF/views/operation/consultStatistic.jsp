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
		function transRecord(){
			alertx("开始同步");
			$.ajax({
				type: "get",
				url: "${ctx}/statistic/consultRecordMongoTransMySql",
				data: {},
				dataType: "json",
				success: function(data){
					if(data.status == "success"){
						alertx("咨询记录同步成功，请查询！");
					}
				}
			});
		}
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li><a href="${ctx}/sys/BaseData/getBaseDataStatistics"><font color="#006400">基础数据统计</font></a>
    </li>
    <li class="active"><a href="${ctx}/consultStatistic/consultStatisticBaseData"><font color="#006400">咨询数据统计</font></a>
    </li>
    <li><a href="${ctx}/sys/BaseData/getUmbrellaDataStatistics"><font color="#006400">宝护伞数据统计</font></a>
    </li>
    <li><a href="${ctx}/sys/BaseData/getConsultDoctorDataStatistics"><font color="#006400">咨询大夫数据统计</font></a>
    </li>
</ul>
    <form:form id="searchForm" modelAttribute="consultStatisticVo" action="${ctx}/consultStatistic/consultStatisticBaseData" method="post" class="form-search">
	    <sys:message content="${message}"/>
        <form:input id="startDate" path="startDate" type="text" readonly="readonly" maxlength="60" class="input-medium Wdate"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			至
			<form:input id="endDate" path="endDate" type="text" readonly="readonly" maxlength="60" class="input-medium Wdate"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		<input id="btnSubmit" class="btn btn-primary" type="button" onclick="transRecord()" value="同步咨询记录到最新版"/>
    </form:form>
            <table class="table table-striped table-bordered table-condensed">
                <thead>
				<th class="sort-column name"><font color="red">日期\项目</font></th>
				<th class="sort-column name">日有效</th>
				<th class="sort-column name">累计</th>
				<th class="sort-column name"><font color="#8b4513">日满意度</font></th>
				<th class="sort-column name"><font color="#8b4513">周满意度</font></th>
				<th class="sort-column name"><font color="#8b4513">月满意度</font></th>
				<th class="sort-column name"><font color="#8b4513">日打赏比例</font></th>
				<th class="sort-column name"><font color="#8b4513">日打赏人数</font></th>
				<th class="sort-column name"><font color="#7fff00">日不满意</font></th>
				<th class="sort-column name"><font color="#7fff00">周不满意</font></th>
				<th class="sort-column name"><font color="#7fff00">月不满意</font></th>
				<th class="sort-column name"><font color="#8b008b">不评价</font></th>
				<th class="sort-column name"><font color="#dc143c">首次咨询取消关注</font></th>
				<th class="sort-column name"><font color="#dc143c">多次咨询取消关注</font></th>
				<th class="sort-column name">首次咨询</th>
                <th class="sort-column name">首次咨询比例</th>
                <th class="sort-column name">多次咨询</th>
                <th class="sort-column name">多次咨询比例</th>
				<th class="sort-column name"><font color="purple">最高金额</font></th>
				<th class="sort-column name"><font color="purple">最小金额</font></th>
				<th class="sort-column name"><font color="purple">打赏总数</font></th>
				<th class="sort-column name"><font color="purple">评价点击量</font></th>
				<th class="sort-column name"><font color="purple">评价占比</font></th>
				<th class="sort-column name"><font color="purple">分享点击量</font></th>
				<th class="sort-column name"><font color="purple">分享占比</font></th>

				<th class="sort-column name"><font color="purple">收费消息推送数</font></th>
				<th class="sort-column name"><font color="purple">收费消息链接点击数</font></th>
				<th class="sort-column name"><font color="purple">付费人数</font></th>
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

					<td>${vo.chargeSendMessageNumber}</td>
					<td>${vo.chargeMessageClickNumber}</td>
					<td>${vo.chargeSuccessNumber}</td>
				</tr>
                </c:forEach>
				</tbody>
            </table>

</body>
</html>