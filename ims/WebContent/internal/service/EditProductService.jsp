<%@page import="internal.db.edit.InternalProductDatabase"%>
<%@page import="internal.db.model.InternalProduct"%>
<%@page import="common.util.Filter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%--Permission Check--%>
<%@ include file="/include/PermissionCheck.jsp"%>
<%
	//set requet encoding
	request.setCharacterEncoding("UTF-8");
	//get param from request
	String id = Filter.nullFilter(request.getParameter("id"));
	String name = Filter.nullFilter(request.getParameter("name"));
	String description = Filter.nullFilter(request.getParameter("description"));
	String asin = Filter.nullFilter(request.getParameter("asin"));
	String status = Filter.nullFilter(request.getParameter("status"));

	//validate param requried
	if (id.isEmpty() || name.isEmpty() || status.isEmpty()) {
		out.print("Missing required parameters!");
		return;
	}

	//validate param format
	//change string to int
	int productId = 0;
	boolean isInt = false;
	try {
		productId = Integer.parseInt(id);
		isInt = true;
	} catch (NumberFormatException e) {
		e.printStackTrace();
	}
	if (!isInt) {
		out.print("Incorrect parameter format!");
		return;
	}

	//edit data from database
	InternalProduct product = new InternalProduct();
	product.setName(name);
	product.setDescription(description.isEmpty() ? null : description);
	product.setAsin(asin.isEmpty() ? null : asin);
	product.setStatus(status);

	product.setProductId(productId);
	int rows = new InternalProductDatabase().updateById(product);

	//response
	String title = rows > 0 ? "Success" : "Failed";
	String titleColor = rows > 0 ? "green" : "red";
	String detail = "Update product " + productId + ", " + rows + "  rows affected!";
	response.sendRedirect("Result.jsp?title=" + title + "&titleColor=" + titleColor + "&detail=" + detail);
%>