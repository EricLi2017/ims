<%@page import="internal.db.query.TransactionQuerier"%>
<%@page
	import="internal.db.query.TransactionQuerier.TransactionAndSupply"%>
<%@page import="internal.db.model.SupplyTransaction"%>
<%@page import="internal.db.model.ProductSupply"%>
<%@page import="common.util.OrderBy"%>
<%@page import="common.util.Filter"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.text.ParseException"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%--Permission Check--%>
<%@ include file="/include/PermissionCheck.jsp"%>
<%
	//set requet encoding
	request.setCharacterEncoding("UTF-8");

	//get request param
	boolean isQuery = (request.getQueryString() != null);
	String supplyId = Filter.nullFilter(request.getParameter("supplyId"));//PK
	String batchNo = Filter.nullFilter(request.getParameter("batchNo"));//PK
	String dateAfter = Filter.nullFilter(request.getParameter("dateAfter"));
	String dateBefore = Filter.nullFilter(request.getParameter("dateBefore"));
	String status = Filter.nullFilter(request.getParameter("status"));
	String productId = Filter.nullFilter(request.getParameter("productId"));//in table product_supply
	//order by params
	String sortedColumnId = Filter.nullFilter(request.getParameter("sortedColumnId"));
	String sortOrder = Filter.nullFilter(request.getParameter("sortOrder"));

	//date format
	String pattern = "yyyy-MM-dd";
	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	// order by Map: <sorted column id, database table column name>
	// select from supply_transaction a left join product_supply b on a.supply_id=b.supply_id
	Map<String, String> orderByMap = new HashMap<>();
	orderByMap.put("1", "a.supply_id");
	orderByMap.put("2", "a.quantity");
	orderByMap.put("3", "a.unit_price");
	orderByMap.put("4", "a.status");
	orderByMap.put("5", "a.batch_no");
	orderByMap.put("6", "a.product_price");
	orderByMap.put("7", "a.shipped_fee");
	orderByMap.put("8", "a.time");
	orderByMap.put("9", "a.operator");
	orderByMap.put("10", "b.product_id");

	//set default order by
	sortedColumnId = !orderByMap.containsKey(sortedColumnId) ? "5" : sortedColumnId;//set default orderBy value
	sortOrder = !OrderBy.ASC_DESC_MAP.containsKey(sortOrder) ? OrderBy.DESCENDING : sortOrder;//set default ascDesc value

	//validate param format
	if (!productId.isEmpty()) {
		boolean isInt = false;
		try {
			Integer.parseInt(productId);

			isInt = true;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (!isInt) {
			out.print("Incorrect parameter format: Product ID!");
			return;
		}
	}
	if (!supplyId.isEmpty()) {
		boolean isInt = false;
		try {
			Integer.parseInt(supplyId);

			isInt = true;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (!isInt) {
			out.print("Incorrect parameter format: Supply ID!");
			return;
		}
	}
	if (!dateAfter.isEmpty()) {
		boolean isDate = false;
		try {
			sdf.parse(dateAfter);

			isDate = true;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (!isDate) {
			out.print("Incorrect parameter format: Date After!");
			return;
		}
	}
	if (!dateBefore.isEmpty()) {
		boolean isDate = false;
		try {
			sdf.parse(dateBefore);

			isDate = true;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (!isDate) {
			out.print("Incorrect parameter format: Date Before!");
			return;
		}
	}

	//convert string to timestamp
	Timestamp after = !dateAfter.isEmpty() ? new Timestamp(sdf.parse(dateAfter).getTime()) : null;
	Timestamp before = !dateBefore.isEmpty() ? new Timestamp(sdf.parse(dateBefore).getTime()) : null;

	//query
	List<TransactionAndSupply> transactionAndSupplies = null;
	if (isQuery) {
		//set order by value only for database use
		String orderBy = orderByMap.get(sortedColumnId);
		String ascDesc = OrderBy.ASC_DESC_MAP.get(sortOrder);

		transactionAndSupplies = new TransactionQuerier().queryTransactionAndSupply(supplyId, batchNo, after,
				before, status, productId, orderBy, ascDesc);
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Supply Transaction Management</title>
<%--Required Head Core Elements--%>
<%@ include file="/include/page/head/Head-Core.jsp"%>
<%--Optional Head Date Elements--%>
<%@ include file="/include/page/head/Head-Date.jsp"%>
<%--special only for this page--%>
<script type="text/javascript">
	/* 
	Reverse sort order or change sorted column

	How to use:
	1: Search form set id;
	2: Search form add 2 hidden sort parameters;
	3: Result table add A link at title TD/TH
	 */
	function reorder(sortCol) {
		if ($("#sortedColumnId").val() == sortCol) {//sorted column is the same
			$("#sortOrder").val(
<%=OrderBy.getOppositeSortOrder(sortOrder)%>
	);//reverse sort order
		} else {
			$("#sortedColumnId").val(sortCol);//change sorted column id
		}

		//submit search form
		$("#searchForm").submit();
	}
</script>
</head>
<body>
	<%--Page Navigation Menu --%>
	<%@ include file="/include/page/Menu.jsp"%>

	<%--Page Main Content --%>
	<div class="content">

		<%--Page title--%>
		<div id="titleDiv">
			<h1>Supply Transaction Management</h1>
		</div>

		<%--Search form--%>
		<div id="searchDiv">
			<form action="" method="get" id="searchForm">
				<div style="display: inline;">
					Status:<select name="status">
						<option value=""></option>
						<option value="Active"
							<%="Active".equalsIgnoreCase(status) ? "selected" : ""%>>Active</option>
						<option value="Inactive"
							<%="Inactive".equalsIgnoreCase(status) ? "selected" : ""%>>Inactive</option>
					</select>
				</div>
				<div style="display: inline;">
					Supply ID:<input type="text" name="supplyId" value="<%=supplyId%>"
						title="">
				</div>
				<div style="display: inline;">
					Batch No.:<input type="text" name="batchNo" value="<%=batchNo%>"
						title="">
				</div>
				<div style="display: inline;">
					Date From:<input type="text" id="dateAfter" name="dateAfter"
						value="<%=dateAfter%>" title="">
				</div>
				<div style="display: inline;">
					Date To:<input type="text" id="dateBefore" name="dateBefore"
						value="<%=dateBefore%>" title="">
				</div>
				<div style="display: inline;">
					Product ID:<input type="text" name="productId"
						value="<%=productId%>" title="">
				</div>
				<div style="display: inline;">
					<input type="hidden" id="sortedColumnId" name="sortedColumnId"
						value="<%=sortedColumnId%>"> <input type="hidden"
						id="sortOrder" name="sortOrder" value="<%=sortOrder%>"><input
						type="submit" value="Search" title="">
				</div>
			</form>
		</div>

		<%--Search result--%>
		<%
			if (isQuery) {
				if (transactionAndSupplies == null) {
					out.print("<font color=red>oh no, something is wrong while searching</font>");
				} else if (transactionAndSupplies.size() < 1) {
					out.print("There are no any result.");
				} else {
					OrderBy orderBy = new OrderBy(sortedColumnId, orderByMap, sortOrder);
		%>
		<div id="resultDiv">
			<table>
				<tr>
					<th></th>
					<th><a href="javascript:void(0)" onclick="reorder('1')">Supply
							ID<%=orderBy.getHtmlArrow("1")%></a></th>
					<th><a href="javascript:void(0)" onclick="reorder('2')">Quantity<%=orderBy.getHtmlArrow("2")%></a></th>
					<th><a href="javascript:void(0)" onclick="reorder('3')">Unit
							Price<%=orderBy.getHtmlArrow("3")%></a></th>
					<th>Price Description</th>
					<th><a href="javascript:void(0)" onclick="reorder('4')">Status<%=orderBy.getHtmlArrow("4")%></a></th>
					<th><a href="javascript:void(0)" onclick="reorder('5')">Batch
							No.<%=orderBy.getHtmlArrow("5")%></a></th>
					<th><a href="javascript:void(0)" onclick="reorder('6')">Product
							Price<%=orderBy.getHtmlArrow("6")%></a></th>
					<th><a href="javascript:void(0)" onclick="reorder('7')">Shipped
							Fee<%=orderBy.getHtmlArrow("7")%></a></th>
					<th><a href="javascript:void(0)" onclick="reorder('8')">Time<%=orderBy.getHtmlArrow("8")%></a></th>
					<th><a href="javascript:void(0)" onclick="reorder('9')">Operator<%=orderBy.getHtmlArrow("9")%></a></th>
					<th>Transaction Description</th>
					<th><a href="javascript:void(0)" onclick="reorder('10')">Product
							ID<%=orderBy.getHtmlArrow("10")%></a></th>
				</tr>
				<%
					int rows = 0;
							for (TransactionAndSupply transactionAndSupply : transactionAndSupplies) {
								SupplyTransaction transaction = transactionAndSupply.getTransaction();
								ProductSupply supply = transactionAndSupply.getSupply();
				%>
				<tr>
					<td><a
						href="EditSupplyTransaction.jsp?supplyId=<%=transaction.getSupplyId()%>&batchNo=<%=transaction.getBatchNo()%>"
						title="click to edit this transaction" target="_blank"><%=++rows%></a></td>
					<td><%=transaction.getSupplyId()%></td>
					<td><%=transaction.getQuantity()%></td>
					<td><%=transaction.getUnitPrice()%></td>
					<td><%=Filter.nullFilter(transaction.getPriceDescription())%></td>
					<td><%=transaction.getStatus()%></td>
					<td><%=transaction.getBatchNo()%></td>
					<td><%=transaction.getProductPrice()%></td>
					<td><%=transaction.getShippedFee()%></td>
					<td><%=transaction.getTime()%></td>
					<td><%=transaction.getOperator()%></td>
					<td><%=Filter.nullFilter(transaction.getTransactionDescription())%></td>
					<td><%=supply.getProductId() == null ? "" : supply.getProductId()%></td>
				</tr>
				<%
					} //loop end
				%>
			</table>
		</div>
		<%
			} //show query result end
			} // is query end
		%>
	</div>

	<%--Page Footer --%>
	<%@ include file="/include/page/Footer.jsp"%>
</body>
</html>