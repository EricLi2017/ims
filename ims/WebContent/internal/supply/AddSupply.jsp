<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%--Permission Check--%>
<%@ include file="/include/jsp/PermissionCheck.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Add Product Supply</title>
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

		<h1 style="text-align: center">Add Product Supply</h1>

		<%--input data--%>
		<div>
			<form method="post" action="../service/AddSupplyService.jsp">
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
						<td><input type="text" name="productId" title=""
							style="width: 100%"></td>
						<td><input type="text" name="supplierId" title=""
							style="width: 100%"></td>
						<td><input type="text" name="supplierName" title=""
							style="width: 100%"></td>
						<td><input type="text" name="supplierDescription" title=""
							style="width: 100%"></td>
						<td><input type="text" name="supplyType" title=""
							style="width: 100%"></td>
						<td><input type="text" name="supplyUrl" title=""
							style="width: 100%"></td>
						<td><input type="text" name="shippedFrom" title=""
							style="width: 100%"></td>
						<td><input type="text" name="unitPrice" title=""
							style="width: 100%"></td>
						<td><input type="text" name="priceDescription" title=""
							style="width: 100%"></td>
						<td><input type="text" name="priceTime" title=""
							style="width: 100%"></td>
						<td><select name="status">
								<option value="Active" selected>Active</option>
								<option value="Inactive">Inactive</option>
						</select></td>
					</tr>
				</table>
				<div>
					<input type="submit" value="Submit">
				</div>
			</form>
		</div>
	</div>


	<%--Page Footer --%>
	<%@ include file="/include/jsp/body/Footer.jsp"%>
</body>
</html>