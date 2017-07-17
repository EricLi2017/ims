<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	//generate verification code
	java.util.Random r = new java.util.Random();
	String code = "";
	for (int i = 0; i < 4; i++) {
		code += r.nextInt(10);
	}

	//set verification code into session
	session.setAttribute("code", code);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Sign In</title>
</head>
<body>
	<h2>IMS</h2>
	
	<form action="LoginExcute.jsp" method="post">
		<table>
			<tr>
				<td>Name:</td>
				<td><input type="text" name="user" title="" />
			</tr>
			<tr>
				<td>Password:</td>
				<td><input type="password" name="password" title="" />
			</tr>
			<tr>
				<td>Code:</td>
				<td><input type="text" name="code" title="" /> <%=session.getAttribute("code")%>
			</tr>
			<tr>
				<td><input type="submit" value="Sign In"> <input
					type="hidden" name="srcUrl"
					value="<%=request.getParameter("srcUrl")%>"></td>
			</tr>
		</table>
	</form>

</body>
</html>