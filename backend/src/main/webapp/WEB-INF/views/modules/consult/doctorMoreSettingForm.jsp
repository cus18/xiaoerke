<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>预约</title>
	<style type="text/css">
		.datesun LI {
			BORDER-RIGHT: #FFD1A4 1px solid;
			DISPLAY: block;
			FLOAT: left;
			HEIGHT: 25px;
			font-family:Arial, Helvetica, sans-serif;
			font-size:12px;
		}
		.datesun LI A {
			PADDING:1px 15px 0;
			DISPLAY: block;
			FONT-WEIGHT: none;
			COLOR: #562505;
			LINE-HEIGHT: 25px;
			TEXT-DECORATION: none;
		}
		.datesun LI A:hover {
			COLOR:#562505;
			BACKGROUND-COLOR: #FFD1A4;
			TEXT-DECORATION: none;
		}

		.locationsun LI {
			BORDER-RIGHT: #FFD1A4 1px solid;
			DISPLAY: block;
			FLOAT: left;
			HEIGHT: 25px;
			font-family:Arial, Helvetica, sans-serif;
			font-size:12px;
		}
		.locationsun LI A {
			PADDING:1px 15px 0;
			DISPLAY: block;
			FONT-WEIGHT: none;
			COLOR: #562505;
			LINE-HEIGHT: 25px;
			TEXT-DECORATION: none;
		}
		.locationsun LI A:hover {
			COLOR:#562505;
			BACKGROUND-COLOR: #FFD1A4;
			TEXT-DECORATION: none;
		}
		.current{
			BACKGROUND-COLOR: #FFD1A4;
		}
		.locationsun li#date{
			color:#FFD1A4;
			PADDING:2px 15px 0;
		}
	</style>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			$("#gender").val('${doctor.gender}');
			$("#type").val('${doctor.type}');
			$("input[type='radio'][name=grabSession][value='${doctor.grabSession}']").attr("checked",true);
			$("input[type='radio'][name=starDoctor][value='${doctor.starDoctor}']").attr("checked",true);
			$("input[type='radio'][name=sendMessage][value='${doctor.sendMessage}']").attr("checked",true);
			$("input[type='radio'][name=receiveDifferentialNotification][value='${doctor.receiveDifferentialNotification}']").attr("checked",true);
			$("#imgSubmit").click(function () {
				var file = $("#picfile").val();
				if (file == "") {
					alertx("请添加医生头像！");
					return false;
				}
			});

			var checkeds = $("#timeid").val();
			var checkArray =checkeds.split(",");
			var checkBoxAll = $("input[name='timeList']");
			for(var i=0;i<checkArray.length;i++){
				$.each(checkBoxAll,function(j,checkbox){
					//获取复选框的value属性
					var checkValue=$(checkbox).val();
					if(checkArray[i]==checkValue){
						$(checkbox).attr("checked",true);
					}
				})
			}
			$("input[name='isPhoneConsult']").each(function(){
			if (this.value==$("#isPhoneConsult").val()){
				this.checked = true;
			}
			});
		});

		function savePersonalData(){
			if($("#hospital").val()==""){
				alertx("请填写医院！");
				return;
			}
			if($("#department").val()==""){
				alertx("请填写科室！");
				return;
			}
			if($("#skill").val()==""){
				alertx("请填写擅长！");
				return;
			}

			var timeList=document.getElementsByName("timeList");
			var timeid = "";
			for(var i=0;i<timeList.length;i++){
				if(timeList[i].checked==true){
					timeid +=timeList[i].value+",";
				}
			}

			var isPhoneConsult = $("input[name='isPhoneConsult']:checked").val();

			$.ajax({
	             type: "post",
	             url: "${ctx}/consult/doctorInfoOper",
	             data: {userId:"${user.id}",password:$("#password").val(),name:"${user.name}",gender:$("#gender").val(),type:$("#type").val(),title:$("#title").val(),hospital:$("#hospital").val(),department:$("#department").val(),practitionerCertificateNo:$("#practitionerCertificateNo").val(),skill:$("#skill").val(),description:$("#description").val(),sort:$("#sort").val(),starDoctor:$('input:radio[name="starDoctor"]:checked').val(),microMallAddress:$("#microMallAddress").val(),"nonRealPayPrice":$("#nonRealPayPrice").val(),"timeid":timeid,"isPhoneConsult":isPhoneConsult,"phonePayPrice":$("#phonePayPrice").val()},
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
		function changeDiv(obj,flag){
			var a=document.getElementById("datesun").getElementsByTagName("li");
			for(var i=0;i<a.length;i++){
				a[i].className="";
			}
			obj.className="active";
			if(flag=='consultRecords'){
				$("#consultRecords").show();
				$("#personalData").hide();
				$("#permissionSettings").hide();
			}
			if(flag=='personalData'){
				$("#consultRecords").hide();
				$("#personalData").show();
				$("#permissionSettings").hide();
			}
			if(flag=='permissionSettings'){
				$("#consultRecords").hide();
				$("#personalData").hide();
				$("#permissionSettings").show();
			}
		}
		function saveLecture(){
			$.ajax({
				type: "post",
				url: "${ctx}/consult/saveLecture",
				data: {userId:"${user.id}",topics:$("#topics").val(),link:$("#link").val(),lectureTime:$("#lectureTime").val()},
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
		function savePermissionSettings(){
			$.ajax({
				type: "post",
				url: "${ctx}/consult/doctorInfoOper",
				data: {userId:"${user.id}",name:"${user.name}",grabSession:$('input:radio[name="grabSession"]:checked').val(),sendMessage:$('input:radio[name="sendMessage"]:checked').val(),receiveDifferentialNotification:$('input:radio[name="receiveDifferentialNotification"]:checked').val()},
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

		function deleteDoctor(){
			confirmx("确认删除吗？",function(){
				if("${user.phone}"==""){
					alertx("电话为空，不能删除！");
					return;
				}
				$.ajax({
					type: "post",
					url: "${ctx}/consult/doctorOper",
					data: {id:"${user.id}",delFlag:'1',phone:"${user.phone}"},
					dataType: "json",
					success: function(data){
						if("suc"==data.result){
							location.href="${ctx}/consult/consultDoctorList";
							alertx("删除成功！");
						}else{
							alertx("删除失败！");
						}
					}
				});
			})
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="">更多设置</a></li>
	</ul>
	<form:form id="inputForm" modelAttribute="user" enctype="multipart/form-data" action="${ctx}/consult/fileUpload" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<sys:message content="${message}"/>
		<form:input path="id" type="hidden" />
		<div class="control-group">
			<label class="control-label">
				<img src="http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/${user.id}" class="img-rounded" /><br/>
				<form id="fileForm" modelAttribute="user" enctype="multipart/form-data" action="${ctx}/consult/fileUpload" method="post" class="form-horizontal">
					<input id="picfile" type="file" name="files" class="btn btn-small" style="position: absolute; filter: alpha(opacity = 0); opacity: 0; width: 3px;" value=""><br/>
					<input type="submit" id="imgSubmit" class="btn btn-success" value="上传">
				</form>
			</label>
			<div class="controls">
				<p>
				姓名:${user.name}<p>
				总服务数量：${sessionCount}  人    非常满意：${verysatisfy}  人   满意：${satisfy}  人   不满意：${unsatisfy}  人<p>
				收到赏金：${redPacket}  元     打赏人数：${redPacketPerson}  人
			</div>
		</div>
	</form:form>
	<ul class="nav nav-tabs" id="datesun">
		<li class="active" onclick="changeDiv(this,'consultRecords')"><a href="#">咨询记录</a></li>
		<li onclick="changeDiv(this,'personalData')"><a href="#">个人资料</a></li>
		<li onclick="changeDiv(this,'permissionSettings')"><a href="#">权限设置</a></li>
	</ul>
	<div id="consultRecords">
		<form:form id="inputForm" modelAttribute="user" action="${ctx}/consult/doctorOper" method="post" class="form-horizontal"><%--
			<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
			<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
			<input type="hidden" value="${user.id}"/>





			<form:form id="searchForm" modelAttribute="doctor" action="${ctx}/register/registerList" method="post" class="breadcrumb form-search ">
				<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
				<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/><p>
					姓名:${user.name}
					就诊时间：<form:input id="fromDate" path="fromDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
						至
						<form:input id="toDate" path="toDate" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
				<hr>
				接待人数：${sessionCount}  人     <%--回复消息总数：150  人--%><p>
			</form:form>
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead><tr><th>日期</th><th>接待人数</th><%--<th>回复消息总数</th>--%>
				</tr></thead>
				<tbody>
				<c:forEach items="${sessionMap}" var="map">
					<tr>
						<td>${map.key}</td>
						<td>${map.value}</td>
						<%--<td>${map.value}</td>--%>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<div class="pagination">${page}</div>






			<div class="form-actions" >
				<input class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
				<input class="btn btn-primary" type="button" onclick="deleteDoctor()" value="删除该医生"/>
			</div>
		</form:form>
	</div>
	<div id="personalData" style="display: none">
		<form:form id="inputForm" modelAttribute="doctor" action="${ctx}/consult/doctorOper" method="post" class="form-horizontal"><%--
			<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
			<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
			<input type="hidden" value="${doctor.id}"/>
			<input type="hidden" id="timeid" value="${doctor.timeid}"/>
			<input type="hidden" id="isPhoneConsult" value="${doctor.isPhoneConsult}"/>
			<div class="control-group">
				<label class="control-label">密码:</label>
				<div class="controls">
					<input id="password" value="${doctor.password}" htmlEscape="false" maxlength="50" class="input-medium"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">性别:</label>
				<div class="controls">
					<select id="gender" class="txt required" style="width:100px;">
						<option value="1">男</option>
						<option value="0">女</option>
					</select>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">类型:</label>
				<div class="controls">
					<select id="type" class="txt required" style="width:100px;">
						<option value="0">全职</option>
						<option value="1">兼职</option>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">医生职称:</label>
				<div class="controls">
					<input id="title" value="${doctor.title}" htmlEscape="false" maxlength="50" class="input-medium"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">医院:</label>
				<div class="controls">
					<input id="hospital" value="${doctor.hospital}" htmlEscape="false" maxlength="50" class="input-medium"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">科室:</label>
				<div class="controls">
					<form:select path="department">
						<c:forEach items="${departmentList}" var="department">
							<form:option value="${department.name}" label="${department.name}"/>
						</c:forEach>
					</form:select>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">执业医师证号:</label>
				<div class="controls">
					<input id="practitionerCertificateNo" value="${doctor.practitionerCertificateNo}" htmlEscape="false" maxlength="50" class="input-medium"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">擅长:</label>
				<div class="controls">
					<textarea id="skill" rows="4" maxlength="250" class="required" style="width:200px;">${doctor.skill}</textarea>
					<span class="help-inline"><font color="red">*</font>每个词尽量控制在8个字以内，以'空格'隔开，<br/><font color="red" size="4">例如：咳嗽 发烧 不吃饭</font></span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">医生介绍:</label>
				<div class="controls">
					<textarea id="description" rows="4" maxlength="250" class="required" style="width:400px;">${doctor.description}</textarea>
					<span class="help-inline">每个词尽量控制在8个字以内，以'空格'隔开，<br/><font color="red" size="4">例如：北京大学博士生导师 美国进修 朝阳医院</font></span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">明星医生:</label>
				<div class="controls">
					<input id="starDoctoryes" name="starDoctor" value="1" type="radio" checked="checked"><label for="starDoctoryes">是</label>
					<input id="starDoctorno" name="starDoctor" value="0" type="radio"><label for="starDoctorno">否</label>
				</div>
			</div>

			<div class="control-group">
				<label class="control-label">非实时收费:</label>
				<div class="controls">
					<input id="nonRealPayPrice" value="${doctor.nonRealPayPrice}" htmlEscape="false" maxlength="50" class="input-medium"/>
					<span class="help-inline">非实时咨询价格</span>
				</div>
			</div>

			<div class="control-group">
				<label class="control-label">开通电话咨询:</label>
				<div class="controls">
					<label><input name="isPhoneConsult" type="radio" value="1" />是</label>
					<label><input name="isPhoneConsult" type="radio" value="0" checked >否</label>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">电话咨询价格:</label>
				<div class="controls">
					<input id="phonePayPrice" value="${doctor.phonePayPrice}" htmlEscape="false" maxlength="50" class="input-medium"/>
					<span class="help-inline">非实时电话咨询价格</span>
				</div>
			</div>

			<div class="control-group"  style="margin-left: -100px">
				<label class="control-label">上午:</label>
				<div class="controls">
					<table style="width: 600px">
						<tr>
							<td>
							<span>
								<input id="7" name="timeList" class="" type="checkbox" value="7" onclick="kick('7')">
								<label for="7"><span id="05:00span" name="spanList">07:00-08:00</span></label>
							</span>
							</td>
							<td>
							<span>
								<input id="8" name="timeList" class="" type="checkbox" value="8" onclick="kick('8')">
								<label for="8"><span id="05:15span" name="spanList">08:00-09:00</span></label>
							</span>
							</td>
							<td>
							<span>
								<input id="9" name="timeList" class="" type="checkbox" value="9" onclick="kick('9')">
								<label for="9"><span id="05:30span" name="spanList">09:00-10:00</span></label>
							</span>
							</td>
							<td>
							<span>
								<input id="10" name="timeList" class="" type="checkbox" value="10" onclick="kick('10')">
								<label for="10"><span id="05:45span" name="spanList">10:00-11:00</span></label>
							</span>
							</td>
						</tr>
						<tr>
							<td>
							<span>
								<input id="11" name="timeList" class="" type="checkbox" value="11" onclick="kick('11')">
								<label for="11"><span id="06:00span" name="spanList">11:00-12:00</span></label>
							</span>
							</td>
						</tr>
					</table>
				</div>
			<%--</div>--%>

			<%--<div class="control-group"  style="margin-left: -100px">--%>
				<label class="control-label">下午:</label>
				<div class="controls">
					<table style="width: 600px">
						<tr>
							<td>
							<span>
								<input id="12" name="timeList" class="" type="checkbox" value="12" onclick="kick('12')">
								<label for="12"><span id="12:00span" name="spanList">12:00-13:00</span></label>
								<span id="12:00showInfo" name="showInfoList"></span>
							</span>
							</td>
							<td>
							<span>
								<input id="13" name="timeList" class="" type="checkbox" value="13" onclick="kick('13')">
								<label for="13"><span id="13:00span" name="spanList">13:00-14:00</span></label>
								<span id="13:00showInfo" name="showInfoList"></span>
							</span>
							</td>
							<td>
							<span>
								<input id="14" name="timeList" class="" type="checkbox" value="14" onclick="kick('14')">
								<label for="14"><span id="13:15span" name="spanList">14:00-15:00</span></label>
								<span id="13:15showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
							</td>
							<td>
							<span>
								<input id="15" name="timeList" class="" type="checkbox" value="15" onclick="kick('15')">
								<label for="15"><span id="13:30span" name="spanList">15:00-16:00</span></label>
								<span id="13:30showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
							</td>
							<td>
							<span>
								<input id="16" name="timeList" class="" type="checkbox" value="16" onclick="kick('16')">
								<label for="16"><span id="13:30span" name="spanList">16:00-17:00</span></label>
								<span id="13:30showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
							</td>
							<td>
							<span>
								<input id="17" name="timeList" class="" type="checkbox" value="17" onclick="kick('17')">
								<label for="17"><span id="13:45span" name="spanList">17:00-18:00</span></label>
								<span id="13:45showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
							</td>
						</tr>
						<tr>
							<td>
							<span>
								<input id="18" name="timeList" class="" type="checkbox" value="18" onclick="kick('18')">
								<label for="18"><span id="14:00span" name="spanList">18:00-19:00</span></label>
								<span id="14:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
							</td>
							<td>
							<span>
								<input id="19" name="timeList" class="" type="checkbox" value="19" onclick="kick('19')">
								<label for="19"><span id="14:00span" name="spanList">19:00-20:00</span></label>
								<span id="14:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
							</td>
							<td>
							<span>
								<input id="20" name="timeList" class="" type="checkbox" value="20" onclick="kick('20')">
								<label for="20""><span id="14:15span" name="spanList">20:00-21:00</span></label>
								<span id="14:15showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
							</td>
							<td>
							<span>
								<input id="21" name="timeList" class="" type="checkbox" value="21" onclick="kick('21')">
								<label for="21"><span id="14:30span" name="spanList">21:00-22:00</span></label>
								<span id="14:30showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
							</td>
							<td>
							<span>
								<input id="22" name="timeList" class="" type="checkbox" value="22" onclick="kick('22')">
								<label for="22"><span id="14:45span" name="spanList">22:00-23:00</span></label>
								<span id="14:45showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
							</td>
						</tr>
						<tr>
							<td>
							<span>
								<input id="23" name="timeList" class="" type="checkbox" value="23" onclick="kick('23')">
								<label for="23"><span id="15:00span" name="spanList">23:00-24:00</span></label>
								<span id="15:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
							</td>
						</tr>
					</table>
				</div>
			</div>



			<div class="control-group">
				<label class="control-label">排序:</label>
				<div class="controls">
					<input id="sort" value="${doctor.sort}" htmlEscape="false" maxlength="50" class="input-medium"/>
					<span class="help-inline">序号相同时，按姓名排序</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">微商城地址:</label>
				<div class="controls">
					<input id="microMallAddress" value="${doctor.microMallAddress}" htmlEscape="false" class="input-medium"/>
				</div>
			</div>


			讲座：
			<c:forEach items="${lectureList}" var="lecture">
				<div class="control-group">
					<label class="control-label">主题:</label>
					<div class="controls">
							${lecture.topics}
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">连接地址:</label>
					<div class="controls">
							${lecture.link}
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">讲座时间:</label>
					<div class="controls">
							${lecture.lectureTime}
					</div>
				</div>
			</c:forEach>

			<div class="control-group">
				<label class="control-label">主题:</label>
				<div class="controls">
					<input id="topics" value="" htmlEscape="false" maxlength="50" class="input-medium"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">连接地址:</label>
				<div class="controls">
					<input id="link" value="" htmlEscape="false" maxlength="50" class="input-medium"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">讲座时间:</label>
				<div class="controls">
					<input id="lectureTime" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				</div>
			</div>
			<div class="form-actions" >
				<input class="btn btn-primary" type="button" onclick="saveLecture()" value="增加课程"/>
			</div>
			<div class="form-actions" >
				<input class="btn btn-primary" type="button" onclick="savePersonalData()" value="确认"/>
				<input class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
				<input class="btn btn-primary" type="button" onclick="deleteDoctor()" value="删除该医生"/>
			</div>
		</form:form>
	</div>
	<div id="permissionSettings" style="display: none">
		<form:form id="inputForm" modelAttribute="doctor" action="${ctx}/consult/doctorOper" method="post" class="form-horizontal"><%--
			<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
			<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
			<input type="hidden" value="${doctor.id}"/>
			<div class="control-group">
				<label class="control-label">是否允许抢接会话:</label>
				<div class="controls">
					<input id="grabSessionno" name="grabSession" value="0" type="radio" checked="checked"><label for="grabSessionno">否</label>
					<input id="grabSessionyes" name="grabSession" value="1" type="radio"><label for="grabSessionyes">是</label>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">是否向用户发送评价消息:</label>
				<div class="controls">
					<input id="sendMessageno" name="sendMessage" value="0" type="radio"><label for="sendMessageno">否</label>
					<input id="sendMessageyes" name="sendMessage" value="1" type="radio" checked="checked"><label for="sendMessageyes">是</label>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">接收差评监控通知:</label>
				<div class="controls">
					<input id="receiveDifferentialNotificationno" name="receiveDifferentialNotification" value="0" type="radio" checked="checked"><label for="receiveDifferentialNotificationno">否</label>
					<input id="receiveDifferentialNotificationyes" name="receiveDifferentialNotification" value="1" type="radio"><label for="receiveDifferentialNotificationyes">是</label>
				</div>
			</div>
			<div class="form-actions" >
				<input class="btn btn-primary" type="button" onclick="savePermissionSettings()" value="保存"/>
				<input class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
				<input class="btn btn-primary" type="button" onclick="deleteDoctor()" value="删除该医生"/>
			</div>
		</form:form>
	</div>
</body>
</html>