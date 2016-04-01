<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>医院信息</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#btnSubmit").click(function () {
                var position = $("#position").val();
                var details = $("#details").val();
                var hospitalName = $("#name").val();
                var cityName = $("#cityName").val();
                var medicalProcess = $("#medicalProcess").val();

                if (hospitalName == "") {
                    alertx("医院名称不能为空！");
                    return false;
                }
                if (position == "") {
                    alertx("医院的地理位置不能为空！");
                    return false;
                }
                if (details == "") {
                    alertx("医院的详细信息描述不能为空！");
                    return false;
                }
                if (cityName == "") {
                    alertx("医生姓名不能为空！");
                    return false;
                }
                if (medicalProcess == "") {
                    alertx("就诊流程不能为空！");
                    return false;
                }


                var selectValue = jQuery("#select  option:selected").text();

                if (selectValue == "合作机构") {
                    var contactName = $("#contactName").val();
                    var contactPhone = $("#contactPhone").val();
//                    var specialDiscount = $("#specialDiscount").val();
//                    var chargeStandard = $("#chargeStandard").val();
//                    var medicalExamination = $("#medicalExamination").val();
                    if (contactName == "") {
                        alertx("合作机构联系人不能为空！");
                        return false;
                    }
                    if (contactPhone == "") {
                        alertx("合作机构联系人电话不能为空！");
                        return false;
                    }
//                    if (specialDiscount == "") {
//                        alertx("特别优惠不能为空！");
//                        return false;
//                    }
//                    if (chargeStandard == "") {
//                        alertx("收费标准不能为空！");
//                        return false;
//                    }
//                    if (medicalExamination == "") {
//                        alertx("开药及检查不能为空！");
//                        return false;
//                    }

                }
            });
            $("#inputForm").validate({
                submitHandler: function (form) {
                    loading('正在提交，请稍等...');
                    form.submit();
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
        });

        function showdiv(contentid) {

            var selectValue = jQuery("#select  option:selected").text();

            var contentid = document.getElementById(contentid);

            if (selectValue == "公立医院") {
                contentid.style.display = "none";
            } else if(selectValue == "合作机构"){
                contentid.style.display = "block";
            }

        }

    </script>

    <style type="text/css">
        #contentid {
            margin-top: 1px;
            margin-left: 180px;
            margin-bottom: 20px;
            width: 500px;
            border: 1px solid #CCC;
            padding: 5px;
        }

        .none {
            display: none;
        }
    </style>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/sys/hospital/hospitalDataImp">医院信息录入</a></li>
</ul>
<br/>


<form:form id="inputForm" modelAttribute="hospitalVo" action="${ctx}/sys/hospital/hospitalDataImp" method="post"
           class="form-horizontal">
    <%--<form:hidden path="id"/>--%>
    <sys:message content="${message}"/>
    <div class="control-group">
        <label class="control-label"><span class="icon-home"></span>&nbsp;&nbsp;医院名称</label>

        <div class="controls">
            <input id="name" name="name" type="text" value="" class="input-xlarge"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>

    <div>
        <label class="control-label"><span class="icon-home"></span>&nbsp;&nbsp;医院性质</label>

        <div class="controls">
            <select id="select" name='select' onChange="showdiv('contentid')">
                <option value='1'>公立医院</option>
                <option value='2'>合作机构</option>
            </select>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>

    </div>
    <br>

    <div id="contentid" class="none">

        <label class="control-label">合作机构联系人姓名:</label>

        <div class="controls">
            <input id="contactName" name="contactName" type="text" value="" class="input-xlarge"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
        <br>
        <label class="control-label">合作机构联系人电话:</label>

        <div class="controls">
            <input id="contactPhone" name="contactPhone" type="text" value="" class="input-xlarge"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
        <%--<br>--%>

        <%--<label class="control-label">特别优惠:</label>--%>
        <%--<div class="controls">--%>
            <%--<input id="specialDiscount" name="specialDiscount" type="text" value="" class="input-xxlarge"/>--%>
            <%--<span class="help-inline"><font color="red">* 如有换行操作，请以中文；区分</font> </span>--%>
        <%--</div>--%>
        <%--<br>--%>

        <%--<label class="control-label">收费标准:</label>--%>
        <%--<div class="controls">--%>
            <%--<input id="chargeStandard" name="chargeStandard" type="text" value="" class="input-xxlarge"/>--%>
            <%--<span class="help-inline"><font color="red">* 如有换行操作，请以中文；区分</font> </span>--%>
        <%--</div>--%>
        <%--<br>--%>

        <%--<label class="control-label">开药及检查:</label>--%>
        <%--<div>--%>
            <%--&nbsp;&nbsp;&nbsp;&nbsp;--%>
            <%--<input id="medicalExamination" name="medicalExamination" type="text" value=""  class="input-xxlarge"/>--%>
            <%--<span class="help-inline"><font color="red">* 如有换行操作，请以中文；区分</font> </span>--%>
        <%--</div>--%>

    </div>


    <div class="control-group">
        <label class="control-label"><span class="icon-retweet"></span>&nbsp;&nbsp;医院地理位置:</label>

        <div class="controls">
            <form:textarea path="position" htmlEscape="false" rows="1" class="input-xxlarge"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">医院的详细信息描述:</label>

        <div class="controls">

            <form:textarea path="details" htmlEscape="false" rows="1" class="input-xxlarge"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>

    <div class="control-group">
        <label class="control-label">所在城市:</label>

        <div class="controls">
            <form:select path="cityName" class="input-medium">
                <form:options items="${fns:getDictList('sys_city')}" itemLabel="label" itemValue="value"
                              htmlEscape="false"/>
            </form:select>
        </div>
    </div>


    <div class="control-group">
        <label class="control-label">就诊流程:</label>

        <div class="controls">
            <form:textarea path="medicalProcess" htmlEscape="false" rows="1" class="input-xxlarge"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>

    <div class="form-actions">
        <input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
    </div>
</form:form>
</body>
</html>