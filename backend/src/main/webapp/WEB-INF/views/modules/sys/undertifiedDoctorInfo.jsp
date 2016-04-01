<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>未审核医生</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th class="sort-column doctorName">医生姓名</th><th>所属医院</th><th>所属科室</th><th>创建时间</th><th>手机</th>
		</tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="unCertifiedDoctorInfo">
			<tr>
				<td>${unCertifiedDoctorInfo.name}</td>
				<td>${unCertifiedDoctorInfo.hospital}</td>
				<td>${unCertifiedDoctorInfo.department}</td>
				<td>${unCertifiedDoctorInfo.mobile}</td>
				<td>${unCertifiedDoctorInfo.cdate}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>