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
<%--Head Required Core Elements--%>
<%@ include file="/include/page/head/Head-Core.jsp"%>
<%--Head Optional Date Elements--%>
<%@ include file="/include/page/head/Head-Date.jsp"%>
<%--special only for this page--%>
<script type="text/javascript">
	$(document).ready(function() {
		/**click to hide and show*/
		$("#hide").click(function() {
			$(".product2").hide();
		});
		$("#show").click(function() {
			$(".product2").show();
		});
		$("#hide").hover(function() {
			$(".product2").hide();
		});
		$("#show").hover(function() {
			$(".product2").show();
		});

	});//end of $(document).ready()

	/** 
	Click sorted column to reorder:
		Reverse sort order if click the current sorted column,
		or change sorted column to the clicked column if click non current sorted column
	
	How to use:
	1: Search form set id;
	2: Search form add 2 hidden sort parameters;
	3: Result table add A link at title TD/TH
	 */
	function reorder(sortCol) {
		//is the same sorted column 
		if ($("#sortedColumnId").val() == sortCol) {
			//reverse sort order
			$("#sortOrder").val(
<%=OrderBy.getOppositeSortOrder(sortOrder)%>
	);
		} else {
			//change sorted column
			$("#sortedColumnId").val(sortCol);
		}

		//submit search form
		$("#searchForm").submit();
	}
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
			<form id="searchForm" action="" method="get">

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
					<input type="hidden" id="sortedColumnId" name="sortedColumnId"
						value="<%=sortedColumnId%>"> <input type="hidden"
						id="sortOrder" name="sortOrder" value="<%=sortOrder%>"> <input
						type="submit" value="Search" title="">
				</div>
				<div style="display: inline;">
					<input type="button" value="+" id="show" title="show all columns"><input
						type="button" value="-" id="hide" title="hide some columns">
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
					<th class="product"><a href="javascript:void(0)"
						onclick="reorder('5')">SKU<%=orderBy.getHtmlArrow("5")%>
					</a></th>
					<th class="product"><a href="javascript:void(0)"
						onclick="reorder('6')">ASIN<%=orderBy.getHtmlArrow("6")%>
					</a></th>
					<th class="product"><a href="javascript:void(0)"
						onclick="reorder('7')">Your Price<%=orderBy.getHtmlArrow("7")%>
					</a></th>
					<th class="product2"><a href="javascript:void(0)"
						onclick="reorder('12')">Title<%=orderBy.getHtmlArrow("12")%>
					</a></th>
					<th class="product2"><a href="javascript:void(0)"
						onclick="reorder('13')">Binding<%=orderBy.getHtmlArrow("13")%>
					</a></th>
					<th class="product2"><a href="javascript:void(0)"
						onclick="reorder('16')">Product Group<%=orderBy.getHtmlArrow("16")%>
					</a></th>
					<th class="product2"><a href="javascript:void(0)"
						onclick="reorder('17')">Product Type Name<%=orderBy.getHtmlArrow("17")%>
					</a></th>
					<th class="product2"><a href="javascript:void(0)"
						onclick="reorder('14')">Brand<%=orderBy.getHtmlArrow("14")%>
					</a></th>
					<th class="product2_img"><a href="javascript:void(0)"
						onclick="reorder('11')">Image<%=orderBy.getHtmlArrow("11")%>
					</a></th>
					<th class="product2"><a href="javascript:void(0)"
						onclick="reorder('18')">Sales Rank<%=orderBy.getHtmlArrow("18")%>
					</a></th>
					<th class="fulfillment"><a href="javascript:void(0)"
						onclick="reorder('8')">FNSKU<%=orderBy.getHtmlArrow("8")%>
					</a></th>
					<th class="fulfillment"><a href="javascript:void(0)"
						onclick="reorder('9')">FBA Total<%=orderBy.getHtmlArrow("9")%>
					</a></th>
					<th class="fulfillment"><a href="javascript:void(0)"
						onclick="reorder('10')">FBA In Stock<%=orderBy.getHtmlArrow("10")%>
					</a></th>
					<th class="order"><a href="javascript:void(0)"
						onclick="reorder('1')">Units Ordered<%=orderBy.getHtmlArrow("1")%>
					</a></th>
					<th class="order"><a href="javascript:void(0)"
						onclick="reorder('2')">Units Ordered B2B<%=orderBy.getHtmlArrow("2")%>
					</a></th>
					<th class="order"><a href="javascript:void(0)"
						onclick="reorder('3')">Sales<%=orderBy.getHtmlArrow("3")%>
					</a></th>
					<th class="order"><a href="javascript:void(0)"
						onclick="reorder('4')">Sales B2B<%=orderBy.getHtmlArrow("4")%>
					</a></th>
				</tr>
				<%
					int rows = 0;
							for (QueryProductAndOrder.ProductAndOrder productAndOrder : productAndOrders) {
				%>
				<tr>
					<td><%=++rows%></td>
					<td class="product"><%=common.util.Filter.nullFilter(productAndOrder.getSku())%>
					</td>
					<td class="product"><a
						href="../../internal/product/InternalProductManagement.jsp?asin=<%=common.util.Filter.nullFilter(productAndOrder.getAsin())%>"
						title="click to view the related internal product" target="_blank"><%=common.util.Filter.nullFilter(productAndOrder.getAsin())%>
					</a></td>
					<td class="product"><%=productAndOrder.getYourPrice() == null ? "" : productAndOrder.getYourPrice()%>
					</td>
					<td class="product2"><%=common.util.Filter.nullFilter(productAndOrder.getTitle())%>
					</td>
					<td class="product2"><%=common.util.Filter.nullFilter(productAndOrder.getBinding())%>
					</td>
					<td class="product2"><%=common.util.Filter.nullFilter(productAndOrder.getProductGroup())%>
					</td>
					<td class="product2"><%=common.util.Filter.nullFilter(productAndOrder.getProductTypeName())%>
					</td>
					<td class="product2"><%=common.util.Filter.nullFilter(productAndOrder.getBrand())%>
					</td>
					<td class="product2_img"><img
						src="<%=common.util.Filter.nullFilter(productAndOrder.getImage())%>"
						alt="" /></td>
					<td class="product2"><%=productAndOrder.getSalesRank() == null ? "" : productAndOrder.getSalesRank()%>
					</td>
					<td class="fulfillment"><%=common.util.Filter.nullFilter(productAndOrder.getFnsku())%>
					</td>
					<td class="fulfillment"><%=productAndOrder.getFbaTotal() == null ? "" : productAndOrder.getFbaTotal()%>
					</td>
					<td class="fulfillment"><%=productAndOrder.getFbaInStock() == null ? "" : productAndOrder.getFbaInStock()%>
					</td>
					<td class="order"><%=productAndOrder.getUnitsOrdered() == null ? "" : productAndOrder.getUnitsOrdered()%>
					</td>
					<td class="order"><%=productAndOrder.getUnitsOrderedB2B() == null ? ""
								: productAndOrder.getUnitsOrderedB2B()%></td>
					<td class="order"><%=productAndOrder.getOrderedProductSales() == null ? ""
								: productAndOrder.getOrderedProductSales()%></td>
					<td class="order"><%=productAndOrder.getOrderedProductSalesB2B() == null ? ""
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