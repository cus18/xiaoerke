<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="org.springframework.security.web.WebAttributes"%>
<html>
<head>
</head>
<base href="/authcenter/"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta name="viewport" content="width=device-width,height=device-height,
inital-scale=1.0,maximum-scale=1.0,user-scalable=no;">
<title>宝大夫</title>
<script src="js/libs/jquery-2.1.3.min.js"></script>
<script src="js/titan.js"></script>
<link rel="stylesheet" href="styles/loginCommon.less" type="text/css" />
<body>
<div class="login-container appoint ">
	<div class="head">
		<h1 class="title" >登录</h1>
	</div>
	<form id="loginForm" class="form-signin" action="sso/auth_center/form" method="post">
		<div class="login-info">
			<ul >
				<li>
					<span >手机号</span>
					<input type="text" id="username" name="username" placeholder="请输入手机号" maxlength="11">
				</li>
				<li>
					<span >验证码</span>
					<input type="text" id="password" name="password" placeholder="请输入验证码" maxlength="6">
					<div class="button btn-code" id="validateCode" onclick="getValidateCode()">
						<div id="code" >获取验证码</div>
					</div>
				</li>
			</ul>
			<%
				Exception error = (Exception)request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
				if(error!=null){
					if(error.getMessage().equals("empty_username")){
			%>
			<div class="error" id="info" >手机号为空</div>
			<% }else if(error.getMessage().equals("empty_password"))  {%> <div class="error" id="info" >验证码为空</div>
			<% }else if(error.getMessage().equals("invalid_verifycode"))  {%> <div class="error" id="info" >验证码错误</div>
			<% }} %>
		</div>

		<input name="toUrl" type="hidden" value="${toUrl}" />
		<button class="button foot-btn" id="appointButton">
			确定
		</button>
	</form>
</div>
</body>
</html>