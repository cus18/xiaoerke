<%@ page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>加号</title>
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
				/* if(${showFloatLayerFlag}){
					showDialog("loading", "20:00后为系统维护时间");
				} */
				<%
					List<String> list = (List<String>)request.getAttribute("beginTimeList");
					List<String> dateList = (List<String>)request.getAttribute("dateList");
					List<String> dislist = (List<String>)request.getAttribute("distimeList");
					String copy = (String)request.getAttribute("repeatFlag");
					String tempinterval = (String)request.getAttribute("intervalFlag");
					for(String time : list){
						String[] times = time.split("S\\|X");
						if("1".equals(times[0])){
					%>
							if("0"=="<%=times[2]%>"){
								document.getElementById("<%=times[1]%>"+"showInfo").innerText="每周重";
							}else{
								document.getElementById("<%=times[1]%>"+"showInfo").innerText="隔周重";
							}
					<%  }else if("3".equals(times[0])){%>
							document.getElementById("<%=times[1]%>"+"oper").innerText="设置";
							document.getElementById("<%=times[1]%>"+"oper").value="oper";
							document.getElementById("<%=times[1]%>"+"span").style.background="#CCCCCC";
							if("0"=="<%=times[2]%>"){
								document.getElementById("<%=times[1]%>"+"showInfo").innerText="每周重";
							}else{
								document.getElementById("<%=times[1]%>"+"showInfo").innerText="隔周重";
							}
					<%  } else if("4".equals(times[0])){%>
							document.getElementById("<%=times[1]%>"+"oper").innerText="设置";
							document.getElementById("<%=times[1]%>"+"oper").value="oper";
							document.getElementById("<%=times[1]%>"+"span").style.background="#FF77FF";
							document.getElementById("<%=times[1]%>"+"showInfo").innerText="约";
					<%	} else if("5".equals(times[0])){%>
							document.getElementById("<%=times[1]%>"+"oper").innerText="设置";
							document.getElementById("<%=times[1]%>"+"oper").value="oper";
							document.getElementById("<%=times[1]%>"+"span").style.background="#FF77FF";
							if("0"=="<%=times[2]%>"){
								document.getElementById("<%=times[1]%>"+"showInfo").innerText="约 每周重";
							}else{
								document.getElementById("<%=times[1]%>"+"showInfo").innerText="约 隔周重";
							}
					<%	} else if("6".equals(times[0])){%>
							document.getElementById("<%=times[1]%>"+"oper").innerText="设置";
							document.getElementById("<%=times[1]%>"+"oper").value="oper";
							document.getElementById("<%=times[1]%>"+"span").style.background="#CCCCCC";
					<%	} else if("8".equals(times[0])){%>
							document.getElementById("<%=times[1]%>"+"oper").innerText="";
							document.getElementById("<%=times[1]%>").disabled=true;
							document.getElementById("<%=times[1]%>"+"span").style.background="#FF77FF";
							if("0"=="<%=times[2]%>"){
								document.getElementById("<%=times[1]%>"+"showInfo").innerText="约 每周重";
							}else{
								document.getElementById("<%=times[1]%>"+"showInfo").innerText="约 隔周重";
							}
					<%	}
					}
					for(String time : dislist){%>
						document.getElementById("<%=time%>"+"oper").innerText="";
						document.getElementById("<%=time%>").disabled=true;
					<%}%>
				document.getElementById("price").value="${registerServiceVo.price}";
				document.getElementById("serverType").value="${registerServiceVo.serviceType}";
				document.getElementsByClassName("locationsun")[0].getElementsByTagName("A")[0].className="current";
				document.getElementsByClassName("datesun")[0].getElementsByTagName("A")[1].className="current";
				document.getElementById("showTime").value=date;
				document.getElementById("sysHospitalId").value=hospitalId;
				document.getElementById("locationId").value=locationId;
				choiceInterval();
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
		});
			
		var pageFlag=0;
		var hospitalId="${registerServiceVo.sysHospitalId}";
		var locationId="${registerServiceVo.locationId}";
		var date="<%=dateList.get(0).substring(0,10)%>";
		var changeFlag = "";
		function change_location_bg(obj,hid,lid){
	 		 addUpdateFlag="init";
			  if(changeFlag=="yes"){
				  if(confirm("还未保存，是否继续？")){
					  document.getElementById("addRegisterArea").style.display="none";
			 		  document.getElementById("deleteRegisterArea").style.display="none";
					  changeFlag="";
				  } else {
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
				  operList[i].innerText="添加";
				  operList[i].value="";
			  }
			  document.getElementById("sysHospitalId").value=hid;
			  document.getElementById("locationId").value=lid;
			  var a=document.getElementsByClassName("locationsun")[0].getElementsByTagName("A");
			  for(var i=0;i<a.length;i++){
				  a[i].className="";
			  }
		  	  obj.className="current";
		  	  hospitalId=hid;
		  	  locationId=lid;
		  	  $.ajax({
	             type: "post",
	             url: "${ctx}/register/getRegisterTime",
	             data: {sysDoctorId:"${registerServiceVo.sysDoctorId}",sysHospitalId:hospitalId,locationId:locationId, date:date},
	             dataType: "json",
	             success: function(data){
	            	 for(var i=0;i<data.timeList.length;i++){
	            		 var timeTemp = data.timeList[i].split("S|X");
	            		 if("1"==timeTemp[0]){
	            			 if("0"==timeTemp[2]){
	            				 document.getElementById(timeTemp[1]+"showInfo").innerText="每周重";
	            			 }else{
	            				 document.getElementById(timeTemp[1]+"showInfo").innerText="隔周重";
	            			 }
	            		 }else if("3"==timeTemp[0]){
	            			 document.getElementById(timeTemp[1]+"oper").innerText="设置";
							 document.getElementById(timeTemp[1]+"oper").value="oper";
							 document.getElementById(timeTemp[1]+"span").style.background="#CCCCCC";
	            			 if("0"==timeTemp[2]){
								document.getElementById(timeTemp[1]+"showInfo").innerText="每周重";
							 }else{
								document.getElementById(timeTemp[1]+"showInfo").innerText="隔周重";
							 }
	            		 }else if("4"==timeTemp[0]){
	            			 document.getElementById(timeTemp[1]+"oper").innerText="设置";
							 document.getElementById(timeTemp[1]+"oper").value="oper";
							 document.getElementById(timeTemp[1]+"span").style.background="#FF77FF";
							 document.getElementById(timeTemp[1]+"showInfo").innerText="约";
	            		 }else if("5"==timeTemp[0]){
	            			 document.getElementById(timeTemp[1]+"oper").innerText="设置";
							 document.getElementById(timeTemp[1]+"oper").value="oper";
							 document.getElementById(timeTemp[1]+"span").style.background="#FF77FF";
	            			 if("0"==timeTemp[2]){
								document.getElementById(timeTemp[1]+"showInfo").innerText="约 每周重";
							 }else{
								document.getElementById(timeTemp[1]+"showInfo").innerText="约 隔周重";
							 }
	            		 } else if("6"==timeTemp[0]){
	            			 document.getElementById(timeTemp[1]+"oper").innerText="设置";
							 document.getElementById(timeTemp[1]+"oper").value="oper";
							 document.getElementById(timeTemp[1]+"span").style.background="#CCCCCC";
	            		 } else if("8"==timeTemp[0]){
	            			 document.getElementById(timeTemp[1]+"oper").innerText="";
	            			 document.getElementById(timeTemp[1]).disabled=true;
	            			 document.getElementById(timeTemp[1]+"span").style.background="#FF77FF";
	            			 if("0"==timeTemp[2]){
								document.getElementById(timeTemp[1]+"showInfo").innerText="约 每周重";
							 }else{
								document.getElementById(timeTemp[1]+"showInfo").innerText="约 隔周重";
							 }
	            		 }
	            	 }
	            	 for(var i=0;i<data.distimeList.length;i++){
	            		 var time = data.distimeList[i];
	            	 	 document.getElementById(time).disabled=true;
	            	 	 document.getElementById(time+"oper").innerText="";
	            	 }
	            	 if(data.price==null){
	            		 data.price="";
	            	 }
	            	 document.getElementById("price").value=data.price;
	 				 document.getElementById("serverType").value=data.serverType;
	             }
	         });
		  }
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
				  operList[i].innerText="添加";
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
	             url: "${ctx}/register/getRegisterTime",
	             data: {sysDoctorId:"${registerServiceVo.sysDoctorId}",sysHospitalId:hospitalId,locationId:locationId, date:date},
	             dataType: "json",
	             success: function(data){
	            	 for(var i=0;i<data.timeList.length;i++){
	            		 var timeTemp = data.timeList[i].split("S|X");
	            		 if("1"==timeTemp[0]){
	            			 if("0"==timeTemp[2]){
	            				 document.getElementById(timeTemp[1]+"showInfo").innerText="每周重";
	            			 }else{
	            				 document.getElementById(timeTemp[1]+"showInfo").innerText="隔周重";
	            			 }
	            		 }else if("3"==timeTemp[0]){
	            			 document.getElementById(timeTemp[1]+"oper").innerText="设置";
							 document.getElementById(timeTemp[1]+"oper").value="oper";
							 document.getElementById(timeTemp[1]+"span").style.background="#CCCCCC";
	            			 if("0"==timeTemp[2]){
								document.getElementById(timeTemp[1]+"showInfo").innerText="每周重";
							 }else{
								document.getElementById(timeTemp[1]+"showInfo").innerText="隔周重";
							 }
	            		 }else if("4"==timeTemp[0]){
	            			 document.getElementById(timeTemp[1]+"oper").innerText="设置";
							 document.getElementById(timeTemp[1]+"oper").value="oper";
							 document.getElementById(timeTemp[1]+"span").style.background="#FF77FF";
							 document.getElementById(timeTemp[1]+"showInfo").innerText="约";
	            		 }else if("5"==timeTemp[0]){
	            			 document.getElementById(timeTemp[1]+"oper").innerText="设置";
							 document.getElementById(timeTemp[1]+"oper").value="oper";
							 document.getElementById(timeTemp[1]+"span").style.background="#FF77FF";
	            			 if("0"==timeTemp[2]){
								document.getElementById(timeTemp[1]+"showInfo").innerText="约 每周重";
							 }else{
								document.getElementById(timeTemp[1]+"showInfo").innerText="约 隔周重";
							 }
	            		 } else if("6"==timeTemp[0]){
	            			 document.getElementById(timeTemp[1]+"oper").innerText="设置";
							 document.getElementById(timeTemp[1]+"oper").value="oper";
							 document.getElementById(timeTemp[1]+"span").style.background="#CCCCCC";
	            		 } else if("8"==timeTemp[0]){
	            			 document.getElementById(timeTemp[1]+"oper").innerText="";
	            			 document.getElementById(timeTemp[1]).disabled=true;
	            			 document.getElementById(timeTemp[1]+"span").style.background="#FF77FF";
	            			 if("0"==timeTemp[2]){
								document.getElementById(timeTemp[1]+"showInfo").innerText="约 每周重";
							 }else{
								document.getElementById(timeTemp[1]+"showInfo").innerText="约 隔周重";
							 }
	            		 }
	            	 }
	            	 for(var i=0;i<data.distimeList.length;i++){
	            		 var time = data.distimeList[i];
	            	 	 document.getElementById(time).disabled=true;
            	 		 document.getElementById(time+"oper").innerText="";
	            	 }
	            	 if(data.price==null){
	            		 data.price="";
	            	 }
	            	 document.getElementById("price").value=data.price;
	 				 document.getElementById("serverType").value=data.serverType;
	             }
	         });
		  }
		  
		  var addUpdateFlag="init";
		  function kick(time){
			  changeFlag="yes"
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
					  alert("只能添加同类型号源");
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
		  function choiceInterval(){
			  if(document.getElementById("copy").checked==true){
				  document.getElementById("interval1").disabled=false;
				  document.getElementById("interval2").disabled=false;
			  }else{
				  document.getElementById("interval1").disabled=true;
				  document.getElementById("interval2").disabled=true;
			  }
		  }
		  function operRegister(time) {
				var operFlag = document.getElementById(time+"oper").value;
				var the_url = "${ctx}/register/operRegisterForm?sysDoctorId=${registerServiceVo.sysDoctorId}&locationId="+locationId+"&date="+date+"&time="+time+"&operFlag="+operFlag;
				registerShowWindow("操作号源", the_url, 500);
          }
		  function addRegister(price,serverType,time,operInterval){
			  sd_remove();
			  $.ajax({
		             type: "post",
		             url: "${ctx}/register/addRegister",
		             data: {sysDoctorId:"${registerServiceVo.sysDoctorId}",sysHospitalId:hospitalId,locationId:locationId,date:date,price:price,serverType:serverType,time:time,operInterval:operInterval,pageFlag:pageFlag},
		             dataType: "json",
		             success: function(data){
		            	 if(data.suc=="suc"){
		            		 if(data.reason==""){
		            			alert("操作成功");
			            		document.getElementById(time+"oper").innerText="设置";
				   			  	document.getElementById(time+"oper").value="oper";
				   			  	document.getElementById(time+"span").style.background="#CCCCCC";
				   			  	if(operInterval=="1"){
				   			  		document.getElementById(time+"showInfo").innerText="隔周重";
				   			  	}
				   			 	if(operInterval=="0"){
				   			 		document.getElementById(time+"showInfo").innerText="每周重";
				   			  	}
		            		 }else{
		            			 alert(data.reason);
		            		 }
		            	 }
		             }
	         });
		  }
		  function addRegisters(){
			  var timeList=document.getElementsByName("timeList");
			  var price = document.getElementById("price").value;
			  var serverType = document.getElementById("serverType").value
			  var array = '';
			  var flag = false;
			  for(var i=0;i<timeList.length;i++){
				  if(timeList[i].checked==true){
					  array+=timeList[i].value+';';
					  flag=true;
				  }
			  }
			  if(flag==true&&document.getElementById("price").value.trim()=="") {
				  alertx("请输入价格！");
			  }else if(flag==true&&isNaN(document.getElementById("price").value.trim())) {
				  alertx("输入的价格必须是数字！");
			  }else if(flag==true&&document.getElementById("serverType").value.trim()=="") {
				  alertx("请选择服务类型！");
			  }else{
				  var intervalFlag="no";
				  if(document.getElementById("copy").checked==true){
					  if(document.getElementById("interval1").checked==true){
						  intervalFlag="0";
					  }else if(document.getElementById("interval2").checked==true){
						  intervalFlag="1";
					  }
				  }
				  $.ajax({
			             type: "post",
			             url: "${ctx}/register/addRegister",
			             data: {sysDoctorId:"${registerServiceVo.sysDoctorId}",sysHospitalId:hospitalId,locationId:locationId,date:date,price:price,serverType:serverType,times:array,operInterval:intervalFlag,pageFlag:pageFlag},
			             dataType: "json",
			             success: function(data){
			            	 if(data.suc=="suc"){
			            		 addUpdateFlag="init";
			            		 if(data.reason==""){
			            			alert("操作成功");
			            			for(var i=0;i<timeList.length;i++){
										  timeList[i].checked=false;
								    }
			            			document.getElementById("addRegisterArea").style.display="none";
			            			var tempTimeList = array.split(";");
					            	 for(var i=0;i<tempTimeList.length;i++){
					            		document.getElementById(tempTimeList[i]+"oper").innerText="设置";
						   			  	document.getElementById(tempTimeList[i]+"oper").value="oper";
						   			  	document.getElementById(tempTimeList[i]+"span").style.background="#CCCCCC";
						   			  	if(intervalFlag=="1"){
						   			  		document.getElementById(tempTimeList[i]+"showInfo").innerText="隔周重";
						   			  	}
						   			 	if(intervalFlag=="0"){
						   			 		document.getElementById(tempTimeList[i]+"showInfo").innerText="每周重";
						   			  	}
					            	}
			            		 }else{
			            			 alert(data.reason);
			            		 }
			            	 }
			             }
		         });
			  }
		  }
		  function updateRegister(price,serverType,time,registerId,operRepeat){
			  sd_remove();
			  $.ajax({
		             type: "post",
		             url: "${ctx}/register/updateRegister",
		             data: {sysDoctorId:"${registerServiceVo.sysDoctorId}",sysHospitalId:hospitalId,locationId:locationId,date:date,price:price,serverType:serverType,time:time,registerId:registerId,operRepeat:operRepeat,pageFlag:pageFlag},
		             dataType: "json",
		             success: function(data){
		            	 if(data.suc=="suc"){
		            		document.getElementById(time+"oper").innerText="设置";
			   			  	document.getElementById(time+"oper").value="oper";
			   			  	document.getElementById(time+"span").style.background="#CCCCCC";
			   			  	if(operInterval=="1"){
			   			  		document.getElementById(time+"showInfo").innerText="隔周重";
			   			  	}
			   			 	if(operInterval=="0"){
			   			 		document.getElementById(time+"showInfo").innerText="每周重";
			   			  	}
		            	 }
		             }
	         });
		  }
		  function deleteRegister(time,registerId,operRepeat){
			  confirmx("确定删除号源吗？",function(){
				  $.ajax({
			             type: "post",
			             url: "${ctx}/register/judgeRepeatEffect",
			             data: {sysDoctorId:"${registerServiceVo.sysDoctorId}",sysHospitalId:hospitalId,locationId:locationId,registerId:registerId,date:date,times:time,operRepeat:operRepeat,pageFlag:pageFlag},
			             dataType: "json",
			             success: function(data){
			            	 if(data.reason==""){
			            		 sd_remove();
			            		 $.ajax({
						             type: "post",
						             url: "${ctx}/register/removeRegister",
						             data: {sysDoctorId:"${registerServiceVo.sysDoctorId}",sysHospitalId:hospitalId,locationId:locationId,registerId:registerId,date:date,times:time,operRepeat:operRepeat,pageFlag:data.pageFlag},
						             dataType: "json",
						             success: function(data){
						            	 if(data.suc=="suc"){
						            		document.getElementById(time+"oper").innerText="添加";
							   			  	document.getElementById(time+"oper").value="";
							   			  	document.getElementById(time+"span").style.background="";
							   			  	if(operRepeat=="yes"){
							   			  		document.getElementById(time+"showInfo").innerText="";
							   			  	}
						            	 }
						             }
					         	});
			            	 }else{
				            	 confirmx(data.reason+"确认删除吗？",function(){
				            		 sd_remove();
				            		 $.ajax({
							             type: "post",
							             url: "${ctx}/register/removeRegister",
							             data: {sysDoctorId:"${registerServiceVo.sysDoctorId}",sysHospitalId:hospitalId,locationId:locationId,registerId:registerId,date:date,times:time,operRepeat:operRepeat,pageFlag:data.pageFlag},
							             dataType: "json",
							             success: function(data){
							            	 if(data.suc=="suc"){
							            		document.getElementById(time+"oper").innerText="添加";
								   			  	document.getElementById(time+"oper").value="";
								   			  	document.getElementById(time+"span").style.background="";
								   			  	if(operRepeat=="yes"){
								   			  		document.getElementById(time+"showInfo").innerText="";
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
			             url: "${ctx}/register/judgeRepeatEffect",
			             data: {sysDoctorId:"${registerServiceVo.sysDoctorId}",sysHospitalId:hospitalId,locationId:locationId,date:date,times:array,operRepeat:operRepeat,pageFlag:pageFlag},
			             dataType: "json",
			             success: function(data){
			            	 if(data.reason==""){
			            		 $.ajax({
						             type: "post",
						             url: "${ctx}/register/removeRegister",
						             data: {sysDoctorId:"${registerServiceVo.sysDoctorId}",sysHospitalId:hospitalId,locationId:locationId,date:date,times:array,operRepeat:operRepeat,pageFlag:data.pageFlag},
						             dataType: "json",
						             success: function(data){
						            	 for(var i=0;i<timeList.length;i++){
											  timeList[i].checked=false;
									     }
						            	 document.getElementById("deleteRegisterArea").style.display="none";
						            	 var tempTimeList = array.split(";");
						            	 for(var i=0;i<tempTimeList.length;i++){
						            		 if(data.suc=="suc"){
								            		document.getElementById(tempTimeList[i]+"oper").innerText="添加";
									   			  	document.getElementById(tempTimeList[i]+"oper").value="";
									   			  	document.getElementById(tempTimeList[i]+"span").style.background="";
									   			  	if(operRepeat=="yes"){
									   			  		document.getElementById(tempTimeList[i]+"showInfo").innerText="";
									   			  	}
									   			 	addUpdateFlag="init";
							            	 }
						            	 }
						             }
					         	});
			            	 }else{
				            	 confirmx(data.reason+"确认删除吗？",function(){
				            		 $.ajax({
							             type: "post",
							             url: "${ctx}/register/removeRegister",
							             data: {sysDoctorId:"${registerServiceVo.sysDoctorId}",sysHospitalId:hospitalId,locationId:locationId,date:date,times:array,operRepeat:operRepeat,pageFlag:data.pageFlag},
							             dataType: "json",
							             success: function(data){
							            	 for(var i=0;i<timeList.length;i++){
												  timeList[i].checked=false;
										     }
							            	 document.getElementById("deleteRegisterArea").style.display="none";
							            	 var tempTimeList = array.split(";");
							            	 for(var i=0;i<tempTimeList.length;i++){
							            		 if(data.suc=="suc"){
									            		document.getElementById(tempTimeList[i]+"oper").innerText="添加";
										   			  	document.getElementById(tempTimeList[i]+"oper").value="";
										   			  	document.getElementById(tempTimeList[i]+"span").style.background="";
										   			  	if(operRepeat=="yes"){
										   			  		document.getElementById(tempTimeList[i]+"showInfo").innerText="";
										   			  	}
										   			 	addUpdateFlag="init";
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
				  window.location.href = "${ctx}/register/registerForm?id=${registerServiceVo.sysDoctorId}&pageFlag="+pageFlag;
		  	  }else if(pageFlag<=0){
		  	  	  window.location.href = "${ctx}/register/registerForm?id=${registerServiceVo.sysDoctorId}&pageFlag="+0;
		  	  }
		  }
		  function nextWeek(){
			  if(pageFlag<3){
				  ++pageFlag;
				  window.location.href = "${ctx}/register/registerForm?id=${registerServiceVo.sysDoctorId}&pageFlag="+pageFlag;
			  }else if(pageFlag>=3){
		  	  	  window.location.href = "${ctx}/register/registerForm?id=${registerServiceVo.sysDoctorId}&pageFlag="+3;
		  	  }
		  }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a>加号</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="registerServiceVo" action="${ctx}/register/save?" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<form:hidden path="sysDoctorId" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<input type="hidden" id="showTime" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<form:input path="sysHospitalId" type="hidden" id="sysHospitalId" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<form:input path="locationId" type="hidden" id="locationId" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">医生姓名:</label>
			<div class="controls">
				${registerServiceVo.doctorName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">所属医院:</label>
			<div class="controls">
				${registerServiceVo.hospitalName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">出诊地点:</label>
			<div class="controls">
				<ul  class="locationsun" id="locationsun">
					<c:forEach items="${locationList}" varStatus="status" var="doctorLocationVo">
						<LI><A href="#" onclick="change_location_bg(this,'${doctorLocationVo.sysHospitalId}','${doctorLocationVo.id}')" >(${ status.index + 1})${doctorLocationVo.hospitalName}${doctorLocationVo.location}</A></LI>
					</c:forEach>
				</ul>
			</div>
		</div>
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
				<table style="width: 600px">
					<tr>
						<td>
							<span>
								<input id="05:00" name="timeList" class="" type="checkbox" value="05:00" onclick="kick('05:00')">
								<label for="05:00"><span id="05:00span" name="spanList">05:00</span></label>
								<label><span id="05:00oper" name="operList" onclick="operRegister('05:00')">添加</span></label>
								<span id="05:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="05:15" name="timeList" class="" type="checkbox" value="05:15" onclick="kick('05:15')">
								<label for="05:15"><span id="05:15span" name="spanList">05:15</span></label>
								<label><span id="05:15oper" name="operList"  onclick="operRegister('05:15')">添加</span></label>
								<span id="05:15showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="05:30" name="timeList" class="" type="checkbox" value="05:30" onclick="kick('05:30')">
								<label for="05:30"><span id="05:30span" name="spanList">05:30</span></label>
								<label><span id="05:30oper" name="operList"  onclick="operRegister('05:30')">添加</span></label>
								<span id="05:30showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="05:45" name="timeList" class="" type="checkbox" value="05:45" onclick="kick('05:45')">
								<label for="05:45"><span id="05:45span" name="spanList">05:45</span></label>
								<label><span id="05:45oper" name="operList"  onclick="operRegister('05:45')">添加</span></label>
								<span id="05:45showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
					</tr>
					<tr>
						<td>
							<span>
								<input id="06:00" name="timeList" class="" type="checkbox" value="06:00" onclick="kick('06:00')">
								<label for="06:00"><span id="06:00span" name="spanList">06:00</span></label>
								<label><span id="06:00oper" name="operList" onclick="operRegister('06:00')">添加</span></label>
								<span id="06:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="06:15" name="timeList" class="" type="checkbox" value="06:15" onclick="kick('06:15')">
								<label for="06:15"><span id="06:15span" name="spanList">06:15</span></label>
								<label><span id="06:15oper" name="operList"  onclick="operRegister('06:15')">添加</span></label>
								<span id="06:15showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="06:30" name="timeList" class="" type="checkbox" value="06:30" onclick="kick('06:30')">
								<label for="06:30"><span id="06:30span" name="spanList">06:30</span></label>
								<label><span id="06:30oper" name="operList"  onclick="operRegister('06:30')">添加</span></label>
								<span id="06:30showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="06:45" name="timeList" class="" type="checkbox" value="06:45" onclick="kick('06:45')">
								<label for="06:45"><span id="06:45span" name="spanList">06:45</span></label>
								<label><span id="06:45oper" name="operList"  onclick="operRegister('06:45')">添加</span></label>
								<span id="06:45showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
					</tr>
					<tr>
						<td>
							<span>
								<input id="07:00" name="timeList" class="" type="checkbox" value="07:00" onclick="kick('07:00')">
								<label for="07:00"><span id="07:00span" name="spanList">07:00</span></label>
								<label><span id="07:00oper" name="operList" onclick="operRegister('07:00')">添加</span></label>
								<span id="07:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="07:15" name="timeList" class="" type="checkbox" value="07:15" onclick="kick('07:15')">
								<label for="07:15"><span id="07:15span" name="spanList">07:15</span></label>
								<label><span id="07:15oper" name="operList"  onclick="operRegister('07:15')">添加</span></label>
								<span id="07:15showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="07:30" name="timeList" class="" type="checkbox" value="07:30" onclick="kick('07:30')">
								<label for="07:30"><span id="07:30span" name="spanList">07:30</span></label>
								<label><span id="07:30oper" name="operList"  onclick="operRegister('07:30')">添加</span></label>
								<span id="07:30showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="07:45" name="timeList" class="" type="checkbox" value="07:45" onclick="kick('07:45')">
								<label for="07:45"><span id="07:45span" name="spanList">07:45</span></label>
								<label><span id="07:45oper" name="operList"  onclick="operRegister('07:45')">添加</span></label>
								<span id="07:45showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
					</tr>
					<tr>
						<td>
							<span>
								<input id="08:00" name="timeList" class="" type="checkbox" value="08:00" onclick="kick('08:00')">
								<label for="08:00"><span id="08:00span" name="spanList">08:00</span></label>
								<label><span id="08:00oper" name="operList"  onclick="operRegister('08:00')">添加</span></label>
								<span id="08:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="08:15" name="timeList" class="" type="checkbox" value="08:15" onclick="kick('08:15')">
								<label for="08:15"><span id="08:15span" name="spanList">08:15</span></label>
								<label><span id="08:15oper" name="operList"  onclick="operRegister('08:15')">添加</span></label>
								<span id="08:15showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="08:30" name="timeList" class="" type="checkbox" value="08:30" onclick="kick('08:30')">
								<label for="08:30"><span id="08:30span" name="spanList">08:30</span></label>
								<label><span id="08:30oper" name="operList"  onclick="operRegister('08:30')">添加</span></label>
								<span id="08:30showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="08:45" name="timeList" class="" type="checkbox" value="08:45" onclick="kick('08:45')">
								<label for="08:45"><span id="08:45span" name="spanList">08:45</span></label>
								<label><span id="08:45oper" name="operList"  onclick="operRegister('08:45')">添加</span></label>
								<span id="08:45showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
					</tr>
					<tr>
						<td>
							<span>
								<input id="09:00" name="timeList" class="" type="checkbox" value="09:00" onclick="kick('09:00')">
								<label for="09:00"><span id="09:00span" name="spanList">09:00</span></label>
								<label><span id="09:00oper" name="operList"  onclick="operRegister('09:00')">添加</span></label>
								<span id="09:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="09:15" name="timeList" class="" type="checkbox" value="09:15" onclick="kick('09:15')">
								<label for="09:15"><span id="09:15span" name="spanList">09:15</span></label>
								<label><span id="09:15oper" name="operList"  onclick="operRegister('09:15')">添加</span></label>
								<span id="09:15showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="09:30" name="timeList" class="" type="checkbox" value="09:30" onclick="kick('09:30')">
								<label for="09:30"><span id="09:30span" name="spanList">09:30</span></label>
								<label><span id="09:30oper" name="operList"  onclick="operRegister('09:30')">添加</span></label>
								<span id="09:30showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="09:45" name="timeList" class="" type="checkbox" value="09:45" onclick="kick('09:45')">
								<label for="09:45"><span id="09:45span" name="spanList">09:45</span></label>
								<label><span id="09:45oper" name="operList"  onclick="operRegister('09:45')">添加</span></label>
								<span id="09:45showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
					</tr>
					<tr>
						<td>
							<span>
								<input id="10:00" name="timeList" class="" type="checkbox" value="10:00" onclick="kick('10:00')">
								<label for="10:00"><span id="10:00span" name="spanList">10:00</span></label>
								<label><span id="10:00oper" name="operList"  onclick="operRegister('10:00')">添加</span></label>
								<span id="10:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="10:15" name="timeList" class="" type="checkbox" value="10:15" onclick="kick('10:15')">
								<label for="10:15"><span id="10:15span" name="spanList">10:15</span></label>
								<label><span id="10:15oper" name="operList"  onclick="operRegister('10:15')">添加</span></label>
								<span id="10:15showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="10:30" name="timeList" class="" type="checkbox" value="10:30" onclick="kick('10:30')">
								<label for="10:30"><span id="10:30span" name="spanList">10:30</span></label>
								<label><span id="10:30oper" name="operList"  onclick="operRegister('10:30')">添加</span></label>
								<span id="10:30showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="10:45" name="timeList" class="" type="checkbox" value="10:45" onclick="kick('10:45')">
								<label for="10:45"><span id="10:45span" name="spanList">10:45</span></label>
								<label><span id="10:45oper" name="operList"  onclick="operRegister('10:45')">添加</span></label>
								<span id="10:45showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
					</tr>
					<tr>
						<td>
							<span>
								<input id="11:00" name="timeList" class="" type="checkbox" value="11:00" onclick="kick('11:00')">
								<label for="11:00"><span id="11:00span" name="spanList">11:00</span></label>
								<label><span id="11:00oper" name="operList"  onclick="operRegister('11:00')">添加</span></label>
								<span id="11:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="11:15" name="timeList" class="" type="checkbox" value="11:15" onclick="kick('11:15')">
								<label for="11:15"><span id="11:15span" name="spanList">11:15</span></label>
								<label><span id="11:15oper" name="operList"  onclick="operRegister('11:15')">添加</span></label>
								<span id="11:15showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="11:30" name="timeList" class="" type="checkbox" value="11:30" onclick="kick('11:30')">
								<label for="11:30"><span id="11:30span" name="spanList">11:30</span></label>
								<label><span id="11:30oper" name="operList"  onclick="operRegister('11:30')">添加</span></label>
								<span id="11:30showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="11:45" name="timeList" class="" type="checkbox" value="11:45" onclick="kick('11:45')">
								<label for="11:45"><span id="11:45span" name="spanList">11:45</span></label>
								<label><span id="11:45oper" name="operList"  onclick="operRegister('11:45')">添加</span></label>
								<span id="11:45showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
					</tr>
					<tr>
						<td>
							<span>
								<input id="12:00" name="timeList" class="" type="checkbox" value="12:00" onclick="kick('12:00')">
								<label for="12:00"><span id="12:00span" name="spanList">12:00</span></label>
								<label><span id="12:00oper" name="operList"  onclick="operRegister('12:00')">添加</span></label>
								<span id="12:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="12:15" name="timeList" class="" type="checkbox" value="12:15" onclick="kick('12:15')">
								<label for="12:15"><span id="12:15span" name="spanList">12:15</span></label>
								<label><span id="12:15oper" name="operList"  onclick="operRegister('12:15')">添加</span></label>
								<span id="12:15showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="12:30" name="timeList" class="" type="checkbox" value="12:30" onclick="kick('12:30')">
								<label for="12:30"><span id="12:30span" name="spanList">12:30</span></label>
								<label><span id="12:30oper" name="operList"  onclick="operRegister('12:30')">添加</span></label>
								<span id="12:30showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="12:45" name="timeList" class="" type="checkbox" value="12:45" onclick="kick('12:45')">
								<label for="12:45"><span id="12:45span" name="spanList">12:45</span></label>
								<label><span id="12:45oper" name="operList"  onclick="operRegister('12:45')">添加</span></label>
								<span id="12:45showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="control-group"  style="margin-left: -100px">
			<label class="control-label">下午:</label>
			<div class="controls">
				<table style="width: 600px">
					<tr>
						<td>
							<span>
								<input id="13:00" name="timeList" class="" type="checkbox" value="13:00" onclick="kick('13:00')">
								<label for="13:00"><span id="13:00span" name="spanList">13:00</span></label>
								<label><span id="13:00oper" name="operList"  onclick="operRegister('13:00')">添加</span></label>
								<span id="13:00showInfo" name="showInfoList"></span>
							</span>
						</td>
						<td>
							<span>
								<input id="13:15" name="timeList" class="" type="checkbox" value="13:15" onclick="kick('13:15')">
								<label for="13:15"><span id="13:15span" name="spanList">13:15</span></label>
								<label><span id="13:15oper" name="operList"  onclick="operRegister('13:15')">添加</span></label>
								<span id="13:15showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="13:30" name="timeList" class="" type="checkbox" value="13:30" onclick="kick('13:30')">
								<label for="13:30"><span id="13:30span" name="spanList">13:30</span></label>
								<label><span id="13:30oper" name="operList"  onclick="operRegister('13:30')">添加</span></label>
								<span id="13:30showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="13:45" name="timeList" class="" type="checkbox" value="13:45" onclick="kick('13:45')">
								<label for="13:45"><span id="13:45span" name="spanList">13:45</span></label>
								<label><span id="13:45oper" name="operList"  onclick="operRegister('13:45')">添加</span></label>
								<span id="13:45showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
					</tr>
					<tr>
						<td>
							<span>
								<input id="14:00" name="timeList" class="" type="checkbox" value="14:00" onclick="kick('14:00')">
								<label for="14:00"><span id="14:00span" name="spanList">14:00</span></label>
								<label><span id="14:00oper" name="operList"  onclick="operRegister('14:00')">添加</span></label>
								<span id="14:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="14:15" name="timeList" class="" type="checkbox" value="14:15" onclick="kick('14:15')">
								<label for="14:15"><span id="14:15span" name="spanList">14:15</span></label>
								<label><span id="14:15oper" name="operList"  onclick="operRegister('14:15')">添加</span></label>
								<span id="14:15showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="14:30" name="timeList" class="" type="checkbox" value="14:30" onclick="kick('14:30')">
								<label for="14:30"><span id="14:30span" name="spanList">14:30</span></label>
								<label><span id="14:30oper" name="operList"  onclick="operRegister('14:30')">添加</span></label>
								<span id="14:30showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="14:45" name="timeList" class="" type="checkbox" value="14:45" onclick="kick('14:45')">
								<label for="14:45"><span id="14:45span" name="spanList">14:45</span></label>
								<label><span id="14:45oper" name="operList"  onclick="operRegister('14:45')">添加</span></label>
								<span id="14:45showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
					</tr>
					<tr>
						<td>
							<span>
								<input id="15:00" name="timeList" class="" type="checkbox" value="15:00" onclick="kick('15:00')">
								<label for="15:00"><span id="15:00span" name="spanList">15:00</span></label>
								<label><span id="15:00oper" name="operList"  onclick="operRegister('15:00')">添加</span></label>
								<span id="15:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="15:15" name="timeList" class="" type="checkbox" value="15:15" onclick="kick('15:15')">
								<label for="15:15"><span id="15:15span" name="spanList">15:15</span></label>
								<label><span id="15:15oper" name="operList"  onclick="operRegister('15:15')">添加</span></label>
								<span id="15:15showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="15:30" name="timeList" class="" type="checkbox" value="15:30" onclick="kick('15:30')">
								<label for="15:30"><span id="15:30span" name="spanList">15:30</span></label>
								<label><span id="15:30oper" name="operList"  onclick="operRegister('15:30')">添加</span></label>
								<span id="15:30showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="15:45" name="timeList" class="" type="checkbox" value="15:45" onclick="kick('15:45')">
								<label for="15:45"><span id="15:45span" name="spanList">15:45</span></label>
								<label><span id="15:45oper" name="operList"  onclick="operRegister('15:45')">添加</span></label>
								<span id="15:45showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
					</tr>
					<tr>
						<td>
							<span>
								<input id="16:00" name="timeList" class="" type="checkbox" value="16:00" onclick="kick('16:00')">
								<label for="16:00"><span id="16:00span" name="spanList">16:00</span></label>
								<label><span id="16:00oper" name="operList"  onclick="operRegister('16:00')">添加</span></label>
								<span id="16:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="16:15" name="timeList" class="" type="checkbox" value="16:15" onclick="kick('16:15')">
								<label for="16:15"><span id="16:15span" name="spanList">16:15</span></label>
								<label><span id="16:15oper" name="operList"  onclick="operRegister('16:15')">添加</span></label>
								<span id="16:15showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="16:30" name="timeList" class="" type="checkbox" value="16:30" onclick="kick('16:30')">
								<label for="16:30"><span id="16:30span" name="spanList">16:30</span></label>
								<label><span id="16:30oper" name="operList"  onclick="operRegister('16:30')">添加</span></label>
								<span id="16:30showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="16:45" name="timeList" class="" type="checkbox" value="16:45" onclick="kick('16:45')">
								<label for="16:45"><span id="16:45span" name="spanList">16:45</span></label>
								<label><span id="16:45oper" name="operList"  onclick="operRegister('16:45')">添加</span></label>
								<span id="16:45showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
					</tr>
					<tr>
						<td>
							<span>
								<input id="17:00" name="timeList" class="" type="checkbox" value="17:00" onclick="kick('17:00')">
								<label for="17:00"><span id="17:00span" name="spanList">17:00</span></label>
								<label><span id="17:00oper" name="operList"  onclick="operRegister('17:00')">添加</span></label>
								<span id="17:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="17:15" name="timeList" class="" type="checkbox" value="17:15" onclick="kick('17:15')">
								<label for="17:15"><span id="17:15span" name="spanList">17:15</span></label>
								<label><span id="17:15oper" name="operList"  onclick="operRegister('17:15')">添加</span></label>
								<span id="17:15showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="17:30" name="timeList" class="" type="checkbox" value="17:30" onclick="kick('17:30')">
								<label for="17:30"><span id="17:30span" name="spanList">17:30</span></label>
								<label><span id="17:30oper" name="operList"  onclick="operRegister('17:30')">添加</span></label>
								<span id="17:30showInfo" name="showInfoList"></span>&nbsp;&nbsp;&nbsp;
							</span>
						</td>
						<td>
							<span>
								<input id="17:45" name="timeList" class="" type="checkbox" value="17:45" onclick="kick('17:45')">
								<label for="17:45"><span id="17:45span" name="spanList">17:45</span></label>
								<label><span id="17:45oper" name="operList"  onclick="operRegister('17:45')">添加</span></label>
								<span id="17:45showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
					</tr>
					<tr>
						<td>
							<span>
								<input id="18:00" name="timeList" class="" type="checkbox" value="18:00" onclick="kick('18:00')">
								<label for="18:00"><span id="18:00span" name="spanList">18:00</span></label>
								<label><span id="18:00oper" name="operList"  onclick="operRegister('18:00')">添加</span></label>
								<span id="18:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="18:15" name="timeList" class="" type="checkbox" value="18:15" onclick="kick('18:15')">
								<label for="18:15"><span id="18:15span" name="spanList">18:15</span></label>
								<label><span id="18:15oper" name="operList"  onclick="operRegister('18:15')">添加</span></label>
								<span id="18:15showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="18:30" name="timeList" class="" type="checkbox" value="18:30" onclick="kick('18:30')">
								<label for="18:30"><span id="18:30span" name="spanList">18:30</span></label>
								<label><span id="18:30oper" name="operList"  onclick="operRegister('18:30')">添加</span></label>
								<span id="18:30showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="18:45" name="timeList" class="" type="checkbox" value="18:45" onclick="kick('18:45')">
								<label for="18:45"><span id="18:45span" name="spanList">18:45</span></label>
								<label><span id="18:45oper" name="operList"  onclick="operRegister('18:45')">添加</span></label>
								<span id="18:45showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
					</tr>
					<tr>
						<td>
							<span>
								<input id="19:00" name="timeList" class="" type="checkbox" value="19:00" onclick="kick('19:00')">
								<label for="19:00"><span id="19:00span" name="spanList">19:00</span></label>
								<label><span id="19:00oper" name="operList"  onclick="operRegister('19:00')">添加</span></label>
								<span id="19:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="19:15" name="timeList" class="" type="checkbox" value="19:15" onclick="kick('19:15')">
								<label for="19:15"><span id="19:15span" name="spanList">19:15</span></label>
								<label><span id="19:15oper" name="operList"  onclick="operRegister('19:15')">添加</span></label>
								<span id="19:15showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="19:30" name="timeList" class="" type="checkbox" value="19:30" onclick="kick('19:30')">
								<label for="19:30"><span id="19:30span" name="spanList">19:30</span></label>
								<label><span id="19:30oper" name="operList"  onclick="operRegister('19:30')">添加</span></label>
								<span id="19:30showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="19:45" name="timeList" class="" type="checkbox" value="19:45" onclick="kick('19:45')">
								<label for="19:45"><span id="19:45span" name="spanList">19:45</span></label>
								<label><span id="19:45oper" name="operList"  onclick="operRegister('19:45')">添加</span></label>
								<span id="19:45showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
					</tr>
					<tr>
						<td>
							<span>
								<input id="20:00" name="timeList" class="" type="checkbox" value="20:00" onclick="kick('20:00')">
								<label for="20:00"><span id="20:00span" name="spanList">20:00</span></label>
								<label><span id="20:00oper" name="operList"  onclick="operRegister('20:00')">添加</span></label>
								<span id="20:00showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="20:15" name="timeList" class="" type="checkbox" value="20:15" onclick="kick('20:15')">
								<label for="20:15"><span id="20:15span" name="spanList">20:15</span></label>
								<label><span id="20:15oper" name="operList"  onclick="operRegister('20:15')">添加</span></label>
								<span id="20:15showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="20:30" name="timeList" class="" type="checkbox" value="20:30" onclick="kick('20:30')">
								<label for="20:30"><span id="20:30span" name="spanList">20:30</span></label>
								<label><span id="20:30oper" name="operList"  onclick="operRegister('20:30')">添加</span></label>
								<span id="20:30showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
						<td>
							<span>
								<input id="20:45" name="timeList" class="" type="checkbox" value="20:45" onclick="kick('20:45')">
								<label for="20:45"><span id="20:45span" name="spanList">20:45</span></label>
								<label><span id="20:45oper" name="operList"  onclick="operRegister('20:45')">添加</span></label>
								<span id="20:45showInfo" name="showInfoList">&nbsp;&nbsp;&nbsp;</span>
							</span>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div id="addRegisterArea" style="display: none">
			<div class="control-group"  style="margin-left: -100px">
				<label class="control-label">价格:</label>
				<div class="controls">
					<input id="price" name="price" htmlEscape="false" maxlength="50"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group"  style="margin-left: -100px">
				<label class="control-label">服务类型:</label>
				<div class="controls">
					<select id="serverType" name="serverType" htmlEscape="false" maxlength="50" onchange="serverChange()">
						<option value="1">专家门诊</option>
						<option value="2">专家门诊需等待</option>
						<option value="3">特需门诊</option>
						<option value="4">特需门诊需等待</option>
						<option value="5">私立医院</option>
					</select>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group"  style="margin-left: -100px">
				<div class="controls" >
					<input id="copy" name="copy" type="checkbox" onclick="choiceInterval()" value="yes">
					<label for="copy" >自动按周重复该设置</label>
				</div>
				<div class="controls" >
					<input id="interval1" name="interval" checked="checked" type="radio" value="0">
					<label for="interval1" >每周重复</label>
					<input id="interval2" name="interval" type="radio" value="1">
					<label for="interval2" >隔周重复</label>
				</div>
			</div>
			<div class="form-actions">
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
	</form:form>
</body>
</html>