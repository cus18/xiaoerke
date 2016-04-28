<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>医生与医院关系</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function () {
			$("#btnSubmit").click(function () {
//				var phone = $("#phone").val();
				var hospitalName = $("#hospitalName").val();

				if (phone == "") {
					alertx("医生手机号不能为空！");
					return false;
				}
				if (hospitalName == "") {
					alertx("医院不能为空！");
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
		
		function AddElement(mytype,lv,id,t1,t2,t3,t4,t5,t6){ 
			TemO=document.getElementById("addLocation"); 
			var newline= document.createElement("br"); 
			TemO.appendChild(newline);  
			var newline1= document.createElement("br"); 
			TemO.appendChild(newline1);  
			var lable = document.createElement("label");
			lable.innerText = "地  址：";
			TemO.appendChild(lable);
			var newInput1 = document.createElement("input");  
			newInput1.type=mytype;  
			newInput1.name="newloc"; 
			newInput1.value=lv;
			TemO.appendChild(newInput1);
			var del = document.createElement("a");
			del.innerText = "删除";
			del.onclick=function()
			{
				TemO.removeChild(newline);
				TemO.removeChild(newline1);
				TemO.removeChild(newInput1);
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
				TemO.removeChild(newline7);
				TemO.removeChild(lable7);
				TemO.removeChild(newInput7);
				TemO.removeChild(newline6);
				TemO.removeChild(lable6);
				TemO.removeChild(newInput6);
				TemO.removeChild(del);
			}
			TemO.appendChild(del);  
			var newline2= document.createElement("br"); 
			TemO.appendChild(newline2); 
			var lable2 = document.createElement("label");
			lable2.innerText = "停车：";
			TemO.appendChild(lable2);
			var newInput2 = document.createElement("input");  
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
			newInput7.type=mytype;  
			newInput7.name="price"; 
			newInput7.value=t6;
			TemO.appendChild(newInput7);
			
	} 
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/doctor/doctorHospitalRelationDataImp">医生与医院关系信息录入</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="doctorHospitalRelationVo" action="${ctx}/sys/doctor/doctorHospitalRelationDataImp" method="post" class="form-horizontal">
		<%--<form:hidden path="id"/>--%>
		<sys:message content="${message}"/>

		<div class="control-group" style="${not empty doctorHospitalRelationVo.doctorName?'':'display:none;'}">
			<label class="control-label">医生姓名:</label>
			<div class="controls">
				<form:input path="doctorName" htmlEscape="false"  readOnly="readOnly"/>
			</div>
		</div>
		
		<div class="control-group" style="${not empty doctorHospitalRelationVo.doctorName?'display:none;':''}">
			<label class="control-label">医生电话:</label>
			<div class="controls">
				<form:input path="phone" htmlEscape="false"  />
				<span class="help-inline"><font color="red">*恢常重要！！！</font> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">医院名称:</label>
			<div class="controls">
			<sys:treeselect  id="hospitalName" name="hospitalName" value="${doctorHospitalRelationVo.hospitalName}" labelName="hospitalName" labelValue="${doctorHospitalRelationVo.hospitalName}"
			title="医院" url="/sys/hospital/treeHospitalData" cssClass="required"/>
				<span class="help-inline"><font color="red">*</font>没有？先去添加医院吧！ </span>
			</div>
		</div>

		</div>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">关系:</label>--%>

			<%--<div class="controls">--%>
				<%--<form:input path="relationType" htmlEscape="false" maxlength="10" class="required"/>--%>
				<%--<span class="help-inline">医生和此医院的关系（1表示归属关系，0表示仅在此坐诊执业）</span>--%>
			<%--</div>--%>
		<%--</div>--%>

		<div class="control-group">
			<label class="control-label">与医院关系:</label>
			<div class="controls">
				<form:select path="relationType" style="width: 100px"  name="relationType" htmlEscape="false" maxlength="50">
					<option value="0">0</option>
					<option value="1">1</option>
				</form:select>
					<%--<form:input path="relationType" htmlEscape="false" maxlength="100"/>--%>
				<span class="help-inline">医生和此医院的关系（1表示归属关系，0表示仅在此坐诊执业，2表示此医院为合作机构）</span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">一级科室:</label>

			<div class="controls">
				<form:input path="departmentLevel1" htmlEscape="false" />
				<span class="help-inline"></span>
			</div>
		</div>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">二级科室:</label>--%>

			<%--<div class="controls">--%>
				<%--<form:input path="departmentLevel2" htmlEscape="false" maxlength="60" class="required"/>--%>

			<%--</div>--%>
		<%--</div>--%>

		<%--<div class="control-group">--%>
			<%--<label class="control-label">一级科室:</label>--%>
			<%--<div class="controls">--%>
				<%--<form:select path="departmentLevel1" class="input-medium">--%>
					<%--<form:options items="${fns:getDictList('primaryDepartment_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>--%>
				<%--</form:select>--%>
				<%--<span class="help-inline">找不到？找管理员哦~</span>--%>
			<%--</div>--%>
		<%--</div>--%>

		<div class="control-group">
			<label class="control-label">二级科室:</label>
			<div class="controls">
				<form:input path="departmentLevel2" htmlEscape="false" />

			</div>
		</div>


		<div class="control-group">
			<label class="control-label">商务部负责人:</label>

			<div class="controls">
				<form:input path="contactPerson" htmlEscape="false" />

			</div>
		</div>
		<div class="control-group">
			<label class="control-label">负责人电话:</label>

			<div class="controls">
				<form:input path="contactPersonPhone" htmlEscape="false" class="input-large"/>
			</div>
		</div>
		<%-- <div class="control-group">
			<label class="control-label">就诊地址:</label>
			<div class="controls">
				<form:input path="placeDetail" htmlEscape="false" maxlength="60" class="input-xxlarge"/>
			</div>
		</div> --%>
		
		<div class="control-group">
	        <label class="control-label"> 就诊地址: </label>
	        <div id ="addLocation" class="controls">
	        	<a href="javascript:AddElement('text','','','','','','','','')">添加</a> 
	        </div>
	    </div>
    
		<div class="form-actions">
			<shiro:hasPermission name="sys:user:edit"><input id="btnSubmit" class="btn btn-success" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-inverse" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>