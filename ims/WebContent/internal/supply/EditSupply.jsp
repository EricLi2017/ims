<%@page import="internal.db.query.SupplyQuerier"%>
<%@page import="internal.db.model.ProductSupply"%>
<%@page import="common.util.Filter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%--Permission Check--%>
<%@ include file="/include/jsp/PermissionCheck.jsp"%>
<%--action --%>
<%
	//get param from request
	String id = Filter.nullFilter(request.getParameter("id"));

	//validate param required
	if (id.isEmpty()) {
		out.print("Missing required parameter!");
		return;
	}

	//validate param format
	//change string to int
	int supplyId = 0;
	boolean isInt = false;
	try {
		supplyId = Integer.parseInt(id);
		isInt = true;
	} catch (NumberFormatException e) {
		e.printStackTrace();
	}
	if (!isInt) {
		out.print("Incorrect parameter format!");
		return;
	}

	//selet data from database
	ProductSupply supply = new SupplyQuerier().selectById(supplyId);
	if (supply == null) {
		out.print("There is no matched data!");
		return;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit Internal Product</title>
<%--page style--%>
<link rel="stylesheet" href="../../include/css/page.css">
<%--use the css style for input area --%>
<link type="text/css" rel="stylesheet"
	href="../../include/css/input.css">
</head>
<body>
	<%--Page Navigation Menu --%>
	<%@ include file="/include/jsp/body/Menu.jsp"%>

	<%--Page Main Content --%>
	<div class="content">

		<h1 style="text-align: center">
			Edit Product Supply (<span class="required">Supply ID=<%=supplyId%></span>)
		</h1>

		<%--input data--%>
		<div>
			<form method="post" action="../service/EditSupplyService.jsp">
				<table align="center" width="100%">
					<tr>
						<!-- 				<th>Supply ID</th> -->
						<th class="required">Product ID</th>
						<th>Supplier ID</th>
						<th>Supplier Name</th>
						<th>Supplier Description</th>
						<th>Supply Type</th>
						<th>Supply URL</th>
						<th>Shipped From</th>
						<th>Unit Price</th>
						<th>Price Description</th>
						<th>Price Time</th>
						<th class="required">Status</th>
					</tr>
					<tr>
						<td><input type="text" name="productId"
							value="<%=supply.getProductId()%>" title="" style="width: 100%"></td>
						<td><input type="text" name="supplierId"
							value="<%=supply.getSupplierId() == null ? "" : supply.getSupplierId()%>"
							title="" style="width: 100%"></td>
						<td><input type="text" name="supplierName"
							value="<%=Filter.nullFilter(supply.getSupplierName())%>" title=""
							style="width: 100%"></td>
						<td><input type="text" name="supplierDescription"
							value="<%=Filter.nullFilter(supply.getSupplierDescription())%>"
							title="" style="width: 100%"></td>
						<td><input type="text" name="supplyType"
							value="<%=Filter.nullFilter(supply.getSupplyType())%>" title=""
							style="width: 100%"></td>
						<td><input type="text" name="supplyUrl"
							value="<%=Filter.nullFilter(supply.getSupplyUrl())%>" title=""
							style="width: 100%"></td>
						<td><input type="text" name="shippedFrom"
							value="<%=Filter.nullFilter(supply.getShippedFrom())%>" title=""
							style="width: 100%"></td>
						<td><input type="text" name="unitPrice"
							value="<%=supply.getUnitPrice() == null ? "" : supply.getUnitPrice()%>"
							title="" style="width: 100%"></td>
						<td><input type="text" name="priceDescription"
							value="<%=Filter.nullFilter(supply.getPriceDescription())%>"
							title="" style="width: 100%"></td>
						<td><input type="text" name="priceTime"
							value="<%=supply.getPriceTime() == null ? "" : supply.getPriceTime()%>"
							title="" style="width: 100%"></td>
						<td><select name="status">
								<option value="Active"
									<%="Active".equalsIgnoreCase(supply.getStatus()) ? "selected" : ""%>>Active</option>
								<option value="Inactive"
									<%="Inactive".equalsIgnoreCase(supply.getStatus()) ? "selected" : ""%>>Inactive</option>
						</select></td>
					</tr>
				</table>
				<div style="text-align: center;">
					<input type="hidden" name="supplyId"
						value="<%=supply.getSupplyId()%>"> <input type="submit"
						value="Submit">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input
						type="reset" value="Reset">
				</div>
			</form>
		</div>
	</div>


	<%--Page Footer --%>
	<%@ include file="/include/jsp/body/Footer.jsp"%>

</body>
</html>
