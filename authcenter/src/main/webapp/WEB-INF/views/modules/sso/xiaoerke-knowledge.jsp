<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="org.springframework.security.web.WebAttributes"%>

<!DOCTYPE html>
<html ng-app="app">
<head lang="en">
    <meta charset="UTF-8">
    <title>登录</title>
</head>
<body ng-controller="Login">
    <form id="loginForm" class="form-signin" action="auth_center/form" method="post">
	    <div id="b-1">
	        <div id="b-2"></div>
	        <div id="b-3">
	            <div class="b-1">登录</div>
	            <div class="b-2">
	                <div class="b-3">
	                    <img src="${pageContext.request.contextPath}/images/39.png" alt=""/>
	                </div>
	                <input id="username" name="username" type="text" class="form-control" placeholder="手机号登录" />
	            </div>
	            <div class="b-2 b-5">
	                <div class="b-3">
	                    <img src="${pageContext.request.contextPath}/images/40.png" alt=""/>
	                </div>
	                <input id="password" name="password" type="password" class="form-control" placeholder="" />
	            </div>
	            <input name="toUrl" type="hidden" value="${toUrl}" />
	            <input id="submitButton" type="submit" value="确认登录" />
				<%
					Exception error = (Exception)request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
					if(error!=null){
						if(error.getMessage().equals("empty_username")){
				%>
				<div id="info" style="color:red;font-size:10px;margin-top:25px;margin-left:135px;">手机号为空</div>
				<% }else if(error.getMessage().equals("empty_password"))  {%> <div id="info" style="color:red;font-size:10px;margin-top:25px;margin-left:135px;">验证码为空</div>
				<% }else if(error.getMessage().equals("invalid_verifycode"))  {%> <div id="info" style="color:red;font-size:10px;margin-top:25px;margin-left:135px;">验证码错误</div>
				<% }} %>
	        </div>
	    </div>
	</form>
</body>
</html>