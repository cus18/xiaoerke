<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>赠送保险</title>
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
			$(".tips").hide();
		});
		
		//校验身份证号
		var checkIdCard= function(){
		    /* $scope.info.IdCardNum=num;*/
		    var sId =$("#idCard").val();
		
		    var aCity={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",
		        23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",
		        41:"河南",42:"湖北", 43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",
		        52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",
		        71:"台湾",81:"香港",82:"澳门",91:"国外"}
		
		    //身份证最后一位的算法
		    var iSum=0;
		    for(var i = 17;i>=0;i --) {
		        iSum += (Math.pow(2,i) % 11) * parseInt(sId.charAt(17 - i),11)
		    }
		    sId=sId.replace(/x$/i,"a");
		    var sBirthday=sId.substr(6,4)+"-"+Number(sId.substr(10,2))+"-"+Number(sId.substr(12,2));
		    var d=new Date(sBirthday.replace(/-/g,"/"));
		    if(!/^\d{17}(\d|x)$/i.test(sId)){
		      /*  $("#IdCard").focus()*/
		        $(".tips").show();
		        $(".tips").html("身份证号不正确");
		        return false;
		    }
		    else if(aCity[parseInt(sId.substr(0,2))]==null)
		    {
		       /* $("#IdCard").focus()*/
		        $(".tips").css("display","block");
		        $(".tips").html("身份证号不正确");
		        return false;
		    }
		
		    else  if(sBirthday!=(d.getFullYear()+"-"+ (d.getMonth()+1) + "-" + d.getDate()))
		    {
		     /*   $("#IdCard").focus()*/
		        $(".tips").show();
		        $(".tips").html("身份证号不正确");
		        return false;
		    }
		    else if(iSum%11!=1){
		       /* $("#IdCard").focus()*/
		        $(".tips").show();
		        $(".tips").html("身份证号不正确");
		        return false;
		    }
		    else{
		        $(".tips").hide();
		    }
		    /*  return aCity[parseInt(sId.substr(0,2))]+","+sBirthday+","+(sId.substr(16,1)%2?"男":"女")*/
		
		
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/insurance/insuranceList?">订单列表</a></li>
		<li><a href="${ctx}/insurance/insuranceList?stateFlag=2">待审核</a></li>
		<li><a href="${ctx}/insurance/insuranceList?stateFlag=3">已补贴</a></li>
		<li class="active"><a>赠送保险</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="InsuranceRegisterService" action="${ctx}/insurance/giveInsurance?" method="post" class="form-horizontal"><%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<sys:message content="${message}"/>
		宝宝信息
		<div class="control-group">
			<label class="control-label">姓名:</label>
			<div class="controls">
				<form:input id="babyName" path="babyName" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">性别:</label>
			<div class="controls">
				<form:select path="gender" class="input-mini">
					<form:option value="0" label="男"/>
					<form:option value="1" label="女"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">出生日期:</label>
			<div class="controls">
				<form:input id="birthday" path="birthday" type="text" readonly="readonly" maxlength="20" class="input-small Wdate"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		家长信息
		<div class="control-group">
			<label class="control-label">姓名:</label>
			<div class="controls">
				<form:input id="parentName" path="parentName" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">关系:</label>
			<div class="controls">
				<form:select path="parentType" class="input-mini">
					<form:option value="0" label="宝爸"/>
					<form:option value="1" label="宝妈"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">手机号码:</label>
			<div class="controls">
				<form:input id="parentPhone" path="parentPhone" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">身份证号:</label>
			<div class="controls">
				<form:input id="idCard" path="idCard" onblur="checkIdCard();" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font class="tips" color="red">身份证号有误</font><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit"  value="确认赠送"/>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>