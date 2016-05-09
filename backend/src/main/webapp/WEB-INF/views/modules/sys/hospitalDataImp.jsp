<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>医院信息</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {
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


<form:form id="inputForm" modelAttribute="contactVo" action="${ctx}/sys/hospital/hospitalDataImp" method="post"
           class="form-horizontal">
    <%--<form:hidden path="id"/>--%>
    <sys:message content="${message}"/>
    <div class="control-group">
        <label class="control-label"><span class="icon-home"></span>&nbsp;&nbsp;医院名称</label>

        <div class="controls">
            <input id="name" name="name" type="text" value="" class="required"/>
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
        </div>

    </div>
    <br>

    <div id="contentid" class="none">

        <label class="control-label">合作机构联系人姓名:</label>

        <div class="controls">
            <input name="contactName" type="text" value="" class="required"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
        <br>
        <label class="control-label">合作机构联系人电话:</label>

        <div class="controls">
            <input id="contactPhone" name="contactPhone" type="text" value="" class="required"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
        <br>

        <label class="control-label">医院图片:</label>
        <div class="controls">
            <input type="hidden" id="hospitalPic" name="hospitalPic" value="${article.hospitalPic}" />
            <sys:ckfinder input="hospitalPic" type="thumb" uploadPath="/sys/hospital" selectMultiple="false"/>
        </div>
        <br/>

        <label class="control-label"> 宝大夫特别优惠:</label>
        <hr/>

        <label class="control-label">费用减免:</label>
            <div class="controls">
                <input id="costReduction" name="costReduction" type="text" value="" class="input-xlarge"/>
            </div>
        <br/>

        <label class="control-label">绿色通道:</label>
            <div class="controls">
                <input id="greenChannel" name="greenChannel" type="text" value="" class="input-xlarge"/>
            </div>
        <br>

        <label class="control-label">限价标准:</label>
            <div class="controls">
                <input id="limitStandard" name="limitStandard" type="text" value="" class="input-xlarge"/>
                <span class="help-inline"><font color="red">例：单次就诊最高花费500元（只限二类疾病）</font> </span>
            </div>
        <br>

        <label class="control-label">1）限价范围:</label>
        <div class="controls">
            <input id="limitRange" name="limitRange" type="text" value="" class="input-xlarge"/>
            <span class="help-inline"><font color="red">例：① 根管治疗1颗    ② 树脂填充2颗</font> </span>
        </div>
        <br>

        <label class="control-label">2）限价疾病:</label>
        <div class="controls">
            <textarea name="limitDisease" rows="4" maxlength="200" style="width:270px;"></textarea>
            <span class="help-inline"><font color="red">注：录入内容时，小标题与内容之间用  “；” 分隔</font> </span>
        </div>
        <br>

        <label class="control-label">开药及检查:</label>
        <div class="controls">
            <select name="drugInspection" class="txt required" style="width:100px;">
                <option value="1">开药及检查</option>
                <option value="0">检查</option>
            </select>
        </div>
        <br>

        <div>
            <label class="control-label">中药:</label>
            <div class="controls">
                <input id="chineseMedicine" name="chineseMedicine" type="text" value="" class="input-xlarge"/>
            </div>
            <br>

            <label class="control-label">西药:</label>
            <div class="controls">
                <input id="westernMedicine" name="westernMedicine" type="text" value="" class="input-xlarge"/>
            </div>
            <br>
        </div>

        <label class="control-label">检查项目:</label>
        <div class="controls">
            <input id="inspectionItems" name="inspectionItems" type="text" value="" class="required"/>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
        <br>

        <label class="control-label">收费标准:</label>
        <hr>

        <label class="control-label">药费:</label>
        <div class="controls">
            <input id="medicineFee" name="medicineFee" type="text" value="" class="input-xlarge"/>
        </div>
        <br>

        <label class="control-label">检查费:</label>
        <div class="controls">
            <input id="inspectionFee" name="inspectionFee" type="text" value="" class="input-xlarge"/>
        </div>
        <br>

        <label class="control-label">诊疗项目:</label>
        <div class="controls">
            <input type="hidden" id="clinicItemsPic" name="clinicItemsPic" value="${article.imageSrc}" />
            <sys:ckfinder input="clinicItemsPic" type="thumb" uploadPath="/sys/hospital" selectMultiple="false"/>
            <textarea name="clinicItems" rows="4" maxlength="200" style="width:270px;"></textarea>
        </div>
    </div>


    <div class="control-group">
        <label class="control-label"><span class="icon-retweet"></span>&nbsp;&nbsp;医院地理位置:</label>

        <div class="controls">
            <textarea name="position" rows="4" maxlength="200" class="required" style="width:270px;"></textarea>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">医院的详细信息描述:</label>

        <div class="controls">
            <textarea name="details" rows="4" maxlength="200" class="required" style="width:270px;"></textarea>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>

    <div class="control-group">
        <label class="control-label">所在城市:</label>

        <div class="controls">
            <select name="cityName" class="input-medium">
                <c:forEach items="${fns:getDictList('sys_city')}" var="city">
                    <option value="${city.value}">${city.label}</option>
                </c:forEach>
            </select>
        </div>
    </div>


    <div class="control-group">
        <label class="control-label">就诊流程:</label>

        <div class="controls">
            <textarea name="medicalProcess" rows="4" maxlength="200" class="required" style="width:270px;"></textarea>
            <span class="help-inline"><font color="red">*</font> </span>
        </div>
    </div>

    <div class="form-actions">
        <input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
    </div>
</form:form>
</body>
</html>