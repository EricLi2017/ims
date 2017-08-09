<%@ page import="java.util.List"%>
<%@ page import="amazon.mws.order.ListOrderAndOrderItemsManager"%>
<%--
  Created by IntelliJ IDEA.
  User: Eric Li
  Date: 3/2/2017
  Time: 3:58 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%--Permission Check--%>
<%@ include file="/include/jsp/PermissionCheck.jsp"%>
<%--Get request date in PST time zone--%>
<%@ include file="/include/jsp/GetRequestDateInPST.jsp"%>
<%
	//Call mws for order and order items and insert them into ims database
	List<ListOrderAndOrderItemsManager.InsertResult> insertResults = new ListOrderAndOrderItemsManager()
			.insertOrderAndOrderItemsFromMWS(createdAfter, createdBefore);
	System.out.println(insertResults);
	int rows = (insertResults == null ? 0 : insertResults.size());

	//response for page content
	out.print("insert " + rows + " orders and related order items:");
	out.print("<br>");
	if (insertResults != null) {
		out.print("orderId");
		out.print("\t");
		out.print("order insert");
		out.print("\t");
		out.print("order items insert");
		for (ListOrderAndOrderItemsManager.InsertResult insertResult : insertResults) {
			out.print("<br>");
			out.print(insertResult.getAmazonOrderId());
			out.print("\t");
			out.print(insertResult.getInsertOrderSuccess());
			out.print("\t");
			out.print(insertResult.getInsertOrderItemsSuccess());
		}
		out.print("<br>");
	}
%>
