<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/treetable.jsp" %>
<html>
<head>
    <title>疾病管理</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#treeTable").treeTable({expandLevel: 1}).show();
        });

        //添加疾病信息
        function addIllness() {
            location.href = '${ctx}/sys/illnessManage/illnessManagerImpl';
        }
        //删除选中的疾病
        function deleteSelect(){
            var tArray = "";
            for(var i=0;i<document.getElementsByName("selectCheckBox").length;i++){
                if(document.getElementsByName("selectCheckBox")[i].getElementsByTagName("input")[0].checked){
                    tArray = tArray + document.getElementsByName("selectCheckBox")[i].getElementsByTagName("input")[0].value +"-";
                }
            }
            $.ajax({
                type: "post",
                url: "${ctx}/sys/illnessManage/illnessManagerDeleteArray",
                data: {tArray: tArray},
                dataType: "json",
                success: function(){
                    location.href = '${ctx}/sys/illnessManage/illnessManage';
                }

            });
        }

        // 确认对话框
        function confirmx2(mess){
            top.$.jBox.confirm(mess,'系统提示',function(v,h,f){
                if(v=='ok'){
                    deleteSelect();
                }
            });
        }

    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/sys/doctor/doctorManage"><font color="#006400">医生列表</font></a></li>
</ul>
<form:form id="searchForm" modelAttribute="illnessVo" action="${ctx}/sys/illnessManage/illnessManage" method="post"
           class="breadcrumb form-search ">
    <%--<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>--%>
    <ul class="ul-form">
        <%--<li><label>一级疾病：</label>--%>
            <%--<form:select path="level_1" class="input-medium">--%>
                <%--<form:options items="${fns:getDictList('illness_type')}" itemLabel="label" itemValue="value"--%>
                              <%--htmlEscape="false"/>--%>
            <%--</form:select>--%>
        <%--</li>--%>
        <li><label>一级疾病：</label><form:input path="level_1" htmlEscape="false" class="input-medium"/></li>
        <li><label>二级疾病：</label><form:input path="level_2" htmlEscape="false" class="input-medium"/></li>
        <li class="btns" style="margin-left:20px;"><input id="btnSubmit" class="btn btn-primary" type="submit"
                                                          value="查询" onclick="return page();"/></li>
        <li>&nbsp;&nbsp;&nbsp;</li>
        <li>
        	<%--<shiro:hasPermission name="sys:illness:add">--%>
        		<input class="btn btn-info" type="button" onclick="addIllness();" value="添加疾病"/>
       		<%--</shiro:hasPermission>--%>
   		</li>
    </ul>

</form:form>

<sys:message content="${message}"/>

<table id="treeTable" class="table table-striped table-bordered table-condensed hide">
    <thead>
    <tr>
        <th class="sort-column name">一级疾病</th>
        <th align="">二级疾病</th>
        <%--<th>一级疾病显示顺序</th>--%>
        <th>二级疾病显示顺序</th>
        <th>操作</th>
        <%--confirmx('确认要删除已经选中的疾病吗？注意：所有医生与疾病的关联关系也会一并删除', deleteSelect())--%>
        <th><span onclick="confirmx2('确认要删除已经选中的疾病吗？注意：所有医生与疾病的关联关系也会一并删除')"><font color="red" onmouseover="this.style.cursor='pointer'">删除已选中</font></span></th>
    </tr>
    </thead>
    <tbody id="treeTableList">
    <c:forEach items="${page.list}" var="illnessVo">
        <tr id="${menu.id}" pId="${menu.parent.id ne '1'?menu.parent.id:'0'}">
            <td>${illnessVo.level_1}</td>
                <%--<a href="${ctx}/sys/doctor/doctorEdit?id=${doctorVo.id}">${doctorVo.doctorName}</a>--%>
            <td>${illnessVo.level_2}</td>
            <%--<td>${illnessVo.sort_level_1}</td>--%>
            <td>${illnessVo.sort}</td>
            <td>
                <%--<a href="${ctx}/sys/illnessManage/illnessManagerEdit?level_1=${illnessVo.level_1}&level_2=${illnessVo.level_2}&id=${illnessVo.id}&isEdit=no">修改</a>--%>
                <%--&nbsp;&nbsp;&nbsp;--%>
                <shiro:hasPermission name="sys:illness:delete">
                	<a href="${ctx}/sys/illnessManage/illnessManagerDelete?id=${illnessVo.id}"  onclick="return confirmx('确认要删除该疾病吗？注意：所有医生与该疾病的关联关系也会一并删除', this.href)">删除</a>
            	</shiro:hasPermission>
            </td>
            <td><div name="selectCheckBox"><input id="${illnessVo.id}" value="${illnessVo.id}" type="checkbox"></div></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>