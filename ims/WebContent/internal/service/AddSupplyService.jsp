<%@page import="java.text.ParseException"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="internal.supply.ProductSupplyDatabase"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="database.model.ProductSupply"%>
<%@page import="common.util.Filter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%--Permission Check--%>
<%@ include file="/include/PermissionCheck.jsp"%>
<%
	//set requet encoding
	request.setCharacterEncoding("UTF-8");
	//get param from request
	String productId = Filter.nullFilter(request.getParameter("productId"));
	String supplierId = Filter.nullFilter(request.getParameter("supplierId"));
	String supplierName = Filter.nullFilter(request.getParameter("supplierName"));
	String supplierDescription = Filter.nullFilter(request.getParameter("supplierDescription"));
	String supplyType = Filter.nullFilter(request.getParameter("supplyType"));
	String supplyUrl = Filter.nullFilter(request.getParameter("supplyUrl"));
	String shippedFrom = Filter.nullFilter(request.getParameter("shippedFrom"));
	String unitPrice = Filter.nullFilter(request.getParameter("unitPrice"));
	String priceDescription = Filter.nullFilter(request.getParameter("priceDescription"));
	String priceTime = Filter.nullFilter(request.getParameter("priceTime"));
	String status = Filter.nullFilter(request.getParameter("status"));

	//validate param required
	if (productId.isEmpty()) {
		out.print("Missing required parameter: Product ID");
		return;
	}
	if (status.isEmpty()) {
		out.print("Missing required parameter: Status");
		return;
	}

	//validate param format
	String pattern = "yyyy-MM-dd";
	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	boolean isCorrectFormat = false;
	try {
		Integer.parseInt(productId);
		if (!supplierId.isEmpty()) {
			Integer.parseInt(supplierId);//maybe throw NumberFormatException
		}
		if (!unitPrice.isEmpty()) {
			new BigDecimal(unitPrice);//maybe throw NumberFormatException
		}
		if (!priceTime.isEmpty()) {
			sdf.parse(priceTime);//maybe throw ParseException
		}

		isCorrectFormat = true;
	} catch (NumberFormatException | ParseException e) {
		e.printStackTrace();
	}
	if (!isCorrectFormat) {
		out.print("Incorrect parameter format!");
		return;
	}

	//insert data to database
	ProductSupply supply = new ProductSupply();
	supply.setProductId(Integer.parseInt(productId));
	supply.setSupplierId(supplierId.isEmpty() ? null : Integer.parseInt(supplierId));
	supply.setSupplierName(supplierName.isEmpty() ? null : supplierName);
	supply.setSupplierDescription(supplierDescription.isEmpty() ? null : supplierDescription);
	supply.setSupplyType(supplyType.isEmpty() ? null : supplyType);
	supply.setSupplyUrl(supplyUrl.isEmpty() ? null : supplyUrl);
	supply.setShippedFrom(shippedFrom.isEmpty() ? null : shippedFrom);
	supply.setUnitPrice(unitPrice.isEmpty() ? null : new BigDecimal(unitPrice));
	supply.setPriceDescription(priceDescription.isEmpty() ? null : priceDescription);
	supply.setPriceTime(priceTime.isEmpty() ? null : sdf.parse(priceTime));
	supply.setStatus(status);

	List<ProductSupply> supplies = new ArrayList<>();
	supplies.add(supply);
	int rows = new ProductSupplyDatabase().insert(supplies);

	//response
	String title = rows > 0 ? "Success" : "Failed";
	String titleColor = rows > 0 ? "green" : "red";
	String detail = "insert rows : " + rows;
	response.sendRedirect("Result.jsp?title=" + title + "&titleColor=" + titleColor + "&detail=" + detail);
%>