<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
    <head>
        <title>用户反馈列表</title>
        <meta name="decorator" content="default"/>
        <script type="text/javascript">
            $(document).ready(function () {
            });
            function page(n, s) {
                if (n) $("#pageNo").val(n);
                if (s) $("#pageSize").val(s);
                $("#searchForm").attr("action", "${ctx}/sys/UserAction/userFeedbackList?");
                $("#searchForm").submit();
                return false;
            }
            function searchSub() {
                $("#searchForm").attr("action", "${ctx}/sys/UserAction/userFeedbackList?");
                $("#searchForm").submit();
            }

            function changeStatus(id){
                $.ajax({
                    type: "post",
                    url: "${ctx}/sys/UserAction/changeSolve",
                    data: {id:id,solve:$("#solve"+id).val()},
                    dataType: "json",
                    success: function(data){
                        if("suc"==data.result){
                            alertx("操作成功！");
                        }else{
                            alertx("操作失败！");
                        }
                    }
                });
            }
        </script>
    </head>
    <body>
        <ul class="nav nav-tabs">
            <li class="active">用户反馈</li>
        </ul>
        <form:form id="searchForm" modelAttribute="vo" action="${ctx}/sys/UserAction/userFeedbackList?" method="post"
                   class="breadcrumb form-search ">
            <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
            <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
            <sys:message content="${message}"/>
            <ul class="ul-form">
                <li><label>openid：</label>
                    <form:input path="openid" htmlEscape="false" maxlength="50" class="input-medium"/>
                </li>
                <li class="btns">
                    <input class="btn btn-primary" type="button" onclick="searchSub()" value="查询"/>
                </li>
                <li class="clearfix"></li>
            </ul>
        </form:form>
        <sys:message content="${message}"/>
        <table id="contentTable" class="table table-bordered table-condensed">
            <thead>
            <tr>
                <th>openid</th>
                <th>时间</th>
                <th>内容</th>
                <th>类型</th>
                <th>是否解决</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.list}" var="u">
                <tr>
                    <td>${u.openid}</td>
                    <td><fmt:formatDate value ="${u.createTime}" pattern="yyyy-MM-dd HH:mm" /></td>
                    <td>${u.content}</td>
                    <td>${u.type}</td>
                    <td>
                        <select id="solve${u.id}" class="txt required" style="width:100px;" onchange="changeStatus(${u.id})">
                            <option value="未解决" <c:if test="${u.solve eq '未解决'}">selected="selected"</c:if>>未解决</option>
                            <option value="解决中" <c:if test="${u.solve eq '解决中'}">selected="selected"</c:if>>解决中</option>
                            <option value="已解决" <c:if test="${u.solve eq '已解决'}">selected="selected"</c:if>>已解决</option>
                        </select>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="pagination">${page}</div>
    </body>
</html>