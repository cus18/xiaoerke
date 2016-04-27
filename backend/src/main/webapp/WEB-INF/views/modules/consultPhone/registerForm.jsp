<%@ page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>电话咨询</title>
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
		.current{
			BACKGROUND-COLOR: #FFD1A4;
		}
		.suntb td{
			width:200px;
		}
	</style>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			<%
                List<String> dateList = (List<String>)request.getAttribute("dateList");
                Integer serverLength = (Integer)request.getAttribute("serverLength");
                List<String> list = (List<String>)request.getAttribute("beginTimeList");
                for(String time : list){
                    String[] times = time.split("S\\|X");
            %>		document.getElementById("<%=times[1]%>"+"oper").value="oper";
			<%		if("1".equals(times[0])){%>
			document.getElementById("<%=times[1]%>"+"span").style.background="#CCCCCC";
			document.getElementById("<%=times[1]%>"+"showInfo").innerText="";
			<%  	} else if("2".equals(times[0])){%>
			document.getElementById("<%=times[1]%>"+"span").style.background="#FF77FF";
			document.getElementById("<%=times[1]%>"+"showInfo").innerText="约";
			<%  	} else if("3".equals(times[0])){%>
			document.getElementById("<%=times[1]%>"+"span").style.background="#CCCCCC";
			document.getElementById("<%=times[1]%>"+"showInfo").innerText="重";
			<%		} else if("4".equals(times[0])){%>
			document.getElementById("<%=times[1]%>"+"span").style.background="#FF77FF";
			document.getElementById("<%=times[1]%>"+"showInfo").innerText="约 重";
			<%		}
                }
            %>
			if("<%=serverLength%>"=="15"){
				$("#15min").attr("checked","checked");
			}
			document.getElementsByClassName("datesun")[0].getElementsByTagName("A")[1].className="current";
			document.getElementById("showTime").value=date;
			pageFlag=${pageFlag};
			if(pageFlag<=0){
				document.getElementById("lastWeek").style.display="none";
			}else{
				document.getElementById("lastWeek").style.display="";
			}
			if(pageFlag>=3){
				document.getElementById("nextWeek").style.display="none";
			}else{
				document.getElementById("nextWeek").style.display="";
			}
			if("yes"=="${phoneConsultFlag}"){
				$("#baseInfoDiv").hide();
				$("#showBaseInfoHref").show();
				$("#dateTimeDiv").show();
			}
		});
		var date="<%=dateList.get(0).substring(0,10)%>";
		function showBaseInfo(){
			$("#baseInfoDiv").show();
			$("#showBaseInfoHref").hide();
		}
		var addUpdateFlag="init";
		function kick(time){
			changeFlag="yes";
			var timeList=document.getElementsByName("timeList");
			var flag = false;
			for(var i=0;i<timeList.length;i++){
				if(timeList[i].checked==true){
					flag=true;
				}
			}
			if(!flag){
				addUpdateFlag="init";
				document.getElementById("addRegisterArea").style.display="none";
				document.getElementById("deleteRegisterArea").style.display="none";
			}
			if(document.getElementById(time).checked==true){
				var operFlag = document.getElementById(time+"oper").value;
				var temp;
				if(operFlag=="oper"){
					temp = "update";
				}else{
					temp = "add";
				}
				if(addUpdateFlag=="init"){
					addUpdateFlag=temp;
				}
				if(addUpdateFlag!=temp){
					alertx("只能选择同类型的号源！");
					document.getElementById(time).checked=false;
					return;
				}
				if(addUpdateFlag=="add"){
					document.getElementById("addRegisterArea").style.display="";
				}else{
					document.getElementById("deleteRegisterArea").style.display="";
				}
			}
		}
		var changeFlag="no";
		function change_date_bg(obj,temp){
			addUpdateFlag="init";
			if(changeFlag=="yes"){
				if(confirm("还未保存，是否继续？")){
					changeFlag="";
					document.getElementById("addRegisterArea").style.display="none";
					document.getElementById("deleteRegisterArea").style.display="none";
				}else{
					return;
				}
			}
			var timeList=document.getElementsByName("timeList");
			for(var i=0;i<timeList.length;i++){
				timeList[i].disabled=false;
				timeList[i].checked=false;
				var tp = timeList[i].value;
				var dd = tp.split("S|X");
				timeList[i].value=dd.length=="2"?dd[1]:tp;
			}
			var spanList=document.getElementsByName("spanList");
			for(var i=0;i<spanList.length;i++){
				spanList[i].style.background="";
			}
			var showInfoList=document.getElementsByName("showInfoList");
			for(var i=0;i<showInfoList.length;i++){
				showInfoList[i].innerText="";
			}
			var operList=document.getElementsByName("operList");
			for(var i=0;i<operList.length;i++){
				operList[i].innerText="";
				operList[i].value="";
			}
			var a=document.getElementsByClassName("datesun")[0].getElementsByTagName("A");
			for(var i=0;i<a.length;i++){
				a[i].className="";
			}
			date=temp;
			document.getElementById("showTime").value=temp;
			obj.className="current";
			$.ajax({
				type: "post",
				url: "${ctx}/consultPhone/getRegisterTime",
				data: {doctorId:"${consulPhonetDoctorRelationVo.doctorId}", date:date},
				dataType: "json",
				success: function(data){
					for(var i=0;i<data.beginTimeList.length;i++){
						var timeTemp = data.beginTimeList[i].split("S|X");
						document.getElementById(timeTemp[1]+"oper").value="oper";
						if("1"==timeTemp[0]){
							document.getElementById(timeTemp[1]+"span").style.background="#CCCCCC";
							document.getElementById(timeTemp[1]+"showInfo").innerText="";
						}else if("2"==timeTemp[0]){
							document.getElementById(timeTemp[1]+"span").style.background="#FF77FF";
							document.getElementById(timeTemp[1]+"showInfo").innerText="约";
						}else if("3"==timeTemp[0]){
							document.getElementById(timeTemp[1]+"span").style.background="#CCCCCC";
							document.getElementById(timeTemp[1]+"showInfo").innerText="重";
						}else if("4"==timeTemp[0]){
							document.getElementById(timeTemp[1]+"span").style.background="#FF77FF";
							document.getElementById(timeTemp[1]+"showInfo").innerText="约 重";
						}
					}
				}
			});
		}
		function openConsultPhone(){
			if($("#price").val()==""){
				alertx("请填写价格！");
				return;
			}
			if(isNaN($("#price").val())){
				alertx("价格必须是数字！");
				return;
			}
			if($("#doctorAnswerPhone").val()==""){
				alertx("请填写医生电话！");
				return;
			}
			$.ajax({
				type: "post",
				url: "${ctx}/consultPhone/openConsultPhone",
				data: {doctorId:"${consulPhonetDoctorRelationVo.doctorId}",price:$("#price").val(),serverLength:$('input:radio[name="duration"]:checked').val(),doctorAnswerPhone:$("#doctorAnswerPhone").val()},
				dataType: "json",
				success: function(data){
					if(data.result=='suc'){
						alertx("电话咨询开通成功！");
						$("#baseInfoDiv").hide();
						$("#showBaseInfoHref").show();
						$("#dateTimeDiv").show();
					}else{
						alertx("电话咨询开通失败！");
					}
				}
			});
		}
		function addRegisters(){
			var timeList=document.getElementsByName("timeList");
			var repeat = "no";
			var flag = false;
			var array = "";
			for(var i=0;i<timeList.length;i++){
				if(timeList[i].checked==true){
					array+=timeList[i].value+';';
				}
			}
			if(document.getElementById("copy").checked==true){
				repeat = "0";
			}
			$.ajax({
				type: "post",
				url: "${ctx}/consultPhone/addRegister",
				data: {sysDoctorId:"${consulPhonetDoctorRelationVo.doctorId}",date:date,repeat:repeat,times:array,pageFlag:pageFlag},
				dataType: "json",
				success: function(data){
					if(data.result=="suc"){
						addUpdateFlag="init";
						if(data.reason==""){
							alertx("操作成功");
							for(var i=0;i<timeList.length;i++){
								timeList[i].checked=false;
							}
							document.getElementById("addRegisterArea").style.display="none";
							var tempTimeList = array.split(";");
							for(var i=0;i<tempTimeList.length;i++){
								//document.getElementById(tempTimeList[i]+"oper").innerText="设置";
								document.getElementById(tempTimeList[i]+"oper").value="oper";
								document.getElementById(tempTimeList[i]+"span").style.background="#CCCCCC";
								if(repeat=="0"){
									document.getElementById(tempTimeList[i]+"showInfo").innerText="重";
								}
								//if(intervalFlag=="0"){
								//	document.getElementById(tempTimeList[i]+"showInfo").innerText="每周重";
								//}
							}
						}else{
							alertx(data.reason);
						}
					}else{
						alertx("操作失败！");
					}
				}
			});
		}
		function deleteRegisters(){
			confirmx("确定删除号源吗？",function(){
				var operRepeat = "no";
				if(document.getElementById("delCopy").checked==true){
					operRepeat="yes";
				}
				var timeList=document.getElementsByName("timeList");
				var array = '';
				for(var i=0;i<timeList.length;i++){
					if(timeList[i].checked==true){
						array+=timeList[i].value+';';
					}
				}
				$.ajax({
					type: "post",
					url: "${ctx}/consultPhone/judgeRepeatEffect",
					data: {sysDoctorId:"${consulPhonetDoctorRelationVo.doctorId}",date:date,times:array,operRepeat:operRepeat,pageFlag:pageFlag},
					dataType: "json",
					success: function(data){
						if(data.reason==""){
							$.ajax({
								type: "post",
								url: "${ctx}/consultPhone/removeRegister",
								data: {sysDoctorId:"${consulPhonetDoctorRelationVo.doctorId}",date:date,times:array,operRepeat:operRepeat,pageFlag:data.pageFlag},
								dataType: "json",
								success: function(data){
									for(var i=0;i<timeList.length;i++){
										timeList[i].checked=false;
									}
									document.getElementById("deleteRegisterArea").style.display="none";
									var tempTimeList = array.split(";");
									for(var i=0;i<tempTimeList.length;i++){
										if(data.result=="suc"){
											//document.getElementById(tempTimeList[i]+"oper").innerText="添加";
											document.getElementById(tempTimeList[i]+"oper").value="";
											document.getElementById(tempTimeList[i]+"span").style.background="";
											if(operRepeat=="yes"){
												document.getElementById(tempTimeList[i]+"showInfo").innerText="";
											}
											addUpdateFlag="init";
										}else{
											alertx("删除失败!");
										}
									}
								}
							});
						}else{
							confirmx(data.reason+"确认删除吗？",function(){
								$.ajax({
									type: "post",
									url: "${ctx}/consultPhone/removeRegister",
									data: {sysDoctorId:"${consulPhonetDoctorRelationVo.doctorId}",date:date,times:array,operRepeat:operRepeat,pageFlag:data.pageFlag},
									dataType: "json",
									success: function(data){
										for(var i=0;i<timeList.length;i++){
											timeList[i].checked=false;
										}
										document.getElementById("deleteRegisterArea").style.display="none";
										var tempTimeList = array.split(";");
										for(var i=0;i<tempTimeList.length;i++){
											if(data.result=="suc"){
												document.getElementById(tempTimeList[i]+"oper").value="";
												document.getElementById(tempTimeList[i]+"span").style.background="";
												if(operRepeat=="yes"){
													document.getElementById(tempTimeList[i]+"showInfo").innerText="";
												}
												addUpdateFlag="init";
											}else{
												alertx("删除失败!");
											}
										}
									}
								});
							})
						}
					}
				});
			})
		}
		function lastWeek(){
			if(pageFlag>0){
				--pageFlag;
				window.location.href = "${ctx}/consultPhone/registerForm?doctorId=${consulPhonetDoctorRelationVo.doctorId}&doctorName=${doctorName}&hospital=${hospital}&phone=${phone}&pageFlag="+pageFlag;
			}else if(pageFlag<=0){
				window.location.href = "${ctx}/consultPhone/registerForm?doctorId=${consulPhonetDoctorRelationVo.doctorId}&doctorName=${doctorName}&hospital=${hospital}&phone=${phone}&pageFlag="+0;
			}
		}
		function nextWeek(){
			if(pageFlag<3){
				++pageFlag;
				window.location.href = "${ctx}/consultPhone/registerForm?doctorId=${consulPhonetDoctorRelationVo.doctorId}&doctorName=${doctorName}&hospital=${hospital}&phone=${phone}&pageFlag="+pageFlag;
			}else if(pageFlag>=3){
				window.location.href = "${ctx}/consultPhone/registerForm?doctorId=${consulPhonetDoctorRelationVo.doctorId}&doctorName=${doctorName}&hospital=${hospital}&phone=${phone}&pageFlag="+3;
			}
		}
		function putInDoctorAnswerPhone(phone){
			document.getElementById("doctorAnswerPhone").value=phone;
		}
	</script>
</head>
<body>
<ul class="nav nav-tabs">
	<li class="active"><a>电话咨询</a></li>
</ul><br/>
<form:form id="inputForm" modelAttribute="consulPhonetDoctorRelationVo" action="${ctx}/register/save?" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
	<input type="hidden" id="showTime" htmlEscape="false" maxlength="255" class="input-xlarge"/>
	<form:hidden path="id" htmlEscape="false" maxlength="255" class="input-xlarge"/>
	<form:hidden path="doctorId" htmlEscape="false" maxlength="255" class="input-xlarge"/>
	<sys:message content="${message}"/>
	<div class="control-group">
		<label class="control-label">医生姓名:</label>
		<div class="controls">
				${doctorName}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;所属医院: &nbsp;&nbsp;&nbsp;${hospital}
		</div>
	</div>
	<div class="control-group" id="showBaseInfoHref" style="display: none">
		<label class="control-label"></label>
		<div class="controls">
			<a href="javascript:showBaseInfo()">↓展开基本信息设置</a>
		</div>
	</div>
	<div id="baseInfoDiv">
		<div class="control-group">
			<label class="control-label">医生电话:</label>
			<div class="controls">
				<form:input path="doctorAnswerPhone" id="doctorAnswerPhone" htmlEscape="false" maxlength="50" class="input-medium"/>
				<input id="btnCancel" class="btn" type="button" value="${phone}" onclick="putInDoctorAnswerPhone('${phone}')"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">价格:</label>
			<div class="controls">
				<form:input path="price" id="price" htmlEscape="false" maxlength="5" style="width: 50px" class="input-medium"/>元
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">选择时长:</label>
			<div class="controls">
				<input id="10min" name="duration" value="10" type="radio" checked="checked"><label for="10min">10min/次</label>
				<input id="15min" name="duration" value="15" type="radio"><label for="15min">15min/次</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"></label>
			<div class="controls">
				<input id="btnCancel" class="btn" type="button" value="确 认 保 存" onclick="openConsultPhone()"/>
			</div>
		</div>
	</div>
	<div id="dateTimeDiv" style="display: none">
		<div class="control-group">
			<label class="control-label">日期:</label>
			<div class="controls">
				<ul class="datesun" id="datesun">
					<LI id="lastWeek"><A href="#" onclick="lastWeek()">《上一周</A></LI>
					<c:forEach items="${dateList}" var="date">
						<LI><A href="#" onclick="change_date_bg(this,'${fn:substring(date, 0, 10)}')">${fn:substring(date, 5, 13)}</A></LI>
					</c:forEach>
					<LI id="nextWeek"><A href="#" onclick="nextWeek()">下一周》</A></LI>
				</ul>
			</div>
		</div>
		<div style="background-color: #FFD1A4;width: 700px;margin-left: 200px">
			<div class="control-group"  style="margin-left: -100px">
				<label class="control-label">上午:</label>
				<div class="controls">
					<table style="width: 600px" class="suntb">
						<tr>
							<td>
									<span>
										<input id="05:00" name="timeList" class="" type="checkbox" value="05:00" onclick="kick('05:00')">
										<label for="05:00"><span id="05:00span" name="spanList">05:00</span></label>
										<label><span id="05:00oper" name="operList" onclick="operRegister('05:00')"></span></label>
										<span id="05:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="05:20" name="timeList" class="" type="checkbox" value="05:20" onclick="kick('05:20')">
										<label for="05:20"><span id="05:20span" name="spanList">05:20</span></label>
										<label><span id="05:20oper" name="operList"  onclick="operRegister('05:20')"></span></label>
										<span id="05:20showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="05:40" name="timeList" class="" type="checkbox" value="05:40" onclick="kick('05:40')">
										<label for="05:40"><span id="05:40span" name="spanList">05:40</span></label>
										<label><span id="05:40oper" name="operList"  onclick="operRegister('05:40')"></span></label>
										<span id="05:40showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
						</tr>
						<tr>
							<td>
									<span>
										<input id="06:00" name="timeList" class="" type="checkbox" value="06:00" onclick="kick('06:00')">
										<label for="06:00"><span id="06:00span" name="spanList">06:00</span></label>
										<label><span id="06:00oper" name="operList" onclick="operRegister('06:00')"></span></label>
										<span id="06:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="06:20" name="timeList" class="" type="checkbox" value="06:20" onclick="kick('06:20')">
										<label for="06:20"><span id="06:20span" name="spanList">06:20</span></label>
										<label><span id="06:20oper" name="operList"  onclick="operRegister('06:20')"></span></label>
										<span id="06:20showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="06:40" name="timeList" class="" type="checkbox" value="06:40" onclick="kick('06:40')">
										<label for="06:40"><span id="06:40span" name="spanList">06:40</span></label>
										<label><span id="06:40oper" name="operList"  onclick="operRegister('06:40')"></span></label>
										<span id="06:40showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
						</tr>
						<tr>
							<td>
									<span>
										<input id="07:00" name="timeList" class="" type="checkbox" value="07:00" onclick="kick('07:00')">
										<label for="07:00"><span id="07:00span" name="spanList">07:00</span></label>
										<label><span id="07:00oper" name="operList" onclick="operRegister('07:00')"></span></label>
										<span id="07:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="07:20" name="timeList" class="" type="checkbox" value="07:20" onclick="kick('07:20')">
										<label for="07:20"><span id="07:20span" name="spanList">07:20</span></label>
										<label><span id="07:20oper" name="operList"  onclick="operRegister('07:20')"></span></label>
										<span id="07:20showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="07:40" name="timeList" class="" type="checkbox" value="07:40" onclick="kick('07:40')">
										<label for="07:40"><span id="07:40span" name="spanList">07:40</span></label>
										<label><span id="07:40oper" name="operList"  onclick="operRegister('07:40')"></span></label>
										<span id="07:40showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
						</tr>
						<tr>
							<td>
									<span>
										<input id="08:00" name="timeList" class="" type="checkbox" value="08:00" onclick="kick('08:00')">
										<label for="08:00"><span id="08:00span" name="spanList">08:00</span></label>
										<label><span id="08:00oper" name="operList"  onclick="operRegister('08:00')"></span></label>
										<span id="08:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="08:20" name="timeList" class="" type="checkbox" value="08:20" onclick="kick('08:20')">
										<label for="08:20"><span id="08:20span" name="spanList">08:20</span></label>
										<label><span id="08:20oper" name="operList"  onclick="operRegister('08:20')"></span></label>
										<span id="08:20showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="08:40" name="timeList" class="" type="checkbox" value="08:40" onclick="kick('08:40')">
										<label for="08:40"><span id="08:40span" name="spanList">08:40</span></label>
										<label><span id="08:40oper" name="operList"  onclick="operRegister('08:40')"></span></label>
										<span id="08:40showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
						</tr>
						<tr>
							<td>
									<span>
										<input id="09:00" name="timeList" class="" type="checkbox" value="09:00" onclick="kick('09:00')">
										<label for="09:00"><span id="09:00span" name="spanList">09:00</span></label>
										<label><span id="09:00oper" name="operList"  onclick="operRegister('09:00')"></span></label>
										<span id="09:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="09:20" name="timeList" class="" type="checkbox" value="09:20" onclick="kick('09:20')">
										<label for="09:20"><span id="09:20span" name="spanList">09:20</span></label>
										<label><span id="09:20oper" name="operList"  onclick="operRegister('09:20')"></span></label>
										<span id="09:20showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="09:40" name="timeList" class="" type="checkbox" value="09:40" onclick="kick('09:40')">
										<label for="09:40"><span id="09:40span" name="spanList">09:40</span></label>
										<label><span id="09:40oper" name="operList"  onclick="operRegister('09:40')"></span></label>
										<span id="09:40showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
						</tr>
						<tr>
							<td>
									<span>
										<input id="10:00" name="timeList" class="" type="checkbox" value="10:00" onclick="kick('10:00')">
										<label for="10:00"><span id="10:00span" name="spanList">10:00</span></label>
										<label><span id="10:00oper" name="operList"  onclick="operRegister('10:00')"></span></label>
										<span id="10:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="10:20" name="timeList" class="" type="checkbox" value="10:20" onclick="kick('10:20')">
										<label for="10:20"><span id="10:20span" name="spanList">10:20</span></label>
										<label><span id="10:20oper" name="operList"  onclick="operRegister('10:20')"></span></label>
										<span id="10:20showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="10:40" name="timeList" class="" type="checkbox" value="10:40" onclick="kick('10:40')">
										<label for="10:40"><span id="10:40span" name="spanList">10:40</span></label>
										<label><span id="10:40oper" name="operList"  onclick="operRegister('10:40')"></span></label>
										<span id="10:40showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
						</tr>
						<tr>
							<td>
									<span>
										<input id="11:00" name="timeList" class="" type="checkbox" value="11:00" onclick="kick('11:00')">
										<label for="11:00"><span id="11:00span" name="spanList">11:00</span></label>
										<label><span id="11:00oper" name="operList"  onclick="operRegister('11:00')"></span></label>
										<span id="11:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td><input id="11:20" name="timeList" class="" type="checkbox" value="11:20" onclick="kick('11:20')"><span>

										<label for="11:20"><span id="11:20span" name="spanList">11:20</span></label>
										<label><span id="11:20oper" name="operList" onclick="operRegister('11:20')"></span></label>
										<span id="11:20showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>

							</td>
							<td>
									<span>
										<input id="11:40" name="timeList" class="" type="checkbox" value="11:40" onclick="kick('11:40')">
										<label for="11:40"><span id="11:40span" name="spanList">11:40</span></label>
										<label><span id="11:40oper" name="operList"  onclick="operRegister('11:40')"></span></label>
										<span id="11:40showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
						</tr>
						<tr>
							<td>
									<span>
										<input id="12:00" name="timeList" class="" type="checkbox" value="12:00" onclick="kick('12:00')">
										<label for="12:00"><span id="12:00span" name="spanList">12:00</span></label>
										<label><span id="12:00oper" name="operList"  onclick="operRegister('12:00')"></span></label>
										<span id="12:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="12:20" name="timeList" class="" type="checkbox" value="12:20" onclick="kick('12:20')">
										<label for="12:20"><span id="12:20span" name="spanList">12:20</span></label>
										<label><span id="12:20oper" name="operList"  onclick="operRegister('12:20')"></span></label>
										<span id="12:20showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="12:40" name="timeList" class="" type="checkbox" value="12:40" onclick="kick('12:40')">
										<label for="12:40"><span id="12:40span" name="spanList">12:40</span></label>
										<label><span id="12:40oper" name="operList"  onclick="operRegister('12:40')"></span></label>
										<span id="12:40showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div class="control-group"  style="margin-left: -100px">
				<label class="control-label">下午:</label>
				<div class="controls">
					<table style="width: 600px" class="suntb">
						<tr>
							<td>
									<span>
										<input id="13:00" name="timeList" class="" type="checkbox" value="13:00" onclick="kick('13:00')">
										<label for="13:00"><span id="13:00span" name="spanList">13:00</span></label>
										<label><span id="13:00oper" name="operList"  onclick="operRegister('13:00')"></span></label>
										<span id="13:00showInfo" name="showInfoList"></span>
									</span>
							</td>
							<td>
									<span>
										<input id="13:20" name="timeList" class="" type="checkbox" value="13:20" onclick="kick('13:20')">
										<label for="13:20"><span id="13:20span" name="spanList">13:20</span></label>
										<label><span id="13:20oper" name="operList"  onclick="operRegister('13:20')"></span></label>
										<span id="13:20showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="13:40" name="timeList" class="" type="checkbox" value="13:40" onclick="kick('13:40')">
										<label for="13:40"><span id="13:40span" name="spanList">13:40</span></label>
										<label><span id="13:40oper" name="operList"  onclick="operRegister('13:40')"></span></label>
										<span id="13:40showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
						</tr>
						<tr>
							<td>
									<span>
										<input id="14:00" name="timeList" class="" type="checkbox" value="14:00" onclick="kick('14:00')">
										<label for="14:00"><span id="14:00span" name="spanList">14:00</span></label>
										<label><span id="14:00oper" name="operList"  onclick="operRegister('14:00')"></span></label>
										<span id="14:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="14:20" name="timeList" class="" type="checkbox" value="14:20" onclick="kick('14:20')">
										<label for="14:20"><span id="14:20span" name="spanList">14:20</span></label>
										<label><span id="14:20oper" name="operList"  onclick="operRegister('14:20')"></span></label>
										<span id="14:20showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="14:40" name="timeList" class="" type="checkbox" value="14:40" onclick="kick('14:40')">
										<label for="14:40"><span id="14:40span" name="spanList">14:40</span></label>
										<label><span id="14:40oper" name="operList"  onclick="operRegister('14:40')"></span></label>
										<span id="14:40showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
						</tr>
						<tr>
							<td>
									<span>
										<input id="15:00" name="timeList" class="" type="checkbox" value="15:00" onclick="kick('15:00')">
										<label for="15:00"><span id="15:00span" name="spanList">15:00</span></label>
										<label><span id="15:00oper" name="operList"  onclick="operRegister('15:00')"></span></label>
										<span id="15:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="15:20" name="timeList" class="" type="checkbox" value="15:20" onclick="kick('15:20')">
										<label for="15:20"><span id="15:20span" name="spanList">15:20</span></label>
										<label><span id="15:20oper" name="operList"  onclick="operRegister('15:20')"></span></label>
										<span id="15:20showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="15:40" name="timeList" class="" type="checkbox" value="15:40" onclick="kick('15:40')">
										<label for="15:40"><span id="15:40span" name="spanList">15:40</span></label>
										<label><span id="15:40oper" name="operList"  onclick="operRegister('15:40')"></span></label>
										<span id="15:40showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
						</tr>
						<tr>
							<td>
									<span>
										<input id="16:00" name="timeList" class="" type="checkbox" value="16:00" onclick="kick('16:00')">
										<label for="16:00"><span id="16:00span" name="spanList">16:00</span></label>
										<label><span id="16:00oper" name="operList"  onclick="operRegister('16:00')"></span></label>
										<span id="16:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="16:20" name="timeList" class="" type="checkbox" value="16:20" onclick="kick('16:20')">
										<label for="16:20"><span id="16:20span" name="spanList">16:20</span></label>
										<label><span id="16:20oper" name="operList"  onclick="operRegister('16:20')"></span></label>
										<span id="16:20showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="16:40" name="timeList" class="" type="checkbox" value="16:40" onclick="kick('16:40')">
										<label for="16:40"><span id="16:40span" name="spanList">16:40</span></label>
										<label><span id="16:40oper" name="operList"  onclick="operRegister('16:40')"></span></label>
										<span id="16:40showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
						</tr>
						<tr>
							<td>
									<span>
										<input id="17:00" name="timeList" class="" type="checkbox" value="17:00" onclick="kick('17:00')">
										<label for="17:00"><span id="17:00span" name="spanList">17:00</span></label>
										<label><span id="17:00oper" name="operList"  onclick="operRegister('17:00')"></span></label>
										<span id="17:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="17:20" name="timeList" class="" type="checkbox" value="17:20" onclick="kick('17:20')">
										<label for="17:20"><span id="17:20span" name="spanList">17:20</span></label>
										<label><span id="17:20oper" name="operList"  onclick="operRegister('17:20')"></span></label>
										<span id="17:20showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="17:40" name="timeList" class="" type="checkbox" value="17:40" onclick="kick('17:40')">
										<label for="17:40"><span id="17:40span" name="spanList">17:40</span></label>
										<label><span id="17:40oper" name="operList"  onclick="operRegister('17:40')"></span></label>
										<span id="17:40showInfo" name="showInfoList"></span>&nbsp;&nbsp;&nbsp;
									</span>
							</td>
						</tr>
						<tr>
							<td>
									<span>
										<input id="18:00" name="timeList" class="" type="checkbox" value="18:00" onclick="kick('18:00')">
										<label for="18:00"><span id="18:00span" name="spanList">18:00</span></label>
										<label><span id="18:00oper" name="operList"  onclick="operRegister('18:00')"></span></label>
										<span id="18:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="18:20" name="timeList" class="" type="checkbox" value="18:20" onclick="kick('18:20')">
										<label for="18:20"><span id="18:20span" name="spanList">18:20</span></label>
										<label><span id="18:20oper" name="operList"  onclick="operRegister('18:20')"></span></label>
										<span id="18:20showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="18:40" name="timeList" class="" type="checkbox" value="18:40" onclick="kick('18:40')">
										<label for="18:40"><span id="18:40span" name="spanList">18:40</span></label>
										<label><span id="18:40oper" name="operList"  onclick="operRegister('18:40')"></span></label>
										<span id="18:40showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
						</tr>
						<tr>
							<td>
									<span>
										<input id="19:00" name="timeList" class="" type="checkbox" value="19:00" onclick="kick('19:00')">
										<label for="19:00"><span id="19:00span" name="spanList">19:00</span></label>
										<label><span id="19:00oper" name="operList"  onclick="operRegister('19:00')"></span></label>
										<span id="19:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="19:20" name="timeList" class="" type="checkbox" value="19:20" onclick="kick('19:20')">
										<label for="19:20"><span id="19:20span" name="spanList">19:20</span></label>
										<label><span id="19:20oper" name="operList"  onclick="operRegister('19:20')"></span></label>
										<span id="19:20showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="19:40" name="timeList" class="" type="checkbox" value="19:40" onclick="kick('19:40')">
										<label for="19:40"><span id="19:40span" name="spanList">19:40</span></label>
										<label><span id="19:40oper" name="operList"  onclick="operRegister('19:40')"></span></label>
										<span id="19:40showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
						</tr>
						<tr>
							<td>
									<span>
										<input id="20:00" name="timeList" class="" type="checkbox" value="20:00" onclick="kick('20:00')">
										<label for="20:00"><span id="20:00span" name="spanList">20:00</span></label>
										<label><span id="20:00oper" name="operList"  onclick="operRegister('20:00')"></span></label>
										<span id="20:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="20:20" name="timeList" class="" type="checkbox" value="20:20" onclick="kick('20:20')">
										<label for="20:20"><span id="20:20span" name="spanList">20:20</span></label>
										<label><span id="20:20oper" name="operList"  onclick="operRegister('20:20')"></span></label>
										<span id="20:20showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
							<td>
									<span>
										<input id="20:40" name="timeList" class="" type="checkbox" value="20:40" onclick="kick('20:40')">
										<label for="20:40"><span id="20:40span" name="spanList">20:40</span></label>
										<label><span id="20:40oper" name="operList"  onclick="operRegister('20:40')"></span></label>
										<span id="20:40showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
									</span>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div id="addRegisterArea" style="display: none">
				<div class="form-actions">
					<input id="copy" name="copy" type="checkbox" value="yes">
					<label for="copy" >自动按周重复该设置</label><br/>
					<input id="btnSubmit" class="btn btn-primary" type="button" onclick="addRegisters()" value="确认添加选中"/>
					<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
				</div>
			</div>
			<div id="deleteRegisterArea" style="display: none">
				<div class="form-actions">
					<input id="delCopy" name="delCopy" type="checkbox" value="yes">
					<label for="delCopy" >删除重复设置的号源</label><br/>
					<input id="btnSubmit" class="btn btn-primary" type="button" onclick="deleteRegisters()" value="确认删除选中"/>
					<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
				</div>
			</div>
		</div>
	</div>
</form:form>
</body>
</html>