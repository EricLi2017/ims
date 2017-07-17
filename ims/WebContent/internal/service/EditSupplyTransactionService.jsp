<%@page import="internal.db.edit.SupplyTransactionDatabase"%>
<%@page import="internal.db.model.SupplyTransaction"%>
<%@page import="java.text.ParseException"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="common.util.Filter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%--Permission Check--%>
<%@ include file="/include/PermissionCheck.jsp"%>
<%
	//set requet encoding
	request.setCharacterEncoding("UTF-8");
	//get param from request
	String supplyId = Filter.nullFilter(request.getParameter("supplyId"));//PK
	String quantity = Filter.nullFilter(request.getParameter("quantity"));
	String unitPrice = Filter.nullFilter(request.getParameter("unitPrice"));
	String priceDescription = Filter.nullFilter(request.getParameter("priceDescription"));
	String status = Filter.nullFilter(request.getParameter("status"));
	String batchNo = Filter.nullFilter(request.getParameter("batchNo"));//PK
	String productPrice = Filter.nullFilter(request.getParameter("productPrice"));
	String shippedFee = Filter.nullFilter(request.getParameter("shippedFee"));
	String time = Filter.nullFilter(request.getParameter("time"));
	String operator = Filter.nullFilter(request.getParameter("operator"));
	String transactionDescription = Filter.nullFilter(request.getParameter("transactionDescription"));

	//validate param requried
	if (supplyId.isEmpty() || quantity.isEmpty() || unitPrice.isEmpty() || status.isEmpty() || batchNo.isEmpty()
			|| productPrice.isEmpty() || shippedFee.isEmpty() || time.isEmpty() || operator.isEmpty()) {
		out.print("Missing required parameters!");
		return;
	}

	//validate param format
	String pattern = "yyyy-MM-dd";
	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	boolean isFormatted = false;
	try {
		Integer.parseInt(supplyId);//PK
		Integer.parseInt(batchNo);//PK
		Integer.parseInt(quantity);
		new BigDecimal(unitPrice);
		new BigDecimal(productPrice);
		new BigDecimal(shippedFee);
		sdf.parse(time);

		isFormatted = true;
	} catch (NumberFormatException | ParseException e) {
		e.printStackTrace();
	}
	if (!isFormatted) {
		out.print("Incorrect parameter format!");
		return;
	}

	//update by PK
	SupplyTransaction transaction = new SupplyTransaction();
	transaction.setSupplyId(Integer.parseInt(supplyId));//PK
	transaction.setQuantity(Integer.parseInt(quantity));
	transaction.setUnitPrice(new BigDecimal(unitPrice));
	transaction.setPriceDescription(priceDescription.isEmpty() ? null : priceDescription);
	transaction.setStatus(status);
	transaction.setBatchNo(Integer.parseInt(batchNo));//PK
	transaction.setProductPrice(new BigDecimal(productPrice));
	transaction.setShippedFee(new BigDecimal(shippedFee));
	transaction.setTime(sdf.parse(time));
	transaction.setOperator(operator);
	transaction.setTransactionDescription(transactionDescription.isEmpty() ? null : transactionDescription);

	int rows = new SupplyTransactionDatabase().updateByPK(transaction);

	//response
	String title = rows > 0 ? "Success" : "Failed";
	String titleColor = rows > 0 ? "green" : "red";
	String detail = "Update product supply transaction " + transaction.getBatchNo() + "-"
			+ transaction.getSupplyId() + " (batchNo-supplyId), " + rows + "  rows affected!";
	response.sendRedirect("Result.jsp?title=" + title + "&titleColor=" + titleColor + "&detail=" + detail);
%>