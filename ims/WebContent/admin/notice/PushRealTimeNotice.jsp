<%@page import="websocket.server.NotifyAllEndpoint"%>
<%@page import="common.util.Filter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%--Permission Check--%>
<%@ include file="/include/jsp/PermissionCheck.jsp"%>
<%
	String message = Filter.nullFilter(request.getParameter("message"));
	if (message != null && "" != message.trim()) {
		NotifyAllEndpoint.pushMessage(message);
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Push Real Time Notice</title>
<%--page style--%>
<link rel="stylesheet" href="../../include/css/page.css">
<script type="text/javascript">
	function reset() {
		alert("clicked");
		document.getElementById("message").innerHTML = "";
	}
</script>
</head>
<body>
	<%--Page Navigation Menu --%>
	<%@ include file="/include/jsp/body/Menu.jsp"%>

	<%--Page Main Content --%>
	<div class="content">
		<div>
			<h1>Push Real Time Notice to All Signed In User</h1>
		</div>
		<div>
			<form id="pushForm" method="post">
				Input Message:
				<textarea rows="5" cols="50" name="message" id="message"><%=message%></textarea>
				<input type="button" value="Clear Content" id="reset"
					onclick="reset()"><input type="submit" value="Push Now"
					id="submit">
			</form>
		</div>
	</div>

	<%--Page Footer --%>
	<%@ include file="/include/jsp/body/Footer.jsp"%>
</body>
</html>