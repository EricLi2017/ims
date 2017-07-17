
<%
	//get user from session
	Object userObject = session.getAttribute("user");
	String user = String.valueOf(userObject);

	//invalidate session
	session.invalidate();
	System.out.println("user " + user + " sign out");

	//redirect page
	response.sendRedirect("/ims/user/Login.jsp");
	return;
%>