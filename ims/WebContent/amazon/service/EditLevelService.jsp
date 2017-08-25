<%@page import="amazon.db.edit.AmazonProductEditor"%>
<%@page import="common.util.Filter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%--Permission Check--%>
<%@ include file="/include/jsp/PermissionCheck.jsp"%>
<%
	//get param from request
	String sku = Filter.nullFilter(request.getParameter("sku"));
	String level = Filter.nullFilter(request.getParameter("level"));

	//validate param requried
	if (sku.isEmpty() || level.isEmpty()) {
		out.print("Missing required parameters!");
		return;
	}

	//validate param format
	boolean isInt = false;
	try {
		Integer.parseInt(level);
		isInt = true;
	} catch (NumberFormatException e) {
		e.printStackTrace();
	}
	if (!isInt) {
		out.print("Incorrect parameter format!");
		return;
	}

	//edit data from database

	int rows = AmazonProductEditor.updateLevelByAsin(Integer.parseInt(level), sku);

	//response
	String title = rows > 0 ? "Success" : "Failed";
	String titleColor = rows > 0 ? "green" : "red";
	String detail = "Update product (sku=" + sku + ") level to " + level + ", " + rows + "  rows affected!";
	response.sendRedirect("Result.jsp?title=" + title + "&titleColor=" + titleColor + "&detail=" + detail);
%>