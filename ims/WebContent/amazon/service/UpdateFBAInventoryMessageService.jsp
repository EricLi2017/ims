<%@ page import="amazon.mws.fulfillment.MessageForUpdateInventory"%>
<%@ page import="java.util.List"%>
<%--
  Created by IntelliJ IDEA.
  User: Eric Li
  Date: 3/1/2017
  Time: 9:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/include/jsp/PermissionCheck.jsp"%>
<%
    //get current Messages
    List<String> messages = MessageForUpdateInventory.getInstance().getMessage();
    if (messages == null) return;
    for (String message : messages) {
        out.print(message);
        out.print("<br>");
    }

    //clear Message after use while Message is complete
    if (MessageForUpdateInventory.getInstance().isComplete()) {
        MessageForUpdateInventory.getInstance().clearMessage();
    }
%>