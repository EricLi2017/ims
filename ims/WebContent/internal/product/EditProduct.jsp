<%@page import="common.util.Filter"%>
<%@page import="internal.db.query.ProductQuerier"%>
<%@page import="internal.db.model.InternalProduct"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%--Permission Check--%>
<%@ include file="/include/PermissionCheck.jsp"%>
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

	//selet data from database
	InternalProduct product = new ProductQuerier().selectById(productId);
	if (product == null) {
		out.print("There is no matched data!");
		return;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit Internal Product</title>
<%--menu style--%>
<link rel="stylesheet" href="../../include/css/menu.css">
<%--use the css style for input area --%>
<link type="text/css" rel="stylesheet"
	href="../../include/css/input.css">
</head>
<body>
	<%--Navigation Menu--%>
	<%@ include file="/include/Menu.jsp"%>

	<h1 style="text-align: center">
		Edit Internal Product (<span class="required">Product ID=<%=productId%></span>)
	</h1>

	<%--edit product data--%>
	<div>
		<form method="post" action="../service/EditProductService.jsp">
			<table align="center" width="100%">
				<tr>
					<th width="30%" class="required">Name</th>
					<th width="50%">Description</th>
					<th width="10%">ASIN</th>
					<th width="10%" class="required">Status</th>
				</tr>
				<tr>
					<td><input type="text" name="name"
						value="<%=Filter.nullFilter(product.getName())%>" title=""
						style="width: 100%"></td>
					<td><input type="text" name="description"
						value="<%=Filter.nullFilter(product.getDescription())%>" title=""
						style="width: 100%"></td>
					<td><input type="text" name="asin"
						value="<%=Filter.nullFilter(product.getAsin())%>" title=""
						style="width: 100%"></td>
					<td><select name="status">
							<option value="Active"
								<%="Active".equalsIgnoreCase(product.getStatus()) ? "selected" : ""%>>Active</option>
							<option value="Inactive"
								<%="Inactive".equalsIgnoreCase(product.getStatus()) ? "selected" : ""%>>Inactive</option>
					</select></td>
				</tr>
			</table>
			<div style="text-align: center;">
				<input type="hidden" name="id" value="<%=product.getProductId()%>"><input
					type="submit" value="Submit">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input
					type="reset" value="Reset">
			</div>
		</form>
	</div>

</body>
</html>