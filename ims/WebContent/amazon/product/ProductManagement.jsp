<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%--Permission Check--%>
<%@ include file="/include/PermissionCheck.jsp"%>
<%--Excute part--%>
<%@ include file="ProductManagementExcute.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%--jquery lib--%>
<script type="text/javascript"
	src="../../include/js/jquery/jquery-3.1.1.min.js"></script>
<%--jquery ui for datepicker function--%>
<script type="text/javascript"
	src="../../include/js/jquery-ui-1.12.1/jquery-ui.js"></script>
<%--jquery ui for datepicker style--%>
<link rel="stylesheet"
	href="../../include/js/jquery-ui-1.12.1/themes/redmond/jquery-ui.css">
<%--page style--%>
<link rel="stylesheet" href="../../include/css/page.css">

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
</script>
<style type="text/css">
.product th:HOVER {
	color: #e47911;
	/* 	background-color: #e47911; */
}
</style>
<title>Amazon Product Management</title>
</head>
<body>
	<%--Page Navigation Menu --%>
	<%@ include file="/include/page/Menu.jsp"%>

	<%--Page Main Content --%>
	<div class="content">
		<%--Page title--%>
		<div id="titleDiv">
			<h1>Amazon Product Management</h1>
		</div>

		<%--Search form--%>
		<div id="searchDiv" class="searchTable">
			<form action="" method="get">

				<div style="display: inline;">
					SKU:<input type="text" name="sku" value="<%=sku%>" title="">
				</div>
				<div style="display: inline;">
					ASIN:<input type="text" name="asin" value="<%=asin%>" title="">
				</div>
				<div style="display: inline;">
					FNSKU:<input type="text" name="fnsku" value="<%=fnsku%>" title="">
				</div>
				<div style="display: inline;">
					Name:<input type="text" name="name" value="<%=name%>" title="">
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
					<input type="submit" value="Search" title="">
				</div>
			</form>
		</div>

		<%--Search result--%>
		<%
			if (isQuery) {
				if (productAndOrders == null) {
					out.print("<font color=red>oh no, something is wrong while searching</font>");
				} else if (productAndOrders.size() < 1) {
					out.print("There are no any result.");
				} else {
		%>
		<div id="resultDiv">
			<table>
				<tr>
					<th></th>
					<th class="product">SKU</th>
					<th class="product">ASIN</th>
					<th class="product">Your Price</th>
					<th class="product2">Title</th>
					<th class="product2">Binding</th>
					<th class="product2">Brand</th>
					<th class="product2">Product Group</th>
					<th class="product2">Product Type Name</th>
					<th class="product2">Image</th>
					<th class="product2">Sales Rank</th>
					<th class="fulfillment">FNSKU</th>
					<th class="fulfillment">FBA Total</th>
					<th class="fulfillment">FBA In Stock</th>
					<th class="order">Units Ordered</th>
					<th class="order">Units Ordered B2B</th>
					<th class="order">Sales</th>
					<th class="order">Sales B2B</th>
				</tr>
				<%
					int rows = 0;
							for (QueryProductAndOrder.ProductAndOrder productAndOrder : productAndOrders) {
				%>
				<tr>
					<td><%=++rows%></td>
					<td><%=common.util.Filter.nullFilter(productAndOrder.getSku())%>
					</td>
					<td><a
						href="../../internal/product/InternalProductManagement.jsp?asin=<%=common.util.Filter.nullFilter(productAndOrder.getAsin())%>"
						title="click to view the related internal product" target="_blank"><%=common.util.Filter.nullFilter(productAndOrder.getAsin())%>
					</a></td>
					<td><%=productAndOrder.getYourPrice() == null ? "" : productAndOrder.getYourPrice()%>
					</td>
					<td><%=common.util.Filter.nullFilter(productAndOrder.getTitle())%>
					</td>
					<td><%=common.util.Filter.nullFilter(productAndOrder.getBinding())%>
					</td>
					<td><%=common.util.Filter.nullFilter(productAndOrder.getBrand())%>
					</td>
					<td><%=common.util.Filter.nullFilter(productAndOrder.getProductGroup())%>
					</td>
					<td><%=common.util.Filter.nullFilter(productAndOrder.getProductTypeName())%>
					</td>
					<td><img
						src="<%=common.util.Filter.nullFilter(productAndOrder.getImage())%>"
						alt="" /></td>
					<td><%=productAndOrder.getSalesRank() == null ? "" : productAndOrder.getSalesRank()%>
					</td>
					<td><%=common.util.Filter.nullFilter(productAndOrder.getFnsku())%>
					</td>
					<td><%=productAndOrder.getFbaTotal() == null ? "" : productAndOrder.getFbaTotal()%>
					</td>
					<td><%=productAndOrder.getFbaInStock() == null ? "" : productAndOrder.getFbaInStock()%>
					</td>
					<td><%=productAndOrder.getUnitsOrdered() == null ? "" : productAndOrder.getUnitsOrdered()%>
					</td>
					<td><%=productAndOrder.getUnitsOrderedB2B() == null ? ""
								: productAndOrder.getUnitsOrderedB2B()%></td>
					<td><%=productAndOrder.getOrderedProductSales() == null ? ""
								: productAndOrder.getOrderedProductSales()%></td>
					<td><%=productAndOrder.getOrderedProductSalesB2B() == null ? ""
								: productAndOrder.getOrderedProductSalesB2B()%></td>
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