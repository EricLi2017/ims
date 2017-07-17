<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%--Permission Check--%>
<%@ include file="/include/PermissionCheck.jsp"%>
<%
	//set requet encoding
	request.setCharacterEncoding("UTF-8");

	//get request param
	//title 
	String title = request.getParameter("title") == null ? "" : request.getParameter("title");
	String titleColor = request.getParameter("titleColor") == null ? "#000000"
			: request.getParameter("titleColor").toString();
	//detail
	String detail = request.getParameter("detail");
%>
<%-- title --%>
<h1 align="center">
	<font color="<%=titleColor%>"><%=title%></font>
</h1>
<%-- detail --%>
<%
	if (detail != null) {
		out.print(detail);
	}
%>