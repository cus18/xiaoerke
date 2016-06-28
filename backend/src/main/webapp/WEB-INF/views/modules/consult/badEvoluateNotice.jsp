<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>差评提醒</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function() {

        });

        function deleteNoticeUser(obj){
            $("#openId").val(obj);
            alert($("#openId").val());
            if(!confirm("确认删除该用户？")){
                window.event.returnValue = false;
            }else{
//                $("#searchForm").attr("action","consult/remindUser/updateRemindUser?");
//                $("#searchForm").submit();
//                document.location.reload();
                $.ajax({
                    type: "GET",
                    url: "${ctx}/consult/remindUser/updateRemindUser?",
                    data: {
                            "openId":$("#openId").val(),
                            "csUserName":$("#csUserName").val()
                    },
                    dataType: "json",
                    success: function(data){
                        if(data.status == 'success'){
                            alert("删除成功！");
                            document.location.reload();
                        }else{
                            alert("删除失败！");
                        }
                    }
                });
            }
        }
        function searchSub(){
            $("#searchForm").attr("action","${ctx}/consult/remindUser/findRemindUser");
            $("#searchForm").submit();
            /*$.ajax({
                type: "GET",
                url: "${ctx}/consult/remindUser/findRemindUser?",
                data: {
                    "openId":$("#openId").val(),
                    "csUserName":$("#csUserName").val()
                },
                dataType: "json",
                success: function(data){
                    if(data.status == 'success'){
                        alert("查找成功！");
                        document.location.reload();
                    }else{
                        alert("查找失败！");
                    }
                }
            });*/
        }

       /* function addCsUser(url){
            $("#searchForm").attr("action",url);
            $("#searchForm").submit();

        }*/

        $(function(){
            $("#addUser").click(function(){
                /*alert($('#openId').val());
                alert($('#csUserName').val()+"");*/
                $.ajax({
                    type: "GET",
                    url: "${ctx}/consult/remindUser/addRemindUser?",
                    data: {
                            "openId":$('#openId').val(),
                            "csUserName":$('#csUserName').val()
                          },
                    dataType: "json",
                    contentType:"application/json",
                    success: function(data){
                        if(data.status == "success"){
                            alert(data.data);
                            document.location.reload();
                        }else if(data.status == "failure"){
                            alert(data.data);
                        }else{
                            alert(data.data);
                        }
                    }
                });
            });
        });

/*
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
*/

/*        function dataExport(type){
            href="${ctx}/consult/exportForm?type="+type;
            top.$.jBox.open('iframe:'+href,'导出统计表',$(top.document).width()-860,$(top.document).height()-330,{
                buttons:{"关闭":true},
                closed: function () {
                    $("#searchForm").attr("action","${ctx}/consult/consultDoctorList?");
                    $("#searchForm").submit();
                }
            });
        }*/
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="consult/remindUser/findRemindUser?">差评提醒</a></li>
</ul>
<form id="searchForm" method="get" class="breadcrumb form-search ">

    <sys:message content="${message}"/>
    <ul class="ul-form">
        <li><label>姓名：</label>
            <input id="csUserName"  name="csUserName"htmlEscape="false" maxlength="50" class="input-medium"/>
        </li>
        <li><label>openID：</label>
            <input id="openId"  name="openId"htmlEscape="false" maxlength="50" class="input-medium"/>
        </li>
        <li class="btns">
            <input class="btn btn-primary" type="button" onclick="searchSub()" value="查询" />
            <input id="addUser" class="btn btn-primary" type="button" value="确认添加" />
        </li>
    </ul>
</form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-bordered table-condensed" >
    <thead>
    <tr>
        <th>微信名</th>
        <th>OpenID</th>
        <th>添加时间</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <c:choose>
        <c:when test="${consultBadEvaluateRemindUserVoList != null && fn:length(consultBadEvaluateRemindUserVoList) > 0}">
            <c:forEach items="${consultBadEvaluateRemindUserVoList}" var="statisticsVo">
                <tr>
                    <td>${statisticsVo.csUserName}</td>
                    <td>${statisticsVo.openId}</td>
                    <td>
                        ${statisticsVo.createDate}
                    </td>
                    <td><a href="javascript:void(0)" onclick="deleteNoticeUser('${statisticsVo.openId}')">删除</a></td>
                </tr>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <tr>
                <td>暂时没有数据，请添加 ！！</td>
            </tr>
        </c:otherwise>
    </c:choose>
    </tbody>
</table>
</body>
</html>