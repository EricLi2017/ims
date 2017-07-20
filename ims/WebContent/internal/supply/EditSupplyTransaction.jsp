<%@page import="internal.db.edit.SupplyTransactionDatabase"%>
<%@page import="internal.db.model.SupplyTransaction"%>
<%@page import="common.util.Filter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%--Permission Check--%>
<%@ include file="/include/PermissionCheck.jsp"%>
<%--action --%>
<%
	//get param from request
	String supplyId = Filter.nullFilter(request.getParameter("supplyId"));
	String batchNo = Filter.nullFilter(request.getParameter("batchNo"));

	//validate param requried
	if (supplyId.isEmpty() || batchNo.isEmpty()) {
		out.print("Missing required parameters!");
		return;
	}

	//validate param format
	boolean isFormatted = false;
	try {
		Integer.parseInt(supplyId);
		Integer.parseInt(supplyId);

		isFormatted = true;
	} catch (NumberFormatException e) {
		e.printStackTrace();
	}
	if (!isFormatted) {
		out.print("Incorrect parameter format!");
		return;
	}

	//selet data from database
	SupplyTransaction transaction = new SupplyTransactionDatabase().selectByPK(Integer.parseInt(supplyId),
			Integer.parseInt(batchNo));
	if (transaction == null) {
		out.print("There is no matched data!");
		return;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit Supply Transaction</title>
<%--page style--%>
<link rel="stylesheet" href="../../include/css/page.css">
<%--use the css style of input area --%>
<link type="text/css" rel="stylesheet"
	href="../../include/css/input.css">
</head>
<body>
	<%--Page Navigation Menu --%>
	<%@ include file="/include/page/Menu.jsp"%>

	<%--Page Main Content --%>
	<div class="content">

		<h1 style="text-align: center">
			Edit Supply Transaction (<span class="required">Batch No.=<%=batchNo%>
				Supply ID=<%=supplyId%></span>)
		</h1>

		<%--input data--%>
		<div>
			<form method="post"
				action="../service/EditSupplyTransactionService.jsp">
				<table align="center" width="100%">
					<tr>
						<!-- 									<th>Supply ID</th> -->
						<th class="required">Supply ID</th>
						<th class="required">Quantity</th>
						<th class="required">Unit Price</th>
						<th>Price Description</th>
						<th class="required">Status</th>
						<th class="required">Batch No.</th>
						<th class="required">Product Price</th>
						<th class="required">Shipped Fee</th>
						<th class="required">Time</th>
						<th class="required">Operator</th>
						<th>Transaction Description</th>
					</tr>
					<tr>
						<td><%=transaction.getSupplyId()%></td>
						<td><input type="text" name="quantity"
							value="<%=transaction.getQuantity()%>" title=""
							style="width: 100%"></td>
						<td><input type="text" name="unitPrice"
							value="<%=transaction.getUnitPrice()%>" title=""
							style="width: 100%"></td>
						<td><input type="text" name="priceDescription"
							value="<%=Filter.nullFilter(transaction.getPriceDescription())%>"
							title="" style="width: 100%"></td>
						<td><select name="status">
								<option value="Paid"
									<%="Paid".equalsIgnoreCase(transaction.getStatus()) ? "selected" : ""%>>Paid</option>
								<option value="Unpaid"
									<%="Unpaid".equalsIgnoreCase(transaction.getStatus()) ? "selected" : ""%>>Unpaid</option>
								<option value="Stopped"
									<%="Stopped".equalsIgnoreCase(transaction.getStatus()) ? "selected" : ""%>>Stopped</option>
								<option value="Received"
									<%="Received".equalsIgnoreCase(transaction.getStatus()) ? "selected" : ""%>>Received</option>
						</select></td>
						<td><%=transaction.getBatchNo()%></td>
						<td><input type="text" name="productPrice"
							value="<%=transaction.getProductPrice()%>" title=""
							style="width: 100%"></td>
						<td><input type="text" name="shippedFee"
							value="<%=transaction.getShippedFee()%>" title=""
							style="width: 100%"></td>
						<td><input type="text" name="time"
							value="<%=transaction.getTime()%>" title="" style="width: 100%"></td>
						<td><input type="text" name="operator"
							value="<%=transaction.getOperator()%>" title=""
							style="width: 100%"></td>
						<td><input type="text" name="transactionDescription"
							value="<%=Filter.nullFilter(transaction.getTransactionDescription())%>"
							title="" style="width: 100%"></td>
					</tr>
				</table>
				<div style="text-align: center;">
					<input type="hidden" name="supplyId"
						value="<%=transaction.getSupplyId()%>"> <input
						type="hidden" name="batchNo" value="<%=transaction.getBatchNo()%>">
					<input type="submit" value="Submit">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input
						type="reset" value="Reset">
				</div>
			</form>
		</div>
	</div>

	<%--Page Footer --%>
	<%@ include file="/include/page/Footer.jsp"%>

</body>
</html>