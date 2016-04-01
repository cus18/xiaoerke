<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta name="decorator" content="default" />
</head>
<body>
	<div style="display: none">
		<form id="inputForm" action="authorize" method="post">
			<input type="hidden" name="user_oauth_approval" value="true"/>	
			<input type="submit">
		</form>
	</div>
</body>

<script type="text/javascript">
	//页面加载完，自动跳转确认授权
		document.getElementById("inputForm").submit();
</script>

</html>