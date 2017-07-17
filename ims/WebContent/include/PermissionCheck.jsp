
<%
	Object userObject = session.getAttribute("user");
	String user = userObject == null ? null : userObject.toString();

	if (user == null || user.trim().isEmpty()) {
		//log
		String ip = common.util.IP.getIpAddr(request);
		System.out.print(request.getRequestURI());
		System.out.print(" illegally accessed by a time-out or not-login user via ip ");
		System.out.println(ip);
		//response
		response.sendRedirect("/ims/user/Login.jsp");
		return;
	} else {
		//log
		String ip = common.util.IP.getIpAddr(request);
		System.out.print(request.getRequestURI());
		System.out.print(" accessed by user ");
		System.out.print(user);
		System.out.print(" via ip ");
		System.out.println(ip);
	}
%>