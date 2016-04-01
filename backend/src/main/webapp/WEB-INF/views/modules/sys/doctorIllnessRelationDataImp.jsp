<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>医生与疾病管理关系</title>
    <meta name="decorator" content="default"/>
    <meta http-equiv="Content-Type" content="text/html charset=ISO-8859-1">
    <script type="text/javascript">
        $(document).ready(function () {
            $("select").change(function () {

                var doctorNameCach = document.getElementById('doctorName');
                alertx(doctorNameCach);
//                var level_1=jQuery("#level_1  option:selected").text();
                <%--location.href="${ctx}/sys/doctorIllnessRelation/doctorIllnessRelationDataImp?seachByLevel_1="+level_1+"&doctorName="+doctorNameCach;--%>
            });

            $("#btnSubmit").click(function () {
                var doctorName = $("#doctorNameId").val();
                if (doctorName == "" || doctorName == null) {
                    alertx("医生不能为空！");
                    return false;
                }
                var level_2 = $("#level_2").val();
                if (level_2 == "") {
                    alertx("二类疾病不能为空！");
                    return false;
                }
            });

            $("#inputForm").validate({
                submitHandler: function (form) {
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
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/sys/doctorIllnessRelation/doctorIllnessRelationDataImp">医生与疾病关系信息录入</a></li>
</ul>
<br/>
<form:form id="inputForm" modelAttribute="doctorIllnessRelationVo"
           action="${ctx}/sys/doctorIllnessRelation/doctorIllnessRelationDataImp" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <input id="level_cache" type="hidden">
    <input id="doctorNameCach" value="${doctorIllnessRelationVo.doctorName}" type="hidden">
    <sys:message content="${message}"/>
    <div class="control-group">
        <label class="control-label">医生:</label>&nbsp;&nbsp;&nbsp;
        <sys:treeselect id="doctorName" name="doctorName" value="${doctorIllnessRelationVo.doctorName}"
                        labelName="doctorName" labelValue="${doctorIllnessRelationVo.doctorName}"
                        title="医生" url="/sys/doctor/treedoctorData" cssClass="required"/>
        <span class="help-inline">没有医生？去添加医生吧！ </span>
    </div>

    <div class="control-group">
        <label class="control-label">一类疾病:</label>

        <div class="controls">
            <form:select path="level_1" class="input-medium">
                <form:options items="${fns:getDictList('illness_type')}" itemLabel="label" itemValue="value"
                              htmlEscape="false"/>
            </form:select>
        </div>
    </div>

    <div class="control-group">
        <label class="control-label">二级疾病:</label>&nbsp;&nbsp;&nbsp;
        <sys:treeselect id="level_2" name="level_2" value="${doctorIllnessRelationVo.level_2}" labelName="level_2"
                        labelValue="${doctorIllnessRelationVo.level_2}"
                        title="二级疾病"
                        url="/sys/doctorIllnessRelation/treeLevel_2Data?level_1=${doctorIllnessRelationVo.level_1}"
                        cssClass="required"/>
        <span class="help-inline">找不到？记得往疾病库里添加疾病哦~ </span>
    </div>
    <div class="form-actions">
        <input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
    </div>
</form:form>
</body>

</html>