<%@page import="amazon.mws.fulfillment.MessageForUpdateInventory"%>
<%@ page import="amazon.mws.fulfillment.ListInventoryManager"%>
<%@ page import="amazon.mws.fulfillment.ObserverForUpdateInventory"%><%--
  Created by IntelliJ IDEA.
  User: Eric Li
  Date: 2/28/2017
  Time: 12:09 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/include/jsp/PermissionCheck.jsp"%>
<%
	//construct ListInventoryManager and add observers
	ListInventoryManager listInventoryManager = new ListInventoryManager();
	ObserverForUpdateInventory observerForUpdateInventory = new ObserverForUpdateInventory();
	listInventoryManager.addObserver(observerForUpdateInventory);

	//clear all the messages and ready to receive/add message
	MessageForUpdateInventory.getInstance().startMessage();
	//update FBA inventory and create messages
	int rows = listInventoryManager.updateAll();
	//clear all the messages
	MessageForUpdateInventory.getInstance().clearMessage();

	out.print(rows);
	out.print(" sku are updated for FBA inventory.");
%>