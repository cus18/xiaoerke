<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/treetable.jsp" %>
<html>
<head>
    <title>热词管理</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#treeTable").treeTable({expandLevel: 1}).show();
            $("#inputForm").validate({
                submitHandler: function(form){
                    loading('正在提交，请稍等...');
                    form.submit();
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
        });
        //添加疾病信息
        function add_keyword() {
            location.href = "${ctx}/sys/keyword/addKeyword";
        }
        function success(){
            alertx("删除热词成功！");
        }

    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/sys/doctor/doctorManage"><font color="#006400">热词列表</font></a></li>
</ul>
<form:form id="inputForm" modelAttribute="SearchKeyword" action="${ctx}/sys/keyword/findAllkeyword" method="post"
           class="breadcrumb form-search ">

    <ul class="ul-form">
        <li><label>热词 ：</label><form:input path="keyword" htmlEscape="false" class="input-medium"/></li>

        <li class="btns" style="margin-left:20px;"><input id="btnSubmit" class="btn btn-primary" type="submit"
                                                          value="查询" onclick="return page();"/></li>
        <li>&nbsp;&nbsp;&nbsp;</li>
        <li><input class="btn btn-info" type="button" onclick="add_keyword()" value="添加热词"/></li>
    </ul>
</form:form>

<sys:message content="${message}"/>
<table id="treeTable" class="table table-striped table-bordered table-condensed hide">

    <thead>
    <tr>
        <th class="sort-column name">id</th>
        <th align="">热词</th>
        <th>设置关联关系</th>
        <th>操作</th>
    </thead>
    <tbody id="treeTableList">
    <c:forEach items="${list}" var="SearchKeyword">
        <tr>
            <td>${SearchKeyword.id}</td>
            <td>${SearchKeyword.keyword}</td>
            <td><a href="${ctx}/sys/keyword/keywordAndillnessRelation?id=${SearchKeyword.id}">查看关联的疾病</a></td>
            <td><a href="${ctx}/sys/keyword/deletekeyword?id=${SearchKeyword.id}" onclick="return confirmx('确定要把这个热词和它的关联全部删除吗？', this.href)" >删除</a></td>

        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>