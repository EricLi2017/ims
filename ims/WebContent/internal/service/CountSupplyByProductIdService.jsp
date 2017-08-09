<%@page import="internal.db.query.SupplyQuerier"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%--Permission Check--%>
<%@ include file="/include/jsp/PermissionCheck.jsp"%>
<%
	//get param from request
	String productId = request.getParameter("productId");

	//validate param required
	if (productId == null || productId.trim() == "") {
		out.print("Missing parameter!");
		return;
	}

	//validate param format
	boolean isInt = false;
	try {
		Integer.parseInt(productId);
		isInt = true;
	} catch (NumberFormatException e) {
		e.printStackTrace();
	}
	if (!isInt) {
		out.print("Incorrect parameter format!");
		return;
	}

	//action
	int count = new SupplyQuerier().countByProductId(Integer.parseInt(productId));

	//respond
	out.print(count);
%>