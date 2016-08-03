<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/treetable.jsp" %>
<html>
<head>
    <title>渠道统计分析</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>

    <%--<script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
    <link href="${ctxStatic}/bootstrap/2.3.1/css_${not empty cookie.theme.value ? cookie.theme.value : 'cerulean'}/bootstrap.min.css"
          type="text/css" rel="stylesheet"/>
    <script src="${ctxStatic}/bootstrap/2.3.1/js/bootstrap.min.js" type="text/javascript"></script>
    <link href="${ctxStatic}/bootstrap/2.3.1/awesome/font-awesome.min.css" type="text/css" rel="stylesheet"/>--%>


    <script type="text/javascript">
        $(document).ready(function(){
            //modal
//            $("#myModal").modal('show');
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
                        if(msg.status == "1"){
                            //聚焦二维码输入框
                            $("#txtMarker").focus();
                            return false;
                        }else if(msg.status == "0"){
                            window.location.href="${ctx}/sys/Channel/ChannelMain";
                        }
                    },
                    error : function(XMLHttpRequest, textStatus, errorThrown) {
                        alert("保存失败!请重试");
                        return false;
                    },
                    dataType : 'json'
                });
            } );
        });
        //点击删除按钮
        function deleteInit(channelId,marketer){
            if(confirm("确定要删除二维码为【" + marketer + "】对应的渠道记录吗？")){
                $.ajax({
                    type : 'POST',
                    url : "${ctx}/sys/Channel/deleteChannelInfo",
                    data : {
                        'channelId' : channelId
                    },
                    success : function(msg) {
                        alert(msg.result);
                        window.location.href="${ctx}/sys/Channel/ChannelMain";
                    },
                    error : function(XMLHttpRequest, textStatus, errorThrown) {
                        alert("删除失败!请重试");
                        return false;
                    },
                    dataType : 'json'
                });
            }
        }
    </script>
</head>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/sys/Channel/ChannelMain"><font color="#006400">渠道添加</font></a></li>
    <li><a href="${ctx}/sys/Channel/userStatisticsChannel"><font color="#006400">用户统计（渠道）</font></a></li>
    <li><a href="${ctx}/sys/Channel/userStatisticsDepartment"><font color="#006400">用户统计（部门）</font></a></li>
    <li><a href="${ctx}/sys/Channel/ChannelConsultStatistics"><font color="#006400">咨询统计(渠道)</font></a></li>
    <li><a href="${ctx}/sys/Channel/DepartmentConsultStatistics"><font color="#006400">咨询统计(部门)</font></a></li>
    <li><a href="${ctx}/sys/Channel/newUserAttentionAndRemainStatistics"><font color="#006400">用户新关注与净留存统计</font></a></li>
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
        <th class="sort-column name">操作</th>
    </thead>
    <tbody id="treeTableList">
    <c:forEach items="${channelList}" var="channelVo">
        <tr id="${menu.id}" pId="${menu.parent.id ne '1'?menu.parent.id:'0'}">
            <td>${channelVo.marketer}</td>
            <td>${channelVo.operater}</td>
            <td>${channelVo.department}</td>
            <td>${channelVo.channel}</td>
            <td><a href="javascript:void(0);" onclick="deleteInit('${channelVo.id}','${channelVo.marketer}');">删除</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<%--<div class="modal fade bs-example-modal-lg" id="myModal" tabindex="-1"
     role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">渠道信息编辑</h4>
            </div>
            <div class="modal-body">
                <form id="questionForm" action="22222">
                    <div class="flextable-item flextable-primary">
                        <label>添加人</label> <input type="hidden" id="txtId"
                                                              name="id" />
                        <input type="text" id="textQuestion" class="form-control" placeholder="" name="question">
                    </div>

                    <div class="flextable-item flextable-primary">
                        <label>回答</label> <input type="text" id="txtAnswer"
                                                            class="form-control" placeholder="" name="answer">
                    </div>
                </form>
            </div>
            <div class="modal-footer">

                <button type="button" class="btn btn-primary btn-lg"
                        onclick="saveQuestionInfo();">保存</button>
            </div>
        </div>
    </div>
</div>--%>
</body>
</html>