<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/treetable.jsp" %>
<html>
<head>
    <title>渠道统计分析</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <script type="text/javascript" src=""></script>
    <script type="text/javascript">
        $(document).ready(function(){
            $("#treeTable").treeTable({expandLevel: 1}).show();
            //点击保存按钮
            $("#btnSave").click(function(){
                $.ajax({
                    type : 'POST',
                    url : "${ctx}/sys/Channel/addChannel",
                    data : {
                        'operater' : $("#txtOperater").val(),
                        'department' : $("#txtDepartment").val(),
                        'marker' : $("#txtMarker").val(),
                        'channel' : $("#txtChannel").val()
                    },
                    success : function(msg) {
                        alert(msg.result);
                        window.location.href="${ctx}/sys/Channel/ChannelMain";
                    },
                    error : function(XMLHttpRequest, textStatus, errorThrown) {
                        alert("保存失败!请重试");
                    },
                    dataType : 'json'
                });
            } );
        });

    </script>
</head>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/sys/Channel/ChannelMain"><font color="#006400">渠道添加</font></a></li>
    <li><a href="${ctx}/sys/Channel/userStatisticsChannel"><font color="#006400">用户统计（渠道）</font></a></li>
    <li><a href="${ctx}/sys/Channel/userStatisticsDepartment"><font color="#006400">用户统计（部门）</font></a></li>
    <li><a href="${ctx}/sys/Channel/ChannelConsultStatistics"><font color="#006400">渠道咨询统计</font></a></li>
    <li><a href="${ctx}/sys/Channel/DepartmentConsultStatistics"><font color="#006400">部门咨询统计</font></a></li>
</ul>

<form:form id="searchForm" modelAttribute="registerServiceVo" action="${ctx}/sys/Channel/ChannelMain" method="post" class="form-search">
    <sys:message content="${message}"/>
    添加人 ：<input id="txtOperater" name="operater" type="text">
    部  门 ：
    <select name="department" id="txtDepartment">
        <c:forEach items="${departs}" var="depart" step="1">
            <option value="${depart}">${depart}</option>
        </c:forEach>
    </select>


    二维码 ：<input id="txtMarker" name="marker" type="text">
    渠道细分 ：<input id="txtChannel" name="channel" type="text">
    <input id="btnSave" class="btn btn-primary"  type="button" value="保存"/>
    <input id="btnSubmit" class="btn btn-primary" type="submit" value="查看"/>
</form:form>

<sys:message content="${message}"/>

<table id="treeTable" class="table table-striped table-bordered table-condensed hide">
    <thead>
    <tr>
        <th class="sort-column name">二维码</th>
        <th class="sort-column name">添加人</th>
        <th class="sort-column name">部门</th>
        <th class="sort-column name">渠道细分</th>
    </thead>
    <tbody id="treeTableList">
    <c:forEach items="${channelList}" var="channelVo">
        <tr id="${menu.id}" pId="${menu.parent.id ne '1'?menu.parent.id:'0'}">
            <td>${channelVo.marketer}</td>
            <td>${channelVo.operater}</td>
            <td>${channelVo.department}</td>
            <td>${channelVo.channel}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>