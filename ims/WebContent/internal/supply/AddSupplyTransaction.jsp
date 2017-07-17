<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%--Permission Check--%>
<%@ include file="/include/PermissionCheck.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Add Supply Transaction</title>

<%--jquery lib--%>
<script type="text/javascript"
	src="../../include/js/jquery/jquery-3.1.1.min.js"></script>
<%--jquery ui for datepicker function--%>
<script type="text/javascript"
	src="../../include/js/jquery-ui-1.12.1/jquery-ui.js"></script>
<%--jquery ui for datepicker style--%>
<link rel="stylesheet"
	href="../../include/js/jquery-ui-1.12.1/themes/redmond/jquery-ui.css">
<%--use the css style for input area --%>
<link type="text/css" rel="stylesheet"
	href="../../include/css/input.css">

<%--special only for this page--%>
<script type="text/javascript">
	$(document).ready(function() {
		/*set date*/
		$("#dateInput").datepicker({
			dateFormat : "yy-mm-dd"
		});

	});//end of $(document).ready()
	/*click add button*/
	function addTr(obj) {
		var tr = $(obj).parent().parent();
		tr.after(tr.clone());
	}
	/*click remove button*/
	function delTr(obj) {
		//must keep at least one data line
		var rows = $("table#arrayTable tr").length;
		if (rows == 2) {
			alert("At least one line must be kept!");
			return false;
		}
		//remove 
		$(obj).parent().parent().remove();
	}
</script>
</head>
<body>
	<h1 style="text-align: center">Add Supply Transaction</h1>

	<%--input data--%>
	<div>
		<form method="post"
			action="../service/AddSupplyTransactionService.jsp">
			<table align="center" width="100%" id="inputTable">
				<tr>
					<!-- 				left part : array parameter area -->
					<td width="50%">
						<table align="center" width="100%" id="arrayTable">
							<tr>
								<th class="required">Supply ID</th>
								<th class="required">Quantity</th>
								<th class="required">Unit Price</th>
								<th>Price Description</th>
								<th class="required">Status</th>
								<th></th>
							</tr>
							<tr>
								<td><input type="text" name="supplyId" title=""
									style="width: 100%"></td>
								<td><input type="text" name="quantity" title=""
									style="width: 100%"></td>
								<td><input type="text" name="unitPrice" title=""
									style="width: 100%"></td>
								<td><input type="text" name="priceDescription" title=""
									style="width: 100%"></td>
								<td><select name="status">
										<option value="Paid" selected>Paid</option>
										<option value="Unpaid">Unpaid</option>
										<option value="Stopped">Stopped</option>
										<option value="Received">Received</option>
								</select></td>
								<td><input type="button" title="add a row"
									onclick="addTr(this)" value="+"> <input type="button"
									title="remove this row" onclick="delTr(this)" value="-">
									<!-- 									if use link, there are two method like below -->
									<!-- 									<a title="add a row" onclick="addTr(this)">+</a>  -->
									<!-- 									<a href="javascript:;" title="remove this row"onclick="delTr(this)">-</a> -->
								</td>
							</tr>
						</table>
					</td>
					<!-- 				right part : single parameter area -->
					<td>
						<table align="center" width="100%">
							<tr>
								<th class="required">Product Price</th>
								<th class="required">Shipped Fee</th>
								<th class="required">Time</th>
								<th class="required">Operator</th>
								<th>Transaction Description</th>
							</tr>
							<tr>
								<td><input type="text" name="productPrice" title=""
									style="width: 100%"></td>
								<td><input type="text" name="shippedFee" title=""
									style="width: 100%"></td>
								<td><input type="text" id="dateInput" name="time" title=""
									style="width: 100%"></td>
								<td><input type="text" name="operator" value="<%=user%>"
									title="" style="width: 100%"></td>
								<td><input type="text" name="transactionDescription"
									value="" title="" style="width: 100%"></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td><input type="submit" value="Submit"></td>
				</tr>
			</table>
		</form>
	</div>

</body>
</html>