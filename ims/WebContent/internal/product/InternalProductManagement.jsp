<%--
  Created by IntelliJ IDEA.
  User: Eric Li
  Date: 3/20/2017
  Time: 12:24 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%--Permission Check--%>
<%@ include file="/include/PermissionCheck.jsp"%>
<%--Excute part--%>
<%@ include file="/internal/product/InternalProductManagementExcute.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Internal Product Management</title>
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
	$(document).ready(function() {
		/*set date*/
		$("#dateAfter").datepicker({
			dateFormat : "yy-mm-dd"
		});
		$("#dateBefore").datepicker({
			dateFormat : "yy-mm-dd"
		});

	});//end of $(document).ready()
	/*async count supply*/
	function countSupply(obj) {
		var id = $(obj).attr("id");
		// async call 
		var action = $.ajax({
			url : "../service/CountSupplyByProductIdService.jsp?productId="
					+ id,
			async : true,
			beforeSend : function() {
				$(obj).attr("disabled", true);//disable this button
				$(".supply." + id).html("counting...");//.class.class
			},
			success : function() {
				$(".supply." + id).html(action.responseText);//.class.class
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				var errMsg = "Oh no, something is wrong! "
						+ XMLHttpRequest.readyState + " : "
						+ XMLHttpRequest.status + " : "
						+ XMLHttpRequest.statusText + " : " + textStatus
						+ " : " + errorThrown;
				$(".supply." + id).html(errMsg);
			},
			complete : function() {
				$(obj).removeAttr("disabled");//active this button
			}
		});
	}
<%--special only for this page--%>
	/*async count transaction*/
	function countTransaction(obj) {
		var id = $(obj).attr("id");
		// async call 
		var action = $
				.ajax({
					url : "../service/CountTransactionByProductIdService.jsp?productId="
							+ id,
					async : true,
					beforeSend : function() {
						$(obj).attr("disabled", true);//disable this button
						$(".transaction." + id).html("counting...");//.class.class
					},
					success : function() {
						$(".transaction." + id).html(action.responseText);//.class.class
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						var errMsg = "Oh no, something is wrong! "
								+ XMLHttpRequest.readyState + " : "
								+ XMLHttpRequest.status + " : "
								+ XMLHttpRequest.statusText + " : "
								+ textStatus + " : " + errorThrown;
						$(".transaction." + id).html(errMsg);
					},
					complete : function() {
						$(obj).removeAttr("disabled");//active this button
					}
				});
	}

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
<style type="text/css">
/* body { */
/* 	text-align: center */
/* } */
</style>
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
			<a href="AddProduct.jsp" target="_blank">Add Internal Product</a>
			&nbsp;|&nbsp; <a href="../supply/SupplyManagement.jsp"
				target="_blank">Manage Product Supply</a> &nbsp;|&nbsp; <a
				href="../supply/SupplyTransactionManagement.jsp" target="_blank">Manage
				Supply Transaction</a> &nbsp;|&nbsp;
		</div>
	</div>

	<%--Page title--%>
	<div id="titleDiv" style="text-align: center">
		<h1>Internal Product Management</h1>
	</div>

	<%--Search form--%>
	<div id="searchDiv" style="text-align: center;">
		<form id="searchForm" action="" method="get">
			<div style="display: inline;">
				Product ID:<input type="text" name="productId"
					value="<%=productId%>" title="">
			</div>
			<div style="display: inline;">
				Name:<input type="text" name="name" value="<%=name%>" title="">
			</div>
			<div style="display: inline;">
				Date After:<input type="text" id="dateAfter" name="dateAfter"
					value="<%=dateAfter%>" title="">00:00
			</div>
			<div style="display: inline;">
				Date Before:<input type="text" id="dateBefore" name="dateBefore"
					value="<%=dateBefore%>" title="">00:00
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
				ASIN:<input type="text" name="asin" value="<%=asin%>" title="">
			</div>
			<div style="display: inline;">
				<input type="hidden" id="sortedColumnId" name="sortedColumnId"
					value="<%=sortedColumnId%>"> <input type="hidden"
					id="sortOrder" name="sortOrder" value="<%=sortOrder%>"> <input
					type="submit" value="Search" title="">
			</div>
		</form>
	</div>

	<%--Search result--%>
	<%
		if (isQuery) {
			if (productList == null) {
				out.print("<font color=red>oh no, something is wrong while searching</font>");
			} else if (productList.size() < 1) {
				out.print("There are no any result.");
			} else {
	%>
	<div id="resultDiv" class="table-c">
		<table>
			<tr align="center">
				<th width="2%"></th>
				<th width="8%"><a href="javascript:void(0)"
					onclick="reorder('1')">Product ID<%=OrderBy.getHtmlArrow("1", sortedColumnId, orderByMap, sortOrder)%></a></th>
				<th width="20%"><a href="javascript:void(0)"
					onclick="reorder('2')">Name<%=OrderBy.getHtmlArrow("2", sortedColumnId, orderByMap, sortOrder)%></a></th>
				<th width="30%">Description</th>
				<th width="10%"><a href="javascript:void(0)"
					onclick="reorder('3')">Create Time<%=OrderBy.getHtmlArrow("3", sortedColumnId, orderByMap, sortOrder)%></a></th>
				<th width="2%"><a href="javascript:void(0)"
					onclick="reorder('4')">Status<%=OrderBy.getHtmlArrow("4", sortedColumnId, orderByMap, sortOrder)%></a></th>
				<th width="8%"><a href="javascript:void(0)"
					onclick="reorder('5')">ASIN<%=OrderBy.getHtmlArrow("5", sortedColumnId, orderByMap, sortOrder)%></a></th>
				<th width="10%">Supply</th>
				<th width="10%">Transaction</th>
			</tr>
			<%
				int rows = 0;
						for (InternalProduct product : productList) {
			%>
			<%--define tr class to data to active mouseover and mouseout events--%>
			<tr class="data">
				<td><%=++rows%></td>

				<td><a href="EditProduct.jsp?id=<%=product.getProductId()%>"
					title="click to edit this product" target="_blank"><%=product.getProductId()%></a></td>
				<td><%=Filter.nullFilter(product.getName())%></td>
				<td><%=Filter.nullFilter(product.getDescription())%></td>
				<td><%=product.getCreateTime()%></td>
				<td><%=Filter.nullFilter(product.getStatus())%></td>
				<td><%=Filter.nullFilter(product.getAsin())%></td>
				<td>
					<%--define id and .class.class for jQuery function--%>
					<button id="<%=product.getProductId()%>"
						title="click to count supply" onclick="countSupply(this)">count</button>
					<a class="supply  <%=product.getProductId()%>"
					href="../supply/SupplyManagement.jsp?productId=<%=product.getProductId()%>"
					target="_blank" title="click to view supply detail">view</a>
				</td>
				<td>
					<%--define id and .class.class for jQuery function--%>
					<button id="<%=product.getProductId()%>"
						title="click to count transaction"
						onclick="countTransaction(this)">count</button> <a
					class="transaction  <%=product.getProductId()%>"
					href="../supply/SupplyTransactionManagement.jsp?productId=<%=product.getProductId()%>"
					target="_blank" title="click to view transaction detail">view</a>
				</td>

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