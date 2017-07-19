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
<%--define the css style of query result table--%>
<link type="text/css" rel="stylesheet"
	href="../../include/css/table.css">
<%--set background color of table tr while mouseover and mouseout--%>
<script type="text/javascript" src="../../include/js/table.js"></script>

<%--special only for this page--%>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						/*set date*/
						$("#dateAfter").datepicker({
							dateFormat : "yy-mm-dd"
						});
						$("#dateBefore").datepicker({
							dateFormat : "yy-mm-dd"
						});
						/*view mouseover affected scope: product*/
						var productA = $("#productA");
						productA.mouseover(function() {
							$(".product").css("background-color", "#FF0000");
						});
						productA.mouseout(function() {
							$(".product").css("background-color", "#7fc5fa");
						});
						/*view mouseover affected scope: fulfilment*/
						var fulfilmentButton = $("#fulfilmentButton");
						fulfilmentButton.mouseover(function() {
							$(".fulfillment")
									.css("background-color", "#FF0000");
						});
						fulfilmentButton.mouseout(function() {
							$(".fulfillment")
									.css("background-color", "#7fc5fa");
						});
						/*view mouseover affected scope: order*/
						var orders = [ $("#orderA"), $("#orderB") ];
						for (var x = 0; x < orders.length; x++) {
							orders[x].mouseover(function() {
								$(".order").css("background-color", "#FF0000");
							});
							orders[x].mouseout(function() {
								$(".order").css("background-color", "#7fc5fa");
							});
						}
						/*  checkSKU async call    */
						$("#checkSkuButton")
								.click(
										function() {
											//action checkSKU
											var obj = $
													.ajax({
														url : "../service/CheckSKUService.jsp",
														async : true,
														beforeSend : function() {
															$(":input").attr(
																	"disabled",
																	true);//disable all input elements
															$(
																	"#checkSkuResultSpan")
																	.html(
																			"checking...");
														},
														success : function() {
															$(
																	"#checkSkuResultSpan")
																	.html(
																			obj.responseText);
														},
														error : function(
																XMLHttpRequest,
																textStatus,
																errorThrown) {
															var errMsg = "Oh no, something is wrong! "
																	+ XMLHttpRequest.readyState
																	+ " : "
																	+ XMLHttpRequest.status
																	+ " : "
																	+ XMLHttpRequest.statusText
																	+ " : "
																	+ textStatus
																	+ " : "
																	+ errorThrown;
															$(
																	"#checkSkuResultSpan")
																	.html(
																			errMsg);
														},
														complete : function() {
															$(":input")
																	.removeAttr(
																			"disabled");//active all input elements
														}
													});
										});//end of $("#checkSkuButton").click()
						/*  updateFBAInventory async call    */
						fulfilmentButton
								.click(function() {
									//action: updateFBAInventory
									var newWin;
									var obj = $
											.ajax({
												url : "../service/UpdateFBAInventoryService.jsp",
												async : true,
												beforeSend : function() {
													$(":input").attr(
															"disabled", true);//disable all input elements
													$("#updateFbaInventorySpan")
															.html(
																	"FBA inventory updating...This may will take several minutes, please wait.");
													$("#intervalDiv").html("");
													newWin = window
															.open(
																	"../service/UpdateFBAInventoryMessagePage.jsp",
																	"",
																	"width=400,height=600");//open a new window to view the result at intervals
												},
												success : function() {
													$("#updateFbaInventorySpan")
															.html(
																	obj.responseText);
												},
												error : function(
														XMLHttpRequest,
														textStatus, errorThrown) {
													var errMsg = "Oh no, something is wrong! "
															+ XMLHttpRequest.readyState
															+ " : "
															+ XMLHttpRequest.status
															+ " : "
															+ XMLHttpRequest.statusText
															+ " : "
															+ textStatus
															+ " : "
															+ errorThrown;
													$("#updateFbaInventorySpan")
															.html(errMsg);
												},
												complete : function() {
													$(":input").removeAttr(
															"disabled");//active all input elements
													newWin.close();//close the interval view window opened by previous javascript
												}
											});
								});//end of fulfilmentButton.click()
					});//end of $(document).ready()
</script>
<title>Product Management</title>
</head>
<body>
	<%--Navigation links--%>
	<div id="navDiv">
		<div style="float: right;">
			hi
			<%=user%>
			<a href="../../user/SignOut.jsp">Sign Out</a>
		</div>
		<div>
			<a id="productA" href="../product/BatchAddProductFromAMZ.jsp"
				target="_blank">Upload SKU From Report</a> &nbsp;|&nbsp;
			<button id="fulfilmentButton">Update All FBA Inventory</button>
			&nbsp;|&nbsp; <a id="orderB"
				href="../order/UpdateAndCheckOrderPage.jsp" target="_blank">Update
				and Check Orders</a> &nbsp;|&nbsp; <a id="orderA"
				href="../order/ManageOrders.jsp" target="_blank">Manage Orders</a>
			&nbsp;|&nbsp; <span id="updateFbaInventorySpan"></span>
		</div>
	</div>
	<%--JS message--%>
	<div id="intervalDiv"></div>
	<%--Check information--%>
	<div>
		<button id="checkSkuButton" type="button">Check SKU Matching
			in Orders and Products</button>
		<span id="checkSkuResultSpan"></span>
	</div>

	<%--Page title--%>
	<div id="titleDiv">
		<h1 style="text-align: center">Product Management</h1>
	</div>

	<%--Search form--%>
	<div id="searchDiv">
		<form action="" method="get">
			<table align="center">
				<tr>
					<td>SKU:</td>
					<td><input type="text" name="sku" value="<%=sku%>" title=""></td>
					<td>FNSKU:</td>
					<td><input type="text" name="fnsku" value="<%=fnsku%>"
						title=""></td>
					<td>Name:</td>
					<td><input type="text" name="name" value="<%=name%>" title=""></td>
				</tr>
				<tr>
					<td>ASIN:</td>
					<td><input type="text" name="asin" value="<%=asin%>" title=""></td>
					<td>Date From:</td>
					<td><input type="text" id="dateAfter" name="dateAfter"
						value="<%=dateAfter%>" title=""></td>
					<td>Date To:</td>
					<td><input type="text" id="dateBefore" name="dateBefore"
						value="<%=dateBefore%>" title=""></td>
					<td><input type="submit" value="Search" title=""></td>
				</tr>
			</table>
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
	<%--use the table.css style--%>
	<div id="resultDiv" class="table-c">
		<table>
			<tr align="center">
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
			<%--define tr class to data to active mouseover and mouseout events--%>
			<tr class="data">
				<td><%=++rows%></td>
				<td><%=common.util.Filter.nullFilter(productAndOrder.getSku())%>
				</td>
				<td><a
					href="../../internal/product/InternalProductManagement.jsp?asin=<%=common.util.Filter.nullFilter(productAndOrder.getAsin())%>"
					title="click to view the related internal product" target="_blank"><%=common.util.Filter.nullFilter(productAndOrder.getAsin())%>
				</a></td>
				<td align="right"><%=productAndOrder.getYourPrice() == null ? "" : productAndOrder.getYourPrice()%>
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
				<td align="right"><%=productAndOrder.getSalesRank() == null ? "" : productAndOrder.getSalesRank()%>
				</td>
				<td><%=common.util.Filter.nullFilter(productAndOrder.getFnsku())%>
				</td>
				<td align="right"><%=productAndOrder.getFbaTotal() == null ? "" : productAndOrder.getFbaTotal()%>
				</td>
				<td align="right"><%=productAndOrder.getFbaInStock() == null ? "" : productAndOrder.getFbaInStock()%>
				</td>
				<td align="right"><%=productAndOrder.getUnitsOrdered() == null ? "" : productAndOrder.getUnitsOrdered()%>
				</td>
				<td align="right"><%=productAndOrder.getUnitsOrderedB2B() == null ? ""
								: productAndOrder.getUnitsOrderedB2B()%></td>
				<td align="right"><%=productAndOrder.getOrderedProductSales() == null ? ""
								: productAndOrder.getOrderedProductSales()%></td>
				<td align="right"><%=productAndOrder.getOrderedProductSalesB2B() == null ? ""
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


</body>
</html>