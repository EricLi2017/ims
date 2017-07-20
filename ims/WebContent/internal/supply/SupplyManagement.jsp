<%@page import="internal.db.query.SupplyQuerier"%>
<%@page import="internal.db.model.ProductSupply"%>
<%@page import="common.util.Filter"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.text.ParseException"%>
<%@page import="common.util.OrderBy"%>
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
	String supplyId = Filter.nullFilter(request.getParameter("supplyId"));
	String productId = Filter.nullFilter(request.getParameter("productId"));
	String supplyUrl = Filter.nullFilter(request.getParameter("supplyUrl"));
	String dateAfter = Filter.nullFilter(request.getParameter("dateAfter"));
	String dateBefore = Filter.nullFilter(request.getParameter("dateBefore"));
	String status = Filter.nullFilter(request.getParameter("status"));
	//order by params
	String sortedColumnId = Filter.nullFilter(request.getParameter("sortedColumnId"));
	String sortOrder = Filter.nullFilter(request.getParameter("sortOrder"));

	//date format
	String pattern = "yyyy-MM-dd";
	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	// order by Map: <sorted column id, database table column name>
	// select from product_supply
	Map<String, String> orderByMap = new HashMap<>();
	orderByMap.put("1", "supply_id");
	orderByMap.put("2", "product_id");
	orderByMap.put("3", "supplier_id");
	orderByMap.put("4", "supplier_name");
	orderByMap.put("5", "supply_type");
	orderByMap.put("6", "supply_url");
	orderByMap.put("7", "shipped_from");
	orderByMap.put("8", "unit_price");
	orderByMap.put("9", "price_time");
	orderByMap.put("10", "status");

	//set default order by
	sortedColumnId = !orderByMap.containsKey(sortedColumnId) ? "1" : sortedColumnId;//set default orderBy value
	sortOrder = !OrderBy.ASC_DESC_MAP.containsKey(sortOrder) ? OrderBy.DESCENDING : sortOrder;//set default ascDesc value

	//validate param format
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

	//query database
	List<ProductSupply> supplyList = null;
	if (isQuery) {
		//set order by value only for database use
		String orderBy = orderByMap.get(sortedColumnId);
		String ascDesc = OrderBy.ASC_DESC_MAP.get(sortOrder);

		supplyList = new SupplyQuerier().querySupply(supplyId, productId, supplyUrl, after, before, status,
				orderBy, ascDesc);
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Product Supply Management</title>
<%--page style--%>
<link rel="stylesheet" href="../../include/css/page.css">
<%--jquery lib--%>
<script type="text/javascript"
	src="../../include/js/jquery/jquery-3.1.1.min.js"></script>
<%--jquery ui for datepicker function--%>
<script type="text/javascript"
	src="../../include/js/jquery-ui-1.12.1/jquery-ui.js"></script>
<%--jquery ui for datepicker style--%>
<link rel="stylesheet"
	href="../../include/js/jquery-ui-1.12.1/themes/redmond/jquery-ui.css">
<%--special only for this page--%>
<script type="text/javascript">
	$(document).ready(function() {
		/*set date*/
		$("#dateAfter").datepicker({
			dateFormat : "yy-mm-dd"
		});
		$("#dateBefore").datepicker({
			dateFormat : "yy-mm-dd"
		});
	});//end of $(document).ready()

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
	<%@ include file="/include/Menu.jsp"%>

	<%--Page Main Content --%>
	<div class="content">

		<%--Page title--%>
		<div id="titleDiv" style="text-align: center">
			<h1>Product Supply Management</h1>
		</div>

		<%--Search form--%>
		<div id="searchDiv" style="text-align: center">
			<form action="" method="get" id="searchForm">
				<div style="display: inline;">
					Supply ID:<input type="text" name="supplyId" value="<%=supplyId%>"
						title="">
				</div>
				<div style="display: inline;">
					Product ID:<input type="text" name="productId"
						value="<%=productId%>" title="">
				</div>
				<div style="display: inline;">
					Supply URL:<input type="text" name="supplyUrl"
						value="<%=supplyUrl%>" title="">
				</div>
				<div style="display: inline;">
					Date After:<input type="text" name="dateAfter"
						value="<%=dateAfter%>" title="" id="dateAfter">
				</div>
				<div style="display: inline;">
					Date Before:<input type="text" name="dateBefore"
						value="<%=dateBefore%>" title="" id="dateBefore">
				</div>
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
				if (supplyList == null) {
					out.print("<font color=red>oh no, something is wrong while searching</font>");
				} else if (supplyList.size() < 1) {
					out.print("There are no any result.");
				} else {
		%>
		<div id="resultDiv">
			<table>
				<tr>
					<th></th>
					<th><a href="javascript:void(0)" onclick="reorder('1')">Supply
							ID<%=OrderBy.getHtmlArrow("1", sortedColumnId, orderByMap, sortOrder)%></a></th>
					<th><a href="javascript:void(0)" onclick="reorder('2')">Product
							ID<%=OrderBy.getHtmlArrow("2", sortedColumnId, orderByMap, sortOrder)%></a></th>
					<th><a href="javascript:void(0)" onclick="reorder('3')">Supplier
							ID<%=OrderBy.getHtmlArrow("3", sortedColumnId, orderByMap, sortOrder)%></a></th>
					<th><a href="javascript:void(0)" onclick="reorder('4')">Supplier
							Name<%=OrderBy.getHtmlArrow("4", sortedColumnId, orderByMap, sortOrder)%></a></th>
					<th>Supplier Description</th>
					<th><a href="javascript:void(0)" onclick="reorder('5')">Supply
							Type<%=OrderBy.getHtmlArrow("5", sortedColumnId, orderByMap, sortOrder)%></a></th>
					<th><a href="javascript:void(0)" onclick="reorder('6')">Supply
							URL<%=OrderBy.getHtmlArrow("6", sortedColumnId, orderByMap, sortOrder)%></a></th>
					<th><a href="javascript:void(0)" onclick="reorder('7')">Shipped
							From<%=OrderBy.getHtmlArrow("7", sortedColumnId, orderByMap, sortOrder)%></a></th>
					<th><a href="javascript:void(0)" onclick="reorder('8')">Unit
							Price<%=OrderBy.getHtmlArrow("8", sortedColumnId, orderByMap, sortOrder)%></a></th>
					<th>Price Description</th>
					<th><a href="javascript:void(0)" onclick="reorder('9')">Price
							Time<%=OrderBy.getHtmlArrow("9", sortedColumnId, orderByMap, sortOrder)%></a></th>
					<th><a href="javascript:void(0)" onclick="reorder('10')">Status<%=OrderBy.getHtmlArrow("10", sortedColumnId, orderByMap, sortOrder)%></a></th>
				</tr>
				<%
					int rows = 0;
							for (ProductSupply supply : supplyList) {
				%>
				<tr>
					<td><%=++rows%></td>
					<td><a href="EditSupply.jsp?id=<%=supply.getSupplyId()%>"
						title="click to edit this supply" target="_blank"><%=supply.getSupplyId()%></a></td>
					<td><%=supply.getProductId() == null ? "" : supply.getProductId()%></td>
					<td><%=supply.getSupplierId() == null ? "" : supply.getSupplierId()%></td>
					<td><%=Filter.nullFilter(supply.getSupplierName())%></td>
					<td><%=Filter.nullFilter(supply.getSupplierDescription())%></td>
					<td><%=Filter.nullFilter(supply.getSupplyType())%></td>
					<td><%=Filter.nullFilter(supply.getSupplyUrl())%></td>
					<td><%=Filter.nullFilter(supply.getShippedFrom())%></td>
					<td><%=supply.getUnitPrice() == null ? "" : supply.getUnitPrice()%></td>
					<td><%=Filter.nullFilter(supply.getPriceDescription())%></td>
					<td><%=supply.getPriceTime() == null ? "" : supply.getPriceTime()%></td>
					<td><%=Filter.nullFilter(supply.getStatus())%></td>
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
	<%@ include file="/include/Footer.jsp"%>
</body>
</html>