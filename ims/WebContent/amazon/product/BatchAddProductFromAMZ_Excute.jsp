<%@page import="amazon.db.edit.InventoryReportDatabase"%>
<%@page import="amazon.mws.product.InventoryReportParser"%>
<%@page import="amazon.db.model.Product"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/include/PermissionCheck.jsp"%>
<%
	//validate file
	String fileName = (String) request.getAttribute("fileName");
	if (fileName == null)
		throw new IllegalArgumentException("File name is null");
	//文件类型判断
	/* if (!fileName.substring(fileName.length() - 3, fileName.length()).equalsIgnoreCase("xls"))
		throw new Exception("文件类型不对!xls"); */

	//insert product list information into database
	List<Product> products = InventoryReportParser.parse(fileName);
	for (Product product : products) {
		System.out.println(product);
	}
	int rows = new InventoryReportDatabase().insert(products);
	System.out.println("insert into database rows num: " + rows);
%>