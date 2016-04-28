<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>医生与医院的关联信息</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {
        	var location = "${DoctorHospitalRelationVo.location}";
        	var route = "${DoctorHospitalRelationVo.route}";//@edite 得良 ，麻烦Author写这么复杂的东西，一定要写好注释。。。。
	    	var locations = location.split("S|X");//将多个就诊地址拆分，如拆分后locations[0]就是一个就诊地址的location，包含了地址id、locaton、price、KindlyReminder

	    	var routes = route.split("S|X");//将多个就诊地址路线拆分

	    	for(var i=0;i<locations.length-1;i++){
                var locationArray = locations[i].split("S|S");// locationArray[0]：id、locationArray[1]：locaton、locationArray[2] ：price、KindlyReminder
	    		var route = routes[i].split("；");//（停车、地铁、公交） 、院内路线 、短信通知
	    		if(route!="null"){
		    		var temp1 = route[0].split("。");//停车、地铁、公交
                    //参数 locationArray[1]：location 地址 ，locationArray[0] locationid ，temp1[0] 停车，temp1[1] 地铁，temp1[2] 公交 ，route[1] 院内路线 ，route[2] 短信通知,locationArray[2] 价格,locationArray[3] 温馨提示
		    		AddElement("text",locationArray[1],locationArray[0],temp1[0],temp1[1],temp1[2],route[1],route[2],locationArray[2],locationArray[3]);
	    		}else{
	    			AddElement("text",locationArray[1],locationArray[0],"","","","","","","");
	    		}
	    	}
            $("#btnSubmit").click(function () {
                var hospitalName = $("#name").val();
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
        </script>
        <script type="text/javascript">
//参数 麻烦以后加注释
// mytype、locationArray[1]：location 地址 ，locationArray[0] locationid ，temp1[0] 停车，temp1[1] 地铁，temp1[2] 公交 ，
// route[1] 院内路线 ，route[2] 短信通知,locationArray[2] 价格,locationArray[3] 温馨提示
        function AddElement(mytype,lv,id,t1,t2,t3,t4,t5,t6,kindlyReminder){
			TemO=document.getElementById("addLocation"); 
			var newline= document.createElement("br"); 
			TemO.appendChild(newline);  
			var newline1= document.createElement("br"); 
			TemO.appendChild(newline1);  
			var lable = document.createElement("label");
			lable.innerText = "地  址：";
			TemO.appendChild(lable);
			var newInput1 = document.createElement("input");
            newInput1.style.width='300px';
			newInput1.type=mytype;
			newInput1.name="newloc";
			newInput1.value=lv;
			TemO.appendChild(newInput1);
			var idInput = document.createElement("input");  
			idInput.type="hidden";  
			idInput.name="idInput"; 
			idInput.value=id;
			TemO.appendChild(idInput);
			var del = document.createElement("a");
			del.innerText = "删除";
			del.onclick=function()
			{
				TemO.removeChild(newline);
				TemO.removeChild(newline1);
				TemO.removeChild(newInput1);
				TemO.removeChild(idInput);
				TemO.removeChild(newline2);
				TemO.removeChild(lable);
				TemO.removeChild(lable2);
				TemO.removeChild(newInput2);
				TemO.removeChild(newline3);
				TemO.removeChild(lable3);
				TemO.removeChild(newInput3);
				TemO.removeChild(newline4);
				TemO.removeChild(lable4);
				TemO.removeChild(newInput4);
				TemO.removeChild(newline5);
				TemO.removeChild(lable5);
				TemO.removeChild(newInput5);
				TemO.removeChild(newline6);
				TemO.removeChild(lable6);
				TemO.removeChild(newInput6);
				TemO.removeChild(del);
				
				TemO.removeChild(newline7);
				TemO.removeChild(lable7);
				TemO.removeChild(newInput7);

                TemO.removeChild(Reminder);
                TemO.removeChild(lable8);
                TemO.removeChild(reminderInput);
			}
			TemO.appendChild(del);  

            var newline2= document.createElement("br");
			TemO.appendChild(newline2); 
			var lable2 = document.createElement("label");
			lable2.innerText = "停车：";
			TemO.appendChild(lable2);
			var newInput2 = document.createElement("input");
            newInput2.style.width="600px"
			newInput2.type=mytype;  
			newInput2.name="stop"; 
			newInput2.value=t1;
			TemO.appendChild(newInput2);

            var newline3= document.createElement("br");
			TemO.appendChild(newline3); 
			var lable3 = document.createElement("label");
			lable3.innerText = "地铁：";
			TemO.appendChild(lable3);
			var newInput3 = document.createElement("input");
            newInput3.style.width = "600px";
			newInput3.type=mytype;  
			newInput3.name="train"; 
			newInput3.value=t2;
			TemO.appendChild(newInput3);

            var newline4= document.createElement("br");
			TemO.appendChild(newline4); 
            var lable4 = document.createElement("label");
			lable4.innerText = "公交：";
			TemO.appendChild(lable4);
			var newInput4 = document.createElement("input");
            newInput4.style.width = "1000px";
			newInput4.type=mytype;  
			newInput4.name="bus"; 
			newInput4.value=t3;
			TemO.appendChild(newInput4);

            var newline5= document.createElement("br");
			TemO.appendChild(newline5); 
			var lable5 = document.createElement("label");
			lable5.innerText = "院内路线：";
			TemO.appendChild(lable5);
			var newInput5 = document.createElement("input");
            newInput5.style.width = "500px";
            newInput5.type=mytype;
			newInput5.name="inroute"; 
			newInput5.value=t4;
			TemO.appendChild(newInput5);

            var newline6= document.createElement("br");
			TemO.appendChild(newline6); 
			var lable6 = document.createElement("label");
			lable6.innerText = "路线短信通知：";
			TemO.appendChild(lable6);
			var newInput6 = document.createElement("input");
            newInput6.style.width = "1300px";
            newInput6.type=mytype;
			newInput6.name="messageroute"; 
			newInput6.value=t5;
			TemO.appendChild(newInput6);
			
			var newline7= document.createElement("br"); 
			TemO.appendChild(newline7); 
			var lable7 = document.createElement("label");
			lable7.innerText = "挂号费：";
			TemO.appendChild(lable7);
			var newInput7 = document.createElement("input");
            newInput7.style.width = "100px";
			newInput7.type=mytype;  
			newInput7.name="price"; 
			newInput7.value=t6;
			TemO.appendChild(newInput7);

            var Reminder= document.createElement("br");
            TemO.appendChild(Reminder);
            var lable8 = document.createElement("label");
            lable8.innerText = "温馨提示：";
            TemO.appendChild(lable8);
            var reminderInput = document.createElement("input");
            reminderInput.style.width = "1000px";
            reminderInput.type=mytype;
            reminderInput.name="kindlyReminder";

            reminderInput.value=kindlyReminder;
            TemO.appendChild(reminderInput);
			
	} 
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li><a href="${ctx}/sys/doctor/doctorManage">医生列表</a></li>
    <li>
    <li><a href="${ctx}/sys/doctor/doctorEdit?id=${DoctorHospitalRelationVo.sysDoctorId}">医生修改</a></li>
    </li>
    <li class="active">
        <a href="#">医生与医院关系</a>
    </li>
</ul>
<br/>
<form:form id="inputForm" modelAttribute="DoctorHospitalRelationVo" action="${ctx}/sys/doctor/doctorHospitalRelationSave" method="post"
           class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="sysDoctorId"/>
    <form:hidden path="sysHospitalId"/>
    <form:hidden  path="location"/>
    <form:hidden  path="route"/>
    <sys:message content="${message}"/>
    <div class="control-group" >
        <label class="control-label">医生姓名:</label>
        <div class="controls">
            <form:input  path="doctorName" htmlEscape="false" />
            <%--<label class="control-label">${DoctorHospitalRelationVo.doctorName}</label>--%>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label"> 医院名称: </label>

        <div class="controls">
            <form:input path="hospitalName" htmlEscape="false" />
            <span class="help-inline"><font color="red">*</font></span>
        </div>
    </div>

    </div>
    <div class="control-group">

        <label class="control-label">与医院关系:</label>
        <div class="controls">
            <form:select path="relationType"  style="width: 100px"  htmlEscape="false" maxlength="50">
                <form:option value="0">0</form:option>
                <form:option value="1">1</form:option>
            </form:select>
                        <%--<form:input path="relationType" htmlEscape="false" maxlength="100"/>--%>
            <span class="help-inline">医生和此医院的关系（1表示归属关系，0表示仅在此坐诊执业，2表示此医院为合作机构）</span>
        </div>

    </div>
    <div class="control-group">
        <label class="control-label"> 一级科室: </label>

        <div class="controls">
            <form:input path="departmentLevel1" htmlEscape="false" />
        </div>
    </div>

    <div class="control-group">
        <label class="control-label"> 二级科室: </label>
        <div class="controls">
            <form:input path="departmentLevel2" htmlEscape="false" />

        </div>
    </div>

    <div class="control-group">
        <label class="control-label"> 联系人: </label>
        <div class="controls">
            <form:input path="contactPerson" htmlEscape="false" />

        </div>
    </div>
    <div class="control-group">
        <label class="control-label"> 联系人电话: </label>
        <div class="controls">
            <form:input path="contactPersonPhone" htmlEscape="false"  class="input-large"/>
        </div>
    </div>
    <%--<div class="control-group">--%>
        <%--<label class="control-label"> 温馨提示: </label>--%>
        <%--<div class="controls">--%>
            <%--<form:input path="kindlyReminder" htmlEscape="false"  class="input-large"/>--%>
            <%--<span class="help-inline">非必填</span>--%>
        <%--</div>--%>
    <%--</div>--%>
    <div class="control-group">
        <label class="control-label"> 就诊地址: </label>
        <div id ="addLocation" class="controls">
        	<a href="javascript:AddElement('text','','','','','','','','','')">添加</a>
        </div>
    </div>
    <div class="form-actions">
            <input id="btnSubmit" class="btn btn-success" type="submit" value="保存" />&nbsp;
            <a href="${ctx}/sys/doctor/doctorHospitalRelationDelete?doctorHospitalRelationId=${DoctorHospitalRelationVo.id}" onclick="return confirmx('确认要删除此关联关系吗？', this.href)">
                <input class="btn btn-danger" type="button" value="删除"/>&nbsp;
            </a>
        <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
    </div>
</form:form>
</body>
</html>