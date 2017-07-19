<%@ page import="amazon.mws.order.ListOrdersManager"%>
<%--
  Created by IntelliJ IDEA.
  User: Eric Li
  Date: 3/3/2017
  Time: 11:50 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%--Permission Check--%>
<%@ include file="/include/PermissionCheck.jsp"%>
<%--Get request date in PST time zone--%>
<%@ include file="/include/GetRequestDateInPST.jsp"%>
<%
	//Call MWS to count order and sum items in order
	ListOrdersManager.OrderSumMWS orderSumMWS = new ListOrdersManager().getSumFromMWS(createdAfter,
			createdBefore);
	//todo
	System.out.println(orderSumMWS);
	out.print(orderSumMWS);
%>
