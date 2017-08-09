<%@ page import="amazon.db.query.OrderMatch"%>
<%--
  Created by IntelliJ IDEA.
  User: Eric Li
  Date: 3/3/2017
  Time: 11:49 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%--Permission Check--%>
<%@ include file="/include/jsp/PermissionCheck.jsp"%>
<%--Get request date in PST time zone--%>
<%@ include file="/include/jsp/GetRequestDateInPST.jsp"%>
<%
	//check ims sum: count() in orders,sum(sku units) in orders,sum(sku units) in order_items
	OrderMatch.OrderMatchResult orderMatchResult = new OrderMatch().check(createdAfter, createdBefore);
	//todo
	System.out.println(orderMatchResult);
	out.print(orderMatchResult);
%>
