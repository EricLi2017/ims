<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%--Permission Check--%>
<%@ include file="/include/PermissionCheck.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Timing Tasks</title>
<%--page style--%>
<link rel="stylesheet" href="../include/css/page.css">
</head>
<body>
	<%--Page Navigation Menu --%>
	<%@ include file="/include/page/Menu.jsp"%>

	<%--Page Main Content --%>
	<div class="content">
		<div id="throttling">
			<h2>MWS API Throttling Values</h2>
			<table>
				<tr>
					<th title="">MWS API</th>
					<th
						title="The number of requests that you can submit at one time without throttling. The request quota decreases with each request you submit, and increases at the restore rate. Requests are calculated for each Amazon seller account and Amazon MWS developer account pair.">Request
						quota</th>
					<th
						title="(also called the recovery rate) The rate at which your request quota increases over time, up to the maximum request quota.">Restore
						rate</th>
					<th
						title="(also called the burst rate) The maximum size that the request quota can reach.">Maximum
						request quota</th>
					<th title="The maximum number of requests you can submit per hour.">Hourly
						request quota</th>
					<th>Other</th>
					<th>Shortest cycle</th>
					<th>Longest cycle</th>
				</tr>
				<tr>
					<td>ListInventorySupply and ListInventorySupplyByNextToken</td>
					<td>30</td>
					<td>two requests every second</td>
					<td>N/A</td>
					<td>N/A</td>
					<td>SellerSkus Maximum: 50 SKUs</td>
					<td>every second: 100 SKUs</td>
					<td>every 15 seconds or above: 1500 SKUs</td>
				</tr>
				<tr>
					<td>ListOrders and ListOrdersByNextToken</td>
					<td>six</td>
					<td>one request every minute</td>
					<td>N/A</td>
					<td>N/A</td>
					<td>MaxResultsPerPage 1-100</td>
					<td>every minute: 100 orders</td>
					<td>every 6 minutes or above: 600 orders</td>
				</tr>
				<tr>
					<td>ListOrderItems and ListOrderItemsByNextToken</td>
					<td>30</td>
					<td>one request every two seconds</td>
					<td>N/A</td>
					<td>N/A</td>
					<td></td>
					<td>every 2 seconds: 1 order item</td>
					<td>every 60 seconds or above: 30 order items</td>
				</tr>
			</table>
		</div>

		<div id="timingTasks">
			<h2>Timing Tasks</h2>
			<table>
				<tr>
					<th title="">Timing Task</th>
					<th title="">MWS API</th>
					<th>Schedule</th>
					<th>Scope</th>
					<th>Setting Requirement</th>

				</tr>
				<tr>
					<td>Update FBA Inventory</td>
					<td>ListInventorySupply and ListInventorySupplyByNextToken</td>
					<td>one time every one hour</td>
					<td>all products in IMS</td>
					<td>Total SKUs less than 3600*100: After first 1500 SKUs, get
						next 100 every one second</td>

				</tr>
				<tr>
					<td>Update Orders</td>
					<td>ListOrders and ListOrdersByNextToken</td>
					<td>one time every one hour</td>
					<td>from 12 hours before</td>
					<td>Orders in 12 hours less than 60*100: After first 600
						orders, get next 100 every one minute</td>

				</tr>
				<tr>
					<td>Update Order Items</td>
					<td>ListOrderItems and ListOrderItemsByNextToken</td>
					<td>one time every one hour</td>
					<td>oldest 30 orders that has no items</td>
					<td>: After first 30 order items, get next one every two
						seconds</td>

				</tr>
			</table>
		</div>
	</div>


	<%--Page Footer --%>
	<%@ include file="/include/page/Footer.jsp"%>
</body>
</html>