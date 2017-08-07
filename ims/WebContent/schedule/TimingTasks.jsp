<%@page import="amazon.mws.order.ListOrdersTimerTask"%>
<%@page import="servlet.ScheduleServlet"%>
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
					<th>Shortest restore cycle</th>
					<th>Full restore cycle</th>
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
				<tr>
					<td>GetOrder</td>
					<td>six</td>
					<td>one request every minute</td>
					<td>N/A</td>
					<td>N/A</td>
					<td>AmazonOrderId Maximum: 50</td>
					<td>every minute: 50 orders</td>
					<td>every 6 minutes or above: 300 orders</td>
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
					<td>GetMatchingProduct</td>
					<td>20</td>
					<td>Two items every second</td>
					<td>N/A</td>
					<td>7200</td>
					<td>ASINList Maximum: 10 ASIN</td>
					<td>every second: 2 ASIN</td>
					<td>every 100 second or above: 200 ASIN</td>
				</tr>
			</table>
		</div>

		<div id="timingTasks">
			<h2>Timer Tasks</h2>
			<table>
				<tr>
					<th title="">Timer Task</th>
					<th title="">MWS API</th>
					<th>Initial Delay</th>
					<th>Delay</th>
					<th>Scope</th>
					<th>Setting Requirement</th>

				</tr>
				<tr>
					<td>List Orders</td>
					<td>ListOrders and ListOrdersByNextToken</td>
					<td><%=ScheduleServlet.INITIAL_DELAY_LIST_ORDERS%> <%=ScheduleServlet.TIME_UNIT%></td>
					<td><%=ScheduleServlet.DELAY_LIST_ORDERS%> <%=ScheduleServlet.TIME_UNIT%></td>
					<td>list_order_track (first from <%=ListOrdersTimerTask.DEFAULT_FIRST_CREATED_AFTER_FROM_NOW_HOUR%>
						hours before current executive time)
					</td>
					<td>Get createdAfter from list_order_track, use it call MWS
						and continuously call getByNextToken until get all result or
						exception happened (but will recall for 503 throttling exception)
						to end task for this time</td>

				</tr>
				<tr>
					<td>List Order Items</td>
					<td>ListOrderItems and ListOrderItemsByNextToken</td>
					<td><%=ScheduleServlet.INITIAL_DELAY_LIST_ORDER_ITEMS%> <%=ScheduleServlet.TIME_UNIT%></td>
					<td><%=ScheduleServlet.DELAY_LIST_ORDER_ITEMS%> <%=ScheduleServlet.TIME_UNIT%></td>
					<td>oldest 30 orders that has no items and is not pending</td>
					<td>: If 30 call(&lt;=30*1 order items) complete or exception
						happened, then task end for this time</td>

				</tr>
				<tr>
					<td>Get Order</td>
					<td>GetOrder</td>
					<td><%=ScheduleServlet.INITIAL_DELAY_GET_ORDER%> <%=ScheduleServlet.TIME_UNIT%></td>
					<td><%=ScheduleServlet.DELAY_GET_ORDER%> <%=ScheduleServlet.TIME_UNIT%></td>
					<td>oldest 300 pending orders</td>
					<td>: If 6 call(&lt;=6*50 orders) complete or exception
						happened, then task end for this time</td>

				</tr>
				<tr>
					<td>List Inventory</td>
					<td>ListInventorySupply and ListInventorySupplyByNextToken</td>
					<td><%=ScheduleServlet.INITIAL_DELAY_LIST_INVENTORY_SUPPLY%> <%=ScheduleServlet.TIME_UNIT%></td>
					<td><%=ScheduleServlet.DELAY_LIST_INVENTORY_SUPPLY%> <%=ScheduleServlet.TIME_UNIT%></td>
					<td>all products in IMS</td>
					<td>Total SKUs &lt;=30*50: Run until all SKU call once or
						total call times reach 30(&lt;=30*50 SKUs), then task end for this
						time</td>

				</tr>
				<tr>
					<td>Get Matching Product</td>
					<td>GetMatchingProduct</td>
					<td><%=ScheduleServlet.INITIAL_DELAY_GET_MATCHING_PRODUCT%> <%=ScheduleServlet.TIME_UNIT%></td>
					<td><%=ScheduleServlet.DELAY_GET_MATCHING_PRODUCT%> <%=ScheduleServlet.TIME_UNIT%></td>
					<td>all products in IMS</td>
					<td>: If all ASINs call(after 20 ASINs call(&lt;=20*10 ) will
						sleep a full restore cycle) complete or exception happened, then
						task end for this time</td>

				</tr>
			</table>
		</div>
	</div>


	<%--Page Footer --%>
	<%@ include file="/include/page/Footer.jsp"%>
</body>
</html>