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
    <li><a href="${ctx}/sys/BaseData/getBaseDataStatistics"><font color="#006400">基础数据统计</font></a>
    </li>
    <li><a href="${ctx}/consultStatistic/consultStatisticBaseData"><font color="#006400">咨询数据统计</font></a>
    </li>
    <li class="active"><a href="${ctx}/sys/BaseData/getUmbrellaDataStatistics"><font color="#006400">宝护伞数据统计</font></a>
    </li>
    <li><a href="${ctx}/sys/BaseData/getConsultDoctorDataStatistics"><font color="#006400">咨询大夫数据统计</font></a>
    </li>
</ul>
<form id="searchForm" modelAttribute="registerServiceVo" action="${ctx}/sys/BaseData/getUmbrellaDataStatistics" method="post" class="form-search">
    <input id="startDate" name="startDate" value="${startDate}" type="text" readonly="readonly" maxlength="20" class="input-small Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
    至
    <input id="endDate" name="endDate" value="${endDate}" type="text" readonly="readonly" maxlength="20" class="input-small Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
    <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
</form>

<table class="table table-striped table-bordered table-condensed">
    <thead>
    <th class="sort-column name">时间\指标</th>
    <th class="sort-column name">新增家庭数</th>
    <th class="sort-column name">累计家庭数</th>
    <th class="sort-column name">新增用户数</th>
    <th class="sort-column name">累计用户数</th>
    </thead>
    <tbody id="treeTableList">
    <c:forEach items="${resultList}" var="map">
        <tr>
            <td>${map.date}</td>
            <td>${map.addFamily}</td>
            <td>${map.totalFamily}</td>
            <td>${map.addUser}</td>
            <td>${map.totalUser}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>