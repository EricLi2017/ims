<%--
  Created by IntelliJ IDEA.
  User: Eric Li
  Date: 3/2/2017
  Time: 3:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%--Permission Check--%>
<%@ include file="/include/jsp/PermissionCheck.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Update and Check Orders Page</title>
<%--jquery lib--%>
<script type="text/javascript"
	src="../../include/js/jquery/jquery-3.1.1.min.js"></script>
<%--jquery ui for datepicker function--%>
<script type="text/javascript"
	src="../../include/js/jquery-ui-1.12.1/jquery-ui.js"></script>
<%--jquery ui for datepicker style--%>
<link rel="stylesheet"
	href="../../include/js/jquery-ui-1.12.1/themes/redmond/jquery-ui.css">
<%--special function--%>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						/*set date*/
						var dA = $("#dateAfter");
						var dB = $("#dateBefore");
						dA.datepicker({
							dateFormat : "yy-mm-dd"
						});
						dB.datepicker({
							dateFormat : "yy-mm-dd"
						});
						/*validate date*/
						function valid() {
							if (dA.val() == "") {
								window.alert('Please input correct date!');
								dA.focus();
								return false;
							}
							if (dB.val() == "") {
								window.alert('Please input correct date!');
								dB.focus();
								return false;
							}
							return true;
						}

						/*update orders from MWS to IMS, async call*/
						$("#updateBtn")
								.click(
										function() {
											//validate date
											if (!valid())
												return false;
											//action:update orders from MWS to IMS
											var obj = $
													.ajax({
														url : "../service/AddOrdersAndOrderItemsService.jsp",
														async : true,
														type : "POST",
														data : $("#dateForm")
																.serialize(),
														beforeSend : function() {
															$(":input").attr(
																	"disabled",
																	true);//disable all input elements
															$("#updateDiv")
																	.html(
																			"Updating orders and order items from MWS...This may will take several minutes, please wait.");
														},
														success : function() {
															$("#updateDiv")
																	.html(
																			obj.responseText);
														},
														error : function(
																XMLHttpRequest,
																textStatus,
																errorThrown) {
															var errMsg = "Oh no, something is wrong! "
																	+ XMLHttpRequest.readyState
																	+ " : "
																	+ XMLHttpRequest.status
																	+ " : "
																	+ XMLHttpRequest.statusText
																	+ " : "
																	+ textStatus
																	+ " : "
																	+ errorThrown;
															$("#updateDiv")
																	.html(
																			errMsg);
														},
														complete : function() {
															$(":input")
																	.removeAttr(
																			"disabled");//active all input elements
														}
													});

										});//end of $("#updateBtn").click()

						/*check orders between MWS and IMS, async call: check MWS orders, check IMS orders*/
						$("#checkBtn")
								.click(
										function() {
											//validate date
											if (!valid())
												return false;
											//get params: if disabled attr is true,the params will be null
											var params = $("#dateForm")
													.serialize();
											//action: check ims orders
											var imsObj = $
													.ajax({
														url : "../service/CheckOrderOfIMSService.jsp",
														async : true,
														type : "POST",
														data : params,
														beforeSend : function() {
															$(":input").attr(
																	"disabled",
																	true);//disable all input elements
															$("#imsDiv")
																	.html(
																			"Checking orders and order items from IMS...This may will take several minutes, please wait.");
															$("#imsDiv")
																	.css(
																			{
																				"-webkit-animation" : "twinkling 1s infinite ease-in-out"
																			});//set animation:twinkling
														},
														success : function() {
															$("#imsDiv")
																	.html(
																			imsObj.responseText);
														},
														error : function(
																XMLHttpRequest,
																textStatus,
																errorThrown) {
															var errMsg = "Oh no, something is wrong! "
																	+ XMLHttpRequest.readyState
																	+ " : "
																	+ XMLHttpRequest.status
																	+ " : "
																	+ XMLHttpRequest.statusText
																	+ " : "
																	+ textStatus
																	+ " : "
																	+ errorThrown;
															$("#imsDiv").html(
																	errMsg);
														},
														complete : function() {
															$(":input")
																	.removeAttr(
																			"disabled");//active all input elements
															$("#imsDiv")
																	.css(
																			{
																				"-webkit-animation" : ""
																			});//remove animation
														}
													});//end of imsObj
											//action: check mws orders
											var mwsObj = $
													.ajax({
														url : "../service/CheckOrderOfMWSService.jsp",
														async : true,
														type : "POST",
														data : params,
														beforeSend : function() {
															$(":input").attr(
																	"disabled",
																	true);//disable all input elements
															$("#mwsDiv")
																	.html(
																			"Checking orders from MWS...This may will take several minutes, please wait.");
														},
														success : function() {
															$("#mwsDiv")
																	.html(
																			mwsObj.responseText);
														},
														error : function(
																XMLHttpRequest,
																textStatus,
																errorThrown) {
															var errMsg = "Oh no, something is wrong! "
																	+ XMLHttpRequest.readyState
																	+ " : "
																	+ XMLHttpRequest.status
																	+ " : "
																	+ XMLHttpRequest.statusText
																	+ " : "
																	+ textStatus
																	+ " : "
																	+ errorThrown;
															$("#mwsDiv").html(
																	errMsg);
														},
														complete : function() {
															$(":input")
																	.removeAttr(
																			"disabled");//active all input elements
														}
													});//end of mwsObj
										});//end of $("#checkBtn").click()

					});//end of $(document).ready()
</script>
<style type="text/css">
@
-webkit-keyframes twinkling { 0% {
	opacity: 0;
}
100%
{
opacity


:


1;
}
}
</style>

</head>
<body>
	<h2 align="center">Update and Check Orders</h2>
	<form name="dateForm" id="dateForm">
		<table align="center">
			<tr>
				<td>Date From:</td>
				<td><input type="text" id="dateAfter" name="dateAfter" value=""
					title=""></td>
				<td>Date To:</td>
				<td><input type="text" id="dateBefore" name="dateBefore"
					value="" title=""></td>
				<td><input id="updateBtn" type="button"
					value="Add Orders and OrderItems to IMS from MWS" title=""></td>
				<td><input id="checkBtn" type="button"
					value="Check Orders between MWS and IMS" title=""></td>
			</tr>
		</table>
	</form>
	<%--update result--%>
	<div id="updateDiv"></div>

	<p></p>

	<%--check result--%>
	<div id="checkDiv">
		<div id="imsDiv"></div>
		<div id="mwsDiv"></div>
	</div>

</body>
</html>
