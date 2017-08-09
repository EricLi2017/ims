<%@ page import="amazon.db.query.SKUMatch"%>
<%--
  Created by IntelliJ IDEA.
  User: Eric Li
  Date: 2/27/2017
  Time: 12:58 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/include/jsp/PermissionCheck.jsp"%>
<%
	//count sku that exists in orders but not exists in products
	int skuCountNotInProducts = new SKUMatch().getSkuCountNotInProducts();
	System.out.println("There are " + skuCountNotInProducts
			+ " sku that not exists in product records but exists in order records.");
	out.print("There are " + skuCountNotInProducts
			+ " sku that not exists in product records but exists in order records.");
	//need to throw exception while the result is less than 0
	//the front page can indicate normal result or error by this exception
	if (skuCountNotInProducts < 0) {
		throw new Exception("error while query database");
	}

	//    List<String> skus = new SKUMatch().getSkuNotInProducts();
	//    System.out.println(skus + "are the sku that exists in order records but not exists in product records.");
	//    out.println(skus + "are the sku that exists in order records but not exists in product records.");
	//    out.print("<br>");
%>
