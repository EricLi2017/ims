<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.SQLException"%>
<%@page import="common.db.DB"%>
<%@page import="common.util.MD5"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	//get param values
	String user = request.getParameter("user");
	String password = request.getParameter("password");
	String code = request.getParameter("code");//verification code
	//get visitor ip
	String ip = common.util.IP.getIpAddr(request);
	//get verification code from session
	String verifyCode = session.getAttribute("code") == null ? null
			: String.valueOf(session.getAttribute("code"));

	//Note: only post method was permitted
	if (!"POST".equalsIgnoreCase(request.getMethod())) {
		out.println("Illegal access!");
		System.out.println("Illegal access: Incorrect request submit method! user:" + user + " ip:" + ip);
		return;
	}
	//Note: only internal login attempt was permitted
	if (verifyCode == null) {
		out.println("Illegal access!");
		System.out.println("Illegal access: Could not get verification code! user:" + user + " ip:" + ip);
		return;
	}

	//empty value checking
	if (user == null || password == null || code == null || "".equals(user.trim()) || "".equals(password.trim())
			|| "".equals(code.trim())) {
		out.println("Missing required information to sign in!");
		System.out.println("Invalid login, missing required parameters! user:" + user + " ip:" + ip);
		return;
	}
	//verification code matching
	if (!code.equalsIgnoreCase(session.getAttribute("code").toString())) {
		out.println("Incorrect verification code!");
		//TODO: refresh the verification code in session

		System.out.println("Invalid login, incorrect verification code! user:" + user + " ip:" + ip);
		return;
	}

	String pwdMD5 = MD5.getMd5String(password);

	//database checking
	boolean isLoginSuccess = false;
	String sql = "select * from user where name=? and password=?";
	Connection con = null;
	try {
		con = DB.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, user);
		ps.setString(2, pwdMD5);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			//login success
			session.setAttribute("user", user);
			isLoginSuccess = true;
		}
		rs.close();
		ps.close();
		con.close();
	} catch (ClassNotFoundException | SQLException e) {
		e.printStackTrace();
	} finally {
		boolean flag = true;
		try {
			if (con == null || con.isClosed()) {
				flag = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (flag) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	if (isLoginSuccess) {
		String url = request.getParameter("srcUrl");
		System.out.println("User " + user + " login successful from ip:" + ip);
		response.sendRedirect("/ims/index.jsp");
		return;
	} else {
		out.println("Invalid login");
		System.out.println("User " + user + " login failed from ip:" + ip);
		return;
	}
%>