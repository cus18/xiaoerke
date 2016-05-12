<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/treetable.jsp" %>
<html>
<!--@Author 得良-->
<head>
    <title>医生管理</title>
    <meta name="decorator" content="default"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {

            var doctorId = $("input:hidden[name='doctorId']").val();
            $.ajax({
                type: "post",
                url: "${ctx}/sys/doctorIllnessRelation/DoctorIllnessRelationData",
                data: {doctorId: doctorId},
                dataType: "json",
                dataType: "json",
                success: function (data) {
                    console.log(data);
                    for (var i = 0; i < data.list.length; i++) {
                        document.getElementById(data.list[i]).checked = true;
                    }
                }
            });

            $("#treeTable").treeTable({expandLevel: 1}).show();


        });
        <!--全选-->
        function allSelect(obj, level_1) {
            var oDiv = document.getElementById(level_1);
            var aCh = oDiv.getElementsByTagName('input');
            for (var i = 0; i < aCh.length; i++) {
                if (obj.checked == true)
                    aCh[i].checked = true;
                else
                    aCh[i].checked = false;
            }
        }
        <!--不选-->
        function allNoSelect(level_1) {
            var oDiv = document.getElementById(level_1);
            var aCh = oDiv.getElementsByTagName('input');
            for (var i = 0; i < aCh.length; i++) {
                aCh[i].checked = false;
            }
        }
        <!--表单提交，发送数据-->
        function sub() {

            var tArray = "";   //先声明一维
            var count_level1 = document.getElementsByName("level_1_list").length;//一级疾病长度

            for (var i = 0; i < count_level1; i++) {

                for (var j = 0; j < document.getElementsByName("level_1_list")[i].getElementsByTagName("input").length; j++) {

                    if (document.getElementsByName("level_1_list")[i].getElementsByTagName("input")[j].checked == true) {

                        //处理传入后台的数据 格式为 : level_1,level_2
                        var level_1 = "";
                        var str = new Array();
                        str = document.getElementsByName("level_1_list")[i].id.split("-");
                        for (var k = 0; k < str.length; k++) {
                            level_1 = str[str.length - 1];
                        }

                        tArray = tArray + level_1 + "-" + document.getElementsByName("level_1_list")[i].getElementsByTagName("input")[j].value + ",";
                    }
                }
            }
            var doctorId = $("input:hidden[name='doctorId']").val();
            alert(doctorId);
            //发送数据
            $.ajax({
                type: "post",
                url: "${ctx}/sys/doctorIllnessRelation/DoctorIllnessRelationData",
                data: {tArray: tArray, doctorId: doctorId},
                dataType: "json",
                success: function () {
                    alertx("成功！");
                }

            });
        }

    </script>
</head>
<>
<%--action="${ctx}/sys/doctorIllnessRelation/DoctorIllnessRelationData"--%>
<form:form id="inputForm" modelAttribute="doctorIllnessRelationVo" method="post">
    <sys:message content="${message}"/>

    <input type="hidden" id="doctorId" name="doctorId" value=${doctorIllnessRelationVo.sys_doctor_id}>

    <table id="treeTable" border="0" class="table table-striped table-bordered table-condensed hide">
        <tbody id="treeTableList">
        <c:forEach items="${page.list}" var="illnessVo">

            <tr>
                <td>
                    <div style="width: 1150px;">
                        <div class="accordion-group">
                            <!--显示所有的一级疾病-->
                            <div style="position: relative;">
                                <span class="icon-book"></span>

                                <a data-toggle="collapse" data-parent="#accordion2"
                                   href="#-${illnessVo.id}-${illnessVo.level_1}">
                                    <font color="blue" size="2">${illnessVo.level_1}</font>
                                </a>

                                <label style="position:absolute;left:159px;top:-1px;">
                                    <input id="123flag${illnessVo.id}" name=${illnessVo.id}
                                            onclick="allSelect(this,'${illnessVo.level_1}');" type="checkbox">
                                    <label>全选</label>
                                </label>

                                <a data-toggle="collapse" data-parent="#accordion2"
                                   style="float:right;margin-right: 15px;"
                                   href="#-${illnessVo.id}-${illnessVo.level_1}">
                                    <i class="icon-hand-down am-icon-lg"></i>
                                </a>

                            </div>
                            <!--显示所有的二级疾病-->
                            <div id="-${illnessVo.id}-${illnessVo.level_1}" name="level_1_list"
                                 class="accordion-body collapse">
                                <div class="accordion-inner" id="seach_level2">
                                    <div class="controls" id="${illnessVo.level_1}">
                                        <c:forEach items="${illnessVo.list}" var="illnessLevel2">
                                            <span style="width:18%;display:inline-block;">
                                                <input id=${illnessLevel2.id} value="${illnessLevel2.level_2}"
                                                       name=${illnessLevel2.level_1} type="checkbox">
                                                <label for=${illnessLevel2.id}>${illnessLevel2.level_2}</label>
                                            </span>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>


    <div class="pagination">${page}</div>
    <div class="form-actions">
        <input id="btnSubmit" class="btn btn-success" type="button" onclick="sub();" value="保 存"/>&nbsp;
        <input id="btnCancel" class="btn btn-inverse" type="button" value="返 回" onclick="history.go(-1)"/>
    </div>
</form:form>
</body>
</html>